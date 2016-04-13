package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "login") )
public class User {

	private long userId;
	private StringProperty login;
	private StringProperty mot_de_passe;
	private StringProperty droit_auth;

	public User() {
		this(null, null, null);
	}

	public User(String login, String mot_de_passe, String droit_auth) {
		this.login = new SimpleStringProperty(login);
		this.mot_de_passe = new SimpleStringProperty(mot_de_passe);
		this.droit_auth = new SimpleStringProperty(droit_auth);
	}

	// =================================================================================================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getUserId() {
		return userId;
	}

	@SuppressWarnings("unused")
	private void setUserId(long userId) {
		this.userId = userId;
	}

	// =================================================================================================
	@NotEmpty
	@Length(min = 6, max = 25)
	public String getLogin() {
		return login.get();
	}

	public void setLogin(String login) {
		this.login.set(login);
	}

	public StringProperty loginProperty() {
		return login;
	}

	// =================================================================================================
	@NotEmpty
	public String getMot_de_passe() {
		return mot_de_passe.get();
	}

	public void setMot_de_passe(String mot_de_passe) {
		this.mot_de_passe.set(mot_de_passe);
	}

	public StringProperty mot_de_passeProperty() {
		return mot_de_passe;
	}

	// =================================================================================================
	@NotNull
	@Length(max = 15)
	public String getDroit_auth() {
		return droit_auth.get();
	}

	public void setDroit_auth(String droit_auth) {
		this.droit_auth.set(droit_auth);
	}

	public StringProperty droit_authProperty() {
		return droit_auth;
	}
}
