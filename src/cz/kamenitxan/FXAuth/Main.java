package cz.kamenitxan.FXAuth;

import cz.kamenitxan.FXAuth.core.AuthenticatorCLI;
import cz.kamenitxan.sceneswitcher.SceneSwitcher;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	private static String secret = null;
	private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();

    @Override
    public void start(Stage stage) throws Exception{
		sceneSwitcher.addScene("a", "a.fxml");
		sceneSwitcher.addScene("b", "b.fxml");

		stage.setTitle("FXAuth");
		stage.setScene(sceneSwitcher.createMainScene(this.getClass()));


		sceneSwitcher.loadScene("a");
		stage.show();


        System.out.println("\nAuthenticator Started!");
        System.out.println(":----------------------------:--------:");
        System.out.println(":       Code Wait Time       :  Code  :");
        System.out.println(":----------------------------:--------:");
        AuthenticatorCLI main = new AuthenticatorCLI();


		main.reminder(secret);

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
