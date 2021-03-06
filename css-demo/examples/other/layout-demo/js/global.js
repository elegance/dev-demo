$(function() {
	window.Util = window.Util || {};

	/**
	 * 巧用a标签得到url的详细信息
	 * @param {String} url
	 */
	Util.parseURL = function(url) {
		var a = document.createElement('a');
		a.href = url;
		return {
			source: url,
			protocol: a.protocol.replace(':', ''),
			host: a.hostname,
			port: a.port,
			query: a.search,
			params: (function() {
				var ret = {},
					seg = a.search.replace(/^\?/, '').split('&'),
					len = seg.length,
					i = 0,
					s;
				for (; i < len; i++) {
					if (!seg[i]) {
						continue;
					}
					s = seg[i].split('=');
					ret[s[0]] = s[1];
				}
				return ret;
			})(),
			file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
			hash: a.hash.replace('#', ''),
			path: a.pathname.replace(/^([^\/])/, '/$1'),
			relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
			segments: a.pathname.replace(/^\//, '').split('/')
		};
	};

	// 头部菜单高亮
	Util.urlInfo = Util.parseURL(location.href);

	var $actDom = $('#tp-menu-' + Util.urlInfo.file.slice(0, Util.urlInfo.file.lastIndexOf('.')));
	console.info($actDom);
	if ($actDom) {
		$actDom.addClass('active');
		$actDom.parent().siblings().find('a.active').removeClass('active');
	}
});
