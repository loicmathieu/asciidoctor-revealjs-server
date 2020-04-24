package fr.loicmathieu.asciidoctor.revealjs.server;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.annotations.Recorder;

import java.io.IOException;

@Recorder
public class AsciidoctorRevealjsRecorder {
    public void configureAsciidoctorRevealjs(BeanContainer beanContainer, AsciidoctorRevealjsConfig configuration) throws IOException {

        AsciidoctorRevealjs asciidoctorRevealjs = asciidocRevealjs(configuration);
        AsciidoctorRevealjsWatcher asciidoctorRevealjsWatcher = asciidocWatcher(configuration);

        AsciidoctorRevealjsProducer producer = beanContainer.instance(AsciidoctorRevealjsProducer.class);
        producer.initialize(asciidoctorRevealjs, asciidoctorRevealjsWatcher);
    }

    private AsciidoctorRevealjs asciidocRevealjs(AsciidoctorRevealjsConfig configuration) throws IOException {
        return new AsciidoctorRevealjs(configuration.slidesPath, configuration.revealJsDir, configuration.revealJsTheme);
    }

    private AsciidoctorRevealjsWatcher asciidocWatcher(AsciidoctorRevealjsConfig configuration){
        return new AsciidoctorRevealjsWatcher( configuration.slidesPath, configuration.watchPeriod);
    }
}
