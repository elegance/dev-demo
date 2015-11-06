var wrapped = _([1, 2, 3]);

console.info(wrapped.reduce(function(total, n) {
	return total + n;
}));

var squares = wrapped.map(function(n) {
	return n * n;
});

console.info(squares.value());
console.info(_.isArray(squares));
console.info(_.isArray(squares.value()));

var users = [
  { 'user': 'barney',  'age': 36 },
  { 'user': 'fred',    'age': 40 },
  { 'user': 'pebbles', 'age': 1 }
];

console.info(
_.chain(users)
	.sortBy('age')
	.map(function(chr) {
		return chr.user + ' is ' + chr.age;
	})
	.first()
	.value()
);
console.info(
	_([1, 2, 3])
		.tap(function(arr) {
			arr.pop();
		})
		.reverse()
		.value()
);
