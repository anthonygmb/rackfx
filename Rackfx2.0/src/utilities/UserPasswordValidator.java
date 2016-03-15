package utilities;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//public class UserPasswordValidator implements ConstraintValidator<UserPassword, String> {

//	private Condition[] conditions;

	// On récupère les conditions à l'initialisation
/*	@Override
	public void initialize(UserPassword constraintAnnotation) {
		conditions = constraintAnnotation.conditions();
	}

	// Et c'est ci que se passe l'évaluation
	@Override
	public boolean isValid(final String password, ConstraintValidatorContext constraintContext) {
		if (password == null)
			return false;

		// On passe simplement en revue chaque condition
		for (Condition c : conditions) {
			// Je vous passe l'implémentation de 'count()'
			if (count(c.target().pattern, password) < c.min())
				return false;
		}
		return true;
	}*/
//}
