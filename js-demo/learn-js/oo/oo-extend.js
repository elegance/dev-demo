//----------------------------------------------------------------对象继承

/**
 *
 *1. 基本模式,原型链 实现，一般不使用 
 *   问题：1. 原型属性共享问题  
 *	       2. 创建子类实例时不能向超类的构造函数中传参
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

//实现继承
SubType.prototype = new SuperType();

SubType.prototype.getSubValue = function () {
	return this.subProperty;
};


//测试
print('---------------------------------------------------------- Prototype mode start:')
var instance1 = new SubType();
print(instance1.getSuperValue());
print(instance1 instanceof Object);
print(instance1 instanceof SuperType);
print(instance1 instanceof SubType);



/**
 *
 *2. 借用构造函数,借用构造函数实现就会有构造函数所带来的问题
 *   问题：1. 方法都在构造函数中定义，方法的复用就无从谈起了,因此单独这么使用这种模式的也很少  
 *
 */
function SuperType2 () {
	this.colors = ['red', 'blue', 'green'];
}

//实现继承
function SubType2 () {
	SuperType2.call(this);
}

//测试
print('---------------------------------------------------------- Constructor mode start:')
var instance2 = new SubType2();
instance2.colors.push('balack');
print(instance2.colors);

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
print('---------------------------------------------------------- Prototype && Constructor mode start:')
var instance4 = new SubType3('merry', '22');
instance4.colors.push('black');
instance4.sayName();
instance4.sayAge();
print(instance4.colors);

var instance5 = new SubType3('jhony', '18');
instance5.sayName();
instance5.sayAge();
print(instance5.colors);



/**
 *
 *4. 原型式继承
 *   1. 基于已有对象创建对象
 *   2. ECMAScript 5通过Object.create 来实现
 *
 */
function object(o) {
	function F() {}
	F.prototype = o;
	return new F();
}

var person = {
	name: 'police',
	friends: ['judge', 'lawyer']
};

//测试
print('---------------------------------------------------------- base on exists Object mode start:')
var anotherPerson1 = object(person);
anotherPerson1.name = 'jack';
anotherPerson1.friends.push('rose');


var anotherPerson2 = object(person);
anotherPerson2.name = 'ajee';
anotherPerson2.friends.push('shit');

print(anotherPerson1.name);
print(anotherPerson1.friends);
print(anotherPerson2.name);
print(anotherPerson2.friends);
print(person.friends);
