import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.*;

/*
 * MyQueueListener is a Message Listener that will listen to jms/myQueue.  Whenever a message
 * is available in that Queue, the onMessage method will be called with the available message.
 */

// This creates the mapping of this MessageListener to the appropriate Queue
@MessageDriven(mappedName = "jms/myQueue", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
})
public class MyQueueListener implements MessageListener {
    // Lookup the ConnectionFactory using resource injection and assign to cf
    @Resource(name = "jms/myConnectionFactory")
    private ConnectionFactory cf;
    // lookup the Queue using resource injection and assign to q
    @Resource(name = "jms/myQueue3")
    private Queue q;

    public MyQueueListener() {
    }
    /*
     * When a message is available in jms/myQueue, onMessage is called.
     */
    public void onMessage(Message message) {
        try {
            String tmt = "";
            // Since there can be different types of Messages, make sure this is the right type.
            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage) message;
                tmt = tm.getText();
                System.out.println("MyQueueListener received: " + tmt);
                Connection con = cf.createConnection();
                Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer writer = session.createProducer(q);
                TextMessage msg = session.createTextMessage();
                msg.setText(tmt + " after processing by MyQueueListener");
                writer.send(msg);
                con.close();
                System.out.println("MyQueueListener sent " + tmt + " after processing by MyQueueListener to " + q.getQueueName());
            } else {
                System.out.println("I don't handle messages of this type");
            }
        } catch (JMSException e) {
            System.out.println("JMS Exception thrown" + e);
        } catch (Throwable e) {
            System.out.println("Throwable thrown" + e);
        }
    }
}

