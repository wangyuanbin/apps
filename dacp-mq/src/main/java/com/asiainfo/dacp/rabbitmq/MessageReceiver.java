package com.asiainfo.dacp.rabbitmq;

public interface MessageReceiver{
	public void onMessage(final Message msgObj);
}
