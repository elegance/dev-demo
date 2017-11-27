local SortTestHelper = require('SortTestHelper')

---------------------------------------------- generateRandomArray
local randomArr1 = SortTestHelper.generateRandomArray(10)
print(table.concat(randomArr1, ', '))
assert(#randomArr1 == 10)

---------------------------------------------- generateNearlyOrderedArray
local randomArr2 = SortTestHelper.generateNearlyOrderedArray(10, 0)
print(table.concat(randomArr2, ', '))
assert(SortTestHelper.isSorted(randomArr2))

randomArr2 = SortTestHelper.generateNearlyOrderedArray(10, 2)
print(table.concat(randomArr2, ', '))

---------------------------------------------- isSorted
local arr = {1, 2, 3, 4, 5}
print(SortTestHelper.isSorted(arr))

arr = {1, 2, 3, 5, 4}
print(SortTestHelper.isSorted(arr))


---------------------------------------------- testSort
-- local BubbleSort = require('./sort/BubbleSort')
-- --local arr = SortTestHelper.generateRandomArray(1000)
-- local arr = SortTestHelper.generateNearlyOrderedArray(10000, 0)
-- SortTestHelper.testSort('BubbleSort', BubbleSort.sort, arr)

local QuickSort = require('./sort/QuickSort')
local arr2 = SortTestHelper.generateRandomArray(10000)
SortTestHelper.testSort('QuickSort', QuickSort.sort, arr2)