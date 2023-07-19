package fr.dtn.launcher.utils;

public enum StepInfo {
    READ("Lecture du fichier json..."),
    DL_LIBS("Téléchargement des libraries..."),
    DL_ASSETS("Téléchargement des ressources..."),
    EXTRACT_NATIVES("Extraction des natives..."),
    FORGE("Installation de forge..."),
    FABRIC("Installation de fabric..."),
    MODS("Téléchargement des mods..."),
    EXTERNAL_FILES("Téléchargement des fichier externes..."),
    POST_EXECUTIONS("Exécution post-installation..."),
    MOD_LOADER("Installation du mod loader..."),
    INTEGRATION("Intégration des mods..."),
    END("Finit !");

    private final String details;
    StepInfo(String details) { this.details = details; }
    public String getDetails() { return details; }
}