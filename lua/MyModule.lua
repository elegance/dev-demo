local exports = {}

local function getName()
	return 'orh'
end

function exports.greeting()
	print('Hello, My name is '..getName())
end

return exports
