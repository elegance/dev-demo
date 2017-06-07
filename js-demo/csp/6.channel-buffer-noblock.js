/**
 * 带缓冲的管道不会在put操作时堵塞
 * 注：学习CSP模型的新手往往会认为有 缓冲的 channel 会比 无缓冲的channel应用更广泛，但实际情况恰恰相反。
 * 有一些场景适合有缓冲的channel，但在使用之前务必深思熟虑，一定要确认使用缓冲的必要性。
 * 
 * 另：为什么没有容量自动增长的缓冲区？
 * 老生常谈的话题，一个看上去“永不枯竭”的资源，可能因为时过境迁，而存在bug，导致堆积等
 */
const csp = require('js-csp');
const {go, chan, put, buffers} = csp;

let ch = chan(buffers.fixed(2));

go(function* () {
    yield put(ch, 'value A');
    yield put(ch, 'value B');
    console.log('I should print!');
    yield put(ch, 'value C'); // buffers 为 2，这里如果消息没有被消费，这里会被阻塞
    console.log('I should not print!');
});