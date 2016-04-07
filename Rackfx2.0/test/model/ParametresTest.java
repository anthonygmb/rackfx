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
	 * Test controllant que les attributs limités à 50 caractères ne peuvent pas
	 * dépasser cette limite Attributs: langue
	 */
	@Test
	public void max50Length() {
		Parametres param = new Parametres(phraseOver50, "test");
		Set<ConstraintViolation<Parametres>> constraintViolations = validator.validate(param);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 50", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test controllant que les attributs limités à 13 caractères ne peuvent pas
	 * dépasser cette limite Attributs: theme
	 */
	@Test
	public void max13Length() {
		Parametres param = new Parametres("test", phraseOver50);
		Set<ConstraintViolation<Parametres>> constraintViolations = validator.validate(param);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 13", constraintViolations.iterator().next().getMessage());
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
