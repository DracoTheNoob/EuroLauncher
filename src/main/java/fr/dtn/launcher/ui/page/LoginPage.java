package fr.dtn.launcher.ui.page;

import fr.dtn.launcher.Launcher;
import fr.dtn.launcher.ui.PanelManager;
import fr.dtn.launcher.ui.panel.FxPanel;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class LoginPage extends FxPanel{
    private final GridPane card = new GridPane();
    private final Launcher launcher;
    private final Saver saver;

    private final TextField user = new TextField();
    private final PasswordField password = new PasswordField();
    private final Label userError = new Label();
    private final Label passwordError = new Label();
    private final Button login = new Button("Connexion");

    public LoginPage(Launcher launcher) {
        super();

        this.launcher = launcher;
        this.saver = launcher.getSaver();
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);

        this.layout.getStyleClass().add("login-layout");

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.LEFT);
        columnConstraints.setMinWidth(350);
        columnConstraints.setMaxWidth(350);
        this.layout.getColumnConstraints().addAll(columnConstraints, new ColumnConstraints());
        this.layout.add(card, 0, 0);

        GridPane bgImage = new GridPane();
        setCanTakeAllSize(bgImage);
        bgImage.getStyleClass().add("bg-image");
        this.layout.add(bgImage, 1, 0);

        setCanTakeAllSize(this.layout);
        card.getStyleClass().add("login-card");
        setLeft(card);
        setHorizontalCenter(card);
        setVerticalCenter(card);

        Label title = new Label("Compte Microsoft");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 30f));
        title.getStyleClass().add("login-title");
        setHorizontalCenter(title);
        setCanTakeAllSize(title);
        setTop(title);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(30d);
        card.getChildren().add(title);

        setCanTakeAllSize(user);
        setVerticalCenter(user);
        setHorizontalCenter(user);
        user.setPromptText("Adresse E-Mail");
        user.setMaxWidth(300);
        user.setTranslateY(-70d);
        user.getStyleClass().add("login-input");
        user.textProperty().addListener((_a, oldValue, newValue) -> this.updateLoginBtnState(user, userError));

        setCanTakeAllSize(userError);
        setVerticalCenter(userError);
        setHorizontalCenter(userError);
        userError.getStyleClass().add("login-error");
        userError.setTranslateY(-45d);
        userError.setMaxWidth(280);
        userError.setTextAlignment(TextAlignment.LEFT);

        setCanTakeAllSize(password);
        setVerticalCenter(password);
        setHorizontalCenter(password);
        password.setPromptText("Mot de passe");
        password.setMaxWidth(300);
        password.setTranslateY(-15d);
        password.getStyleClass().add("login-input");
        password.textProperty().addListener((_a, oldValue, newValue) -> this.updateLoginBtnState(password, passwordError));

        setCanTakeAllSize(passwordError);
        setVerticalCenter(passwordError);
        setHorizontalCenter(passwordError);
        passwordError.getStyleClass().add("login-error");
        passwordError.setTranslateY(10d);
        passwordError.setMaxWidth(280);
        passwordError.setTextAlignment(TextAlignment.LEFT);

        setCanTakeAllSize(login);
        setVerticalCenter(login);
        setHorizontalCenter(login);
        login.setDisable(true);
        login.setMaxWidth(300);
        login.setTranslateY(40d);
        login.getStyleClass().add("login-log-btn");
        login.setOnMouseClicked(e -> this.auth(user.getText(), password.getText()));

        card.getChildren().addAll(user, userError, password, passwordError, login);
    }

    @Override public void onResize(){}

    private void updateLoginBtnState(TextField field, Label label){
        if(field.getText().length() == 0){
            label.setText("Le champs ne peut pas être vide");
        }else{
            label.setText("");
        }

        login.setDisable(user.getText().length() == 0 || password.getText().length() == 0);
    }

    private void auth(String user, String password){
        MicrosoftAuthenticator auth = new MicrosoftAuthenticator();

        try{
            MicrosoftAuthResult result = auth.loginWithCredentials(user, password);

            saver.set("refreshToken", result.getRefreshToken());
            saver.set("accessToken", result.getAccessToken());

            launcher.setProfile(result.getProfile());
            launcher.getPanelManager().showPanel(new MainPage(launcher));
        }catch (MicrosoftAuthenticationException e) {
            passwordError.setText("E-Mail ou Mot de passe invalide.");
        }
    }

    @Override public String getName() { return getClass().getSimpleName(); }
    @Override public String getStylesheetPath() { return "css/login.css"; }
}