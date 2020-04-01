package fr.loicmathieu.asciidoctor.revealjs.server.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class AsciidoctorRevealjsServerProcessor {

    private static final String FEATURE = "asciidoctor-revealjs-server";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
