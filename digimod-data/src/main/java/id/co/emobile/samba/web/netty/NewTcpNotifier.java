package id.co.emobile.samba.web.netty;

import org.jboss.netty.channel.Channel;

public interface NewTcpNotifier {
	
	public void setTcpClient(NewTcpClient tcpClient);
	public void notifyDataReceived(Channel channel, String message);
}
