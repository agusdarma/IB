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

public class SumutPipelineFactory implements BasePipelineFactory{
	private static final Logger LOGGER = LoggerFactory.getLogger(SumutPipelineFactory.class);
	
	protected ChannelHandler handler;
	
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
				StringBuilder sb = new StringBuilder(4);
				sb.append((char)buffer.readByte());
				sb.append((char)buffer.readByte());
				sb.append((char)buffer.readByte());
				sb.append((char)buffer.readByte());
				
				int msgLen = 0;
				try {
					msgLen = Integer.parseInt(sb.toString());
				} catch (NumberFormatException e) {
					LOGGER.warn("Unable to get header length: " + sb.toString());
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
		});
		pipeline.addLast("encoder", new OneToOneEncoder() {
			@Override
			protected Object encode(ChannelHandlerContext ctx, Channel channel,
					Object msg) throws Exception {
				if (!(msg instanceof String)) {
					// Ignore what this encoder can't encode.
					return msg;
				}

				String data = (String) msg;
				String msgLen = "0000" + data.length();
				msgLen = msgLen.substring(msgLen.length() - 4);
				int strLength = data.length() + msgLen.length();
				
				ChannelBuffer c = ChannelBuffers.dynamicBuffer(strLength);
				c.writeBytes(msgLen.getBytes());
				c.writeBytes(data.getBytes());
				
				return c;
			}
		});

		// and then business logic.
		// Please note we create a handler for every new channel
		// because it has stateful properties.
		pipeline.addLast("handler", handler);
		
		return pipeline;
	}
}
