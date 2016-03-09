package model;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Indexed
public class Representation {

	private long repreId;
	private ObjectProperty<Time> heure_debut;
	private ObjectProperty<Time> heure_fin;
	private StringProperty nom_groupe;
	// private StringProperty nom_titre;
	 private Rencontre rencontre;
//	private Set<Rencontre> liste_rencontre = new HashSet<Rencontre>();
	// private Titre titre;
	private Groupe groupe;

	public Representation() {
		this(null, null, /* null, */ null);
	}

	public Representation(String nom_groupe, /* String nom_titre, */Time heure_debut, Time heure_fin) {
		this.nom_groupe = new SimpleStringProperty(nom_groupe);
		// this.nom_titre = new SimpleStringProperty(nom_titre);
		this.heure_debut = new SimpleObjectProperty<Time>();
		this.heure_fin = new SimpleObjectProperty<Time>();
	}

	// =================================================================================================
	@Id
	@GeneratedValue
	public long getRepreId() {
		return repreId;
	}

	@SuppressWarnings("unused")
	private void setRepreId(long repreId) {
		this.repreId = repreId;
	}

	// =================================================================================================
	@Field
	public Time getHeure_debut() {
		return heure_debut.get();
	}

	public void setHeure_debut(Time heure_debut) {
		this.heure_debut.set(heure_debut);
	}

	public ObjectProperty<Time> heure_debutProperty() {
		return heure_debut;
	}

	// =================================================================================================
	public Time getHeure_fin() {
		return heure_fin.get();
	}

	public void setHeure_fin(Time heure_fin) {
		this.heure_fin.set(heure_fin);
	}

	public ObjectProperty<Time> heure_fintProperty() {
		return heure_fin;
	}

	// =================================================================================================
	@NotNull
	@Column(name = "groupe_joue")
	public String getNom_Groupe() {
		return nom_groupe.get();
	}

	public void setNom_Groupe(String nom_groupe) {
		this.nom_groupe.set(nom_groupe);
	}

	public StringProperty nom_groupeProperty() {
		return nom_groupe;
	}

	// =================================================================================================
	// @NotNull
	// @Column(name = "titre_joue")
	// public String getNom_Titre() {
	// return nom_titre.get();
	// }
	//
	// public void setNom_Titre(String nom_titre) {
	// this.nom_titre.set(nom_titre);
	// }
	//
	// public StringProperty nom_titreProperty() {
	// return nom_titre;
	// }

	// =================================================================================================
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinTable(name = "renc_repre", joinColumns = @JoinColumn(name = "repreId") , inverseJoinColumns = @JoinColumn(name = "rencontreId") )
	 public Rencontre getRencontre() {
	 return rencontre;
	 }
	
	 public void setRencontre(Rencontre rencontre) {
	 this.rencontre = rencontre;
	 }
	
//	@ManyToMany(cascade=CascadeType.ALL)
//	@JoinTable(name = "renc_repre", joinColumns = @JoinColumn(name = "repreId") , inverseJoinColumns = @JoinColumn(name = "rencontreId") )
//	public Set<Rencontre> getListe_rencontre() {
//		return liste_rencontre;
//	}
//
//	public void setListe_rencontre(Set<Rencontre> liste_rencontre) {
//		this.liste_rencontre = liste_rencontre;
//	}

	// =================================================================================================
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "titreId", nullable = false)
	// public Titre getTitre() {
	// return titre;
	// }
	//
	// public void setTitre(Titre titre) {
	// this.titre = titre;
	// }

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupeId", nullable = false)
	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

}
