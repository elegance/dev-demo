local function sort(arr)
    for i = 1, #arr, 1 do
        for j = 1, #arr - i, 1 do
            if arr[j] > arr[j+1] then
                arr[j], arr[j+1] = arr[j+1], arr[j]
            end
        end
    end
end

return {
    sort = sort
}