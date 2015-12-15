$(function() {
	var pools = [
		{
			code: "IF1511",
			name: "沪深1511"
		},
		{
			code: "IF1512",
			name: "沪深1512"
		},
		{
			code: "IF1603",
			name: "沪深1603"
		},
		{
			code: "IF1606",
			name: "沪深1606"
		}
	];

	function QuotesViewModel() {
		var self = this;
		self.rows = ko.observableArray();
		
		var rowLookup = {};
		// 初始化load数据
		self.loadDatas = function(dataArr) {
			for (var i = 0, j = dataArr.length; i < j; i++) {
				var row = new FuturesRow(dataArr[i]);
				self.rows.push(row);
				rowLookup[row.code] = row;
			}
		};

		// 更新行情
		self.processQuote = function(quote) {
			if (quote.code) {
				rowLookup[quote.code.slice(0, quote.code.indexOf('.'))].updateQuotes(quote);
			}
		};
	}

	function FuturesRow(data) {
		var self = this;

		//行情变化时
		self.updateQuotes = function(qData) {
			self.price(fm(qData.price));
			self.open(fm(qData.open));
			self.preSettlePrice(fm(qData.preSettlePrice));
			self.high(fm(qData.high));
			self.low(fm(qData.low));
			self.volume(qData.volume);
			self.openInterest(qData.openInterest);
			self.askprice(fm(qData.askprice[0]));
			self.askvol(qData.askvol[0]);
			self.bidprice(fm(qData.bidprice[0]));
			self.bidvol(qData.bidvol[0]);
			self.time(qData.dataTime);
		};

		self.price = ko.observable('--');
		self.open = ko.observable('--');
		self.preSettlePrice = ko.observable('--');
		self.high = ko.observable('--');
		self.low = ko.observable('--');
		self.volume = ko.observable('--');
		self.openInterest = ko.observable('--');
		self.change = ko.computed(function() {
			if (self.open() == '--') {
				return;
			}
			var n1 = self.price() * 100;
			var n2 = self.preSettlePrice() * 100;
			var chg = n1 - n2; 
			self.arrow(chg < 0 ? '<i class="icon-arrow-down"></i>' : '<i class="icon-arrow-up"></i>');
			return fmp((chg / n2) * 100, 2) + '%';
		});
		self.arrow = ko.observable();
		self.changeNum = ko.computed(function() {
			var n1 = self.price() * 100;
			var n2 = self.preSettlePrice() * 100;
			return self.open() == '--' ? 0 : fmp((n1 - n2) / 100);
		});
		self.askprice = ko.observable('--');
		self.askvol = ko.observable('--');
		self.bidprice = ko.observable('--');
		self.bidvol = ko.observable('--');
		self.time = ko.observable(new Date());

		return $.extend(self, data);
	}

	function fmp(n) {
		return Tzb.NumberExt.fixed(n, 2, 'round');
	}
	function fm(n) {
		return Tzb.NumberExt.fixed(n / 10000, 2);
	}
	
	var viewModel = new QuotesViewModel();
	ko.applyBindings(viewModel);
	viewModel.loadDatas(pools);

	var quotesSocket = io.connect('/futures');
	quotesSocket.on('connect', function() {
		console.info('Connected');
		var sendStr = '';
		$.each(pools, function(idx, item) {
			sendStr += item.code + '.CF,';
		});
		quotesSocket.send(sendStr.slice(0, sendStr.lastIndexOf(',')));

		// 接收行情推送
		quotesSocket.on('message', function(message) {
			//console.info('message:' + message);
			if (message) {
				var data = $.parseJSON(message),
					day = data.day.slice(0, 4) + '-' + data.day.slice(4, 6) + '-' + data.day.slice(6, 8),
					time = Tzb.utils.lpad(data.time, 9), //时间不足9位时，左侧补0，9位：'hhmissSSS'
					dataTime = day + ' ' + time.slice(0, 2) + ':' + time.slice(2, 4) + ':' + time.slice(4, 6) + '.' + time.slice(6, 9);
				data.dataTime = dataTime;
				viewModel.processQuote(data);
			}
		});

		quotesSocket.on('disconnect', function() {
			console.info('Disconnected');
		});
	});
});
