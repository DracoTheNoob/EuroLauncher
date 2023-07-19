package fr.dtn.launcher.ui.panel;

import fr.dtn.launcher.Launcher;
import fr.dtn.launcher.ui.PanelManager;
import fr.flowarg.flowlogger.ILogger;
import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public abstract class FxPanel implements Panel, Movable, TakePlace{
    protected GridPane layout = new GridPane();
    protected PanelManager manager;

    public FxPanel(){}

    @Override
    public void init(PanelManager manager) {
        this.manager = manager;
        setCanTakeAllSize(this.layout);
    }

    @Override public final GridPane getLayout() {
        return layout;
    }

    @Override public void onShow() {
        FadeTransition fade = new FadeTransition(Duration.seconds(1), this.layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setAutoReverse(true);
        fade.play();
    }

    public abstract void onResize();
    @Override public abstract String getName();
    @Override public String getStylesheetPath() { return null; }

    @Override public final void setLeft(Node node) { GridPane.setHalignment(node, HPos.LEFT); }
    @Override public final void setRight(Node node) { GridPane.setHalignment(node, HPos.RIGHT); }
    @Override public final void setTop(Node node) { GridPane.setValignment(node, VPos.TOP); }
    @Override public final void setBottom(Node node) { GridPane.setValignment(node, VPos.BOTTOM); }

    @Override public final void setBaseline(Node node) { GridPane.setValignment(node, VPos.BASELINE); }
    @Override public final void setHorizontalCenter(Node node) { GridPane.setHalignment(node, HPos.CENTER); }
    @Override public final void setVerticalCenter(Node node) { GridPane.setValignment(node, VPos.CENTER); }
}