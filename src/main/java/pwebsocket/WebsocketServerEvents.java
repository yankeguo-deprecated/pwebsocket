package pwebsocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

/**
 * @author Lasse Steenbock Vestergaard
 * <p>
 * This class is responsible for handling all events relating to the websocket server
 */
@WebSocket
public class WebsocketServerEvents {

    public Session session;
    public WebsocketServerController ctrl;

    public WebsocketServerEvents(WebsocketServerController ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Handles new client connections, and adding the client to a list of clients, so that we can send messages to it later
     *
     * @param session The connection session between the server and a specific client (not all clients!)
     */
    @OnWebSocketConnect
    public void handleConnect(Session session) {
        this.session = session;
        ctrl.join(this);
    }

    /**
     * Handles closing down a specific client connection, and removes the client from the list of clients
     *
     * @param statusCode Reason code for closing the connection
     * @param reason     Description of close down reason
     */
    @OnWebSocketClose
    public void handleClose(int statusCode, String reason) {
        ctrl.remove(this);
    }

    /**
     * Handles incoming messages, and passes them on to the Processing sketch's websocket event method
     *
     * @param message The incoming message
     */
    @OnWebSocketMessage
    public void handleMessage(String message) {
        ctrl.sendToOnMessageListener(message);
    }

    @OnWebSocketMessage
    public void handleBinary(byte[] buf, int offset, int length) {
        ctrl.sendToOnBinaryListener(buf, offset, length);
    }

    /**
     * Handling connection errors and writes the to the console
     *
     * @param error The error that occurred
     */
    @OnWebSocketError
    public void handleError(Throwable error) {
        error.printStackTrace();
    }
}