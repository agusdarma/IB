package id.co.emobile.samba.web.data;

import java.util.HashMap;
import java.util.Map;

public class WebConstants {
	
	public static final String HTTP_KEY_EMAIL		= "email";
	public static final String HTTP_KEY_PASSWORD	= "password";
	public static final String HTTP_KEY_SESSION		= "session";
	public static final String HTTP_KEY_MYFXBOOKID	= "id";
	
	public static final String MYFXBOOK_EMAIL		= "agusdk2011@gmail.com";
	public static final String MYFXBOOK_PASSWORD	= "r4H4s14181014";
	public static final String MYFXBOOK_URL_LOGIN	= "https://www.myfxbook.com/api/login.json";
	public static final String MYFXBOOK_URL_LOGOUT	= "https://www.myfxbook.com/api/logout.json";
	public static final String MYFXBOOK_URL_GET_HISTORY_TRADING	= "https://www.myfxbook.com/api/get-history.json";
	
	
	public static final int INQ		= 1;
	public static final int SETT	= 0;
	public static final String WEB_PARAM_BIT48_KEY		= "WEB_PARAM_BIT48_KEY";
	
	// status: 1: uploaded by maker, 2: checked by checker, 3: approved by callback, 4: approved by approval,  
	//                              12: rejected by checker, 13: rejected by callback, 14: rejected by approval
	//                              50: process distribution, 51: distribution finished, 52: dist partial finished
	public static final int DIST_STATUS_UPLOADED	= 1;
	public static final int DIST_STATUS_CHECKED		= 2;
	public static final int DIST_STATUS_CALLBACK	= 3;
	public static final int DIST_STATUS_APPROVED	= 4;
	public static final int DIST_STATUS_PROCESSING	= 50;
	public static final int DIST_STATUS_FINISHED	= 51;
	public static final int DIST_STATUS_PARTIAL_FIN	= 52;
	public static final int DIST_STATUS_FAILED	= 53;
	
	public static final int PAJAK_DIST_STATUS_UPLOADED	= 100;
	public static final int PAJAK_DIST_STATUS_CHECKED	= 200;
	public static final int PAJAK_DIST_STATUS_CALLBACK	= 300;
	public static final int PAJAK_DIST_STATUS_APPROVED	= 400;
	public static final int PAJAK_DIST_STATUS_PROCESSING	= 500;
	public static final int PAJAK_DIST_STATUS_FINISHED	= 510;
	public static final int PAJAK_DIST_STATUS_PARTIAL_FIN	= 520;
	
	public static final int PAJAK_DIST_STATUS_REJECTED_CHECKER	= 120;
	public static final int PAJAK_DIST_STATUS_REJECTED_CALLBACK	= 130;
	public static final int PAJAK_DIST_STATUS_REJECTED_APPROVAL	= 140;
	
	
	
	public static final int DIST_STATUS_REJECTED_CHECKER	= 12;
	public static final int DIST_STATUS_REJECTED_CALLBACK	= 13;
	public static final int DIST_STATUS_REJECTED_APPROVAL	= 14;
	
	//process_status: 1: init, 2: success, 3: failed, 21: skip success, 22: skip failed
	public static final int PROCESS_STATUS_INIT		= 1;
	public static final int PROCESS_STATUS_SUCCESS	= 2;
	public static final int PROCESS_STATUS_FAILED	= 3;
	public static final int PROCESS_STATUS_SKIP_SUCCESS	= 21;
	public static final int PROCESS_STATUS_SKIP_FAILED	= 22;
	
	public static final int LEVEL_TYPE_NORMAL		= 0;
	public static final int LEVEL_TYPE_MAKER		= 1;
	public static final int LEVEL_TYPE_CHECKER		= 2;
	public static final int LEVEL_TYPE_CALLBACK		= 3;
	public static final int LEVEL_TYPE_APPROVAL		= 4;
	
//	Account Status (0-Not Active, 1-Active, 3-Block, 7-Passive, 9-Close )
	public static final int ACC_STATUS_INACTIVE		= 0;
	public static final int ACC_STATUS_ACTIVE		= 1;
	public static final int ACC_STATUS_BLOCK		= 3;
	public static final int ACC_STATUS_PASSIVE		= 7;
	public static final int ACC_STATUS_CLOSE		= 9;
	
	
	// wd status
	public static final int WD_STATUS_PENDING		= 1;
	public static final int WD_STATUS_PROCESS		= 2;
	public static final int WD_STATUS_COMPLETED		= 3;
	
	public static final Map<Integer, String> MAP_ACC_STATUS = new HashMap<Integer, String>();
	static {
		MAP_ACC_STATUS.put(ACC_STATUS_INACTIVE, "Not Active");
		MAP_ACC_STATUS.put(ACC_STATUS_ACTIVE, "Active");
		MAP_ACC_STATUS.put(ACC_STATUS_BLOCK, "Blocked");
		MAP_ACC_STATUS.put(ACC_STATUS_PASSIVE, "Passive");
		MAP_ACC_STATUS.put(ACC_STATUS_CLOSE, "Close");
	}
	
	public static final String[] ARRAY_PAGING		= {"25", "50", "100"};
	public static final int DEFAULT_ROW_PER_PAGE	= 25;
	public static final String[] ARRAY_TRX_TYPE		= {"Finansial", "Non Finansial", "Other", "Total"};
	public static final String[] ARRAY_TIME			= {"00:00 - 06:00", "06:00 - 12:00", "12:00 - 18:00", "18:00 - 00:00"};

	public static final String FOLDER_MERCHANT = "folder_merchant";
	public static final String FOLDER_BANK = "folder_bank";
	public static final String FOLDER_ADVERTISING = "folder_advertising";
	public static final String FOLDER_ADVERTISING_FULL = "folder_advertising_full";
	public static final String FOLDER_MENU = "folder_menu";
	public static final String FOLDER_HOME_SCREEN = "folder_homeScreen";
	
	public static final int HAS_NOT_CHANGE_PASS = 0;
	public static final int HAS_CHANGE_PASS = 1;
	
	public static final String VERSIONING_SUFFIX = "_r";
	
	public static final String ENGINE_COMMAND_STATEMENT 		= "cmd";
	public static final String ENGINE_SUBCOMMAND_STATEMENT 		= "subcmd";
	public static final String ENGINE_COMMAND_RELOAD			= "reload";
	public static final String ENGINE_SUBCOMMAND_RELOAD_SETTING			= "setting";
	public static final String ENGINE_SUBCOMMAND_RELOAD_MENU			= "menu";
	public static final String ENGINE_SUBCOMMAND_RELOAD_ADVERTISING		= "advertising";
	public static final String ENGINE_SUBCOMMAND_RELOAD_PARAM			= "param";
	public static final String ENGINE_SUBCOMMAND_RELOAD_WHITELIST		= "whitelist";
	public static final String ENGINE_SUBCOMMAND_RELOAD_SMS_PREFIX		= "smsprefix";
	public static final String ENGINE_SUBCOMMAND_BANK_CODE	= "bankcode";
	
	public static final String TABLE_TRX_HEADER	= "tbl_trx_header";
	public static final String TABLE_TRX_DETAIL	= "tbl_trx_detail";
	public static final String TABLE_SMS_NOTIF  = "tbl_sms_notif";
	
	public static final String TABLE_TRXLOGH	= "trxlogh";
	public static final String TABLE_TRXLOGD	= "trxlogd";
	
	public static final String[] ARRAY_HOUR	= {"00", "01", "02", "03", "04", "05", "06",
		"07", "08", "09", "10", "11", "12",
		"13", "14", "15", "16", "17", "18",
		"19", "20", "21", "22", "23"};

	public static final String TRX_TYPE_FIN			= "Finansial";
	public static final String TRX_TYPE_NON_FIN		= "Non Finansial";
	public static final String TRX_TYPE_OTHER		= "Other";
	public static final int STATUS_INACTIVE		= 0;
	public static final int STATUS_ACTIVE		= 1;
	public static final int STATUS_LOCK			= 2;

	public static final int RESULT_SUCCESS	= 0;

	public static final int	TYPE_INSERT		= 0;
	public static final int TYPE_UPDATE		= 1;

	public static final int DB_FALSE			= 0;
	public static final int DB_TRUE				= 1;

	public static final boolean DB_FALSE_BOOLEAN			= false;
	public static final boolean DB_TRUE_BOOLEAN				= true;

	public static final String SORT_ORDER_ASC	="ASC";
	public static final String SORT_ORDER_DESC	="DESC";

	public static final int FULL_ACCESS				= 1;
	public static final int READ_ONLY				= 0;

	public static final int DAILY				= 3;
	public static final int WEEKLY				= 2;
	public static final int MONTHLY				= 1;

	public static final int VERSION_STATUS_UNCHANGED		= 0;
	public static final int VERSION_STATUS_CHANGED			= 1;
	public static final int VERSION_STATUS_DELETE			= 2;
	public static final int APPS_WORDING_CAT_ICON			= 1;
	public static final int APPS_WORDING_CAT_CONTENT		= 2;
	public static final int APPS_WORDING_CAT_MENU_HEADER	= 3;
	
	public static final String DISPLAY_FORMAT_DATETIME 		= "dd-MMM-yyyy HH:mm:ss";
	public static final String DISPLAY_FORMAT_AMOUNT 		= "#,##0";
	public static final String DISPLAY_FORMAT_DATE 		= "dd-MMM-yyyy";

	public static final int LOGIN_TYPE_NONE				= 0;
	public static final int LOGIN_TYPE_ALLOWED_FIRST	= 1;
	public static final int LOGIN_TYPE_ALLOWED_LAST		= 2;

	public static final String CHAR_EMPTY_STR	= "";
	public static final String NULL	= null;
	public static final String CHAR_DOT			= ".";

	public static final String STAT_NO_NEED_AUTHORIZATION= "N";
	public static final String STAT_NOT_AUTHORIZED= "Y";
	public static final String STAT_AUTHORIZED= "A";
	public static final String STAT_REJECTED= "R";

	/* mobile service at menu auth customer */
	public static final String M_ATM= "mATM";
	public static final String SMS_TEXT_PLAIN= "SMS Text Plain";
	public static final String WALLET= "Wallet";

	public static final String STAT_ACTIVE= "A";
	public static final String JDBC_KEY	= "3M0b1lEp@5$wOrDdEfenDe12z";

	//css default
	public static final String DEFAULT_THEME		= "Telkomsel";
	public static final String DEFAULT_FONTFAMILY	= "Utsaah Regular";
	public static final String DEFAULT_FONTSIZE		= "Medium";
	public static final String DEFAULT_LANGUAGE		= "English";

	public static final String NULL_STRING_PARAM	= "^";

	public static final int CARDWORKS_URL_ID	= 16;
	public static final int CARDWORKS_PARAM_ID	= 17;
	public static final int CARDWORKS_KEY1_ID	= 18;
	public static final int CARDWORKS_KEY2_ID	= 19;
	
	public static final String TABLE_CONJUNCTION = "_";
	public static final String TABLE_PREFIX_USSD_MENU = "ussd_menu";
	public static final String TABLE_PREFIX_USSD_PARAM_DATA = "ussd_param_data";
	public static final String TABLE_PREFIX_SUB_PARAM_DATA = "sub_param_data";
	
	public static final String ACT_TYPE_LOGIN			= "Login";
	public static final String ACT_TYPE_LOGOFF			= "Logoff";
	public static final String ACT_TYPE_INSERT			= "Insert";
	public static final String ACT_TYPE_UPDATE			= "Update";
	public static final String ACT_TYPE_DELETE			= "Delete";

	public static final String ACT_TABLE_USER_DATA			= "USER_DATA";
	public static final String ACT_TABLE_USER_LEVEL			= "USER_LEVEL";
	public static final String ACT_TABLE_USER_PREFERENCE	= "USER_PREFERENCE";
	public static final String ACT_TABLE_BANK				= "BANK";
	public static final String ACT_TABLE_MERCHANT			= "MERCHANT";
	public static final String ACT_TABLE_ADVERTISING		= "ADVERTISING";
	
	public static final String ACT_TABLE_SOURCE_ACCOUNT		= "SOURCE_ACCOUNT";
	public static final String ACT_TABLE_USER_GROUP			= "USER_GROUP";
	public static final String ACT_TABLE_USER_GROUP_APPROVAL			= "USER_GROUP_APPROVAL";
	
	public static final String ACT_TABLE_APPS_MENU_TREE		= "APPS_MENU_TREE";
	public static final String ACT_TABLE_APPS_MENU_CONTENT	= "APPS_MENU_CONTENT";
	public static final String ACT_TABLE_APPS_WORDING		= "APPS_WORDING";
	public static final String ACT_TABLE_APPS_ICON			= "APPS_ICON";
	public static final String ACT_TABLE_APPS_LIST_CATEGORY	= "APPS_LIST_CATEGORY";
	public static final String ACT_TABLE_APPS_LIST_CONTENT	= "APPS_LIST_CONTENT";
	public static final String ACT_TABLE_APPS_LIST_CATEGORY_BILLER	= "APPS_LIST_BILLER_CATEGORY";
	public static final String ACT_TABLE_APPS_LIST_CONTENT_BILLER	= "APPS_LIST_BILLER";
	public static final String ACT_TABLE_APPS_TRX_TYPE		= "APPS_TRX_TYPE";
	public static final String ACT_TABLE_APPS_MESSAGE_SUCCESS = "APPS_MESSAGE_SUCCESS";
	public static final String ACT_TABLE_MERCHANT_BANK		= "MERCHANT_BANK";
	public static final String ACT_TABLE_APPS_VERSIONING	= "VERSIONING";
	
	public static final String ACT_MODULE_LOGIN				="Login";
	public static final String ACT_MODULE_LOGOFF			="Logoff";
	public static final String ACT_MODULE_USER_LEVEL		="User Level";
	public static final String ACT_MODULE_USER_DATA			="User Data";
	public static final String ACT_MODULE_CHANGE_PASSWORD	="Change Password";
	public static final String ACT_MODULE_RESET_PASSWORD	="Reset Password";
	public static final String ACT_MODULE_USER_PREFERENCE	="User Preference";
	
	/*Module system support*/
	public static final String ACT_MODULE_SOURCE_ACCOUNT		="Source Account";
	public static final String ACT_MODULE_USER_GROUP			="User Group";
	public static final String ACT_MODULE_BANK					="Manage Bank";
	public static final String ACT_MODULE_MERCHANT				="Manage Merchant";
	public static final String ACT_MODULE_ADVERTISING			="Manage Advertising";
	public static final String ACT_MODULE_MIGRATE_APPS			="Migrate Apps Data";
	public static final String ACT_MODULE_GROUP_APPROVAL		="Group Approval";
	
	/*Module apps*/
	public static final String ACT_MODULE_APPS_WORDING			="Apps Wording";
	public static final String ACT_MODULE_APPS_ICON				="Apps Menu Logo";
	public static final String ACT_MODULE_APPS_MENU				="Apps Menu";
	public static final String ACT_MODULE_APPS_CONTENT			="Apps Content";	
	public static final String ACT_MODULE_APPS_LIST_CATEGORY	="Category";	
	public static final String ACT_MODULE_APPS_LIST_CONTENT		="List Value";
	public static final String ACT_MODULE_APPS_LIST_CATEGORY_BILLER		="Category Biller";	
	public static final String ACT_MODULE_APPS_LIST_CONTENT_BILLER		="List Value Biller";	
	public static final String ACT_MODULE_APPS_TRX_TYPE			="Apps Menu Trx Type";
	public static final String ACT_MODULE_APPS_MESSAGE_SUCCESS	="Apps Message Success";
	
	//PATH UPDATE
	/*SECURITY*/
	public static final String PATH_UPDATE_USER_DATA			="UserData!execute.web";
	public static final String PATH_UPDATE_USER_LEVEL			="UserLevel!execute.web";
	public static final String PATH_UPDATE_CHANGE_PASSWORD		="ChangePassword!execute.web";
	public static final String PATH_UPDATE_RESET_PASSWORD		="ResetPassword!execute.web";
	
	/*SYSTEM SUPPORT*/
	public static final String PATH_UPDATE_SOURCE_ACCOUNT		="SourceAccount!execute.web";
	public static final String PATH_UPDATE_USER_GROUP			="UserGroup!execute.web";
	public static final String PATH_UPDATE_USER_BRANCH			="UserBranch!execute.web";
	public static final String PATH_UPDATE_USER_GROUP_APPROVAL ="GroupApproval!execute.web";
	public static final String PATH_UPDATE_MASTER_TRADING_ACCOUNT			="MstTradeAccount!execute.web";
	
	public static final String PATH_UPDATE_SYSTEM_SETTING		="SystemSetting!execute.web";
	public static final String PATH_UPDATE_BANK					="Bank!execute.web";
	public static final String PATH_UPDATE_MERCHANT				="Merchant!execute.web";
	public static final String PATH_UPDATE_ADVERTISING			="Ads!execute.web";
	public static final String PATH_UPDATE_PAIR_MERCHANT_BANK	="PairMerchantBank!execute.web";
	
	/*APPS SETTING*/
	public static final String PATH_UPDATE_APPS_WORDING					="AppsWording!execute.web";
	public static final String PATH_UPDATE_APPS_MENU_LOGO				="AppsMenuLogo!execute.web";
	public static final String PATH_UPDATE_APPS_HOME_SCREEN				="AppsMenuLogo!homeScreen.web";
	public static final String PATH_UPDATE_APPS_MENU					="AppsMenu!execute.web";
	public static final String PATH_UPDATE_APPS_CONTENT					="AppsContent!execute.web";
	public static final String PATH_UPDATE_APPS_MENU_TRX_TYPE			="AppsMenuTrxType!execute.web";
	public static final String PATH_UPDATE_APPS_MESSAGE_SUCCESS			="AppsMessageSuccess!execute.web";
	public static final String PATH_UPDATE_APPS_LIST_CATEGORY			="AppsContentListCategory!execute.web";
	public static final String PATH_UPDATE_APPS_LIST_CONTENT			="AppsContentList!execute.web";
	public static final String PATH_UPDATE_APPS_LIST_CATEGORY_BILLER	="AppsContentListCategoryBiller!execute.web";
	public static final String PATH_UPDATE_APPS_LIST_CONTENT_BILLER		="AppsContentListBiller!execute.web";
	
	public static final String PATH_UPDATE_CARDWORKS_SETTING	="CardworksSetting!execute.web";
	public static final String PATH_UPDATE_WHITELIST			="WhitelistGateway!execute.web";
	public static final String PATH_UPDATE_LOOKUP_C81			="UssdParamData!gotoEditLookup.web";
	public static final String PATH_UPDATE_SMS_PREFIX			="SmsPrefix!execute.web";
	public static final String PATH_UPDATE_TRX_CODE_INFO		="TrxCodeInfo!execute.web";
	public static final String PATH_UPDATE_TRX_TYPE				="TransactionType!execute.web";
	public static final String PATH_UPDATE_LOOKUP_DATA			="LookupData!execute.web";
	public static final String PATH_UPDATE_USER_LEVEL_TYPE_DATA ="UserLevelType!execute.web";
	public static final String PATH_UPDATE_MAPPING_BANK			="MappingBank!execute.web";
	
	public static final int[] GraphRGB1 = {250, 150, 40};
	public static final int[] GraphRGB2 = {60, 180, 75};
	public static final int[] GraphRGB3 = {45, 170, 225};
	public static final int[] GraphRGB4 = {239, 70, 60};
	public static final int[] GraphRGB5 = {100, 60, 20};
	public static final int[] GraphRGB6 = {40, 50, 100};
	public static final int[] GraphRGB7 = {215, 225, 80};
	public static final int[] GraphRGB8 = {175, 75, 160};
	public static final int[] GraphRGB9 = {100, 100, 100};
	public static final int[] GraphRGB10 = {225, 225, 225};
	public static final int[] GraphRGB11 = {0, 0, 0};

	public static final int[][] GraphPrimaryColor = {GraphRGB1, GraphRGB2, GraphRGB3, GraphRGB4, GraphRGB5, GraphRGB6, GraphRGB7, GraphRGB8, GraphRGB9, GraphRGB10, GraphRGB11};

	public static final String PREFIX_URI = "/";
	public static final String SUFFIX_URI = ".web";
	public static final String[] AJAX_PROCESS = new String[] {
		PREFIX_URI + "UserData!processInput" + SUFFIX_URI,
		PREFIX_URI + "ChangePassword!process" + SUFFIX_URI, 
		PREFIX_URI + "ResetPassword!processInput" + SUFFIX_URI,
		PREFIX_URI + "UserLevel!processInput" + SUFFIX_URI,
		PREFIX_URI + "SystemSetting!processInput" + SUFFIX_URI,
		PREFIX_URI + "Ads!processInput" + SUFFIX_URI,
		PREFIX_URI + "Bank!processInput" + SUFFIX_URI,
		PREFIX_URI + "GenerateSummaryData!generateSummaryData" + SUFFIX_URI,
		PREFIX_URI + "Merchant!processInputNoBiller" + SUFFIX_URI,
		PREFIX_URI + "AppsContentListBiller!processInput" + SUFFIX_URI,
		PREFIX_URI + "AppsContentListCategoryBiller!processInput" + SUFFIX_URI,
		PREFIX_URI + "AppsContentListCategory!processInput" + SUFFIX_URI,
		PREFIX_URI + "AppsContentList!processInput" + SUFFIX_URI,
		PREFIX_URI + "AppsContent!processPopup" + SUFFIX_URI,
		PREFIX_URI + "AppsContent!deleteAppsContent" + SUFFIX_URI,
		PREFIX_URI + "AppsMenuLogo!processHomeScreen" + SUFFIX_URI,
		PREFIX_URI + "AppsMenuLogo!processInput" + SUFFIX_URI,
		PREFIX_URI + "AppsMenu!processPopupNoBiller" + SUFFIX_URI,
		PREFIX_URI + "AppsMenu!deleteMenuNoBiller" + SUFFIX_URI,
		PREFIX_URI + "AppsMenuTrxType!processInput" + SUFFIX_URI,
		PREFIX_URI + "AppsWording!processInput" + SUFFIX_URI,
		PREFIX_URI + "AppsMessageSuccess!processInput" + SUFFIX_URI,
	};
}
