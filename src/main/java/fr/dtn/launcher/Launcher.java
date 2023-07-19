package fr.dtn.launcher;

import fr.dtn.launcher.ui.PanelManager;
import fr.dtn.launcher.ui.page.LoginPage;
import fr.dtn.launcher.ui.page.MainPage;
import fr.dtn.launcher.utils.Path;
import fr.dtn.launcher.utils.StepInfo;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.CurseFileInfo;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.download.json.OptiFineInfo;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.ModLoaderVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Launcher extends Application{
    private final Path path;
    private final Saver saver;
    private MinecraftProfile profile;
    private PanelManager panelManager;

    private ProgressBar downloadBar;
    private Label stepLabel, fileLabel;

    private final List<OptionalMod> optionalMods;

    public Launcher(){
        this.path = new Path();
        File directory = path.get("");

        if(!directory.exists() && !directory.mkdir())
            throw new RuntimeException("Unable to make launcher directory");

        this.saver = new Saver(path.get("config.properties").toPath());
        this.saver.load();

        initConfiguration();

        this.optionalMods = new ArrayList<>();
        initOptionalMods();
    }

    private void initConfiguration(){
        if(saver.get("maxRam") == null){
            SystemInfo systemInfo = new SystemInfo();
            GlobalMemory memory = systemInfo.getHardware().getMemory();

            saver.set("maxRam", String.valueOf((int)(Math.ceil(memory.getTotal() / Math.pow(1024, 2)) * .25/1024)));
        }

        for(OptionalMod mod : OptionalMod.values())
            if(saver.get(mod.name()) == null)
                saver.set(mod.name(), "false");
    }

    private void initOptionalMods(){
        for(OptionalMod mod : OptionalMod.values())
            if(saver.get(mod.name()).equals("true"))
                enableOptionalMod(mod);
    }

    public void setOptionalModEnabled(OptionalMod mod, boolean enabled){
        if(enabled)
            enableOptionalMod(mod);
        else
            disableOptionalMod(mod);
    }

    public void enableOptionalMod(OptionalMod mod){
        this.optionalMods.remove(mod);
        this.optionalMods.add(mod);
        this.saver.set(mod.name(), "true");
    }

    public void disableOptionalMod(OptionalMod mod){
        this.optionalMods.remove(mod);
        this.saver.set(mod.name(), "true");
        if(!new File(path.get("mods"), mod.getFileName()).delete())
            System.err.println("fail");
    }

    @Override
    public void start(Stage stage){
        this.panelManager = new PanelManager(this, stage);
        this.panelManager.init();

        if(isLoggedIn()){
            this.panelManager.showPanel(new MainPage(this));
        }else{
            this.panelManager.showPanel(new LoginPage(this));
        }
    }

    public boolean isLoggedIn(){
        String refreshToken = saver.get("refreshToken");

        if(refreshToken != null){
            try{
                MicrosoftAuthenticator auth = new MicrosoftAuthenticator();
                MicrosoftAuthResult result = auth.loginWithRefreshToken(refreshToken);

                saver.set("refreshToken", result.getRefreshToken());
                saver.set("accessToken", result.getAccessToken());

                this.profile = result.getProfile();
                return true;
            }catch(MicrosoftAuthenticationException e){
                saver.remove("refreshToken");
                saver.remove("accessToken");
                return false;
            }
        }

        return false;
    }

    public void updateHomeElements(ProgressBar bar, Label stepLabel, Label fileLabel){
        this.downloadBar = bar;
        this.stepLabel = stepLabel;
        this.fileLabel = fileLabel;
    }

    public void update(){
        setProgress(0, 0);

        IProgressCallback callback = new IProgressCallback() {
            private final DecimalFormat decimal = new DecimalFormat("#.#");
            private String stepText = "";
            private String percentText = "0.0%";

            @Override public void step(Step step) {
                Platform.runLater(() -> {
                    stepText = StepInfo.valueOf(step.name()).getDetails();
                    setStatus(String.format("%s (%s)", stepText, percentText));
                });
            }

            @Override public void update(DownloadList.DownloadInfo info) {
                Platform.runLater(() -> {
                    percentText = decimal.format(info.getDownloadedBytes() * 100.0 / info.getTotalToDownloadBytes()) + "%";
                    setStatus(String.format("%s (%s)", stepText, percentText));
                    setProgress(info.getDownloadedBytes(), info.getTotalToDownloadBytes());
                });
            }

            @Override public void onFileDownloaded(java.nio.file.Path filePath) {
                Platform.runLater(() -> {
                    String p = filePath.toString();
                    fileLabel.setText(p.replace(path.get("").getAbsolutePath(), "").replace("\\", "/"));
                });
            }
        };

        try{
            VanillaVersion vanilla = null;

            try{
                vanilla = new VanillaVersion.VanillaVersionBuilder()
                        .withName(Game.VERSION)
                        .build();
            }catch(Exception ignored){}

            List<CurseFileInfo> mods = CurseFileInfo.getFilesFromJson(Game.MODS_LIST);

            for(OptionalMod mod : optionalMods)
                mods.add(new CurseFileInfo(mod.getModId(), mod.getFileId()));

            final AbstractForgeVersion forge = new ForgeVersionBuilder(Game.FORGE_VERSION_TYPE)
                    .withForgeVersion(Game.FORGE_VERSION)
                    .withCurseMods(mods)
                    .withOptiFine(new OptiFineInfo(Game.OPTIFINE_VERSION, false))
                    .build();

            final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
                    .withVanillaVersion(vanilla)
                    .withModLoaderVersion(forge)
                    .withProgressCallback(callback)
                    .build();

            updater.update(this.getPath().get("").toPath());
            this.play(updater.getVanillaVersion().getName());
        }catch(Exception e){
            Platform.runLater(() -> panelManager.getStage().show());
        }
    }

    public void play(String version){
        GameInfos info = new GameInfos(
                Game.SERVER_NAME,
                true,
                new GameVersion(version, Game.OLL_GAME_TYPE.setNFVD(Game.OLL_FORGE_DISCRIMINATOR)),
                new GameTweak[]{}
        );

        try{
            AuthInfos auth = new AuthInfos(profile.getName(), saver.get("accessToken"), profile.getId());
            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(info, GameFolder.FLOW_UPDATER, auth);
            profile.getVmArgs().add(getRamArgs());

            ExternalLauncher launcher = new ExternalLauncher(profile);
            Platform.runLater(() -> panelManager.getStage().hide());

            Process p = launcher.launch();
            Platform.runLater(() -> {
                try{
                    p.waitFor();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setStatus(String status){ this.stepLabel.setText(status); }
    private void setProgress(double current, double max){ this.downloadBar.setProgress(current / ((max == 0) ? 1 : max)); }

    public String getRamArgs(){
        int ram = 2;

        if(saver.get("maxRam") != null)
            ram = Integer.parseInt(saver.get("maxRam"));
        else
            saver.set("maxRam", String.valueOf(ram));

        return "-Xmx" + ram + "G";
    }

    public Saver getSaver() { return saver; }
    public MinecraftProfile getProfile() { return profile; }
    public void setProfile(MinecraftProfile profile) { this.profile = profile; }
    public PanelManager getPanelManager() { return panelManager; }
    public Path getPath(){ return path; }
}