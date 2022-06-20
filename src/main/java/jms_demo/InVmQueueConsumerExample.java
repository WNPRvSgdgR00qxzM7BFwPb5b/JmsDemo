package jms_demo;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("jms-consume")
class InVmQueueConsumerExample extends HttpServlet {
    private MessageConsumer consumer;

    @Override
    public void init() {
        try {
            InitialContext context = new InitialContext();
            QueueConnectionFactory connectionFactory = (QueueConnectionFactory) context.lookup("java:/ConnectionFactory");
            QueueConnection connection = connectionFactory.createQueueConnection();
            connection.start();
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) context.lookup("java:/jms/queue/MyTestQueue");
            consumer = session.createConsumer(queue);
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try (PrintWriter responseWriter = resp.getWriter()) {

            Message message = consumer.receiveNoWait();
            if (message instanceof TextMessage textMessage) {
                responseWriter.write(textMessage.getText());
            } else {
                responseWriter.write("Unexpected result, message: " + message);
            }

        } catch (JMSException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
