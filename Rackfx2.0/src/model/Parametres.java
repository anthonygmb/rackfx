package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Parametres {

	private long parametresId;
	private String langue;

	public Parametres() {
		this(null);
	}

	public Parametres(String langue) {
		this.langue = langue;
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
	public String getLangue() {
		return langue;
	}

	public void setLangue(String langue) {
		this.langue = langue;
	}
}
