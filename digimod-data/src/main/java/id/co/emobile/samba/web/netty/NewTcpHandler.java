package id.co.emobile.samba.web.netty;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public class NewTcpHandler extends SimpleChannelHandler {
	private static final Logger LOG = LoggerFactory.getLogger(NewTcpHandler.class);
	
	private Channel channel;
	
	private NewTcpNotifier tcpNotifier;
	
	public void setTcpNotifier(NewTcpNotifier tcpNotifier) {
		this.tcpNotifier = tcpNotifier;
	}
	
	public void sendData(String data) {
		if (channel != null && channel.isConnected() && channel.isWritable()) {
			LOG.debug("{} Send [{}]", channel.toString(), data );
			channel.write(data);
		} else {
			LOG.warn("Channel is closed. Unable to send: [{}]", data );
		}
	}
	
	public void closeChannel() {
		if (this.channel != null) {
			LOG.info("Closing Channel from " + this.channel.getRemoteAddress());
			ChannelFuture future = this.channel.close();
			future.awaitUninterruptibly(5000);
			this.channel = null;
		}
		LOG.debug("Channel closed");
	}
	
	public boolean isConnected() {
		if (channel == null)
			return false;
		else if (channel.isConnected() && channel.isOpen())
			return true;
		else
			return false;
	}
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof ChannelStateEvent) {
			LOG.debug(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		if (this.channel != null) {
			ChannelFuture future = this.channel.close();
			future.awaitUninterruptibly(5000);
			
			LOG.info("Closing previous Channel of " + this.channel.getRemoteAddress());
			this.channel = null;
		}
		LOG.info("Connected to " + e.getChannel().getRemoteAddress());
		this.channel = e.getChannel();
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		LOG.info("Disconnected from " + e.getChannel().getRemoteAddress());
		this.channel = null;
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		String request = (String) e.getMessage();
		String remoteAddress = "N/A";
		int remotePort = 0;
		if (e.getRemoteAddress() instanceof InetSocketAddress) {
			remoteAddress = ((InetSocketAddress) e.getRemoteAddress()).getAddress().getHostAddress();
			remotePort = ((InetSocketAddress) e.getRemoteAddress()).getPort();
		}
		LOG.debug("[<- {}:{}] Rcv [{}]", remoteAddress, remotePort, request );
		if (tcpNotifier == null)
			LOG.warn("Unable to find Notifier. Dropping data: " + request);
		else
			tcpNotifier.notifyDataReceived(e.getChannel(), request);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		LOG.error("Exception from Netty: " + e.getCause().getMessage());
	}

}
