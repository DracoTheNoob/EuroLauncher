package fr.dtn.launcher.ui.panel;

import fr.dtn.launcher.ui.PanelManager;
import javafx.scene.layout.GridPane;

public interface Panel {
    void init(PanelManager manager);
    GridPane getLayout();
    void onShow();
    String getName();
    String getStylesheetPath();
}