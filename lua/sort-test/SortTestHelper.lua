--[[
    定义一些 排序 测试的辅助函数
--]]

-- os.time 返回的是秒单位，不符合要求，所以使用 socket.gettime() 来替代
local socket = require('socket')

-- 1. 生成指定个数的随机数， 比如生成1000w个随机数
--@param count: 生成多少个
--@param lower: 范围起始值
--@param upper: 范围结束值
local function generateRandomArray(count, lower, upper)
    lower = (lower == nil) and 1 or lower -- 1作为默认起始值
    upper = (upper == nil) and count or upper

    local arr = {}
    math.randomseed(tonumber(tostring(os.time()):reverse():sub(1,6)))
    for i = 1, count, 1 do
        arr[i] = math.random(lower, upper)
    end

    return arr
end

-- 生成一个近乎有序的数组
-- 首先生成一个含有[0...n-1]的完全有序数组, 之后随机交换swapTimes对数据
-- swapTimes定义了数组的无序程度:
-- swapTimes == 0 时, 数组完全有序
-- swapTimes 越大, 数组越趋向于无序
local function generateNearlyOrderedArray(count, swapTimes)
    local arr = {}
    for i = 1, count, 1 do
        arr[i] = i
    end

    math.randomseed(tonumber(tostring(os.time()):reverse():sub(1,6)))
    for i = 1, swapTimes, 1 do
        local a = math.random(1, count)
        local b = math.random(1, count)

        arr[a], arr[b] = arr[b], arr[a]
    end

    return arr
end

-- 判断一个数组是否有序
local function isSorted(arr)
    for i = 2, #arr, 1 do
        if arr[i] < arr[i-1] then
            return false
        end
    end

    return true
end

local function testSort(funcName, func, arr)
    local startTime = socket.gettime()
    print('startTime:' .. startTime)

    func(arr)

    local endTime = socket.gettime()
    print('endTime:' .. endTime)

    assert(isSorted(arr), '排序不正确')

    print(funcName .. ' : ' .. (endTime - startTime) .. 'ms')
end

return {
    generateRandomArray = generateRandomArray,
    generateNearlyOrderedArray = generateNearlyOrderedArray,
    isSorted = isSorted,
    testSort = testSort
}