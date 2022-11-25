package io.ep2p.kademlia.netty.standalone;

import io.ep2p.kademlia.NodeSettings;
import io.ep2p.kademlia.exception.UnsupportedBoundingException;
import io.ep2p.kademlia.node.KeyHashGenerator;
import io.ep2p.kademlia.util.BoundedHashUtil;

import java.math.BigInteger;

public class ShellKeyHashGenerator implements KeyHashGenerator<BigInteger, String> {

    @Override
    public BigInteger generateHash(String key) {
        try {
            return new BoundedHashUtil(NodeSettings.Default.IDENTIFIER_SIZE).hash(key.hashCode(), BigInteger.class);
        } catch (UnsupportedBoundingException e) {
            e.printStackTrace();
        }
        return BigInteger.valueOf(key.hashCode());
    }
}
