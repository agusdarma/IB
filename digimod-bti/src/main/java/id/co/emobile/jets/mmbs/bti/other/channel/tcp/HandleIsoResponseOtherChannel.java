//package id.co.emobile.jets.mmbs.bti.other.channel.tcp;
//
//import java.io.IOException;
//
//import org.apache.commons.lang3.StringUtils;
//import org.jboss.netty.channel.Channel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.jets.mmbs.bti.other.channel.iso.BaseIsoLogic;
//import id.co.emobile.jets.mmbs.bti.other.channel.iso.IsoLogicFactory;
//
//public class HandleIsoResponseOtherChannel extends BaseIsoTcp {
//	private final static Logger LOG = LoggerFactory.getLogger(HandleIsoResponseOtherChannel.class);
//	
//	private IsoLogicFactory logicFactory;
//	
//	public void sendResponse(TransactionTO task){
//		logDebug("Sending response: " + task);
//		// create IsoMsg
//		String rawIso = task.getTerm(TermConstant.ISO_REQUEST);
//		if (StringUtils.isEmpty(rawIso)) {
//			logWarn("Unable to read request ISO from: " + task);
//			return;
//		}
//		IsoMsg isoReq = HostIsoMsgFactory.createHostIsoMsg().parse(rawIso);
//		String accType = isoReq.getItem(IsoMsg.ADDITIONAL_DATA_4);
//		
//		BaseIsoLogic isoLogic = logicFactory.getLogic(accType);
//		if (isoLogic == null) {
//			logWarn("Unknown processing code ID: " + accType);
//			return;
//		}		
//		Channel channel = task.getMapChannelTcp().remove(task.getMsgLogNo());
//		if (channel == null) {
//			logWarn("Unable to find Channel for MsgLogNo: " + task.getMsgLogNo());
//			return;
//		}
//		
//		try {
//			IsoMsg response = isoLogic.composeIsoResponse(isoReq, task);		
//			logDebug("ISO Created: " + response.printIso());
//			
//			sendData(channel, response.toString());		
//		} catch (IOException ioe) {
//			logError("I/O Exception when sending data: " + ioe.getMessage());
//			return;
//		} catch (Exception e) {
//			logError("Unknown Exception: " + e.getMessage(), e);
//			return;
//		}
//	}
//	
//	
//	private void sendData(Channel channel, String response) {
//		if (channel != null && channel.isConnected() && channel.isWritable()) {
//			LOG.debug("{} Send Response [{}]", channel.toString(), response );
//			channel.write(response);
//		} else {
//			LOG.warn("Channel is closed. Unable to send: [{}]", response );			
//		}
//		
//	}
//
//
//	public void setLogicFactory(IsoLogicFactory logicFactory) {
//		this.logicFactory = logicFactory;
//	}	
//}
