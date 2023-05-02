import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.*;

// This creates the mapping of this MessageListener to the appropriate Queue
@MessageDriven(mappedName = "jms/myQueue2", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MyQueueListener2 implements MessageListener {

    public MyQueueListener2() {
    }

    /*
     * When a message is available in jms/myQueue, then onMessage is called.
     */
    public void onMessage(Message message) {
        try {
            String tmt = "";
            // Since there can be different types of Messages, make sure this is the right type.
            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage) message;
                tmt = tm.getText();
                System.out.println("MyQueueListener2 received: " + tmt);
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