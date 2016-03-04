//package controller;
//
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.MenuBar;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import utilities.CryptEtDecrypt;
//
//public final class RootLayoutController {
//
//	/* Singleton */
//	/** Instance unique pré-initialisée */
//	private static RootLayoutController INSTANCE_ROOT_CONTROLLER;
//
//	/** Point d'accès pour l'instance unique du singleton */
//	public static RootLayoutController getInstance() {
//		return INSTANCE_ROOT_CONTROLLER;
//	}
//
//	public RootLayoutController() {
//	}
//
//	/*
//	 * =========================================================================
//	 * BANDEAU DE CONNECTION
//	 */
//
////	@FXML
////	private MenuBar mnb_menu;
////	@FXML
////	private Pane pane_bandeau;
////	@FXML
////	public TextField tf_login;
////	@FXML
////	private PasswordField pwf_mdp;
////	@FXML
////	private Button btn_connect;
////	@FXML
////	private Label lb_connection;
//
////	/**
////	 * Methode de connection à la session admin ou user. Elle est appelée
////	 * lorsque l'on clique sur le bouton connection du bandeau. Vérifie si le
////	 * login et le mot de passe sont conforme. Fait appel à la méthode
////	 * <code>deconnection</code>
////	 * 
////	 * @throws NoSuchProviderException
////	 * @throws NoSuchAlgorithmException
////	 */
////	@FXML
////	private void connection() {
////		if (btn_connect.getText().equals("Connection")) {
////			if (tf_login.getText().equals(MainViewController.getInstance().login_admin)
////					&& pwf_mdp.getText().equals(MainViewController.getInstance().pwd_admin)) {
////				MainViewController.getInstance().connectAdmin = true;
////			}
////
////			/* Test de connection */
////			for (int i = 0; i < MainApp.getInstance().getUserData().size(); i++) {
////				if (tf_login.getText().equals(MainApp.getInstance().getUserData().get(i).getLogin())
////						&& CryptEtDecrypt.getSecurePassword(pwf_mdp.getText(), MainViewController.getInstance().salt)
////								.equals(MainApp.getInstance().getUserData().get(i).getMot_de_passe())) {
////
////					if (MainApp.getInstance().getUserData().get(i).getDroit_auth().equals("administrateur")) {
////						MainViewController.getInstance().connectAdmin = true;
////					} else {
////						MainViewController.getInstance().connectUser = true;
////					}
////				}
////			}
////
////			/* Cas si l'utilisateur est connecté */
////			if (MainViewController.getInstance().connectAdmin || MainViewController.getInstance().connectUser) {
////				btn_connect.setText("Deconnection");
////				if (tf_login.getText().equals(MainViewController.getInstance().login_admin)
////						&& btn_connect.getText().equals("Deconnection")) {
////					lb_connection.setText("Connecté en temps que [super admin] ");
////				} else {
////					if (MainViewController.getInstance().connectUser) {
////						lb_connection.setText("Connecté en temps que [user] ");
////					} else {
////						lb_connection.setText("Connecté en temps que [admin] ");
////					}
////				}
////				lb_connection.setTextFill(Color.web("#DEFFFF"));
////				pwf_mdp.setStyle("-fx-text-inner-color: #000000;");
////				tf_login.setStyle("-fx-text-inner-color: #000000;");
////				tf_login.setDisable(true);
////				pwf_mdp.setDisable(true);
////				pwf_mdp.clear();
////				MainViewController.getInstance().tab_administration
////						.setDisable((MainViewController.getInstance().connectAdmin) ? false : true);
////				MainViewController.getInstance().btn_new_groupe.setDisable(false);
////				MainViewController.getInstance().btn_new_event.setDisable(false);
////				MainViewController.getInstance().btn_supp_groupe
////						.setDisable((MainViewController.getInstance().connectAdmin) ? false : true);
////				MainViewController.getInstance().btn_supp_event
////						.setDisable((MainViewController.getInstance().connectAdmin) ? false : true);
////			} else {
////				/* Cas si le login ou le mot de passe est faux */
////				pwf_mdp.setStyle("-fx-text-inner-color: dc320c;");
////				tf_login.setStyle("-fx-text-inner-color: dc320c;");
////				lb_connection.setText("Erreur de connection");
////				lb_connection.setTextFill(Color.web("#dc320c"));
////				MainViewController.getInstance().connectAdmin = false;
////			}
////		} else {
////			deconnection();
////		}
////	}
////
////	/**
////	 * Methode de deconnection
////	 */
////	public void deconnection() {
////		MainViewController.getInstance().tab_administration.setDisable(true);
////		MainViewController.getInstance().tabpane_onglets.getSelectionModel().select(0);
////		lb_connection.setText("");
////		btn_connect.setText("Connection");
////		tf_login.setDisable(false);
////		pwf_mdp.setDisable(false);
////		tf_login.clear();
////		pwf_mdp.clear();
////		MainViewController.getInstance().connectAdmin = false;
////		MainViewController.getInstance().connectUser = false;
////		MainViewController.getInstance().btn_new_groupe.setDisable(true);
////		MainViewController.getInstance().btn_new_event.setDisable(true);
////		MainViewController.getInstance().btn_supp_groupe.setDisable(true);
////		MainViewController.getInstance().btn_supp_event.setDisable(true);
////	}
//}
