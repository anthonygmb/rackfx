package model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Indexed
public class Rencontre {

	private long rencontreId;
	private StringProperty nom_renc;
	private StringProperty ville_renc;
	private StringProperty lieu_renc;
	private ObjectProperty<Date> date_deb_renc;
	private ObjectProperty<Date> date_fin_renc;
	private StringProperty periodicite_renc;
	private LongProperty nb_pers_attendues;
	private Set<Organisateur> liste_orga = new HashSet<Organisateur>();
	private Set<Representation> liste_repre = new HashSet<Representation>();

	public Rencontre() {
		this(null, null);
	}

	public Rencontre(String nom_renc, String ville_renc) {
		this.nom_renc = new SimpleStringProperty(nom_renc);
		this.ville_renc = new SimpleStringProperty(ville_renc);
		this.lieu_renc = new SimpleStringProperty("");
		this.date_deb_renc = new SimpleObjectProperty<Date>();
		this.date_fin_renc = new SimpleObjectProperty<Date>();
		this.periodicite_renc = new SimpleStringProperty("");
		this.nb_pers_attendues = new SimpleLongProperty();
	}

	// =================================================================================================
	@Id
	@GeneratedValue
	public long getRencontreId() {
		return rencontreId;
	}

	@SuppressWarnings("unused")
	private void setRencontreId(long rencontreId) {
		this.rencontreId = rencontreId;
	}

	// =================================================================================================
	@NotNull
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getNom_renc() {
		return nom_renc.get();
	}

	public void setNom_renc(String nom_renc) {
		this.nom_renc.set(nom_renc);
	}

	public StringProperty nom_rencProperty() {
		return nom_renc;
	}

	// =================================================================================================
	@NotNull
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getVille_renc() {
		return ville_renc.get();
	}

	public void setVille_renc(String ville_renc) {
		this.ville_renc.set(ville_renc);
	}

	public StringProperty ville_rencProperty() {
		return ville_renc;
	}

	// =================================================================================================
	public String getLieu_renc() {
		return lieu_renc.get();
	}

	public void setLieu_renc(String lieu_renc) {
		this.lieu_renc.set(lieu_renc);
	}

	public StringProperty lieu_rencProperty() {
		return lieu_renc;
	}

	// =================================================================================================
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public Date getDate_deb_renc() {
		return date_deb_renc.get();
	}

	public void setDate_deb_renc(Date date_deb_renc) {
		this.date_deb_renc.set(date_deb_renc);
	}

	public ObjectProperty<Date> date_deb_rencProperty() {
		return date_deb_renc;
	}

	// =================================================================================================
	public Date getDate_fin_renc() {
		return date_fin_renc.get();
	}

	public void setDate_fin_renc(Date date_fin_renc) {
		this.date_fin_renc.set(date_fin_renc);
	}

	public ObjectProperty<Date> date_fin_rencProperty() {
		return date_fin_renc;
	}

	// =================================================================================================
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getPeriodicite_renc() {
		return periodicite_renc.get();
	}

	public void setPeriodicite_renc(String periodicite_renc) {
		this.periodicite_renc.set(periodicite_renc);
	}

	public StringProperty periodicite_rencProperty() {
		return periodicite_renc;
	}

	// =================================================================================================
	public Long getNb_pers_attendues() {
		return nb_pers_attendues.get();
	}

	public void setNb_pers_attendues(Long nb_pers_attendues) {
		this.nb_pers_attendues.set(nb_pers_attendues);
	}

	public LongProperty nb_pers_attenduesProperty() {
		return nb_pers_attendues;
	}

	// =================================================================================================
	@IndexedEmbedded
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "rencontre")
	public Set<Organisateur> getListe_orga() {
		return liste_orga;
	}

	public void setListe_orga(Set<Organisateur> liste_orga) {
		this.liste_orga = liste_orga;
	}

	// =================================================================================================
	@IndexedEmbedded
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "rencontre")
	public Set<Representation> getListe_repre() {
		return liste_repre;
	}

	public void setListe_repre(Set<Representation> liste_repre) {
		this.liste_repre = liste_repre;
	}
}
