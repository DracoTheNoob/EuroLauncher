package fr.dtn.launcher;

public enum OptionalMod {
    MINIMAP("Xaero's Minimap", "Affiche une minimap et permet l'utilisation de waypoints.", 263420, 4593172, "Xaeros_Minimap_23.5.0_Forge_1.16.5.jar"),
    WORLD_MAP("Xaero's World Map", "Permet l'utilisation d'une map monde, compatible avec Xaero's Minimap.", 317780, 4593207, "XaerosWorldMap_1.30.5_Forge_1.16.5.jar"),
    APPLESKIN("AppleSkin", "Affiche la barre de saturation et le gain de nourriture et de saturation des consommables.", 248787, 3686480, "appleskin-forge-mc1.16.x-2.4.0.jar"),
    NEAT("Neat", "Affiche une barre de vie au dessus de la tête des entités vivantes.", 238372, 3182258, "Neat 1.7-27.jar");

    private final String display, description;
    private final int modId, fileId;
    private final String fileName;

    OptionalMod(String display, String description, int modId, int fileId, String fileName){
        this.display = display;
        this.description = description;
        this.modId = modId;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    public String getDisplay() { return display; }
    public String getDescription() { return description; }
    public int getModId() { return modId; }
    public int getFileId() { return fileId; }
    public String getFileName() { return fileName; }
}