--[[
    插入排序, 像插牌一样,后面的牌往前依次对比,往前插入
    特点: 当不能往前插入时,可提前退出循环, 进入下一轮循环
    应用: 针对近乎有序的数据能达到O(n)的复杂度,如果知道数据接近有序的, 那么使用这种方式将比快排等其他算法更优, 比如日志中可能有少量时间排序不对,可能是异步导致的.
--]]

tb = {1, 3, 2, 4, 6, 5, 7}

print('origin:'..table.concat( tb, " "))

function sort( arr )
    for i = 2, #arr, 1 do
        for j = i, 2, -1 do -- 注意 此处是2, 同样因为是索引从1开始的, 要保证 j-1 在有效范围内
            if arr[j] < arr[j-1]  then
                arr[j], arr[j-1] = arr[j-1], arr[j]
            else
                break --提前退出,很重要 性能的关键
            end
        end
    end
end

sort(tb)
print(' after:'..table.concat( tb, " "))

return sort