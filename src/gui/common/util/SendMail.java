package gui.common.util;

//set CLASSPATH=%CLASSPATH%;activation.jar;mail.jar

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

 
public class SendMail {
	
    public static void main(String[] args) throws Exception {
    	
    	//TestUtil.zip(System.getProperty("user.dir")+"\\XSLT_Reports\\output");
    	boolean emailWorking = false;
    	
    	String report_source_folder = System.getProperty("user.dir")+"\\XSLT_Reports";
    	String destination_zip_file = System.getProperty("user.dir")+"\\email_XSLT_Reports.zip";
    	
    	Zip.zipFolder(report_source_folder, destination_zip_file);
    	
    	System.out.println("************************************************************");
    	System.out.println("*** Zipped the XSLT Reports. Use the zip file to send email.");
    	System.out.println("*** Source Folder : " + report_source_folder);
    	System.out.println("*** Zip File      : " + destination_zip_file);
    	System.out.println("************************************************************");
    	
    	if(!emailWorking) {
    		System.out.println("Automatic Email sending NOT working as of now.");
    		System.out.println("Use the above zip file to send email manually.");
    		return;
    	}
    	
    	//*********** USE BELOW CODE WHEN EMAIL STARTS WORKING ****************
    	
        String[] to={"selenium.hpsum@gmail.com"};
        String[] cc={};
        String[] bcc={};

        //This is for google
      //"selenium.hpsum@gmail.com",
        SendMail.sendMail("ajay.ksingh@hp.com",
        		            "webdriver",
        		            "smtp.gmail.com",
        		            "465",
        		            "true",
        		            "true",
        		            true,
        		            "javax.net.ssl.SSLSocketFactory",
        		            "false",
        		            to,
        		            cc,
        		            bcc,
        		            "Automation test Reports",
        		            "Please find the reports attached.\n\n Regards\nWebMaster",
        		        	System.getProperty("user.dir")+"\\email_XSLT_Reports.zip",
        		        	"Reports.zip");
    }

 

    public  static boolean sendMail(String userName,
					        		String passWord,
					        		String host,
					        		String port,
					        		String starttls,
					        		String auth,
					        		boolean debug,
					        		String socketFactoryClass,
					        		String fallback,
					        		String[] to,
					        		String[] cc,
					        		String[] bcc,
					        		String subject,
					        		String text,
					        		String attachmentPath,
					        		String attachmentName) {
    	
		Properties props = new Properties();
		//Properties props=System.getProperties();
	
	    props.put("mail.smtp.user", userName);
	    props.put("mail.smtp.host", host);
	
		if(!"".equals(port))
			props.put("mail.smtp.port", port);
	
	    if(!"".equals(starttls))
	    	props.put("mail.smtp.starttls.enable",starttls);
	
	    props.put("mail.smtp.auth", auth);
	   // props.put("mail.smtps.auth", "true");
	
	
	    if(debug) {
		    props.put("mail.smtp.debug", "true");
		}
	    else {
	    	props.put("mail.smtp.debug", "false");
	    }
		
	    if(!"".equals(port))
	    	props.put("mail.smtp.socketFactory.port", port);
	
		if(!"".equals(socketFactoryClass))
			props.put("mail.smtp.socketFactory.class",socketFactoryClass);
	
		if(!"".equals(fallback))
			props.put("mail.smtp.socketFactory.fallback", fallback);
		
        try {

        	Session session = Session.getDefaultInstance(props, null);
            session.setDebug(debug);
            
            MimeMessage msg = new MimeMessage(session);
            msg.setText(text);
            msg.setSubject(subject);
            
            //attachment start
            
            // create the message part
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            
            DataSource source = new FileDataSource(attachmentPath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(attachmentName);
            multipart.addBodyPart(messageBodyPart);
            
            // attachment ends

            // Put parts in message
            msg.setContent(multipart);
            msg.setFrom(new InternetAddress("selenium.hpsum@gmail.com"));

            for(int i=0 ; i<to.length ; i++){
            	msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
            }

            for(int i=0;i<cc.length;i++){
            	msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
            }

            for(int i=0;i<bcc.length;i++){
            	msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
            }

            msg.saveChanges();

            Transport transport = session.getTransport("smtp");
            transport.connect(host, userName, passWord);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
    
}

