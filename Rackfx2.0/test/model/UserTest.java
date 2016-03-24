package model;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class UserTest {

	private static Validator validator;
	private String phraseOver50 = "Phrase qui depasse le nombre maximal de caracteres authorisé";

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
	 * Attributs: login, mot_de_passe
	 */
	@Test
	public void notEmptyTest() {
		User user = new User(null, null, "test");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertEquals(2, constraintViolations.size());
		assertEquals("ne peut pas être vide", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs NotNull ne peuvent pas etre null
	 * Attributs: droit_auth
	 */
	@Test
	public void notNullTest() {
		User user = new User("testEtTest", "testEtTest", null);
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertEquals(1, constraintViolations.size());
		assertEquals("ne peut pas être nul", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à minimum 6 caractères ne
	 * peuvent pas dépasser cette limite Attributs: login
	 */
	@Test
	public void min6Size() {
		User user = new User("test", "testEtTest", "test");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 6 et 25", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 25 caractères ne peuvent pas
	 * dépasser cette limite Attributs: login
	 */
	@Test
	public void max25Size() {
		User user = new User(phraseOver50, "testEtTest", "test");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 6 et 25", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test qui certifie qu'un bean crée en respectant les contraintes est
	 * valide
	 */
	@Test
	public void valid() {
		User user = new User("testEtTest", "testEtTest", "test");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertEquals(0, constraintViolations.size());
	}
}
