package com.qa.automation.utils.java.utils.jms;

import javax.jms.JMSException;
import javax.naming.NamingException;
import com.qa.automation.utils.java.utils.exception.JavaException;

/**
 * Clase principal para consulta, envío y recepción de mensajes JMS
 * @author IRamirezS
 * @version 1.0
 * @since 2019-06-20
 */
public class PointToPoint {

    private String  initialContextFactory;
    private String  providerURL;
    private String  queueConnectionFactory;
    private String  queueRequest;
    private String  queueResponse;
    private String  messageRequest;

    public PointToPoint (String initialContextFactory, String providerURL, String queueConnectionFactory, String queueRequest, String queueResponse, String messageRequest) {
        setup(initialContextFactory, providerURL, queueConnectionFactory, queueRequest, queueResponse, messageRequest);
    }
    
    /**
     * Constructor de la clase
     * @param initialContextFactory (<code>String</code>) Contexto inicial de factoría
     * @param providerURL (<code>String</code>) URL del proveedor
     * @param queueConnectionFactory (<code>String</code>) Cola de conexión de factoría
     * @param queueRequest (<code>String</code>) Cola de petición
     * @param queueResponse (<code>String</code>) Cola de respuesta
     * @param messageRequest (<code>String</code>)
     */
    public void setup ( String initialContextFactory, String providerURL, String queueConnectionFactory, String queueRequest, String queueResponse, String messageRequest ) {
        this.initialContextFactory = initialContextFactory;
        this.providerURL = providerURL;
        this.queueConnectionFactory = queueConnectionFactory;
        this.queueRequest = queueRequest;
        this.queueResponse = queueResponse;
        this.messageRequest = messageRequest;
    }

    /**
     * Consultar la cola de mensaje enviados
     */
    public void BrowseRequestQueue () {
        try {
            new Search( initialContextFactory, providerURL, queueConnectionFactory, queueRequest );
        } catch ( NamingException | JMSException e ) {
            new JavaException().catchException(e);
        }
    }

    /**
     * Consultar la cola de mensajes recibidos
     */
    public void BrowseResponseQueue () {
        try {
            new Search( initialContextFactory, providerURL, queueConnectionFactory, queueResponse );
        } catch ( NamingException | JMSException e ) {
            new JavaException().catchException(e);
        }
    }

    /**
     * Obtener mensajes asíncronos
     */
    public void ReceiveAsyncMessage () {
        new AsyncReceiver( initialContextFactory, providerURL, queueConnectionFactory, queueResponse );
    }

    /**
     * Obtener mensajes síncronos
     */
    public void ReceiveSyncMessage () {
        try {
            new SyncReceiver( initialContextFactory, providerURL, queueConnectionFactory, queueResponse );
        } catch ( NamingException | JMSException | InterruptedException e ) {
            new JavaException().catchException(e);
        }
    }

    /**
     * Enviar mensajes a cola
     * @return (<code>String</code>) Número de mensaje enviado
     */
    public String SendMessage () {
        try {
            Sender sender = new Sender( initialContextFactory, providerURL, queueConnectionFactory, queueRequest, messageRequest );
            return sender.getMsgID();
        } catch ( NamingException | JMSException e ) {
            new JavaException().catchException(e);
        }
        return "";
    }

}