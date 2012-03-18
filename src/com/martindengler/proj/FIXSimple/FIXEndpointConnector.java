package com.martindengler.proj.FIXSimple;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * Convenience class for Initiators
 */

public class FIXEndpointConnector extends FIXEndpoint {


    public FIXEndpointConnector(String senderCompId, String targetCompId) {
        super(senderCompId, targetCompId);
    }


    public Boolean connect(String hostname, int port) {
        return this.connect(hostname, port, 10);
    }


    public Boolean connect(String hostname, int port, int timeout) {
        InetSocketAddress endpointAddress = new InetSocketAddress(hostname, port);
        try {
            Socket endpoint = new Socket();
            endpoint.connect(endpointAddress, timeout);

            return this.connect(endpoint);

        } catch (IOException ioe) {
            System.err.println(String.format("FIXEndpoint.connect(...): couldn't connect to %s:%s (timeout: %s)",
                            hostname, port, timeout));
            System.err.println(ioe.getMessage());
            System.err.println(ioe.toString());

            this.connected = false;

            return false;
        }
    }

}