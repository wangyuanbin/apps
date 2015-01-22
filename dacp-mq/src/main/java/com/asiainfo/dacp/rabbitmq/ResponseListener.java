package com.asiainfo.dacp.rabbitmq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
public  class ResponseListener implements MessageListener{
	@Autowired
	private RabbitTemplate rabbitTemplate;
	private ResponseHandler responseHandler;
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	private Gson jsonTool = new Gson();
	private Logger logger = LoggerFactory.getLogger(ResponseListener.class);
	private SimpleMessageConverter converter = new SimpleMessageConverter();
	public ResponseHandler getResponseHandler() {
		return responseHandler;
	}
	public void setResponseHandler(ResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}
	public void onMessage( final Message requestMessage) {
		threadPool.execute(new Runnable() {
			public void run() {
				Object msg = converter.fromMessage(requestMessage);
				logger.info("recieve resquest message {}",jsonTool.toJson(msg));
				String res = responseHandler.handler(msg);
				String reply = requestMessage.getMessageProperties().getReplyTo();
				rabbitTemplate.convertAndSend(reply, res);
			}
		});
	}
}
