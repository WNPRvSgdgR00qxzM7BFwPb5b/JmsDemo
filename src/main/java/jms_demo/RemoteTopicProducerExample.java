package jms_demo;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

class RemoteTopicProducerExample {
    public static void main(String[] args) {

        try {
            Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            env.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
            env.put(Context.SECURITY_PRINCIPAL, "jmsExample");
            env.put(Context.SECURITY_CREDENTIALS, "jmsExample");

            Context namingContext = new InitialContext(env);

            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("jms/RemoteConnectionFactory");
            JMSContext context = connectionFactory.createContext("jmsExample", "jmsExample");

            JMSProducer producer = context.createProducer();
            Topic destination = (Topic) namingContext.lookup("jms/queue/MyTestTopic");
            TextMessage message = context.createTextMessage("test");
            producer.send(destination, message);


            namingContext.close();
            context.close();

        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
