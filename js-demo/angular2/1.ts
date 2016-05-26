// angular命令行工具(适用于angular2)
> npm install angular-cli -g // 安装angular-cli
> ng new project-name   //创建一个新的项目，置为默认设置
> ng build  //构建/编译应用
> ng test //运行单元测试
> ng e2e //运行端到端(end-to-end)测试
> ng serve //启动一个小型的web服务器
> ng deploy // 部署

// 除了一些构建方面的功能，angular-cli还可以：
> ng generate component my-comp //生成一个新的组件
> ng generate directive my-dir //生成指令
> ng generate pipe my-pipe //生成管道
> ng generate service my-service //生成服务
> ng generate route my-route //生成路由
> ng generate class my-class //生成一个简易的模型类

// 从模块库引入三个类型定义
import {Component} from 'angular2/core';
import {bootstrap} from 'angular2/platform/browser';

//组件定义
@Component({
    selector: 'my-app',
    template: '<h1>Hello,Angular2</h1>',
    //templateUrl: 'testTplate.html',
    styles: [`
        h1{background:#4dba6c;color:#fff}
    `],
    //styleUrls: ['ez-gretting.css'],
    properties: ['name', 'country'],
    events: ['change'],
    pipes: ['CustomerPipe'], //依赖管道
    directives: [EzHilight] //依赖指令
})
@RouteConfig([
    {path: '/video', component: EzVideo, name='video'},
    {path: '/music', component: EzMusic, name='music'}
])
class EzApp{}

// 管道定义
@Pipe({
    name = 'reverse'
    pure: true  //一般为纯净的无状态管道
})
class ReversePipe {
    transform(input, args) {
        return input.split('').reverse().join('');
    }
}

// 指令定义
@Directive({
    selector: 'ez-h',
    inputs: ['bgColor: ez-h'],
    host: {
        '(click)': 'onMyClick()',
        '(mouseover)': 'onMyMouseOver()'
    }
})
class EzHilight{
    constructor(@Inject(ElementRef) elRef, @Inject(Renderer) renderer) {
        this.el = elRef.nativeElement;  //获取当前指令所在的Dom元素
        this.el.styles.color = 'red';
        this.renderer = renderer;
    }
    set bgColor(c) {
        this.el.style.background = c;
    }
    onMyClick() {
        
    }
    onMyMouseOver() {

    }
}

//渲染组件
bootstrap(EzApp);


