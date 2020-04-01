package fr.loicmathieu.asciidoctor.revealjs.server;

import io.quarkus.arc.Unremovable;
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

    @Produces
    @Dependent
    @Default
    AsciidoctorRevealjs asciidocRevealjs() throws IOException {
        String slidesPath = ConfigProvider.getConfig().getValue("quarkus.asciidoctor.revealjs.slides-path", String.class);
        String revealJsDir = ConfigProvider.getConfig().getValue("quarkus.asciidoctor.revealjs.reveal-js-dir", String.class);
        String revealJsTheme = ConfigProvider.getConfig().getValue("quarkus.asciidoctor.revealjs.reveal-js-theme", String.class);
        return new AsciidoctorRevealjs(slidesPath, revealJsDir, revealJsTheme);
    }

    @Produces
    @Dependent
    @Default
    @Unremovable
    AsciidoctorRevealjsWatcher asciidocWatcher(){
        String slidesPath = ConfigProvider.getConfig().getValue("quarkus.asciidoctor.revealjs.slides-path", String.class);
        Duration watchPeriod = ConfigProvider.getConfig().getValue("quarkus.asciidoctor.revealjs.watch-period", Duration.class);
        return new AsciidoctorRevealjsWatcher(bus, slidesPath, watchPeriod);
    }
}
