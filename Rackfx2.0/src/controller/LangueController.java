package controller;

import java.util.ResourceBundle;

import org.controlsfx.control.ToggleSwitch;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Parametres;
import sql.CRUD;
import utilities.Res_listes;
import utilities.Validateur;
import utilities.ValidateurExeption;

public class LangueController {

	@FXML
	private ComboBox<String> langue_cmbbox = new ComboBox<>(Res_listes.liste_langues);
	@FXML
	private ToggleSwitch ts_theme;
	private Parametres param;
	private Stage dialogStage;
	private ResourceBundle Lang_bundle;

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
		this.Lang_bundle = MainApp.getInstance().Lang_bundle;
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
		if (param.getLangue() == null) {
			langue_cmbbox.getSelectionModel().selectFirst();
		} else {
			langue_cmbbox.getSelectionModel().select(param.getLangue());
		}
		if (param.getTheme() != null && param.getTheme().equals("Dark")) {
			ts_theme.setSelected(true);
		} else {
			ts_theme.setSelected(false);
		}
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
		param.setLangue(langue_cmbbox.getSelectionModel().getSelectedItem());
		if (ts_theme.isSelected()) {
			param.setTheme("Dark");
		} else {
			param.setTheme("Modena");
		}
		try {
			Validateur.validator(param);
			if (MainApp.getInstance().parametresData.isEmpty()) {
				MainApp.getInstance().parametresData.add(param);
			} else {
				MainApp.getInstance().parametresData.set(0, param);
			}
			CRUD.saveOrUpdate(param);
			MainViewController.getInstance().lang_modif = true;
			dialogStage.close();
		} catch (ValidateurExeption e) { /* si la validation a échoué */
			Validateur.showPopup(AlertType.WARNING, Lang_bundle.getString("Erreur"),
					Lang_bundle.getString("Violation.de.contrainte"), e.getMessage()).showAndWait();
		}
	}
}
