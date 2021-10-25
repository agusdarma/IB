package id.co.emobile.samba.web.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.handler.codec.string.StringDecoder;

public class TerminatedCharPipelineFactory implements BasePipelineFactory {
	private byte terminatedChar = 3;
	
	protected ChannelHandler handler;
	
	@Override
	public void setChannelHandler(ChannelHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = Channels.pipeline();
		
		// Add the text line codec combination first,
		pipeline.addLast("framer", getDelimiterBasedFrameDecoder());
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new OneToOneEncoder() {
			@Override
			protected Object encode(ChannelHandlerContext ctx, Channel channel,
					Object msg) throws Exception {
				if (!(msg instanceof String)) {
					// Ignore what this encoder can't encode.
					return msg;
				}

				String data = (String) msg;
				int strLength = data.length();
				ChannelBuffer c = ChannelBuffers.dynamicBuffer(strLength + 2);
				c.writeBytes(data.getBytes());
				c.writeByte(terminatedChar);
				
				return c;
			}
		});
		
		// and then business logic.
		pipeline.addLast("handler", handler);
		
		return pipeline;

	}

	protected DelimiterBasedFrameDecoder getDelimiterBasedFrameDecoder() {
		return new DelimiterBasedFrameDecoder(8192, 
			new ChannelBuffer[] {
				ChannelBuffers.wrappedBuffer(new byte[] {terminatedChar })
			});
	}
	
	public void setTerminatedChar(byte terminatedChar) {
		this.terminatedChar = terminatedChar;
	}
}
