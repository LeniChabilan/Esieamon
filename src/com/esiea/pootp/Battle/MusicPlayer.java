package com.esiea.pootp.Battle;

import java.io.File;

/**
 * Lecteur de musique minimaliste pour les combats.
 * <p>
 * Cette classe lance la lecture d'un fichier audio dans un thread dédié et
 * s'appuie sur des lecteurs système disponibles (ffplay, paplay, mpv, aplay).
 * La lecture est bouclée tant que {@link #stop()} n'est pas appelée.
 * <p>
 * Conçu pour un usage simple: {@link #play(File)} pour démarrer et {@link #stop()} pour arrêter.
 * Les erreurs sont loggées en console avec le préfixe "[AUDIO]".
 */
public class MusicPlayer {
    
    private Process audioProcess;
    private Thread audioThread;
    private volatile boolean audioPlaying = false;
    
    /**
     * Démarre la lecture en boucle d'un fichier audio dans un thread en arrière-plan.
     * <p>
     * Si une lecture est déjà en cours, l'appel est ignoré.
     * Le thread de lecture est marqué en "daemon" afin de ne pas empêcher l'arrêt de l'application.
     *
     * @param musicFile fichier audio à lire (chemin absolu recommandé)
     */
    public void play(File musicFile) {
        if (audioPlaying) {
            return; // Déjà en cours de lecture
        }
        
        try {
            audioPlaying = true;
            audioThread = new Thread(() -> playAudioLoop(musicFile));
            audioThread.setDaemon(true);
            audioThread.start();
            System.out.println("[AUDIO] Musique lancée");
        } catch (Exception e) {
            System.out.println("[AUDIO] Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Arrête proprement la lecture en cours.
     * <p>
     * Cette méthode:
     * <ul>
     *   <li>Indique au thread de lecture de s'arrêter,</li>
     *   <li>Détruit le processus système associé au lecteur, avec secours forcé au besoin,</li>
     *   <li>Tente de tuer d'éventuels lecteurs restants via <code>killall</code>,</li>
     *   <li>Attend la fin du thread (maximum ~1 seconde).</li>
     * </ul>
     */
    public void stop() {
        audioPlaying = false;
        
        // Arrêter le processus système
        if (audioProcess != null) {
            try {
                audioProcess.destroy();
                if (!audioProcess.waitFor(500, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    audioProcess.destroyForcibly();
                }
                
                // Tuer aussi avec kill -9 au cas où
                try {
                    Runtime.getRuntime().exec(new String[]{"killall", "-9", "ffplay"}).waitFor();
                    Runtime.getRuntime().exec(new String[]{"killall", "-9", "paplay"}).waitFor();
                    Runtime.getRuntime().exec(new String[]{"killall", "-9", "mpv"}).waitFor();
                    Runtime.getRuntime().exec(new String[]{"killall", "-9", "aplay"}).waitFor();
                } catch (Exception ignored) {}
            } catch (Exception e) {
                // Ignoré
            }
            audioProcess = null;
        }
        
        // Attendre que le thread se termine
        if (audioThread != null && audioThread.isAlive()) {
            try {
                audioThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            audioThread = null;
        }
        
        System.out.println("[AUDIO] Musique arrêtée");
    }
    
    /**
     * Boucle de lecture tant que l'état {@link #audioPlaying} reste à vrai.
     *
     * @param musicFile fichier audio à lire
     */
    private void playAudioLoop(File musicFile) {
        try {
            while (audioPlaying) {
                playWithSystemCommand(musicFile);
                if (!audioPlaying) break;
            }
        } catch (Exception e) {
            System.out.println("[AUDIO] Erreur lecture: " + e.getMessage());
        }
    }
    
    /**
     * Tente de lire le fichier avec différents lecteurs système.
     * <p>
     * Ordre d'essai: ffplay, paplay, mpv, aplay. La méthode bloque tant que le
     * processus du lecteur est actif et que la lecture n'est pas interrompue.
     *
     * @param musicFile fichier audio à lire
     * @return {@code true} si un lecteur a pu être lancé, sinon {@code false}
     */
    private boolean playWithSystemCommand(File musicFile) {
        String[] players = {"ffplay", "paplay", "mpv", "aplay"};
        String[] commands = {
            "ffplay -nodisp -autoexit -loglevel quiet " + musicFile.getAbsolutePath(),
            "paplay " + musicFile.getAbsolutePath(),
            "mpv --no-video --really-quiet " + musicFile.getAbsolutePath(),
            "aplay " + musicFile.getAbsolutePath()
        };
        
        for (int i = 0; i < commands.length; i++) {
            try {
                audioProcess = Runtime.getRuntime().exec(new String[]{"sh", "-c", commands[i]});
                System.out.println("[AUDIO] Exécution: " + players[i]);
                
                while (audioProcess.isAlive() && audioPlaying) {
                    Thread.sleep(100);
                }
                
                if (!audioPlaying) {
                    audioProcess.destroyForcibly();
                    audioProcess.waitFor();
                }
                audioProcess = null;
                return true;
            } catch (Exception e) {
                System.out.println("[AUDIO] " + players[i] + " non disponible");
                if (audioProcess != null) {
                    try {
                        audioProcess.destroyForcibly();
                    } catch (Exception ignored) {}
                    audioProcess = null;
                }
            }
        }
        
        System.out.println("[AUDIO] Aucun lecteur audio disponible");
        return false;
    }
}
