package partychat;

import java.net.InetAddress;

public class Message {
    char OPCode;
    InetAddress senderIP;
    String content;

    public void setOP(char op) {
        this.OPCode = op;
    }

    public void setSender(InetAddress sender) {
        this.senderIP = sender;
    }

    public void setMessage(String msg) {
        this.content = msg; 
    }

    public char getOP() {
        return this.OPCode;
    }

    public InetAddress getSenderIP() {
        return this.senderIP;
    }

    public String getMessage() {
        return this.content;
    }
}
