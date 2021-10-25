//package id.co.emobile.samba.web.service;
//
//import java.io.File;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.MessageSource;
//
//import id.co.emobile.samba.web.data.DistDetailVO;
//import id.co.emobile.samba.web.data.DistMakerVO;
//import id.co.emobile.samba.web.data.SourceAccountVO;
//import id.co.emobile.samba.web.data.UserDataLoginVO;
//import id.co.emobile.samba.web.data.WebConstants;
//import id.co.emobile.samba.web.data.param.ReportParamVO;
//import id.co.emobile.samba.web.entity.DistributionDetail;
//import id.co.emobile.samba.web.entity.DistributionHeader;
//import id.co.emobile.samba.web.entity.UserData;
//import id.co.emobile.samba.web.entity.UserGroup;
//import id.co.emobile.samba.web.mapper.DistributionDetailMapper;
//import id.co.emobile.samba.web.mapper.DistributionHeaderMapper;
//import id.co.emobile.samba.web.mapper.UserDataMapper;
//import id.co.emobile.samba.web.utils.CipherUtils;
//import id.co.emobile.samba.web.utils.CommonUtil;
//
//public class DistMoneyService {
//	private static final Logger LOG = LoggerFactory.getLogger(DistMoneyService.class);
//
//	@Autowired
//	private AppsTimeService timeService;
//
//	@Autowired
//	private SourceAccountService sourceAccountService;
//
//	@Autowired
//	private UserGroupService userGroupService;
//
//	@Autowired
//	private DistributionHeaderMapper distHeaderMapper;
//
//	@Autowired
//	private DistributionDetailMapper distDetailMapper;
//
//	@Autowired
//	@Qualifier("sysSequenceGenerator")
//	private SequenceGeneratorService sysLogNoGenerator;
//
//	@Autowired
//	private SmsSenderService smsSenderService;
//
//	@Autowired
//	private MessageSource messageSource;
//
//	@Autowired
//	private SettingService settingService;
//
//	@Autowired
//	private UserDataMapper userDataMapper;
//
//	@Autowired
//	private UserActivityService userActivityService;
//
//	private File folderAssignment;
//	private Map<String, String> mapSysLogNo = new ConcurrentHashMap<>();
//
//	public File getFileFromName(String fileName) {
//		if (StringUtils.isEmpty(fileName))
//			return null;
//		String justName = FilenameUtils.getName(fileName);
//		File file = new File(folderAssignment, justName);
//		if (!file.exists() || !file.canRead())
//			return null;
//		return file;
//	}
//
//	public DistMakerVO processCreateIdBilling(String userCode, String npwpSsp, String nama, String alamat,
//			String remarks, String fileAssignmentName, File fileAssignment, String jenisPajak, String jenisSetoran,
//			Date startDatePbb, Date endDatePbb, String tahunPajak, String jumlahSetor, int sracId, String uraianSsp,
//			String npwpPenyetor, String noSk, String nop, String kotaWp, boolean isNonNpwp,String nik) throws SambaWebException {
//		try {
//			LOG.info("updated 15/07/2021");
//			if (StringUtils.isEmpty(noSk)) {
//				noSk = "000000000000000";
//			}
//			if (noSk.length() != 15) {
//				LOG.warn("invalid length noSk");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "noSk" });
//			}
//			if (StringUtils.isNotEmpty(npwpSsp)) {
//				if (npwpSsp.length() != 15) {
//					LOG.warn("invalid length npwpSsp");
//					throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "npwpSsp" });
//				}	
//			}
//			if (StringUtils.isNotEmpty(npwpPenyetor)) {
//				if (npwpPenyetor.length() != 15) {
//					LOG.warn("invalid length npwpPenyetor");
//					throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "npwpPenyetor" });
//				}	
//			}
//			if (StringUtils.isNotEmpty(jenisPajak)) {				
//				try {
//					String [] a = jenisPajak.split("-");
//					if(a.length == 1) {
//						String kodeJenisPajak = a[0].trim();
//						if(!StringUtils.isNumeric(kodeJenisPajak)) {
//							LOG.error("format harus numeric jenisPajak");
//							throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "jenisPajak" });
//						}				
//						jenisPajak = kodeJenisPajak;
//					}else if(a.length == 2) {
//						String kodeJenisPajak = a[0].trim();
//						if(!StringUtils.isNumeric(kodeJenisPajak)) {
//							LOG.error("format harus numeric jenisPajak");
//							throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "jenisPajak" });
//						}
//						jenisPajak = kodeJenisPajak;
//					}
//					LOG.info("jenisPajak : " + jenisPajak);
//				} catch (Exception e) {
//					LOG.error("Exception jenisPajak : " + e.getMessage());
//					throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "jenisPajak" });
//				}
//			}
//			if (StringUtils.isNotEmpty(jenisSetoran)) {				
//				try {
//					String [] a = jenisSetoran.split("-");
//					if(a.length == 1) {
//						String kodeJenisSetoran = a[0].trim();
//						if(!StringUtils.isNumeric(kodeJenisSetoran)) {
//							LOG.error("format harus numeric jenisSetoran");
//							throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "jenisSetoran" });
//						}				
//						jenisSetoran = kodeJenisSetoran;
//					}else if(a.length == 2) {
//						String kodeJenisSetoran = a[0].trim();
//						if(!StringUtils.isNumeric(kodeJenisSetoran)) {
//							LOG.error("format harus numeric jenisSetoran");
//							throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "jenisSetoran" });
//						}
//						jenisSetoran = kodeJenisSetoran;
//					}
//					LOG.info("jenisSetoran : " + jenisSetoran);
//				} catch (Exception e) {
//					LOG.error("Exception jenisSetoran : " + e.getMessage());
//					throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "jenisSetoran" });
//				}
//			}
//			if (StringUtils.isNotEmpty(nop)) {
//				if (nop.length() != 18) {
//					LOG.warn("invalid length nop");
//					throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "nop" });
//				}	
//			}
//			if (StringUtils.isNotEmpty(nik)) {
//				if (nik.length() != 16) {
//					LOG.warn("invalid length nik");
//					throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "nik" });
//				}
//			}
//			if (startDatePbb == null) {
//				LOG.warn("Missing startDatePbb");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "startDatePbb" });
//			}
//			if (endDatePbb == null) {
//				LOG.warn("Missing endDatePbb");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "endDatePbb" });
//			}
//			if (StringUtils.isEmpty(remarks)) {
//				LOG.warn("Missing Remarks");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "Remarks" });
//			}
//			String extAssignment = FilenameUtils.getExtension(fileAssignmentName);
//			if (!"pdf".equalsIgnoreCase(extAssignment)) {
//				LOG.warn("Invalid extension file for {}, only accepts pdf", fileAssignmentName);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "PDF File" });
//			}
//			try {
//				PDDocument document = PDDocument.load(fileAssignment);
//				int pages = document.getNumberOfPages();
//				LOG.info("Verified PDF file {} with {} pages", fileAssignmentName, pages);
//			} catch (Exception e) {
//				LOG.warn("Unable to read PDF file " + fileAssignmentName + ", " + fileAssignment.getAbsolutePath(), e);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "PDF File" });
//			}
//
//			SourceAccountVO srac = sourceAccountService.findSourceAccountById(sracId);
//			if (srac == null) {
//				LOG.warn("Invalid source account {}", sracId);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "Rekening Giro" });
//			}
//			if (srac.getSracStatus() != WebConstants.STATUS_ACTIVE) {
//				LOG.warn("Inactive source account {}", sracId);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "Rekening Giro" });
//			}
//			// copy file
////			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
//			// String destFile = sdf.format(timeService.getCurrentTime()) + "_" +
//			// FilenameUtils.getName(fileAssignmentName);
//			String destFile = FilenameUtils.getName(fileAssignmentName);
//			File fDest = new File(folderAssignment, destFile);
//			List<DistributionHeader> listExisting = distHeaderMapper.findDistributionHeaderByFileAssignment(destFile);
//			if (listExisting != null && listExisting.size() > 0) {
//				LOG.warn("File {} is already exists, probably already uploaded.", destFile);
//				throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA, new String[] { "File " + destFile });
//			}
//			FileUtils.copyFile(fileAssignment, fDest);
//
//			// process fileDist
//			DistMakerVO vo = new DistMakerVO();
//			String sysLogNo = mapSysLogNo.get(userCode);
//			if (sysLogNo == null) {
//				sysLogNo = sysLogNoGenerator.getNextSequence();
//				mapSysLogNo.put(userCode, sysLogNo);
//			}
//
//			Map<String, String> mapData = settingService.getSettingAsJson(SettingService.SETTING_COLLEGA_PARAM_DATA);
//			String dsacPajak = mapData.get(SettingService.COLLEGA_PARAM_DSAC_PAJAK);
//			String dsacPajakName = mapData.get(SettingService.COLLEGA_PARAM_DSAC_PAJAK_NAME);
//			if (dsacPajak == null) {
//				LOG.warn("Invalid destination account {}", dsacPajak);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT,
//						new String[] { "No Rekening Tujuan Pajak" });
//			}
//			if (dsacPajakName == null) {
//				LOG.warn("Invalid destination account name {}", dsacPajakName);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT,
//						new String[] { "Nama No Rekening Tujuan Pajak" });
//			}
//
//			vo.setSysLogNo(sysLogNo);
//			vo.setSracId(sracId);
//			vo.setSracNo(srac.getSracNumber());
//			vo.setSracName(srac.getSracName());
//			vo.setRemarks(remarks);
//			vo.setFileAssignment(destFile);
//			vo.setFileData("");
//			double totalDebet = 0;
//			double totalCredit = 0;
////			totalDebet += Double.parseDouble(nominal);
////			totalCredit += Double.parseDouble(nominal);
//			DistDetailVO detail = new DistDetailVO();
//			detail.setIndex(1);
//			detail.setNpwp(npwpSsp);
//			detail.setNpwpPenyetor(npwpPenyetor);
//			detail.setNonNpwp(isNonNpwp);
//			detail.setNama(nama);
//			detail.setAlamat(alamat);
//			detail.setKotaWp(kotaWp);
//			detail.setAccNumber(dsacPajak);
//			detail.setAccName(dsacPajakName);
//			detail.setJenisPajak(jenisPajak);
//			detail.setJenisSetoran(jenisSetoran);
//			detail.setStartDatePbb(startDatePbb);
//			detail.setEndDatePbb(endDatePbb);
//			detail.setTahunPajak(tahunPajak);
//			detail.setJumlahSetor(Double.parseDouble(jumlahSetor));
//			detail.setAmount(Double.parseDouble(jumlahSetor));
//			detail.setUraianSsp(uraianSsp);
//			if (StringUtils.isEmpty(noSk)) {
//				noSk = "000000000000000";
//			}
//			detail.setNoSk(noSk);
//			detail.setNop(nop);
//			detail.setNik(nik);
//			String kodeKkpSsp = "000";
//			try {
//				kodeKkpSsp = npwpSsp.substring(9, 12);
//			} catch (Exception e) {
//				LOG.error("Exception in kodeKkpSsp", e);
//			}
//			detail.setKodeKkpSsp(kodeKkpSsp);
//			smsSenderService.validateBillPbb(vo, detail);
//			vo.getListDetail().add(detail);
////			if (totalDebet != totalCredit) {
////				LOG.warn("Unbalance debet {} and credit {}", totalDebet, totalCredit);
////				throw new SambaWebException(SambaWebException.NE_INVALID_BALANCE);
////			}
////			if (totalDebet == 0) {
////				LOG.warn("No Data has been processed");
////				throw new SambaWebException(SambaWebException.NE_INVALID_DIST_CONTENT);
////			}
//			// create OTP
//			int otpLength = settingService.getSettingAsInt(SettingService.SETTING_OTP_LENGTH);
//			String otp = CommonUtil.generateRandomPin(otpLength);
//			vo.setOtp(otp);
//			LOG.debug("Created {}", vo);
//			return vo;
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in processDistMaker", e);
//			throw new SambaWebException(e);
//		}
//	}
//
//	public DistMakerVO processDistMaker(String userCode, int sracId, String remarks, String fileDistName, File fileDist,
//			String fileAssignmentName, File fileAssignment) throws SambaWebException {
//		try {
//			if (sracId == 0) {
//				LOG.warn("Invalid sracId {}", sracId);
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "Rekening Giro" });
//			}
//			if (StringUtils.isEmpty(remarks)) {
//				LOG.warn("Missing Remarks");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "Remarks" });
//			}
//			// validate file Name
//			String extDist = FilenameUtils.getExtension(fileDistName);
//			if (!"csv".equalsIgnoreCase(extDist)) {
//				LOG.warn("Invalid extension file for {}, only accepts csv", fileDistName);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "CSV File" });
//			}
//			String extAssignment = FilenameUtils.getExtension(fileAssignmentName);
//			if (!"pdf".equalsIgnoreCase(extAssignment)) {
//				LOG.warn("Invalid extension file for {}, only accepts pdf", fileDistName);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "PDF File" });
//			}
//			try {
//				PDDocument document = PDDocument.load(fileAssignment);
//				int pages = document.getNumberOfPages();
//				LOG.info("Verified PDF file {} with {} pages", fileAssignmentName, pages);
//			} catch (Exception e) {
//				LOG.warn("Unable to read PDF file " + fileAssignmentName + ", " + fileAssignment.getAbsolutePath(), e);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "PDF File" });
//			}
//
//			SourceAccountVO srac = sourceAccountService.findSourceAccountById(sracId);
//			if (srac == null) {
//				LOG.warn("Invalid source account {}", sracId);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "Rekening Giro" });
//			}
//			if (srac.getSracStatus() != WebConstants.STATUS_ACTIVE) {
//				LOG.warn("Inactive source account {}", sracId);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "Rekening Giro" });
//			}
//			// copy file
////			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
//			// String destFile = sdf.format(timeService.getCurrentTime()) + "_" +
//			// FilenameUtils.getName(fileAssignmentName);
//			String destFile = FilenameUtils.getName(fileAssignmentName);
//			File fDest = new File(folderAssignment, destFile);
//			List<DistributionHeader> listExisting = distHeaderMapper.findDistributionHeaderByFileAssignment(destFile);
//			if (listExisting != null && listExisting.size() > 0) {
//				LOG.warn("File {} is already exists, probably already uploaded.", destFile);
//				throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA, new String[] { "File " + destFile });
//			}
//			FileUtils.copyFile(fileAssignment, fDest);
//
////			String destDataFile = sdf.format(timeService.getCurrentTime()) + "_" + FilenameUtils.getName(fileDistName);
//			String destDataFile = FilenameUtils.getName(fileDistName);
//			File fDestData = new File(folderAssignment, destDataFile);
//			listExisting = distHeaderMapper.findDistributionHeaderByFileData(destDataFile);
//			if (listExisting != null && listExisting.size() > 0) {
//				LOG.warn("File {} is already exists, probably already uploaded.", destDataFile);
//				throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA,
//						new String[] { "File " + destDataFile });
//			}
//			FileUtils.copyFile(fileDist, fDestData);
//
//			// process fileDist
//			DistMakerVO vo = new DistMakerVO();
//			String sysLogNo = mapSysLogNo.get(userCode);
//			if (sysLogNo == null) {
//				sysLogNo = sysLogNoGenerator.getNextSequence();
//				mapSysLogNo.put(userCode, sysLogNo);
//			}
//			vo.setSysLogNo(sysLogNo);
//			vo.setSracId(sracId);
//			vo.setSracNo(srac.getSracNumber());
//			vo.setSracName(srac.getSracName());
//			vo.setRemarks(remarks);
//			vo.setFileAssignment(destFile);
//			vo.setFileData(destDataFile);
//			double totalDebet = 0;
//			double totalCredit = 0;
//			List<String> listContent = FileUtils.readLines(fileDist, "UTF-8");
//			int[] listNumber = new int[listContent.size()];
//			int x = 0;
//			for (String content : listContent) {
//				LOG.debug("Content: {}", content);
//				// No;No Rekening;Nama Rekening;TX;Nominal;No_Handphone
//				if (StringUtils.isEmpty(content))
//					continue; // skip through empty lines
//				String[] parts = content.split(";");
//				if (parts.length != 6) {
//					LOG.warn("Invalid content {}", content);
//					throw new SambaWebException(SambaWebException.NE_INVALID_DIST_CONTENT);
//				}
//				if (parts[0].equalsIgnoreCase("No"))
//					continue; // skip through title
//				int index = Integer.parseInt(parts[0]);
//				// checking index
//				for (int i = 0; i < x; i++) {
//					if (listNumber[i] == index) {
//						LOG.warn("Duplicate index {} in content {}", index, content);
//						throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA,
//								new String[] { "Content " + content });
//					}
//				}
//				listNumber[x] = index;
//				x++;
//
//				String accNo = parts[1].trim();
//				if (!StringUtils.isNumeric(accNo)) {
//					LOG.warn("Account Number must be numeric, {} has non numeric", accNo);
//					throw new SambaWebException(SambaWebException.NE_INVALID_DIST_CONTENT);
//				}
//				String accName = parts[2].trim();
//				String tx = parts[3].trim();
//				double amount = Double.parseDouble(parts[4].trim());
//				String phoneNo = parts[5].trim();
//				if (tx.equalsIgnoreCase("D")) {
//					if (!srac.getSracNumber().equals(accNo)) {
//						LOG.warn("Invalid source account selected {} with source account {}", srac, accNo);
//						throw new SambaWebException(SambaWebException.NE_INVALID_SOURCE_ACCOUNT);
//					}
//					totalDebet += amount;
//				} else if (tx.equalsIgnoreCase("K")) {
//					totalCredit += amount;
//					DistDetailVO detail = new DistDetailVO();
//					detail.setIndex(index);
//					detail.setAccNumber(accNo);
//					detail.setAccName(accName);
//					detail.setAmount(amount);
//					detail.setPhoneNo(phoneNo);
//					smsSenderService.validateAccountFor(detail);
//					vo.getListDetail().add(detail);
//				} else {
//					LOG.warn("Invalid trxType: {}, it must be D or K", tx);
//					throw new SambaWebException(SambaWebException.NE_INVALID_DIST_CONTENT);
//				}
//			} // end for looping
//			if (totalDebet != totalCredit) {
//				LOG.warn("Unbalance debet {} and credit {}", totalDebet, totalCredit);
//				throw new SambaWebException(SambaWebException.NE_INVALID_BALANCE);
//			}
//			if (totalDebet == 0) {
//				LOG.warn("No Data has been processed");
//				throw new SambaWebException(SambaWebException.NE_INVALID_DIST_CONTENT);
//			}
//			// create OTP
//			int otpLength = settingService.getSettingAsInt(SettingService.SETTING_OTP_LENGTH);
//			String otp = CommonUtil.generateRandomPin(otpLength);
//			vo.setOtp(otp);
//			LOG.debug("Created {}", vo);
//			return vo;
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in processDistMaker", e);
//			throw new SambaWebException(e);
//		}
//	}
//
//	public DistMakerVO processDistMakerOther(String userCode, int sracId, String remarks, String kodeBank, String dsac,
//			String nominal, String dsacName, String phoneNo, String fileAssignmentName, File fileAssignment)
//			throws SambaWebException {
//		try {
//			if (sracId == 0) {
//				LOG.warn("Invalid sracId {}", sracId);
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "Rekening Giro" });
//			}
//			if (StringUtils.isEmpty(remarks)) {
//				LOG.warn("Missing Remarks");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, new String[] { "Remarks" });
//			}
//			String extAssignment = FilenameUtils.getExtension(fileAssignmentName);
//			if (!"pdf".equalsIgnoreCase(extAssignment)) {
//				LOG.warn("Invalid extension file for {}, only accepts pdf", fileAssignmentName);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "PDF File" });
//			}
//			try {
//				PDDocument document = PDDocument.load(fileAssignment);
//				int pages = document.getNumberOfPages();
//				LOG.info("Verified PDF file {} with {} pages", fileAssignmentName, pages);
//			} catch (Exception e) {
//				LOG.warn("Unable to read PDF file " + fileAssignmentName + ", " + fileAssignment.getAbsolutePath(), e);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "PDF File" });
//			}
//
//			SourceAccountVO srac = sourceAccountService.findSourceAccountById(sracId);
//			if (srac == null) {
//				LOG.warn("Invalid source account {}", sracId);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "Rekening Giro" });
//			}
//			if (srac.getSracStatus() != WebConstants.STATUS_ACTIVE) {
//				LOG.warn("Inactive source account {}", sracId);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT, new String[] { "Rekening Giro" });
//			}
//			// copy file
////			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
//			// String destFile = sdf.format(timeService.getCurrentTime()) + "_" +
//			// FilenameUtils.getName(fileAssignmentName);
//			String destFile = FilenameUtils.getName(fileAssignmentName);
//			File fDest = new File(folderAssignment, destFile);
//			List<DistributionHeader> listExisting = distHeaderMapper.findDistributionHeaderByFileAssignment(destFile);
//			if (listExisting != null && listExisting.size() > 0) {
//				LOG.warn("File {} is already exists, probably already uploaded.", destFile);
//				throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA, new String[] { "File " + destFile });
//			}
//			FileUtils.copyFile(fileAssignment, fDest);
//
//			// process fileDist
//			DistMakerVO vo = new DistMakerVO();
//			String sysLogNo = mapSysLogNo.get(userCode);
//			if (sysLogNo == null) {
//				sysLogNo = sysLogNoGenerator.getNextSequence();
//				mapSysLogNo.put(userCode, sysLogNo);
//			}
//			vo.setSysLogNo(sysLogNo);
//			vo.setSracId(sracId);
//			vo.setSracNo(srac.getSracNumber());
//			vo.setSracName(srac.getSracName());
//			vo.setRemarks(remarks);
//			vo.setFileAssignment(destFile);
//			vo.setFileData("");
//			double totalDebet = 0;
//			double totalCredit = 0;
//			totalDebet += Double.parseDouble(nominal);
//			totalCredit += Double.parseDouble(nominal);
//			DistDetailVO detail = new DistDetailVO();
//			detail.setIndex(0);
//			detail.setAccNumber(dsac);
//			detail.setAccName(dsacName);
//			detail.setAmount(Double.parseDouble(nominal));
//			detail.setPhoneNo(phoneNo);
//			detail.setBankCode(kodeBank);
//			smsSenderService.validateAccountForIso(vo, detail);
//			vo.getListDetail().add(detail);
//			if (totalDebet != totalCredit) {
//				LOG.warn("Unbalance debet {} and credit {}", totalDebet, totalCredit);
//				throw new SambaWebException(SambaWebException.NE_INVALID_BALANCE);
//			}
//			if (totalDebet == 0) {
//				LOG.warn("No Data has been processed");
//				throw new SambaWebException(SambaWebException.NE_INVALID_DIST_CONTENT);
//			}
//			// create OTP
//			int otpLength = settingService.getSettingAsInt(SettingService.SETTING_OTP_LENGTH);
//			String otp = CommonUtil.generateRandomPin(otpLength);
//			vo.setOtp(otp);
//			LOG.debug("Created {}", vo);
//			return vo;
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in processDistMaker", e);
//			throw new SambaWebException(e);
//		}
//	}
//
//	public void sendDistMakerOtp(String phoneNo, DistMakerVO distMakerVO) {
//		// send otp
//		try {
//			String message = messageSource.getMessage("msg.otp.maker", new String[] { distMakerVO.getOtp() }, null);
//			smsSenderService.sendSms(phoneNo, message, distMakerVO.getSysLogNo(), "OTP");
//			LOG.debug("sendDistMakerOtp: {}", distMakerVO);
//		} catch (Exception e) {
//			LOG.warn("Exception in sendDistMakerOtp to " + phoneNo, e);
//		}
//	}
//
//	public void sendDistCheckerOtp(String phoneNo, DistributionHeader header) {
//		// send otp
//		try {
//			String message = messageSource.getMessage("msg.otp.checker", new String[] { header.getOtp() }, null);
//			smsSenderService.sendSms(phoneNo, message, header.getSysLogNo(), "OTP");
//			LOG.debug("sendDistCheckerOtp: {}", header);
//		} catch (Exception e) {
//			LOG.warn("Exception in sendDistCheckerOtp to " + phoneNo, e);
//		}
//	}
//
//	public void sendDistApprovalOtp(String phoneNo, DistributionHeader header) {
//		// send otp
//		try {
//			String message = messageSource.getMessage("msg.otp.approval", new String[] { header.getOtp() }, null);
//			smsSenderService.sendSms(phoneNo, message, header.getSysLogNo(), "OTP");
//			LOG.debug("sendDistApprovalOtp: {}", header);
//		} catch (Exception e) {
//			LOG.warn("Exception in sendDistApprovalOtp to " + phoneNo, e);
//		}
//	}
//
//	public void verifyDistMakerPajak(UserDataLoginVO loginVO, DistMakerVO distMakerVO, String otp)
//			throws SambaWebException {
//		LOG.debug("verifyDistMakerPajak {}, otp {}", distMakerVO, otp);
//		try {
//			if (!distMakerVO.getOtp().equals(otp)) {
//				LOG.warn("Invalid OTP {}", otp);
//				throw new SambaWebException(SambaWebException.NE_INVALID_OTP);
//			}
//			// check if all data has been checked
//			boolean checked = true;
//			for (DistDetailVO detailVO : distMakerVO.getListDetail()) {
//				if (!detailVO.isCheckSuccess()) {
//					LOG.warn("Data {} is failed, please correct first", detailVO);
//					checked = false;
//					break;
//				}
//			}
//			if (!checked) {
//				throw new SambaWebException(SambaWebException.NE_INVALID_DIST_CONTENT);
//			}
//			SourceAccountVO srac = sourceAccountService.findSourceAccountById(distMakerVO.getSracId());
//			DistributionHeader h = new DistributionHeader();
//			h.setSysLogNo(distMakerVO.getSysLogNo()); // syslogno VARCHAR(14) NOT NULL,
//			h.setGroupId(loginVO.getGroupId());
//			h.setGroupName(loginVO.getGroupName());
//			h.setFileData(distMakerVO.getFileData()); // file_data VARCHAR(50) NOT NULL,
//			h.setFileAssignment(distMakerVO.getFileAssignment()); // file_assignment VARCHAR(50) NOT NULL,
//			h.setSourceAccountId(distMakerVO.getSracId()); // source_account_id INT NOT NULL,
//			h.setPhoneNo(srac.getPhoneNo()); // phone_no VARCHAR(50) NOT NULL,
//			h.setSracNumber(srac.getSracNumber()); // srac_number VARCHAR(50) NOT NULL,
//			h.setSracName(srac.getSracName());
//			h.setMakerRemarks(distMakerVO.getRemarks()); // maker_remarks VARCHAR(1000) NOT NULL,
//			h.setCheckerRemarks(""); // checker_remarks VARCHAR(1000) NOT NULL,
//			h.setCallbackRemarks(""); // approval_remarks VARCHAR(1000) NOT NULL,
//			h.setApprovalRemarks(""); // approval_remarks VARCHAR(1000) NOT NULL,
//			h.setStatus(WebConstants.PAJAK_DIST_STATUS_UPLOADED); // status INT NOT NULL,
//			h.setUploadedAmount(distMakerVO.getListDetail().size()); // uploaded_amount INT,
//			h.setUploadedValue(distMakerVO.getTotalAmount()); // uploaded_value DECIMAL(12,2),
//			h.setUploadedBy(loginVO.getId()); // uploaded_by INT NOT NULL,
//			h.setUploadedOn(timeService.getCurrentTime()); // uploaded_on DATETIME,
//			h.setCheckedBy(0); // checked_by INT NOT NULL,
//			// private Date checkedOn; // checked_on DATETIME,
//			h.setCallbackBy(0);
//			h.setApprovedBy(0); // approved_by INT NOT NULL,
//			// private Date approvedOn; // approved_on DATETIME,
//			h.setProcessSuccess(0); // process_success INT,
//			h.setProcessFailed(0); // process_failed INT,
//			h.setProcessValue(0); // process_value DECIMAL(12,2),
////			private Date processStarted;  // process_started DATETIME,
////			private Date processFinished;  // process_finished DATETIME,
//			h.setTrxCode(distMakerVO.getTrxCode());
//			int created = distHeaderMapper.createDistributionHeader(h);
//			LOG.debug("Created {} {}", created, h);
//			for (DistDetailVO detail : distMakerVO.getListDetail()) {
//				DistributionDetail d = new DistributionDetail();
//				d.setSysLogNo(h.getSysLogNo()); // syslogno VARCHAR(14) NOT NULL,
//				d.setDetailId(detail.getIndex()); // detail_id INT NOT NULL,
//				d.setPhoneNo(detail.getPhoneNo()); // phone_no VARCHAR(50) NOT NULL,
//				d.setAccountNo(detail.getAccNumber()); // account_no VARCHAR(50) NOT NULL,
//				d.setBankCode(detail.getBankCode());
//				d.setAccountName(detail.getAccName());
//				d.setMoneyValue(detail.getAmount()); // money_value DECIMAL(12,2) NOT NULL,
//				d.setHostRefNo(""); // host_ref_no VARCHAR(20),
//				d.setHostRc(""); // host_rc VARCHAR(10),
//				d.setHostMessage("");
//				d.setProcessStatus(WebConstants.PROCESS_STATUS_INIT); // process_status INT NOT NULL,
//				String dataCheck = d.getSysLogNo() + "_" + d.getDetailId() + "_" + d.getPhoneNo() + "_"
//						+ d.getAccountNo() + "_" + d.getMoneyValue();
//				String check = CommonUtil.toHexString(CipherUtils.hashSHA256(dataCheck));
//				d.setDataCheck(check);
//				d.setNama(detail.getNama());
//				d.setNpwp(detail.getNpwp());
//				d.setNonNpwp(detail.isNonNpwp());
//				d.setNpwpPenyetor(detail.getNpwpPenyetor());
//				d.setAlamat(detail.getAlamat());
//				d.setKotaWp(detail.getKotaWp());
//				d.setJenisPajak(detail.getJenisPajak());
//				d.setJenisSetoran(detail.getJenisSetoran());
//				d.setStartDatePbb(detail.getStartDatePbb());
//				d.setEndDatePbb(detail.getEndDatePbb());
//				d.setTahunPajak(detail.getTahunPajak());
//				d.setJumlahSetor(detail.getJumlahSetor());
//				d.setUraianSsp(detail.getUraianSsp());
//				d.setNoSk(detail.getNoSk());
//				d.setNop(detail.getNop());
//				d.setNik(detail.getNik());
//				d.setKodeKkpSsp(detail.getKodeKkpSsp());
//				d.setIdBilling(detail.getIdBilling());
//				d.setMasaAktifBilling(detail.getMasaAktifBilling());
//
////				private Date processStarted;  // process_started DATETIME,
////				private Date processFinished;  // process_finished DATETIME,
//				int createdDetail = distDetailMapper.createDistributionDetail(d);
//				LOG.debug("Created {} {}", createdDetail, d);
//			}
//			mapSysLogNo.remove(loginVO.getUserCode());
//			// Send to checker
//			try {
//				NumberFormat nf = new DecimalFormat("#,##0");
//				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
//				List<UserData> listChecker = userDataMapper.findUserDataActiveByGroupAndLevelType(loginVO.getGroupId(),
//						WebConstants.LEVEL_TYPE_CHECKER);
//				if (listChecker != null && listChecker.size() > 0) {
//					for (UserData checker : listChecker) {
//						String[] params = { h.getSysLogNo(), loginVO.getUserCode(), h.getFileAssignment(),
//								nf.format(h.getUploadedValue()), sdf.format(h.getUploadedOn()), loginVO.getPhoneNo(),
//								loginVO.getUserCode(), loginVO.getUserName() };
//						String notif = messageSource.getMessage("msg.notif.checker", params, null);
//						smsSenderService.sendSms(checker.getPhoneNo(), notif, h.getSysLogNo(), "NOTIF");
//					}
//				}
//			} catch (Exception e) {
//				LOG.warn("Exception sending notif to checker", e);
//			}
//			userActivityService.createUserActivityMaker(loginVO, h);
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in verifyDistMaker", e);
//			throw new SambaWebException(e);
//		}
//	}
//
//	public void verifyDistMaker(UserDataLoginVO loginVO, DistMakerVO distMakerVO, String otp) throws SambaWebException {
//		LOG.debug("verifyDistMaker {}, otp {}", distMakerVO, otp);
//		try {
//			if (!distMakerVO.getOtp().equals(otp)) {
//				LOG.warn("Invalid OTP {}", otp);
//				throw new SambaWebException(SambaWebException.NE_INVALID_OTP);
//			}
//			// check if all data has been checked
//			boolean checked = true;
//			for (DistDetailVO detailVO : distMakerVO.getListDetail()) {
//				if (!detailVO.isCheckSuccess()) {
//					LOG.warn("Data {} is failed, please correct first", detailVO);
//					checked = false;
//					break;
//				}
//			}
//			if (!checked) {
//				throw new SambaWebException(SambaWebException.NE_INVALID_DIST_CONTENT);
//			}
//			SourceAccountVO srac = sourceAccountService.findSourceAccountById(distMakerVO.getSracId());
//			DistributionHeader h = new DistributionHeader();
//			h.setSysLogNo(distMakerVO.getSysLogNo()); // syslogno VARCHAR(14) NOT NULL,
//			h.setGroupId(loginVO.getGroupId());
//			h.setGroupName(loginVO.getGroupName());
//			h.setFileData(distMakerVO.getFileData()); // file_data VARCHAR(50) NOT NULL,
//			h.setFileAssignment(distMakerVO.getFileAssignment()); // file_assignment VARCHAR(50) NOT NULL,
//			h.setSourceAccountId(distMakerVO.getSracId()); // source_account_id INT NOT NULL,
//			h.setPhoneNo(srac.getPhoneNo()); // phone_no VARCHAR(50) NOT NULL,
//			h.setSracNumber(srac.getSracNumber()); // srac_number VARCHAR(50) NOT NULL,
//			h.setSracName(srac.getSracName());
//			h.setMakerRemarks(distMakerVO.getRemarks()); // maker_remarks VARCHAR(1000) NOT NULL,
//			h.setCheckerRemarks(""); // checker_remarks VARCHAR(1000) NOT NULL,
//			h.setCallbackRemarks(""); // approval_remarks VARCHAR(1000) NOT NULL,
//			h.setApprovalRemarks(""); // approval_remarks VARCHAR(1000) NOT NULL,
//			h.setStatus(WebConstants.DIST_STATUS_UPLOADED); // status INT NOT NULL,
//			h.setUploadedAmount(distMakerVO.getListDetail().size()); // uploaded_amount INT,
//			h.setUploadedValue(distMakerVO.getTotalAmount()); // uploaded_value DECIMAL(12,2),
//			h.setUploadedBy(loginVO.getId()); // uploaded_by INT NOT NULL,
//			h.setUploadedOn(timeService.getCurrentTime()); // uploaded_on DATETIME,
//			h.setCheckedBy(0); // checked_by INT NOT NULL,
//			// private Date checkedOn; // checked_on DATETIME,
//			h.setCallbackBy(0);
//			h.setApprovedBy(0); // approved_by INT NOT NULL,
//			// private Date approvedOn; // approved_on DATETIME,
//			h.setProcessSuccess(0); // process_success INT,
//			h.setProcessFailed(0); // process_failed INT,
//			h.setProcessValue(0); // process_value DECIMAL(12,2),
////			private Date processStarted;  // process_started DATETIME,
////			private Date processFinished;  // process_finished DATETIME,
//			h.setTrxCode(distMakerVO.getTrxCode());
//			int created = distHeaderMapper.createDistributionHeader(h);
//			LOG.debug("Created {} {}", created, h);
//			for (DistDetailVO detail : distMakerVO.getListDetail()) {
//				DistributionDetail d = new DistributionDetail();
//				d.setSysLogNo(h.getSysLogNo()); // syslogno VARCHAR(14) NOT NULL,
//				d.setDetailId(detail.getIndex()); // detail_id INT NOT NULL,
//				d.setPhoneNo(detail.getPhoneNo()); // phone_no VARCHAR(50) NOT NULL,
//				d.setAccountNo(detail.getAccNumber()); // account_no VARCHAR(50) NOT NULL,
//				d.setBankCode(detail.getBankCode());
//				d.setAccountName(detail.getAccName());
//				d.setMoneyValue(detail.getAmount());
//				; // money_value DECIMAL(12,2) NOT NULL,
//				d.setHostRefNo(""); // host_ref_no VARCHAR(20),
//				d.setHostRc(""); // host_rc VARCHAR(10),
//				d.setHostMessage("");
//				d.setProcessStatus(WebConstants.PROCESS_STATUS_INIT); // process_status INT NOT NULL,
//				String dataCheck = d.getSysLogNo() + "_" + d.getDetailId() + "_" + d.getPhoneNo() + "_"
//						+ d.getAccountNo() + "_" + d.getMoneyValue();
//				String check = CommonUtil.toHexString(CipherUtils.hashSHA256(dataCheck));
//				d.setDataCheck(check);
//				d.setNama(detail.getNama());
//				d.setNpwp(detail.getNpwp());
//				d.setNpwpPenyetor(detail.getNpwpPenyetor());
//				d.setAlamat(detail.getAlamat());
//				d.setKotaWp(detail.getKotaWp());
//				d.setJenisPajak(detail.getJenisPajak());
//				d.setJenisSetoran(detail.getJenisSetoran());
//				d.setStartDatePbb(detail.getStartDatePbb());
//				d.setEndDatePbb(detail.getEndDatePbb());
//				d.setTahunPajak(detail.getTahunPajak());
//				d.setJumlahSetor(detail.getJumlahSetor());
//				d.setUraianSsp(detail.getUraianSsp());
//				d.setNoSk(detail.getNoSk());
//				d.setNop(detail.getNop());
//				d.setKodeKkpSsp(detail.getKodeKkpSsp());
//				d.setIdBilling(detail.getIdBilling());
//				d.setMasaAktifBilling(detail.getMasaAktifBilling());
//
////				private Date processStarted;  // process_started DATETIME,
////				private Date processFinished;  // process_finished DATETIME,
//				int createdDetail = distDetailMapper.createDistributionDetail(d);
//				LOG.debug("Created {} {}", createdDetail, d);
//			}
//			mapSysLogNo.remove(loginVO.getUserCode());
//			// Send to checker
//			try {
//				NumberFormat nf = new DecimalFormat("#,##0");
//				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
//				List<UserData> listChecker = userDataMapper.findUserDataActiveByGroupAndLevelType(loginVO.getGroupId(),
//						WebConstants.LEVEL_TYPE_CHECKER);
//				if (listChecker != null && listChecker.size() > 0) {
//					for (UserData checker : listChecker) {
//						String[] params = { h.getSysLogNo(), loginVO.getUserCode(), h.getFileAssignment(),
//								nf.format(h.getUploadedValue()), sdf.format(h.getUploadedOn()), loginVO.getPhoneNo(),
//								loginVO.getUserCode(), loginVO.getUserName() };
//						String notif = messageSource.getMessage("msg.notif.checker", params, null);
//						smsSenderService.sendSms(checker.getPhoneNo(), notif, h.getSysLogNo(), "NOTIF");
//					}
//				}
//			} catch (Exception e) {
//				LOG.warn("Exception sending notif to checker", e);
//			}
//			userActivityService.createUserActivityMaker(loginVO, h);
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in verifyDistMaker", e);
//			throw new SambaWebException(e);
//		}
//	}
//
//	public void verifyDistChecker(UserDataLoginVO loginVO, DistributionHeader header, String otp)
//			throws SambaWebException {
//		LOG.debug("verifyDistChecker {}, otp {}", header, otp);
//		try {
//			if (!header.getOtp().equals(otp)) {
//				LOG.warn("Invalid OTP {}", otp);
//				throw new SambaWebException(SambaWebException.NE_INVALID_OTP);
//			}
//			if (StringUtils.isEmpty(header.getCheckerRemarks())) {
//				LOG.warn("Missing checker remarks");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, "Remarks");
//			}
//			if (loginVO.getId() == header.getUploadedBy()) {
//				LOG.warn("Checker {} must be different from maker {}", loginVO.getId(), header.getUploadedBy());
//				throw new SambaWebException(SambaWebException.NE_NOT_AUTHORIZED);
//			}
//			if (header.getStatus() == WebConstants.DIST_STATUS_CHECKED
//					|| header.getStatus() == WebConstants.DIST_STATUS_REJECTED_CHECKER) {
//				header.setCheckedBy(loginVO.getId()); // checked_by INT NOT NULL,
//				header.setCheckedOn(timeService.getCurrentTime());
//			} else {
//				if (StringUtils.isEmpty(header.getCallbackRemarks())) {
//					LOG.warn("Missing callback remarks");
//					throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, "Remarks");
//				}
//				if (loginVO.getId() == header.getCheckedBy()) {
//					LOG.warn("Callback {} must be different from checker {}", loginVO.getId(), header.getCheckedBy());
//					throw new SambaWebException(SambaWebException.NE_NOT_AUTHORIZED);
//				}
//				header.setCallbackBy(loginVO.getId()); // checked_by INT NOT NULL,
//				header.setCallbackOn(timeService.getCurrentTime());
//			}
//
//			int updated = distHeaderMapper.updateDistributionHeader(header);
//			LOG.debug("Updated {} {}", updated, header);
//
//			if (header.getStatus() == WebConstants.DIST_STATUS_CHECKED
//					|| header.getStatus() == WebConstants.DIST_STATUS_CALLBACK) {
//				// Send to approval
//				try {
//					NumberFormat nf = new DecimalFormat("#,##0");
//					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
//					int nextLevel = WebConstants.LEVEL_TYPE_APPROVAL;
//					String msgCode = "msg.notif.approval";
//					Date processedOn = header.getCallbackOn();
//					List<UserData> listApproval;
//					if (header.getStatus() == WebConstants.DIST_STATUS_CHECKED) {
//						// send to callback
//						nextLevel = WebConstants.LEVEL_TYPE_CALLBACK;
//						msgCode = "msg.notif.callback";
//						processedOn = header.getCheckedOn();
//						listApproval = userDataMapper.findUserDataActiveByGroupAndLevelType(loginVO.getGroupId(),
//								nextLevel);
//					} else {
//						// send to approval
//						listApproval = userDataMapper.findUserDataApprovalActiveForGroup(loginVO.getGroupId());
//					}
//					UserData userMaker = userDataMapper.findUserById(header.getUploadedBy());
//					if (listApproval != null && listApproval.size() > 0) {
//						for (UserData checker : listApproval) {
//							String[] params = { header.getSysLogNo(), loginVO.getUserCode(), header.getFileAssignment(),
//									nf.format(header.getUploadedValue()), sdf.format(processedOn),
//									userMaker.getPhoneNo(), userMaker.getUserCode(), userMaker.getUserName() };
//							String notif = messageSource.getMessage(msgCode, params, null);
//							smsSenderService.sendSms(checker.getPhoneNo(), notif, header.getSysLogNo(), "NOTIF");
//						}
//					}
//				} catch (Exception e) {
//					LOG.warn("Exception sending notif to callback / approval", e);
//				}
//			} // if approved
//
//			// create user activity
//			if (header.getStatus() == WebConstants.DIST_STATUS_CHECKED) {
//				userActivityService.createUserActivityChecker(loginVO, header, true);
//			} else if (header.getStatus() == WebConstants.DIST_STATUS_REJECTED_CHECKER) {
//				userActivityService.createUserActivityChecker(loginVO, header, false);
//			} else if (header.getStatus() == WebConstants.DIST_STATUS_CALLBACK) {
//				userActivityService.createUserActivityCallback(loginVO, header, true);
//			} else if (header.getStatus() == WebConstants.DIST_STATUS_REJECTED_CALLBACK) {
//				userActivityService.createUserActivityCallback(loginVO, header, false);
//			} else {
//				LOG.warn("Unknown header status: {}. Not creating user activity", header.getStatus());
//			}
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in verifyDistChecker", e);
//			throw new SambaWebException(e);
//		}
//	}
//
//	public void verifyDistCheckerPajak(UserDataLoginVO loginVO, DistributionHeader header, String otp)
//			throws SambaWebException {
//		LOG.debug("verifyDistCheckerPajak {}, otp {}", header, otp);
//		try {
//			if (!header.getOtp().equals(otp)) {
//				LOG.warn("Invalid OTP {}", otp);
//				throw new SambaWebException(SambaWebException.NE_INVALID_OTP);
//			}
//			if (StringUtils.isEmpty(header.getCheckerRemarks())) {
//				LOG.warn("Missing checker remarks");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, "Remarks");
//			}
//			if (loginVO.getId() == header.getUploadedBy()) {
//				LOG.warn("Checker {} must be different from maker {}", loginVO.getId(), header.getUploadedBy());
//				throw new SambaWebException(SambaWebException.NE_NOT_AUTHORIZED);
//			}
//			if (header.getStatus() == WebConstants.PAJAK_DIST_STATUS_CHECKED
//					|| header.getStatus() == WebConstants.PAJAK_DIST_STATUS_REJECTED_CHECKER) {
//				header.setCheckedBy(loginVO.getId()); // checked_by INT NOT NULL,
//				header.setCheckedOn(timeService.getCurrentTime());
//			} else {
//				if (StringUtils.isEmpty(header.getCallbackRemarks())) {
//					LOG.warn("Missing callback remarks");
//					throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, "Remarks");
//				}
//				if (loginVO.getId() == header.getCheckedBy()) {
//					LOG.warn("Callback {} must be different from checker {}", loginVO.getId(), header.getCheckedBy());
//					throw new SambaWebException(SambaWebException.NE_NOT_AUTHORIZED);
//				}
//				header.setCallbackBy(loginVO.getId()); // checked_by INT NOT NULL,
//				header.setCallbackOn(timeService.getCurrentTime());
//			}
//
//			int updated = distHeaderMapper.updateDistributionHeader(header);
//			LOG.debug("Updated {} {}", updated, header);
//
//			if (header.getStatus() == WebConstants.PAJAK_DIST_STATUS_CHECKED
//					|| header.getStatus() == WebConstants.PAJAK_DIST_STATUS_CALLBACK) {
//				// Send to approval
//				try {
//					NumberFormat nf = new DecimalFormat("#,##0");
//					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
//					int nextLevel = WebConstants.LEVEL_TYPE_APPROVAL;
//					String msgCode = "msg.notif.approval";
//					Date processedOn = header.getCallbackOn();
//					List<UserData> listApproval;
//					if (header.getStatus() == WebConstants.PAJAK_DIST_STATUS_CHECKED) {
//						// send to callback
//						nextLevel = WebConstants.LEVEL_TYPE_CALLBACK;
//						msgCode = "msg.notif.callback";
//						processedOn = header.getCheckedOn();
//						listApproval = userDataMapper.findUserDataActiveByGroupAndLevelType(loginVO.getGroupId(),
//								nextLevel);
//					} else {
//						// send to approval
//						listApproval = userDataMapper.findUserDataApprovalActiveForGroup(loginVO.getGroupId());
//					}
//					UserData userMaker = userDataMapper.findUserById(header.getUploadedBy());
//					if (listApproval != null && listApproval.size() > 0) {
//						for (UserData checker : listApproval) {
//							String[] params = { header.getSysLogNo(), loginVO.getUserCode(), header.getFileAssignment(),
//									nf.format(header.getUploadedValue()), sdf.format(processedOn),
//									userMaker.getPhoneNo(), userMaker.getUserCode(), userMaker.getUserName() };
//							String notif = messageSource.getMessage(msgCode, params, null);
//							smsSenderService.sendSms(checker.getPhoneNo(), notif, header.getSysLogNo(), "NOTIF");
//						}
//					}
//				} catch (Exception e) {
//					LOG.warn("Exception sending notif to callback / approval", e);
//				}
//			} // if approved
//
//			// create user activity
//			if (header.getStatus() == WebConstants.PAJAK_DIST_STATUS_CHECKED) {
//				userActivityService.createUserActivityChecker(loginVO, header, true);
//			} else if (header.getStatus() == WebConstants.PAJAK_DIST_STATUS_REJECTED_CHECKER) {
//				userActivityService.createUserActivityChecker(loginVO, header, false);
//			} else if (header.getStatus() == WebConstants.PAJAK_DIST_STATUS_CALLBACK) {
//				userActivityService.createUserActivityCallback(loginVO, header, true);
//			} else if (header.getStatus() == WebConstants.PAJAK_DIST_STATUS_REJECTED_CALLBACK) {
//				userActivityService.createUserActivityCallback(loginVO, header, false);
//			} else {
//				LOG.warn("Unknown header status: {}. Not creating user activity", header.getStatus());
//			}
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in verifyDistChecker", e);
//			throw new SambaWebException(e);
//		}
//	}
//
//	public void verifyDistApproval(UserDataLoginVO loginVO, DistributionHeader header,
//			List<DistributionDetail> listDetail, String otp) throws SambaWebException {
//		LOG.debug("verifyDistApproval {}, otp {}", header, otp);
//		try {
//			if (header.getSysLogNo() == null) {
//				LOG.error("Existing process has been running, skip processing {}", header);
//				throw new SambaWebException(SambaWebException.NE_EXISTING_PROCESS_ALREADY_RUNNING);
//			}
//			DistributionHeader existing = distHeaderMapper.findDistributionHeaderBySysLogNo(header.getSysLogNo());
//			if (WebConstants.DIST_STATUS_PROCESSING == existing.getStatus()
//					|| WebConstants.PAJAK_DIST_STATUS_PROCESSING == existing.getStatus()) {
//				LOG.error("Existing process has been running, skip processing {}", header);
//				throw new SambaWebException(SambaWebException.NE_EXISTING_PROCESS_ALREADY_RUNNING);
//			}
//			if (!header.getOtp().equals(otp)) {
//				LOG.warn("Invalid OTP {}", otp);
//				throw new SambaWebException(SambaWebException.NE_INVALID_OTP);
//			}
//			if (StringUtils.isEmpty(header.getApprovalRemarks())) {
//				LOG.warn("Missing approval remarks");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, "Remarks");
//			}
//			if (loginVO.getId() == header.getCheckedBy() || loginVO.getId() == header.getUploadedBy()) {
//				LOG.warn("Approval {} must be different from checker {}", loginVO.getId(), header.getCheckedBy());
//				throw new SambaWebException(SambaWebException.NE_NOT_AUTHORIZED);
//			}
//
//			SourceAccountVO srac = sourceAccountService.findSourceAccountById(header.getSourceAccountId());
//			if (srac == null || srac.getSracStatus() != WebConstants.STATUS_ACTIVE) {
//				LOG.warn("Source Account [{}] {} is not found / not active", header.getSourceAccountId(),
//						header.getSracNumber());
//				throw new SambaWebException(SambaWebException.NE_NOT_AUTHORIZED);
//			}
//			header.setApprovedBy(loginVO.getId()); // checked_by INT NOT NULL,
//			header.setApprovedOn(timeService.getCurrentTime());
//
//			// check for detail
//			int successCount = header.getProcessSuccess();
//			double successValue = header.getProcessValue();
//			int failedCount = header.getProcessFailed();
//			List<DistributionDetail> listOnDB = distDetailMapper.findDistributionDetailBySysLogNo(header.getSysLogNo());
//			for (DistributionDetail d : listDetail) {
//				if (d.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_SUCCESS
//						|| d.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_FAILED) {
//					for (DistributionDetail onDB : listOnDB) {
//						if (onDB.getSysLogNo().equals(d.getSysLogNo()) && onDB.getDetailId() == d.getDetailId()) {
//							if (onDB.getProcessStatus() != d.getProcessStatus()) {
//								if (d.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_SUCCESS) {
//									successCount++;
//									successValue += d.getMoneyValue();
//								} else if (d.getProcessStatus() == WebConstants.PROCESS_STATUS_FAILED) {
//									failedCount++;
//								}
//								d.setProcessStarted(timeService.getCurrentTime());
//								d.setProcessFinished(timeService.getCurrentTime());
//								int updated = distDetailMapper.updateDistributionDetail(d);
//								LOG.debug("Updated {} {}", updated, d);
//							}
//						}
//					} // onDB
//				} // process skip
//			} // detail
//			header.setProcessFailed(failedCount);
//			header.setProcessSuccess(successCount);
//			header.setProcessValue(successValue);
//
//			int updated = distHeaderMapper.updateDistributionHeader(header);
//			LOG.debug("Updated {} {}", updated, header);
//
//			// Process distribution if approved
//			if (WebConstants.DIST_STATUS_APPROVED == header.getStatus()) {
//				List<DistributionDetail> listExistingDetail = distDetailMapper
//						.findDistributionDetailBySysLogNo(header.getSysLogNo());
//				LOG.debug("listExistingDetail {} {}", listExistingDetail, header);
//				if (listExistingDetail.size() == 1) {
//					if (StringUtils.isNotEmpty(listExistingDetail.get(0).getBankCode())) {
//						// untuk transfer antar bank
//						smsSenderService.processDistributionSett(header.getSysLogNo(), header.getApprovedBy(), 0);
//					} else {
//						// untuk transfer sesama bank ( bulk )
//						smsSenderService.processDistribution(header.getSysLogNo(), header.getApprovedBy());
//					}
//				} else {
//					// untuk transfer sesama bank ( bulk )
//					smsSenderService.processDistribution(header.getSysLogNo(), header.getApprovedBy());
//				}
//				userActivityService.createUserActivityApproval(loginVO, header, true);
//			} else {
//				userActivityService.createUserActivityApproval(loginVO, header, false);
//			}
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in verifyDistApproval", e);
//			throw new SambaWebException(e);
//		}
//	}
//
//	public Map<String, Object> verifyDistApprovalPajak(UserDataLoginVO loginVO, DistributionHeader header,
//			List<DistributionDetail> listDetail, String otp, int state) throws SambaWebException {
//		LOG.debug("verifyDistApprovalPajak {}, otp {}", header, otp);
//		Map<String, Object> bit48 = new HashMap<String, Object>();
//		try {
//			DistributionHeader existing = distHeaderMapper.findDistributionHeaderBySysLogNo(header.getSysLogNo());
//			if (WebConstants.DIST_STATUS_PROCESSING == existing.getStatus()
//					|| WebConstants.PAJAK_DIST_STATUS_PROCESSING == existing.getStatus()) {
//				LOG.error("Existing process has been running, skip processing {}", header);
//				throw new SambaWebException(SambaWebException.NE_EXISTING_PROCESS_ALREADY_RUNNING);
//			}
//			if (!header.getOtp().equals(otp)) {
//				LOG.warn("Invalid OTP {}", otp);
//				throw new SambaWebException(SambaWebException.NE_INVALID_OTP);
//			}
//			if (StringUtils.isEmpty(header.getApprovalRemarks())) {
//				LOG.warn("Missing approval remarks");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT, "Remarks");
//			}
//			if (loginVO.getId() == header.getCheckedBy() || loginVO.getId() == header.getUploadedBy()) {
//				LOG.warn("Approval {} must be different from checker {}", loginVO.getId(), header.getCheckedBy());
//				throw new SambaWebException(SambaWebException.NE_NOT_AUTHORIZED);
//			}
//
//			SourceAccountVO srac = sourceAccountService.findSourceAccountById(header.getSourceAccountId());
//			if (srac == null || srac.getSracStatus() != WebConstants.STATUS_ACTIVE) {
//				LOG.warn("Source Account [{}] {} is not found / not active", header.getSourceAccountId(),
//						header.getSracNumber());
//				throw new SambaWebException(SambaWebException.NE_NOT_AUTHORIZED);
//			}
//			header.setApprovedBy(loginVO.getId()); // checked_by INT NOT NULL,
//			header.setApprovedOn(timeService.getCurrentTime());
//
//			// check for detail
//			int successCount = header.getProcessSuccess();
//			double successValue = header.getProcessValue();
//			int failedCount = header.getProcessFailed();
//			List<DistributionDetail> listOnDB = distDetailMapper.findDistributionDetailBySysLogNo(header.getSysLogNo());
//			for (DistributionDetail d : listDetail) {
//				if (d.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_SUCCESS
//						|| d.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_FAILED) {
//					for (DistributionDetail onDB : listOnDB) {
//						if (onDB.getSysLogNo().equals(d.getSysLogNo()) && onDB.getDetailId() == d.getDetailId()) {
//							if (onDB.getProcessStatus() != d.getProcessStatus()) {
//								if (d.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_SUCCESS) {
//									successCount++;
//									successValue += d.getMoneyValue();
//								} else if (d.getProcessStatus() == WebConstants.PROCESS_STATUS_FAILED) {
//									failedCount++;
//								}
//								d.setProcessStarted(timeService.getCurrentTime());
//								d.setProcessFinished(timeService.getCurrentTime());
//								int updated = distDetailMapper.updateDistributionDetail(d);
//								LOG.debug("Updated {} {}", updated, d);
//							}
//						}
//					} // onDB
//				} // process skip
//			} // detail
//			header.setProcessFailed(failedCount);
//			header.setProcessSuccess(successCount);
//			header.setProcessValue(successValue);
//
//			int updated = distHeaderMapper.updateDistributionHeader(header);
//			LOG.debug("Updated {} {}", updated, header);
//
//			// Process distribution if approved
//			if (WebConstants.PAJAK_DIST_STATUS_APPROVED == header.getStatus()) {
//				bit48 = smsSenderService.processDistributionPajak(header.getSysLogNo(), header.getApprovedBy(), state,
//						header.getBit48());
//
////				if (WebConstants.PAJAK_DIST_STATUS_APPROVED != header.getStatus()) {
////					throw new SambaWebException(SambaWebException.NE_FAILED_PROCESS_APPROVAL,header.getStatusDisplay() );		
////				}
//
//				userActivityService.createUserActivityApproval(loginVO, header, true);
//			} else {
//				userActivityService.createUserActivityApproval(loginVO, header, false);
//			}
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in verifyDistChecker", e);
//			throw new SambaWebException(e);
//		}
//		LOG.debug("bit48 {}", bit48);
//		return bit48;
//	}
//
//	public DistributionHeader findDistHeaderBySysLogNo(String sysLogNo) {
//		return distHeaderMapper.findDistributionHeaderBySysLogNo(sysLogNo);
//	}
//
//	public List<DistributionHeader> findDistHeaderByGroupAndStatus(int groupId, int distStatus) {
//		return findDistHeaderByGroupAndStatus(groupId, new int[] { distStatus });
//	}
//
//	public List<DistributionHeader> findDistHeaderByGroupAndStatus(int groupId, int[] distStatus) {
//		List<DistributionHeader> list = new ArrayList<>();
//		for (int status : distStatus) {
//			List<DistributionHeader> listHeader = distHeaderMapper.findDistributionHeaderByGroupAndStatus(groupId,
//					status);
//			if (listHeader != null && listHeader.size() > 0)
//				list.addAll(listHeader);
//		}
//		return list;
//	}
//
////	public List<DistributionHeader> findDistHeaderForReport(UserDataLoginVO loginVO) {
////		List<DistributionHeader> list = null;
////		try {
////			if (loginVO.getLevelVO().getLevelType() == WebConstants.LEVEL_TYPE_NORMAL)
////				list = distHeaderMapper.findDistributionHeaderAll();
////			else {
////				List<Integer> listGroupId = new ArrayList<>();
////				if (loginVO.getLevelVO().getLevelType() == WebConstants.LEVEL_TYPE_APPROVAL) {
////					List<UserGroup> listGroup = userGroupService.findUserGroupForApproval(loginVO.getId());
////					for (UserGroup g: listGroup)
////						listGroupId.add(new Integer(g.getId()));
////				} else {
////					listGroupId.add(new Integer(loginVO.getGroupId()));
////				}
////				list = distHeaderMapper.findDistributionHeaderByGroup(listGroupId);
////			}
////		} catch (Exception e) {
////			LOG.warn("Exception when findDistHeaderForReport for " + loginVO, e);
////		}
////		if (list == null) {
////			list = new ArrayList<>();
////		}
////		return list;
////	}
//
//	public List<DistributionHeader> findDistHeaderForReport(UserDataLoginVO loginVO, ReportParamVO paramVO) {
//		List<DistributionHeader> list = null;
//		try {
//			if (loginVO.getLevelVO().getLevelType() == WebConstants.LEVEL_TYPE_NORMAL) {
//				LOG.info("LEVEL_TYPE_NORMAL : " + loginVO);
//				LOG.info("PARAMVO : " + paramVO);
//				list = distHeaderMapper.findDistributionHeaderAll(paramVO);
//			} else {
//				LOG.info("LEVEL_TYPE_APPROVAL : " + loginVO);
//
//				List<Integer> listGroupId = new ArrayList<>();
//				if (loginVO.getLevelVO().getLevelType() == WebConstants.LEVEL_TYPE_APPROVAL) {
//					List<UserGroup> listGroup = userGroupService.findUserGroupForApproval(loginVO.getId());
//					for (UserGroup g : listGroup)
//						listGroupId.add(new Integer(g.getId()));
//				} else {
//					listGroupId.add(new Integer(loginVO.getGroupId()));
//				}
//				if (paramVO != null) {
//					paramVO.setListGroupId(listGroupId);
//				}
//				LOG.info("PARAMVO : " + paramVO);
//				list = distHeaderMapper.findDistributionHeaderByGroup(paramVO);
//			}
//		} catch (Exception e) {
//			LOG.warn("Exception when findDistHeaderForReport for " + loginVO, e);
//		}
//		if (list == null) {
//			list = new ArrayList<>();
//		}
//		LOG.info("findDistHeaderForReport : " + list);
//		return list;
//	}
//
//	public List<DistributionDetail> findDistDetailBySysLogNo(String sysLogNo) {
//		List<DistributionDetail> list = distDetailMapper.findDistributionDetailBySysLogNo(sysLogNo);
//		if (list == null) {
//			list = new ArrayList<>();
//		}
//		return list;
//	}
//
//	public void setFolderAssignment(String folderAssignment) throws Exception {
//		this.folderAssignment = new File(folderAssignment);
//		if (!this.folderAssignment.exists() || !this.folderAssignment.canWrite()) {
//			throw new Exception("Unable to set folderAssignment " + folderAssignment);
//		}
//		LOG.debug("set folderAssignment: " + this.folderAssignment.getAbsolutePath());
//	}
//
//}
