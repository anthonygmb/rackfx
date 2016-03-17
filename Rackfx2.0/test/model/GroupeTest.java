package model;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import model.Groupe;

public class GroupeTest {

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
	 * Attributs: nom_groupe
	 */
	@Test
	public void notEmptyTest() {
		Groupe groupe = new Groupe(null, null, null, null);
		Set<ConstraintViolation<Groupe>> constraintViolations = validator.validate(groupe);
		assertEquals(1, constraintViolations.size());
		assertEquals("ne peut pas être vide", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 50 caractères
	 * ne peuvent pas dépasser cette limite
	 * Attributs: nom_groupe, carac_groupe, region_groupe
	 */
	@Test
	public void max50Length() {
		Groupe groupe = new Groupe(phraseOver50, phraseOver50, null, phraseOver50);
		Set<ConstraintViolation<Groupe>> constraintViolations = validator.validate(groupe);
		assertEquals(3, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 50", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test qui certifie qu'un bean crée en respectant les contraintes est
	 * valide
	 */
	@Test
	public void valid() {
		Groupe groupe = new Groupe("test", "test", "test", "test");
		Set<ConstraintViolation<Groupe>> constraintViolations = validator.validate(groupe);
		assertEquals(0, constraintViolations.size());
	}
}
