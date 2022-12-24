package io.ep2p.kademlia.netty.standalone;

import io.ep2p.kademlia.NodeSettings;
import io.ep2p.kademlia.model.LookupAnswer;
import io.ep2p.kademlia.model.StoreAnswer;
import io.ep2p.kademlia.netty.NettyKademliaDHTNode;
import io.ep2p.kademlia.netty.builder.NettyKademliaDHTNodeBuilder;
import io.ep2p.kademlia.netty.common.NettyConnectionInfo;
import io.ep2p.kademlia.netty.common.NettyExternalNode;
import lombok.SneakyThrows;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@ShellComponent
public class ShellCommands {
    private final ApplicationContext applicationContext = new ApplicationContext();

    @ShellMethod
    public String setIdentifierSize(int size){
        NodeSettings.Default.IDENTIFIER_SIZE = size;
        return "Done";
    }

    @SneakyThrows
    @ShellMethod(value = "Creates and start new server", key = "serve")
    public String serve(
            @ShellOption(value = { "--id" }) String id,
            @ShellOption(value = { "--host" }) String host,
            @ShellOption(value = { "--port" }) int port,
            @ShellOption(value = { "--bootstrap" }, help = "format: id:host:port", defaultValue="none") String bootstrapNodeAddress
    ){
        NettyKademliaDHTNode<String, String> node = new NettyKademliaDHTNodeBuilder<>(
                new BigInteger(id),
                new NettyConnectionInfo(host, port),
                new SampleRepository(),
                new ShellKeyHashGenerator(),
                String.class, String.class
        ).build();

        if (!bootstrapNodeAddress.equals("none")){
            String[] split = bootstrapNodeAddress.split(":");
            if (split.length < 3){
                return "Failed to start. Need id, host and port of the bootstrap node";
            }

            BigInteger bootstrapNodeId = new BigInteger(split[0]);
            String bootstrapNodeHost = split[1];
            int bootstrapNodePort = Integer.parseInt(split[2]);

            Boolean bootstrapped = node.start(new NettyExternalNode(new NettyConnectionInfo(bootstrapNodeHost, bootstrapNodePort), bootstrapNodeId)).get(20, TimeUnit.SECONDS);
            if (bootstrapped){
                System.out.println("Bootstrapped with " + bootstrapNodeAddress);
            }else {
                node.stopNow();
                return "Failed to bootstrap";
            }
        }else {
            node.start();
        }
        applicationContext.setNode(node);
        return "Started node";
    }

    @ShellMethod(value = "Stores data into DHT")
    public String store(String key, String value) throws ExecutionException, InterruptedException {
        StoreAnswer<BigInteger, NettyConnectionInfo, String> storeAnswer = this.applicationContext.getNode().store(key, value).get();
        return String.format("Store result: %s - Node: %s%n", storeAnswer.getResult(), storeAnswer.getNode().getId());
    }

    @ShellMethod(value = "Looks up data from DHT")
    public String lookup(String key) throws ExecutionException, InterruptedException {
        LookupAnswer<BigInteger, NettyConnectionInfo, String, String> lookupAnswer = this.applicationContext.getNode().lookup(key).get();
        return String.format("Lookup result: %s - Value: %s%n", lookupAnswer.getResult(), lookupAnswer.getValue());
    }

    @ShellMethod
    public void stop(){
        this.applicationContext.getNode().stopNow();
    }

}
