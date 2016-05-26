$(function() {
	// 股票行情model
	function QuotesViewModel() {
		var self = this;
		self.rows = ko.observableArray();

		var rowLookup = {};

		// 更新行情
		self.processQuote = function(quote) {
			if (rowLookup[quote.code]) {
				rowLookup[quote.code].updateQuote(quote);
			} else {
				var row = new StockRow(quote);
				self.rows.push(row);
				rowLookup[quote.code] = row;
			}
		};
	}

	function StockRow(data) {
		var self = this;
		
		self.code = data.code;
		self.name = data.symbol;
		// 当前价
		self.price = ko.observable(data.price);
		// 涨跌额
		self.priceChange = ko.computed(function() {
			return self.price() - data.preclose;
		});
		// 涨跌幅
		self.changePercent = ko.computed(function() {
			return self.priceChange() / data.preclose;
		});
		// 买入
		self.buy = ko.observable(data.askprice[0]);
		// 卖出
		self.sell = ko.observable(data.bidprice[0]);
		// 昨收
		self.preClose = data.preclose;
		// 今开
		self.open = data.open;
		// 最高
		self.high = ko.observable(data.high);
		// 最低
		self.low = ko.observable(data.low);

		self.fmtDataTime = function(day, time) {
			var day = day.slice(0, 4) + '-' + day.slice(4, 6) + '-' + day.slice(6, 8);
			var time = _.padStart(time, 9, '0');
			var dataTime = day + ' ' + time.slice(0, 2) + ':' + time.slice(2, 4) + ':' + time.slice(4, 6);// + ':' + time.slice(6, 9);
			return dataTime;
		};
		self.time = ko.observable(self.fmtDataTime(data.day, data.time));

		// 更新单个股票行情
		self.updateQuote = function(qData) {
			self.price(qData.price);
			self.buy(qData.askprice[0]);
			self.sell(qData.bidprice[0]);
			self.low(qData.low);
			self.high(qData.high);

			self.time(self.fmtDataTime(qData.day, qData.time));
		};
	}

	var viewModel = new QuotesViewModel();
	ko.applyBindings(viewModel);

	// 获取行情
	var t1Socket = io.connect('ws://120.55.88.227/stocks');

	t1Socket.on('connect', function() {
		console.log('connected...');

		t1Socket.send('600570.SH,600892.SH');

		t1Socket.on('message', function(message) {
			var data = $.parseJSON(message);
			viewModel.processQuote(data);
		});

		t1Socket.on('disconnect', function() {
			console.log('disconnect...');
		});
	});
});
