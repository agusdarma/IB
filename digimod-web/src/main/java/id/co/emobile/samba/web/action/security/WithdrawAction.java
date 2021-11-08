package id.co.emobile.samba.web.action.security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.action.BaseListAction;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.BankParamVO;
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.HistoryWithdrawService;
import id.co.emobile.samba.web.service.SambaWebException;

public class WithdrawAction extends BaseListAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(WithdrawAction.class);

	@Autowired
	private HistoryWithdrawService historyWithdrawService;

	private WebResultVO wrv;

	private String message;
	private String json;
	private String amount;

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	// INITIATION AND PROCESS AREA
	@Override
	public String execute() {
		setMessage(getFlashMessage());
		return INPUT;
	}

	public String gotoInput() {
		return INPUT;
	}

	public String gotoSearch() {
		return SEARCH;
	}

	public String processInput() {
		getLogger().debug("processing: process input() " + amount);
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		Locale language = (Locale) session.get(WEB_LOCALE_KEY);
		try {
			wrv = historyWithdrawService.withdrawing(amount, loginVO, language);
			if (wrv.getType() == WebConstants.TYPE_UPDATE) {
				setFlashMessage(wrv.getMessage());
			}
		} catch (SambaWebException gwe) {
			wrv = handleJsonException(gwe);
		} catch (Exception e) {
			wrv = handleJsonException(e);
		}
		try {
			json = objectMapper.writeValueAsString(wrv);
		} catch (Exception e) {
			LOG.warn("Exception in serializing " + wrv, e);
		}
		return "inputJson";
	}

	@Override
	public int getMenuId() {
		return WebModules.MODULE_SECURITY_WITHDRAW;
	}

	// END SETTER GETTER AREA
	public InputStream getWrv() {
		return new ByteArrayInputStream(json.toString().getBytes());
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void setWrv(WebResultVO wrv) {
		this.wrv = wrv;
	}

	/**************************************
	 * ESSENTIAL FOR SEARCH
	 *******************************************/
	private String resultSearchJson;

	@Override
	public ParamPagingVO getParamVO() {
		if (paramVO == null)
			paramVO = new BankParamVO();
		return paramVO;
	}

	public InputStream getWsr() {
		return new ByteArrayInputStream(resultSearchJson.toString().getBytes());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
