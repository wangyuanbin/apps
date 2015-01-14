package com.asiainfo.dacp.rabbitmq;

public interface ResponseHandler {
	String handler(Object msg);
}
