package id.co.emobile.samba.web.netty;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.utils.CommonUtil;

public class MandiriDspPipelineFactory implements BasePipelineFactory {
	private static final Logger LOG = LoggerFactory.getLogger(MandiriDspPipelineFactory.class);
	
	protected ChannelHandler handler;
	
	private String footer3Str;
	
	private byte[] hostHeader;
	
	public void initData() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			String ipAddress = ip.getHostAddress(); //"10.204.9.37";
			String socketHeader = "*MOBI" + ipAddress;
			byte[] footer1 = padByteArray(21 - ipAddress.length());
			byte[] footer2 = new byte[]{(byte) 32};
			byte[] footer3 = new byte[]{(byte) 89, (byte) 48};
			
			String footer1Str = new String(footer1);
			String footer2Str = new String(footer2);
			footer3Str = new String(footer3);
			
			hostHeader = combineByteArray(convertToEbcdic(socketHeader + footer1Str), 
					footer2);
			
		} catch (UnknownHostException e) {
			System.out.println("Unknown network!");
		}
	}
	
	@Override
	public void setChannelHandler(ChannelHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = Channels.pipeline();
		
		// Add the codec first,

		pipeline.addLast("decoder", new FrameDecoder() {
			@Override
			protected Object decode(ChannelHandlerContext ctx, Channel channel,
					ChannelBuffer buffer) throws Exception {
				// Wait until the length prefix is available.
				if (buffer.readableBytes() < 4) {
					return null;
				}
				// set marker reader -> so we can reset if data is not sufficient
				buffer.markReaderIndex();
				byte[] length = new byte[4];
				buffer.readBytes(length);
				int msgLen = CommonUtil.byteArrayToInt(length);
				
				if (buffer.readableBytes() < msgLen) {
					buffer.resetReaderIndex();
					return null;
				}
				byte[] d = new byte[msgLen];
				buffer.readBytes(d);
				LOG.debug("Receive data in byte[]: " + Arrays.toString(d));
				String resp = convertToAscii(d);
				LOG.debug("Raw Receiving (ASCII): [{}]", resp);
				resp = resp.substring(213 + 29); //Host header + Socket header length = 213 + 29
				LOG.debug("Receiving: [{}]", resp);
				
				return resp;
			}
		});
		pipeline.addLast("encoder", new OneToOneEncoder() {
			@Override
			protected Object encode(ChannelHandlerContext ctx, Channel channel,
					Object msg) throws Exception {
				if (!(msg instanceof String)) {
					// Ignore what this encoder can't encode.
					return msg;
				}
				
				byte[] data = new byte[] {};
				LOG.debug("Sending (ASCII): [{}]", msg);
				data = combineByteArray(hostHeader, convertToEbcdic(footer3Str + msg));
				LOG.debug("Send data in byte[]: " + Arrays.toString(data));
				LOG.debug("Sending: [{}]", new String(data));
				
				int len = data.length;
				byte[] d = CommonUtil.intToByteArray(len);
				int strLength = 4 + data.length;
				
				ChannelBuffer c = ChannelBuffers.dynamicBuffer(strLength);
				c.writeBytes(d);
				c.writeBytes(data);
				
				return c;
			}
		});

		// and then business logic.
		// Please note we create a handler for every new channel
		// because it has stateful properties.
		pipeline.addLast("handler", handler);
		
		return pipeline;
	}
	
	private byte[] padByteArray(int len) {
		byte[] b = new byte[len];
		for (int i=0; i<len; i++) {
			b[i] = (byte) 00;
		}
		return b;
	}
	
	private String convertToAscii(byte[] msg) {
		try {
			return new String(msg, "cp037");
			//return new String(msg, "cp1047");
		} catch (Exception e) {
			LOG.warn("Failed convert EBCDIC to ASCII: " + e);
			return new String(msg);
		}
	}

	private byte[] convertToEbcdic(String msg) {
		try {
			return msg.getBytes("cp037");
			//return msg.getBytes("cp1047");
		} catch (Exception e) {
			LOG.warn("Failed convert ASCII to EBCDIC: " + e);
			return new byte[] {};
		}
	}
	
	private byte[] combineByteArray(byte[] one, byte[] two) {
		byte[] combined = new byte[one.length + two.length];

		System.arraycopy(one,0,combined,0         ,one.length);
		System.arraycopy(two,0,combined,one.length,two.length);
		
		return combined;
	}
	
}
