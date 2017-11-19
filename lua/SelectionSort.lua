--[[
    选择排序, 每轮选出最小元素放置到未排序段的首位
    特点:每轮大循环只需要 swap 一次值, 因为需要查找最小, 所以不能提前退出循环
--]]
tb = {1, 3, 2, 4, 6, 5}

print('origin:'..table.concat( tb, " "))

function sort(arr)
    for i = 1, #arr, 1 do
        minIndex = i
        for j = i + 1, #arr, 1 do
            if arr[j] < arr[minIndex] then
                minIndex = j
            end
        end
        arr[i], arr[minIndex] = arr[minIndex], arr[i]
    end
end

sort(tb)

print(' after:'..table.concat( tb, " "))

return sort