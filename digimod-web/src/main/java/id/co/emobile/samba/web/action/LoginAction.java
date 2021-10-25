package id.co.emobile.samba.web.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;

import id.co.emobile.samba.web.data.LoginData;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.SecurityService;
import id.co.emobile.samba.web.servlet.Captcha;
import id.co.emobile.samba.web.utils.CommonUtil;

public class LoginAction extends BaseAction implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(LoginAction.class);
	public static final String SESSION_TOKEN = "session_token";

	private Random r = new Random();
	private int captchaHeight = 30;
	private int captchaWidth = 120;
	private InputStream imageStream;
	
	private LoginData loginData;
	private HttpServletRequest httpRequest;
	private String message;

	private String token;
	
	@Autowired
	private SecurityService securityService;

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	public LoginData getLoginData() {
		return loginData;
	}
	public void setLoginData(LoginData loginData) {
		this.loginData = loginData;
	}

	@Override
	public String execute() {
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY); 
		if(loginVO!=null)
		{
			return SUCCESS;			
		}
		return INPUT;
	}

	public String process() {
		LOG.debug("Process user {}, token {}", loginData, token);
		try {
			String originalToken = (String) session.get(SESSION_TOKEN);	
			if (originalToken == null || originalToken.trim().equals("")) {
				LOG.warn("Missing originalToken in session. Perhaps the session is timeout");
				throw new SambaWebException(SambaWebException.NE_WRONG_CAPTCHA);
			}
			if(!originalToken.equals(token))
			{
				setMessage(getText("rc." + SambaWebException.NE_METHOD_NOT_ALLOWED));
				LOG.warn("Direct URL Hit detected, Token Server : " + originalToken + ", Token From Client : " + token);
				return INPUT;
			}
			validateCaptcha();
			HttpServletRequest request = ServletActionContext.getRequest();
			String ipAddress = request.getRemoteAddr();
			String sessionId = httpRequest.getSession().getId();
			UserDataLoginVO loginVO = securityService.validateUserLogin(loginData, sessionId, ipAddress);
			Locale localeID = CommonUtil.localeFinder(loginVO.getUserPreference().getLanguage());
			session.put(LOGIN_KEY, loginVO);
			session.put(WEB_LOCALE_KEY, localeID);
			return SUCCESS;
		} catch (SambaWebException fae) {
			//LOG.warn("SambaWebException", fae);
			WebResultVO wrv = handleJsonException(fae);
			setMessage(wrv.getMessage());
		} catch (Exception e) {
			LOG.warn("Exception", e);
			WebResultVO wrv = handleJsonException(e);
			setMessage(wrv.getMessage());
		}
		return INPUT;
	}

	public String mainMenu() {
		return SUCCESS;
	}

	public String logoff() {
		UserDataLoginVO loginVO = (UserDataLoginVO) session.remove(LOGIN_KEY);
		session.clear();
		LOG.debug("Logoff: " + (loginVO == null? "N/A" : loginVO.getUserCode()) );
		securityService.logoutUser(loginVO);
		return SUCCESS;
	}

	public String expired() {
		this.addActionError(getText("rc." +  SambaWebException.NE_SESSION_EXPIRED));
		message = getText("rc." +  SambaWebException.NE_SESSION_EXPIRED);
		return INPUT;
	}

	public String invalidModule()
	{
		addActionError(getText("err.invalidUserRole"));
		return SUCCESS;
	}

	public String processCaptcha() {
		try {
			BufferedImage image = new BufferedImage(captchaWidth, captchaHeight, BufferedImage.TYPE_INT_RGB); 
			Graphics2D graphics2D = image.createGraphics();
			String token = Long.toString(Math.abs(r.nextLong()), 36);
			String ch = token.substring(0,6);
			Color c = new Color(0.6662f, 0.4569f, 0.3232f);
			GradientPaint gp = new GradientPaint(30, 30, c, 15, 25, Color.white, true);
			graphics2D.setPaint(gp);
			Font font=new Font("Verdana", Font.CENTER_BASELINE , 26);
			graphics2D.setFont(font);
			graphics2D.drawString(ch,2,20);
			graphics2D.dispose();
			session.put(Captcha.CAPTCHA_KEY, ch);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "jpeg", outputStream);
			imageStream = new ByteArrayInputStream(outputStream.toByteArray());
			outputStream.close();
		} catch (Exception e) {
			LOG.warn("Exception when processCaptcha", e);
		}
		return SUCCESS;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request)  {
		this.httpRequest = request;
	}

	public String goToContact()
	{
		return SUCCESS;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private void validateCaptcha() throws SambaWebException{
		HttpServletRequest request  = (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		
		javax.servlet.http.HttpSession session = request.getSession();
		String parm = request.getParameter("j_captcha_response");
		String c= (String)session.getAttribute(Captcha.CAPTCHA_KEY) ;
		if(!parm.equals(c) ){
			LOG.warn("Invalid captcha {} with {}", parm, c);
			throw new SambaWebException(SambaWebException.NE_WRONG_CAPTCHA);
		}
	}

	public String getToken() {
		createToken();
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	private void createToken()
	{
		token = CommonUtil.tokenGenerator();
		session.put(SESSION_TOKEN, token);
	}
	
	public InputStream getImageStream() {
		return imageStream;
	}
}