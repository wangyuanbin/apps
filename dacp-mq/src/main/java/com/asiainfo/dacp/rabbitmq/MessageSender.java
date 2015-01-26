package com.asiainfo.dacp.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
	public static Logger logger = LoggerFactory
			.getLogger(MessageReceiver.class);
	@Autowired
	private ConnectionFactory connectionFactory;
	@Value("${rabbit-send-exchange}")
	private String rabbit_send_exchange;

	public boolean sendMessageToQueue(String queueName, String msgId,
			Message msgObj) {
		boolean sendFlag = false;
		try {
			String row_key = queueName.concat("_key");
			DirectExchange dexchange = new DirectExchange(rabbit_send_exchange);
			Queue queue = new Queue(queueName);
			RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
			RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
			rabbitAdmin.declareExchange(dexchange);
			rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(dexchange)
					.with(row_key));
			rabbitTemplate
					.convertAndSend(rabbit_send_exchange, row_key, msgObj);
			sendFlag = true;
		} catch (AmqpException ex) {
			logger.error("send message failed! msgId:" + msgId);
			logger.error("", ex);
		}
		return sendFlag;
	}

	public boolean sendMessageToQueue(String exchangeName, String queueName,
			String msgId, Message msgObj) {
		boolean sendFlag = false;
		try {
			String row_key = queueName.concat("_key");
			DirectExchange dexchange = new DirectExchange(exchangeName);
			Queue queue = new Queue(queueName);
			RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
			RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
			rabbitAdmin.declareExchange(dexchange);
			rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(dexchange)
					.with(row_key));
			rabbitTemplate.convertAndSend(exchangeName, row_key, msgObj);
			sendFlag = true;
		} catch (AmqpException ex) {
			logger.error("send message failed! msgId:{}", msgId);
			logger.error(ex.toString());
		}
		return sendFlag;
	}

	public boolean pushMessage(String exchangeName, String msgId, Message msgObj) {
		boolean sendFlag = false;
		try {
			RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
			RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
			FanoutExchange fanExchange = new FanoutExchange(exchangeName, true,
					false);
			rabbitAdmin.declareExchange(fanExchange);
			rabbitAdmin.declareBinding(BindingBuilder.bind(fanExchange).to(
					fanExchange));
			rabbitTemplate.convertAndSend(exchangeName, "", msgObj);
			sendFlag = true;
		} catch (AmqpException ex) {
			logger.error("send message failed! msgId:{}", msgId);
			logger.error("", ex.toString());
		}
		return sendFlag;
	}
	/**
	 * 
	 * @param queueName 队列名
	 * @param resquestMessage 消息
	 * @param replyTimeout 超时时间
	 * @return
	 */
	public String sendAndRecieve(String queueName, Object resquestMessage,
			long replyTimeout) {
		try{
			RabbitTemplate _template = new RabbitTemplate(connectionFactory);
			if (replyTimeout > 0) {
				_template.setReplyTimeout(replyTimeout);
			}
			Object replyObject = _template.convertSendAndReceive( queueName,
					resquestMessage);
			String responseMessage = replyObject == null ? null
					: (String) replyObject;
			return responseMessage;
		}catch(Exception ex){
			logger.error("",ex);
		}
		return null;
	}
}
