### HTML DOM 事件
参考：[HTML DOM 事件](http://www.runoob.com/jsref/dom-obj-event.html)

`HTML DOM事件`允许`Javascript`在`HTML`文档元素中注册不同事件处理程序。

`事件`通常与`函数`结合使用，函数不会在事件发生之前被执行！


#### 事件属性
* `bubbles`: 返回布尔值，指示事件是否是起泡事件类型
* `cancelablea`: 返回布尔值，指示事件是否可拥可取消的默认动作
* `currentTarget`: 返回事件监听器触发该事件的元素
* `target`: 返回触发此事件的元素（事件目标节点）
* `timeStamp`: 返回事件生成的日期和时间
* `type`: 返回当前 Event 对象表示的事件的名称

#### 方法
* `initEvent()`: 初始化新创建的 Event 对象的属性
* `preventDefault()`: 通知浏览器不要执行与事件关联的默认动作
* `stopPropagation()`: 不派发事件，停止冒泡。

#### 事件分类
参考：[事件类型一览表](https://developer.mozilla.org/zh-CN/docs/Web/Events)

* 鼠标
* 键盘
* 框架/对象（Frame/Object）
* 表单
* 剪切板
* 拖动
* 打印
* 多媒体
* 动画
* 过渡
* 其他..

#### 目标事件对象-方法
* `target.addEventListener(type, listener[, useCapture/options])`：将指定的监听器注册到 EventTarget（IE8- attachEvent()）- 完整参考: [EventTarget.addEventListener()](https://developer.mozilla.org/zh-CN/docs/Web/API/EventTarget/addEventListener)
* `target.dispatchEvent()`: 发送事件到监听器上（IE8- fireEvent()）
* `target.removeEventListener(type, listener[, useCapture])`: 目标事件对象上移除