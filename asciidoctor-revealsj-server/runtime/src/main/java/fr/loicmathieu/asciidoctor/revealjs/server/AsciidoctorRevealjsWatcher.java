package fr.loicmathieu.asciidoctor.revealjs.server;


import io.vertx.mutiny.core.eventbus.EventBus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Duration;

public class AsciidoctorRevealjsWatcher {
    private boolean watchForChange = true;
    private FileTime lastModifiedTime;
    private EventBus bus;
    private String slidePath;
    private Duration watchPeriod;

    AsciidoctorRevealjsWatcher(EventBus bus, String slidePath,  Duration watchPeriod){
        this.bus = bus;
        this.slidePath = slidePath;
        this.watchPeriod = watchPeriod;
    }

    public void watchFileChange() {
        Path slidesPath = Paths.get(this.slidePath);

        try {
            this.lastModifiedTime = Files.getLastModifiedTime(slidesPath);

            while(this.watchForChange){
                Thread.sleep(watchPeriod.toMillis());

                FileTime currentModifiedTime = Files.getLastModifiedTime(slidesPath);
                if(currentModifiedTime.compareTo( this.lastModifiedTime ) > 0){
                    System.out.println("File changed");
                    bus.sendAndForget("browser-live-reload", "reload");
                    this.lastModifiedTime = currentModifiedTime;
                }
            }
        } catch (IOException | InterruptedException e) {
           throw new RuntimeException(e);
        }
    }

    public void endWatchFileChange() {
        this.watchForChange = false;
    }
}
