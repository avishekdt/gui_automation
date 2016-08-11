package test;

//File Name SendFileEmail.java

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendFileEmail
{
	public static void main(String [] args)
	{
	   
	   // Recipient's email ID needs to be mentioned.
	   //String to = "selenium.hpsum@gmail.com";
		String to = "ajay.ksingh@hp.com";
	
	   // Sender's email ID needs to be mentioned
	   //String from = "selenium.hpsum@gmail.com";
		String from = "ajay.ksingh@hp.com";
	
	   // Assuming you are sending email from localhost
	   //String host = "localhost";
	   String host = "casarray1.houston.hp.com";
	
	   // Get system properties
	   Properties properties = System.getProperties();
	
	   // Setup mail server
	   properties.setProperty("mail.smtp.host", host);
	   //casarray1.houston.hp.com
	   
	
	   // Get the default Session object.
	   Session session = Session.getDefaultInstance(properties);
	
	   try{
	      // Create a default MimeMessage object.
	      MimeMessage message = new MimeMessage(session);
	
	      // Set From: header field of the header.
	      message.setFrom(new InternetAddress(from));
	
	      // Set To: header field of the header.
	      message.addRecipient(Message.RecipientType.TO,
	                               new InternetAddress(to));
	
	      // Set Subject: header field
	      message.setSubject("This is the Subject Line!");
	
	      // Create the message part 
	      BodyPart messageBodyPart = new MimeBodyPart();
	
	      // Fill the message
	      messageBodyPart.setText("This is message body");
	      
	      // Create a multipar message
	      Multipart multipart = new MimeMultipart();
	
	      // Set text message part
	      multipart.addBodyPart(messageBodyPart);
	
	      // Part two is attachment
	      messageBodyPart = new MimeBodyPart();
	      String filename = "c:/file.txt";
	      DataSource source = new FileDataSource(filename);
	      messageBodyPart.setDataHandler(new DataHandler(source));
	      messageBodyPart.setFileName(filename);
	      multipart.addBodyPart(messageBodyPart);
	
	      // Send the complete message parts
	      message.setContent(multipart );
	
	      // Send message
	      Transport.send(message);
	      System.out.println("Sent message successfully....");
	   }catch (MessagingException mex) {
	      mex.printStackTrace();
	   }
	}
}
