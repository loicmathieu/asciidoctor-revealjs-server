# example project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

It uses asciidocj-revealjs to generates revealsj slides from asciidoctor files: https://github.com/asciidoctor/asciidoctorj-reveal.js


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

The slides are located in `src/main/asciidoc/slides.adoc`, when updating them, your browser will automatically
reload the page thanks to a websocket and some js we include inside the generated HTML.

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `asciidoctor-revealsj-example-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/asciidoctor-revealsj-example-1.0.0-SNAPSHOT-runner.jar`.

**WARNING : this is not supported yet**

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/asciidoctor-revealsj-example-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide.

**WARNING : this is not supported yet**