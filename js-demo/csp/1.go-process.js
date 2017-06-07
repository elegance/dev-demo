/**
 * 例1 "进程"：这里不是操作系统的进程，是js的模拟
 */
const csp = require('js-csp');
const {go} = csp;


go(function* () {  // 启动方式：generator 函数 作为参数传递给 go 函数
    console.log('something...');
});