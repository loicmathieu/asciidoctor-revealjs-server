package fr.loicmathieu.asciidoctor.revealjs.server;

import io.quarkus.vertx.ConsumeEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/browserWatch/socket")
@ApplicationScoped
public class BrowserReloadWebSocket {
    private static final Logger LOGGER = Logger.getLogger(BrowserReloadWebSocket.class);
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.debugf("Registering sessions %s", session.getId());
        sessions.put(session.getId(), session);
    }

    @OnClose
    public void onClose(Session session) {
        LOGGER.debugf("Removing sessions %s", session.getId());
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.debugf("Removing sessions %s for error %s", session.getId(), throwable.getMessage());
        sessions.remove(session.getId());
    }

    @ConsumeEvent("browser-live-reload")
    public void broadcast(String message) {
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendObject(message, result ->  {
                LOGGER.debugf("Sending messages to %s", session.getId());
                if (result.getException() != null) {
                    LOGGER.debugf("Unable to send message: %s", result.getException());
                }
            });
        });
    }
}
