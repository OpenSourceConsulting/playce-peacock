package peacock.controller.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebSocketClientSample {

	private Session userSession;
	private MessageHandler messageHandler;
	
	public WebSocketClientSample(URI endpointURI) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
    /**
     * Callback hook for Connection open events.
     * 
     * @param userSession
     *            the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
    }
 
    /**
     * Callback hook for Connection close events.
     * 
     * @param userSession
     *            the userSession which is getting closed.
     * @param reason
     *            the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }
 
    /**
     * Callback hook for Message Events. This method will be invoked when a
     * client send a message.
     * 
     * @param message
     *            The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null)
            this.messageHandler.handleMessage(message);
    }
 
    /**
     * register message handler
     * 
     * @param message
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }
 
    /**
     * Send a message.
     * 
     * @param user
     * @param message
     */
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }
 
    /**
     * Message handler.
     * 
     * @author Jiji_Sasidharan
     */
    public static interface MessageHandler {
        public void handleMessage(String message);
    }
    
    /*
    public static void main(String[] args) throws Exception {
        final WebSocketClientSample clientEndPoint = new WebSocketClientSample(new URI("ws://localhost:8080/controller/chat"));
        clientEndPoint.addMessageHandler(new WebSocketClientSample.MessageHandler() {
            public void handleMessage(String message) {
                JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
                String userName = jsonObject.getString("user");
                if (!"bot".equals(userName)) {
                    clientEndPoint.sendMessage(getMessage("Hello " + userName +", How are you?"));
                    // other dirty bot logic goes here.. :)
                }
            }
        });
 
        while (true) {
            clientEndPoint.sendMessage(getMessage("Hi There!!"));
            Thread.sleep(30000);
        }
    }
 
    private static String getMessage(String message) {
        return Json.createObjectBuilder()
                       .add("user", "bot")
                       .add("message", message)
                   .build()
                   .toString();
    }
    */
    

    public static void main(String[] args) throws Exception {
        final WebSocketClientSample clientEndPoint = new WebSocketClientSample(new URI("ws://localhost:8180/controller/chat"));
        
        String name = null;
        
        if (args.length == 0) {
        	name = "scpark";
        } else {
        	name = args[1];
        }

        String input = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        clientEndPoint.addMessageHandler(new WebSocketClientSample.MessageHandler() {
            public void handleMessage(String message) {
                JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
                System.out.println(jsonObject.getString("user") + ": " + jsonObject.getString("message"));
            }
        });
 
        while (true) {
        	input = br.readLine();
        	
        	if ("quit".equals(input)) {
        		break;
        	}
        	
        	clientEndPoint.sendMessage(Json.createObjectBuilder().add("user", name).add("message", input).build().toString());
        }
    }
}
