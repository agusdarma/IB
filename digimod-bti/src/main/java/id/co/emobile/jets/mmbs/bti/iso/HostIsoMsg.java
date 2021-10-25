package id.co.emobile.jets.mmbs.bti.iso;

public class HostIsoMsg extends IsoMsg {

	 private String primaryBitmapLength[] =
	 {
		     "16", "LL",  "6", "12",  "0",  "0", "10",  "0",  //8
		      "0",  "0",  "6",  "6",  "4",  "4",  "4",  "0",  //16
		      "0",  "4",  "0",  "0",  "0",  "3",  "0",  "0",  //24
		      "0",  "0",  "0",  "0",  "0",  "0",  "0", "LL",  //32
		      "0",  "0", "LL",  "0", "12",  "0",  "2",  "0",  //40
		      "8", "15", "40",  "0",  "0",  "0",  "0",  "LLL",  //48
		      "3",  "0",  "0", "16",  "0","LLL",  "0",  "0",  //56
		      "0",  "0",  "0",  "0","LLL",	"LLL","LLL",  "0"   //64
	 };
	 
	 private String secondaryBitmapLength[] =
	 {
		      "0", "0", "0", "0", "0",  "3",  "0",  "0",  //72
		      "0", "0", "0", "0", "0",  "0",  "0",  "0",  //80
		      "0", "0", "0", "0", "0",  "0",  "0",  "0",  //88
		      "0","42", "0", "0", "0",  "0",  "0",  "0",  //96
		      "0", "0", "0", "0", "0",  "LL",  "0",  "0",  //104
		      "0", "0", "0", "0", "0",  "0",  "0",  "0",  //112
		      "0", "0", "0", "0", "0",  "0",  "0",  "0",  //120
		      "0", "0", "0", "0", "0",  "0",  "0",  "0"   //128
	 };
	
	public HostIsoMsg() {
		super();
	}
	
	public HostIsoMsg(IsoMsgHeader header) {
		super(header);
	}
	
	@Override
	protected String getPrimaryBitmapLength(int index) {
		return primaryBitmapLength[index-1];
	}

	@Override
	protected String getSecondaryBitmapLength(int index) {
		return secondaryBitmapLength[index-1];
	}

}
