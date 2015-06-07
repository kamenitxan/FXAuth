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

	public void updateCode(String code) {
		if(Platform.isFxApplicationThread()) {
			this.code.setText(code);
		} else {
			Platform.runLater(() -> this.code.setText(code));
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

	public void setTitle(String title) {
		Platform.runLater(() -> this.title.setText(title));
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
