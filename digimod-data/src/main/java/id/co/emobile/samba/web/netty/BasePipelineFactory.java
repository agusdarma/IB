package id.co.emobile.samba.web.netty;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipelineFactory;

public interface BasePipelineFactory extends ChannelPipelineFactory {
	public void setChannelHandler(ChannelHandler handler);
}
