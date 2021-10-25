package id.co.emobile.samba.web.utils;

import java.beans.PropertyDescriptor;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class AttributeCheckerUtil {
	private static final Logger LOG = LoggerFactory.getLogger(AttributeCheckerUtil.class);
	
	private static ThreadLocal<NumberFormat> nf = new ThreadLocal<NumberFormat>() {
		@Override
		protected NumberFormat initialValue() {
			return new DecimalFormat("###0");
		}
	};
	private static ThreadLocal<NumberFormat> nfDecimal = new ThreadLocal<NumberFormat>() {
		@Override
		protected NumberFormat initialValue() {
			return new DecimalFormat("###0.00");
		}
	};
	private static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
	};

	public static List<List<String>> checkForObject(Object newObject, Object oldObject, String[] listAttribute) {
		LOG.info("listAttribute " + listAttribute);
		List<List<String>> listChanged = new ArrayList<List<String>>();
		if (oldObject == null) {
			if(listAttribute !=null) {
				for (String attribute: listAttribute) {
					try {
						PropertyDescriptor desc = BeanUtils.getPropertyDescriptor(newObject.getClass(), attribute);
						Object obj = desc.getReadMethod().invoke(newObject, new Object[] {});
						checkAttribute(listChanged, attribute, obj, null);
					} catch (NullPointerException npe) {
					} catch (Exception e) {
						LOG.warn("Error reading " + attribute + " from newObject: " + e.getMessage(), e);
					}
				}	
			}
			
		} else {
			if(listAttribute !=null) {
				for (String attribute: listAttribute) {
					PropertyDescriptor desc = BeanUtils.getPropertyDescriptor(newObject.getClass(), attribute);
					Object objNew = null;
					try {
						objNew = desc.getReadMethod().invoke(newObject, new Object[] {});
					} catch (NullPointerException npe) {
					} catch (Exception e) {
						LOG.warn("Error reading " + attribute + " from newObject: " + e.getMessage());
					}
					Object objOld = null;
					try {
						objOld = desc.getReadMethod().invoke(oldObject, new Object[] {});
					} catch (NullPointerException npe) {
					} catch (Exception e) {
						LOG.warn("Error reading " + attribute + " from oldObject: " + e.getMessage());
					}
					checkAttribute(listChanged, attribute, objNew, objOld);
				}	
			}
			
		}
		return listChanged;
	}
	
	private static void checkAttribute(List<List<String>> listChanged, String fieldName, Object newValue, Object oldValue) {
		if (newValue == null && oldValue == null) {
			// nothing to do, both value are null
		} else {
			String newString;
			String oldString;
			if (newValue instanceof String || oldValue instanceof String) {
				newString = (String) newValue;
				oldString = (String) oldValue;
			} else if (newValue instanceof Integer || oldValue instanceof Integer) {
				newString = (newValue == null? null:nf.get().format(newValue));
				oldString = (oldValue == null? null:nf.get().format(oldValue));				
			} else if (newValue instanceof Double || oldValue instanceof Double) {
				newString = (newValue == null? null:nfDecimal.get().format(newValue));
				oldString = (oldValue == null? null:nfDecimal.get().format(oldValue));				
			} else if (newValue instanceof Date || oldValue instanceof Date) {
				newString = (newValue == null? null:sdf.get().format(newValue));
				oldString = (oldValue == null? null:sdf.get().format(oldValue));				
			} else {
				newString = (newValue == null? null:newValue.toString());
				oldString = (oldValue == null? null:oldValue.toString());	
			}
			if (newValue == null || !newValue.equals(oldValue)) {
				boolean needMasked = "sracPin".equals(fieldName);
				List<String> list = new ArrayList<String>();
				list.add(fieldName);
				if (needMasked) {
					list.add("***");
					list.add("***");
				} else {
					list.add(oldString);
					list.add(newString);
				}
				listChanged.add(list);
			}
		}
	}
	
}
