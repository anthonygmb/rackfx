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

public class Validateur {

	private static Stage popup;

	/**
	 * Méthode de validation des beans
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public static <T> void validator(T obj) throws ValidateurExeption {
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
			throw new ValidateurExeption(errorMessage);
		}
	}

	/**
	 * Méthode d'affichage de popup de messages d'erreurs.
	 * 
	 * @param type
	 * @param titre
	 * @param headerText
	 * @param message
	 * @return alert
	 */
	public static Alert showPopup(AlertType type, String titre, String headerText, String message) {
		Alert alert = new Alert(type);
		alert.initOwner(popup);
		alert.setTitle(titre);
		alert.setHeaderText(headerText);
		alert.setContentText(message);
		return alert;
	}

	/**
	 * Méthode pour vérifier si la date de début n'est pas avant la date de fin
	 * 
	 * @param dateDebut
	 * @param dateFin
	 * @throws Exception
	 */
	public static void valideDate(LocalDate dateDebut, LocalDate dateFin) throws ValidateurExeption {
		if (!dateDebut.isBefore(dateFin)) {
			throw new ValidateurExeption(
					MainApp.getInstance().Lang_bundle.getString("La.date.de.debut.doit.etre.avant.la.date.de.fin"));
		}
	}

	/**
	 * Méthode pour vérifier si l'heure de début n'est pas avant l'heure de fin
	 * 
	 * @param timeDebut
	 * @param timeFin
	 * @throws Exception
	 */
	public static void valideTime(LocalTime timeDebut, LocalTime timeFin) throws ValidateurExeption {
		if (!timeDebut.isBefore(timeFin)) {
			throw new ValidateurExeption(
					MainApp.getInstance().Lang_bundle.getString("L'heure.de.debut.doit.etre.avant.l'heure.de.fin"));
		}
	}
}
