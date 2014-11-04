package EmailNow;
/**
 * Erik Kaasila
 */
import java.util.Properties;
import java.util.TimerTask;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

/**
 * Send the properly timed email updates
 * of the desktop
 */
public class EmailNow extends TimerTask {

  /**
   * Responds to requests to send emails and execute requested tasks
   */
  public void run() {
    SendFileEmail mail = new SendFileEmail();
    Properties props = new Properties();
    props.setProperty("mail.store.protocol", "imaps");
    try {
      Session session = Session.getInstance(props, null);
      Store store = session.getStore();
      store.connect("imap.gmail.com", SendFileEmail.sendEmail, SendFileEmail.sendPass);
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
}
