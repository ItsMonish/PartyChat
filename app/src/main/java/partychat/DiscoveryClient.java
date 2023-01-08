package partychat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscoveryClient {
    ArrayList<String> serverlist;
    HashMap<String,InetAddress> serversIP;
    int count;
    DatagramSocket clientSocket;
    String discoveryString;

    public DiscoveryClient() throws SocketException {
        count = 0;
        serversIP = new HashMap<String,InetAddress>();
        serverlist = new ArrayList<String>();
        clientSocket = new DatagramSocket(2022);
        discoveryString = OPCodes.SERVER_DISCOVERY+"";
    }

    public void initDiscovery() {
        try{
            InetAddress broadCastAddress = InetAddress.getByName("255.255.255.255");
            while(true) {
                byte[] buffer = new byte[100];
                DatagramPacket discoveryPacket = new DatagramPacket(discoveryString.getBytes(), discoveryString.length(), broadCastAddress, 2022);
                clientSocket.send(discoveryPacket);
                DatagramPacket response = new DatagramPacket(buffer, 100);
                clientSocket.receive(response);
                InetAddress newServerIP = response.getAddress();
                String newServerName = new String(response.getData(),0,response.getLength()).trim();
                if( newServerName.equalsIgnoreCase(discoveryString)) continue;
                if( !serversIP.keySet().contains(newServerName) ) {
                    serverlist.removeAll(serverlist);
                    serverlist.add(newServerName);
                    serversIP.put(newServerName, newServerIP);
                    count = count + 1; 
                }
            }
        } catch( Exception e ) { } 
    }

}
