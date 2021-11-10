package id.co.emobile.samba.web.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import id.co.emobile.samba.web.entity.HistoryWithdraw;

@Service
public class SendEmailService {
	private static final Logger LOG = LoggerFactory.getLogger(SendEmailService.class);

	protected Logger getLogger() {
		return LOG;
	}

	public void sendEmail(HistoryWithdraw historyWithdraw, String subject, String emailTo,String name, String statusValue) throws SambaWebException {
		getLogger().info("send email notification " + historyWithdraw);
		String userName = name;
		String status = statusValue;
		String amount = "$" + Double.toString(historyWithdraw.getAmount());
		// Recipient's email ID needs to be mentioned.
		String to = emailTo;

		// Sender's email ID needs to be mentioned
		String from = "admin@nabungdividen.com";
		final String username = "admin@nabungdividen.com";// change accordingly
		final String password = "r4H4s14181014NABUNGDIVIDEN";// change accordingly

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp.hostinger.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.ssl.enable", true);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "465");

		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Send the actual HTML message, as big as you like
//			message.setContent("<h1>This is actual message embedded in HTML tags</h1>", "text/html");
			message.setContent(
					"<h1 style=\"text-align: center;\"><img src=\"https://nabungdividen.com/wp-content/uploads/2021/06/cropped-logo-final-1.png\" alt=\"\" /></h1>\r\n"
							+ "<h1 style=\"text-align: center;\">Withdrawal Request Received</h1>\r\n"
							+ "<p>Dear, <strong>"+userName+"</strong></p>\r\n"
							+ "<p>Your withdrawal request of your commission has been processed:</p>\r\n"
							+ "<p><strong>Status: " + status + "</strong></p>\r\n" + "<p><strong>Amount : " + amount
							+ "</strong></p>\r\n" + "<p>&nbsp;</p>\r\n"
							+ "<p><strong>Best Regards,</strong><br /><strong>Nabungdividen Team.</strong></p>",
					"text/html");

			// Send message
			Transport.send(message);

			getLogger().info("Sent message successfully....");

		} catch (Exception e) {
			e.printStackTrace();
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT);
		}
	}
}
