package com.asiainfo.dacp.rabbitmq;

import org.springframework.stereotype.Service;

@Service
public class TestHandle implements ResponseHandler {

	public String handler(Object msg) {
		return "response message:"+(String)msg;
	}

}
