package fr.dtn.launcher;

import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.theshark34.openlauncherlib.minecraft.GameType;

public class Game {
    public static final String VERSION = "1.16.5";
    public static final ForgeVersionBuilder.ForgeVersionType FORGE_VERSION_TYPE = ForgeVersionBuilder.ForgeVersionType.NEW;
    public static final String FORGE_VERSION = "1.16.5-36.2.34";
    public static final String OPTIFINE_VERSION = "1.16.5_HD_U_G8";
    public static final String SERVER_NAME = "eurolauncher";

    public static final GameType OLL_GAME_TYPE = GameType.V1_13_HIGHER_FORGE;
    public static final NewForgeVersionDiscriminator OLL_FORGE_DISCRIMINATOR = new NewForgeVersionDiscriminator(
            FORGE_VERSION.split("-")[1],
            VERSION,
            "20210115.111550"
    );

    public static final String MODS_LIST = "http://localhost:3000/file/mods.json";
}