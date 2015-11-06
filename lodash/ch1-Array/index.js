console.info('_.chunk:');
console.info(_.chunk(['a', 'b', 'c', 'd'], 2));
console.info(_.chunk(['a', 'b', 'c', 'd'], 3));
console.info('_.compact:');
console.info(_.compact([0, 1, false, 2, '', 3]));
console.info('_.diffrence:')
console.info(_.difference([1, 2, 3], [4, 2]));
console.info('_.drop:')
console.info(_.drop([1, 2, 3]));
console.info(_.drop([1, 2, 3], 2));
console.info(_.drop([1, 2, 3], 5));
console.info(_.drop([1, 2, 3], 0));
console.info('_.dropRight:')
console.info(_.dropRight([1, 2, 3]));
console.info(_.dropRight([1, 2, 3], 2));
console.info(_.dropRight([1, 2, 3], 5));
console.info(_.dropRight([1, 2, 3], 0));
console.info('_.fill:');
var arr = [1, 2, 3];
_.fill(arr, 0);
console.info(arr);
console.info(_.fill([1, 2, 3], 0));
console.info(_.fill(Array(3), 2));

console.info('_.findIndex:');
var users = [
  { 'user': 'barney',  'active': false },
  { 'user': 'fred',    'active': false },
  { 'user': 'pebbles', 'active': true }
];
console.info(_.findIndex(users, function(item) {
	return item.user == 'fred';
}));
console.info(_.findIndex(users, {'user': 'fred', 'active': false}));
console.info(_.findIndex(users, {'user': 'pebbles'}));
console.info(_.findIndex(users, 'active', false));
console.info(_.findIndex(users, 'active'));

console.info('_.flatten:');
console.info(_.flatten([1, [2, 3, [4]]]));
console.info(_.flatten([1, [2, 3, [4]]], true));
console.info('_.initial:');
console.info(_.initial([1, 2, 3]));
console.info(_.initial([1, 2, 3, 4]));

console.info('_.intersection:');
console.info(_.intersection([1, 2], [2, 3], [4, 2]));
console.info(_.intersection([1, 2], [2, 3], [4, 3]));

console.info('_.pull:');
console.info(_.pull([1, 2, 3, 3], 3));
console.info(_.pull([1, 2, 3, 3], 2, 3));

console.info('_.pullAt:');
var array = [5, 10, 15, 20];
var evens = _.pullAt(array, 1, 3);
console.info(array);
console.info(evens);

console.info('_.remove:');
var arr2 = [1, 2, 3, 4];
var evens2 = _.remove(arr2, function(n) {
	return n % 2 == 0;
});
console.info(arr2);
console.info(evens2);

console.info('_.union:');
console.info(_.union([1, 2], [2, 3]));
console.info(_.union([1, 2], [2, 3], [3, 4]));

console.info('_.uniq:');
console.info(_.uniq([2, 1, 1]));
console.info(_.uniq([1, 1, 2], true));
console.info(_.uniq([1, 2.5, 1.5, 2], function(n) {
	return this.floor(n);
}, Math));

console.info('_.zip:');
console.info(_.zip([1, 2], [true, false], [30, 40, 5]));

var ziped = _.zip(['a', 'b'], [30, 40, 50], [true, false]);
console.info(_.unzip(ziped));

console.info('_.without:');
console.info(_.without([1, 2, 3, 3], 1, 2));

console.info('_.xor:');
console.info(_.xor([1, 2], [4, 2]));
console.info(_.xor([1, 2, 3, 6, 7], [4, 2, 3, 5, 6]));
