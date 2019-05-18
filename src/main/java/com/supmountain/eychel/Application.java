package com.supmountain.eychel;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class Application {

	public static void main(String args[]) throws Exception {
		CamelContext context = new DefaultCamelContext();

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
		context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		ConnectionFactory connectionFactory2 = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
		JmsComponent jmsComponent = JmsComponent.jmsComponentAutoAcknowledge(connectionFactory2);
		context.addComponent("test-jms2", jmsComponent);

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// from("test-jms:queue:test.queue").to("file://test");
				from("test-jms:queue:test.queue").to("test-jms:queue:test.queue");
			}
		});

		ProducerTemplate template = context.createProducerTemplate();
		context.start();

		for (int i = 0; i < 10; i++) {
			template.sendBody("test-jms:queue:test.queue", "Test Message: " + i);
		}

		Thread.sleep(1000);

		ConsumerTemplate consumerTemplate = context.createConsumerTemplate(30);

		for (int i = 0; i < 20; i++) {
			System.out.println("XXXX" + i);
			Object object = consumerTemplate.receiveBody("test-jms2:queue:test.queue");
			System.out.println(i);
			if (object != null) {
				System.out.println(object.toString());
			} else {
				System.out.println("null");
			}
		}

		context.stop();
	}
}
