package com.esiea.pootp.Object.Medicine;

import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.ObjectMonster;


public class Medicine extends ObjectMonster {
    private String name;
    private int weight;
    private MedecineType medecineType;
    
    public Medicine(String name, int weight, MedecineType medecineType) {
        super(weight);
        this.name = name;
        this.weight = weight;
        this.medecineType = medecineType; 
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public MedecineType getMedecineType() {
        return medecineType;
    }

    @Override
    public void use(Monster monster) {
        // Implementation of medicine usage effect
    }
    
    
}
