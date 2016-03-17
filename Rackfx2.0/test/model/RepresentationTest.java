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
	public void repreNotEmptyTest() {
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
	public void repreNotNullTest() {
		Representation repre = new Representation("test", "test", null, null);
		Set<ConstraintViolation<Representation>> constraintViolations = validator.validate(repre);
		assertEquals(2, constraintViolations.size());
		assertEquals("ne peut pas être nul", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test qui certifie qu'un bean crée en respectant les contraintes est
	 * valide
	 */
	@Test
	public void repreValid() {
		Representation repre = new Representation("test", "test", Time.valueOf("01:01:01"), Time.valueOf("01:01:01"));
		Set<ConstraintViolation<Representation>> constraintViolations = validator.validate(repre);
		assertEquals(0, constraintViolations.size());
	}
}
