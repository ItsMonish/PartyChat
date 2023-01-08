package partychat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ControlClient extends ControlClass {
    
    Socket connection;
    InetAddress serverIP;
    BufferedReader socketIn;
    PrintWriter socketOut;
    ClientChat associatedChat;
    boolean CLIENT_FLAG;

    public ControlClient(InetAddress serverAddress) throws IOException {
        this.serverIP = serverAddress;
        CLIENT_FLAG = true;
        connection = new Socket(serverIP, DEFAULT_PORT);
        socketIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        socketOut = new PrintWriter(connection.getOutputStream(),true);
    }

    public int createConnection(String userName, String password) throws IOException {
        socketOut.println(OPCodes.CONNECTION_REQUEST + userName+"::"+password);
        System.out.println("wrote to server buffer");
        String response = socketIn.readLine();
        System.out.println("Response recieved is :"+response);
        if( response.equals((OPCodes.CLIENT_ACCEPTED+"")) ) {
            return 1;
        } else if ( response.equals(OPCodes.CLIENT_DENIED+"") ) { 
            return -1;
        } else {
            return 0;
        }
    }

    public void sendMessage(String Message) {
        socketOut.println(OPCodes.CHAT_ROOM_MESSAGE+Message);
    }

    public void recieveMessages() {
        while(CLIENT_FLAG) {
            String inboundMessage;
            try {
                inboundMessage = socketIn.readLine();
                System.out.println("Recieved:"+inboundMessage);
                associatedChat.chatRoom.append(inboundMessage+"\n");
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void terminateConnection() {
        try {
            socketOut.write(OPCodes.CLIENT_CONNECTION_TERMINATION+"");
            socketIn.close();
            socketOut.close();
            connection.close();
        } catch ( IOException e ) { e.printStackTrace(); }    
    }
}
