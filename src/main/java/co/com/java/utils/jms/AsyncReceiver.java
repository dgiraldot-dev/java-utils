package co.com.java.utils.jms;

import java.util.Hashtable;
import javax.jms.*;
import javax.naming.*;
import co.com.java.utils.exception.JavaException;

/**
 * Clase para consulta asíncrona con servidor JMS
 * @author IRamirezS
 * @version 1.0
 * @since 2019-06-20
 */
public class AsyncReceiver implements MessageListener, ExceptionListener {

    private static String textMsg = "";
    
    public AsyncReceiver () {

    }
    
    public AsyncReceiver (String initialContextFactory, String providerURL, String queueConnectionFactory, String queueResponse) {
        try {
            setup(initialContextFactory, providerURL, queueConnectionFactory, queueResponse);
        } catch (NamingException | JMSException | InterruptedException e) {
            new JavaException().catchException(e);
        }
    }

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
    public void setup ( String initialContextFactory, String providerURL, String queueConnectionFactory, String queueResponse ) throws NamingException, JMSException, InterruptedException {
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

        // Configurar busqueda asíncrona de los mensajes
        AsyncReceiver receiver;
        receiver = new AsyncReceiver();
        queueReceiver.setMessageListener( receiver );

        // Configurar excepción asíncrona de los mensajes en caso de error
        queueConn.setExceptionListener( receiver );

        // Iniciar conexión
        queueConn.start();

        // Esperar por mensajes
        System.out.print( "Esperando por mensajes" );
        for ( int i = 0; i < 20; i++ ) {
            Thread.sleep( 1000 );
            System.out.print( "." );
        }
        queueConn.close();
    }

    /**
     * Obtener mensaje del JMS
     * @return (<code>String</code>) Mensaje de respuesta
     */
    public String getMessage () {
        return AsyncReceiver.textMsg;
    }

    /**
     * Este metodo es llamado de forma asíncrona por JMS cuando algún error ocurre. Esta función siempre se debe definir ya que la función onMessage no detecta las excepciones.
     * @param exception (<code>JMSException</code>) Exepción de la cola de respuesta.
     */
    public void onException ( JMSException exception ) {
        System.err.println( "Un error ha ocurrido: " + exception );
    }

    /**
     * Este metodo es llamado de forma asíncrona por JMS cuando un mensaje llega a la cola de respuesta. Las aplicaciones de cliente no identifican excepciones en esta función.
     * @param message (<code>Message</code>) Mensaje obtenido de la cola de respuesta.
     */
    public void onMessage ( Message message ) {
        TextMessage msg = (TextMessage) message;
        try {
            System.out.println(msg.getText());
        } catch ( JMSException e ) {
            new JavaException().catchException(e);
        }
    }
}
