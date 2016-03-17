package model;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class RencontreTest {

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
	 * Attributs: nom_renc, ville_renc
	 */
	@Test
	public void notEmptyTest() {
		Rencontre rencontre = new Rencontre(null, null, null, Date.valueOf("2020-01-01"), Date.valueOf("2020-01-01"), null, 0);
		Set<ConstraintViolation<Rencontre>> constraintViolations = validator.validate(rencontre);
		assertEquals(2, constraintViolations.size());
		assertEquals("ne peut pas être vide", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test controllant que les attributs NotNull ne peuvent pas etre null
	 * Attributs: date_deb_renc, date_fin_renc
	 */
	@Test
	public void notNullTest() {
		Rencontre rencontre = new Rencontre("test", "test", null, null, null, null, 0);
		Set<ConstraintViolation<Rencontre>> constraintViolations = validator.validate(rencontre);
		assertEquals(2, constraintViolations.size());
		assertEquals("ne peut pas être nul", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test controllant que les attributs limités à 80 caractères
	 * ne peuvent pas dépasser cette limite
	 * Attributs: nom_renc, ville_renc, lieu_renc
	 */
	@Test
	public void max80Length() {
		Rencontre rencontre = new Rencontre(phraseOver50.concat(phraseOver50), phraseOver50.concat(phraseOver50), phraseOver50.concat(phraseOver50), Date.valueOf("2020-01-01"), Date.valueOf("2020-01-01"), null, 0);
		Set<ConstraintViolation<Rencontre>> constraintViolations = validator.validate(rencontre);
		assertEquals(3, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 80", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test controllant que les attributs limités ne peuvent pas depasser une valeur
	 * Attributs: nb_pers_attendues
	 */
	@Test
	public void maxValueTest() {
		Rencontre rencontre = new Rencontre("test", "test", "test", Date.valueOf("2020-01-01"), Date.valueOf("2020-01-01"), null, 15000);
		Set<ConstraintViolation<Rencontre>> constraintViolations = validator.validate(rencontre);
		assertEquals(1, constraintViolations.size());
		assertEquals("doit être au maximum égal à 10000", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test qui certifie qu'un bean crée en respectant les contraintes est
	 * valide
	 */
	@Test
	public void valid() {
		Rencontre rencontre = new Rencontre("test", "test", "test", Date.valueOf("2020-01-01"), Date.valueOf("2020-01-01"), "test", 1000);
		Set<ConstraintViolation<Rencontre>> constraintViolations = validator.validate(rencontre);
		assertEquals(0, constraintViolations.size());
	}
}
