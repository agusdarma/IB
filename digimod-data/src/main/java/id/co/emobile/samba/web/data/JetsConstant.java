package id.co.emobile.samba.web.data;

public class JetsConstant {

	public static final String CIF_ACCESS_TYPE_ADMIN 		= "1";
	public static final String ALLOW_UNENCRYPTED_TRX			= "yes";
	public static final String LOG_TRX_BTI					= "bti.transaction";
	public static final String LOG_TRX_SMI					= "smi.transaction";
	public static final String LOG_TRX_XTI					= "xti.transaction";
	
	public static final String NEWLINE						= "<CR>";
	public static final String NULL_STRING_PARAM 			= "^";
	
	public static final int ACC_INDEX_UTAMA					= 1;
	
	//PAYEE CODE
		public static final String ISO_TYPE_WAKAF 				= "wakafpay";
	
	//USSD HTTP Param from Gateway
	public static final String HTTP_MESSAGE_ENCRYPTED		= "x";
	public static final String HTTP_CREDENTIAL				= "cr";
	public static final String HTTP_PHONE_NO				= "p";
	public static final String HTTP_MESSAGE					= "m";
	public static final String HTTP_CHANNEL					= "c";
	public static final String HTTP_REFNO_GW				= "g";
	public static final String HTTP_REFNO_MMBS				= "rm";
	public static final String HTTP_RC						= "rc";
	public static final String HTTP_MMBS_REF				= "mmbsref";
	public static final String HTTP_SMI_ID					= "smi";
	public static final String HTTP_CHARGE_CODE				= "ch";
	public static final String HTTP_ORIGINATOR				= "or";
	public static final String HTTP_DCS						= "dcs";
	public static final String HTTP_DSAC_NAME				= "dsacname";
	public static final String HTTP_BANK_CODE				= "bc";
	public static final String HTTP_BILLER_CODE				= "billercode";
	public static final String HTTP_PREFIX					= "px";
	public static final String HTTP_DOB						= "dob";
	public static final String HTTP_TRX_CODE				= "trx";
	public static final String HTTP_STATE					= "state";
	public static final String HTTP_SYSLOGNO				= "syslogno";
	public static final String HTTP_MSGLOGNO				= "msglogno";
	
	public static final String HTTP_PARAM_TRX_CODE			= "TrxCode";
	public static final String HTTP_PARAM_ACCOUNT_NO		= "AccountNo";
	public static final String HTTP_PARAM_CARD_NO			= "CardNo";
	public static final String HTTP_PARAM_MOBILE_PHONE		= "MobilePhone";
	public static final String HTTP_PARAM_STATE				= "State";
	public static final String HTTP_PARAM_CIF_NAME			= "CifName";
	public static final String HTTP_PARAM_EMAIL				= "Email";
	public static final String HTTP_PARAM_CIF_GROUP			= "CifGroup";
	public static final String HTTP_PARAM_ACCESS_GROUP		= "AccessGroup";
	public static final String HTTP_PARAM_MOBILE_STATUS		= "MobileStatus";
	public static final String HTTP_PARAM_DEFAULT_ACCOUNT	= "DefaultAccount";
	public static final String HTTP_PARAM_ACCOUNT_TYPE		= "AccountType";
	public static final String HTTP_PARAM_ACCOUNT_DATA		= "AccountData";
	public static final String HTTP_PARAM_CARD_TYPE			= "CardType";
	public static final String HTTP_PARAM_USER_ID			= "UserId";
	public static final String HTTP_PARAM_BRANCH_CODE		= "BranchCode";
	public static final String HTTP_PARAM_PIN				= "Pin";
	public static final String HTTP_PARAM_BLAST_SMS_TIME	= "BlastSMSTime";
	public static final String HTTP_PARAM_BLAST_SMS			= "BlastSms";
	public static final String HTTP_PARAM_CLIENT			= "client";
	public static final String HTTP_PARAM_ENC_EMO			= "enc";
	public static final String HTTP_PARAM_DATA_EMO			= "data";
	
	public  static  final  String BANK_CODE_SULTENG			= "134";
	

	public static final String CURR_IDR						= "IDR";
	public static final String ACCOUNT_MODIFY_STATUS_UNCHANGED = "U";
	
	public static final String STATUS_ACTIVE 				= "A";
	public static final String STATUS_TERMINATE 			= "T";
	public static final String STATUS_BLOCK 				= "B";
	
	public static final String UPDATED_BY_SYSTEM 			= "SYS";
	
	public static final String AUTH_STATUS_NO_NEED_AUTH		= "N";
	public static final String AUTH_STATUS_AUTHORIZED		= "A";
	public static final String AUTH_STATUS_PENDING			= "Y";
	
	// action for sms notifikasi
	public static final String ACTION_REGIS					= "REG";
	public static final String ACTION_UNREGIS				= "UNREG";
	public static final String ACTION_DO_NOTHING			= "NOTHING";
	
	public static final String LANGUAGE_ENGLISH				= "EN";
	public static final String LANGUAGE_INDONESIAN			= "ID";
	
	public static final String ACT_TYPE_INSERT				= "Insert";
	public static final String ACT_TYPE_UPDATE				= "Update";
	public static final String ACT_TYPE_DELETE				= "Delete";
	
	public static final int DB_TRUE							= 1;
	public static final int DB_FALSE						= 0;
	
	public static final String BILLER_PLN_PREPAID			= "30300";
	public static final String BILLER_PLN_PREPAID_ADVICE	= "30301";
	public static final String BILLER_PLN_PRUDENTIAL		= "23000";
	public static final String BILLER_PLN					= "00003";
	public static final String BILLER_PAM_PALYJA			= "00004";
	public static final String BILLER_PAM_AETRA				= "00041";
	public static final String BILLER_MANDIRI_CC			= "100";
	public static final String BILLER_GARUDA				= "101";
	public static final String BILLER_PBB					= "120";
	
	public static final String TRX_CODE_GET_LIST_ACCOUNT	= "GETLISTSRAC";
	
	//verification
	public static final String VERIFICATION_TYPE 			= "VerType";
	
	//UBP Constant
	public static final String BILL_PAYMENT_UBP_INQ			= "INQUBP";
	public static final String BILL_PAYMENT_UBP_SET			= "SETUBP";
	public static final String BILL_PAYMENT_H2H 			= "INQPayment";
	public static final String BILL_PAYMENT_OPEN 			= "SETPayment";
	public static final String BILL_PAYMENT_NEW 			= "INQNEW";
	public static final String BILL_PAYMENT_LINK 			= "LINKUBP";
	public static final String BILL_PAYMENT_PHONE 			= "INQPhone";
	
	public static final String PAYEE_CODE_CREDIT_CARD 		= "KK";
	public static final String PAYEE_CODE_PHONE 			= "HP";
		
	public static final int BILL_PAYMENT_TYPE_MANDIRI_CC 	= 4;
	public static final int BILL_PAYMENT_TYPE_UBP 			= 3;
	public static final int BILL_PAYMENT_TYPE_OPEN 			= 2;
	public static final int BILL_PAYMENT_TYPE_PHONE 		= 1;
	public static final int BILL_PAYMENT_TYPE_H2H 			= 0;
	
	public static final String BILL_PAYMENT_STATUS_ACTIVE	= "A";
	public static final String BILL_PAYMENT_STATUS_INACTIVE	= "I";
	
	public static final int STATUS_NOT_YET_REGISTER			= 0;
	public static final int STATUS_ALREADY_REGISTER			= 1;
	
	//fund transfer code
	public static final int FUND_TRANSFER_INHOUSE 			= -1;
	public static final int FUND_TRANSFER_INTERBANK 		= 0;
//	public static final int FUND_TRANSFER_AC 				= 0;
//	public static final int FUND_TRANSFER_RTGS 				= 1;
//	public static final int FUND_TRANSFER_ATM_BERSAMA 		= 2;
	
	public static final String CHECKER_NUM 	 				= "[0-9]{1,99}";
	public static final String CHECKER_ALPHANUM 			= "[0-9a-zA-Z]{1,99}";
	
	public static final String UBP_KEY_TYPE_NUM				= "1";
	public static final String UBP_KEY_TYPE_ALPHANUM		= "2";
	
	public static final int UBP_PAYMENT_TYPE_STANDARD		= 0;
	public static final int UBP_PAYMENT_TYPE_SINGLE			= 1;
	public static final int UBP_PAYMENT_TYPE_ALL			= 2;
	
	public static final String CHANNEL_TYPE_SMS 			= "SMS";
	public static final String CHANNEL_TYPE_USSD 			= "USSD";
	public static final String CHANNEL_TYPE_GPRS 			= "GPRS";
	public static final String CHANNEL_TYPE_WEB 			= "WEB";
	public static final String CHANNEL_TYPE_ISO 			= "ISO";
	
	public static final int PUSH_SMS_HEADER_ACTIVE			= 0;
	public static final int PUSH_SMS_HEADER_PROCESSED 		= 1;
	public static final int PUSH_SMS_HEADER_FINISH 			= 2;
	public static final int PUSH_SMS_HEADER_CANCELED 		= 3;
	
	public static final int PUSH_SMS_DETAIL_ACTIVE			= 0;
	public static final int PUSH_SMS_DETAIL_PROCESSED 		= 1;
	public static final int PUSH_SMS_DETAIL_SENT 			= 2;
	public static final int PUSH_SMS_DETAIL_FAILED 			= 3;
	
	public static final int PROMOTION_STATUS_ACTIVE			= 0;
	public static final int PROMOTION_STATUS_INACTIVE		= 1;
	public static final int PROMOTION_STATUS_EXPIRED		= 2;
	
	public static final String BILLER_CODE_FLEXI_POSTPAID	= "102";
	
//	public static final String BILLER_CODE_HALO					= "0001";
//	public static final String BILLER_CODE_PDAM_TIRTANADI		= "0002";
//	public static final String BILLER_CODE_XL_POSTPAID			= "0003";
//	public static final String BILLER_CODE_SPEEDY				= "0004";
//	public static final String BILLER_CODE_PSTN					= "0005";
//	public static final String BILLER_CODE_TRANSVISION			= "0006";
//	public static final String BILLER_CODE_INDOVISION			= "0007";
//	public static final String BILLER_CODE_AORA_TV				= "0008";
//	public static final String BILLER_CODE_PDAM_TIRTABULIAN		= "0009";
//	public static final String BILLER_CODE_ESAMSAT				= "0010";
//	public static final String BILLER_CODE_TAX_AND_RETRIBUTION	= "0011";
	
	public static final String BILLER_CODE_HALO					= "03005";
	public static final String BILLER_CODE_XL_POSTPAID			= "03014";
	public static final String BILLER_CODE_BPJS					= "03015";
	public static final String BILLER_CODE_SPEEDY				= "03002";
	public static final String BILLER_CODE_PSTN					= "03001";
	public static final String BILLER_CODE_INDOVISION			= "03006";
	public static final String BILLER_CODE_PBB_LUWUK			= "03016";
//	public static final String BILLER_CODE_INDOVISION			= "0007";
//	public static final String BILLER_CODE_AORA_TV				= "0008";
//	public static final String BILLER_CODE_PDAM_TIRTABULIAN		= "0009";
//	public static final String BILLER_CODE_ESAMSAT				= "0010";
//	public static final String BILLER_CODE_TAX_AND_RETRIBUTION	= "0011";
	
	public static final String KEY_CHANGE_PIN_ATM = "0123456789ABCDEF";
	
	public static final String KEY_PIN_ATM_SULTENG = "0123456789ABCDEF";
	
	public static final String DEFAULT_CARD_NUMBER          = "6274800000000000";
	
	public  static  final  String STAT_NO_NEED_CHANGE_PIN	= "N";
	public  static  final  String STAT_NEED_CHANGE_PIN		= "Y";
	
	public static final String TRX_CODE_FUND_TRF_OTH		= "FUND_TRF_OTH";
	
	public static final int NON_FINANCIAL 					= 2;
	public static final int FINANCIAL 						= 3;
	
	public static final String PRD_TYPE_AIR_TIME			= "AIR_TIME";
	public static final String PRD_TYPE_DATA_PACKAGE		= "DATA_PACK";
	
}
