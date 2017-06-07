/**
 * 固定大小N的缓冲区，在N个数据之后会阻塞，除此之外，还有对缓冲的 dropping 和 sliding控制
 * 缓冲 dropping ，增加超出缓冲大小的数据进入管道，管道会将其丢弃
 * 缓冲 sliding ，则是接收新的，丢弃老的
 */
const csp = require('js-csp');
const { go, chan, buffers, put, take} = csp;

let droppingChan = chan(buffers.dropping(1));
let slidingChan = chan(buffers.sliding(1));

go(function* () {
    yield put(droppingChan, 'value A');
    yield put(droppingChan, 'value B'); // 一直无人消费，所以新的消息都被丢弃
    yield put(droppingChan, 'value C'); // ...
    console.log('DROPPING CHAN:', yield take(droppingChan));  // 拿到 value A
});

go(function* () {
    yield put(slidingChan, 'value A');
    yield put(slidingChan, 'value B'); // 后浪推前浪
    yield put(slidingChan, 'value C'); // 后浪推前浪..
    console.log('SLIDING CHAN:', yield take(slidingChan)); // 拿到最后的浪 C
});