fis.match('*.html', {
	useMap: true,
	parser: require('./fis-parser-nunjucks')
});

//fis.match('layout/*.html', {
//    release: false
//});

var util = require('util');
console.log(util.inspect(fis, false, null));
