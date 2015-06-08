package cz.kamenitxan.FXAuth;

import com.aquafx_project.AquaFx;
import cz.kamenitxan.FXAuth.core.AuthenticatorCLI;
import cz.kamenitxan.sceneswitcher.SceneSwitcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
	private static String secret = null;
	private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
	private List<AuthenticatorCLI> authenticators = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception{

		sceneSwitcher.addScene("a", "a.fxml");
		sceneSwitcher.addScene("b", "b.fxml");

		stage.setTitle("FXAuth");
		stage.setScene(sceneSwitcher.createMainScene(this.getClass()));
		AquaFx.style();
		sceneSwitcher.loadScene("a");

		stage.show();

        System.out.println("Authenticator Started!");
        System.out.println(":----------------------------:--------:");
        System.out.println(":       Code Wait Time       :  Code  :");
        System.out.println(":----------------------------:--------:");
        AuthenticatorCLI main = new AuthenticatorCLI("Facebook", secret);
		authenticators.add(main);

		AuthenticatorCLI google = new AuthenticatorCLI("Google", secret);
		authenticators.add(google);

		stage.setOnCloseRequest(e -> {
			authenticators.stream().forEach(a -> a.stop());
			Platform.exit();
		});

		authenticators.stream().forEach(a -> a.reminder());
    }

    public static void main(String[] args) {
		secret = getSecret(args);
        launch(args);
    }

	private static String getSecret(String[] args) {
		if (args.length > 0  && args[0].indexOf("-secret=") == 0) {
			return args[0].substring(8);
		}
		return "";
	}
}
