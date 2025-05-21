package com.springboot.conferentieapp;

import java.time.LocalDate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "conferentie")
public class ConferentieEigenschappen {
    private LocalDate start;
    private LocalDate einde;

    public LocalDate getStart() { return start; }
    public void setStart(LocalDate start) { this.start = start; }

    public LocalDate getEinde() { return einde; }
    public void setEinde(LocalDate einde) { this.einde = einde; }
}
