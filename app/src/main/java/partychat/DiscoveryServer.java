package partychat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

public class DiscoveryServer extends ControlClass implements Runnable {
    
    DatagramSocket discoverySocket;
    NetworkInterface thisInterface;
    boolean DISCOVERY_FLAG;

    public DiscoveryServer() throws SocketException {
        discoverySocket = new DatagramSocket(2022);
        DISCOVERY_FLAG = true;
    }

    public void setDiscoveryInterface(NetworkInterface newInterface) {
        this.thisInterface = newInterface;
    }

    public void run() {
        try {
            while(true) {
                byte[] buffer = new byte[100];
                DatagramPacket recieved = new DatagramPacket(buffer, 100);
                discoverySocket.receive(recieved);
                InetAddress sender = recieved.getAddress();
                String content = new String(recieved.getData(),0,recieved.getLength()).trim();
                if (content.equals(OPCodes.SERVER_DISCOVERY+"")) {
                    byte[] responseContent =  getServerName().getBytes();
                    DatagramPacket response = new DatagramPacket(responseContent, responseContent.length, sender, 2022);
                    discoverySocket.send(response);
                }
                if( Thread.currentThread().isInterrupted() ) {
                    discoverySocket.close();
                    break;
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
