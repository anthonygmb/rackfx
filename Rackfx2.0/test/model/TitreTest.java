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

public class TitreTest {

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
	 * Attributs: titre, auteur
	 */
	@Test
	public void titreNotEmptyTest() {
		Titre titre = new Titre(null, null, Time.valueOf("01:01:01"), null, false, null);
		Set<ConstraintViolation<Titre>> constraintViolations = validator.validate(titre);
		assertEquals(2, constraintViolations.size());
		assertEquals("ne peut pas être vide", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 100 caractères ne peuvent
	 * pas dépasser cette limite Attributs: titre, auteur
	 */
	@Test
	public void titreMax100Length() {
		Titre titre = new Titre(phraseOver50.concat(phraseOver50), null, Time.valueOf("01:01:01"), null, false,
				phraseOver50.concat(phraseOver50));
		Set<ConstraintViolation<Titre>> constraintViolations = validator.validate(titre);
		assertEquals(2, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 100", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 4 caractères ne peuvent pas
	 * dépasser cette limite Attributs: annee
	 */
	@Test
	public void titreMax4Length() {
		Titre titre = new Titre("test", phraseOver50, Time.valueOf("01:01:01"), null, false, "test");
		Set<ConstraintViolation<Titre>> constraintViolations = validator.validate(titre);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 4", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs NotNull ne peuvent pas etre null
	 * Attributs: duree, reprise_titre
	 */
	@Test
	public void titreNotNullTest() {
		Titre titre = new Titre("test", "test", null, null, false, "test");
		Set<ConstraintViolation<Titre>> constraintViolations = validator.validate(titre);
		assertEquals(1, constraintViolations.size());
		assertEquals("ne peut pas être nul", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 50 caractères ne peuvent pas
	 * dépasser cette limite Attributs: genre
	 */
	@Test
	public void titreMax50Length() {
		Titre titre = new Titre("test", null, Time.valueOf("01:01:01"), phraseOver50, false, "test");
		Set<ConstraintViolation<Titre>> constraintViolations = validator.validate(titre);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 50", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test qui certifie qu'un bean crée en respectant les contraintes est
	 * valide
	 */
	@Test
	public void titreValid() {
		Titre titre = new Titre("test", "test", Time.valueOf("01:01:01"), "test", false, "test");
		Set<ConstraintViolation<Titre>> constraintViolations = validator.validate(titre);
		assertEquals(0, constraintViolations.size());
	}
}
