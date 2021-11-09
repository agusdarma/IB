package id.co.emobile.samba.web.service;

public class SambaWebException extends Exception {
	private static final long serialVersionUID = 1L;

	public static final int NE_INSUFFICIENT_BALANCE = 99998;
	public static final int NE_UNKNOWN_ERROR = 99999;
	public static final int NE_MISSING_INPUT = 10000;
	public static final int NE_SESSION_EXPIRED = 10001;
	public static final int NE_INVALID_USER = 10002;
	public static final int NE_DATA_NOT_FOUND = 10003;
	public static final int NE_USER_DATA_INACTIVE = 10004;
	public static final int NE_USER_DATA_BLOCKED = 10005;
	public static final int NE_USER_DATA_INVALID_PASS = 10006;
	public static final int NE_USER_DATA_INVALID_LEVEL = 10007;
	public static final int NE_SETTING_INVALID_TYPE = 10008;
	public static final int NE_SETTING_EMPTY = 10009;
	public static final int NE_DUPLICATE_DATA = 10010;
	public static final int NE_INVALID_INPUT = 10011;
	public static final int NE_WRONG_PASSWORD = 10012;
	public static final int NE_MUST_CHOOSE_MENU = 10013;
	public static final int NE_PASSWORD_DIFFERENT = 10014;
	public static final int NE_INVALID_PASSWORD_LENGTH = 10015;
	public static final int NE_PASSWORD_NEW_OLD_SAME = 10016;
	public static final int NE_INVALID_PASS_FORMAT = 10017;
	public static final int NE_OLD_PASSWORD_INVALID = 10018;
	public static final int NE_MUST_CHOOSE_TRANSACTION = 10019;
	public static final int NE_MASTER_DATA_REQUIRED = 10020;
	public static final int NE_MUST_CHOOSE_BANK = 10021;
	public static final int NE_USER_ALREADY_LOGIN = 10022;
	public static final int NE_NOT_A_VALID_IMAGE_FILE = 10023;
	public static final int NE_CANNOT_UPDATE_HAS_CONTENT = 10024;
	public static final int NE_CANNOT_BE_ZERO_VALUE = 10025;
	public static final int NE_WRONG_CAPTCHA = 10108;
	public static final int NE_TPDA_OR_USSDCODE_MUST_BE_FILLED = 10026;
	public static final int NE_FAILED_TO_UPLOAD_IMAGE = 10027;
	public static final int NE_LEAF_MENU_SHOULD_BE_A_TRANSACTION = 10028;
	public static final int NE_NOT_A_VALID_DIMENSION = 10029;
	
	public static final int NE_INVALID_OTP = 10030;
	public static final int NE_INVALID_DIST_CONTENT = 10031;
	public static final int NE_INVALID_SOURCE_ACCOUNT = 10032;
	public static final int NE_INVALID_SYSLOGNO			= 10033;
	public static final int NE_NOT_AUTHORIZED			= 10034;
	public static final int NE_INVALID_BALANCE			= 10035;
	public static final int NE_FAILED_PROCESS_DEBET_KREDIT			= 10036;
	public static final int NE_EXISTING_PROCESS_ALREADY_RUNNING = 10037;
	
	public static final int NE_INVALID_URI = 20000;
	public static final int NE_ERROR_RESPONSE_HOST = 20001;
	public static final int NE_CONNECTION_ERROR = 20002;
	public static final int NE_NO_RESPONSE_FROM_HOST = 20003;
	public static final int NE_INVALID_DESTINATION = 20004;
	
	public static final int NE_USSD_MENU_EMPTY = 30000;
	public static final int NE_APPS_MENU_CANNOT_DELETE_HAS_CHILDREN_MENU = 30001;
	public static final int NE_APPS_CONTENT_CANNOT_MORE_THAN_ONE_BILLER = 30002;
	public static final int NE_APPS_MENU_CANNOT_DELETE_HAS_CONTENT_MENU = 30003;
	
	public static final int NE_APPS_LIST_VALUE_CANNOT_DELETE_BECAUSE_PARENT_ID = 30004;
	
	public static final int NE_CONTENT_CODE_REQUIRED = 40000;
	
	public static final int NE_METHOD_NOT_ALLOWED	= 50000;
	
	private int errorCode;
	private String[] info;

	public SambaWebException(Throwable t) {
		super("rc." + NE_UNKNOWN_ERROR, t);
		this.errorCode = NE_UNKNOWN_ERROR;
		this.info = new String[] { t.getMessage() };
	}

	public SambaWebException() {
		super();
		this.errorCode = NE_UNKNOWN_ERROR;
	}

	public SambaWebException(int errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public SambaWebException(int errorCode, String[] info) {
		super();
		this.errorCode = errorCode;
		this.info = info;
	}

	public SambaWebException(int errorCode, String info) {
		super();
		this.errorCode = errorCode;
		this.info = new String[] { info };
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String[] getInfo() {
		return info;
	}

	public boolean hasInfo() {
		return (info != null) && (info.length > 0);
	}
}
