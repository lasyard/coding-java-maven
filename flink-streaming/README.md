# flink-streaming

This is a flink streaming demo using socket.

To run, start listening on port 12345 first.

On Linux

```sh
nc -lvvp 12345
```

On macOS

```sh
nc -lv 12345
```

If you run it in a distributed environment, where is `localhost`?

To see the result, you should go to the log dir and see the `.out` file.
