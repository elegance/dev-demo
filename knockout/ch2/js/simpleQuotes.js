/**
 * 
 * 1. 支持实时性行情依赖、一次性取行情依赖
 * 2. 每个股票代码行情建立计数方式回收，单个股票只会订阅一次，当依赖减少为0时自动退订
 * 3. 一次性计算数据，只取一次，自动退订，节省带宽与服务器推送压力
 * 4. 依赖行情实时计算的数据，行情变化时，关联变化精确到具体的dom，比如某个td
 * 
 * @required socket.io
 * @required lodash.js
 * @required knockout.js
 * @require jquery.js [Deferred, $.noop]
 * @require Tzb.js [NumberExt.fixed]
 */
(function (global) {
    var socket = io.connect('http://120.55.88.227/socket.io/simpleStocks');

    global.simpleQuote = new SimpleQuote();
    socket.on('message', global.simpleQuote.onMessage);
    socket.on('batchMessage', global.simpleQuote.onBatchMessage);

    function SimpleQuote() {
        var that = this;

        this.nsps = {};
        this.nspsCb = {};
        this.refs = {};

        // 所有行情信息
        this.quotesInfo = {};

        // 获取某个股票的行情(gt 方法不可暴露给外部直接使用, 因为gt方法不保证获取所有代码的行情，需要先promise一些股票代码，再返回gt方法供使用)
        var gt = function (code) {
            if (code.indexOf('.') < 0) {
                code = that.wrapCode(code);
            }
            var r = that.quotesInfo[code];
            if (isNaN(r)) {
                r = 0;
                console.error(code, '请确保使用[gt]方法前，你已经订阅了你所需的股票代码。 或者没有该股票代码的行情价，可能是数据问题，要与C端行情或者C端数据生成逻辑沟通。(现有处理返回0)')
            }
            return r;
        };

        this.wrapCode = function (code) {
            return code + '.' + (code.startsWith('6') ? 'SH' : 'SZ');
        };

        // 同一个namespace 只订阅一组股票代码
        this.subcribe = function (nsp, codes, cb) {
            var codes = _(codes).uniq().value(),
                newSubs = [], unSubs = [],
                needSubs = [], needUnsubs = [];

            that.nspsCb[nsp] = cb;

            if (!that.nsps[nsp]) {
                that.nsps[nsp] = [];
            }

            newSubs = _(codes).difference(that.nsps[nsp]).value();
            unSubs = _(that.nsps[nsp]).difference(codes).value();
            that.nsps[nsp] = codes;

            newSubs.forEach(function (code) {
                that.refs[code] = (that.refs[code] || (needSubs.push(code) && 0)) + 1;
            });

            unSubs.forEach(function (code) {
                that.refs[code] = (that.refs[code] > 1) ? (that.refs[code] - 1) : (needUnsubs.push(code) && 0);
            });

            if (needSubs.length > 0) {
                socket.emit('appendMessage', needSubs.join(','));
            }
            if (needUnsubs.length > 0) {
                socket.emit('unsubcribe', needUnsubs.join(','));
            }
        };

        // 批量返回的行情
        this.onBatchMessage = function (msg) {
            msg.split('|').forEach(function (pairStr) {
                var pair = pairStr.split(',');
                that.quotesInfo[pair[0]] = pair[1];
            });
        };

        //单个推送的行情消息
        this.onMessage = function (msgPair) {
            var pair = msgPair.split(',');
            that.quotesInfo[pair[0]] = pair[1];

            for (var nsp in that.nsps) {
                if (that.nsps[nsp].indexOf(pair[0]) >= 0) {
                    that.nspsCb[nsp](pair);
                }
            }
        };

        /**
         * 生成集合数据ko使用的viewmode，用于集合数据依赖行情实时变化
         * @param {String} nsp 命名空间，注意保持命名空间的唯一，如果命名空间相同将会覆盖之前订阅的数据
         * @param {Array} rows 数据集合
         * @param {Function} getDepCodesFun 从data中取股票代码(集合)的方法（因为不同的数据集合中的股票代码名称属性可能不一样，有的叫'code'、'codes'，还有单行依赖多个股票的，需要从集合或字符串分隔取得的）
         * @param {Array} 需要新增的属性名以及属性名依赖行情的计算方法的集合
         */
        this.genRowsViewModel = function (nsp, rows, getDepCodesFun, computeInfoArr) {
            var subCodes = [];

            // 获取 这个viewModel需要依赖的股票代码
            rows.forEach(function (item) {
                var rowDepCodes = getDepCodesFun(item);
                rowDepCodes = (typeof rowDepCodes === 'string' ? rowDepCodes.split(',') : rowDepCodes);
                subCodes = _(subCodes).union(rowDepCodes).value(); //合并、去重
                item['_depCodes'] = rowDepCodes; //为数据行添加上算得的依赖代码
            });
            that.subcribe(nsp, subCodes, $.noop);
            return promiseGenKoRowsViewModel(nsp, rows, subCodes, computeInfoArr);
        };

        /**
         * 生成单行数据ko使用的viewmode，用于单行非集合数据依赖行情实时变化
         * @param {String} nsp 命名空间，注意保持命名空间的唯一，如果命名空间相同将会覆盖之前订阅的数据
         * @param {Object} rowData 行数据对象
         * @param {Function} getDepCodesFun  从data中取股票代码(集合)的方法（因为不同的数据集合中的股票代码名称属性可能不一样，有的叫'code'、'codes'，还有单行依赖多个股票的，需要从集合或字符串分隔取得的）
         * @param {Array} 需要新增的属性名以及属性名依赖行情的计算方法的集合
         */
        this.genSingleViewModel = function (nsp, rowData, getDepCodesFun, computeInfoArr) {
            var subCodes = getDepCodesFun(rowData);

            subCodes = (typeof subCodes === 'string' ? subCodes.split(',') : subCodes);
            subCodes = _(subCodes).uniq().value();
            that.subcribe(nsp, subCodes, $.noop);
            return promiseGenKoSingleViewModel(nsp, rowData, subCodes, computeInfoArr);
        };

        /**
         * 确保能指定的股票代码(集合)行情，返回gt取相应代码行情的方法
         * @param {String} nsp 命名空间，注意保持命名空间的唯一，如果命名空间相同将会覆盖之前订阅的数据
         * @param {Array} codes  订阅的股票代码的数组集合
         * @param {Boolean} isOnce 是否只需要一次行情，为真时将只取一次这些股票代码的行情，主要用于依赖行情的一次性计算，取消这些订阅，减少带宽、服务器推送的压力
         */
        this.promiseCodesQuote = function (nsp, codes, isOnce) {
            codes = _(codes).compact().uniq().value();
            that.subcribe(nsp, codes, $.noop);

            var dfd = $.Deferred(),
                len = codes ? codes.length : 0,
                idx = -1,
                cnt = 0,
                sid = setInterval(function () {
                    idx = -1;
                    cnt++;
                    while (++idx <= len) {
                        if (len !== 0 && typeof that.quotesInfo[codes[idx]] === 'undefined') {
                            if (cnt === 20) { // 0.2s中内未获取到，控制台打印错误
                                console.error(codes[idx], 'TODO: 无法获取该股票代码的行情价， 可能是数据问题，要与C端行情或者C端数据生成逻辑沟通。')
                                clearInterval(sid);
                                dfd.reject(codes[idx] + '无法获取该股票代码的行情。');
                            }
                            break;
                        }
                        if (len === 0 || idx + 1 === len) {
                            clearInterval(sid);
                            if (isOnce) {
                                 that.subcribe(nsp, [], $.noop);
                            }
                            dfd.resolve(gt);
                        }
                    }
                }, 10);
            return dfd.promise();
        };

         /**
         * 根据行情填充数据对象
         * @param {String} nsp 命名空间，注意保持命名空间的唯一，如果命名空间相同将会覆盖之前订阅的数据
         * @param {Object} data 集合数据或者单个数据对象均可
         * @param {Function} getCodesFun  从data中取股票代码(集合)的方法（因为不同的数据集合中的股票代码名称属性可能不一样，有的叫'code'、'codes'，还有单行依赖多个股票的，需要从集合或字符串分隔取得的）
         * @param {Array} computeInfoArr 计算信息数组，需要新增的属性以及属性计算的方法集合
         */
        this.promiseFillQuoteData = function (nsp, data, getCodesFun, computeInfoArr) {
            var codes = [],
                tmpDatas = Array.isArray(data) ? data : [data];

            data.forEach(function (row) {
                codes = codes.concat(getCodesFun(row));
            });

            return that.promiseCodesQuote(nsp, codes, true).then(function (gt) {
                tmpDatas.forEach(function (row) {
                    computeInfoArr.forEach(function (cptInfo) {
                        row[cptInfo.attr] = cptInfo.computed(gt, row);
                    });
                });
            });
        };

        function promiseGenKoRowsViewModel(nsp, data, codes, computeInfoArr) {
            return promiseGenKoViewModel(1, nsp, data, codes, computeInfoArr);
        }

        function promiseGenKoSingleViewModel(nsp, data, codes, computeInfoArr) {
            return promiseGenKoViewModel(2, nsp, data, codes, computeInfoArr);
        }

        function promiseGenKoViewModel(type, nsp, data, codes, computeInfoArr) {
            return that.promiseCodesQuote(nsp, codes).then(function (gt) {
                var rsViewModel;
                if (type === 1) {
                    rsViewModel = new RowsViewModel(data, codes, computeInfoArr, gt);
                } else {
                    rsViewModel = new Row(data, computeInfoArr, gt);
                }
                if (codes && codes.length !== 0) {
                    that.nspsCb[nsp] = function (quotePair) {
                        rsViewModel.processQuote({
                            code: quotePair[0],
                            price: quotePair[1]
                        });
                    };
                }
                return rsViewModel;
            });
        }
    }

    function RowsViewModel(datas, depCodes, computeInfoArr, gt) {
        var self = this;

        self._depCodes = depCodes;
        self.data = ko.observableArray();

        // init
        datas.forEach(function (item) {
            var row = new Row(item, computeInfoArr, gt);
            self.data.push(row);
        });

        // process Quote
        self.processQuote = function (quote) {
            if (self.isDepTheQuote(quote)) {
                self.data().forEach(function (row) {
                    if (row.isDepTheQuote(quote)) {
                        row.processQuote(quote);
                    }
                });
            }
        };

        // view mode 是否依赖这个行情
        self.isDepTheQuote = function (quote) {
            return self._depCodes.indexOf(quote.code) > -1;
        };

    }

    function Row(row, computeInfoArr, gt) {
        var self = this;

        // 作为computed的依赖
        self._num = ko.observable(0);
        self._depCodes = row._depCodes;

        for (var a in row) {
            if (!a.startsWith('_')) {
                self[a] = row[a];
            }
        }

        // 计算表达式
        computeInfoArr.forEach(function (cptInfo) {
            self[cptInfo.attr] = ko.computed(function () {
                return +cptInfo.computed(gt, self) + (self._num() - self._num());
            });
        });

        // 更新依赖触发 computed
        self.processQuote = function (quote) {
            self._num(new Date().getTime()); //只需要改变数值触发改变即可
        };

        // 判断是否依赖某个行情
        self.isDepTheQuote = function (quote) {
            return self._depCodes.indexOf(quote.code) >= 0;
        };

    }

    // ko使用的 数据格式化方法
    window.Fmt = window.Fmt || {};
    Fmt.dateTimeFmt = function (dateTime) {
        if (dateTime && dateTime.length > 10) {
            return dateTime.slice(2);
        }
        return dateTime;
    };

    Fmt.dateFmt = function (dateTime) {
        if (dateTime && dateTime.length > 8) {
            return dateTime.slice(2);
        }
        return dateTime;
    };

    // 默认四舍五入
    Fmt.priceFmt = function (num, precision, type, whenIntegerPad) {
        //precision = (typeof precision == 'undefined') ? 3 : precision; // 默认保留3位数
        type = type || 'round'; // ko默认使用四舍五入的方式
        whenIntegerPad = typeof whenIntegerPad !== 'undefined' ? whenIntegerPad : true;
        return Tzb.NumberExt.fixed(num, precision, type, whenIntegerPad);
    };

    // 根据股票代码获取名称
    Fmt.gtName = function (code) {
        return Tzb.stock.gtName(code);
    };
})(window);