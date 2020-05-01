package fr.loicmathieu.asciidoctor.revealjs.server.deployment;

import fr.loicmathieu.asciidoctor.revealjs.server.runtime.AsciidoctorRevealjsConfig;
import fr.loicmathieu.asciidoctor.revealjs.server.runtime.AsciidoctorRevealjsRecorder;
import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.ShutdownContextBuildItem;

import java.io.IOException;

class AsciidoctorRevealjsServerProcessor {

    private static final String FEATURE = "asciidoctor-revealjs-server";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void build(AsciidoctorRevealjsRecorder recorder, BeanContainerBuildItem beanContainerBuildItem,
               AsciidoctorRevealjsConfig configuration, ShutdownContextBuildItem shutdownContext) throws IOException {

        recorder.configureAsciidoctorRevealjs(beanContainerBuildItem.getValue(), configuration, shutdownContext);
    }
}
