package partychat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ServerMethods{
    Socket thisSocket;
    BufferedReader serverIn;
    PrintWriter serverOut;
    InetAddress associatedIP;
    ControlServer associatedServer;

    public ServerMethods(Socket socket,ControlServer associated) throws IOException {
        this.thisSocket = socket;
        this.associatedServer = associated;
        associatedIP = socket.getInetAddress();
        serverIn = new BufferedReader(new InputStreamReader(thisSocket.getInputStream()));
        serverOut = new PrintWriter(thisSocket.getOutputStream(),true);
    }

    public void initListening() {
        try {
            while(true) {
                String InboundMessage;
                if((InboundMessage = serverIn.readLine()) != null) {
                    Message newmsg = new Message();
                    newmsg.setOP(InboundMessage.charAt(0));
                    newmsg.setSender(associatedIP);
                    newmsg.setMessage(InboundMessage.substring(1));
                    associatedServer.messageBuffer.add(newmsg);
                }
                if ( Thread.currentThread().isInterrupted() ) {
                    serverIn.close();
                    serverOut.close();
                    thisSocket.close();
                    break;
                }
            }
        } catch (IOException errIO) { }
    }
}