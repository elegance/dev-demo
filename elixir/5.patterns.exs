defmodule Patterns do
    def foo({x, y}) do
        IO.puts("Got a pair, first element #{x}, second #{y}")
    end
end

Patterns.foo({:a, 42})