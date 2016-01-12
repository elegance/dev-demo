// 添加 minify依赖
var minify = require('html-minifier').minify;

// 设置fis属性
fis.set('project.ignore', ['*.bak', '/layout/*', 'fis-parser-nunjucks.js', 'package.json'].concat(fis.get('project.ignore')));

fis.match('*.html', {
	useMap: true,
	parser: require('./fis-parser-nunjucks')
});

fis.media('prod').match('*.html', {
	optimizer: function(content, file, settings) {
		// 更多配置参考：https://github.com/kangax/html-minifier
		// 在线UI:http://kangax.github.io/html-minifier/
		return minify(content, {
			removeComments: true,//是否移除html注释
			collapseWhitespace: true,//是否去掉空格
			minifyJS: true,//是否压缩html里的js
			minifyCSS: true//是否压缩html里的css
		});
	}
});

fis.match('*.css', {
  optimizer: fis.plugin('clean-css')
});

fis.media('prod').match('*.js', {
	optimizer: fis.plugin('uglify-js')
});
