/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.co.emobile.samba.web;

import id.co.emobile.samba.web.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * 
 */
public class DateConverter extends StrutsTypeConverter {
	
	SimpleDateFormat sdfFull = new SimpleDateFormat(Constants.NE_SDF_FULL);
	SimpleDateFormat sdfFullSlash = new SimpleDateFormat(Constants.NE_SDF_FULL_SLASH);
	SimpleDateFormat sdfFullNos = new SimpleDateFormat(Constants.NE_SDF_FULL_NOS);
	SimpleDateFormat sdfFullSlashNos = new SimpleDateFormat(Constants.NE_SDF_FULL_SLASH_NOS);
	
	SimpleDateFormat sdfDate = new SimpleDateFormat(Constants.NE_SDF_DATE);
	SimpleDateFormat sdfDateWithSlash = new SimpleDateFormat(Constants.NE_SDF_DATE_SLASH);
	SimpleDateFormat sdfDojo = new SimpleDateFormat(Constants.NE_SDF_DOJO);
	SimpleDateFormat sdfCustom = new SimpleDateFormat(Constants.NE_SDF_CUSTOM);

	// 2011-07-05T23:59:59
    public Object convertFromString(@SuppressWarnings("rawtypes") Map context, 
    		String[] values, @SuppressWarnings("rawtypes") Class toClass) {
        if (values != null && values.length > 0 && values[0] != null && values[0].length() > 0) {
//          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            try {
//                return sdf.parse(values[0]);
//            }
//            catch(ParseException e) {
//                throw new TypeConversionException(e);
//            }
        	
        	try {
            	if(values[0].contains("T"))
            	{
            		return sdfDojo.parse(values[0]);
            	}
            	else if (values[0].length() >= 8 && values[0].length() <= 10) {
            		if (values[0].indexOf("/") > 0)
            			return sdfDateWithSlash.parse(values[0]);
            		else
            			return sdfDate.parse(values[0]);
            	} else if (values[0].length() >= 14 && values[0].length() <= 16) {
            		if (values[0].indexOf("/") > 0)
            			return sdfFullSlashNos.parse(values[0]);
            		else
            			return sdfFullNos.parse(values[0]);
            	}else{     
            		if (values[0].indexOf("/") > 0) {            			
            			if (values[0].length() == 6) {            				            			
            				return sdfCustom.parse(values[0]);
                		}else {
                			return sdfFullSlash.parse(values[0]);	
                		}            			            		        
            		}else {
            			return sdfFull.parse(values[0]);
            		}
            			
            			
            	}
            		
            }
            catch(ParseException e) {
                throw new TypeConversionException(e);
            }
        }
        Object o = super.performFallbackConversion(context, values, toClass);
        return o; //null;
    }
    
    public String convertToString(@SuppressWarnings("rawtypes") Map context, Object o) {
        if (o instanceof Date) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdfFull.format((Date)o);
        }
        return "";
    }
}

