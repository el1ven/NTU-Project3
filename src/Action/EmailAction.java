package Action;

import com.opensymphony.xwork2.ActionSupport;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by el1ven on 29/12/14.
 */
public class EmailAction extends ActionSupport {

    private String from;
    private String password;
    private String to;
    private String subject;
    private String body;

    static Properties properties = new Properties();
    static{
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
    }

    public String execute()throws Exception{
        String flag = "success";
        try{

            Session session = Session.getDefaultInstance(
                    properties, new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, password);
                        }
                    }
            );

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);

        }catch(Exception e){
            flag = "fail";
            e.printStackTrace();

        }

        return flag;
    }


    public void setFrom(String from) {this.from = from;}
    public String getFrom() {return from;}

    public void setPassword(String password) {this.password = password;}
    public String getPassword() {return password;}

    public void setTo(String to) {this.to = to;}
    public String getTo() {return to;}

    public void setSubject(String subject) {this.subject = subject;}
    public String getSubject() {return subject;}

    public void setBody(String body) {this.body = body;}
    public String getBody() {return body;}

    public static void setProperties(Properties properties) {EmailAction.properties = properties;}
    public static Properties getProperties() {return properties;}

}
