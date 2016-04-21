package utilities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import controller.MainApp;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Groupe;
import model.Organisateur;
import model.Parametres;
import model.Personne;
import model.Rencontre;
import model.Representation;
import model.Titre;
import model.User;

public class Validateur {

	private static Stage popup;

	/**
	 * Méthode de validation des beans, cette méthode controle le format des
	 * attributs passés dans le model.
	 *
	 * @param <T>
	 *            the generic type
	 * @param obj
	 *            the obj
	 * @return true si le model n'a pas problème de format
	 * @throws Exception 
	 */
	public static <T> void validator(T obj) throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		String errorMessage = "";

		Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
		if (constraintViolations.size() > 0) {
			for (ConstraintViolation<T> contraintes : constraintViolations) {
				errorMessage += contraintes.getPropertyPath().toString().substring(0, 1).toUpperCase()
						+ contraintes.getPropertyPath().toString().substring(1) + ": " + contraintes.getMessage()
						+ "\n";
			}
			throw new Exception(errorMessage);
		}

//		if (!result) {
//			showPopup(AlertType.ERROR, MainApp.getInstance().Lang_bundle.getString("Erreur"),
//					MainApp.getInstance().Lang_bundle.getString("Violation.de.contrainte"), errorMessage).showAndWait();
//		} // TODO message erreur en anglais
	}

	/**
	 * Méthode d'affichage de popup de messages d'erreurs.
	 *
	 * @param message
	 *            the message
	 */
	public static Alert showPopup(AlertType type, String titre, String headerText, String message) {

		Alert alert = new Alert(type);
		alert.initOwner(popup);
		alert.setTitle(titre);
		alert.setHeaderText(headerText);
		alert.setContentText(message);
		// if (alert.getAlertType() == AlertType.NONE) {
		// PauseTransition pause = new PauseTransition(Duration.seconds(5));
		// pause.setOnFinished(e -> alert.hide());
		// pause.play();
		// }
		return alert;
	}

	/**
	 * Valide date.
	 *
	 * @param dateDebut
	 *            the date debut
	 * @param dateFin
	 *            the date fin
	 * @return true, if successful
	 */
	public static boolean valideDate(LocalDate dateDebut, LocalDate dateFin) {
		if (dateDebut.isBefore(dateFin)) {
			return true;
		} else {
			showPopup(AlertType.ERROR, MainApp.getInstance().Lang_bundle.getString("Erreur"),
					MainApp.getInstance().Lang_bundle.getString("Violation.de.contrainte"),
					MainApp.getInstance().Lang_bundle.getString("La.date.de.debut.doit.etre.avant.la.date.de.fin"))
							.showAndWait();
			return false;
		}
	}

	/**
	 * Valide time.
	 *
	 * @param timeDebut
	 *            the time debut
	 * @param timeFin
	 *            the time fin
	 * @return true, if successful
	 */
	public static boolean valideTime(LocalTime timeDebut, LocalTime timeFin) {
		if (timeDebut.isBefore(timeFin)) {
			return true;
		} else {
			showPopup(AlertType.ERROR, MainApp.getInstance().Lang_bundle.getString("Erreur"),
					MainApp.getInstance().Lang_bundle.getString("Violation.de.contrainte"),
					MainApp.getInstance().Lang_bundle.getString("L'heure.de.debut.doit.etre.avant.l'heure.de.fin"))
							.showAndWait();
			return false;
		}
	}
}
