package cz.kamenitxan.sceneswitcher.demo.gui.scenes;

import cz.kamenitxan.sceneswitcher.SceneSwitcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Main controller class for the entire layout.
 */
public class AController {
	private static AController aController = null;

	@FXML
	private Button switchButton;

	@FXML
	private Label labe;

	@FXML
	private void switchscene() {
		SceneSwitcher.getInstance().loadScene("b", "cs");
	}

	private void setCode(String newCode) {
		labe.setText(newCode);
	}

	public void updateCode(String code) {
		if(Platform.isFxApplicationThread()) {
			setCode(code);
		} else {
			Platform.runLater(() -> setCode(code));
		}
	}

	public AController() {
		aController = this;
	}

	public static AController getInstalnce() {
		return aController;
	}
}
