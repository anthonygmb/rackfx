package bean;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import model.Personne;

public class PersonneTest {

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
	 * Attributs: nom_membre, prenom_membre, civi_membre
	 */
	@Test
	public void personneNotEmptyTest() {
		Personne personne = new Personne(null, null, null, null, null, null, null, false, null, null, null, null);
		Set<ConstraintViolation<Personne>> constraintViolations = validator.validate(personne);
		assertEquals(3, constraintViolations.size());
		assertEquals("ne peut pas être vide", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test controllant que les attributs limités à 50 caractères
	 * ne peuvent pas dépasser cette limite
	 * Attributs: nom_personne, prenom_personne
	 */
	@Test
	public void personneMax50Length() {
		Personne personne = new Personne(phraseOver50, phraseOver50, "test", null, null, null, null, false, null, null, null, null);
		Set<ConstraintViolation<Personne>> constraintViolations = validator.validate(personne);
		assertEquals(2, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 50", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test controllant que les attributs limités à 100 caractères
	 * ne peuvent pas dépasser cette limite
	 * Attributs: adresse_cor
	 */
	@Test
	public void personneMax100Length() {
		Personne personne = new Personne("test", "test", "test", null, null, null, null, false, phraseOver50.concat(phraseOver50), null, null, null);
		Set<ConstraintViolation<Personne>> constraintViolations = validator.validate(personne);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 100", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test controllant que les attributs limités à 13 caractères
	 * ne peuvent pas dépasser cette limite
	 * Attributs: tel_cor, fax_cor
	 */
	@Test
	public void personneMax13Size() {
		Personne personne = new Personne("test", "test", "test", null, null, null, null, false, null, phraseOver50, phraseOver50, null);
		Set<ConstraintViolation<Personne>> constraintViolations = validator.validate(personne);
		assertEquals(2, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 13", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test controllant que les attributs formattés en email
	 * respectent ce format
	 * Attributs: mail_cor
	 */
	@Test
	public void personneEmailTest() {
		Personne personne = new Personne("test", "test", "test", null, null, null, null, false, null, null, null, "testEmail");
		Set<ConstraintViolation<Personne>> constraintViolations = validator.validate(personne);
		assertEquals(1, constraintViolations.size());
		assertEquals("Adresse email mal formée", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test controllant que les attributs de date
	 * respectent les contraintes
	 * Attributs: date_naiss_membre
	 */
	@Test
	public void personneDateTest() {
		Personne personne = new Personne("test", "test", "test", Date.valueOf("2020-01-01"), null, null, null, false, null, null, null, null);
		Set<ConstraintViolation<Personne>> constraintViolations = validator.validate(personne);
		assertEquals(1, constraintViolations.size());
		assertEquals("doit être dans le passé", constraintViolations.iterator().next().getMessage());
	}
	
	/**
	 * Test qui certifie qu'un bean crée en respectant les contraintes est
	 * valide
	 */
	@Test
	public void personneValid() {
		Personne personne = new Personne("test", "test", "test", Date.valueOf("2010-01-01"), "test", "test", "test", false, "test", "test", "test", "test@email.com");
		Set<ConstraintViolation<Personne>> constraintViolations = validator.validate(personne);
		assertEquals(0, constraintViolations.size());
	}
}
