package fr.dtn.launcher.ui.page;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.dtn.launcher.Launcher;
import fr.dtn.launcher.ui.PanelManager;
import fr.dtn.launcher.ui.page.section.SectionPanel;
import fr.dtn.launcher.ui.page.section.HomeSection;
import fr.dtn.launcher.ui.page.section.SettingsSection;
import fr.dtn.launcher.ui.panel.FxPanel;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class MainPage extends FxPanel{
    private final Launcher launcher;
    private final Saver saver;
    private SectionPanel section;

    private final GridPane side = new GridPane(), content = new GridPane(), background = new GridPane();
    private Node activeLink = null;
    private final Button home = new Button("Accueil"), settings = new Button("Paramètres");

    public MainPage(Launcher launcher){
        this.launcher = launcher;
        this.saver = launcher.getSaver();
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);

        this.layout.getStyleClass().add("app-layout");
        setCanTakeAllSize(this.layout);

        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setHalignment(HPos.LEFT);
        constraints.setMinWidth(350);
        constraints.setMaxWidth(350);
        this.layout.getColumnConstraints().addAll(constraints, new ColumnConstraints());

        this.side.getStyleClass().add("sidemenu");
        setLeft(side);
        setHorizontalCenter(side);
        setVerticalCenter(side);
        this.layout.add(side, 0, 0);

        setCanTakeAllSize(background);
        background.getStyleClass().add("bg-image");
        background.resize(manager.getStage().getMaxWidth(), manager.getStage().getHeight());
        this.layout.add(background, 1, 0);

        content.getStyleClass().add("nav-content");
        setLeft(content);
        setHorizontalCenter(content);
        setVerticalCenter(content);
        this.layout.add(content, 1, 0);

        home.getStyleClass().add("sidemenu-nav-btn");
        home.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.HOME));
        setCanTakeAllSize(home);
        setTop(home);
        home.setTranslateY(90);
        home.setOnMouseClicked(e -> setPage(new HomeSection(launcher), home));

        settings.getStyleClass().add("sidemenu-nav-btn");
        settings.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.GEARS));
        setCanTakeAllSize(settings);
        setTop(settings);
        settings.setTranslateY(130);
        settings.setOnMouseClicked(e -> setPage(new SettingsSection(launcher), settings));

        GridPane user = new GridPane();

        setCanTakeAllWidth(user);
        user.setMaxHeight(120);
        user.setMinHeight(120);
        user.getStyleClass().add("user-pane");
        setBottom(user);

        String avatarURL = "https://minotar.net/avatar/" + launcher.getProfile().getId();
        ImageView avatar = new ImageView(new Image(avatarURL));
        avatar.setPreserveRatio(true);
        avatar.setFitHeight(50);
        setVerticalCenter(avatar);
        setCanTakeAllSize(avatar);
        setLeft(avatar);
        setTop(avatar);
        avatar.setTranslateY(15);
        avatar.setTranslateX(15);

        Label username = new Label(launcher.getProfile().getName());
        username.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 25));
        setCanTakeAllSize(username);
        setVerticalCenter(username);
        setLeft(username);
        setTop(username);
        username.setTranslateY(25);
        username.getStyleClass().add("username-label");
        username.setTranslateX(75);
        setCanTakeAllWidth(username);

        Button logout = new Button("Se déconnecter");
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.SIGN_OUT);
        icon.getStyleClass().add("logout-icon");
        setCanTakeAllSize(logout);
        setVerticalCenter(logout);
        setBottom(logout);
        logout.getStyleClass().add("logout-btn");
        logout.setGraphic(icon);
        logout.setTranslateX(15);
        logout.setTranslateY(-5);
        logout.setOnMouseClicked(e -> {
            saver.remove("refreshToken");
            saver.remove("accessToken");
            saver.save();

            launcher.setProfile(null);
            manager.showPanel(new LoginPage(launcher));
        });

        user.getChildren().addAll(avatar, username, logout);
        side.getChildren().addAll(home, settings, user);
    }

    @Override public void onResize() {
        background.resize(manager.getStage().getMaxWidth(), manager.getStage().getHeight());

        if(section instanceof HomeSection)
            section.onResize();
    }

    @Override
    public void onShow(){
        super.onShow();

        setPage(new HomeSection(launcher), home);
    }

    private void setPage(SectionPanel section, Node button){
        if(activeLink != null)
            activeLink.getStyleClass().remove("active");

        activeLink = button;
        activeLink.getStyleClass().add("active");

        this.content.getChildren().clear();

        if(section != null){
            this.content.getChildren().add(section.getLayout());

            if(section.getStylesheetPath() != null){
                this.manager.getStage().getScene().getStylesheets().clear();
                this.manager.getStage().getScene().getStylesheets().addAll(
                        this.getStylesheetPath(),
                        section.getStylesheetPath()
                );
            }

            section.init(this.manager);
            section.onShow();
        }

        this.section = section;
    }

    @Override public String getName() { return getClass().getSimpleName(); }
    @Override public String getStylesheetPath() { return "css/main.css"; }
}