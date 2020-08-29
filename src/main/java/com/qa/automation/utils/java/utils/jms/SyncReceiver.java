package com.qa.automation.utils.java.utils.jms;

import java.util.Hashtable;
import javax.jms.*;
import javax.naming.*;

/**
 * Clase para el envío de mensajes a JMS
 * @author IRamirezS
 * @version 1.0
 * @since 2019-06-20
 */
public class SyncReceiver {

    private String msg;

    /**
     * Crea una conexión Point-to-Point y recibe los mensajes de forma asíncrona.
     * @param initialContextFactory (<code>String</code>) Contexto inicial de factoría
     * @param providerURL (<code>String</code>) URL del proveedor
     * @param queueConnectionFactory (<code>String</code>) Cola de conexión de factoría
     * @param queueResponse (<code>String</code>) Cola de respuesta
     * @throws NamingException
     * @throws JMSException
     * @throws InterruptedException
     */
    public SyncReceiver ( String initialContextFactory, String providerURL, String queueConnectionFactory, String queueResponse ) throws NamingException, JMSException, InterruptedException {

        // Definición de las variables de ambiente para conexión
        Hashtable < String, String > env = new Hashtable < String, String >();
        env.put( Context.INITIAL_CONTEXT_FACTORY, initialContextFactory );
        env.put( Context.PROVIDER_URL, providerURL );

        // Contexto inicial
        InitialContext ctx = new InitialContext( env );

        // Cola de response
        Queue queue = (Queue) ctx.lookup( queueResponse );

        // Cola de conexión 
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup( queueConnectionFactory );

        // Crear conexión
        QueueConnection queueConn = connFactory.createQueueConnection();

        // Crear una sesión con la cola de petición
        QueueSession queueSession = queueConn.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );

        // Crear una cola de respuesta
        QueueReceiver queueReceiver = queueSession.createReceiver( queue );

        // Iniciar conexión
        queueConn.start();

        // Recibir el mensaje
        TextMessage message = (TextMessage) queueReceiver.receive();

        // Mostrar el mensaje
        System.out.println(message.getText());
        msg = message.getText();

        // Cerrar conexión
        queueConn.close();
    }

    /**
     * Obtener el mensaje del JMS
     * @return (<code>String</code>) Mensaje de respuesta
     */
    public String getMessage () {
        return msg;
    }

}
