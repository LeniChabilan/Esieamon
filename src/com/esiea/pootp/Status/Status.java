package com.esiea.pootp.Status;

import java.util.HashMap;

import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Ground.Ground;

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

    public HashMap<String, String> performStatus(Monster monster, Ground ground) {
        HashMap<String, String> result = new HashMap<>();
        result.put("attackAble", "true");
        return result;
    }
}