//组合使用构造函数和原型模式构建对象
function Bird(name, age) {
    //属性
    this.name = name;
    this.age = age;
    this.firends = ['human','nature'];
}

Bird.prototype = {
    constructor: Bird,
    sayName: function () {
        print(this.name);
    }
};


//动态原型模式构造对象
function Person(name, age, job) {
    //属性
    this.name = name;
    this.age = age;
    this.job = job;

    //方法
    if (typeof this.sayName != "function") {
        //debug info,Will be initialized only once.
        print('init sayName function, only once.');

        //Define function
        Person.prototype.sayName = function () {
            print(this.name);
        }
    }
}

var p1 = new Person('orh', 24, 'SoftWare Enginner');
p1.sayName();
var p2 = new Person('petter', 23, 'SoftWare Enginner');
p2.sayName();

