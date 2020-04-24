package fr.loicmathieu.asciidoctor.revealjs.server;

import io.vertx.mutiny.core.eventbus.EventBus;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;

@ApplicationScoped
public class AsciidoctorRevealjsProducer {
    @Inject
    EventBus bus;

    private volatile AsciidoctorRevealjs asciidoctorRevealjs;
    private volatile AsciidoctorRevealjsWatcher asciidoctorRevealjsWatcher;

    @Produces
    @Dependent
    @Default
    AsciidoctorRevealjs asciidocRevealjs() throws IOException {
        return this.asciidoctorRevealjs;
    }

    @Produces
    @Dependent
    @Default
    AsciidoctorRevealjsWatcher asciidocWatcher(){
        this.asciidoctorRevealjsWatcher.setBus(this.bus);//lazily set the bus
        return this.asciidoctorRevealjsWatcher;
    }

    public void initialize(AsciidoctorRevealjs asciidoctorRevealjs, AsciidoctorRevealjsWatcher asciidoctorRevealjsWatcher) {
        this.asciidoctorRevealjs = asciidoctorRevealjs;
        this.asciidoctorRevealjsWatcher = asciidoctorRevealjsWatcher;
    }
}
