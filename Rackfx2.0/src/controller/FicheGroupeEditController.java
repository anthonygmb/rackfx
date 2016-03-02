package controller;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.hibernate.Session;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.LocalTimePicker;
import model.Groupe;
import model.Personne;
import model.Rencontre;
import model.Titre;
import sql.CRUD;
import sql.HibernateSetUp;

public class FicheGroupeEditController {
	/*
	 * =========================================================================
	 * GENERAL
	 */
	@FXML
	private TabPane tp_fiche_groupe;
	private Stage dialogStage;
	private boolean okClicked = false;
	private boolean modifMembre = false;
	private boolean modifTitre = false;
	private String telNumber = "";
	private String faxNumber = "";
	private DateFormat formatAnnee = new SimpleDateFormat("yyyy");
	private Date auj = new Date();
	private String annee = "";
	private ObservableList<Rencontre> rencontreDataTri = FXCollections.observableArrayList();

	/**
	 * Constructeur.
	 */
	public FicheGroupeEditController() {
	}

	/* Singleton */
	/** Instance unique pré-initialisée */
	private static FicheGroupeEditController INSTANCE_FICHE_GROUPE_CONTROLLER;

	/** Point d'accès pour l'instance unique du singleton */
	public static FicheGroupeEditController getInstance() {
		return INSTANCE_FICHE_GROUPE_CONTROLLER;
	}

	/**
	 * Initialise la classe controller. Cette methode est appelé automatiquement
	 * après que le fichier fxml a été chargé.
	 */
	@FXML
	private void initialize() {
		INSTANCE_FICHE_GROUPE_CONTROLLER = this;

		/*
		 * récupération de la liste de personnes et de titres si le groupe est
		 * existant ou pas
		 */
		if (MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem() != null) {
			personneData.addAll(CRUD.getAllWhere("Personne", "groupeId",
					MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem().getGroupeId()));
			titreData.addAll(CRUD.getAllWhere("Titre", "groupeId",
					MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem().getGroupeId()));
		}
		cmbox_membre.setItems(personneData);
		tbv_titre.getItems().addAll(titreData);

		/* formatte la combobox pour qu'elle affiche le texte voulu */
		cmbox_membre.setButtonCell(new ListCell<Personne>() {
			@Override
			protected void updateItem(Personne item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				if (!empty && item != null) {
					final String text = String.format("%s %s", item.getNom_membre(), item.getPrenom_membre());
					setText(text);
				}
			}
		});
		cmbox_membre.setCellFactory(new Callback<ListView<Personne>, ListCell<Personne>>() {
			@Override
			public ListCell<Personne> call(ListView<Personne> param) {
				return new ListCell<Personne>() {

					@Override
					protected void updateItem(Personne personne, boolean empty) {
						super.updateItem(personne, empty);
						setText(null);
						if (!empty && personne != null) {
							final String text = String.format("%s %s", personne.getNom_membre(),
									personne.getPrenom_membre());
							setText(text);
						}
					}
				};
			}
		});

		/* valeures par defaut de l'onglet membre */
		cmbox_spe_membre.getItems().addAll(spe_membre);
		cmbox_instru_membre.getItems().addAll(instru_membre);
		cmbox_respon_membre.getItems().addAll(respon_membre);
		annulerPersonne();

		/* formatte le textfield au format numéro de téléphone */
		tf_tel_cor.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*") && newValue.length() < 13) {
					telNumber = newValue;
				} else {
					tf_tel_cor.setText(oldValue);
				}
			}
		});
		tf_tel_cor.positionCaret(tf_tel_cor.getLength());

		/* formatte le textfield au format numéro de téléphone */
		tf_fax_cor.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*") && newValue.length() < 13) {
					faxNumber = newValue;
				} else {
					tf_fax_cor.setText(oldValue);
				}
			}
		});
		tf_fax_cor.positionCaret(tf_fax_cor.getLength());

		/* formatte le tableau de titres */
		col_titre_titre.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
		col_annee_titre.setCellValueFactory(cellData -> cellData.getValue().anneeProperty());
		col_duree_titre.setCellValueFactory(cellData -> cellData.getValue().dureeProperty());
		tbv_titre.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> setTitre());

		/* valeures par defaut de l'onglet titre */
		annulerTitre();

		/*
		 * formatte le textfield pour qu'il accepte uniquement quatre chiffres
		 * et la date ne peut pas depasser l'année actuelle
		 */
		String currentYear = formatAnnee.format(auj);
		tf_annee_titre.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.matches("\\d*") && newValue.length() <= 4 && newValue.compareTo(currentYear) <= 0) {
					annee = newValue;
				} else {
					tf_annee_titre.setText(oldValue);
				}
			}
		});
		tf_annee_titre.positionCaret(tf_annee_titre.getLength());

		btn_creer_groupe.setDisable(true);
		btn_creer_membre.setDisable(true);
		btn_supp_membre.setDisable(true);
		btn_creer_titre.setDisable(true);
		btn_supp_titre.setDisable(true);

		if (MainViewController.getInstance().connectAdmin) {
			btn_creer_groupe.setDisable(false);
			btn_creer_membre.setDisable(false);
			btn_supp_membre.setDisable(false);
			btn_creer_titre.setDisable(false);
			btn_supp_titre.setDisable(false);
		} else if (MainViewController.getInstance().connectUser) {
			btn_creer_groupe.setDisable(false);
			btn_creer_membre.setDisable(false);
			btn_creer_titre.setDisable(false);
		}

		/* formatte le tableau d'événement futurs */
		col_event_event_f.setCellValueFactory(cellData -> cellData.getValue().nom_rencProperty());
		col_ville_event_f.setCellValueFactory(cellData -> cellData.getValue().ville_rencProperty());
		col_deb_event_f.setCellValueFactory(cellData -> cellData.getValue().date_deb_rencProperty());
		col_fin_event_f.setCellValueFactory(cellData -> cellData.getValue().date_fin_rencProperty());
		
		/* formatte le tableau d'événement passés */
		col_event_event_p.setCellValueFactory(cellData -> cellData.getValue().nom_rencProperty());
		col_ville_event_p.setCellValueFactory(cellData -> cellData.getValue().ville_rencProperty());
		col_deb_event_p.setCellValueFactory(cellData -> cellData.getValue().date_deb_rencProperty());
		col_fin_event_p.setCellValueFactory(cellData -> cellData.getValue().date_fin_rencProperty());

		/*
		 * récupération de la liste de rencontres pour les placer dans les
		 * tableaux d'événements futurs et passés
		 */
//		if (MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem() != null) {
//			
//			// if
//			// (!MainViewController.getInstance().tv_planif.getSelectionModel().isEmpty()
//			// &&
//			// !MainViewController.getInstance().tv_reper.getSelectionModel().isEmpty())
//			// {
//			rencontreDataTri.addAll(CRUD.getAllWhere("Rencontre", "groupeId",
//					MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem().getGroupeId()));
//
//			for (Rencontre rencTri : rencontreDataTri) {
//				if (rencTri.getDate_fin_renc().getTime() > auj.getTime()) {
//					rencontreDataF.add(rencTri);
//				} else {
//					rencontreDataP.add(rencTri);
//				}
//			}
//		}
//		tbv_event_f.getItems().addAll(rencontreDataF);
//		tbv_event_p.getItems().addAll(rencontreDataP);
	}

	/**
	 * Methode pour afficher ou cacher les onglets autres que l'onglet
	 * information.
	 * 
	 * @param modif
	 */
	public void geleTab(boolean modif) {
		if (!modif) {
			tab_membres_groupe.setDisable(true);
			tab_titres_groupe.setDisable(true);
			tab_event_f_groupe.setDisable(true);
			tab_event_p_groupe.setDisable(true);
		} else {
			tab_membres_groupe.setDisable(false);
			tab_titres_groupe.setDisable(false);
			tab_event_f_groupe.setDisable(false);
			tab_event_p_groupe.setDisable(false);
		}
	}

	/**
	 * Importe stage dans cette fenetre.
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/*
	 * =========================================================================
	 * ONGLET INFORMATIONS
	 */
	@FXML
	private Tab tab_infos_groupe;
	@FXML
	private TextField tf_nom_groupe;
	@FXML
	private TextField tf_carac_groupe;
	@FXML
	private TextField tf_region_groupe;
	private ObservableList<String> pays_groupe = FXCollections.observableArrayList("Afghanistan", "Afrique du Sud",
			"Akrotiri", "Albanie", "Algérie", "Allemagne", "Andorre", "Angola", "Anguilla", "Antarctique",
			"Antigua-et-Barbuda", "Antilles néerlandaises", "Arabie saoudite", "Arctic Ocean", "Argentine", "Arménie",
			"Aruba", "Ashmore and Cartier Islands", "Atlantic Ocean", "Australie", "Autriche", "Azerbaïdjan", "Bahamas",
			"Bahreïn", "Bangladesh", "Barbade", "Belau", "Belgique", "Belize", "Bénin", "Bermudes", "Bhoutan",
			"Biélorussie", "Birmanie", "Bolivie", "Bosnie-Herzégovine", "Botswana", "Brésil", "Brunei", "Bulgarie",
			"Burkina Faso", "Burundi", "Cambodge", "Cameroun", "Canada", "Cap-Vert", "Chili", "Chine", "Chypre",
			"Clipperton Island", "Colombie", "Comores", "Congo", "Coral Sea Islands", "Corée du Nord", "Corée du Sud",
			"Costa Rica", "Côte d'Ivoire", "Croatie", "Cuba", "Danemark", "Dhekelia", "Djibouti", "Dominique", "Égypte",
			"Émirats arabes unis", "Équateur", "Érythrée", "Espagne", "Estonie", "États-Unis", "Éthiopie",
			"ex-République yougoslave de Macédoine", "Finlande", "France", "Gabon", "Gambie", "Gaza Strip", "Géorgie",
			"Ghana", "Gibraltar", "Grèce", "Grenade", "Groenland", "Guam", "Guatemala", "Guernsey", "Guinée",
			"Guinée équatoriale", "Guinée-Bissao", "Guyana", "Haïti", "Honduras", "Hong Kong", "Hongrie", "Ile Bouvet",
			"Ile Christmas", "Ile Norfolk", "Iles Cayman", "Iles Cook", "Iles des Cocos (Keeling)", "Iles Falkland",
			"Iles Féroé", "Iles Fidji", "Iles Géorgie du Sud et Sandwich du Sud", "Iles Heard et McDonald",
			"Iles Marshall", "Iles Pitcairn", "Iles Salomon", "Iles Svalbard et Jan Mayen", "Iles Turks-et-Caicos",
			"Iles Vierges américaines", "Iles Vierges britanniques", "Inde", "Indian Ocean", "Indonésie", "Iran",
			"Iraq", "Irlande", "Islande", "Israël", "Italie", "Jamaïque", "Jan Mayen", "Japon", "Jersey", "Jordanie",
			"Kazakhstan", "Kenya", "Kirghizistan", "Kiribati", "Koweït", "Laos", "Lesotho", "Lettonie", "Liban",
			"Liberia", "Libye", "Liechtenstein", "Lituanie", "Luxembourg", "Macao", "Madagascar", "Malaisie", "Malawi",
			"Maldives", "Mali", "Malte", "Man, Isle of", "Mariannes du Nord", "Maroc", "Maurice", "Mauritanie",
			"Mayotte", "Mexique", "Micronésie", "Moldavie", "Monaco", "Monde", "Mongolie", "Monténégro", "Montserrat",
			"Mozambique", "Namibie", "Nauru", "Navassa Island", "Népal", "Nicaragua", "Niger", "Nigeria", "Nioué",
			"Norvège", "Nouvelle-Calédonie", "Nouvelle-Zélande", "Oman", "Ouganda", "Ouzbékistan", "Pacific Ocean",
			"Pakistan", "Panama", "Papouasie-Nouvelle-Guinée", "Paracel Islands", "Paraguay", "Pays-Bas", "Pérou",
			"Philippines", "Pologne", "Polynésie française", "Porto Rico", "Portugal", "Qatar",
			"République centrafricaine", "République démocratique du Congo", "République dominicaine",
			"République tchèque", "Roumanie", "Royaume-Uni", "Russie", "Rwanda", "Sahara occidental",
			"Saint-Christophe-et-Niévès", "Sainte-Hélène", "Sainte-Lucie", "Saint-Marin", "Saint-Pierre-et-Miquelon",
			"Saint-Siège", "Saint-Vincent-et-les-Grenadines", "Salvador", "Samoa", "Samoa américaines",
			"Sao Tomé-et-Principe", "Sénégal", "Serbie", "Seychelles", "Sierra Leone", "Singapour", "Slovaquie",
			"Slovénie", "Somalie", "Soudan", "Southern Ocean", "Spratly Islands", "Sri Lanka", "Suède", "Suisse",
			"Suriname", "Swaziland", "Syrie", "Tadjikistan", "Taïwan", "Tanzanie", "Tchad",
			"Terres australes françaises", "Territoire britannique de l'Océan Indien", "Thaïlande", "Timor Oriental",
			"Togo", "Tokélaou", "Tonga", "Trinité-et-Tobago", "Tunisie", "Turkménistan", "Turquie", "Tuvalu", "Ukraine",
			"Union européenne", "Uruguay", "Vanuatu", "Venezuela", "Viêt Nam", "Wake Island", "Wallis-et-Futuna",
			"West Bank", "Yémen", "Zambie", "Zimbabwe");
	@FXML
	private ComboBox<String> cmbox_pays_groupe = new ComboBox<>(pays_groupe);
	@FXML
	private Button btn_creer_groupe;
	@FXML
	private Button btn_annuler_groupe;
	private Groupe groupe;

	/**
	 * renseigne la fenetre d'édition de groupe avec le groupe passé en
	 * parametre.
	 * 
	 * @param groupe
	 * @param modif
	 * @param tab
	 */
	public void setGroupe(Groupe groupe, boolean modif, int tab) {
		tp_fiche_groupe.getSelectionModel().select(tab);
		this.groupe = groupe;
		tf_nom_groupe.setText(groupe.getNom_groupe());
		tf_carac_groupe.setText(groupe.getCarac_groupe());
		if (modif) {
			if (groupe.getPays_groupe().equals("")) {
				cmbox_pays_groupe.getSelectionModel().clearSelection();
			} else {
				cmbox_pays_groupe.getSelectionModel().select(groupe.getPays_groupe());
			}
		}
		cmbox_pays_groupe.getItems().addAll(pays_groupe);
		tf_region_groupe.setText(groupe.getRegion_groupe());
		btn_creer_groupe.setText((modif) ? "Appliquer" : "Créer");
	}

	/**
	 * retourne true si l'utilisateur clique OK retourne false sinon
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Renseigne les attributs de l'objet Groupe et passe le booleen
	 * <code>okClicked</code> à true. Elle fait appel à la méthode
	 * <code>isInputValid</code>
	 */
	@FXML
	private void creerModifierGroupe() {
		if (isInputValidGroupe()) {
			groupe.setNom_groupe(tf_nom_groupe.getText());
			groupe.setCarac_groupe(tf_carac_groupe.getText());
			if (cmbox_pays_groupe.getSelectionModel().isEmpty()) {
				groupe.setPays_groupe("");
			} else {
				groupe.setPays_groupe(cmbox_pays_groupe.getSelectionModel().getSelectedItem().toString());
			}
			groupe.setRegion_groupe(tf_region_groupe.getText());
			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Annuler.
	 * Ferme la fenetre sans enregistrer les modifications.
	 */
	@FXML
	private void annulerGroupe() {
		dialogStage.close();
	}

	/**
	 * Methode de vérification des champs obligatoires.
	 *
	 * @return true si les entrée son valides
	 */
	private boolean isInputValidGroupe() {
		String errorMessage = "";
		if (tf_nom_groupe.getText() == null || tf_nom_groupe.getText().length() == 0) {
			errorMessage += "Veuillez entrer un nom de groupe!\n";
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

	/*
	 * =========================================================================
	 * ONGLET MEMBRES
	 */
	@FXML
	private Tab tab_membres_groupe;
	@FXML
	private CheckBox ckbox_mme_civi_membre;
	@FXML
	private CheckBox ckbox_mr_civi_membre;
	@FXML
	private TextField tf_nom_membre;
	@FXML
	private TextField tf_prenom_membre;
	@FXML
	private TextField tf_adress_cor;
	ObservableList<String> spe_membre = FXCollections.observableArrayList("Pas de spécialité", "Choriste", "Soliste",
			"Musicien", "Chanteur");
	@FXML
	private ComboBox<String> cmbox_spe_membre = new ComboBox<>(spe_membre);
	ObservableList<String> instru_membre = FXCollections.observableArrayList("Pas d'instrument", "Guitare", "Basse",
			"Saxophone", "violon", "cornemuse", "etc...");
	@FXML
	private ComboBox<String> cmbox_instru_membre = new ComboBox<>(instru_membre);
	ObservableList<String> respon_membre = FXCollections.observableArrayList("Pas de responsabilité", "Chauffeur",
			"Habilleur", "dirigeant", "trésorier", "etc...");
	@FXML
	private ComboBox<String> cmbox_respon_membre = new ComboBox<>(respon_membre);
	@FXML
	private TextField tf_tel_cor;
	@FXML
	private TextField tf_fax_cor;
	@FXML
	private TextField tf_mail_cor;
	@FXML
	private CheckBox ckbox_corres_membre;
	@FXML
	private DatePicker dtp_date_naiss_membre;
	private ObservableList<Personne> personneData = FXCollections.observableArrayList();
	@FXML
	private ComboBox<Personne> cmbox_membre = new ComboBox<>(personneData);
	@FXML
	private Button btn_creer_membre;
	@FXML
	private Button btn_annuler_membre;
	@FXML
	private Button btn_supp_membre;
	private Personne personne;

	/**
	 * Methode sur la checkBox Monsieur, si la case Monsieur est cochée, Madamme
	 * est décochée.
	 */
	@FXML
	private void setCiviliteMr() {
		if (ckbox_mr_civi_membre.isSelected()) {
			ckbox_mme_civi_membre.setSelected(false);
		}
	}

	/**
	 * Methode sur la checkBox Madamme, si la case Madamme est cochée, Monsieur
	 * est décochée.
	 */
	@FXML
	private void setCiviliteMme() {
		if (ckbox_mme_civi_membre.isSelected()) {
			ckbox_mr_civi_membre.setSelected(false);
		}
	}

	/**
	 * Methode sur la checkBox correspondant, si la case est cochée, les
	 * textfields de correspondants sont désactivés. Fait appel à la méthode
	 * <code>activeCorrespondant</code>
	 */
	@FXML
	private void isCorrespondant() {
		if (!ckbox_corres_membre.isSelected()) {
			activeCorrespondant(false);
		} else {
			activeCorrespondant(true);
		}
	}

	/**
	 * Methode pour activer / désactiver les champs de texte correspondant et
	 * vider leurs contenus.
	 * 
	 * @param active
	 */
	private void activeCorrespondant(boolean active) {
		if (!active) {
			tf_adress_cor.setDisable(true);
			tf_tel_cor.setDisable(true);
			tf_fax_cor.setDisable(true);
			tf_mail_cor.setDisable(true);
		} else {
			tf_adress_cor.setDisable(false);
			tf_tel_cor.setDisable(false);
			tf_fax_cor.setDisable(false);
			tf_mail_cor.setDisable(false);
		}
		tf_adress_cor.clear();
		tf_tel_cor.clear();
		tf_fax_cor.clear();
		tf_mail_cor.clear();
	}

	/**
	 * renseigne la fenetre d'édition de la personne.
	 */
	@FXML
	public void setPersonne() {
		if (cmbox_membre.getSelectionModel().getSelectedItem() == null) {
			annulerPersonne();
		} else {
			modifMembre = true;
			personne = cmbox_membre.getSelectionModel().getSelectedItem();
			tf_nom_membre.setText(personne.getNom_membre());
			tf_prenom_membre.setText(personne.getPrenom_membre());
			dtp_date_naiss_membre.setValue(personne.getDate_naiss_membre().toLocalDate());
			if (personne.getCivi_membre().equals("Monsieur")) {
				ckbox_mr_civi_membre.setSelected(true);
				ckbox_mme_civi_membre.setSelected(false);
			} else {
				ckbox_mme_civi_membre.setSelected(true);
				ckbox_mr_civi_membre.setSelected(false);
			}

			if (personne.getSpe_membre().equals("")) {
				cmbox_spe_membre.getSelectionModel().clearSelection();
			} else {
				cmbox_spe_membre.getSelectionModel().select(personne.getSpe_membre());
			}

			if (personne.getInstru_membre().equals("")) {
				cmbox_instru_membre.getSelectionModel().clearSelection();
			} else {
				cmbox_instru_membre.getSelectionModel().select(personne.getInstru_membre());
			}

			if (personne.getRespon_membre().equals("")) {
				cmbox_respon_membre.getSelectionModel().clearSelection();
			} else {
				cmbox_respon_membre.getSelectionModel().select(personne.getRespon_membre());
			}

			if (personne.getCorrespondant() == true) {
				ckbox_corres_membre.setSelected(true);
				activeCorrespondant(true);
				tf_adress_cor.setText(personne.getAdresse_cor());

				if (personne.getTel_cor() == 0) {
					tf_tel_cor.clear();
				} else {
					tf_tel_cor.setText(String.valueOf(personne.getTel_cor()));
				}
				if (personne.getFax_cor() == 0) {
					tf_fax_cor.clear();
				} else {
					tf_fax_cor.setText(String.valueOf(personne.getFax_cor()));
				}

				tf_mail_cor.setText(personne.getMail_cor());
			} else {
				ckbox_corres_membre.setSelected(false);
				activeCorrespondant(false);
			}

			btn_creer_membre.setText((modifMembre) ? "Appliquer" : "Créer");
			btn_supp_membre.setDisable((MainViewController.getInstance().connectAdmin) ? false : true);
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Renseigne les attributs de l'objet Personne. Elle fait appel à la méthode
	 * <code>isInputValidPersonne</code> et la methode
	 * <code>annulerPersonne</code>
	 */
	@FXML
	private void creerModifierPersonne() {
		if (isInputValidPersonne()) {
			if (cmbox_membre.getSelectionModel().getSelectedItem() == null) {
				personne = new Personne();
			} else {
				personne = cmbox_membre.getSelectionModel().getSelectedItem();
			}
			personne.setNom_membre(tf_nom_membre.getText());
			personne.setPrenom_membre(tf_prenom_membre.getText());
			personne.setDate_naiss_membre(java.sql.Date.valueOf(dtp_date_naiss_membre.getValue()));
			if (ckbox_mr_civi_membre.isSelected()) {
				personne.setCivi_membre("Monsieur");
			} else {
				personne.setCivi_membre("Madamme");
			}
			if (cmbox_spe_membre.getSelectionModel().getSelectedItem() == null) {
				personne.setSpe_membre("");
			} else {
				personne.setSpe_membre(cmbox_spe_membre.getSelectionModel().getSelectedItem());
			}
			if (cmbox_instru_membre.getSelectionModel().getSelectedItem() == null) {
				personne.setInstru_membre("");
			} else {
				personne.setInstru_membre(cmbox_instru_membre.getSelectionModel().getSelectedItem());
			}
			if (cmbox_respon_membre.getSelectionModel().getSelectedItem() == null) {
				personne.setRespon_membre("");
			} else {
				personne.setRespon_membre(cmbox_respon_membre.getSelectionModel().getSelectedItem());
			}
			if (ckbox_corres_membre.isSelected()) {
				personne.setCorrespondant(true);
				personne.setAdresse_cor(tf_adress_cor.getText());
				if (telNumber.equals("")) {
					personne.setTel_cor((long) 0);
				} else {
					personne.setTel_cor(Long.parseLong(telNumber));
				}
				if (faxNumber.equals("")) {
					personne.setFax_cor((long) 0);
				} else {
					personne.setFax_cor(Long.parseLong(faxNumber));
				}
				personne.setMail_cor(tf_mail_cor.getText());
			} else {
				personne.setCorrespondant(false);
			}
			if (cmbox_membre.getSelectionModel().getSelectedItem() == null) {
				cmbox_membre.getItems().add(personne);
				Session s = HibernateSetUp.getSession();
				s.beginTransaction();
				Groupe groupeH = (Groupe) s.load(Groupe.class,
						MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem().getGroupeId());
				personne.setGroupe(groupeH);
				groupeH.getListe_personne().add(personne);
				s.save(personne);
				s.getTransaction().commit();
				s.close();
			} else {
				cmbox_membre.getItems().set(cmbox_membre.getSelectionModel().getSelectedIndex(), personne);
				CRUD.update(personne);
			}
			annulerPersonne();
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Annuler.
	 * Réinitialise les champs et les composants. Fait appel à la methode
	 * <code>activeCorrespondant</code>
	 */
	@FXML
	private void annulerPersonne() {
		tf_nom_membre.clear();
		tf_prenom_membre.clear();
		dtp_date_naiss_membre.setValue(LocalDate.now());
		ckbox_mr_civi_membre.setSelected(true);
		ckbox_mme_civi_membre.setSelected(false);
		cmbox_spe_membre.getSelectionModel().clearSelection();
		cmbox_instru_membre.getSelectionModel().clearSelection();
		cmbox_respon_membre.getSelectionModel().clearSelection();
		ckbox_corres_membre.setSelected(true);
		activeCorrespondant(true);
		cmbox_membre.getSelectionModel().clearSelection();
		btn_supp_membre.setDisable(true);
		modifMembre = false;
		btn_creer_membre.setText((modifMembre) ? "Appliquer" : "Créer");
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton supprimer. Supprime la
	 * personne dans la liste à l'index sélectionné.
	 */
	@FXML
	private void supprimerPersonne() {
		int selectedIndex = cmbox_membre.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation d'action");
		alert.setHeaderText("Confirmation de suppression");
		alert.setContentText("Voulez-vous supprimer ce membre ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			CRUD.delete(cmbox_membre.getSelectionModel().getSelectedItem());
			cmbox_membre.getItems().remove(selectedIndex);
			annulerPersonne();
		}
	}

	/**
	 * Methode de vérification des champs obligatoires.
	 *
	 * @return true si les entrée son valides
	 */
	private boolean isInputValidPersonne() {
		String errorMessage = "";
		if (tf_nom_membre.getText() == null || tf_nom_membre.getText().length() == 0) {
			errorMessage += "Veuillez entrer un nom de membre!\n";
		}
		if (tf_prenom_membre.getText() == null || tf_prenom_membre.getText().length() == 0) {
			errorMessage += "Veuillez entrer un prénom de membre!\n";
		}
		if (dtp_date_naiss_membre.getValue().isEqual(LocalDate.now())
				|| dtp_date_naiss_membre.getValue().isAfter(LocalDate.now())) {
			errorMessage += "Veuillez entrer une date inférieure à aujourd'hui!\n";
		}
		if (ckbox_corres_membre.isSelected()) {
			if ((!tf_mail_cor.getText().contains("@")) || tf_mail_cor.getLength() < 6) {
				errorMessage += "Veuillez entrer une adresse mail valide!\n";
			}
			if (telNumber.length() < 10) {
				errorMessage += "Veuillez entrer un numero de téléphone valide!\n";
			}
			if (faxNumber.length() > 0 && faxNumber.length() < 10) {
				errorMessage += "Veuillez entrer un numero de fax valide!\n";
			}
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

	/*
	 * =========================================================================
	 * ONGLET TITRES
	 */
	@FXML
	private Tab tab_titres_groupe;
	@FXML
	private Button btn_creer_titre;
	@FXML
	private Button btn_annuler_titre;
	@FXML
	private Button btn_supp_titre;
	private ObservableList<Titre> titreData = FXCollections.observableArrayList();
	@FXML
	private TableView<Titre> tbv_titre = new TableView<>(titreData);
	@FXML
	private TableColumn<Titre, String> col_titre_titre;
	@FXML
	private TableColumn<Titre, String> col_annee_titre;
	@FXML
	private TableColumn<Titre, Time> col_duree_titre;
	@FXML
	private TextField tf_titre;
	@FXML
	private TextField tf_annee_titre;
	@FXML
	private TextField tf_genre_titre;
	@FXML
	private CheckBox ckbox_reprise_titre;
	@FXML
	private TextField tf_auteur_titre;
	@FXML
	private LocalTimePicker lt_pk_duree;
	private Titre titre;

	/**
	 * Methode sur la checkBox correspondant, si la case est cochée, le
	 * textfield d'auteur est désactivé. Fait appel à la méthode
	 * <code>activeAuteur</code>
	 */
	@FXML
	private void isReprise() {
		if (!ckbox_reprise_titre.isSelected()) {
			tf_auteur_titre.setDisable(true);
			tf_auteur_titre.setText(
					MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem().getNom_groupe());
		} else {
			tf_auteur_titre.setDisable(false);
			tf_auteur_titre.clear();
		}
	}

	/**
	 * Renseigne la fenetre d'édition du titre.
	 */
	@FXML
	public void setTitre() {
		if (tbv_titre.getSelectionModel().getSelectedItem() == null) {
			annulerTitre();
		} else {
			modifTitre = true;
			titre = tbv_titre.getSelectionModel().getSelectedItem();
			tf_titre.setText(titre.getTitre());
			if (titre.getAnnee().equals("")) {
				tf_annee_titre.clear();
			} else {
				tf_annee_titre.setText(titre.getAnnee());
			}
			tf_genre_titre.setText(titre.getGenre());
			lt_pk_duree.setLocalTime(titre.getDuree().toLocalTime());
			if (titre.getReprise_titre() == true) {
				ckbox_reprise_titre.setSelected(true);
				tf_auteur_titre.setDisable(false);
				tf_auteur_titre.setText(titre.getAuteur());
			} else {
				ckbox_reprise_titre.setSelected(false);
				tf_auteur_titre.setDisable(true);
				tf_auteur_titre.setText(MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem()
						.getNom_groupe());
			}
			btn_creer_titre.setText((modifTitre) ? "Appliquer" : "Créer");
			btn_supp_titre.setDisable((MainViewController.getInstance().connectAdmin) ? false : true);
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Renseigne les attributs de l'objet Titre. Elle fait appel à la méthode
	 * <code>isInputValidTitre</code> et la methode <code>annulerTitre</code>
	 * 
	 * @throws ParseException
	 */
	@FXML
	private void creerModifierTitre() throws ParseException {
		if (isInputValidTitre()) {
			if (tbv_titre.getSelectionModel().getSelectedItem() == null) {
				titre = new Titre();
			} else {
				titre = tbv_titre.getSelectionModel().getSelectedItem();
			}
			titre.setTitre(tf_titre.getText());
			titre.setAnnee(annee);
			titre.setGenre(tf_genre_titre.getText());
			titre.setDuree(java.sql.Time.valueOf(lt_pk_duree.getLocalTime()));
			if (ckbox_reprise_titre.isSelected()) {
				titre.setReprise_titre(true);
				titre.setAuteur(tf_auteur_titre.getText());
			} else {
				titre.setReprise_titre(false);
				titre.setAuteur(MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem()
						.getNom_groupe());
			}
			if (tbv_titre.getSelectionModel().getSelectedItem() == null) {
				tbv_titre.getItems().add(titre);
				Session s = HibernateSetUp.getSession();
				s.beginTransaction();
				Groupe groupeH = (Groupe) s.load(Groupe.class,
						MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem().getGroupeId());
				titre.setGroupe(groupeH);
				groupeH.getListe_titre().add(titre);
				s.save(titre);
				s.getTransaction().commit();
				s.close();
			} else {
				tbv_titre.getItems().set(tbv_titre.getSelectionModel().getSelectedIndex(), titre);
				CRUD.update(titre);
			}
			annulerTitre();
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Annuler.
	 * Réinitialise les champs et les composants.
	 */
	@FXML
	private void annulerTitre() {
		tf_titre.clear();
		tf_annee_titre.clear();
		tf_genre_titre.clear();
		lt_pk_duree.setLocalTime(MainApp.getInstance().def_time);
		ckbox_reprise_titre.setSelected(true);
		tf_auteur_titre.setDisable(false);
		tf_auteur_titre.clear();
		btn_supp_titre.setDisable(true);
		tbv_titre.getSelectionModel().clearSelection();
		modifTitre = false;
		btn_creer_titre.setText((modifTitre) ? "Appliquer" : "Créer");
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton supprimer. Supprime le
	 * titre dans la liste à l'index sélectionné.
	 */
	@FXML
	private void supprimerTitre() {
		int selectedIndex = tbv_titre.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation d'action");
		alert.setHeaderText("Confirmation de suppression");
		alert.setContentText("Voulez-vous supprimer ce titre ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			CRUD.delete(tbv_titre.getSelectionModel().getSelectedItem());
			tbv_titre.getItems().remove(selectedIndex);
			annulerTitre();
		}
	}

	/**
	 * Methode de vérification des champs obligatoires.
	 *
	 * @return true si les entrée son valides
	 */
	private boolean isInputValidTitre() {
		String errorMessage = "";
		if (tf_titre.getText() == null || tf_titre.getText().length() == 0) {
			errorMessage += "Veuillez entrer un nom de titre!\n";
		}
		if (ckbox_reprise_titre.isSelected()
				&& (tf_auteur_titre.getText() == null || tf_auteur_titre.getText().length() == 0)) {
			errorMessage += "Veuillez entrer un nom d'auteur!\n";
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

	/*
	 * =========================================================================
	 * ONGLET EVENTS FUTURS
	 */
	@FXML
	private Tab tab_event_f_groupe;
	private ObservableList<Rencontre> rencontreDataF = FXCollections.observableArrayList();
	@FXML
	public TableView<Rencontre> tbv_event_f = new TableView<>(rencontreDataF);
	@FXML
	private TableColumn<Rencontre, String> col_event_event_f;
	@FXML
	private TableColumn<Rencontre, String> col_ville_event_f;
	@FXML
	private TableColumn<Rencontre, java.sql.Date> col_deb_event_f;
	@FXML
	private TableColumn<Rencontre, java.sql.Date> col_fin_event_f;

	/*
	 * =========================================================================
	 * ONGLET EVENTS PASSES
	 */
	@FXML
	private Tab tab_event_p_groupe;
	private ObservableList<Rencontre> rencontreDataP = FXCollections.observableArrayList();
	@FXML
	private TableView<Rencontre> tbv_event_p = new TableView<>(rencontreDataP);
	@FXML
	private TableColumn<Rencontre, String> col_event_event_p;
	@FXML
	private TableColumn<Rencontre, String> col_ville_event_p;
	@FXML
	private TableColumn<Rencontre, java.sql.Date> col_deb_event_p;
	@FXML
	private TableColumn<Rencontre, java.sql.Date> col_fin_event_p;
}
