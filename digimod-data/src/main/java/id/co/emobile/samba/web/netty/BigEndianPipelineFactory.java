package id.co.emobile.samba.web.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;


public class BigEndianPipelineFactory implements BasePipelineFactory {
	protected ChannelHandler handler;
	
	@Override
	public void setChannelHandler(ChannelHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = Channels.pipeline();
		
		//BIG ENDIAN
		// Add the codec first,
		pipeline.addLast("decoder", new BigEndianFrameDecoder());
		pipeline.addLast("encoder", new BigEndianOneToOneEncoder());

		// and then business logic.
		// Please note we create a handler for every new channel
		// because it has stateful properties.
		pipeline.addLast("handler", handler);
		
		return pipeline;
	}
	
	
}

@ChannelHandler.Sharable
class BigEndianFrameDecoder extends FrameDecoder {
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		// Wait until the length prefix is available.
		if (buffer.readableBytes() < 2) {
			return null;
		}
		// set marker reader -> so we can reset if data is not sufficient
		buffer.markReaderIndex();
		
		byte len1 = buffer.readByte();
		byte len2 = buffer.readByte();
//		int msgLen = (len1 & 0xFF) + (len2 & 0xFF) * 256;
		int msgLen = (len2 & 0xFF) + (len1 & 0xFF) * 256;
		
		if (buffer.readableBytes() < msgLen) {
			buffer.resetReaderIndex();
			return null;
		}
		byte[] d = new byte[msgLen];
		buffer.readBytes(d);
		  
		return new String(d);
	}
}

@ChannelHandler.Sharable
class BigEndianOneToOneEncoder extends OneToOneEncoder {
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof String)) {
			// Ignore what this encoder can't encode.
			return msg;
		}

		String data = (String) msg;
		int strLength = data.length();
		byte[] buf = new byte[2];
		buf[0] = (byte) (strLength / 256);
		buf[1] = (byte) (strLength % 256);
		
		ChannelBuffer c = ChannelBuffers.dynamicBuffer(strLength + 2);
		c.writeBytes(buf);
		c.writeBytes(data.getBytes());
		
		return c;
	}
}