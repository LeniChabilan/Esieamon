package com.esiea.pootp.Attack;

import java.util.HashMap;

import com.esiea.pootp.Monster.Monster;

public abstract class Attack {
    protected String name;

    public Attack(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> performAttack(Monster attacker, Monster defender) {
        return new HashMap<>();
    }
}
