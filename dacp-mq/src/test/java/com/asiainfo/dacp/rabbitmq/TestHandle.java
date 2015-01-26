package com.asiainfo.dacp.rabbitmq;
public class TestHandle implements ResponseHandler {
	public String handler(Object msg) {
		return "response message:"+msg.toString().toUpperCase();
	}

}
