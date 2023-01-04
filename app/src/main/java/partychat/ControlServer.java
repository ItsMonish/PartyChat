package partychat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ControlServer extends ControlClass {

    boolean SERVER_FLAG;
    private NetworkInterface interfaceUsing;
    private InetAddress serverIP;
    int clientCount;
    ServerSocket server;
    Socket serverSocket;
    HashMap<InetAddress,String> connectedClients;
    ArrayList<ServerMethods> clientThreads;
    ArrayList<Message> messageBuffer;
    ServerChat associatedChat;
    DiscoveryServer discoveryInstance;

    public void initAction() throws IOException {
        connectedClients = new HashMap<InetAddress,String>();
        clientThreads = new ArrayList<ServerMethods>();
        messageBuffer = new ArrayList<Message>();
        discoveryInstance = new DiscoveryServer();
        SERVER_FLAG = true;
        clientCount = 0;
        serverIP = ((InterfaceAddress)interfaceUsing.getInetAddresses()).getAddress();
        server = new ServerSocket();
        server.bind(new InetSocketAddress(serverIP, DEFAULT_PORT));
        discoveryInstance.run();
        while(SERVER_FLAG){
            serverSocket = server.accept();
            ServerMethods clientThread = new ServerMethods(serverSocket,messageBuffer);
            clientThreads.add(clientThread);
            clientThread.setName(serverSocket.getInetAddress().toString()+"client");
            clientThread.start();
            clientCount = clientCount + 1;
        }
    }

    public void actionInbounds() throws IOException {
        while(SERVER_FLAG) {
            if( !messageBuffer.isEmpty() ) {
                Message newMsg = messageBuffer.remove(0);
                char thisOP = newMsg.getOP();
                String thisSender = newMsg.getSenderIP().toString();
                String Message = newMsg.getMessage();
                if(thisOP == OPCodes.CHAT_ROOM_MESSAGE) broadCastToChatRoom(thisSender, Message);
                else if(thisOP == OPCodes.CONNECTION_REQUEST) handleConnectionRequest(thisSender, Message);
                else if(thisOP == OPCodes.CLIENT_CONNECTION_TERMINATION) handleConnectionTermination(thisSender, Message);
            }
        }
    }

    public void broadCastToChatRoom(String sender, String message) {
         try {
            String toSendMessage;
            if (sender.equals("")) toSendMessage = "[ " + getUserName() + " ] > " + message + "\n" ;
            toSendMessage = "[ " + connectedClients.get(InetAddress.getByName(sender)) + " ] > " + message + "\n" ;
            for ( ServerMethods client : clientThreads ) {
                client.OutboundBuffer.add(toSendMessage);
            }
            associatedChat.chatBox.append(toSendMessage+'\n');
        } catch ( IOException e ) { }
    }

    public void handleConnectionRequest(String senderIP, String message) {
        try {
            String[] contents = message.split("||");
            String userName = contents[0];
            String password = contents[1];
            InetAddress requestIP = InetAddress.getByName(senderIP);
            if(password.equals(getServerPassword())) {
                for( ServerMethods client : clientThreads ) {
                    if(client.getName().equals(senderIP+"client")) client.OutboundBuffer.add(OPCodes.CLIENT_DENIED+"\n");
                    client.interrupt();
                    return;
                }
            }
            connectedClients.put(requestIP, userName);
            for( ServerMethods client : clientThreads ) {
                if(client.getName().equals(senderIP+"client")) client.OutboundBuffer.add(OPCodes.CLIENT_ACCEPTED+"\n");
            }
            broadCastToChatRoom("","[ "+ getServerName() +" ] > "+userName+" joined the chat room...");
            associatedChat.chatBox.append("[ "+ getServerName() +" ] > "+userName+" joined the chat room..." + '\n');
        } catch ( IOException e ) { }
    }

    public void handleConnectionTermination(String senderIP, String userName) throws IOException {
        InetAddress requestIP = InetAddress.getByName(senderIP);
        for( ServerMethods client : clientThreads ) {
            if( client.getName().equals(requestIP+"client") ) client.interrupt();
        }
        connectedClients.remove(requestIP, userName);
        broadCastToChatRoom("", "[ " + getServerName() + " ] > "+ userName+ " has left the chat room");
        associatedChat.chatBox.append("[ "+ getServerName() +" ] > "+userName+" has left the chat room..." + '\n');
    }

    public void terminateServer() {
        try {
            Thread.sleep(3000);
            SERVER_FLAG = false;
            serverSocket.close();
            server.close();
        } catch ( Exception e ) { }
    }

    public void setInterface(NetworkInterface newInterface) {
        this.interfaceUsing = newInterface;
    }

}
