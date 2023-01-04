package partychat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ControlClient extends ControlClass {
    
    Socket connection;
    InetAddress serverIP;
    BufferedReader socketIn;
    BufferedWriter socketOut;
    ClientChat associatedChat;

    public ControlClient(InetAddress serverAddress) throws IOException {
        this.serverIP = serverAddress;
        connection = new Socket(serverIP, DEFAULT_PORT);
        socketIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        socketOut = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
    }

    public int createConnection(String userName, String password) throws IOException {
        socketOut.write(OPCodes.CONNECTION_REQUEST + userName+"||"+password);
        String response = socketIn.readLine();
        if( response.equals((OPCodes.CLIENT_ACCEPTED+"")) ) {
            return 1;
        } else if ( response.equals(OPCodes.CLIENT_DENIED+"") ) { 
            return -1;
        } else {
            return 0;
        }
    }

    public void sendMessage(String Message) {
        try {
            socketOut.write(OPCodes.CHAT_ROOM_MESSAGE+Message+'\n');
        } catch ( IOException e ) { }  
    }

    public void recieveMessages() throws IOException {
        String inboundMessage;
        while ( (inboundMessage = socketIn.readLine()) != null ) {
            associatedChat.chatRoom.append(inboundMessage+'\n');
        }
    }

    public void terminateConnection() {
        try {
            socketOut.write(OPCodes.CLIENT_CONNECTION_TERMINATION+"");
            socketIn.close();
            socketOut.close();
            connection.close();
        } catch ( IOException e ) { }    
    }
}
