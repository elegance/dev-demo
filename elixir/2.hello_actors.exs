defmodule Talker do
    def loop do
        receive do
            {:greet, name} -> IO.puts("Hello #{name}")
            {:praise, name} -> IO.puts("#{name}, you're amazing'")
            {:celebrate, name, age} -> IO.puts("Here's to anaother #{age} years, #{name}")
            {:shutdown} -> exit(:normal)
        end
        loop
    end
end

Process.flag(:trap_exit, true)
pid = spawn_link(&Talker.loop/0)

send(pid, {:greet, "Huey"})
send(pid, {:praise, "Dewey"})
send(pid, {:celebrate, "Louie", 16})
send(pid, {:shutdown})

receive do
    {:EXIT, ^pid, reason} -> IO.puts("Talker has exited (#{reason})") # ^ 脱字符的第二个元素，将不会绑定到消息的第二个数据
end