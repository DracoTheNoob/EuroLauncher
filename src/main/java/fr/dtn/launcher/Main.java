package fr.dtn.launcher;

import fr.dtn.launcher.utils.Path;
import javafx.application.Application;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Path path = new Path();

        try{
            Class.forName("javafx.application.Application");
            Application.launch(Launcher.class, args);
        }catch(ClassNotFoundException e){
            JOptionPane.showMessageDialog(
                    null,
                    "Impossible de charger le Launcher : JavaFX non importé."
                            + "\nMerci de rapporter ce problème à un administrateur.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE)
            ;

            return;
        }

        System.out.println(path.get("").getPath());
    }
}