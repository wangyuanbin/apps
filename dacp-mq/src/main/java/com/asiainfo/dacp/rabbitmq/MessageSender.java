package com.asiainfo.dacp.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class MessageSender {
	public static Logger logger=LoggerFactory.getLogger(MessageReceiver.class);
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private RabbitAdmin rabbitAdmin;
	@Value("${rabbit-send-exchange}")
	private String rabbit_send_exchange;
	public boolean sendMessageToQueue(String queueName,String msgId,Message msgObj){
		boolean sendFlag=false;
		try{
			String row_key=queueName.concat("_key");
			DirectExchange dexchange = new DirectExchange(rabbit_send_exchange);
			Queue queue = new Queue(queueName);
			rabbitAdmin.declareExchange(dexchange);
			rabbitAdmin.declareBinding(
			        BindingBuilder.bind(queue).to(dexchange).with(row_key));
			rabbitTemplate.convertAndSend(rabbit_send_exchange,row_key,msgObj);
			sendFlag=true;
		}catch(AmqpException ex){
			logger.error("send message failed! msgId:"+msgId);
			logger.error("",ex);
		}
		return sendFlag;
	}
	public boolean sendMessageToQueue(String exchangeName,String queueName,String msgId,Message msgObj){
		boolean sendFlag=false;
		try{
			String row_key=queueName.concat("_key");
			DirectExchange dexchange = new DirectExchange(exchangeName);
			Queue queue = new Queue(queueName);
	
			rabbitAdmin.declareExchange(dexchange);
			rabbitAdmin.declareBinding(
			        BindingBuilder.bind(queue).to(dexchange).with(row_key));
			rabbitTemplate.convertAndSend(exchangeName,row_key,msgObj);
			sendFlag=true;
		}catch(AmqpException ex){
			logger.error("send message failed! msgId:{}",msgId);
			logger.error(ex.toString());
		}
		return sendFlag;
	}
	public boolean pushMessage(String exchangeName,String msgId,Message msgObj){
		boolean sendFlag=false;
		try{
		FanoutExchange fanExchange = new FanoutExchange(exchangeName, true, false);
		rabbitAdmin.declareExchange(fanExchange);
		rabbitAdmin.declareBinding(BindingBuilder.bind(fanExchange).to(fanExchange));
		rabbitTemplate.convertAndSend(exchangeName, "", msgObj);
		sendFlag = true;
		}catch(AmqpException ex){
			logger.error("send message failed! msgId:{}",msgId);
			logger.error("",ex.toString());
		}
		return sendFlag;
	}
}
