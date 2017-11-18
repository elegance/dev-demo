/**
 * 例2 "进程"可以暂停
 */
const csp = require('js-csp');
const {go, timeout} = csp;

go(function* () {
    yield timeout(1000); // 此处yield看似暂停了一个进程(generator -> yield -> js-csp之timeout函数)
    console.log('something else after 1 second!');
});
console.log('something!');