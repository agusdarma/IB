package id.co.emobile.samba.web.data;

public class ResultCode {

	public static final String ERR_UNKNOWN				= "X90"; // unknown error
	public static final String ERR_FAILED_CHECK			= "X91"; // failed check account
	public static final String ERR_NOT_CHECK			= "X92"; // not check account for MMBS
	
	public static final String ERR_UNABLE_PARSE_RESPONSE	= "X96";
	public static final String ERR_MISSING_DATA			= "X97"; // missing pin
	public static final String ERR_UNABLE_CONNECT_HOST	= "X98";
	public static final String ERR_INVALID_DATA_CHECK	= "X99"; // invalid data check
	public static final String ERR_INVALID_DATA_CHECK_PAJAK	= "99"; // invalid data check
	public static final String ERR_UNABLE_CONNECT_HOST_PAJAK	= "98";
	public static final String ERR_UNKNOWN_PAJAK				= "90"; // unknown error
	
	public static final String SUCCESS_CODE 					= "0";
	public static final String APPLID_PENAMPUNG 				= "00";
	public static final String RC_94 					= "94";
	
	public static final String MUTASI_EMPTY_RC 					= "73";
	
	// BTI error
	public static final String BTI_UNKNOWN_ERROR 				= "5000";
	public static final String BTI_TRANSACTION_EXPIRED 			= "5001";
	public static final String BTI_NETWORK_IO_ERROR 			= "5002";
	public static final String BTI_UNSUPPORTED_TRX				= "5003";
	public static final String BTI_ERROR_COMPOSE_ISO 			= "5004";
	public static final String BTI_NO_RESPONSE_FROM_HOST 		= "5005";
	public static final String BTI_UNKNOWN_STATE 				= "5006";
	public static final String BTI_UNDEFINED_PAYEE_CODE 		= "5007";
	public static final String BTI_ERROR_PARSE_ISO 				= "5008";
	public static final String BTI_NOT_SUPPORTED_CURR 			= "5009";
	public static final String BTI_NO_DEFAULT_ACC 				= "5010";
	public static final String BTI_MISSING_RESPONSE_CODE 		= "5011";
	public static final String BTI_BUYER_IS_NOT_REGISTERED 		= "5111";
	public static final String BTI_INVALID_DSAC 				= "5120";
	public static final String BTI_INVALID_BILL_INDEX 			= "5121";
	public static final String BTI_NO_HP_INVALID	 			= "5150";
	public static final String BTI_CIF_ID_DIFFERENT	 			= "5300";
	
	public static final String JETS_INVALID_DSAC	 			= "7040";
	
	// Jets error
	public static final String JETS_UNKNOWN_ERROR 				= "7000";
	public static final String JETS_DATABASE_ERROR 				= "7001";
	// if term requested in TransactionTO does not exists
	public static final String JETS_TERM_MISSING				= "7002"; 
	public static final String JETS_UNKNOWN_STATE 				= "7003";
	public static final String JETS_CACHE_TIMEOUT 				= "7004";
	// system error when setting is not completed / invalid
	public static final String JETS_SYSTEM_ERROR				= "7005"; 
	public static final String JETS_REQUEST_LOGIC_NOT_FOUND 	= "7006";
	public static final String JETS_RESPONSE_LOGIC_NOT_FOUND 	= "7007";
	
	public static final String JETS_CIF_NOT_FOUND				= "7011";
	public static final String JETS_NOT_ALLOWED_TRX 			= "7012";
	public static final String JETS_CIF_NOT_ACTIVE 				= "7013";
	public static final String JETS_CIF_HISTORY_NOT_FOUND		= "7014";
	public static final String JETS_INVALID_MIN_TRF				= "7015";
	public static final String JETS_LIMIT_EXCEEDS 		 		= "7016";
	
	public static final String JETS_FUND_FLOW_NOT_FOUND 		= "7017";
	public static final String JETS_UNDEFINED_ACCOUNT 			= "7018";
	public static final String JETS_CIF_NEED_CHG_PIN 			= "7019";
	public static final String JETS_ACCT_NOT_FOUND 				= "7020";
	
	public static final String JETS_ACCT_NOT_ACTIVE 			= "7021";
	public static final String JETS_INVALID_PIN 				= "7022";
	public static final String JETS_DIFF_CONF_PIN 				= "7024";
	public static final String JETS_UNSUPPORTED_SMS_TRX 		= "7025";
	public static final String JETS_UNKNOWN_FORMAT 				= "7026";
	public static final String JETS_INVALID_PIN_LENGTH 			= "7028";
	public static final String JETS_INVALID_BUYER_PHONE 		= "7029";
	public static final String JETS_INVALID_TELCO 				= "7030";
	public static final String JETS_INVALID_TOP_UP_AMOUNT 		= "7032";
	public static final String JETS_PIN_BLOCK 					= "7033";
	public static final String JETS_CACHE_NOT_FOUND 			= "7035";
	public static final String JETS_VER_PIN_DIFFERENT_CHANNEL	= "7036";
	public static final String JETS_UNKNOWN_PRODUCT 			= "7037";
	public static final String JETS_UNDEFINED_BILLER 			= "7056";
	public static final String JETS_UNKNOWN_PREFIX 				= "7068";
	public static final String JETS_ACCOUNT_INDEX_NOT_FOUND		= "7070";
	public static final String JETS_OLDPIN_NOT_MATCH_CURRENTPIN	= "7071";
	public static final String JETS_OLDPIN_EQUAL_NEWPIN			= "7072";
	public static final String JETS_NEWPIN_NOT_EQUAL_CONFPIN	= "7073";
	public static final String JETS_INVALID_CREDENTIAL			= "7080";
	public static final String JETS_MISSING_INPUT				= "7081";
	public static final String JETS_INVALID_BILL_KEY			= "7082";
	public static final String JETS_INVALID_AMOUNT				= "7083";
	public static final String JETS_INVALID_PIN_DUPLICATE 		= "7084";
	
	public static final String JETS_PRODUCT_NOT_AVAILABLE 		= "7088";
	public static final String JETS_DENOM_NOT_AVAILABLE 		= "7089";
	public static final String JETS_BANK_NOT_AVAILABLE 			= "7091";
	
	public static final String JETS_HOST_IS_BUSY				= "7999";
	
	// XTI
	public static final String JETS_FAILED_CONVERT_TO_JSON 		= "7121";
	public static final String JETS_FAILED_CONVERT_FROM_JSON 	= "7122";
	
	public static final String JETS_FAILED_SEND_TO_EMO_SWITCH 	= "7201";
	public static final String XTI_NETWORK_IO_ERROR 			= "7202";
}
