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
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
@Service("responseListener")
public  class ResponseListener implements MessageListener{
	@Autowired
	private ResponseHandler handler;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	private Gson jsonTool = new Gson();
	private Logger logger = LoggerFactory.getLogger(ResponseListener.class);
	private SimpleMessageConverter converter = new SimpleMessageConverter();
	public void onMessage( final Message requestMessage) {
		threadPool.execute(new Runnable() {
			public void run() {
				Object msg = converter.fromMessage(requestMessage);
				logger.info("recieve resquest message {}",jsonTool.toJson(msg));
				String res = handler.handler(msg);
				String reply = requestMessage.getMessageProperties().getReplyTo();
				rabbitTemplate.convertAndSend(reply, res);
			}
		});
	}
}
