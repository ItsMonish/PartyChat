package partychat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMethods extends Thread{
    Socket thisSocket;
    BufferedReader serverIn;
    BufferedWriter serverOut;
    ArrayList<Message> buffer;
    ArrayList<String> OutboundBuffer;
    InetAddress associatedIP;

    public ServerMethods(Socket socket, ArrayList<Message> msgBuffer) throws IOException {
        this.thisSocket = socket;
        this.buffer = msgBuffer;
        this.OutboundBuffer = new ArrayList<String>();
        associatedIP = socket.getInetAddress();
        serverIn = new BufferedReader(new InputStreamReader(thisSocket.getInputStream()));
        serverOut = new BufferedWriter(new OutputStreamWriter(thisSocket.getOutputStream()));
    }

    public void run() {
        try {
            while(true) {
                String InboundMessage;
                if((InboundMessage = serverIn.readLine()) != null){
                    Message newmsg = new Message();
                    newmsg.setOP(InboundMessage.charAt(0));
                    newmsg.setSender(associatedIP);
                    newmsg.setMessage(InboundMessage.substring(1));
                    buffer.add(newmsg);
                }
                if ( !OutboundBuffer.isEmpty() ) {
                    serverOut.write(OutboundBuffer.remove(0)+'\n');
                }
                if ( Thread.currentThread().isInterrupted() ) {
                    serverIn.close();
                    serverOut.close();
                    thisSocket.close();
                    break;
                }
            }
        } catch (IOException errIO) {
            errIO.printStackTrace();
        }
    }
}
