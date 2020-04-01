package fr.loicmathieu.asciidoctor.revealjs.server;

import io.quarkus.vertx.ConsumeEvent;

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
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Registering sessions " + session.getId());
        sessions.put(session.getId(), session);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Removing sessions " + session.getId());
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Removing sessions " + session.getId() + " for error " + throwable.getMessage());
        sessions.remove(session.getId());
    }

    @ConsumeEvent("browser-live-reload")
    public void broadcast(String message) {
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendObject(message, result ->  {
                System.out.println("Sending messages to " + session.getId());
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}
