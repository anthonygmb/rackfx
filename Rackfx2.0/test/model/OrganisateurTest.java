package model;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class OrganisateurTest {

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
	 * Attributs: nom_orga, prenom_orga, civi_orga, tel_orga, entreprise_orga
	 */
	@Test
	public void notEmptyTest() {
		Organisateur orga = new Organisateur(null, null, false, null, null, null, null, null);
		Set<ConstraintViolation<Organisateur>> constraintViolations = validator.validate(orga);
		assertEquals(4, constraintViolations.size());
		assertEquals("ne peut pas être vide", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 50 caractères ne peuvent pas
	 * dépasser cette limite Attributs: nom_orga, prenom_orga, entreprise_orga
	 */
	@Test
	public void max50Length() {
		Organisateur orga = new Organisateur(phraseOver50, phraseOver50, false, null, "test", null,
				"azertyuiopqsdfghjklmwxcvbnazertyuiopqsdfghjklmwxcv@gmail.com", phraseOver50);
		Set<ConstraintViolation<Organisateur>> constraintViolations = validator.validate(orga);
		assertEquals(4, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 50", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 100 caractères ne peuvent
	 * pas dépasser cette limite Attributs: adresse_entreprise_orga
	 */
	@Test
	public void max100Length() {
		Organisateur orga = new Organisateur("test", "test", false, phraseOver50.concat(phraseOver50), "test", null,
				null, "test");
		Set<ConstraintViolation<Organisateur>> constraintViolations = validator.validate(orga);
		assertEquals(1, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 100", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs limités à 13 caractères ne peuvent pas
	 * dépasser cette limite Attributs: tel_orga, fax_orga
	 */
	@Test
	public void max13Length() {
		Organisateur orga = new Organisateur("test", "test", false, "test", phraseOver50, phraseOver50, null, "test");
		Set<ConstraintViolation<Organisateur>> constraintViolations = validator.validate(orga);
		assertEquals(2, constraintViolations.size());
		assertEquals("la taille doit être entre 0 et 13", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test controllant que les attributs formattés en email respectent ce
	 * format Attributs: mail_orga
	 */
	@Test
	public void emailTest() {
		Organisateur orga = new Organisateur("test", "test", false, null, "test", null, "testEmail", "test");
		Set<ConstraintViolation<Organisateur>> constraintViolations = validator.validate(orga);
		assertEquals(1, constraintViolations.size());
		assertEquals("Adresse email mal formée", constraintViolations.iterator().next().getMessage());
	}

	/**
	 * Test qui certifie qu'un bean crée en respectant les contraintes est
	 * valide
	 */
	@Test
	public void valid() {
		Organisateur orga = new Organisateur("test", "test", false, "test", "test", "test", "test@email.com", "test");
		Set<ConstraintViolation<Organisateur>> constraintViolations = validator.validate(orga);
		assertEquals(0, constraintViolations.size());
	}
}
