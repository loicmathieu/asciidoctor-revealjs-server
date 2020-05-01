package fr.loicmathieu.asciidoctor.revealjs.server.runtime;

import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjs;
import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjsProducer;
import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjsWatcher;
import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.ShutdownContext;
import io.quarkus.runtime.annotations.Recorder;

import java.io.IOException;

@Recorder
public class AsciidoctorRevealjsRecorder {
    public void configureAsciidoctorRevealjs(BeanContainer beanContainer, AsciidoctorRevealjsConfig configuration,
                                             ShutdownContext shutdownContext) throws IOException {

        AsciidoctorRevealjs asciidoctorRevealjs = asciidocRevealjs(configuration);
        AsciidoctorRevealjsWatcher asciidoctorRevealjsWatcher = asciidocWatcher(configuration);

        AsciidoctorRevealjsProducer producer = beanContainer.instance(AsciidoctorRevealjsProducer.class);
        producer.initialize(asciidoctorRevealjs, asciidoctorRevealjsWatcher);

        shutdownContext.addShutdownTask(() -> asciidoctorRevealjsWatcher.endWatchFileChange());
    }

    private AsciidoctorRevealjs asciidocRevealjs(AsciidoctorRevealjsConfig configuration) throws IOException {
        return new AsciidoctorRevealjs(configuration.slidesPath, configuration.revealJsDir, configuration.revealJsTheme);
    }

    private AsciidoctorRevealjsWatcher asciidocWatcher(AsciidoctorRevealjsConfig configuration){
        return new AsciidoctorRevealjsWatcher( configuration.slidesPath, configuration.watchPeriod);
    }
}
