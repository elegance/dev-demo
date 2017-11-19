--[[
    冒泡排序
--]]
-- 测试数据
tb = {1, 5, 2, 4, 6, 3}

print('origin:'..table.concat(tb, ' '))

-- swap 原理, 可以采取以下赋值方式达到 swap 效果 
-- tb[2], tb[3] = tb[3], tb[2]

function sort(arr)
    for i = 1, #arr, 1 do
        for j = 1, #arr - i, 1 do -- 注意此处呀, 索引是从 1 开始的, length结尾, 故不需要像其他语言那样 length-i-1
            if arr[j] > arr[j+1] then
                arr[j], arr[j+1] = arr[j+1], arr[j]
            end
        end
    end
end

sort(tb)

print(' after:'..table.concat(tb, ' '))

return sort