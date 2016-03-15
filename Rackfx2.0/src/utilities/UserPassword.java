package utilities;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


//Comme le disait Marty, suivez le doc' !
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = UserPasswordValidator.class)
@Documented
public @interface UserPassword {

	String message() default "Invalid password";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

//	// Et là on ajoute notre liste de conditions
//	Condition[] conditions() default {};
}

//// Conditions qui sont définies ainsi
//public @interface Condition {
//	static enum Type {
//		LOWERCASE("a-z"), UPPERCASE("A-Z"), NUMBER("0-9");
//		String pattern;
//
//		Type(String pattern) {
//			this.pattern = pattern;
//		}
//	}
//
//	Type target();
//
//	int min();
//}

