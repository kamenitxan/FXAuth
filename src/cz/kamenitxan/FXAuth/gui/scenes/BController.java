package cz.kamenitxan.sceneswitcher.demo.gui.scenes;

import cz.kamenitxan.sceneswitcher.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Main controller class for the entire layout.
 */
public class BController {
	@FXML
	private Button switchButton;

	@FXML
	private void switchscene() {
		SceneSwitcher.getInstance().loadScene("a", "");
	}

}
