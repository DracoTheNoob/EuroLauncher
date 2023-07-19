package fr.dtn.launcher.ui.panel;

import javafx.scene.Node;

public interface Movable {
    void setLeft(Node node);
    void setRight(Node node);
    void setTop(Node node);
    void setBottom(Node node);
    void setBaseline(Node node);
    void setHorizontalCenter(Node node);
    void setVerticalCenter(Node node);
}