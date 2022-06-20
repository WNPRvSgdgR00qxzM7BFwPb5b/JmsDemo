package jms_demo;


import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;


public class RemoteQueueConsumerExample {

    public static void main(String[] args) {
        // żeby to zadziałało, musimy dodać na serwerze użytkownika w grupie guest, którego username i hasło będą brzmiały "jmsExample"
        try {
            Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            env.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
            env.put(Context.SECURITY_PRINCIPAL, "jmsExample");
            env.put(Context.SECURITY_CREDENTIALS, "jmsExample");

            Context namingContext = new InitialContext(env);

            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("jms/RemoteConnectionFactory");

            Queue destination = (Queue) namingContext.lookup("jms/queue/MyTestQueue");

            JMSContext context = connectionFactory.createContext("jmsExample", "jmsExample");

            JMSConsumer consumer = context.createConsumer(destination);

            Message message = consumer.receiveNoWait();
            if (message instanceof TextMessage textMessage) {
                System.out.println("Got message: " + textMessage.getText());
            } else {
                System.out.println("Unexpected result, message: " + message);
            }

            namingContext.close();
            context.close();

        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
