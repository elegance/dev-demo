/**
 * 例3 "进程"等待来自管道的数据
 */
const csp = require('js-csp');
const {go, chan, take, putAsync} = csp;

let ch = chan();

go(function* () {
    const received = yield take(ch);
    console.log('RECEIVED:', received);
});

const text = 'something';
console.log('SENDING:', text);

putAsync(ch, text);