package com.gest.art.security.config;

import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class readerConfig {

    final Environment environment;

    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(environment.getProperty("tesseracts.data.path"));
        tesseract.setLanguage(environment.getProperty("tesseracts.data.language"));
        tesseract.setTessVariable("user_defined_dpi", environment.getProperty("tesseracts.data.dpi"));
        return tesseract;
    }
}
