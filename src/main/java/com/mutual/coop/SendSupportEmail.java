package com.mutual.coop;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mutual.common.DbConstant;
import mutual.common.GenerateNotificationTemplete;
import mutual.common.JSFBoundleProvider;
import mutual.common.JSFMessagers;

/**
 * Servlet implementation class SendSupportEmail
 */
public class SendSupportEmail extends HttpServlet implements DbConstant {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "SendSupportEmail :: ";

	/* to manage validation messages */
	private boolean isValid;

	/* class injection */
	GenerateNotificationTemplete gen = new GenerateNotificationTemplete();
	JSFBoundleProvider provider = new JSFBoundleProvider();

	/* end class injection */
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fname = request.getParameter("name");
		String lname = request.getParameter("surname");
		String email = request.getParameter("email");
		String need = request.getParameter("need");
		String message = request.getParameter("message");

		sendMailTest(fname, lname, email, need, message);

		LOGGER.info(CLASSNAME + ":::notification sent to Support team");
		response.sendRedirect("default.xhtml?msg=Your request sent successfully..");

	}

	public void sendMailTest(String fname, String lname, String email, String need, String msgContent) {

		String msg = "<p>Please take look on the bellow request.</p>" + "<table width=\"50%\" border=\"5px\">\n"
				+ "  <tbody>\n" + "	<tr>" + "      <td class=\"labelbold\">Custome Names</td>\n" + "      <td>\n"
				+ "		  " + fname + " " + lname + "\n" + "	  </td>\n" + "    </tr>" + "	<tr>\n"
				+ "      <td class=\"labelbold\">Customer Email</td>\n" + "      <td>\n" + "		  " + email + "\n"
				+ "	  </td></tr>" + "	<tr>" + "      <td class=\"labelbold\">Customer Need</td>\n" + "      <td>\n"
				+ "		  " + need + "\n" + "	  </td></tr>"

				+ "	<tr>" + "      <td class=\"labelbold\">Customer Message</td>\n" + "      <td>\n" + "		  "
				+ msgContent + "\n" + "	  </td></tr>" + "<tr>" + "      <td class=\"labelbold\">Application URL</td>\n"
				+ "      <td> <a href='http://localhost:8080/vfpProject_v1/default.xhtml'>click here to acces the service</a>  </td></tr>"
				+ "  </tbody>\n" + "</table>\n";
		/* End send content in table sample */
		try {
			gen.sendEmailNotification("ngangoju@gmail.com", "Support Team ", need, msg);
		} catch (AddressException e) {
			setValid(false);
			e.printStackTrace();
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.emailerror"));
			e.printStackTrace();
		} catch (MessagingException e) {
			setValid(false);
			e.printStackTrace();
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.notificationError"));
			e.printStackTrace();
		}
		LOGGER.info("::: notidficatio sent   ");
	}

	public boolean sendMailStrategicPlan(String type, String fname, String senderName, String email) {

		boolean valid = false;
		if ((null != fname) && (null != email) && (null != senderName)) {
			String msg = null;
			if (type.equals("plan")) {
				msg = "<p>" + "I hope this email finds you well." + "<br/>"
						+ "This is to notify you the new strategic plan created, " + "<br/>"
						+ "you can now check it out and find the attached document with full details." + "<br/>"
						+ "</p>";
			} else if (type.equals("task")) {
				msg = "<p>" + "I hope this email finds you well." + "<br/>"
						+ "This is to notify you the new target created by your board superviser, " + "<br/>"
						+ "you can now check it out and start to create weekly activities." + "<br/>" + "</p>";
			}
			/* End send content in table sample */
			try {

				gen.sendEmailNotification(email, fname, "Strategic plan", msg);
				valid = true;
			} catch (AddressException e) {
				LOGGER.info("returing false1");
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.emailerror"));
				e.printStackTrace();
				LOGGER.info("returing false2");
				LOGGER.info("This content" + msg + " was not send to MY BE wrong address check email ::" + email
						+ " on " + timestamp);
			} catch (MessagingException e) {
				LOGGER.info("This content" + msg + " was not send to ::" + email + " on " + timestamp);
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.notificationError"));
				e.printStackTrace();
			}
			LOGGER.info("::: notidficatio sent   ");
		} else {
			valid = false;
		}
		LOGGER.info("returing values" + valid);
		return (valid);
	}

	public boolean sendMailMutualCoopRep(String type,String receivername, String givenpswd, String senderName, String email) {

		boolean valid = false;
		if (( null !=receivername && null != givenpswd) && (null != email) && (null != senderName)) {
			String msg = null;
			if (type.equals("accepted")) {
				msg = "<p>" + "I hope this email finds you well." + "<br/>"
						+ "This is to notify you that your request has been accepted, " + "<br/>"
						+ "now you can access our system with the given credentials." + "<br/>"
						+ "Your username." +email+ "<br/>"
						+ "Your password." +givenpswd+ "<br/>"
						+ "___________" +"<br/>"
						+ "Best Regards." + "<br/>"
						+ "Support Team." + "<br/>"
						+ "</p>";
				
			}
			/* End send content in table sample */
			try {

				gen.sendEmailNotification(email, receivername, "MC.Registration", msg);
				valid = true;
			} catch (AddressException e) {
				LOGGER.info("returing false1");
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.emailerror"));
				e.printStackTrace();
				LOGGER.info("returing false2");
				LOGGER.info("This content" + msg + " was not send to MY BE wrong address check email ::" + email
						+ " on " + timestamp);
			} catch (MessagingException e) {
				LOGGER.info("This content" + msg + " was not send to ::" + email + " on " + timestamp);
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.notificationError"));
				e.printStackTrace();
			}
			LOGGER.info("::: notidficatio sent   ");
		} else {
			valid = false;
		}
		LOGGER.info("returing values" + valid);
		return (valid);
	}
	
	
	public boolean sendMailGuestReq(String type,String receivername,String senderName, String email) {

		boolean valid = false;
		if (( null !=receivername && null != email && null != senderName &&null!=type)) {
			String msg = null;
			if (type.equals("accepted")) {
				msg = "<p>" + "I hope this email finds you well." + "<br/>"
						+ "This is to notify you that your request has been accepted, " + "<br/>"
						+ "now you can access our system with the given link." + "<br/>"
						+ "<a href="+LINK+ "Mutual-cooperative/home.xhtml>click here to acces the service</a>"+"<br/>"
						+ "</p>";
				
			}
			/* End send content in table sample */
			try {

				gen.sendEmailNotification(email, receivername, "MC.Registration", msg);
				valid = true;
			} catch (AddressException e) {
				LOGGER.info("returing false1");
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.emailerror"));
				e.printStackTrace();
				LOGGER.info("returing false2");
				LOGGER.info("This content" + msg + " was not send to MY BE wrong address check email ::" + email
						+ " on " + timestamp);
			} catch (MessagingException e) {
				LOGGER.info("This content" + msg + " was not send to ::" + email + " on " + timestamp);
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.notificationError"));
				e.printStackTrace();
			}
			LOGGER.info("::: notidficatio sent   ");
		} else {
			valid = false;
		}
		LOGGER.info("returing values" + valid);
		return (valid);
	}
	public boolean sendMailTestVersion(String fname, String lname, String email) {

		boolean valid = false;
		if ((null != fname) && (null != lname) && (null != email)) {
			String msg = "<p>Please take look on the bellow request.</p>" + "<table width=\"50%\" border=\"5px\">\n"
					+ "  <tbody>\n" + "	<tr>" + "      <td class=\"labelbold\">Custome Names</td>\n" + "      <td>\n"
					+ "		  " + fname + " " + lname + "\n" + "	  </td>\n" + "    </tr>" + "	<tr>\n"
					+ "      <td class=\"labelbold\">Customer Email</td>\n" + "      <td>\n" + "		  " + email
					+ "\n" + "	  </td></tr>"

					+ "<tr>" + "      <td class=\"labelbold\">Application URL</td>\n" + "      <td> <a href=" + LINK
					+ "vfpProject_v1/default.xhtml>click here to acces the service</a>  </td></tr>" + "  </tbody>\n"
					+ "</table>\n";

			/* End send content in table sample */
			try {

				gen.sendEmailNotification(email, fname + " " + lname + "", "Support Team", msg);
				valid = true;
			} catch (AddressException e) {
				LOGGER.info("returing false1");
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.emailerror"));
				e.printStackTrace();
				LOGGER.info("returing false2");
				LOGGER.info("This content" + msg + " was not send to MY BE wrong address check email ::" + email
						+ " on " + timestamp);
			} catch (MessagingException e) {
				LOGGER.info("This content" + msg + " was not send to ::" + email + " on " + timestamp);
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.notificationError"));
				e.printStackTrace();
			}
			LOGGER.info("::: notidficatio sent   ");
		} else {
			valid = false;
		}
		LOGGER.info("returing values" + valid);
		return (valid);
	}

	public void sendMailForInstitution(String fname, String lname, String email, String need, String msgContent) {

		String msg = "<p>Please take look on the bellow request.</p>" + "<table width=\"50%\" border=\"5px\">\n"
				+ "  <tbody>\n" + "	<tr>" + "      <td class=\"labelbold\">Custome Names</td>\n" + "      <td>\n"
				+ "		  " + fname + " " + lname + "\n" + "	  </td>\n" + "    </tr>" + "	<tr>\n"
				+ "      <td class=\"labelbold\">Customer Email</td>\n" + "      <td>\n" + "		  " + email + "\n"
				+ "	  </td></tr>" + "	<tr>" + "      <td class=\"labelbold\">Customer Need</td>\n" + "      <td>\n"
				+ "		  " + need + "\n" + "	  </td></tr>"

				+ "	<tr>" + "      <td class=\"labelbold\">Customer Message</td>\n" + "      <td>\n" + "		  "
				+ msgContent + "\n" + "	  </td></tr>" + "<tr>" + "      <td class=\"labelbold\">Application URL</td>\n"
				+ "      <td> <a href='http://localhost:8080/vfpProject_v1/default.xhtml'>click here to acces the service</a>  </td></tr>"
				+ "  </tbody>\n" + "</table>\n";
		/* End send content in table sample */
		try {
			gen.sendEmailNotification(email, "Institution Registration Confirmation ", need, msg);
		} catch (AddressException e) {
			setValid(false);
			e.printStackTrace();
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.emailerror"));
			e.printStackTrace();
		} catch (MessagingException e) {
			setValid(false);
			e.printStackTrace();
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.notificationError"));
			e.printStackTrace();
		}
		LOGGER.info("::: notidficatio sent   ");
	}

	public boolean sendMailMutualMember(String type,String receivername, String amount,Date givenDate, String senderName, String email) {

		boolean valid = false;
		if (( null !=receivername && null != amount) && (null != givenDate) && (null != email) && (null != senderName)) {
			String msg = null;
			if (type.equals("account")) {
				msg = "<p>" + "I hope this email finds you well." + "<br/>"
						+ "This is to notify you that there is a change on your funds, " + "<br/>"
						+ " a change on the amount you provided we have update it with  the amount below." + "<br/>"
						+ "Amount provided on." +givenDate+ "<br/>"
						+ " Updated amount." +amount+ "<br/>"
						+ "___________" +"<br/>"
						+ "</p>";
				
			}
			/* End send content in table sample */
			try {

				gen.sendEmailNotification(email, receivername, "Account Updates", msg);
				valid = true;
			} catch (AddressException e) {
				LOGGER.info("returing false1");
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.emailerror"));
				e.printStackTrace();
				LOGGER.info("returing false2");
				LOGGER.info("This content" + msg + " was not send to MY BE wrong address check email ::" + email
						+ " on " + timestamp);
			} catch (MessagingException e) {
				LOGGER.info("This content" + msg + " was not send to ::" + email + " on " + timestamp);
				valid = false;
				setValid(false);
				e.printStackTrace();
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.notificationError"));
				e.printStackTrace();
			}
			LOGGER.info("::: notidficatio sent   ");
		} else {
			valid = false;
		}
		LOGGER.info("returing values" + valid);
		return (valid);
	}
	
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public JSFBoundleProvider getProvider() {
		return provider;
	}

	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
	}

}
