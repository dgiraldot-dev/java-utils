package co.com.java.utils.jms;

import java.util.Hashtable;
import javax.jms.*;
import javax.naming.*;

/**
 * Clase para el envío de mensajes a JMS
 * @author IRamirezS
 * @version 1.0
 * @since 2019-06-20
 */
public class Sender {

    private String msgID;

    /**
     * Crea una conexión Point-to-Point y envia un mensaje al servidor.
     * @param initialContextFactory (<code>String</code>) Contexto inicial de factoría
     * @param providerURL (<code>String</code>) URL del proveedor
     * @param queueConnectionFactory (<code>String</code>) Cola de conexión de factoría
     * @param queueRequest (<code>String</code>) Cola de petición
     * @param messageRequest (<code>String</code>) Mensaje de petición
     * @throws NamingException
     * @throws JMSException
     */
    public Sender ( String initialContextFactory, String providerURL, String queueConnectionFactory, String queueRequest, String messageRequest ) throws NamingException, JMSException {

        // Definición de las variables de ambiente para conexión
        Hashtable < String, String > env = new Hashtable < String, String >();
        env.put( Context.INITIAL_CONTEXT_FACTORY, initialContextFactory );
        env.put( Context.PROVIDER_URL, providerURL );

        // Contexto inicial
        InitialContext ctx = new InitialContext( env );

        // Cola de request
        Queue queue = (Queue) ctx.lookup( queueRequest );

        // Cola de conexión 
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup( queueConnectionFactory );

        // Crear conexión
        QueueConnection queueConn = connFactory.createQueueConnection();

        // Crear una sesión con la cola de petición
        QueueSession queueSession = queueConn.createQueueSession( false, Session.DUPS_OK_ACKNOWLEDGE );

        // Crear una cola de petición
        QueueSender queueSender = queueSession.createSender( queue );
        queueSender.setDeliveryMode( DeliveryMode.NON_PERSISTENT );

        // Crear el mensaje a enviar
        TextMessage message = queueSession.createTextMessage( messageRequest );

        // Enviar el mensaje
        queueSender.send( message );

        // Obtener el número de mensaje
        msgID = message.getJMSMessageID().substring( 4, message.getJMSMessageID().length() - 1 );

        // Cerrar conexión
        queueConn.close();
    }

    /**
     * Obtener el identificador de mensaje
     * @return (<code>String</code>) Identificador del mensaje
     */
    public String getMsgID () {
        return msgID;
    }

}
