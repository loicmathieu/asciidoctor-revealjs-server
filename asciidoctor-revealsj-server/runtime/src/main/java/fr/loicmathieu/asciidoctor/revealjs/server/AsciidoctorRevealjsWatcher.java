package fr.loicmathieu.asciidoctor.revealjs.server;


import io.vertx.mutiny.core.eventbus.EventBus;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Duration;

public class AsciidoctorRevealjsWatcher {
    private static final Logger LOGGER = Logger.getLogger(AsciidoctorRevealjsWatcher.class);

    private boolean watchForChange = true;
    private FileTime lastModifiedTime;
    private EventBus bus;
    private String slidePath;
    private Duration watchPeriod;

    private Thread watcherThread;

    AsciidoctorRevealjsWatcher(String slidePath,  Duration watchPeriod){
        this.slidePath = slidePath;
        this.watchPeriod = watchPeriod;
    }

    void setBus(EventBus bus){
        this.bus = bus;
    }

    void watchFileChange() {
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
        LOGGER.info("Stopping the AsciidoctorRevealjsWatcher");
        this.watchForChange = false;
    }

    public void startWatchFileChange() {
        LOGGER.info("Starting the AsciidoctorRevealjsWatcher");
        this.watchForChange = true;

        watcherThread = new Thread(() -> this.watchFileChange());
        watcherThread.start();
    }
}
