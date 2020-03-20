package fr.loicmathieu.asciidoctor.revealjs.server;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AsciidoctorRevealjs {

    private String slidePath;
    private String htmlPath;
    private String revealJsDir;
    private String revealJsTheme;

    AsciidoctorRevealjs(String slidePath, String revealJsDir, String revealJsTheme){
        this.slidePath = slidePath;
        this.htmlPath = slidePath.substring(0, slidePath.lastIndexOf('.') ) + ".html";
        this.revealJsDir = revealJsDir;
        this.revealJsTheme = revealJsTheme;
    }

    public String generateSlides() throws IOException {
        System.out.println("Generating slides from Asciidoctor");
        long start = System.currentTimeMillis();
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.requireLibrary("asciidoctor-revealjs");
        asciidoctor.requireLibrary("asciidoctor-diagram");

        Options options = OptionsBuilder.options()
                .backend("revealjs")
                .safe(SafeMode.UNSAFE)
                .attributes(
                        AttributesBuilder.attributes()
                                .attribute("revealjsdir", this.revealJsDir)
                                .attribute("revealjs_theme", this.revealJsTheme)
                )
                .get();

        asciidoctor.convertFile(new File(this.slidePath), options);

        java.nio.file.Path path = Paths.get(this.htmlPath);
        String html = new String(Files.readAllBytes(path));

        // add livereload websocket
        // FIXME add this only in dev mode
        Document sourcePage = Jsoup.parse(html);
        sourcePage.body().lastElementSibling().after(
                String.format("<script src=\"%s\"></script>", "browserWatch.js")
        );
        System.out.println("Slides generated in " + (System.currentTimeMillis() - start) + "ms");

        return sourcePage.outerHtml();
    }
}
