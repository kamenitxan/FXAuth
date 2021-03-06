package cz.kamenitxan.FXAuth;

import com.aquafx_project.AquaFx;
import cz.kamenitxan.FXAuth.core.AuthenticatorCLI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
	private static List<String[]> secrets = null;
	private List<AuthenticatorCLI> authenticators = new ArrayList<>();
	public static Stage stage = null;
	public static boolean isOSX = false;

    @Override
    public void start(Stage stage) throws Exception{
		Main.stage = stage;
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("a.fxml"));
		stage.setScene(new Scene(root, 300, 275));
		stage.setMaxWidth(300);
		stage.setMinWidth(300);
		stage.show();

		stage.setTitle("FXAuth");
		if(System.getProperty("os.name").equals("Mac OS X")){
			isOSX = true;
			AquaFx.style();
		}

		stage.show();

        System.out.println("Authenticator Started!");

		for (String[] site : secrets) {
			AuthenticatorCLI authenticator = new AuthenticatorCLI(site[0], site[1]);
			authenticators.add(authenticator);
		}

		stage.setOnCloseRequest(e -> {
			authenticators.forEach(AuthenticatorCLI::stop);
			Platform.exit();
		});

		authenticators.forEach(AuthenticatorCLI::reminder);
    }

    public static void main(String[] args) {
		getSecret(args);
        launch(args);
    }

	private static void getSecret(String[] args) {
		secrets = new ArrayList<>();
		for (String arg : args) {
			String[] couple = arg.split(":");
			couple[0] = couple[0].substring(1);
			secrets.add(couple);
		}
	}
}
