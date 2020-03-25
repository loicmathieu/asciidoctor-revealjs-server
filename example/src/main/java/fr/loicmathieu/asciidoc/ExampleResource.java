package fr.loicmathieu.asciidoc;

import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjs;
import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjsWatcher;
import io.quarkus.arc.Unremovable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;


@Path("/")
public class ExampleResource {

    @Inject
    AsciidoctorRevealjs asciidocRevealjs;

    @Inject
    AsciidoctorRevealjsWatcher asciidocWatcher;

    private Thread watcherThread;

    @PostConstruct
    void startWatcher(){
        System.out.println("Starting the AsciidoctorRevealjsWatcher");
        watcherThread = new Thread(() -> asciidocWatcher.watchFileChange());
        watcherThread.start();
    }

    @PreDestroy
    void stopWatcher() {
        System.out.println("Stoping the AsciidoctorRevealjsWatcher");
        asciidocWatcher.endWatchFileChange();
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    public String hello() throws IOException {
        return asciidocRevealjs.generateSlides();
    }
}