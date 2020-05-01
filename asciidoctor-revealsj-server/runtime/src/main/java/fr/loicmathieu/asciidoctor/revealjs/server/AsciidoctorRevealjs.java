package fr.loicmathieu.asciidoctor.revealjs.server;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.jboss.logging.Logger;
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
import java.nio.file.Path;

public class AsciidoctorRevealjs {

    private static final Logger LOGGER = Logger.getLogger(BrowserReloadWebSocket.class);

    private String slidePath;
    private String htmlFileName;
    private Path htmlPath;
    private String revealJsDir;
    private String revealJsTheme;

    private String browserWatch;

    AsciidoctorRevealjs(String slidePath, String revealJsDir, String revealJsTheme) throws IOException {
        this.slidePath = slidePath;
        this.htmlFileName = slidePath.substring(0, slidePath.lastIndexOf('.') ) + ".html";
        if(this.htmlFileName.indexOf('/') != -1){
            // remove the part before '/' as we only want the name of the
            this.htmlFileName = this.htmlFileName.substring(this.htmlFileName.lastIndexOf('/') + 1);
        }
        this.htmlPath = Files.createTempDirectory("slides-");
        this.revealJsDir = revealJsDir;
        this.revealJsTheme = revealJsTheme;

        init();
    }

    private void init() throws IOException {
        LOGGER.info("Load browserWatch.js");
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
        LOGGER.info("Generating slides from Asciidoctor");
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

        // set the destination path to a temporal directory
        options.setToDir(this.htmlPath.toString());

        asciidoctor.convertFile(new File(this.slidePath), options);
        String html = new String(Files.readAllBytes(this.htmlPath.resolve(this.htmlFileName)));

        // add livereload websocket
        // FIXME add this only in dev mode
        Document sourcePage = Jsoup.parse(html);
        sourcePage.body().append("<script>\n" + browserWatch + "\n</script>");

        LOGGER.infof("Slides generated in %sms", (System.currentTimeMillis() - start));

        return sourcePage.outerHtml();
    }
}
