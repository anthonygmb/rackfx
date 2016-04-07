package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Parametres {

	private long parametresId;
	private String langue;
	private String theme;

	public Parametres() {
		this(null, null);
	}

	public Parametres(String langue, String theme) {
		this.langue = langue;
		this.theme = theme;
	}

	// =================================================================================================
	@Id
	@GeneratedValue
	public long getParametresId() {
		return parametresId;
	}

	@SuppressWarnings("unused")
	private void setParametresId(long parametresId) {
		this.parametresId = parametresId;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 50)
	public String getLangue() {
		return langue;
	}

	public void setLangue(String langue) {
		this.langue = langue;
	}

	// =================================================================================================
	@NotEmpty
	@Length(max = 13)
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
}
