package controller;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import utilities.Res_listes;
import javafx.scene.control.ComboBox;

public class LangueController {
	private Stage dialogStage;
	@FXML
	private Label langue_lb;
	@FXML
	private ComboBox<String> langue_cmbbox = new ComboBox<>(Res_listes.liste_langues);
	@FXML
	private Button langue_btn_valid;
	@FXML
	private Button langue_btn_annuler;

	@FXML
	private void initialize() {
		langue_cmbbox.getItems().addAll(Res_listes.liste_langues);
	}
	
	/**
	 * Importe stage dans cette fenetre.
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	/**
	 * Methode d'annulation de changement de langue
	 */
	@FXML
	private void annulerlangue() {
		dialogStage.close();
	}
	
	/**
	 * Methode de validation de changement de langue
	 */
	@FXML
	private void validerlangue() {
		MainViewController.getInstance().langue = langue_cmbbox.getSelectionModel().getSelectedItem();
		dialogStage.close();
	}
	
}
