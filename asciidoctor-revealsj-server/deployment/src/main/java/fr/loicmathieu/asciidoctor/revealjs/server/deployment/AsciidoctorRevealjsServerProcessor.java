package fr.loicmathieu.asciidoctor.revealjs.server.deployment;

import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjsProducer;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class AsciidoctorRevealjsServerProcessor {

    private static final String FEATURE = "asciidoctor-revealjs-server";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem build() {
        return AdditionalBeanBuildItem.unremovableOf(AsciidoctorRevealjsProducer.class);
    }

}
