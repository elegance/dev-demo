var nunjucks = require('nunjucks');

//设置选项
nunjucks.configure({ autoescape: true });
//nunjucks.configure('views', { autoescape: true });   不用设置此选项，此与express有关

//字符串输出
console.log(nunjucks.renderString('Hello {{ userName }}', {userName: 'poxi!'}));
console.log(nunjucks.renderString('Hello {{ username }}', { username: 'James' }));

// 文件渲染
var s = nunjucks.render('child-1.html');
console.log(s);
