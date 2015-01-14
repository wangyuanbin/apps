package com.asiainfo.dacp.rabbitmq;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import com.google.gson.Gson;
public  class FanoutListener implements MessageListener{
	private Gson jsonTool = new Gson();
	private Logger logger = LoggerFactory.getLogger(ResponseListener.class);
	private SimpleMessageConverter converter = new SimpleMessageConverter();
	public void onMessage( final Message requestMessage) {
				Object msg = converter.fromMessage(requestMessage);
				logger.info("recieve resquest message {}",jsonTool.toJson(msg));
	}
}
