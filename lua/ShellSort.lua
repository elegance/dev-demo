--[[
    希尔排序: 希尔排序其实可以理解为 插入排序的延伸, 基本原理一致, 将步进值做了调整
--]]

tb = {1, 3, 5, 4, 8, 2, 7, 6}

print('origin:'..table.concat(tb, ' '))

function sort(arr)
    -- 步进值
    h = math.ceil( #arr / 3 )

    repeat
        for i = h, #arr, h do
            for j = i, h + 1, -h do
                if arr[j] < arr[j-h] then
                    arr[j], arr[j-h] = arr[j-h], arr[j]
                else
                    break
                end
            end
        end
        if h == 1 then 
            break
        end

        h = math.ceil(h / 3)
    until false
end


sort(tb)
print(' after:'..table.concat( tb, " "))

return sort