package id.co.emobile.samba.web.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import id.co.emobile.samba.web.entity.UserData;

public class AttributeCheckerUtilTest {

	@Test
	public void testCheckAttribute() {
		UserData userData = new UserData();
		userData.setId(1);;  // id INT NOT NULL,
	    userData.setUserCode("CODE001"); // user_code VARCHAR(16) NOT NULL,
	    userData.setUserName("Nama Satu");  // user_name VARCHAR(32) NOT NULL,
	    userData.setInvalidCount(0);  // invalid_count INT NOT NULL,
	    userData.setUserStatus(2);  // user_status IN)T NOT NULL,
	    
	    List<String> listAttribute = new ArrayList<String>();
	    listAttribute.add("id");
	    listAttribute.add("userCode");
	    listAttribute.add("userName");
	    listAttribute.add("invalidCount");
	    listAttribute.add("userStatus");
	    listAttribute.add("phoneNo");
	    listAttribute.add("groupId");
	    
	    List<List<String>> listChanged = AttributeCheckerUtil.checkForObject(userData, null, listAttribute.toArray(new String[0]));
	    for (List<String> list: listChanged) {
	    	String name = list.get(0);
	    	String oldValue = list.get(1);
	    	String newValue = list.get(2);
	    	System.out.println(" -> " + name + ":" + oldValue + " <> " + newValue);
	    }
	    
	    UserData oldData = new UserData();
	    oldData.setId(1);;  // id INT NOT NULL,
	    oldData.setUserCode("CODE002"); // user_code VARCHAR(16) NOT NULL,
	    oldData.setUserName("Nama Satu");  // user_name VARCHAR(32) NOT NULL,
	    oldData.setInvalidCount(1);  // invalid_count INT NOT NULL,
//	    oldData.setUserStatus(2);  // user_status IN)T NOT NULL,
	    oldData.setPhoneNo("08123123");  // user_status IN)T NOT NULL,
	    oldData.setGroupId(3);  // user_status IN)T NOT NULL,
	    
	    System.out.println("===========");
	    List<List<String>> listChanged2 = AttributeCheckerUtil.checkForObject(userData, oldData, listAttribute.toArray(new String[0]));
	    for (List<String> list: listChanged2) {
	    	String name = list.get(0);
	    	String oldValue = list.get(1);
	    	String newValue = list.get(2);
	    	System.out.println(" -> " + name + ":" + oldValue + " <> " + newValue);
	    }
	}
}
