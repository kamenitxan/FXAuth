package cz.kamenitxan.FXAuth.gui.scenes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * Main controller class for the entire layout.
 */
public class AController {
	private static AController aController = null;

	@FXML
	private Label title;

	@FXML
	public Label code;

	@FXML
	private ProgressBar timer;

	private void setCode(String newCode) {
		code.setText(newCode);
	}

	public void updateCode(String code) {
		if(Platform.isFxApplicationThread()) {
			setCode(code);
		} else {
			Platform.runLater(() -> setCode(code));
		}
	}

	public void updateTimer(double count) {
		final double time = (count * 3.333) / 100;
		//System.out.println(time);
		if(Platform.isFxApplicationThread()) {
			timer.setProgress(count);
		} else {
			Platform.runLater(() -> timer.setProgress(time));
		}
	}

	public AController() {
		aController = this;
	}

	public static AController getInstalnce() {
		if (aController == null) {
			aController = new AController();
		}
		return aController;
	}
}
