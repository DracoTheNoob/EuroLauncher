package fr.dtn.launcher.ui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.dtn.launcher.Launcher;
import fr.dtn.launcher.ui.PanelManager;
import fr.dtn.launcher.ui.panel.FxPanel;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class TopBar extends FxPanel{
    private GridPane bar;

    public TopBar() {
        super();
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);

        this.bar = super.layout;
        super.layout.setStyle("-fx-background-color: rgb(35, 40, 40);");

        ImageView icon = new ImageView();
        icon.setImage(new Image("icon.png"));
        icon.setPreserveRatio(true);
        icon.setFitHeight(30);
        icon.setTranslateX(10);

        setLeft(icon);
        super.layout.getChildren().add(icon);

        Label title = new Label("EuroLauncher");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 30));
        title.setStyle("-fx-text-fill: white;");

        setHorizontalCenter(title);
        super.layout.getChildren().add(title);

        GridPane button = new GridPane();
        button.setMinWidth(120);
        button.setMaxWidth(120);

        setCanTakeAllSize(button);
        setRight(button);
        super.layout.getChildren().add(button);

        FontAwesomeIconView close = new FontAwesomeIconView(FontAwesomeIcon.WINDOW_CLOSE);
        close.setFill(Color.WHITE);
        close.setOpacity(.7);
        close.setSize("20px");
        close.setOnMouseEntered(e -> close.setFill(Color.INDIANRED));
        close.setOnMouseExited(e -> close.setFill(Color.WHITE));
        close.setOnMouseClicked(e -> System.exit(0));
        close.setTranslateX(80);

        FontAwesomeIconView fullscreen = new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MAXIMIZE);
        fullscreen.setFill(Color.WHITE);
        fullscreen.setOpacity(.7);
        fullscreen.setSize("16px");
        fullscreen.setOnMouseEntered(e -> fullscreen.setFill(Color.DARKSEAGREEN));
        fullscreen.setOnMouseExited(e -> fullscreen.setFill(Color.WHITE));
        fullscreen.setOnMouseClicked(e -> this.manager.getStage().setMaximized(!this.manager.getStage().isMaximized()));
        fullscreen.setTranslateX(40);

        FontAwesomeIconView minimize = new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MINIMIZE);
        minimize.setFill(Color.WHITE);
        minimize.setOpacity(.7);
        minimize.setSize("20px");
        minimize.setOnMouseEntered(e -> minimize.setFill(Color.CADETBLUE));
        minimize.setOnMouseExited(e -> minimize.setFill(Color.WHITE));
        minimize.setOnMouseClicked(e -> this.manager.getStage().setIconified(true));
        minimize.setTranslateX(0);
        minimize.setTranslateY(-4);

        setCanTakeAllWidth(close, fullscreen, minimize);
        setCanTakeAllHeight(close, fullscreen, minimize);
        button.getChildren().addAll(close, fullscreen, minimize);
    }

    @Override public void onResize() {}

    @Override public String getName() { return "TopBar"; }
    @Override public String getStylesheetPath() { return null; }
}