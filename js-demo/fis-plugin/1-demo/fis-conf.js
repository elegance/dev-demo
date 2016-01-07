fis.match('*.js', {
	// 代码校验检查，比较特殊，所以需要 release 命令命令行添加 -l 参数
	// 不需要返回，可以使用fis.log.warning/fis.log.error来打印信息
	lint: function(content, file, settings) {
		console.info('lint plugin ....');
		fis.log.warning('lint 输出警告')
	},

	//预处理阶段，比如 less、sass、es6、react 前端模板等都在此处预编译处理
	parser: function(content, file, settings) {
		console.info('parser plugin ....');
		return content += '(parse)';
	},

	//标准化前处理插件, 会有缓存，如果文件有变动才会执行此步骤
	// fis3 release 会调用
	preprocessor: function(content, file, settings) {
		console.info('preprocessor plugin ....');
		return content += '(preprocessor)'; //在文件内容中迭加 一个时间
	},

	//标准化插件，处理内置语法
	standard: function(content, file, settings) {
		console.info('standard plugin ....');
		return content += '(standard)';
	},

	// 标准化后处理插件
	postprocessor: function(content, file, settings) {
		console.info('postprocessor plugin ....');
		return content += '(postprocessor)';
	},

	// optimizer, 最后优化处理
	optimizer: function(content, file, settings) {
		console.info('optimizer plugin ....');
		return content += '(optimizer)';
	}
});

//不清楚，应该写哪个阶段的插件
