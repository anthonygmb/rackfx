package model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Indexed
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "nom_orga", "prenom_orga" }) )
public class Organisateur {

	private long orgaId;
	private String nom_orga;
	private String prenom_orga;
	private boolean civi_orga;
	private String adresse_entreprise_orga;
	private String tel_orga;
	private String fax_orga;
	private String mail_orga;
	private String entreprise_orga;
	private Rencontre rencontre;

	public Organisateur() {
		this(null, null, false, null, null, null, null, null);
	}

	public Organisateur(String nom_orga, String prenom_orga, boolean civi_orga, String adresse_entreprise_orga,
			String tel_orga, String fax_orga, String mail_orga, String entreprise_orga) {
		this.nom_orga = nom_orga;
		this.prenom_orga = prenom_orga;
		this.civi_orga = civi_orga;
		this.adresse_entreprise_orga = adresse_entreprise_orga;
		this.tel_orga = tel_orga;
		this.fax_orga = fax_orga;
		this.mail_orga = mail_orga;
		this.entreprise_orga = entreprise_orga;
	}

	// =================================================================================================
	@Id
	@DocumentId
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getOrgaId() {
		return orgaId;
	}

	@SuppressWarnings("unused")
	private void setOrgaId(long orgaId) {
		this.orgaId = orgaId;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 50)
	@Field
	public String getNom_orga() {
		return nom_orga;
	}

	public void setNom_orga(String nom_orga) {
		this.nom_orga = nom_orga;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 50)
	@Field
	public String getPrenom_orga() {
		return prenom_orga;
	}

	public void setPrenom_orga(String prenom_orga) {
		this.prenom_orga = prenom_orga;
	}

	// =================================================================================================
	public boolean getCivi_orga() {
		return civi_orga;
	}

	public void setCivi_orga(boolean civi_orga) {
		this.civi_orga = civi_orga;
	}

	// =================================================================================================
	@Length(max = 100)
	public String getAdresse_entreprise_orga() {
		return adresse_entreprise_orga;
	}

	public void setAdresse_entreprise_orga(String adresse_entreprise_orga) {
		this.adresse_entreprise_orga = adresse_entreprise_orga;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 13)
	public String getTel_orga() {
		return tel_orga;
	}

	public void setTel_orga(String tel_orga) {
		this.tel_orga = tel_orga;
	}

	// =================================================================================================
	@Length(max = 13)
	public String getFax_orga() {
		return fax_orga;
	}

	public void setFax_orga(String fax_orga) {
		this.fax_orga = fax_orga;
	}

	// =================================================================================================
	@Email
	@Length(max = 50)
	public String getMail_orga() {
		return mail_orga;
	}

	public void setMail_orga(String mail_orga) {
		this.mail_orga = mail_orga;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 50)
	@Field
	public String getEntreprise_orga() {
		return entreprise_orga;
	}

	public void setEntreprise_orga(String entreprise_orga) {
		this.entreprise_orga = entreprise_orga;
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
