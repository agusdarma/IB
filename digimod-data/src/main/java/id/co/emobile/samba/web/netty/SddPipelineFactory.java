package id.co.emobile.samba.web.netty;


import java.math.BigInteger;
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

public class SddPipelineFactory implements BasePipelineFactory{
	private static final Logger LOGGER = LoggerFactory.getLogger(SddPipelineFactory.class);
	
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
				if (buffer.readableBytes() < 12) {
					return null;
				}
				buffer.markReaderIndex();
				int lengthAll = buffer.array().length;
				byte allMsg[] = new byte[lengthAll];
				buffer.readBytes(allMsg);
				byte byHeader[] = new byte[12];
				System.arraycopy(allMsg, 0, byHeader, 0, 12);
		    	byte abyte0[] = new byte[2];
		    	System.arraycopy(byHeader, 10, abyte0, 0, 2);		    
				int msgLen = (abyte0[1] & 0xff) << 8 | abyte0[0] & 0xff;		    
		    	byte result[] = new byte[msgLen];
		    	System.arraycopy(allMsg, 12, result, 0, msgLen);
				return new String(result);
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
				byte abyte0[] = (new BigInteger(Integer.toString(data.length()))).toByteArray();
		        byte abyte1[] = {
		            abyte0.length != 1 ? abyte0[0] : 0, abyte0[abyte0.length != 1 ? 1 : 0]
		        };
		        byte abyte2[] = new byte[2];
		    	System.arraycopy(abyte1, 1, abyte2, 0, 1);
		    	System.arraycopy(abyte1, 0, abyte2, 1, 1);
		    	
		        byte[] byHead = new byte[] {0x49, 0x53, 0x4F, 0x4D, 0x50, 0x4E, 0x47, 0x45, 0x4E, 0x32};	//"ISOMPNGEN2"
		    	
		    	byte[] result = new byte[data.length()+12];
		    	System.arraycopy(byHead, 0, result, 0, 10);
		    	System.arraycopy(abyte2, 0, result, 10, 2);
		    	System.arraycopy(data.getBytes(), 0, result, 12, data.length());
				
				ChannelBuffer c = ChannelBuffers.dynamicBuffer(data.length()+12);
				c.writeBytes(result);
				
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
