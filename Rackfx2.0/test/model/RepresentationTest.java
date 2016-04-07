package model;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class RepresentationTest {

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
	 * Attributs: nom_groupe, nom_titre
	 */
	@Test
	public void notEmptyTest() {
		Representation repre = new Representation(null, null, Time.valueOf("01:01:01"), Time.valueOf("01:01:01"));
		Set<ConstraintViolation<Representation>> constraintViolations = validator.validate(repre);
		assertEquals(2, constraintViolations.size());
		assertEquals("ne peut pas être vide", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs NotNull ne peuvent pas etre null
	 * Attributs: heure_debut, heure_fin
	 */
	@Test
	public void notNullTest() {
		Representation repre = new Representation("test", "test", null, null);
		Set<ConstraintViolation<Representation>> constraintViolations = validator.validate(repre);
		assertEquals(2, constraintViolations.size());
		assertEquals("ne peut pas être nul", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 50 caractères ne peuvent pas
	 * dépasser cette limite Attributs: nom_groupe
	 */
	@Test
	public void max50Length() {
		Representation repre = new Representation(phraseOver50, "test", Time.valueOf("01:01:01"),
				Time.valueOf("01:01:01"));
		Set<ConstraintViolation<Representation>> constraintViolations = validator.validate(repre);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 50", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 100 caractères ne peuvent
	 * pas dépasser cette limite Attributs: nom_titre
	 */
	@Test
	public void max100Length() {
		Representation repre = new Representation("test", phraseOver50.concat(phraseOver50), Time.valueOf("01:01:01"),
				Time.valueOf("01:01:01"));
		Set<ConstraintViolation<Representation>> constraintViolations = validator.validate(repre);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 100", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test qui certifie qu'un bean crée en respectant les contraintes est
	 * valide
	 */
	@Test
	public void valid() {
		Representation repre = new Representation("test", "test", Time.valueOf("01:01:01"), Time.valueOf("01:01:01"));
		Set<ConstraintViolation<Representation>> constraintViolations = validator.validate(repre);
		assertEquals(0, constraintViolations.size());
	}
}
