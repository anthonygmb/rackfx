package controller;

import java.io.File;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import org.hibernate.exception.ConstraintViolationException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.LocalTimePicker;
import model.Groupe;
import model.Personne;
import model.Rencontre;
import model.Representation;
import model.Titre;
import sql.CRUD;
import utilities.FileUtils;
import utilities.Res_listes;
import utilities.Validateur;
import utilities.ValidateurExeption;

public class FicheGroupeEditController {

	/*
	 * =========================================================================
	 * GENERAL
	 */

	@FXML
	private TabPane tp_fiche_groupe;
	private Stage dialogStage;
	private String telNumber = "";
	private String faxNumber = "";
	private DateFormat formatAnnee = new SimpleDateFormat("yyyy");
	private Date auj = new Date();
	private String annee = "";
	private ObservableList<Representation> repreDataTri = FXCollections.observableArrayList();
	private ObservableList<Rencontre> rencontreDataTri = FXCollections.observableArrayList();
	private Image imageOrigine;
	private ResourceBundle Lang_bundle;
	private Label vide1;
	private Label vide2;
	private Label vide3;

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
		this.Lang_bundle = MainApp.getInstance().Lang_bundle;

		imageOrigine = new Image("file:src/img/cd_music.png");

		img_view.imageProperty().addListener(new ChangeListener<Image>() {

			@Override
			public void changed(ObservableValue<? extends Image> observable, Image oldValue, Image newValue) {
				if (!img_view.getImage().toString().equals(imageOrigine.toString())) {
					bt_import_img.setDisable(true);
					bt_supp_img.setDisable(false);
				} else {
					bt_import_img.setDisable(false);
					bt_supp_img.setDisable(true);
				}
			}
		});

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
		cmbox_spe_membre.getItems().addAll(Res_listes.spe_membre);
		cmbox_instru_membre.getItems().addAll(Res_listes.instru_membre);
		cmbox_respon_membre.getItems().addAll(Res_listes.respon_membre);
		annulerPersonne();

		/* formatte le textfield au format numéro de téléphone */
		tf_tel_cor.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*") && newValue.length() <= 13) {
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
				if (newValue.matches("\\d*") && newValue.length() <= 13) {
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
		bt_import_img.setDisable(true);
		bt_supp_img.setDisable(true);

		if (MainViewController.getInstance().connectAdmin) {
			btn_creer_groupe.setDisable(false);
			btn_creer_membre.setDisable(false);
			btn_supp_membre.setDisable(false);
			btn_creer_titre.setDisable(false);
			btn_supp_titre.setDisable(false);
			bt_import_img.setDisable(false);
			bt_supp_img.setDisable(false);
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

		vide1 = new Label(Lang_bundle.getString("vide"));
		vide2 = new Label(Lang_bundle.getString("vide"));
		vide3 = new Label(Lang_bundle.getString("vide"));
		tbv_titre.setPlaceholder(vide1);
		tbv_event_f.setPlaceholder(vide2);
		tbv_event_p.setPlaceholder(vide3);
	}

	/**
	 * Methode pour afficher ou cacher les onglets autres que l'onglet
	 * information.
	 * 
	 * @param modif
	 */
	protected void geleTab(boolean modif) {
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
	protected void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Méthode de mise à jour des entités enfants
	 */
	private void loadChildren() {
		personneData.setAll(CRUD.getAllWhere("Personne", "groupeId", groupe.getGroupeId()));
		titreData.setAll(CRUD.getAllWhere("Titre", "groupeId", groupe.getGroupeId()));
		cmbox_membre.setItems(personneData);
		tbv_titre.setItems(titreData);
	}

	/*
	 * =========================================================================
	 * ONGLET INFORMATIONS
	 */

	@FXML
	private TextField tf_nom_groupe;
	@FXML
	private TextField tf_carac_groupe;
	@FXML
	private TextField tf_region_groupe;
	@FXML
	private ComboBox<String> cmbox_pays_groupe = new ComboBox<>(Res_listes.pays_groupe);
	@FXML
	private Button btn_creer_groupe;
	@FXML
	private Button bt_import_img;
	@FXML
	private Button bt_supp_img;
	@FXML
	private ImageView img_view;
	private Groupe groupe;
	private final FileChooser fileChooser = new FileChooser();
	private File file;

	/**
	 * renseigne la fenetre d'édition de groupe avec le groupe passé en
	 * parametre.
	 * 
	 * @param groupe
	 * @param modif
	 * @param tab
	 */
	protected void setGroupe(Groupe groupe, boolean modif, int tab) {
		tp_fiche_groupe.getSelectionModel().select(tab);
		this.groupe = groupe;
		tf_nom_groupe.setText(groupe.getNom_groupe());
		tf_carac_groupe.setText(groupe.getCarac_groupe());
		if (groupe.getPays_groupe() == null) {
			cmbox_pays_groupe.getSelectionModel().clearSelection();
		} else {
			cmbox_pays_groupe.getSelectionModel().select(groupe.getPays_groupe());
		}
		cmbox_pays_groupe.getItems().addAll(Res_listes.pays_groupe);
		tf_region_groupe.setText(groupe.getRegion_groupe());
		if (groupe.getImage() != null) {
			img_view.setImage(FileUtils.convertByteToImage(groupe.getImage()));
		} else {
			img_view.setImage(imageOrigine);
		}
		btn_creer_groupe.setText((modif) ? Lang_bundle.getString("Appliquer") : Lang_bundle.getString("Creer"));
		loadChildren();
		loadRencontres();
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Enregistre ou met à jour un groupe en base de données.
	 */
	@FXML
	private void creerModifierGroupe() {
		if (MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem() == null) {
			groupe = new Groupe();
		}
		groupe.setNom_groupe(tf_nom_groupe.getText());
		groupe.setCarac_groupe(tf_carac_groupe.getText());
		if (!cmbox_pays_groupe.getSelectionModel().isEmpty()) {
			groupe.setPays_groupe(cmbox_pays_groupe.getSelectionModel().getSelectedItem().toString());
		}
		groupe.setRegion_groupe(tf_region_groupe.getText());
		if (!img_view.getImage().toString().equals(imageOrigine.toString()) && groupe.getImage() == null) {
			groupe.setImage(FileUtils.convertFileToByte(file));
		}
		try {
			Validateur.validator(groupe);
			if (dialogStage.getTitle().equals(Lang_bundle.getString("Nouveau.groupe"))) {
				CRUD.saveOrUpdate(groupe);
				geleTab(true);
				MainApp.getInstance().groupeData.add(groupe);
				MainViewController.getInstance().tv_reper.getSelectionModel().selectLast();
			} else {
				CRUD.saveOrUpdate(groupe);
				MainViewController.getInstance().showGroupeDetails(groupe);
				int index = MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedIndex();
				MainApp.getInstance().groupeData.setAll(CRUD.getAll("Groupe"));
				MainViewController.getInstance().tv_reper.getSelectionModel().select(index);
			}
			dialogStage.setTitle(groupe.getNom_groupe());
			btn_creer_groupe.setText(Lang_bundle.getString("Appliquer"));
			loadChildren();
		} catch (ConstraintViolationException e) { /* test de doublons */
			Validateur
					.showPopup(AlertType.WARNING, Lang_bundle.getString("Attention"),
							Lang_bundle.getString("Doublon.detecte"), Lang_bundle.getString("Ce.groupe.existe.deja"))
					.showAndWait();
		} catch (ValidateurExeption e) { /* si la validation a échoué */
			Validateur.showPopup(AlertType.WARNING, Lang_bundle.getString("Erreur"),
					Lang_bundle.getString("Violation.de.contrainte"), e.getMessage()).showAndWait();
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
	 * Methode d'importation d'image avec le Filechooser de javaFx. On définit
	 * le file chooser pour privilégier les formats image.
	 */
	@FXML
	private void importerImagerGroupe() {
		fileChooser.setTitle(Lang_bundle.getString("Importer.une.image"));
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
		file = fileChooser.showOpenDialog(dialogStage);
		if (file != null) {
			img_view.setImage(FileUtils.convertFileToImage(file));
		}
	}

	/**
	 * Methode de suppression d'image de groupe
	 */
	@FXML
	private void supprimerImage() {
		if (!img_view.getImage().toString().equals(imageOrigine.toString())) {
			img_view.setImage(imageOrigine);
			groupe.setImage(null);
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
	@FXML
	private ComboBox<String> cmbox_spe_membre = new ComboBox<>(Res_listes.spe_membre);
	@FXML
	private ComboBox<String> cmbox_instru_membre = new ComboBox<>(Res_listes.instru_membre);
	@FXML
	private ComboBox<String> cmbox_respon_membre = new ComboBox<>(Res_listes.respon_membre);
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
	private void setPersonne() {
		if (cmbox_membre.getSelectionModel().getSelectedItem() == null) {
			annulerPersonne();
		} else {
			personne = cmbox_membre.getSelectionModel().getSelectedItem();
			tf_nom_membre.setText(personne.getNom_membre());
			tf_prenom_membre.setText(personne.getPrenom_membre());
			dtp_date_naiss_membre.setValue(personne.getDate_naiss_membre().toLocalDate());
			if (personne.getCivi_membre() == true) {
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

				if (personne.getTel_cor() == null) {
					tf_tel_cor.clear();
				} else {
					tf_tel_cor.setText(String.valueOf(personne.getTel_cor()));
				}
				if (personne.getFax_cor() == null) {
					tf_fax_cor.clear();
				} else {
					tf_fax_cor.setText(String.valueOf(personne.getFax_cor()));
				}

				tf_mail_cor.setText(personne.getMail_cor());
			} else {
				ckbox_corres_membre.setSelected(false);
				activeCorrespondant(false);
			}
			btn_creer_membre.setText(Lang_bundle.getString("Appliquer"));
			btn_supp_membre.setDisable((MainViewController.getInstance().connectAdmin) ? false : true);
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Enregistre ou met à jour une personne en base de données.
	 */
	@FXML
	private void creerModifierPersonne() {
		if (cmbox_membre.getSelectionModel().getSelectedItem() == null) {
			personne = new Personne();
		} else {
			personne = cmbox_membre.getSelectionModel().getSelectedItem();
		}
		personne.setNom_membre(tf_nom_membre.getText());
		personne.setPrenom_membre(tf_prenom_membre.getText());
		personne.setDate_naiss_membre(java.sql.Date.valueOf(dtp_date_naiss_membre.getValue()));
		if (ckbox_mr_civi_membre.isSelected()) {
			personne.setCivi_membre(true);
		} else {
			personne.setCivi_membre(false);
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
				personne.setTel_cor(null);
			} else {
				personne.setTel_cor(telNumber);
			}
			if (faxNumber.equals("")) {
				personne.setFax_cor(null);
			} else {
				personne.setFax_cor(faxNumber);
			}
			personne.setMail_cor(tf_mail_cor.getText());
		} else {
			personne.setCorrespondant(false);
		}
		try {
			Validateur.validator(personne);
			if (cmbox_membre.getSelectionModel().getSelectedItem() == null) {
				personne.setGroupe(groupe);
				groupe.getListe_personne().add(personne);
				CRUD.saveOrUpdate(personne);
				cmbox_membre.getItems().add(personne);
			} else {
				CRUD.saveOrUpdate(personne);
				cmbox_membre.getItems().set(cmbox_membre.getSelectionModel().getSelectedIndex(), personne);
			}
			annulerPersonne();
		} catch (ConstraintViolationException e) { /* test de doublons */
			Validateur.showPopup(AlertType.WARNING, Lang_bundle.getString("Attention"),
					Lang_bundle.getString("Doublon.detecte"), Lang_bundle.getString("Cette.personne.existe.deja"))
					.showAndWait();
		} catch (ValidateurExeption e) { /* si la validation a échoué */
			Validateur.showPopup(AlertType.WARNING, Lang_bundle.getString("Erreur"),
					Lang_bundle.getString("Violation.de.contrainte"), e.getMessage()).showAndWait();
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
		btn_creer_membre.setText(Lang_bundle.getString("Creer"));
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton supprimer. Supprime la
	 * personne dans la liste à l'index sélectionné.
	 */
	@FXML
	private void supprimerPersonne() {
		int selectedIndex = cmbox_membre.getSelectionModel().getSelectedIndex();
		MainViewController.getInstance().result = Validateur.showPopup(AlertType.CONFIRMATION,
				Lang_bundle.getString("Confirmation.d'action"), Lang_bundle.getString("Confirmation.de.suppression"),
				Lang_bundle.getString("Voulez-vous.supprimer.ce.membre.?")).showAndWait();
		if (MainViewController.getInstance().result.get() == ButtonType.OK) {
			CRUD.delete(cmbox_membre.getSelectionModel().getSelectedItem());
			cmbox_membre.getItems().remove(selectedIndex);
			annulerPersonne();
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
	private void setTitre() {
		if (tbv_titre.getSelectionModel().getSelectedItem() == null) {
			annulerTitre();
		} else {
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
			btn_creer_titre.setText(Lang_bundle.getString("Appliquer"));
			btn_supp_titre.setDisable((MainViewController.getInstance().connectAdmin) ? false : true);
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Enregistre ou met à jour un titre en base de données.
	 */
	@FXML
	private void creerModifierTitre() {
		if (tbv_titre.getSelectionModel().getSelectedItem() == null) {
			titre = new Titre();
		} else {
			titre = tbv_titre.getSelectionModel().getSelectedItem();
		}
		titre.setTitre(tf_titre.getText());
		titre.setAnnee(annee);
		titre.setGenre(tf_genre_titre.getText());
		if (java.sql.Time.valueOf(lt_pk_duree.getLocalTime()).toString().equals("00:00:00")) {
			titre.setDuree(null);
		} else {
			titre.setDuree(java.sql.Time.valueOf(lt_pk_duree.getLocalTime()));
		}

		if (ckbox_reprise_titre.isSelected()) {
			titre.setReprise_titre(true);
			titre.setAuteur(tf_auteur_titre.getText());
		} else {
			titre.setReprise_titre(false);
			titre.setAuteur(
					MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem().getNom_groupe());
		}
		try {
			Validateur.validator(titre);
			if (tbv_titre.getSelectionModel().getSelectedItem() == null) {
				titre.setGroupe(groupe);
				groupe.getListe_titre().add(titre);
				CRUD.saveOrUpdate(titre);
				tbv_titre.getItems().add(titre);
			} else {
				CRUD.saveOrUpdate(titre);
				tbv_titre.getItems().set(tbv_titre.getSelectionModel().getSelectedIndex(), titre);
			}
			annulerTitre();
		} catch (ValidateurExeption e) { /* si la validation a échoué */
			Validateur.showPopup(AlertType.WARNING, Lang_bundle.getString("Erreur"),
					Lang_bundle.getString("Violation.de.contrainte"), e.getMessage()).showAndWait();
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
		btn_creer_titre.setText(Lang_bundle.getString("Creer"));
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton supprimer. Supprime le
	 * titre dans la liste à l'index sélectionné.
	 */
	@FXML
	private void supprimerTitre() {
		int selectedIndex = tbv_titre.getSelectionModel().getSelectedIndex();
		MainViewController.getInstance().result = Validateur.showPopup(AlertType.CONFIRMATION,
				Lang_bundle.getString("Confirmation.d'action"), Lang_bundle.getString("Confirmation.de.suppression"),
				Lang_bundle.getString("Voulez-vous.supprimer.ce.titre.?")).showAndWait();
		if (MainViewController.getInstance().result.get() == ButtonType.OK) {
			CRUD.delete(tbv_titre.getSelectionModel().getSelectedItem());
			tbv_titre.getItems().remove(selectedIndex);
			annulerTitre();
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
	private TableView<Rencontre> tbv_event_f = new TableView<>(rencontreDataF);
	@FXML
	private TableColumn<Rencontre, String> col_event_event_f;
	@FXML
	private TableColumn<Rencontre, String> col_ville_event_f;
	@FXML
	private TableColumn<Rencontre, java.sql.Date> col_deb_event_f;
	@FXML
	private TableColumn<Rencontre, java.sql.Date> col_fin_event_f;

	/**
	 * récupération de la liste de rencontres pour les placer dans les tableaux
	 * d'événements futurs et passés
	 */
	private void loadRencontres() {
		repreDataTri.setAll(CRUD.getAllWhere("Representation", "groupeId", groupe.getGroupeId()));

		for (Representation repreTri : repreDataTri) {
			rencontreDataTri
					.setAll(CRUD.getAllWhere("Rencontre", "rencontreId", repreTri.getRencontre().getRencontreId()));

			for (Rencontre rencTri : rencontreDataTri) {
				if (rencTri.getDate_fin_renc().getTime() > auj.getTime()) {
					rencontreDataF.setAll(rencTri);
				} else {
					rencontreDataP.setAll(rencTri);
				}
			}
		}
		tbv_event_f.setItems(rencontreDataF);
		tbv_event_p.setItems(rencontreDataP);
	}

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
