package com.asiainfo.dacp.rabbitmq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestMQServer {
	public static void main(String[] args) {
		System.out.println("start --server");
		ApplicationContext context = new FileSystemXmlApplicationContext(
				"conf/rabbitmq-server.xml");
		
	}

}
