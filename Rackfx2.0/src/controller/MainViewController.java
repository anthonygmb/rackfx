package controller;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.textfield.CustomTextField;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.Groupe;
import model.Organisateur;
import model.Personne;
import model.Rencontre;
import model.Titre;
import model.User;
import sql.CRUD;
import sql.HibernateSetUp;
import utilities.CryptEtDecrypt;

public final class MainViewController {

	/*
	 * =========================================================================
	 * GENERAL
	 */
	@FXML
	public TabPane tabpane_onglets;
	private Stage dialogStage;
	public boolean modifGroupe = false;
	private boolean modifRencontre = false;
	public boolean connectAdmin = false;
	public boolean connectUser = false;
	public String login_admin = "root";
	public String pwd_admin = "admin";
	public String salt = "[B@49396ed";

	/* Singleton */
	/** Instance unique pré-initialisée */
	private static MainViewController INSTANCE_MAIN_VIEW_CONTROLLER;

	/** Point d'accès pour l'instance unique du singleton */
	public static MainViewController getInstance() {
		return INSTANCE_MAIN_VIEW_CONTROLLER;
	}

	/**
	 * Constructeur appelé après la méthode <code>initialize</code>
	 */
	public MainViewController() {
	}

	/**
	 * Initialise la classe controller. Cette methode est appelé automatiquement
	 * après que le fichier fxml a été chargé. Elle initialise la tableview
	 * groupe avec ses colonnes, vide les détails de groupe et place un listener
	 * sur la tableview groupe. Idem pour la tableview rencontre. Elle fait
	 * appel à la méthode <code>showGroupeDetails</code> et
	 * <code>showEventDetails</code>
	 * 
	 * @throws InterruptedException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 */
	@FXML
	private void initialize() throws InterruptedException {

		INSTANCE_MAIN_VIEW_CONTROLLER = this;

		Session s = HibernateSetUp.getSession();
		FullTextSession fullTextSession = Search.getFullTextSession(s);
		fullTextSession.createIndexer().startAndWait();
		fullTextSession.close();

		/* récupération des listes de groupes, de rencontres et de users */
		tv_reper.setItems(MainApp.getInstance().getGroupeData());
		tv_planif.setItems(MainApp.getInstance().getRencontreData());
		tv_admin.setItems(MainApp.getInstance().getUserData());

		nom_groupeColumn.setCellValueFactory(cellData -> cellData.getValue().nom_groupeProperty());
		showGroupeDetails(null);
		tv_reper.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showGroupeDetails(newValue));

		col_event.setCellValueFactory(cellData -> cellData.getValue().nom_rencProperty());
		col_ville.setCellValueFactory(cellData -> cellData.getValue().ville_rencProperty());
		showEventDetails(null);
		tv_planif.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showEventDetails(newValue));
		tab_administration.setDisable(true);

		col_login.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
		col_mdp.setCellValueFactory(cellData -> cellData.getValue().mot_de_passeProperty());
		col_droits.setCellValueFactory(cellData -> cellData.getValue().droit_authProperty());
		ts_adm_user.setSelected(true);

		btn_new_groupe.setDisable(true);
		btn_new_event.setDisable(true);
		btn_supp_groupe.setDisable(true);
		btn_supp_event.setDisable(true);
	}

	/*
	 * =========================================================================
	 * ONGLET ACCUEIL
	 */
	@FXML
	private Tab tab_accueil;
	@FXML
	private CustomTextField cst_tf_search;
	@FXML
	private VBox vb_link = new VBox();

	@FXML
	private void searchBar() {
		vb_link.getChildren().clear();
		vb_link.setStyle("-fx-background-color: #353C42;");

		if (!cst_tf_search.getText().equals("")) {
			Session s = HibernateSetUp.getSession();
			FullTextSession fullTextSession = Search.getFullTextSession(s);
			Transaction tx = fullTextSession.beginTransaction();

			QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Groupe.class).get();
			org.apache.lucene.search.Query query = qb.keyword().onFields("nom_groupe").matching(cst_tf_search.getText())
					.createQuery();
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Groupe.class);
			@SuppressWarnings("unchecked")
			List<Groupe> result1 = hibQuery.list();

			qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Personne.class).get();
			query = qb.keyword().onFields("nom_membre", "prenom_membre").matching(cst_tf_search.getText())
					.createQuery();
			hibQuery = fullTextSession.createFullTextQuery(query, Personne.class);
			@SuppressWarnings("unchecked")
			List<Personne> result2 = hibQuery.list();

			qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Titre.class).get();
			query = qb.keyword().onFields("titre", "annee").matching(cst_tf_search.getText()).createQuery();
			hibQuery = fullTextSession.createFullTextQuery(query, Titre.class);
			@SuppressWarnings("unchecked")
			List<Titre> result3 = hibQuery.list();

			qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Rencontre.class).get();
			query = qb.keyword().onFields("nom_renc", "ville_renc", "periodicite_renc")
					.matching(cst_tf_search.getText()).createQuery();
			hibQuery = fullTextSession.createFullTextQuery(query, Rencontre.class);
			@SuppressWarnings("unchecked")
			List<Rencontre> result4 = hibQuery.list();

			qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Organisateur.class).get();
			query = qb.keyword().onFields("nom_orga", "prenom_orga", "entreprise_orga")
					.matching(cst_tf_search.getText()).createQuery();
			hibQuery = fullTextSession.createFullTextQuery(query, Organisateur.class);
			@SuppressWarnings("unchecked")
			List<Organisateur> result5 = hibQuery.list();

			// qb =
			// fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Representation.class).get();
			// query =
			// qb.keyword().onFields("heure_debut").matching(cst_tf_search.getText()).createQuery();
			// hibQuery = fullTextSession.createFullTextQuery(query,
			// Representation.class);
			// List<Representation> result6 = hibQuery.list();

			if (!result1.isEmpty() || !result2.isEmpty() || !result3.isEmpty() || !result4.isEmpty()
					|| !result5.isEmpty()) {
				vb_link.setStyle("-fx-background-color: #353C42;");
			}

			if (!result1.isEmpty()) {
				Label ctgr_groupe = new Label(" GROUPES:");
				vb_link.getChildren().add(ctgr_groupe);
				for (Groupe groupe : result1) {
					Hyperlink link_groupe = new Hyperlink(groupe.getNom_groupe());
					link_groupe.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							linkGroupe(groupe, 0);
						}
					});
					vb_link.getChildren().add(link_groupe);
				}
			}
			if (!result2.isEmpty()) {
				Line ligne = new Line(0, 0, 588, 0);
				Label ctgr_personne = new Label(" PERSONNES:");
				vb_link.getChildren().add(ligne);
				vb_link.getChildren().add(ctgr_personne);
				for (Personne personne : result2) {
					Hyperlink link_personne = new Hyperlink(personne.getNom_membre());
					Groupe groupeParent = personne.getGroupe();
					link_personne.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							linkGroupe(groupeParent, 1);
						}
					});
					vb_link.getChildren().add(link_personne);
				}
			}
			if (!result3.isEmpty()) {
				Line ligne = new Line(0, 0, 588, 0);
				Label ctgr_titre = new Label(" TITRES:");
				vb_link.getChildren().add(ligne);
				vb_link.getChildren().add(ctgr_titre);
				for (Titre titre : result3) {
					Hyperlink link_titre = new Hyperlink(titre.getTitre());
					Groupe groupeParent = titre.getGroupe();
					link_titre.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							linkGroupe(groupeParent, 2);
						}
					});
					vb_link.getChildren().add(link_titre);
				}
			}
			if (!result4.isEmpty()) {
				Line ligne = new Line(0, 0, 588, 0);
				Label ctgr_rencontre = new Label(" RENCONTRES:");
				vb_link.getChildren().add(ligne);
				vb_link.getChildren().add(ctgr_rencontre);
				for (Rencontre rencontre : result4) {
					Hyperlink link_rencontre = new Hyperlink(rencontre.getNom_renc());
					link_rencontre.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							linkEvent(rencontre, 0);
						}
					});
					vb_link.getChildren().add(link_rencontre);
				}

			}
			if (!result5.isEmpty()) {
				Line ligne = new Line(0, 0, 588, 0);
				Label ctgr_organisateur = new Label(" ORGANISATEURS:");
				vb_link.getChildren().add(ligne);
				vb_link.getChildren().add(ctgr_organisateur);
				for (Organisateur organisateur : result5) {
					Hyperlink link_organisateur = new Hyperlink(organisateur.getNom_orga());
					Rencontre rencontreParent = organisateur.getRencontre();
					link_organisateur.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							linkEvent(rencontreParent, 1);
						}
					});
					vb_link.getChildren().add(link_organisateur);
				}
			}
			// if (result6.isEmpty()) {
			// resultat1 += "REPRESENTATIONS :\nAucuns résultats\n";
			// } else {
			// for (Representation representation : result6) {
			// resultat1 += "REPRESENTATIONS :\n" +
			// representation.getHeure_debut()
			// + "\n";
			// }
			// }

			tx.commit();
			s.close();
		}
	}

	/**
	 * Appelé quand l'utilisateur clique sur le lien de la recherche de la
	 * fenetre principale. Fait appel à la méthode
	 * <code>showFicheGroupeEditDialog</code> Fait appel à la méthode
	 * <code>showGroupeDetails</code> Sauve le groupe en base de donnée via
	 * Hibernate.
	 * 
	 * @param groupeTemp
	 * @param tab
	 */
	private void linkGroupe(Groupe groupeTemp, int tab) {
		Groupe selectedGroupe = groupeTemp;
		modifGroupe = true;
		boolean okClicked = MainApp.getInstance().showFicheGroupeEditDialog(selectedGroupe, modifGroupe, tab);
		if (okClicked) {
			showGroupeDetails(selectedGroupe);
			CRUD.update(selectedGroupe);
		}
	}

	/**
	 * Appelé quand l'utilisateur clique sur le lien de la recherche de la
	 * fenetre principale. Fait appel à la méthode
	 * <code>showFicheEventEditDialog</code> Fait appel à la méthode
	 * <code>showEventDetails</code> Sauve la rencontre en base de donnée via
	 * Hibernate.
	 * 
	 * @param rencontreTemp
	 * @param tab
	 */
	private void linkEvent(Rencontre rencontreTemp, int tab) {
		Rencontre selectedRencontre = rencontreTemp;
		modifRencontre = true;
		boolean okClicked = MainApp.getInstance().showFicheEventEditDialog(selectedRencontre, modifRencontre, tab);
		if (okClicked) {
			showEventDetails(selectedRencontre);
			CRUD.update(selectedRencontre);
		}
	}

	/*
	 * =========================================================================
	 * ONGLET REPERTOIRE
	 */
	@FXML
	private Tab tab_repertoire;
	@FXML
	public TableView<Groupe> tv_reper;
	@FXML
	private TableColumn<Groupe, String> nom_groupeColumn;
	@FXML
	private Label lb_nom_groupe;
	@FXML
	private Label lb_carac_groupe;
	@FXML
	private Label lb_region_groupe;
	@FXML
	private Label lb_pays_groupe;
	@FXML
	public Button btn_new_groupe;
	@FXML
	private Button btn_edit_groupe;
	@FXML
	public Button btn_supp_groupe;

	/**
	 * Appelé quand l'utilisateur clique sur le bouton Nouveau de l'onglet
	 * repertoire de la fenetre principale. Fait appel à la méthode
	 * <code>showFicheGroupeEditDialog</code> Fait appel à la méthode
	 * <code>getGroupeData</code> Sauve le groupe en base de donnée via
	 * Hibernate.
	 * 
	 * @throws SQLException
	 */
	@FXML
	private void nouveauGroupe() throws SQLException {
		Groupe tempGroupe = new Groupe();
		modifGroupe = false;
		boolean okClicked = MainApp.getInstance().showFicheGroupeEditDialog(tempGroupe, modifGroupe, 0);
		if (okClicked) {
			MainApp.getInstance().getGroupeData().add(tempGroupe);
			CRUD.save(tempGroupe);
		}
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton Editer de l'onglet
	 * repertoire de la fenetre principale. Fait appel à la méthode
	 * <code>showFicheGroupeEditDialog</code> Fait appel à la méthode
	 * <code>showGroupeDetails</code> Necessite de selectionner un groupe dans
	 * la liste de groupes autrement un message d'erreur apparait.
	 */
	@FXML
	private void editGroupe() {
		Groupe selectedGroupe = tv_reper.getSelectionModel().getSelectedItem();
		if (selectedGroupe != null) {
			modifGroupe = true;
			boolean okClicked = MainApp.getInstance().showFicheGroupeEditDialog(selectedGroupe, modifGroupe, 0);
			if (okClicked) {
				showGroupeDetails(selectedGroupe);
				CRUD.update(selectedGroupe);
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(MainApp.getInstance().getPrimaryStage());
			alert.setTitle("Aucune sélection");
			alert.setHeaderText("Aucun groupe selectionné");
			alert.setContentText("Veuillez sélectionner un groupe dans la liste");
			alert.showAndWait();
		}
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton Supprimer de l'onglet
	 * repertoire de la fenetre principale. Necessite de selectionner un groupe
	 * dans la liste de groupes autrement un message d'erreur apparait.
	 * 
	 * @throws SQLException
	 */
	@FXML
	private void supprimerGroupe() throws SQLException {
		int selectedIndex = tv_reper.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation d'action");
		alert.setHeaderText("Confirmation de suppression");
		alert.setContentText(
				"Voulez-vous supprimer ce groupe ?\n\nN.B. tous les membres et titres\nlui appartenant seront également supprimé");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			
			Session s = HibernateSetUp.getSession();
			s.beginTransaction();
			
			Query query = s.createQuery("delete from Representation where groupe_joue = " + "'" + tv_reper.getSelectionModel().getSelectedItem().getNom_groupe() + "'");
			query.executeUpdate();
			s.getTransaction().commit();
			s.close();
			
			
			CRUD.delete(tv_reper.getSelectionModel().getSelectedItem());//TODO
			tv_reper.getItems().remove(selectedIndex);
		}
	}

	/**
	 * Methode appelée par la méthode <code>handleEditGroupe</code> pour
	 * renseigner les labels de l'onglet repertoire de la fenetre principale.
	 */
	private void showGroupeDetails(Groupe groupe) {
		if (groupe != null) {
			lb_nom_groupe.setText(groupe.getNom_groupe());
			lb_carac_groupe.setText(groupe.getCarac_groupe());
			lb_pays_groupe.setText(groupe.getPays_groupe());
			lb_region_groupe.setText(groupe.getRegion_groupe());
		} else {
			lb_nom_groupe.setText("");
			lb_carac_groupe.setText("");
			lb_pays_groupe.setText("");
			lb_region_groupe.setText("");
		}
	}

	/*
	 * =========================================================================
	 * ONGLET PLANIFICATION
	 */
	@FXML
	private Tab tab_planification;
	@FXML
	public TableView<Rencontre> tv_planif;
	@FXML
	private TableColumn<Rencontre, String> col_event;
	@FXML
	private TableColumn<Rencontre, String> col_ville;
	@FXML
	private Label lb_lieu;
	@FXML
	private Label lb_date_deb;
	@FXML
	private Label lb_date_fin;
	@FXML
	private Label lb_perio;
	@FXML
	private Label lb_nb_pers;
	@FXML
	public Button btn_new_event;
	@FXML
	private Button btn_edit_event;
	@FXML
	public Button btn_supp_event;

	/**
	 * Appelé quand l'utilisateur clique sur le bouton Nouveau de l'onglet
	 * planification de la fenetre principale. Fait appel à la méthode
	 * <code>showFicheEventEditDialog</code> Fait appel à la méthode
	 * <code>getRencontreData</code>
	 */
	@FXML
	private void nouvelEvent() {
		Rencontre tempRencontre = new Rencontre();
		modifRencontre = false;
		boolean okClicked = MainApp.getInstance().showFicheEventEditDialog(tempRencontre, modifRencontre, 0);
		if (okClicked) {
			MainApp.getInstance().getRencontreData().add(tempRencontre);
			CRUD.save(tempRencontre);
		}
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton Editer de l'onglet
	 * planification de la fenetre principale. Fait appel à la méthode
	 * <code>showFicheEventEditDialog</code> Fait appel à la méthode
	 * <code>showEventDetails</code> Necessite de selectionner une rencontre
	 * dans la liste de rencontres autrement un message d'erreur apparait.
	 */
	@FXML
	private void editEvent() {
		Rencontre selectedRencontre = tv_planif.getSelectionModel().getSelectedItem();
		if (selectedRencontre != null) {
			modifRencontre = true;
			boolean okClicked = MainApp.getInstance().showFicheEventEditDialog(selectedRencontre, modifRencontre, 0);
			if (okClicked) {
				showEventDetails(selectedRencontre);
				CRUD.update(selectedRencontre);
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(MainApp.getInstance().getPrimaryStage());
			alert.setTitle("Aucune sélection");
			alert.setHeaderText("Aucune rencontre selectionnée");
			alert.setContentText("Veuillez sélectionner une rencontre dans la liste");
			alert.showAndWait();
		}
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton Supprimer de l'onglet
	 * planification de la fenetre principale. Necessite de selectionner une
	 * rencontre dans la liste de rencontres autrement un message d'erreur
	 * apparait.
	 */
	@FXML
	private void supprimerEvent() {
		int selectedIndex = tv_planif.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation d'action");
		alert.setHeaderText("Confirmation de suppression");
		alert.setContentText(
				"Voulez-vous supprimer cette rencontre ?\n\nN.B. tous les organisateurs et représentations\nlui appartenant seront également supprimé");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			CRUD.delete(tv_planif.getSelectionModel().getSelectedItem());
			tv_planif.getItems().remove(selectedIndex);
		}
	}

	/**
	 * Methode appelée par la méthode <code>handleEditEvent</code> pour
	 * renseigner les labels de l'onglet planification de la fenetre principale.
	 */
	private void showEventDetails(Rencontre rencontre) {
		if (rencontre != null) {
			lb_lieu.setText(rencontre.getLieu_renc());
			lb_date_deb.setText(rencontre.getDate_deb_renc().toString());
			lb_date_fin.setText(rencontre.getDate_fin_renc().toString());
			lb_nb_pers.setText(Long.toString(rencontre.getNb_pers_attendues()));
			lb_perio.setText(rencontre.getPeriodicite_renc());
		} else {
			lb_lieu.setText("");
			lb_date_deb.setText("");
			lb_date_fin.setText("");
			lb_nb_pers.setText("");
			lb_perio.setText("");
		}
	}

	/*
	 * =========================================================================
	 * ONGLET ADMINISTRATION
	 */
	@FXML
	public Tab tab_administration;
	@FXML
	private TableView<User> tv_admin;
	@FXML
	private TableColumn<User, String> col_login;
	@FXML
	private TableColumn<User, String> col_mdp;
	@FXML
	private TableColumn<User, String> col_droits;
	@FXML
	private Button btn_admin_creer;
	@FXML
	private Button btn_admin_annuler;
	@FXML
	private Button btn_admin_supp;
	@FXML
	private TextField tf_admin_login;
	@FXML
	private TextField tf_admin_mdp;
	@FXML
	private ToggleSwitch ts_adm_user;
	private User user;

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Renseigne les attributs de l'objet User. Elle fait appel à la méthode
	 * <code>isInputValidUser</code> et la methode <code>annulerUser</code>
	 * 
	 * @throws ParseException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 */
	@FXML
	private void creerModifierUser() throws NoSuchAlgorithmException, NoSuchProviderException {
		if (isInputValidUser()) {
			user = new User();
			user.setLogin(tf_admin_login.getText());
			user.setMot_de_passe(CryptEtDecrypt.getSecurePassword(tf_admin_mdp.getText(), salt));
			if (!ts_adm_user.isSelected()) {
				user.setDroit_auth("administrateur");
			} else {
				user.setDroit_auth("utilisateur");
			}
			tv_admin.getItems().add(user);
			CRUD.save(user);
			annulerUser();
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Annuler.
	 * Réinitialise les champs et les composants.
	 */
	@FXML
	private void annulerUser() {
		tf_admin_login.clear();
		tf_admin_mdp.clear();
		if (!ts_adm_user.isSelected()) {
			ts_adm_user.setSelected(true);
		}
		tv_admin.getSelectionModel().clearSelection();
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton supprimer. Supprime
	 * l'utilisateur dans la liste à l'index sélectionné.
	 */
	@FXML
	private void supprimerUser() {
		int selectedIndex = tv_admin.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation d'action");
		alert.setHeaderText("Confirmation de suppression");

		if (tv_admin.getSelectionModel().getSelectedItem().getLogin()
				.equals(RootLayoutController.getInstance().tf_login.getText())) {
			alert.setContentText(
					"Attention, ce login est actuellement utilisé\n" + "Voulez-vous supprimer cet utilisateur ?\n"
							+ "Après la suppression vous serez déconnecté\n" + "et ne pourrez plus vous connecter");
		} else {
			alert.setContentText("Voulez-vous supprimer cet utilisateur ?");
		}
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			CRUD.delete(tv_admin.getSelectionModel().getSelectedItem());
			tv_admin.getItems().remove(selectedIndex);
			annulerUser();
			RootLayoutController.getInstance().deconnection();
		}
	}

	/**
	 * Methode de vérification des champs obligatoires.
	 *
	 * @return true si les entrée son valides
	 */
	private boolean isInputValidUser() {
		String errorMessage = "";
		if (tf_admin_login.getText() == null || tf_admin_login.getText().length() == 0) {
			errorMessage += "Veuillez entrer un login!\n";
		}
		if (tf_admin_mdp.getText() == null || tf_admin_mdp.getText().length() == 0) {
			errorMessage += "Veuillez entrer un login!\n";
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Erreur");
			alert.setHeaderText("Informations obligatoires requises");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
		}
	}
}
