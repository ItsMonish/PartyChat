package partychat;

import java.net.SocketException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import com.formdev.flatlaf.FlatDarkLaf;

public class ClientConnection extends javax.swing.JFrame {


    public ClientConnection() {
        initComponents();
        serverPassword.setEnabled(false);
        jButton2.setEnabled(false);
        DISCOVERY_FLAG = true;
        try {
            discovery = new DiscoveryClient();
        } catch ( SocketException e ) { e.printStackTrace(); }
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listModel = new DefaultListModel<String>();
        serverLists = new javax.swing.JList<String>(listModel);
        jLabel2 = new javax.swing.JLabel();
        serverPassword = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        userName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client Connection");

        jLabel1.setFont(jLabel1.getFont());
        jLabel1.setText("Select a Server to Join In:");

        serverLists.setFixedCellWidth(200);
        serverLists.setVisibleRowCount(-1);
        serverLists.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                serverListsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(serverLists);

        jLabel2.setFont(jLabel2.getFont());
        jLabel2.setText("Enter Password:");

        serverPassword.setFont(serverPassword.getFont());
        serverPassword.setEchoChar('*');

        jButton1.setText("< Back");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Connect");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(jLabel3.getFont());
        jLabel3.setText("Enter UserName:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(userName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(20, 20, 20)
                                .addComponent(jButton2))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(serverPassword)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)))))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serverPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(18, 18, 18))
        );

        pack();
    }

    private void serverListsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        serverPassword.setEnabled(true);
        jButton2.setEnabled(true);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String selectedServer = serverLists.getSelectedValue();
            String password = new String(serverPassword.getPassword());
            String clientName = userName.getText();
            ControlClient newClient = new ControlClient(discovery.serversIP.get(selectedServer));
            newClient.setServerName(selectedServer);
            newClient.setServerPassword(password);
            newClient.setUserName(clientName);
            newClient.setUserClient();
            ClientChat newClientChat = new ClientChat(newClient);
            int res = newClient.createConnection(clientName,password);
            if(res == 1) {
                newClientChat.start();
                this.dispose();
            } else if (res == -1) {
                JOptionPane.showMessageDialog(this, "The connection was refused. Please check your password.");
            } else {
                JOptionPane.showMessageDialog(this, "No reponse from server. Make sure it is up and try again");
            }
        } catch( Exception e ) { e.printStackTrace(); }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        MainWindow obj = new MainWindow();
        obj.setVisible(true);
        DISCOVERY_FLAG = false;
        this.dispose();
    }

    public void start() {

        try {
            javax.swing.UIManager.setLookAndFeel(FlatDarkLaf.class.getName());
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if("WindowsClassic".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });

        Thread discoveryThread = new Thread(new Runnable() {
            @Override public void run() {
                discovery.run();
            }
        });

        new Thread(new Runnable() {
           @Override public void run() {
            while(DISCOVERY_FLAG) {
                listModel.removeAllElements();
                for(String name: discovery.serverlist) {
                    listModel.addElement(name);
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e ) { e.printStackTrace(); }
            }
           } 
        }).start();

        discoveryThread.start();
    }

    private boolean DISCOVERY_FLAG;
    DiscoveryClient discovery;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> serverLists;
    private javax.swing.JPasswordField serverPassword;
    private javax.swing.JTextField userName;
    private javax.swing.DefaultListModel<String> listModel;

}
