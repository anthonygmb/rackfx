package model;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
	private StringProperty genre;
	private BooleanProperty reprise_titre;
	private StringProperty auteur;
	private Groupe groupe;

	public Titre() {
		this(null, null);
	}

	public Titre(String titre, String auteur) {
		this.titre = new SimpleStringProperty(titre);
		this.annee = new SimpleStringProperty("2016");
		this.duree = new SimpleObjectProperty<Time>();
		this.genre = new SimpleStringProperty("");
		this.reprise_titre = new SimpleBooleanProperty(true);
		this.auteur = new SimpleStringProperty(auteur);
	}

	// =================================================================================================
	@Id
	@DocumentId
	@GeneratedValue
	public long getTitreId() {
		return titreId;
	}

	@SuppressWarnings("unused")
	private void setTitreId(long titreId) {
		this.titreId = titreId;
	}

	// =================================================================================================
	@NotNull
	@NotEmpty
	@Field
	@Analyzer(definition = "customanalyzer")
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
	@NotNull
	@NotEmpty
	@Field
	@Analyzer(definition = "customanalyzer")
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
	public String getGenre() {
		return genre.get();
	}

	public void setGenre(String genre) {
		this.genre.set(genre);
	}

	public StringProperty genreProperty() {
		return genre;
	}

	// =================================================================================================
	public Boolean getReprise_titre() {
		return reprise_titre.get();
	}

	public void setReprise_titre(Boolean reprise_titre) {
		this.reprise_titre.set(reprise_titre);
	}

	public BooleanProperty reprise_titreProperty() {
		return reprise_titre;
	}

	// =================================================================================================
	@NotNull
	@NotEmpty
	public String getAuteur() {
		return auteur.get();
	}

	public void setAuteur(String auteur) {
		this.auteur.set(auteur);
	}

	public StringProperty auteurProperty() {
		return auteur;
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
