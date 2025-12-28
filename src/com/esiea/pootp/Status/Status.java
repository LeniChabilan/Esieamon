package com.esiea.pootp.Status;

public class Status {
    private String name;
    private int duration; 

    public Status(String name) {
        this.name = name;
        this.duration = -1;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
}