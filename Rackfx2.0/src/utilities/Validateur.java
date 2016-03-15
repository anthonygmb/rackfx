package utilities;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Groupe;
import model.Organisateur;
import model.Personne;
import model.Rencontre;
import model.Representation;
import model.Titre;
import model.User;

public class Validateur {

	private static Stage popup;

	/**
	 * Méthode de validation des beans, cette méthode controle le format des
	 * attributs passés dans le bean.
	 * 
	 * @param obj
	 * @return true si le bean n'a pas problème de format
	 */
	public static <T> boolean validator(T obj) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		String errorMessage = "";
		boolean result = true;

		if (obj instanceof Groupe) {
			Groupe groupe = (Groupe) obj;
			Set<ConstraintViolation<Groupe>> constraintViolations = validator.validate(groupe);
			if (constraintViolations.size() > 0) {
				for (ConstraintViolation<Groupe> contraintes : constraintViolations) {
					errorMessage += contraintes.getPropertyPath() + ": " + contraintes.getMessage() + "\n";
				}
				result = false;
			}
		} else if (obj instanceof Personne) {
			Personne personne = (Personne) obj;
			Set<ConstraintViolation<Personne>> constraintViolations = validator.validate(personne);
			if (constraintViolations.size() > 0) {
				for (ConstraintViolation<Personne> contraintes : constraintViolations) {
					errorMessage += contraintes.getPropertyPath() + ": " + contraintes.getMessage() + "\n";
				}
				result = false;
			}
		} else if (obj instanceof Titre) {
			Titre titre = (Titre) obj;
			Set<ConstraintViolation<Titre>> constraintViolations = validator.validate(titre);
			if (constraintViolations.size() > 0) {
				for (ConstraintViolation<Titre> contraintes : constraintViolations) {
					errorMessage += contraintes.getPropertyPath() + ": " + contraintes.getMessage() + "\n";
				}
				result = false;
			}
		} else if (obj instanceof Rencontre) {
			Rencontre rencontre = (Rencontre) obj;
			Set<ConstraintViolation<Rencontre>> constraintViolations = validator.validate(rencontre);
			if (constraintViolations.size() > 0) {
				for (ConstraintViolation<Rencontre> contraintes : constraintViolations) {
					errorMessage += contraintes.getPropertyPath() + ": " + contraintes.getMessage() + "\n";
				}
				result = false;
			}
		} else if (obj instanceof Organisateur) {
			Organisateur organisateur = (Organisateur) obj;
			Set<ConstraintViolation<Organisateur>> constraintViolations = validator.validate(organisateur);
			if (constraintViolations.size() > 0) {
				for (ConstraintViolation<Organisateur> contraintes : constraintViolations) {
					errorMessage += contraintes.getPropertyPath() + ": " + contraintes.getMessage() + "\n";
				}
				result = false;
			}
		} else if (obj instanceof Representation) {
			Representation representation = (Representation) obj;
			Set<ConstraintViolation<Representation>> constraintViolations = validator.validate(representation);
			if (constraintViolations.size() > 0) {
				for (ConstraintViolation<Representation> contraintes : constraintViolations) {
					errorMessage += contraintes.getPropertyPath() + ": " + contraintes.getMessage() + "\n";
				}
				result = false;
			}
		} else if (obj instanceof User) {
			User user = (User) obj;
			Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
			if (constraintViolations.size() > 0) {
				for (ConstraintViolation<User> contraintes : constraintViolations) {
					errorMessage += contraintes.getPropertyPath() + ": " + contraintes.getMessage() + "\n";
				}
				result = false;
			}
		}
		if (!result) {
			showPopup(errorMessage);
		}
		return result;
	}

	/**
	 * Méthode d'affichage de popup de messages d'erreurs
	 * 
	 * @param message
	 */
	public static void showPopup(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(popup);
		alert.setTitle("Erreur");
		alert.setHeaderText("Erreur dans la saisie des données");
		alert.setContentText(message);
		alert.showAndWait();
	}
}
