package id.co.emobile.samba.web.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.BankParamVO;
import id.co.emobile.samba.web.entity.Bank;
import id.co.emobile.samba.web.mapper.BankMapper;
import id.co.emobile.samba.web.utils.CommonUtil;

@Service
public class BankService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BankService.class);

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private BankMapper bankMapper;

	@Autowired
	private BizMessageService messageService;
	
	@Autowired
	private UserActivityService userActivityService;
	
//	@Autowired
//	private FtpService ftpService;
	
	private int wDimension = 128;
	private int hDimension = 128;
	
	public List<Bank> findAllBank()
	{
		try {
			List<Bank> listBank = bankMapper.findAllBank();
			return listBank;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return new ArrayList<Bank>();
		}
	}
	
	public List<Bank> findBankByParam(BankParamVO paramVO) {
		try {
			List<Bank> listBank = bankMapper.findBankByParam(paramVO);
			return listBank;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return new ArrayList<Bank>();
		}
	}

	public int countBankByParam(BankParamVO paramVO) {
		try {
			int count = bankMapper.countBankByParam(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return 0;
		}
	}

	public Bank findBankByBankCode(String bankCode) {
		try {
			Bank bank = bankMapper.findBankByBankCode(bankCode);
			return bank;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return new Bank();
		}
	}

	public Bank findBankById(int id) {
		try {
			Bank bank = bankMapper.findBankById(id);
			return bank;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return new Bank();
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public WebResultVO insertOrUpdateBank(Bank bank, UserDataLoginVO loginVO, Locale language, String extension) throws SambaWebException, IOException {
		try {
			WebResultVO wrv = new WebResultVO();
			bank.setCreatedBy(loginVO.getId());
			bank.setCreatedOn(timeService.getCurrentTime());
			bank.setUpdatedBy(loginVO.getId());
			bank.setUpdatedOn(timeService.getCurrentTime());
			
			if(StringUtils.isEmpty(bank.getBankCode())) {
				LOGGER.warn("Bank code is empty");
				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.bankCode")});
			}
			if(StringUtils.isEmpty(bank.getBankName())) {
				LOGGER.warn("Bank name is empty");
				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.bankName")});
			}
			if(StringUtils.isEmpty(bank.getBankInitial())) {
				LOGGER.warn("Bank initial is empty");
				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.bankInitial")});
			}
			if(StringUtils.isEmpty(bank.getBankUrl())) {
				LOGGER.warn("Bank url is empty");
				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.bankUrl")});
			}
			if(bank.getShowOrder()==0)
			{
				LOGGER.warn("Missing show order");
				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.showOrder")});
			}
			if(StringUtils.isEmpty(bank.getUssdCode()) && StringUtils.isEmpty(bank.getTpda()))
			{
				LOGGER.warn("Ussd code or tpda must be filled");
				throw new SambaWebException(SambaWebException.NE_TPDA_OR_USSDCODE_MUST_BE_FILLED,new String[] {messageService.getMessageFor("l.showOrder")});
			}
			if(bank.getId()==0)
			{
				if(bank.getImageLogo()==null)
				{
					LOGGER.warn("Bank logo is empty");
					throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.bankLogo")});
				}
				if(CommonUtil.checkImageExtension(extension)==false)
				{
					LOGGER.warn("uploaded file is not valid image file");
					throw new SambaWebException(SambaWebException.NE_NOT_A_VALID_IMAGE_FILE,new String[] {messageService.getMessageFor("l.bankLogo")});
				}
				BufferedImage bimg = ImageIO.read(bank.getImageLogo());
				int width          = bimg.getWidth();
				int height         = bimg.getHeight();
				if(width!=wDimension || height!=hDimension)
				{
					LOGGER.warn("display image dimension is not valid!");
					throw new SambaWebException(SambaWebException.NE_NOT_A_VALID_DIMENSION,new String[] {messageService.getMessageFor("l.bankLogo")});	
				}
				Bank bankcheck = bankMapper.findBankByBankCode(bank.getBankCode());
				if(bankcheck!=null)
				{
					LOGGER.warn("duplicate bank code, it is already exist. Bank Code : {}", bank.getBankCode());
					throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA,new String[] {messageService.getMessageFor("l.bankCode")});					
				}
				
				Bank bankTemp = bankMapper.findBankByShowOrder(bank.getShowOrder());
				if(bankTemp!=null)
				{
					LOGGER.warn("Show Order already used");
					throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA,new String[] {messageService.getMessageFor("l.showOrder")});					
				}					
				
				bank.setBankLogo(bank.getBankCode()+"."+extension);
				bank.setVersionStatus(WebConstants.VERSION_STATUS_CHANGED);
				int created = bankMapper.createBank(bank);
//				ftpService.uploadFile(bank.getImageLogo(), bank.getBankCode()+"."+extension, WebConstants.FOLDER_BANK);
				LOGGER.debug("Created {} {}", created, bank);
				
				/** SET TO USER ACTIVITY **/
//				try 
//				{			
//					Collection<String> excludes = new ArrayList<String>();
//					Bank original = new Bank();
//					excludes.add("createdOn");
//					excludes.add("createdBy");
//					excludes.add("updatedOn");
//					excludes.add("updatedBy");
//					userActivityService.generateHistoryActivity(excludes, original, bank, 
//							loginVO.getId(), loginVO.getUserCode(), loginVO.getUserName(), 
//							WebConstants.ACT_MODULE_BANK, WebConstants.ACT_TYPE_INSERT, WebConstants.ACT_TABLE_BANK, 
//							bank.getId(), loginVO.getIpAddress());
//				} 
//				catch (Exception e) 
//				{
//					LOGGER.warn("Unable to Create History Activity: " + e.getMessage());
//				}
				/** SET TO USER ACTIVITY **/
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] {messageService.getMessageFor("l.bank", language),
						messageService.getMessageFor("l.created", language)}, language));
				wrv.setType(WebConstants.TYPE_INSERT);
				return wrv;
			}
			else
			{
				Bank original = bankMapper.findBankById(bank.getId());			
				bank.setVersionStatus(WebConstants.VERSION_STATUS_CHANGED);
				if(bank.getShowOrder()!=original.getShowOrder())
				{
					Bank bankTemp = bankMapper.findBankByShowOrder(bank.getShowOrder());
					if(bankTemp!=null)
					{
						LOGGER.warn("Show Order already used");
						throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA,new String[] {messageService.getMessageFor("l.showOrder")});					
					}
				}
				if(bank.getImageLogo()!=null)
				{
					if(CommonUtil.checkImageExtension(extension)==false)
					{
						LOGGER.warn("uploaded file is not valid image file");
						throw new SambaWebException(SambaWebException.NE_NOT_A_VALID_IMAGE_FILE,new String[] {messageService.getMessageFor("l.bankLogo")});
					}
					BufferedImage bimg = ImageIO.read(bank.getImageLogo());
					int width          = bimg.getWidth();
					int height         = bimg.getHeight();
					if(width!=wDimension || height!=hDimension)
					{
						LOGGER.warn("display image dimension is not valid!");
						throw new SambaWebException(SambaWebException.NE_NOT_A_VALID_DIMENSION,new String[] {messageService.getMessageFor("l.bankLogo")});	
					}
					bank.setBankLogo(bank.getBankCode()+"."+extension);
					int updated = bankMapper.updateBank(bank);
					LOGGER.debug("Updated {} {}", updated, bank);
//					ftpService.uploadFile(bank.getImageLogo(), bank.getBankCode()+"."+extension, WebConstants.FOLDER_BANK);
				}
				else
				{
					int updated = bankMapper.updateBank(bank);
					LOGGER.debug("Updated {} {}", updated, bank);
				}
												
				/** SET TO USER ACTIVITY **/
//				try 
//				{			
//					Collection<String> excludes = new ArrayList<String>();
//					excludes.add("createdOn");
//					excludes.add("createdBy");
//					excludes.add("updatedOn");
//					excludes.add("updatedBy");
//					userActivityService.generateHistoryActivity(excludes, original, bank, 
//							loginVO.getId(), loginVO.getUserCode(), loginVO.getUserName(), 
//							WebConstants.ACT_MODULE_BANK, WebConstants.ACT_TYPE_UPDATE, WebConstants.ACT_TABLE_BANK, 
//							bank.getId(), loginVO.getIpAddress());
//				} 
//				catch (Exception e) 
//				{
//					LOGGER.warn("Unable to Create History Activity: " + e.getMessage());
//				}
				/** SET TO USER ACTIVITY **/
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] {messageService.getMessageFor("l.bank", language),
						messageService.getMessageFor("l.updated", language)}, language));
				wrv.setType(WebConstants.TYPE_UPDATE);
				wrv.setPath(WebConstants.PATH_UPDATE_BANK);
				return wrv;
			}
		} catch (SambaWebException gwe) {
			LOGGER.warn("Exception when saving {}, error code {}", bank, gwe.getErrorCode());
			throw gwe;
		}
	}

}
