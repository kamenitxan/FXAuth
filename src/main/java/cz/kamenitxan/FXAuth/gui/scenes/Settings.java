package cz.kamenitxan.FXAuth.gui.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by tomaspavel on 16.12.16.
 */
public class Settings {

	@FXML
	private Button newSiteBtn;

	public void newSiteAction() {
		System.out.println("te");
	}

	public static void openSettings() {
		FXMLLoader fxmlLoader = new FXMLLoader(Settings.class.getClassLoader().getResource("settings.fxml"));
		Parent root1 = null;
		try {
			root1 = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage stage = new Stage();
		stage.setTitle("Settings");
		stage.setScene(new Scene(root1));
		stage.show();
	}
}
