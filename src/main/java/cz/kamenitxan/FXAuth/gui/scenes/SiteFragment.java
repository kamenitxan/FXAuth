package cz.kamenitxan.FXAuth.gui.scenes;


import cz.kamenitxan.FXAuth.core.AuthenticatorCLI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class SiteFragment {
	private String name;
	private BorderPane borderPane;
	private Label nameLabel;
	private Label codeLabel;
	private ProgressBar progressBar;

	public SiteFragment(AuthenticatorCLI authenticatorCLI) {
		name = authenticatorCLI.getName();
		borderPane = new BorderPane();
		nameLabel = new Label() {{setText(authenticatorCLI.getName());}};
		borderPane.setTop(nameLabel);
		codeLabel = new Label();
		codeLabel.setFont(new Font(46));
		borderPane.setCenter(codeLabel);
		progressBar = new ProgressBar() {{
			setPrefWidth(260);
			setPrefHeight(20);
		}};
		borderPane.setBottom(progressBar);

		BorderPane.setAlignment(nameLabel, Pos.CENTER);
		BorderPane.setAlignment(codeLabel, Pos.CENTER);
		BorderPane.setAlignment(progressBar, Pos.CENTER);
		BorderPane.setMargin(nameLabel, new Insets(5));
		BorderPane.setMargin(progressBar, new Insets(5));
	}

	public String getName() {
		return name;
	}

	public BorderPane getBorderPane() {
		return borderPane;
	}

	public Label getNameLabel() {
		return nameLabel;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public Label getCodeLabel() {
		return codeLabel;
	}
}
