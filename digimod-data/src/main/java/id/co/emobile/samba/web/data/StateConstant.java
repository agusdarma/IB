package id.co.emobile.samba.web.data;

public class StateConstant {
	//cif registration
	public  static  final  int  STATE_CIF_REG_CHECK_ACC 	= 0;
	public  static  final  int  STATE_CIF_REG_AUTH 			= 0;
	
	public  static  final  int  STATE_SETTLE 				= 0;
	public  static  final  int  STATE_INQUIRY 				= 1;
	public  static  final  int  STATE_NOTIF 				= 99;
	
	//wakaf payment
		public static final int WAKAF_SETTLEMENT 		= 0;
		public static final int WAKAF_INQUIRY 			= 1;
	
	//account balance state
	public static final int ACC_BAL_INQ 					= 1;
	public static final int ACC_BAL_CHECK_BAL 				= 0;
		
	//account statement state
	public static final int ACC_STAT_INQUIRY 				= 1;
	public static final int ACC_STAT_VALIDATE_PIN 			= 0;
		
	//change trx account state
	public static final int CHG_ACC_GET_LIST 				= 2;
	public static final int CHG_ACC_INQ_PIN 				= 1;
	public static final int CHG_ACC_APPROVE 				= 0;
		
	//credit card balance
	//credit card balance state
	public static final int CC_BAL_VALIDATE_PIN 			= 1;
	public static final int CC_BAL_INQUIRY 					= 0;
	
	//fund transfer state
	public static final int FUND_TRANSFER_SETTLEMENT 		= 0;
	public static final int FUND_TRANSFER_INQUIRY 			= 1;
	public static final int FUND_TRANSFER_REQUIRE_TEXT 		= 2;
	public static final int FUND_TRANSFER_REQUIRE_BENEF 	= 3;
	public static final int FUND_TRANSFER_REQUIRE_BANK 		= 4;
	public static final int FUND_TRANSFER_REQUIRE_RESIDENT 	= 5;
	

	//fund transfer atm code
	public static final int FUND_TRANSFER_ATM_BERSAMA 		= 2;
	public static final int FUND_TRANSFER_RTGS 				= 1;

	//purchase state
	public static final int PURCHASE_SETTLEMENT 			= 1;
	public static final int PURCHASE_REVERSAL 				= 2;
	public static final int PURCHASE_COMPLETE 				= 0;
	
	//merchant initiated payment state
	// this 2 state MER_INIT_CHK_BUYER n MER_INIT_GET_DEF_ACC is not used anymore,
	// provided here as backward compatibility
	public static final int MER_INIT_CHK_BUYER 				= 3;  // not used anymore
	public static final int MER_INIT_GET_DEF_ACC 			= 2;  // not used anymore
	public static final int MER_INIT_CHECK_MER_PIN 			= 1;
	public static final int MER_INIT_SETTLE 				= 0;
	
	//buyer initiated payment state
	public static final int BYR_INIT_CHECK_BYR_PIN 			= 1;
	public static final int BYR_INIT_SETTLE 				= 0;
	
	//bp payment state
	public static final int BILL_PAYMENT_GET_DEF_ACC_PIN 	= 3;
	public static final int BILL_PAYMENT_GET_AMOUNT 		= 2;
	public static final int BILL_PAYMENT_INQ 				= 1;
	public static final int BILL_PAYMENT_SETTLE 			= 0;
	
	public static final int BILL_PAYMENT_TYPE_MANDIRI_CC 	= 4;
	public static final int BILL_PAYMENT_TYPE_UBP 			= 3;
	public static final int BILL_PAYMENT_TYPE_OPEN 			= 2;
	public static final int BILL_PAYMENT_TYPE_PHONE 		= 1;
	public static final int BILL_PAYMENT_TYPE_H2H 			= 0;
	
	// bp presentment state
	public static final int BILL_PRESENTMENT_ASK_PIN 		= 1;
	public static final int BILL_PRESENTMENT_SETTLE 		= 0;
	
	// airtime refill state
	public static final int AIRTIME_REFILL_GET_AMNT 		= 3;
	public static final int AIRTIME_REFILL_INQ 				= 1;
	public static final int AIRTIME_REFILL_SETTLE 			= 0;
	
	// cif registration state
	public static final int REGISTRATION_INFO				= 2;
	public static final int REGISTRATION_INQ				= 1;
	public static final int REGISTRATION_SETTLE				= 0;
	
	// change pin state
	public static final int CHANGE_PIN_INQ					= 1;
	public static final int CHANGE_PIN_SETTLE				= 0;
	
	public static final int NOTIF							= 99;
}
