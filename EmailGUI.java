package EmailNow;
/**
 * Erik Kaasila
 */
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 * GUI for the RemoteDesktop
 */
public class EmailGUI extends JFrame {

  private JTextField textField;
  private JTextField textField_1;
  private JTextField textField_2;

  public EmailGUI() {
    final SendFileEmail sfe = new SendFileEmail();
    getContentPane().setLayout(null);

    JLabel lblSendFromInformation = new JLabel("Send From Information");
    lblSendFromInformation.setBounds(10, 11, 165, 14);
    getContentPane().add(lblSendFromInformation);

    JLabel lblEmail = new JLabel("Email");
    lblEmail.setBounds(10, 27, 46, 14);
    getContentPane().add(lblEmail);

    this.textField = new JTextField();
    this.textField.setBounds(10, 40, 124, 20);
    getContentPane().add(this.textField);
    this.textField.setColumns(10);

    JLabel lblPassword = new JLabel("Password");
    lblPassword.setBounds(144, 27, 64, 14);
    getContentPane().add(lblPassword);

    this.textField_1 = new JTextField();
    this.textField_1.setBounds(144, 40, 108, 20);
    getContentPane().add(this.textField_1);
    this.textField_1.setColumns(10);

    JLabel lblSentToInformation = new JLabel("Send\r\n To Information");
    lblSentToInformation.setBounds(10, 71, 140, 14);
    getContentPane().add(lblSentToInformation);

    JLabel lblEmail_1 = new JLabel("Email");
    lblEmail_1.setBounds(10, 87, 46, 14);
    getContentPane().add(lblEmail_1);

    this.textField_2 = new JTextField();
    this.textField_2.setBounds(10, 100, 124, 20);
    getContentPane().add(this.textField_2);
    this.textField_2.setColumns(10);

    JLabel lblFrequency = new JLabel("Frequency");
    lblFrequency.setBounds(144, 87, 86, 14);
    getContentPane().add(lblFrequency);

    final JSpinner spinner = new JSpinner();
    spinner.setBounds(144, 100, 86, 20);
    getContentPane().add(spinner);

    JButton btnStartMonitoring = new JButton("Start Monitoring");
    btnStartMonitoring.setBounds(20, 159, 210, 23);
    getContentPane().add(btnStartMonitoring);

    JLabel lblStatus = new JLabel("Status:");
    lblStatus.setBounds(10, 124, 46, 14);
    getContentPane().add(lblStatus);

    final JLabel lblOffline = new JLabel("OFFLINE");
    lblOffline.setBounds(10, 141, 46, 14);
    getContentPane().add(lblOffline);

    btnStartMonitoring.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        System.out.println("ALL");
        sfe.setSendEmail(EmailGUI.this.textField.getText());
        sfe.setSendPass(EmailGUI.this.textField_1.getText());
        sfe.setToEmail(EmailGUI.this.textField_2.getText());
        sfe.setFreq(((Integer) spinner.getValue()).intValue());
        sfe.start(true);
        lblOffline.setText("ONLINE");
      }
    });
  }
}
