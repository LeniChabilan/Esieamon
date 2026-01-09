package com.esiea.pootp.Battle;

import java.io.File;

public class MusicPlayer {
    
    private Process audioProcess;
    private Thread audioThread;
    private volatile boolean audioPlaying = false;
    
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
