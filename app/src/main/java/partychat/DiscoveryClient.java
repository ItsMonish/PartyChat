package partychat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class DiscoveryClient extends Thread {
    String servers[];
    HashMap<String,InetAddress> serversIP;
    int count;
    DatagramSocket clientSocket;
    String discoveryString;

    public DiscoveryClient() throws SocketException {
        count = 0;
        servers = new String[10];
        clientSocket = new DatagramSocket(2022);
        discoveryString = OPCodes.SERVER_DISCOVERY+"";
    }

    public void run() {
        try{
            InetAddress broadCastAddress = InetAddress.getByName("255.255.255.255");
            while(true) {
                DatagramPacket discoveryPacket = new DatagramPacket(discoveryString.getBytes(), discoveryString.length(), broadCastAddress, 2022);
                clientSocket.send(discoveryPacket);
                DatagramPacket response = new DatagramPacket(null, 10);
                clientSocket.receive(response);
                InetAddress newServerIP = response.getAddress();
                String newServerName = new String(response.getData(),0,response.getLength()).trim();
                if( !serversIP.containsValue(newServerIP) ) {
                    servers[count] = newServerName;
                    serversIP.put(newServerName, newServerIP);
                }
            }
        } catch( Exception e ) { } 
    }

}
