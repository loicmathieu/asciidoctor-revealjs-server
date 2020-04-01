package fr.loicmathieu.asciidoctor.revealjs.server;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AsciidoctorRevealjs {

    private String slidePath;
    private String htmlPath;
    private String revealJsDir;
    private String revealJsTheme;

    private String browserWatch;

    AsciidoctorRevealjs(String slidePath, String revealJsDir, String revealJsTheme) throws IOException {
        this.slidePath = slidePath;
        this.htmlPath = slidePath.substring(0, slidePath.lastIndexOf('.') ) + ".html";
        this.revealJsDir = revealJsDir;
        this.revealJsTheme = revealJsTheme;

        init();
    }

    private void init() throws IOException {
        System.out.println("Load browserWatch.js");
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("browserWatch.js");
             Reader reader = new BufferedReader(new InputStreamReader(is, Charset.forName(StandardCharsets.UTF_8.name())))) {
            StringBuilder browserWatchBuilder = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                browserWatchBuilder.append((char) c);
            }
            this.browserWatch = browserWatchBuilder.toString();
        }
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
        sourcePage.body().append("<script>\n" + browserWatch + "\n</script>");

        System.out.println("Slides generated in " + (System.currentTimeMillis() - start) + "ms");

        return sourcePage.outerHtml();
    }
}
