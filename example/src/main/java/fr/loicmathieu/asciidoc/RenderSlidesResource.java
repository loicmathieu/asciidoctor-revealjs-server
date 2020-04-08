package fr.loicmathieu.asciidoc;

import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjs;
import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjsWatcher;
import io.quarkus.vertx.web.Route;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class RenderSlidesResource {

    @Inject
    AsciidoctorRevealjs asciidocRevealjs;

    @Inject
    AsciidoctorRevealjsWatcher asciidocWatcher;

    @PostConstruct
    void startWatcher(){
        System.out.println("Starting the AsciidoctorRevealjsWatcher");
        asciidocWatcher.startWatchFileChange();
    }

    @PreDestroy
    void stopWatcher() {
        System.out.println("Stopping the AsciidoctorRevealjsWatcher");
        asciidocWatcher.endWatchFileChange();
    }

    @Route(path = "/", methods = HttpMethod.GET)
    public void renderSlides(RoutingContext rc) throws IOException {
        rc.response().putHeader("Content-Type", "text/html");
        rc.response().end(asciidocRevealjs.generateSlides());
    }
}