package partychat;

public class ControlClass {
    static int SERVER_USER = 0;
    static int CLIENT_USER = 1;
    static int DEFAULT_PORT = 2023;
    private int mode;
    private String userName;
    private String serverName;
    private String password;
    
    public int getUserMode() {
        return mode;
    }

    public String getServerPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerPassword(String pass) {
        this.password = pass;
    }

    public void setUserServer() {
        this.mode = SERVER_USER;
    }

    public void setUserClient() {
        this.mode = CLIENT_USER;
    }

    public void setUserName(String newUserName) {
        this.userName = newUserName;
    }

    public void setServerName(String newServerName) {
        this.serverName = newServerName;
    }

}
