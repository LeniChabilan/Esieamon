package com.esiea.pootp.Object.Medicine;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Ground.FloodedGround;
import com.esiea.pootp.Ground.NormalGround;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.ObjectMonster;
import com.esiea.pootp.Status.BurnedStatus;
import com.esiea.pootp.Status.NormalStatus;
import com.esiea.pootp.Status.ParalyzedStatus;
import com.esiea.pootp.Object.Medicine.MedecineType;
import com.esiea.pootp.Status.PoisonedStatus;


public class Medicine extends ObjectMonster {
    private int weight;
    private MedecineType medecineType;
    
    public Medicine(int weight, MedecineType medecineType) {
        super(weight);
        this.weight = weight;
        this.medecineType = medecineType; 
    }

    @Override
    public String getName() {
        switch (medecineType) {
            case BURN_HEAL:
                return "Antibrûlure";
            case PARALYZE_HEAL:
                return "Antiparalysie";
            case POISON_HEAL:
                return "Antipoison";
            case SPONGE_GROUND:
                return "Éponge de terrain";
            default:
                return "";
        }
    }

    public int getWeight() {
        return weight;
    }

    public MedecineType getMedecineType() {
        return medecineType;
    }

    @Override
    public String use(Monster monster, Battle battle) {
        switch (medecineType) {
            case BURN_HEAL:
                if (monster.getStatus() instanceof BurnedStatus) {
                    monster.setStatus(new NormalStatus());
                    return monster.getName() + " a été guéri du statut Brûlé !";
                }
                break;
            case PARALYZE_HEAL:
                if (monster.getStatus() instanceof ParalyzedStatus) {
                    monster.setStatus(new NormalStatus());
                    return monster.getName() + " a été guéri du statut Paralysé !";
                }
                break;
            case POISON_HEAL: 
                if (monster.getStatus() instanceof PoisonedStatus) {
                    monster.setStatus(new NormalStatus());
                    return monster.getName() + " a été guéri du statut Empoisonné !";
                }
                break;
            case SPONGE_GROUND:
                if (battle.getGround() instanceof FloodedGround) {
                    battle.setGround(new NormalGround());
                    return "Le terrain a été asséché et est redevenu Normal !";
                }
            default:
                break;
        }
        return null;
    }
    
    
}
