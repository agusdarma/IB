package id.co.emobile.samba.web.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewTcpClient {
	private static final Logger LOG = LoggerFactory.getLogger(NewTcpClient.class);
	
	private String host;
	protected int port;
	protected int localPort = 0;
	protected int socketTimeout = 5000;  // default 1000ms
	private BasePipelineFactory pipelineFactory;
	
	private ChannelFactory clientFactory;
	private NewTcpHandler tcpHandler;
	
	public NewTcpClient() {
		clientFactory = new NioClientSocketChannelFactory (
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());	
	}

	public void connect(String host, int port) {
		LOG.info("Opening TCP socket to {}:{}", host, port );
		if (isConnected() ) {
			disconnect();
		}
		ClientBootstrap clientBootstrap = new ClientBootstrap(clientFactory);
		clientBootstrap.setOption("tcpNoDelay" , true);
		clientBootstrap.setOption("keepAlive", true);
		clientBootstrap.setOption("connectTimeoutMillis", socketTimeout);
		clientBootstrap.setOption("remoteAddress", new InetSocketAddress(host, port));

		pipelineFactory.setChannelHandler(tcpHandler);
		clientBootstrap.setPipelineFactory(pipelineFactory);
		// wait for client to be connected
		ChannelFuture future = clientBootstrap.connect();
		future.awaitUninterruptibly(socketTimeout, TimeUnit.MILLISECONDS);
		if (future.isDone() && future.isSuccess()) {
			int x = socketTimeout / 200;
			while (!isConnected()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException ie) {}
				if (x-- <= 0) break;
			}
		}  // end if no error
	}
	
	public void connect() {
		connect(host, port);
	}

	public void disconnect() {
		tcpHandler.closeChannel();
	}
	
	public void shutdown() {
		if (tcpHandler.isConnected())
			tcpHandler.closeChannel();
		clientFactory.releaseExternalResources();
		LOG.info("Shutdown TcpClient");
	}
	
	public void sendData(String data) {
		tcpHandler.sendData(data);
	}
	
	public boolean isConnected() {
		return tcpHandler.isConnected();
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public void setPipelineFactory(BasePipelineFactory pipelineFactory) {
		this.pipelineFactory = pipelineFactory;
	}
	
	public void setTcpNotifier(NewTcpNotifier tcpNotifier) {
		tcpNotifier.setTcpClient(this);
		tcpHandler = new NewTcpHandler();
		tcpHandler.setTcpNotifier(tcpNotifier);
	}
}
