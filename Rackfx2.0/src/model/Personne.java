package model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Indexed
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "nom_membre", "prenom_membre" }) )
public class Personne {

	private long personneId;
	private StringProperty nom_membre;
	private StringProperty prenom_membre;
	private StringProperty civi_membre;
	private ObjectProperty<Date> date_naiss_membre;
	private StringProperty spe_membre;
	private StringProperty instru_membre;
	private StringProperty respon_membre;
	private BooleanProperty correspondant;
	private StringProperty adresse_cor;
	private StringProperty tel_cor;
	private StringProperty fax_cor;
	private StringProperty mail_cor;
	private Groupe groupe;

	public Personne() {
		this(null, null);
	}

	public Personne(String nom_membre, String prenom_membre) {
		this.nom_membre = new SimpleStringProperty(nom_membre);
		this.prenom_membre = new SimpleStringProperty(prenom_membre);
		this.civi_membre = new SimpleStringProperty("");
		this.date_naiss_membre = new SimpleObjectProperty<Date>();
		this.spe_membre = new SimpleStringProperty("");
		this.instru_membre = new SimpleStringProperty("");
		this.respon_membre = new SimpleStringProperty("");
		this.correspondant = new SimpleBooleanProperty(true);
		this.adresse_cor = new SimpleStringProperty("");
		this.tel_cor = new SimpleStringProperty("");
		this.fax_cor = new SimpleStringProperty("");
		this.mail_cor = new SimpleStringProperty("");
	}

	// =================================================================================================
	@Id
	@DocumentId
	@GeneratedValue
	public long getPersonneId() {
		return personneId;
	}

	@SuppressWarnings("unused")
	private void setPersonneId(long personneId) {
		this.personneId = personneId;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 50)
	@Field
	@Analyzer(definition = "customanalyzer")
	public String getNom_membre() {
		return nom_membre.get();
	}

	public void setNom_membre(String nom_membre) {
		this.nom_membre.set(nom_membre);
	}

	public StringProperty nom_membreProperty() {
		return nom_membre;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 50)
	@Field
	@Analyzer(definition = "customanalyzer")
	public String getPrenom_membre() {
		return prenom_membre.get();
	}

	public void setPrenom_membre(String prenom_membre) {
		this.prenom_membre.set(prenom_membre);
	}

	public StringProperty prenom_membreProperty() {
		return prenom_membre;
	}

	// =================================================================================================
	@NotEmpty
	public String getCivi_membre() {
		return civi_membre.get();
	}

	public void setCivi_membre(String civi_membre) {
		this.civi_membre.set(civi_membre);
	}

	public StringProperty civi_membreProperty() {
		return civi_membre;
	}

	// =================================================================================================
	@Past
	public Date getDate_naiss_membre() {
		return date_naiss_membre.get();
	}

	public void setDate_naiss_membre(Date date_naiss_membre) {
		this.date_naiss_membre.set(date_naiss_membre);
	}

	public ObjectProperty<Date> date_naiss_membreProperty() {
		return date_naiss_membre;
	}

	// =================================================================================================
	public String getSpe_membre() {
		return spe_membre.get();
	}

	public void setSpe_membre(String spe_membre) {
		this.spe_membre.set(spe_membre);
	}

	public StringProperty spe_membreProperty() {
		return spe_membre;
	}

	// =================================================================================================
	public String getInstru_membre() {
		return instru_membre.get();
	}

	public void setInstru_membre(String instru_membre) {
		this.instru_membre.set(instru_membre);
	}

	public StringProperty instru_membreProperty() {
		return instru_membre;
	}

	// =================================================================================================
	public String getRespon_membre() {
		return respon_membre.get();
	}

	public void setRespon_membre(String respon_membre) {
		this.respon_membre.set(respon_membre);
	}

	public StringProperty respon_membreProperty() {
		return respon_membre;
	}

	// =================================================================================================
	@NotNull
	public Boolean getCorrespondant() {
		return correspondant.get();
	}

	public void setCorrespondant(Boolean correspondant) {
		this.correspondant.set(correspondant);
	}

	public BooleanProperty correspondantProperty() {
		return correspondant;
	}

	// =================================================================================================
	@Length(max = 100)
	public String getAdresse_cor() {
		return adresse_cor.get();
	}

	public void setAdresse_cor(String adresse_cor) {
		this.adresse_cor.set(adresse_cor);
	}

	public StringProperty adresse_corProperty() {
		return adresse_cor;
	}

	// =================================================================================================
	@Size(max = 13)
	public String getTel_cor() {
		return tel_cor.get();
	}

	public void setTel_cor(String tel_cor) {
		this.tel_cor.set(tel_cor);
	}

	public StringProperty tel_corProperty() {
		return tel_cor;
	}

	// =================================================================================================
	@Size(max = 13)
	public String getFax_cor() {
		return fax_cor.get();
	}

	public void setFax_cor(String fax_cor) {
		this.fax_cor.set(fax_cor);
	}

	public StringProperty fax_corProperty() {
		return fax_cor;
	}

	// =================================================================================================
	@Email
	public String getMail_cor() {
		return mail_cor.get();
	}

	public void setMail_cor(String mail_cor) {
		this.mail_cor.set(mail_cor);
	}

	public StringProperty mail_corProperty() {
		return mail_cor;
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
