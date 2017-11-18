# 有状态的 actor , 每收到一个消息计数累加1
# defmodule Counter do
#     def loop(count) do
#         receive do
#             {:shutdown} -> exit(:normal)
#             {:next} -> 
#                 IO.puts("Current count: #{count}")
#                 loop(count + 1)
#         end
#     end
# end

# counter = spawn(Counter, :loop, [1])

# send(counter, {:next})
# send(counter, {:next})
# send(counter, {:next})


# 用API 隐藏通信细节

defmodule Counter do
    def start(count) do
        spawn(__MODULE__, :loop, [count])
    end
    def next(counter) do
        send(counter, {:next})
    end
    def loop(count) do
        receive do
            {:shutdown} -> exit(:normal)
            {:next} -> 
                IO.puts("Current count: #{count}")
                loop(count + 1)
        end
    end
end

counter = Counter.start(42)
Counter.next(counter)
Counter.next(counter)



receive do
    {:EXIT, ^counter, reason} -> IO.puts("Talker has exited (#{reason})") # ^ 脱字符的第二个元素，将不会绑定到消息的第二个数据
end

send(counter, {:shutdown})