# asciidoctor-revealjs-server

A Quarkus extension for Asciidoctor and Revealjs.

It uses AsciidoctorJ and Revealjs in order to be able to build asciidoctor based slides and render them with Revealjs thanks to Quarkus webserver.

It includes browser livereload so you can update your slides and see the results instantly in your brower.

Go see the [example](example) application for how to use it.

As the extension is not published in Maven central, you need to build if first.

## Building the asciidoctor-revealjs-server extension

After cloning this repository, got to the asciidoctor-revealjs-server directory and build the extension via Maven:

```
mvn clean install
```

## Using the asciidoctor-revealjs-server extension

If you don't want to follow these steps, you can directly go to the [example](example) application.

First, you need to generate a Quarkus application. <br/>
Both Resteasy or Vert.X web based application will work, as Vert.X web based applications are lighter,
 and we don't need anything more than a web server, I will use it for this example. 
 For more info of using Vert.X web with Quarkus see: [https://quarkus.io/guides/reactive-routes](Reactive Routes)

To generate the application, use the following Maven command:
```
mvn io.quarkus:quarkus-maven-plugin:<quarkus_version>:create \
    -DprojectGroupId=org.acme \
    -DprojectArtifactId=asciidoctor-revealsj-example \
    -DclassName="org.acme.asciidoctor.revealjs.RenderSlidesResource" \
    -Dpath="/hello" \
    -Dextensions="vertx-web"
cd asciidoctor-revealsj-example
```

Then add the `asciidoctor-revealjs-server` extension to your `pom.xml`.

```xml
    <dependency>
      <groupId>fr.loicmathieu.asciidoc</groupId>
      <artifactId>asciidoctor-revealjs-server</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
```

Then, delete the `RenderSlidesResource` and create a `RenderSlideApplication` that will setup a Vert.x handler to serve the rendered slides.

```java
import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjs;
import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjsHandler;
import fr.loicmathieu.asciidoctor.revealjs.server.AsciidoctorRevealjsWatcher;
import io.vertx.ext.web.Router;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class RenderSlideApplication {

    @Inject
    AsciidoctorRevealjs asciidocRevealjs;

    @Inject
    AsciidoctorRevealjsWatcher asciidocWatcher;

    // this will start the watcher that trigger browser live reload, it will be automatically stopped at application shutdown
    @PostConstruct
    void startWatcher(){
        asciidocWatcher.startWatchFileChange();
    }

    // this will create a route on '/' that will serve the rendered slides
    public void init(@Observes Router router) throws Exception {
        router.route("/").blockingHandler(AsciidoctorRevealjsHandler.create(asciidocRevealjs));
    }
}
```

Finally, add a `src/main/asciidoc/slides.adoc` file with the content of your slides.

After launching Quarkus with `mvn clean quarkus:dev` you can open you browser at http://localhost:8080, and your slides will be rendered. 
Each time you will modify the `slides.adoc` file your browser will be reloaded automatically. 