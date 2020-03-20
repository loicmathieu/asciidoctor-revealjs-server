package fr.loicmathieu.asciidoctor.revealjs.server;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.time.Duration;

/**
 * Configuration of the asciidoctor-revealjs extension
 */
@ConfigRoot(phase = ConfigPhase.RUN_TIME, name = "asciidoctor.revealjs")
public class AsciidoctorRevealjsConfig {
    /**
     * The path to the slides adoc file.
     */
    @ConfigItem(defaultValue = "../src/main/asciidoc/slides.adoc")
    public String slidesPath;

    /**
     * The revealjs dir
     */
    @ConfigItem(defaultValue = "https://cdn.jsdelivr.net/npm/reveal.js@3.9.2")
    public String revealJsDir;

    /**
     * The revealjs theme
     */
    @ConfigItem(defaultValue = "black")
    public String revealJsTheme;

    /**
     * The sleep period between two watch
     */
    @ConfigItem(defaultValue = "PT1S")
    public Duration watchPeriod;
}
