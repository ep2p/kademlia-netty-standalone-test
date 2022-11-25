# kademlia netty standalone test

Simple CLI application written using `Spring Shell` for Kademlia Netty.


## How to use:

First, download the jar file from releases or build the project yourself using mvn:

```
mvn clean package
```

### Run

First, run the jar file:

```
java -jar path/to/appliaction.jar
```

You will get spring shell:

```
shell:>
```

Now, server your first node by entering the shell command `serve` and pass id, host and port of the current machine:

```
shell:> serve --id 1 --host 127.0.0.1 --port 8001
```

expected output:

```
2022-11-25 15:02:52.132  WARN 40382 --- [           main] io.netty.bootstrap.ServerBootstrap       : Unknown channel option 'SO_KEEPALIVE' for channel '[id: 0x2ef03ff4]'
2022-11-25 15:02:52.133  INFO 40382 --- [ntLoopGroup-4-1] io.netty.handler.logging.LoggingHandler  : [id: 0x2ef03ff4] REGISTERED
2022-11-25 15:02:52.134  INFO 40382 --- [ntLoopGroup-4-1] io.netty.handler.logging.LoggingHandler  : [id: 0x2ef03ff4] BIND: /127.0.0.1:8001
2022-11-25 15:02:52.135  INFO 40382 --- [ntLoopGroup-4-1] io.netty.handler.logging.LoggingHandler  : [id: 0x2ef03ff4, L:/127.0.0.1:8001] ACTIVE
Started node
```

Run the application in second server or machine (surely works on local too) and bootstrap it with first node:
```
shell:> serve --id 2 --host 127.0.0.1 --port 8002 --bootstrap 1:127.0.0.1:8001
```
Format of bootstrap node data is: `id:host:port`.

### Store and lookup

After running your nodes, you can start storing and looking up for data:

```
shell:> store key value
```

```
shell:> lookup key
```

### Stopping application

Before stopping the application run `stop` in all nodes:

```
shell:> stop
```

And then enter `exit` command to exit the shell.
