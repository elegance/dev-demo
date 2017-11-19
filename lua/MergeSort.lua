--[[
    归并排序, 从中间一分为二, 递归直到不能再分, 然后利用辅助空间 **将两个分别有序的数组合并为一个有序数组**, 递归合并
    特点: 相比N^2级别, 它是O(nlogn)级别的突破(图解归并排序它其实是一个树形的方式), 它是一个稳定排序, 非原地排序,需要辅助空间,
    应用: 如果对空间比较敏感就不太适合, 需要额外空间是O(n)
--]]

local tb = {3, 1, 2, 4, 6, 5, 8, 7}

print('origin:'..table.concat(tb, ' '))

function mergeSort(arr, l, r)
    -- 归并推出条件
    if l >= r then
        return
    end
    -- mid = (l + r) / 2 -- l + r 可能发生溢出, 所以采用下面的方式取 mid
    mid = math.floor((r - l) /2) + l --另外注意 lua 的 / 是 除法, 会有小数, 需要处理好小数

    -- 分别对左半部分/右半部分进行 递归 mergeSort
    mergeSort(arr, l, mid)
    mergeSort(arr, mid + 1, r)

    -- 并操作, 将两个分别有序的数组合并为一个有序数组
    merge(arr, l, mid, r)
end

--
-- 并操作, 将两个分别有序的数组合并为一个有序数组
-- arr[l..mid] 与 arr[mid+1..r] 是分别有序的两个数组
function merge(arr, l, mid, r)
    -- 将[l...r]建立出辅助空间, 左边的有序数组:1...(mid-l), 右边的有序数组: (mid+1)....r
    -- 没有找到 lua 类似 java/c 中的"copy"能直接从数组段中复制一段的方法, 这里使用for循环来建立辅助空间
    aux = {}
    offset = l - 1 -- 数组索引是从1开始的, 定义一个变量存储便宜,方便使用, 不用每次 使用: l - 1

    for i = 1, r - l + 1, 1 do
        aux[i] = arr[offset+i]
    end

    -- i 代表 遍历aux的索引, j 代表 左版部分索引, k 代表 右半部分索引
    i, j, k = 1, 1, mid+1-offset

    repeat
        if j > mid-offset then
            arr[offset+i] = aux[k]
            k = k + 1
        elseif k > #aux then
            arr[offset+i] = aux[j]
            j = j + 1;
        elseif aux[j] < aux[k] then
            arr[offset+i] = aux[j]
            j = j + 1;
        else
            arr[offset+i] = aux[k]
            k = k + 1
        end
        i = i + 1
    until i > #aux
end

function sort(arr)
    mergeSort(arr, 1, #arr)
end

sort(tb)

print(' after:'..table.concat(tb, ' '))

return sort