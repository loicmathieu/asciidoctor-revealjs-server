package fr.loicmathieu.asciidoctor.revealjs.server;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

public class AsciidoctorRevealjsHandler implements Handler<RoutingContext> {
    private AsciidoctorRevealjs asciidocRevealjs;

    AsciidoctorRevealjsHandler(AsciidoctorRevealjs asciidocRevealjs){
        this.asciidocRevealjs = asciidocRevealjs;
    }

    public static Handler<RoutingContext> create(AsciidoctorRevealjs asciidocRevealjs) {
        return new AsciidoctorRevealjsHandler(asciidocRevealjs);
    }

    @Override
    public void handle(RoutingContext event) {
        String response;
        try {
            response = asciidocRevealjs.generateSlides();
            event.response().setStatusCode(200);
        }
        catch(IOException e) {
            response = "Unable to render slides: " + e.getMessage();
            event.response().setStatusCode(500);
        }
        event.response().putHeader("Content-Type", "text/html");
        event.response().end(response);
    }
}
