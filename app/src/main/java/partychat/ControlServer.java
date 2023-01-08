package partychat;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

public class ControlServer extends ControlClass {

    boolean SERVER_FLAG;
    private NetworkInterface interfaceUsing;
    private InetAddress serverIP;
    private Thread discoveryThread;
    private Thread serverConnectionThread;
    int clientCount;
    ServerSocket server;
    Socket serverSocket;
    HashMap<InetAddress,String> connectedClients;
    HashMap<Thread,ServerMethods> clientThreads;
    ServerChat associatedChat;
    DiscoveryServer discoveryInstance;
    List<Message> messageBuffer = Collections.synchronizedList(new ArrayList<Message>());
    ControlServer self = this;

    public void initAction() {
        try {
            connectedClients = new HashMap<InetAddress,String>();
            clientThreads = new HashMap<Thread,ServerMethods>();
            discoveryInstance = new DiscoveryServer();
            SERVER_FLAG = true;
            clientCount = 0;
            Enumeration<InetAddress> addresses = interfaceUsing.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if( addr instanceof Inet6Address) continue;
                serverIP = addr;
            }
            server = new ServerSocket();
            server.bind(new InetSocketAddress(serverIP, DEFAULT_PORT));
            discoveryInstance.setDiscoveryInterface(interfaceUsing);
            discoveryInstance.setAssociatedServer(this);

            discoveryThread = new Thread(new Runnable() {
               public void run() {
                discoveryInstance.initDiscovery();
                if(Thread.interrupted()) {
                    discoveryInstance.DISCOVERY_FLAG = false;
                    discoveryInstance.discoverySocket.close();
                }
               } 
            });
            discoveryThread.start();
            serverConnectionThread = new Thread(new Runnable() {
               public void run() {
                    while(SERVER_FLAG){
                        try {
                            serverSocket = server.accept();
                            ServerMethods clientMethods = new ServerMethods(serverSocket,self);
                            Thread clientThread = new Thread(new Runnable() {
                                public void run() {
                                    Thread.currentThread().setName(serverSocket.getInetAddress().toString()+"client");
                                    clientMethods.initListening();
                                }
                            });
                            clientThreads.put(clientThread,clientMethods);
                            clientThread.start();
                            clientCount = clientCount + 1;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } 
            });
            serverConnectionThread.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void actionInbounds() {
        try {
            while(SERVER_FLAG) {
                if( messageBuffer.size() != 0 ) {
                    Message newMsg = messageBuffer.remove(0);
                    char thisOP = newMsg.getOP();
                    InetAddress thisSender = newMsg.getSenderIP();
                    String Message = newMsg.getMessage();
                    if(thisOP == OPCodes.CHAT_ROOM_MESSAGE) broadCastToChatRoom(thisSender, Message);
                    else if(thisOP == OPCodes.CONNECTION_REQUEST) handleConnectionRequest(thisSender, Message);
                    else if(thisOP == OPCodes.CLIENT_CONNECTION_TERMINATION) handleConnectionTermination(thisSender, Message);
                }
            }
        } catch ( Exception e ) { e.printStackTrace(); }
    }

    public void broadCastToChatRoom(InetAddress sender, String message) {
        String toSendMessage;
        try {
            if (sender == null) toSendMessage = "[ " + getUserName() + " ] > " + message + "\n" ;
            else if (sender == InetAddress.getByName("localhost")) toSendMessage = message + "\n";
            else toSendMessage = "[ " + connectedClients.get(sender) + " ] > " + message + "\n" ;
            for ( ServerMethods client : clientThreads.values() ) {
                client.serverOut.println(toSendMessage);
            }
            associatedChat.chatBox.append(toSendMessage);
        } catch ( UnknownHostException e) { e.printStackTrace(); }
    }

    public void handleConnectionRequest(InetAddress senderIP, String message) {
        System.out.println("Lets see..."+message);
        String[] contents = message.split("::");
        String userName = contents[0];
        String password = null;
        if( contents.length > 1 ) password = contents[1];
        if(password != null && !password.equals(getServerPassword())) {
            for( Thread client : clientThreads.keySet() ) {
                if(client.getName().equals(senderIP+"client")) clientThreads.get(client).serverOut.println(OPCodes.CLIENT_DENIED+"");
                client.interrupt();
                return;
            }
        }
        connectedClients.put(senderIP, userName);
        for( Thread client : clientThreads.keySet() ) {
            if(client.getName().equals(senderIP+"client")){ 
                clientThreads.get(client).serverOut.println(OPCodes.CLIENT_ACCEPTED+"");
            }
        }
        try {
            broadCastToChatRoom(InetAddress.getByName("localhost"),"[ "+ getServerName() +" ] > "+userName+" joined the chat room...");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void handleConnectionTermination(InetAddress senderIP, String userName) throws IOException {
        InetAddress requestIP = senderIP;
        for( Thread client : clientThreads.keySet() ) {
            if( client.getName().equals(requestIP+"client") ) client.interrupt();
        }
        connectedClients.remove(requestIP, userName);
        broadCastToChatRoom(null, "[ " + getServerName() + " ] > "+ userName+ " has left the chat room");
        associatedChat.chatBox.append("[ "+ getServerName() +" ] > "+userName+" has left the chat room..." + '\n');
    }

    public void terminateServer() {
        try {
            SERVER_FLAG = false;
            for ( Thread client : clientThreads.keySet() ) {
                client.interrupt();
            }
            Thread.sleep(3000);
            discoveryThread.interrupt();
            serverConnectionThread.interrupt();
            server.close();
        } catch ( Exception e ) { e.printStackTrace(); }
    }

    public void setInterface(NetworkInterface newInterface) {
        this.interfaceUsing = newInterface;
    }

}
