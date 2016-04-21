package utilities;

public class ValidateurExeption extends Exception {

	private static final long serialVersionUID = 1L;

	public ValidateurExeption() {
		super();
	}

	public ValidateurExeption(String errorMessage) {
		super(errorMessage);
	}
}
