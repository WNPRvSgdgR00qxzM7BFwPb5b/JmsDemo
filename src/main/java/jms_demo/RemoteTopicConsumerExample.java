package jms_demo;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

class RemoteTopicConsumerExample {
    public static void main(String[] args) {

        try {
            Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            env.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
            env.put(Context.SECURITY_PRINCIPAL, "jmsExample");
            env.put(Context.SECURITY_CREDENTIALS, "jmsExample");

            Context namingContext = new InitialContext(env);

            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("jms/RemoteConnectionFactory");

            Topic destination = (Topic) namingContext.lookup("jms/queue/MyTestTopic");

            JMSContext context = connectionFactory.createContext("jmsExample", "jmsExample");

            JMSConsumer consumer = context.createConsumer(destination);

            consumer.setMessageListener(System.out::println);
            TimeUnit.DAYS.sleep(1);

            namingContext.close();
            context.close();

        } catch (NamingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
