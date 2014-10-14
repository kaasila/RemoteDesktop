package EmailNow;
/**
 * @author Erik Kaasila
 */
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

public class SendFileEmail extends TimerTask {

  static String sendEmail = null;
  static String sendPass = null;
  static String receiveEmail = null;
  static int freq = 1;
  static boolean start = false;

  /**
   * Draft and send messages!
   */
  public void run() {
    Properties props = new Properties();
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
                new Authenticator() {
                  protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                      SendFileEmail.sendEmail, SendFileEmail.sendPass);
                    }
                });
    try {
      String
          trueStamp =
          new SimpleDateFormat("MM/dd/YYYY HH:mm:ss").format(Calendar.getInstance().getTime());
      String
          timeStamp =
          new SimpleDateFormat("MM_dd---HH_mm_ss").format(Calendar.getInstance().getTime());
      Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      BufferedImage capture = null;
      try {
        capture = new Robot().createScreenCapture(screenRect);
      } catch (AWTException e) {
        e.printStackTrace();
      }
      File file = new File(timeStamp + ".png");
      ImageIO.write(capture, "png", file);

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(sendEmail));
      message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(receiveEmail));
      message.setSubject("Desktop Update: " + trueStamp);
      message.setText("Here is an update!");

      MimeBodyPart messageBodyPart = new MimeBodyPart();
      Multipart multipart = new MimeMultipart();

      messageBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(file);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(timeStamp + ".png");
      multipart.addBodyPart(messageBodyPart);
      message.setContent(multipart);

      Transport.send(message);

      System.out.println("Done");
      file.delete();
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Connect to the email account, check for requests, act on those requests
   */
  public static void email() {
    SendFileEmail mail = new SendFileEmail();
    Properties props = new Properties();
    props.setProperty("mail.store.protocol", "imaps");
    try {
      Session session = Session.getInstance(props, null);
      Store store = session.getStore();
      store.connect("imap.gmail.com", sendEmail, sendPass);
      Folder inbox = store.getFolder("INBOX");
      inbox.open(2);
      Message[] msg = inbox.search(new FlagTerm(new Flags(
          Flags.Flag.SEEN), false));
      for (Message m : msg) {
        if (m.getSubject().equals("JAVA: END")) {
          Runtime rt = Runtime.getRuntime();
          rt.exec("taskkill /F /IM java.exe");
          MimeMessage source = (MimeMessage) m;
          new MimeMessage(source);
          endTask();
        }
        if (m.getSubject().equals("DESKTOP: STATUS")) {
          mail.run();
          MimeMessage source = (MimeMessage) m;
          new MimeMessage(source);
        }
      }
    } catch (Exception mex) {
      mex.printStackTrace();
    }
  }

  /**
   * Force quit this program
   */
  public static void endTask() {
    System.exit(0);
  }

  /**
   * Set the source email address
   * @param email source address
   */
  public void setSendEmail(String email) {
    sendEmail = email;
  }

  /**
   * Set the password for the source account
   *
   * @param pass source password
   */
  public void setSendPass(String pass) {
    sendPass = pass;
  }

  /**
   * Set the email address to send updats to
   *
   * @param email destination email
   */
  public void setToEmail(String email) {
    receiveEmail = email;
  }

  /**
   * Calculate the frequency
   *
   * @param i the frequency, in minutes
   */
  public void setFreq(int i) {
    freq = i * 60000;
  }

  /**
   * Set start
   * @param b the new value for start
   */
  public void start(boolean b) {
    start = b;
  }


  /**
   * Run the monitoring program
   *
   * @param args
   * @throws IOException
   * @throws AWTException
   * @throws InterruptedException
   */
  public static void main(String[] args)
      throws IOException, AWTException, InterruptedException {
    EmailGUI gui = new EmailGUI();
    gui.setBounds(0, 0, 270, 250);
    gui.setVisible(true);
    gui.setDefaultCloseOperation(3);

    while (!start) {
      Thread.sleep(1000L);
    }

    if (start) {
      Timer timer = new Timer();
      timer.schedule(new SendFileEmail(), 0L, freq);
      timer.schedule(new EmailNow(), 0L, 10000L);
    }
  }
}