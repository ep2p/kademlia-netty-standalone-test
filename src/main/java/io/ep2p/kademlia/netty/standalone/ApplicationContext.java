package io.ep2p.kademlia.netty.standalone;

import io.ep2p.kademlia.netty.NettyKademliaDHTNode;
import lombok.Data;

@Data
public class ApplicationContext {
    NettyKademliaDHTNode<String, String> node;
}
