package fr.dtn.launcher.ui.page.section;

import fr.dtn.launcher.ui.panel.FxPanel;
import javafx.animation.FadeTransition;

public abstract class SectionPanel extends FxPanel{
    @Override
    public void onShow() {
        FadeTransition transition = new FadeTransition();
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setAutoReverse(true);
        transition.play();
    }
}