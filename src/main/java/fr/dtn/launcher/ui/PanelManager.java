package fr.dtn.launcher.ui;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import fr.dtn.launcher.Launcher;
import fr.dtn.launcher.ui.panel.FxPanel;
import fr.flowarg.flowcompat.Platform;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PanelManager {
    private final Launcher launcher;
    private final Stage stage;
    private FxPanel panel;
    private GridPane layout;
    private final TopBar topBar;
    private final GridPane content = new GridPane();

    public PanelManager(Launcher launcher, Stage stage) {
        this.launcher = launcher;
        this.stage = stage;
        this.topBar = new TopBar();
    }

    public void init(){
        this.stage.setMinWidth(1280);
        this.stage.setMinHeight(800);
        this.stage.setMaxWidth(1920);
        this.stage.setMaxHeight(1080);
        this.stage.setWidth(this.stage.getMinWidth());
        this.stage.setHeight(this.stage.getMinHeight());
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.centerOnScreen();

        this.layout = new GridPane();

        if(Platform.isOnLinux()){
            Scene scene = new Scene(this.layout);
            this.stage.setScene(scene);
        }else{
            BorderlessScene scene = new BorderlessScene(this.stage, StageStyle.UNDECORATED, this.layout);
            scene.setResizable(true);
            scene.setMoveControl(this.topBar.getLayout());
            scene.removeDefaultCSS();

            this.stage.setScene(scene);

            RowConstraints constraints = new RowConstraints();
            constraints.setValignment(VPos.TOP);
            constraints.setMinHeight(40);
            constraints.setMaxHeight(40);

            this.layout.getRowConstraints().addAll(constraints, new RowConstraints());
            this.layout.add(this.topBar.getLayout(), 0, 0);
            this.topBar.init(this);
        }

        this.layout.add(this.content, 0, 1);
        GridPane.setVgrow(this.content, Priority.ALWAYS);
        GridPane.setHgrow(this.content, Priority.ALWAYS);

        this.stage.widthProperty().addListener((obs, oldValue, newValue) -> panel.onResize());
        this.stage.heightProperty().addListener((obs, oldValue, newValue) -> panel.onResize());

        this.stage.show();
    }

    public void showPanel(FxPanel panel){
        this.content.getChildren().clear();
        this.content.getChildren().add(panel.getLayout());

        if(panel.getStylesheetPath() != null){
            this.stage.getScene().getStylesheets().clear();
            this.stage.getScene().getStylesheets().add(panel.getStylesheetPath());
        }

        panel.init(this);
        panel.onShow();

        this.panel = panel;
    }

    public Launcher getLauncher() { return launcher; }
    public Stage getStage() { return stage; }
}