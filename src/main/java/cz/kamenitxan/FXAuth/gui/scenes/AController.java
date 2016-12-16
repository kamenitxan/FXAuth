package cz.kamenitxan.FXAuth.gui.scenes;

import cz.kamenitxan.FXAuth.Main;
import cz.kamenitxan.FXAuth.core.AuthenticatorCLI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static cz.kamenitxan.FXAuth.gui.scenes.Settings.openSettings;

/**
 * Main controller class for the entire layout.
 */
public class AController implements Initializable{
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
		Main.stage.setHeight(Main.stage.getHeight() +  120);
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MenuBar menuBar = new MenuBar();
		if (Main.isOSX) {
			menuBar.useSystemMenuBarProperty().set(true);
		} else {
			Main.stage.setHeight(40);
		}

		final Menu menu1 = new Menu("Options");
		final MenuItem sitesOption = new MenuItem("Sites");
		sitesOption.setOnAction(a -> openSettings());
		menu1.getItems().add(sitesOption);
		menuBar.getMenus().add(menu1);

		vbox.getChildren().add(menuBar);

	}



}
