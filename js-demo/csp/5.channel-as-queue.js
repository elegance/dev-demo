/**
 * 管道也是队列
 */
const csp = require('js-csp');
const { go, chan, take, put } = csp;

let ch = chan();

// Process A
go(function* () {
    const text = yield take(ch);
    console.log('A RECEIVED:', text);
});

// Process B
go(function* () {
    const text = yield take(ch);
    console.log('B RECEIVED:', text);
});

// Process C
go(function* () {
    const text = 'dog';
    console.log('C SENDING:', text);
    yield put(ch, text);
    // yield put(ch, text);
});