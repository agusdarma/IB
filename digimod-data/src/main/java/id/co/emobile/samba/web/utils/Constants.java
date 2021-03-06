package id.co.emobile.samba.web.utils;

public class Constants {
	
	/*HTML 5
	public static final String NE_SDF_FULL			= "dd/MM/yyyy HH:mm:ss";
	public static final String NE_SDF_DATE			= "yyyy-MM-dd";
	public static final String NE_SDF_DOJO			= "yyyy-MM-dd'T'HH:mm:ss";*/
	
	/* SJDATEPICKER */
	public static final String NE_SDF_FULL			= "dd-MM-yyyy HH:mm:ss";
	public static final String NE_SDF_FULL_SLASH	= "dd/MM/yyyy HH:mm:ss";
	public static final String NE_SDF_FULL_NOS		= "dd-MM-yyyy HH:mm";
	public static final String NE_SDF_FULL_SLASH_NOS= "dd/MM/yyyy HH:mm";
	public static final String NE_SDF_DATE			= "dd-MM-yyyy";
	public static final String NE_SDF_DATE_SLASH	= "dd/MM/yyyy";
	public static final String NE_SDF_DOJO			= "yyyy-MM-dd'T'HH:mm:ss";
	public static final String NE_SDF_CUSTOM	    = "dd/MMMM";
	
	public static final String NE_SDF_TIME			= "HH:mm:ss";
	public static final String NE_FORMAT_DATE_JSP	= "dd-mm-yy";
	public static final String NE_FORMAT_TIME_JSP	= "HH:mm:ss";
	public static final String NE_ZERO				= "0";
	
	public static final int NE_NEED_CHG_PASS		= 0;
	public static final int NE_SUCCESS_CHG_PASS		= 1;
	
	/* status for table mxuinf */
	public static final String STATUS_DISABLED		= "0";
	public static final String STATUS_ACTIVE		= "1";
	
	public static final int STATUS_CHANGE_PIN		= 1;
	public static final int STATUS_RESET_PIN		= 2;
	
	public static final String ACTIVE_CODE			= "A";
	public static final String BLOCK_CODE			= "B";

	/* account type */
	public static final String ACCOUNT_SAVING		= "S";
	
	public static final String STATUS_APPROVE		= "A";
	public static final String STATUS_TERMINATED	= "T";
	public static final String STATUS_PENDING		= "Y";
	public static final String STATUS_BLOCK			= "B";
	public static final String STATUS_NO_NEED_AUTH	= "N";
	public static final String STATUS_REJECT		= "R";
	
	public static final String HTTP_PARAM_CARD_NO	= "CardNo";
	public static final String HTTP_PARAM_MOBILE_PHONE= "MobilePhone";
	public static final String HTTP_PARAM_CIF_NAME= "CifName";
	public static final String HTTP_PARAM_EMAIL= "Email";
	public static final String HTTP_PARAM_CIF_GROUP= "CifGroup";
	public static final String HTTP_PARAM_ACCESS_GROUP= "AccessGroup";
	public static final String HTTP_PARAM_MOBILE_STATUS= "MobileStatus";
	public static final String HTTP_PARAM_DEFAULT_ACCOUNT= "DefaultAccount";
	public static final String HTTP_PARAM_ACCOUNT_TYPE= "AccountType";
	public static final String HTTP_PARAM_CARD_TYPE= "CardType";
	public static final String HTTP_PARAM_TRX_CODE	= "TrxCode";
	public static final String HTTP_PARAM_USER_ID	= "UserId";
	public static final String HTTP_PARAM_PIN		= "Pin";
	public static final String HTTP_PARAM_BRANCH_CODE = "BranchCode";
	public static final String HTTP_PARAM_OLD_PIN		= "OldPin";
	public static final String HTTP_PARAM_NEW_PIN		= "NewPin";
	public static final String HTTP_PARAM_CONFIRM_PIN	= "ConfirmPin";
	public static final String HTTP_PARAM_BLAST_SMS_TIME= "BlastSMSTime";
	
	public static final String TRX_CODE_INQ_ACC_LIST= "InqAccList";
	public static final String TRX_CODE_CIF			= "Cif";
	public static final String TRX_CODE_CHANGE_PIN	= "ChangePin";
	public static final String TRX_CODE_RESET_PIN	= "ResetPin";
	public static final String TRX_CODE_UNREG		= "Unreg";
	
	public static final String RC_SUCCESS	= "0";
	
	public static final String BRANCH	= "Branch";
	
}
