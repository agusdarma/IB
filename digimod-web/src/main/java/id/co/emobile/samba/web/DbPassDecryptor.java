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

import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.utils.CipherUtils;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 */
public class DbPassDecryptor extends BasicDataSource {
	
	@Override
	public void setUsername(String username) {
		if (StringUtils.isNotEmpty(username) && username.startsWith("ENC(") && username.endsWith(")") ){
			String encUsername = username.substring(4, username.length() - 1);
			String plainUsername = CipherUtils.decryptDESede(encUsername, WebConstants.JDBC_KEY);
			super.setUsername(plainUsername);
		} else {
			super.setUsername(username);
		}
	}
	
	@Override
	public void setPassword(String password) {
		if (StringUtils.isNotEmpty(password) && password.startsWith("ENC(") && password.endsWith(")") ){
			String encPassword = password.substring(4, password.length() - 1);
			String plainPassword = CipherUtils.decryptDESede(encPassword, WebConstants.JDBC_KEY);
			super.setPassword(plainPassword);
		} else {
			super.setPassword(password);
		}
	}
}

