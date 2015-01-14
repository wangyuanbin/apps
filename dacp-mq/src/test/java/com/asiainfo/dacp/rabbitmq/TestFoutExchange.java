package com.asiainfo.dacp.rabbitmq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
public class TestFoutExchange {
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext(
				"conf/rabbitmq-client.xml");
		final MessageSender sender = context.getBean(MessageSender.class);
		Message msgObj = new Message();
		msgObj.setMsgId("123242");
		sender.pushMessage("dacp-inter-exchange", "", msgObj);
	}

}
