--[[
    快速排序, 选定基准元素,将其排放到**它最终排好序应该的位置**
    特点: O(nlogn)时间复杂度, 原地排序, 不属于稳定排序,这里的稳定是指: 对于相等的元素,排序前后他们的相对位置没有发生变化
    应用: 递归, 栈需要 O(logn)的而外空间
--]]

tb = {1, 3, 5, 4, 2, 7, 6}

print('origin:'..table.concat(tb, ' '))

function sort(arr)
    quickSort(arr, 1, #arr)
end

function quickSort(arr, l, r)
    if l >= r then
        return
    end

    p = partition(arr, l, r)

    quickSort(arr, l, p-1)
    quickSort(arr, p+1, r)
end

function partition(arr, l, r)
    v = arr[l]

    -- purpose: arr[l+1...j] < v < arr[j+1...i)
    i, j = l + 1, l
    repeat
        if arr[i] < v then
            arr[i], arr[j+1] = arr[j+1], arr[i]
            j = j + 1
        end
        i = i + 1
    until i > r
    arr[l], arr[j] = arr[j], arr[l]
    return j
end

sort(tb)
print(' after:'..table.concat( tb, " "))

return sort