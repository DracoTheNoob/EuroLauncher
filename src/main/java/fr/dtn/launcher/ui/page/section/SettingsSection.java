package fr.dtn.launcher.ui.page.section;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.dtn.launcher.Launcher;
import fr.dtn.launcher.OptionalMod;
import fr.dtn.launcher.ui.PanelManager;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

public class SettingsSection extends SectionPanel {
    private final Launcher launcher;
    private final Saver saver;

    private final GridPane content = new GridPane();

    public SettingsSection(Launcher launcher){
        this.launcher = launcher;
        this.saver = launcher.getSaver();
    }

    @Override public void init(PanelManager manager) {
        super.init(manager);

        this.layout.getStyleClass().add("settings-layout");
        this.layout.setPadding(new Insets(40));
        setCanTakeAllSize(this.layout);

        content.getStyleClass().add("content-pane");
        setCanTakeAllSize(content);

        Label title = new Label("Paramètres");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 25f));
        title.getStyleClass().add("settings-title");
        setLeft(title);
        setCanTakeAllSize(title);
        setTop(title);
        title.setTextAlignment(TextAlignment.LEFT);
        title.setTranslateY(40d);
        title.setTranslateX(25d);

        Label ram = new Label("Mémoire max");
        ram.getStyleClass().add("settings-labels");
        setLeft(ram);
        setCanTakeAllSize(ram);
        setTop(ram);
        ram.setTextAlignment(TextAlignment.LEFT);
        ram.setTranslateX(25d);
        ram.setTranslateY(100d);


        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getStyleClass().add("ram-selector");
        comboBox.onActionProperty().set(a -> {
            // a.
        });

        for(int i = 2; i <= (int)Math.ceil(memory.getTotal() / Math.pow(1024, 3) + 1) / 2; i++)
            comboBox.getItems().add(i + " Go");

        int val = 2;

        if(saver.get("maxRam") != null){
            val = Integer.parseInt(saver.get("maxRam"));
        }else{
            saver.set("maxRam", String.valueOf(val));
            saver.save();
        }

        if(comboBox.getItems().contains(val + " Go"))
            comboBox.setValue(val + " Go");
        else
            comboBox.setValue("2 Go");

        setLeft(comboBox);
        setCanTakeAllSize(comboBox);
        setTop(comboBox);
        comboBox.setTranslateX(35d);
        comboBox.setTranslateY(130d);

        Label mods = new Label("Mods optionnels");
        mods.getStyleClass().add("settings-labels");
        setLeft(mods);
        setCanTakeAllSize(mods);
        setTop(mods);
        mods.setTextAlignment(TextAlignment.LEFT);
        mods.setTranslateX(25d);
        mods.setTranslateY(170);

        content.getChildren().addAll(title, ram, comboBox, mods);

        for(int i = 0; i < OptionalMod.values().length; i++){
            OptionalMod mod = OptionalMod.values()[i];
            String name = mod.name().charAt(0) + mod.name().substring(1).toLowerCase();
            CheckBox checkbox = new CheckBox(name + " : " + mod.getDescription());

            setLeft(checkbox);
            setCanTakeAllSize(checkbox);
            setTop(checkbox);
            checkbox.setTranslateX(35d);
            checkbox.setTranslateY(200d + i * 30);
            checkbox.setTextFill(Color.BLACK);
            checkbox.getStyleClass().add("check-box");
            checkbox.setSelected(saver.get(mod.name()).equals("true"));
            checkbox.onActionProperty().set(e -> launcher.setOptionalModEnabled(mod, checkbox.isSelected()));

            content.getChildren().add(checkbox);
        }

        this.layout.getChildren().add(content);
    }

    @Override public void onResize() {}

    @Override public String getName() { return getClass().getSimpleName(); }
    @Override public String getStylesheetPath() { return "css/section/settings.css"; }
}