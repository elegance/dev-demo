$(function() {
	var pools = [
		{
			code: "IF1512",
			name: "沪深1512"
		},
		{
			code: "IF8888",
			name: "沪深指数"
		}
	];

	var dataStr = '{"code":"IF1512.CF","canTrade":"1","status":"0","day":"20151026","time":"112957800","price":"33850000","openInterest":"19407","volume":"1401","turnover":"1421704680","askprice":[33858000,33872000,33890000,33898000,33900000],"askvol":[1,2,1,1,3],"bidprice":[33818000,33810000,33808000,33790000,33718000],"bidvol":[1,1,2,1,4],"highlimited":"36832000","lowlimited":"30136000","preSettlePrice":"33484000","open":"33780000","high":"33996000","low":"33604000"}';



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
			rowLookup[quote.code.slice(0, quote.code.indexOf('.'))].updateQuotes(quote);
		};
	}

	function FuturesRow(data) {
		var self = this;

		//行情变化时
		self.updateQuotes = function(qData) {
			self.price(qData.price);
			self.open(qData.open);
			self.preSettlePrice(qData.preSettlePrice);
			self.high(qData.high);
			self.low(qData.low);
			self.volume(qData.volume);
			self.openInterest(qData.openInterest);
			self.askprice(qData.askprice[0]) 
			self.askvol(qData.askvol[0]);
			self.bidprice(qData.bidprice[0]);
			self.bidvol(qData.bidvol[0]);
			self.time(new Date());
		};

		self.price = ko.observable('--');
		self.open = ko.observable('--');
		self.preSettlePrice = ko.observable('--');
		self.high = ko.observable('--');
		self.low = ko.observable('--');
		self.volume = ko.observable('--');
		self.openInterest = ko.observable('--');
		self.change = ko.computed(function() {
			return self.open() == '--' ? '--' : fmp(((self.price() - self.preSettlePrice()) / self.preSettlePrice()) * 100, 2) + '%';
		});
		self.changeNum = ko.computed(function() {
			return self.open() == '--' ? 0 : self.price() - self.preSettlePrice();
		});
		self.askprice = ko.observable('--');
		self.askvol = ko.observable('--');
		self.bidprice = ko.observable('--');
		self.bidvol = ko.observable('--');
		self.time = ko.observable(new Date());

		return $.extend(self, data);
	}

	function fmp(n) {
		return Tzb.NumberExt.fixed(n, 2);
	}
	function fm(n) {
		return Tzb.NumberExt.fixed(n / 10000, 2);
	}
	
	var viewModel = new QuotesViewModel();
	ko.applyBindings(viewModel);

	setTimeout(function() {
		viewModel.loadDatas(pools);

		setInterval(function() {
			var tdata = JSON.parse(dataStr);
			tdata.price = parseInt(tdata.price) + (Tzb.utils.random(-400, 100)) * 1000;
			
			viewModel.processQuote(tdata);
		}, 1000);
	}, 50);


	//var sUrl = '//' + location.host + (location.port ? ':' + location.port : '') + '/futures';
	//var quotesSocket = io.connect(sUrl);

	//quotesSocket.on('connect', function() {
	//	console.info('Connected');

	//	// 接收行情推送
	//	quotesSocket.on('message', function(message) {
	//		console.info('message:' + message);
	//		if (message) {
	//			var data = $.parseJSON(message);
	//		}
	//	});

	//	quotesSocket.on('disconnect', function() {
	//		console.info('Disconnected');
	//	});

	//	//if (e.keyCode == 13 && contractCode.length > 4) {
	//	//	quotesSocket.send(contractCode + '.' + exchange);
	//	//}
	//});
});
