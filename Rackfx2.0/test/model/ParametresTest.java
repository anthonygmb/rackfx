package model;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class ParametresTest {

	private static Validator validator;

	/**
	 * Initialisation du validateur de hibernate validator
	 */
	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	/**
	 * Test controllant que les attributs NotEmpty ne peuvent pas etre null
	 * Attributs: langue
	 */
	@Test
	public void notEmptyTest() {
		Parametres param = new Parametres(null, null);
		Set<ConstraintViolation<Parametres>> constraintViolations = validator.validate(param);
		assertEquals(2, constraintViolations.size());
		assertEquals("ne peut pas être vide", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test qui certifie qu'un bean crée en respectant les contraintes est
	 * valide
	 */
	@Test
	public void valid() {
		Parametres param = new Parametres("test", "test");
		Set<ConstraintViolation<Parametres>> constraintViolations = validator.validate(param);
		assertEquals(0, constraintViolations.size());
	}
}
