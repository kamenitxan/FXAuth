package cz.kamenitxan.FXAuth.gui.scenes;

import cz.kamenitxan.FXAuth.core.AuthenticatorCLI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * Main controller class for the entire layout.
 */
public class AController {
	private static AController aController = null;
	private Map<String, SiteFragment> siteFragments = new HashMap<>();

	@FXML
	private VBox vbox;

	public void updateCode(String code, String site) {
		Label label = siteFragments.get(site).getCodeLabel();
		Platform.runLater(() -> label.setText(code));
	}

	public void updateTimer(double count, String site) {
		final double time = (count * 3.333) / 100;
		//System.out.println(time);
		ProgressBar timer = siteFragments.get(site).getProgressBar();
		Platform.runLater(() -> timer.setProgress(time));
	}

	public void addSite(AuthenticatorCLI authenticatorCLI) {
		SiteFragment siteFragment = new SiteFragment(authenticatorCLI);
		vbox.getChildren().add(siteFragment.getBorderPane());

		siteFragments.put(authenticatorCLI.getName(), siteFragment);
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
