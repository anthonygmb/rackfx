package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Parametres;
import sql.CRUD;
import utilities.Res_listes;

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
	Parametres param;

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
	
	public void setParametres(Parametres param) {
		this.param = param;
		langue_cmbbox.getSelectionModel().select(param.getLangue());
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
		if (langue_cmbbox.getSelectionModel().getSelectedItem() == null) {
			param = new Parametres();
		}
		param.setLangue(langue_cmbbox.getSelectionModel().getSelectedItem());
		MainApp.getInstance().getParametresData().add(param);
		CRUD.saveOrUpdate(param);
		dialogStage.close();
	}	
}
