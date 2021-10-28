package id.co.emobile.samba.web.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.data.ResponseData;
import id.co.emobile.samba.web.service.BizMessageService;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.utils.CommonUtil;

public class HttpTransmitterAgent {
	private final static Logger LOG = LoggerFactory.getLogger(HttpTransmitterAgent.class);
	
	private final BizMessageService messageService;
	
	private PoolingHttpClientConnectionManager cm;
	private HttpClient httpClient;
	private AuthCache authCache;
	private String id;
	private int timeout = 30000;  // wait for 30s
	private int maxConnection = 100;  // default 100
	
	private String serverUrl;
	private String userName;
	private String password;

	private boolean allowAllSsl = true;
	private String keyStoreName;
	private String keyStorePass;
	
	@Autowired
	public HttpTransmitterAgent(BizMessageService messageService) {
		this.messageService = messageService;
	}
	
	public void start() throws Exception {
		if (allowAllSsl) {
			LOG.info("Starting with allow all SSL Certificate");
			// setup a Trust Strategy that allows all certificates.
			SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
			sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
				
				@Override
				public boolean isTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					return true;
				}
				
			});
		    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		    		sslContextBuilder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		    
			Registry<ConnectionSocketFactory> registryConnection = RegistryBuilder.<ConnectionSocketFactory>create()
	                .register("http", PlainConnectionSocketFactory.getSocketFactory())
	                .register("https", sslsf)
	                .build();
			
			cm = new PoolingHttpClientConnectionManager(registryConnection);
		} else if (StringUtils.isNotEmpty(keyStoreName)) {
			// Trust own CA and all self-signed certs
			LOG.info("Loading keyStore from {} with pass {}", keyStoreName, keyStorePass);
			
			ClassLoader cl = this.getClass().getClassLoader();
		    URL url = cl.getResource(keyStoreName);  //"test.keystore");
		    KeyStore keystore  = KeyStore.getInstance("jks");
		    LOG.debug("URL: {}, KeyStore instance jks: {}", url, keystore);
		    keystore.load(url.openStream(), keyStorePass.toCharArray()); 
		    KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
		            KeyManagerFactory.getDefaultAlgorithm());
		    //LOG.debug("Factory: {}", kmfactory);		    
		    kmfactory.init(keystore, keyStorePass.toCharArray()); 
		    KeyManager[] keymanagers = kmfactory.getKeyManagers(); 
		    
		    TrustManagerFactory tmf = TrustManagerFactory.getInstance(
		            TrustManagerFactory.getDefaultAlgorithm());
		    tmf.init(keystore);
		    TrustManager[] tm = tmf.getTrustManagers();
		    
		    SSLContext sslContext = SSLContext.getInstance("TLS");
		    sslContext.init(keymanagers, tm, null);
		    
		    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		    		sslContext, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		    Registry<ConnectionSocketFactory> registryConnection = RegistryBuilder.<ConnectionSocketFactory>create()
	                .register("http", PlainConnectionSocketFactory.getSocketFactory())
	                .register("https", sslsf)
	                .build();
			
			cm = new PoolingHttpClientConnectionManager(registryConnection);
		} else {
			LOG.info("Default implementation of HTTP Connection");
			cm = new PoolingHttpClientConnectionManager();
		}
		cm.setDefaultMaxPerRoute(maxConnection);
		cm.setMaxTotal(maxConnection);

		// Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
            .setTcpNoDelay(true)
            .setSoTimeout(timeout)
            .build();
        cm.setDefaultSocketConfig(socketConfig);
        
        HttpClientBuilder httpClientBuilder = HttpClients.custom().
        		setConnectionManager(cm);
        
		if (StringUtils.isNotEmpty(serverUrl) && StringUtils.isNotEmpty(userName)) {
			URIBuilder builder = new URIBuilder(serverUrl);
			HttpHost targetHost = new HttpHost(builder.getHost(),builder.getPort());
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
	        credsProvider.setCredentials(
	                new AuthScope(builder.getHost(), builder.getPort()),
	                new UsernamePasswordCredentials(userName, password));
	        httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
	        
			// Create AuthCache instance
	        authCache = new BasicAuthCache();
	        // Generate BASIC scheme object and add it to the local
	        // auth cache
	        BasicScheme basicAuth = new BasicScheme();
	        authCache.put(targetHost, basicAuth);
	        LOG.debug("Using basic authentication with userName: " + userName);
		}

		httpClient = httpClientBuilder.build(); 
		
		LOG.info("[{}] HttpTransmitterAgent is started with timeout {}ms and {} connection", id, timeout, maxConnection );
	}
	
	public void stop() {
		//httpClient.getConnectionManager().shutdown();
		cm.shutdown();
		LOG.info("[{}] HttpTransmitterAgent has been stopped", id );
	}
	
	public ResponseData sendPostMessage(String destUrl, List<NameValuePair> params) {
		ResponseData respData = new ResponseData();
		respData.setResultCode(SambaWebException.NE_UNKNOWN_ERROR);
		
		HttpPost httpPost = null;
		String realUrl = destUrl;
		try {
        	if (StringUtils.isEmpty(realUrl))
        		realUrl = serverUrl;
			httpPost = new HttpPost(realUrl);
			if (LOG.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				for (NameValuePair nvp: params)
					sb.append(nvp.getName()).append("=").append(nvp.getValue()).append(",");
				LOG.info("[{}] Execute POST to {}, Params: {}", id, realUrl, sb.toString());
			}
			httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
		} catch (Exception e) {
			if (httpPost != null)
				httpPost.abort();
            LOG.warn("[" + id + "] Unknown Error", e);
            respData.setResultCode(SambaWebException.NE_UNKNOWN_ERROR);
            respData.setMsgToUser(messageService.getErrorMessageFor(respData.getResultCode()));
            respData.setSysMessage(e.getMessage());
		}
		if (httpPost != null)
			_internalHttpExecute(httpPost, respData);
		return respData;
	}
	
	public ResponseData sendPostMultipleHeaderMessage(String destUrl, List<NameValuePair> headers, String content,
			String userName, String password) {
		ResponseData respData = new ResponseData();
		respData.setResultCode(SambaWebException.NE_UNKNOWN_ERROR);
		
		HttpPost httpPost = null;
		String realUrl = destUrl;
		try {
        	if (StringUtils.isEmpty(realUrl))
        		realUrl = serverUrl;
			httpPost = new HttpPost(realUrl);
			String maskedContent = CommonUtil.maskNumberForMessage(content);
			if (headers == null) {
				LOG.info("[{}] Execute POST to {}, Content: {}", id, realUrl, maskedContent);				
			} else {
				StringBuilder sb = new StringBuilder();
				for (NameValuePair nvp: headers) {
					httpPost.setHeader(nvp.getName(), nvp.getValue());
					sb.append(nvp.getName()).append("=").append(nvp.getValue()).append(",");
				}
				LOG.info("[{}] Execute POST to {}, Headers: {}, Content: {}", id, realUrl, sb.toString(), maskedContent);
			}
			if (StringUtils.isNotEmpty(userName)) {
				String auth = userName + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
				String authHeader = "Basic " + new String(encodedAuth);
				
				httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
			}
			httpPost.setEntity(new StringEntity(content, Consts.UTF_8));
			
		} catch (Exception e) {
			if (httpPost != null)
				httpPost.abort();
            LOG.warn("[" + id + "] Unknown Error", e);
            respData.setResultCode(SambaWebException.NE_UNKNOWN_ERROR);
            respData.setMsgToUser(messageService.getErrorMessageFor(respData.getResultCode()));
            respData.setSysMessage(e.getMessage());
		}
		if (httpPost != null)
			_internalHttpExecute(httpPost, respData);
		return respData;
	}
	
	public ResponseData sendPostMessageWithEntity(String destUrl, String content) {
		return sendPostMultipleHeaderMessage(destUrl, null, content, null, null);
	}
	
	public ResponseData sendGetMessage(String destUrl, HashMap<String, String> params) {
		List<NameValuePair> listData = new ArrayList<NameValuePair>();
		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = params.get(key);
			listData.add(new BasicNameValuePair(key, value));
		}
		return sendGetMessage(destUrl, listData);
	}
	
	public ResponseData sendGetMessage(String destUrl, List<NameValuePair> listData) {
		ResponseData respData = new ResponseData();
		respData.setResultCode(SambaWebException.NE_UNKNOWN_ERROR);
		HttpGet httpGet = _internalCreateHttpGet(destUrl, listData, null, respData);
		if (httpGet == null)
			return respData;
		_internalHttpExecute(httpGet, respData);
		return respData;
	}
	
	public ResponseData sendGetMessageWithHeaders(String destUrl, List<NameValuePair> listData,
			List<NameValuePair> listHeader) {
		ResponseData respData = new ResponseData();
		respData.setResultCode(SambaWebException.NE_UNKNOWN_ERROR);
		HttpGet httpGet = _internalCreateHttpGet(destUrl, listData, listHeader, respData);
		if (httpGet == null)
			return respData;
		_internalHttpExecute(httpGet, respData);
		return respData;
	}
	
	private HttpGet _internalCreateHttpGet(String destUrl, List<NameValuePair> listData,
			List<NameValuePair> listHeader, ResponseData respData) {
		HttpGet httpGet = null;
		String realUrl = destUrl;
		try {
        	if (StringUtils.isEmpty(realUrl))
        		realUrl = serverUrl;
        	URIBuilder builder = new URIBuilder(realUrl);
        	StringBuilder sb = new StringBuilder();
			for (NameValuePair data: listData) {
				builder.addParameter(data.getName(), data.getValue());
				sb.append(data.getName()).append("=").append(data.getValue()).append(",");
			}			
			httpGet = new HttpGet(builder.build());
			if (listHeader == null) {
//				LOG.info("[{}] Execute GET to {}, Params: {}", id, realUrl, sb.toString());	
			} else {
				StringBuilder sbHeader = new StringBuilder();
				for (NameValuePair header: listHeader) {
					httpGet.setHeader(header.getName(), header.getValue());
					if (HttpHeaders.AUTHORIZATION.equals(header.getName()))
						sbHeader.append(header.getName()).append("=").append("***").append(",");	
					else
						sbHeader.append(header.getName()).append("=").append(header.getValue()).append(",");	
				}
//				LOG.info("[{}] Execute GET to {}, Params: {}, Headers: {}", id, realUrl, sb.toString(), sbHeader.toString());
			}
		} catch (URISyntaxException us) {
			LOG.warn("[{}] URL [{}] is not valid", id, realUrl);
			respData.setResultCode(SambaWebException.NE_INVALID_DESTINATION);
			respData.setMsgToUser(messageService.getErrorMessageFor(respData.getResultCode()));
            respData.setSysMessage(us.getMessage());
		} catch (Exception e) {
			LOG.warn("[" + id + "] Unknown Error", e);
            respData.setResultCode(SambaWebException.NE_UNKNOWN_ERROR);
            respData.setMsgToUser(messageService.getErrorMessageFor(respData.getResultCode()));
            respData.setSysMessage(e.getMessage());
		}
		return httpGet;
	}
	
	private void _internalHttpExecute(HttpRequestBase httpRequestBase, ResponseData respData) {
		if (httpRequestBase == null) return;
		try {
			// Add AuthCache to the execution context if available
			HttpResponse response;
			if (authCache != null) {
	            HttpClientContext localContext = HttpClientContext.create();
	            localContext.setAuthCache(authCache);
	            response = httpClient.execute(httpRequestBase, localContext);
			} else {
				response = httpClient.execute(httpRequestBase);
			}
            if (response == null) {
            	LOG.warn("[{}] No Response from HOST", id);
            	respData.setResultCode(SambaWebException.NE_NO_RESPONSE_FROM_HOST);
            	respData.setMsgToUser(messageService.getErrorMessageFor(respData.getResultCode()));
                respData.setSysMessage("No Response from HOST");
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ||
            		response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
				String respString = EntityUtils.toString(response.getEntity());
//                LOG.info("[{}] Response: {}", id, respString);
                
                respData.setResultCode(0);
                respData.setMsgToUser(respString);
                respData.setSysMessage("");
			} else {
				LOG.warn("[{}] Invalid statusCode: {}", 
						id, response.getStatusLine().getStatusCode() );
				String respString = EntityUtils.toString(response.getEntity());
                
				respData.setResultCode(SambaWebException.NE_ERROR_RESPONSE_HOST);
				respData.setMsgToUser(messageService.getErrorMessageFor(respData.getResultCode()));
                respData.setSysMessage("Invalid StatusCode: " + 
                		response.getStatusLine().getStatusCode() + 
                		", Resp: " + respString);
			}  // end if statusCode != 200
		} catch (IOException ioe) {
			LOG.warn("[{}] Connection error to {}, exception: {}", id, httpRequestBase, ioe.getMessage());
			respData.setResultCode(SambaWebException.NE_CONNECTION_ERROR);
			respData.setMsgToUser(messageService.getErrorMessageFor(respData.getResultCode()));
            respData.setSysMessage(ioe.getMessage());
		} catch (Exception e) {
			if (httpRequestBase != null)
				httpRequestBase.abort();
            LOG.warn("[" + id + "] Unknown Error", e);
            respData.setResultCode(SambaWebException.NE_UNKNOWN_ERROR);
            respData.setMsgToUser(messageService.getErrorMessageFor(respData.getResultCode()));
            respData.setSysMessage(e.getMessage());
        } finally {
        	if (httpRequestBase != null)
        		httpRequestBase.releaseConnection();
        } // end try catch
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAllowAllSsl(boolean allowAllSsl) {
		this.allowAllSsl = allowAllSsl;
	}

	public void setKeyStoreName(String keyStoreName) {
		this.keyStoreName = keyStoreName;
	}

	public void setKeyStorePass(String keyStorePass) {
		this.keyStorePass = keyStorePass;
	}

}
