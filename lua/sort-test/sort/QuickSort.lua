local function partition(arr, l, r)
    local v = arr[l]
    local i , j = l + 1, l

    repeat
        if arr[i] < v then
            arr[i], arr[j+1] = arr[j+1], arr[i]
            j = j + 1
        end
        i = i + 1
    until i > #arr
    arr[l], arr[j] = arr[j], arr[l]
    
    return j
end

local function quickSort(arr, l, r)
    if l >= r then
        return
    end

    local p = partition(arr, l, r)

    quickSort(arr, l, p-1)
    quickSort(arr, p+1, r)
end

local function sort(arr)
    quickSort(arr, 1, #arr)
end

return {
    sort = sort
}
