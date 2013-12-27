//----------------------------------------------------------------对象继承

/**
 *
 *1. 基本模式,原型链 实现，一般不使用 
 *   问题：1. 原型属性共享问题  
 *         2. 创建子类实例时不能向超类的构造函数中传参
 *
 */
function SuperType () {
    this.property = true;
}

SuperType.prototype.getSuperValue = function () {
    return this.property;
}

function SubType () {
    this.subProperty = false;
}

var instance3 = new SubType2();
print(instance3.colors);


/**
 *
 *3. 组合继承
 *   集原型、构造函数的有点来实现，思路：用原型链来对原型属性和方法的继承，通过构造函数来对实例属性的继承
 *
 */

function SuperType3 (name) {
    this.name = name;
    this.colors = ['red', 'blue', 'green'];
}

SuperType3.prototype.sayName = function () {
    print(this.name);
};


function SubType3(name, age) {
    //继承属性
    SuperType3.call(this, name);
    this.age = age;
}

//继承方法
SubType3.prototype = new SuperType3();

SubType3.prototype.sayAge = function() {
    print(this.age);
};

//测试
var instance4 = new SubType3('merry', '22');
instance4.colors.push('black');
instance4.sayName();
instance4.sayAge();
print(instance4.colors);

var instance5 = new SubType3('jhony', '18');
instance5.sayName();
instance5.sayAge();
print(instance5.colors);
