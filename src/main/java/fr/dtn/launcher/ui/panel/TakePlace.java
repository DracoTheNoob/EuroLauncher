package fr.dtn.launcher.ui.panel;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public interface TakePlace {
    default void setCanTakeAllSize(Node node){
        GridPane.setHgrow(node, Priority.ALWAYS);
        GridPane.setVgrow(node, Priority.ALWAYS);
    }

    default void setCanTakeAllWidth(Node... nodes){
        for(Node node : nodes)
            GridPane.setHgrow(node, Priority.ALWAYS);
    }

    default void setCanTakeAllHeight(Node... nodes){
        for(Node node : nodes)
            GridPane.setVgrow(node, Priority.ALWAYS);
    }
}