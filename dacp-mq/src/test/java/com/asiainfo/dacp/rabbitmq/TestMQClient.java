package com.asiainfo.dacp.rabbitmq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
public class TestMQClient {
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext(
				"conf/rabbitmq-client.xml");
		final MessageSender sender = context.getBean(MessageSender.class);
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					System.out.println("recive-a:"+sender.sendAndRecieve("requsetAndResponse", "1234a", 1000*10));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					System.out.println("recive-b:"+sender.sendAndRecieve("requsetAndResponse", "1234b", 1000*10));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}

}
