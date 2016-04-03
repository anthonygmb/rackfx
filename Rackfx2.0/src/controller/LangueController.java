package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Parametres;
import sql.CRUD;
import utilities.Res_listes;
import utilities.Validateur;

public class LangueController {

	@FXML
	private ComboBox<String> langue_cmbbox = new ComboBox<>(Res_listes.liste_langues);
	private Parametres param;
	private Stage dialogStage;

	/* Singleton */
	/** Instance unique pré-initialisée */
	private static LangueController INSTANCE_LANG_CONTROLLER;

	/** Point d'accès pour l'instance unique du singleton */
	public static LangueController getInstance() {
		return INSTANCE_LANG_CONTROLLER;
	}

	@FXML
	private void initialize() {
		INSTANCE_LANG_CONTROLLER = this;
		MainViewController.getInstance().lang_modif = false;
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
	 * Methode d'affichage des paramètres contenus dans la base de donnée.
	 * 
	 * @param param
	 */
	public void setParametres(Parametres param) {
		this.param = param;
		langue_cmbbox.getSelectionModel().select(param.getLangue());
	}

	/**
	 * Methode d'annulation de changement de langue.
	 */
	@FXML
	private void annulerlangue() {
		dialogStage.close();
	}

	/**
	 * Methode de validation de changement de langue.
	 */
	@FXML
	private void validerlangue() {
		if (langue_cmbbox.getSelectionModel().getSelectedItem() == null) {
			param = new Parametres();
		}
		param.setLangue(langue_cmbbox.getSelectionModel().getSelectedItem());
		/* validation des contraintes */
		if (Validateur.validator(param)) {
			if (MainApp.getInstance().parametresData.isEmpty()) {
				MainApp.getInstance().parametresData.add(param);
			} else {
				MainApp.getInstance().parametresData.set(0, param);
			}
			CRUD.saveOrUpdate(param);
			MainViewController.getInstance().lang_modif = true;
			dialogStage.close();
		}
	}
}
