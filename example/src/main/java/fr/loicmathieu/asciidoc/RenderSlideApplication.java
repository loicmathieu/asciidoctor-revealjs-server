package fr.loicmathieu.asciidoc;

import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjs;
import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjsHandler;
import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjsWatcher;
import io.vertx.ext.web.Router;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class RenderSlideApplication {

    @Inject
    AsciidoctorRevealjs asciidocRevealjs;

    @Inject
    AsciidoctorRevealjsWatcher asciidocWatcher;

    // this will start the watcher that trigger browser live reload, it will be automatically stopped at application shutdown
    @PostConstruct
    void startWatcher(){
        asciidocWatcher.startWatchFileChange();
    }

    // this will create a route on '/' that will serve the rendered slides
    public void init(@Observes Router router) throws Exception {
        router.route("/").blockingHandler(AsciidoctorRevealjsHandler.create(asciidocRevealjs));
    }
}
