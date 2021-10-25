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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DanamonPipelineFactory implements BasePipelineFactory {
	private static final Logger LOG = LoggerFactory.getLogger(DanamonPipelineFactory.class);
	
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
		pipeline.addLast("decoder", new DanamonFrameDecoder());
		pipeline.addLast("encoder", new DanamonOneToOneEncoder());

		// and then business logic.
		// Please note we create a handler for every new channel
		// because it has stateful properties.
		pipeline.addLast("handler", handler);

		return pipeline;
	}


}

@ChannelHandler.Sharable
class DanamonFrameDecoder extends FrameDecoder {
	private static final Logger LOG = LoggerFactory.getLogger(DanamonFrameDecoder.class);
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		// Wait until the length prefix is available.
		if (buffer.readableBytes() < 4) {
			return null;
		}

		StringBuilder sb = new StringBuilder(4);
		sb.append((char)buffer.readByte());
		sb.append((char)buffer.readByte());
		sb.append((char)buffer.readByte());
		sb.append((char)buffer.readByte());

		int msgLen = 0;
		try {
			msgLen = (int) Long.parseLong(sb.toString(),16);
			LOG.debug("Decoded Message length: " + msgLen);

			//			System.out.println(msgLen);
		} catch (Exception e) {
			LOG.error("Unable to get header length: " + msgLen);
			buffer.resetReaderIndex();
			return null;
		}

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
class DanamonOneToOneEncoder extends OneToOneEncoder {
	private static final Logger LOG = LoggerFactory.getLogger(DanamonOneToOneEncoder.class);
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof String)) {
			// Ignore what this encoder can't encode.
			return msg;
		}

		String data = (String) msg;
		String msgLen = "0000" + Integer.toHexString(data.length() + 1).toUpperCase();
		msgLen = msgLen.substring(msgLen.length() - 4);
		LOG.debug("Encoded message length: " + msgLen);

		ChannelBuffer c = ChannelBuffers.dynamicBuffer(data.length() + 5);
		c.writeBytes(msgLen.getBytes());
		c.writeBytes(data.getBytes());
		c.writeInt(3); // Termination character

		return c;
	}
}