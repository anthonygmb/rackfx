package model;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Indexed
public class Titre {

	private long titreId;
	private StringProperty titre;
	private StringProperty annee;
	private ObjectProperty<Time> duree;
	private String genre;
	private boolean reprise_titre;
	private String auteur;
	private Groupe groupe;

	public Titre() {
		this(null, null, null, null, false, null);
	}

	public Titre(String titre, String annee, Time duree, String genre, boolean reprise_titre, String auteur) {
		this.titre = new SimpleStringProperty(titre);
		this.annee = new SimpleStringProperty(annee);
		this.duree = new SimpleObjectProperty<Time>(duree);
		this.genre = genre;
		this.reprise_titre = reprise_titre;
		this.auteur = auteur;
	}

	// =================================================================================================
	@Id
	@DocumentId
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getTitreId() {
		return titreId;
	}

	@SuppressWarnings("unused")
	private void setTitreId(long titreId) {
		this.titreId = titreId;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 100)
	@Field
	@Analyzer(definition = "ngram")
	public String getTitre() {
		return titre.get();
	}

	public void setTitre(String titre) {
		this.titre.set(titre);
	}

	public StringProperty titreProperty() {
		return titre;
	}

	// =================================================================================================
	@Length(max = 4)
	@Field
	@Analyzer(definition = "ngram")
	public String getAnnee() {
		return annee.get();
	}

	public void setAnnee(String annee) {
		this.annee.set(annee);
	}

	public StringProperty anneeProperty() {
		return annee;
	}

	// =================================================================================================
	@NotNull
	public Time getDuree() {
		return duree.get();
	}

	public void setDuree(Time duree) {
		this.duree.set(duree);
	}

	public ObjectProperty<Time> dureeProperty() {
		return duree;
	}

	// =================================================================================================
	@Length(max = 50)
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	// =================================================================================================
	public boolean getReprise_titre() {
		return reprise_titre;
	}

	public void setReprise_titre(boolean reprise_titre) {
		this.reprise_titre = reprise_titre;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 100)
	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	// =================================================================================================
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "groupeId", nullable = false)
	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}
}
