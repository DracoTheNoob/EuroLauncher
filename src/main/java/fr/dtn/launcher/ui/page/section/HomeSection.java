package fr.dtn.launcher.ui.page.section;

import fr.dtn.launcher.Launcher;
import fr.dtn.launcher.ui.PanelManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class HomeSection extends SectionPanel {
    private final Launcher launcher;

    private final GridPane boxPane = new GridPane();
    private final ProgressBar progressBar = new ProgressBar();
    private final Button play = new Button("JOUER");
    private final Label stepLabel = new Label();
    private final Label fileLabel = new Label();

    public HomeSection(Launcher launcher){
        this.launcher = launcher;
    }

    @Override public void init(PanelManager manager) {
        super.init(manager);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        rowConstraints.setMinHeight(75);
        rowConstraints.setMaxHeight(75);
        this.layout.getRowConstraints().addAll(rowConstraints, new RowConstraints());
        boxPane.getStyleClass().add("box-pane");
        setCanTakeAllSize(boxPane);
        boxPane.setPadding(new Insets(20));
        this.layout.add(boxPane, 0, 0);
        this.layout.getStyleClass().add("home-layout");

        progressBar.getStyleClass().add("download-progress");
        stepLabel.getStyleClass().add("download-status");
        fileLabel.getStyleClass().add("download-status");

        progressBar.setTranslateY(-15);
        setHorizontalCenter(progressBar);
        setCanTakeAllWidth(progressBar);

        stepLabel.setTranslateY(5);
        setHorizontalCenter(stepLabel);
        setCanTakeAllSize(stepLabel);

        fileLabel.setTranslateY(20);
        setHorizontalCenter(fileLabel);
        setCanTakeAllSize(fileLabel);

        this.showPlayButton();
        launcher.updateHomeElements(progressBar, stepLabel, fileLabel);
    }

    private void showPlayButton(){
        boxPane.getChildren().clear();
        setCanTakeAllSize(play);
        setHorizontalCenter(play);
        setVerticalCenter(play);
        play.setMinWidth(200);
        play.setTranslateY(manager.getStage().getHeight() - 130);
        play.getStyleClass().add("play-btn");
        play.setOnMouseClicked(e -> {
            boxPane.getChildren().clear();
            boxPane.getChildren().addAll(progressBar, stepLabel, fileLabel);

            Platform.runLater(() -> new Thread(launcher::update).start());
        });

        boxPane.getChildren().add(play);
    }

    @Override public void onResize(){ play.setTranslateY(manager.getStage().getHeight() - 130); }
    @Override public String getName() { return null; }
    @Override public String getStylesheetPath() { return "css/section/home.css"; }
}