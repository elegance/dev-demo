var nunjucks = require('nunjucks');

//设置选项
nunjucks.configure({ autoescape: true });

module.exports = function (content, file, settings) {
	console.log('plugin nunjucks..' + file.basename);
	return nunjucks.render(file.realpath);
};
