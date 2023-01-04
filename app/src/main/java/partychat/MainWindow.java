package partychat;
import com.formdev.flatlaf.FlatDarkLaf;

public class MainWindow extends javax.swing.JFrame {


    public MainWindow() {
        initComponents();
        nextbtn.setEnabled(false);
    }

    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        hostoption = new javax.swing.JRadioButton();
        connectoption = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        nextbtn = new javax.swing.JButton();
        closebtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(150, 150));
        
        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Welcome To PartyChat");

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD, 16));
        jLabel2.setText("Select Your Mode of Operation:");

        buttonGroup1.add(hostoption);
        hostoption.setFont(hostoption.getFont().deriveFont((float)14));
        hostoption.setText("Host A Server");
        hostoption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostoptionActionPerformed(evt);
            }
        });

        buttonGroup1.add(connectoption);
        connectoption.setFont(connectoption.getFont().deriveFont((float)14));
        connectoption.setText("Connect to a Server");
        connectoption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectoptionActionPerformed(evt);
            }
        });

        jLabel3.setFont(jLabel3.getFont().deriveFont((float)13));
        jLabel3.setText("( You Should know the Name and Password of the chat )");

        nextbtn.setText("Next >");
        nextbtn.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                nextbtnActionPerformed(evt);
            }
        });
        closebtn.setText("Close");
        closebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(connectoption)
                    .addComponent(hostoption))
                .addGap(0, 39, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nextbtn)
                .addGap(18, 18, 18)
                .addComponent(closebtn)
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hostoption)
                .addGap(18, 18, 18)
                .addComponent(connectoption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextbtn)
                    .addComponent(closebtn))
                .addGap(22, 22, 22))
        );

        pack();
    }
    private void nextbtnActionPerformed(java.awt.event.ActionEvent evt){
        if( hostoption.isSelected() ){
            ServerConfig serverPage = new ServerConfig();
            serverPage.start();
        } else {
            ClientConnection clientPage = new ClientConnection();
            clientPage.start();
        }
        this.dispose();
    }

    private void hostoptionActionPerformed(java.awt.event.ActionEvent evt) {                                           
        nextbtn.setEnabled(true);
    }                                          

    private void connectoptionActionPerformed(java.awt.event.ActionEvent evt) {                                              
        nextbtn.setEnabled(true);
    }                                             

    private void closebtnActionPerformed(java.awt.event.ActionEvent evt) {                                         
        System.exit(0);
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
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton closebtn;
    private javax.swing.JRadioButton connectoption;
    private javax.swing.JRadioButton hostoption;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton nextbtn;
}
