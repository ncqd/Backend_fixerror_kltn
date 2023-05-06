package com.iuh.backendkltn32.jms;




import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.iuh.backendkltn32.dto.DangKyDeTaiRequest;
import com.iuh.backendkltn32.dto.DangKyNhomRequest;

@Component
public class JmsPublishProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendMessageOnDeTaiChanel(final DangKyDeTaiRequest message) {

//		jmsTemplate.setDefaultDestinationName("deTai_channel");
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage =  session.createMapMessage();
				mapMessage.setString("maNhom", message.getMaNhom());
				mapMessage.setString("maDeTai", message.getMaDeTai());
				return mapMessage;		
			}
		});
	}
	
	
	public void sendMessageOnNhomChanel(final DangKyNhomRequest message) {

		jmsTemplate.setDefaultDestinationName("nhom_channel");
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage =  session.createMapMessage();
				mapMessage.setObject("dsMaSinhVien", message.getDsMaSinhVien());
				mapMessage.setString("maNhom", message.getMaNhom());
				mapMessage.setString("maDeTai", message.getMaDeTai());
				return mapMessage;		
			}
		});
	}

}
