package model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Indexed
public class Organisateur {

	private long orgaId;
	private StringProperty nom_orga;
	private StringProperty prenom_orga;
	private StringProperty civi_orga;
	private StringProperty adresse_entreprise_orga;
	private LongProperty tel_orga;
	private LongProperty fax_orga;
	private StringProperty mail_orga;
	private StringProperty entreprise_orga;
	private Rencontre rencontre;

	public Organisateur() {
		this(null, null);
	}

	public Organisateur(String nom_orga, String prenom_orga) {
		this.nom_orga = new SimpleStringProperty(nom_orga);
		this.prenom_orga = new SimpleStringProperty(prenom_orga);
		this.civi_orga = new SimpleStringProperty("");
		this.adresse_entreprise_orga = new SimpleStringProperty("");
		this.tel_orga = new SimpleLongProperty();
		this.fax_orga = new SimpleLongProperty();
		this.mail_orga = new SimpleStringProperty("");
		this.entreprise_orga = new SimpleStringProperty("");
	}

	// =================================================================================================
	@Id
	@GeneratedValue
	public long getOrgaId() {
		return orgaId;
	}

	@SuppressWarnings("unused")
	private void setOrgaId(long orgaId) {
		this.orgaId = orgaId;
	}

	// =================================================================================================
	@NotNull
	@Field
	public String getNom_orga() {
		return nom_orga.get();
	}

	public void setNom_orga(String nom_orga) {
		this.nom_orga.set(nom_orga);
	}

	public StringProperty nom_orgaProperty() {
		return nom_orga;
	}

	// =================================================================================================
	@NotNull
	@Field
	public String getPrenom_orga() {
		return prenom_orga.get();
	}

	public void setPrenom_orga(String prenom_orga) {
		this.prenom_orga.set(prenom_orga);
	}

	public StringProperty prenom_orgaProperty() {
		return prenom_orga;
	}

	// =================================================================================================
	@NotNull
	public String getCivi_orga() {
		return civi_orga.get();
	}

	public void setCivi_orga(String civi_orga) {
		this.civi_orga.set(civi_orga);
	}

	public StringProperty civi_orgaProperty() {
		return civi_orga;
	}

	// =================================================================================================
	public String getAdresse_entreprise_orga() {
		return adresse_entreprise_orga.get();
	}

	public void setAdresse_entreprise_orga(String adresse_entreprise_orga) {
		this.adresse_entreprise_orga.set(adresse_entreprise_orga);
	}

	public StringProperty adresse_entreprise_orgaProperty() {
		return adresse_entreprise_orga;
	}

	// =================================================================================================
	@NotNull
	public Long getTel_orga() {
		return tel_orga.get();
	}

	public void setTel_orga(Long tel_orga) {
		this.tel_orga.set(tel_orga);
	}

	public LongProperty tel_orgaProperty() {
		return tel_orga;
	}

	// =================================================================================================
	public Long getFax_orga() {
		return fax_orga.get();
	}

	public void setFax_orga(Long fax_orga) {
		this.fax_orga.set(fax_orga);
	}

	public LongProperty fax_orgaProperty() {
		return fax_orga;
	}

	// =================================================================================================
	public String getMail_orga() {
		return mail_orga.get();
	}

	public void setMail_orga(String mail_orga) {
		this.mail_orga.set(mail_orga);
	}

	public StringProperty mail_orgaProperty() {
		return mail_orga;
	}

	// =================================================================================================
	@Field
	public String getEntreprise_orga() {
		return entreprise_orga.get();
	}

	public void setEntreprise_orga(String entreprise_orga) {
		this.entreprise_orga.set(entreprise_orga);
	}

	public StringProperty entreprise_orgaProperty() {
		return entreprise_orga;
	}

	// =================================================================================================
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rencontreId", nullable = false)
	public Rencontre getRencontre() {
		return rencontre;
	}

	public void setRencontre(Rencontre rencontre) {
		this.rencontre = rencontre;
	}
}
