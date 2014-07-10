package peacock.controller.sample;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/chat")
public class WebSocketServerSample {
    
    /**
     * Callback hook for Message Events.
     * 
     * This method will be invoked when a client send a message.
     * 
     * @param message The text message
     * @param userSession The session of the client
     */
    @OnMessage
    public void onMessage(String message, Session userSession) {
        for (Session session : userSession.getOpenSessions()) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        }
    }
}