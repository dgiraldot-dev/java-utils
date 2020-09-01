package com.qa.automation.utils.java.utils.jms;

import java.util.*;
import javax.jms.*;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Clase para consultar colas en JMS
 * @author IRamirezS
 * @version 1.0
 * @since 2019-06-20
 */
public class Search {

    public Search ( String initialContextFactory, String providerURL, String queueConnectionFactory, String queueToBrows ) throws NamingException, JMSException {

        // Definición de las variables de ambiente para conexión
        Hashtable < String, String > env = new Hashtable < String, String >();
        env.put( Context.INITIAL_CONTEXT_FACTORY, initialContextFactory );
        env.put( Context.PROVIDER_URL, providerURL );

        // Contexto inicial
        InitialContext ctx = new InitialContext( env );

        // Cola de a buscar
        Queue queue = (Queue) ctx.lookup( queueToBrows );

        // Cola de conexión 
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup( queueConnectionFactory );

        // Crear conexión
        QueueConnection queueConn = connFactory.createQueueConnection();

        // Crear una sesión con la cola de petición
        QueueSession queueSession = queueConn.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );

        // Crear una cola al buscador
        QueueBrowser queueBrowser = queueSession.createBrowser( queue );

        // Iniciar conexión
        queueConn.start();

        // Obtener mensajes
        Enumeration < ? > e = queueBrowser.getEnumeration();
        int numMsgs = 0;

        // Contar el número de mensajes
        while ( e.hasMoreElements() ) {
            //Message message = (Message) e.nextElement();
            e.nextElement();
            numMsgs++;
        }

        // Mostrar cantidad de mensajes en la cola
        System.out.println(queue + " tiene " + numMsgs + " mensajes en cola.");

        // Finalizar conexión
        queueConn.close();
    }

}