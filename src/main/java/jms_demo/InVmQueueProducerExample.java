package jms_demo;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/jms-produce")
class InVmQueueProducerExample extends HttpServlet {

    /**
     * disclaimer: to jest najprostszy, niezbyt ładny przykład otwierający połączenie i sesję przy każdym zapytaniu
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {

            InitialContext context = new InitialContext();

            QueueConnectionFactory connectionFactory = (QueueConnectionFactory) context.lookup("java:/ConnectionFactory");

            QueueConnection connection = connectionFactory.createQueueConnection();
            connection.start();

            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue queue = (Queue) context.lookup("java:/jms/queue/MyTestQueue");
            QueueSender sender = session.createSender(queue);
            TextMessage message = session.createTextMessage(req.getParameter("message"));
            sender.send(message);

        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
