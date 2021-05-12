package at.ac.univie.imagechecker;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class Startup {

    @PostConstruct
    public void init() {
        log.info("***Loading opencv_java.class***");
        Loader.load(opencv_java.class);
    }

}
