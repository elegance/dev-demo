# 双向通信
# 这个版本不再输出当前计数，而是将当前计数返回给发送者
defmodule Counter do
    def start(count) do
        spawn(__MODULE__, :loop, [count])
    end
    def next(counter) do
        ref = make_ref()
        send(counter, {:next, self(), ref})
        receive do
            {:ok, ^ref, count} -> count
        end
    end
    def loop(count) do
        receive do
            {:next, sender, ref} ->
                send(sender, {:ok, ref, count})
                loop(count + 1)
        end
    end
end


# 测试一下
counter = Counter.start(42)
Counter.next(counter)
Counter.next(counter)

:timer.sleep(1000)