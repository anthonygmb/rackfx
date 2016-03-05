package controller;

import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
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
import model.Organisateur;
import model.Rencontre;
import model.Representation;
import model.Titre;
import sql.CRUD;
import sql.HibernateSetUp;

public class FicheEventEditController {

	/*
	 * =========================================================================
	 * GENERAL
	 */
	@FXML
	private TabPane tp_fiche_event;
	private Stage dialogStage;
	private String NbPers;
	private String telNumber = "";
	private String faxNumber = "";

	/* Singleton */
	/** Instance unique pré-initialisée */
	private static FicheEventEditController INSTANCE_FICHE_EVENT_CONTROLLER;

	/** Point d'accès pour l'instance unique du singleton */
	public static FicheEventEditController getInstance() {
		return INSTANCE_FICHE_EVENT_CONTROLLER;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		INSTANCE_FICHE_EVENT_CONTROLLER = this;

		/*
		 * récupération de la liste de personnes et de titres si le groupe est
		 * existant ou pas
		 */
		if (MainViewController.getInstance().tv_planif.getSelectionModel().getSelectedItem() != null) {
			orgaData.addAll(CRUD.getAllWhere("Organisateur", "rencontreId",
					MainViewController.getInstance().tv_planif.getSelectionModel().getSelectedItem().getRencontreId()));
			repreData.addAll(CRUD.getAllWhere("Representation", "rencontreId",
					MainViewController.getInstance().tv_planif.getSelectionModel().getSelectedItem().getRencontreId()));
		}
		cmbox_orga.setItems(orgaData);
		tbv_prog.setItems(repreData);

		cmbox_orga.setButtonCell(new ListCell<Organisateur>() {
			@Override
			protected void updateItem(Organisateur item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				if (!empty && item != null) {
					final String text = String.format("%s, %s %s", item.getEntreprise_orga(), item.getCivi_orga(),
							item.getNom_orga());
					setText(text);
				}
			}
		});

		cmbox_orga.setCellFactory(new Callback<ListView<Organisateur>, ListCell<Organisateur>>() {
			@Override
			public ListCell<Organisateur> call(ListView<Organisateur> param) {
				return new ListCell<Organisateur>() {

					@Override
					protected void updateItem(Organisateur item, boolean empty) {
						super.updateItem(item, empty);
						setText(null);
						if (!empty && item != null) {
							final String text = String.format("%s, %s %s", item.getEntreprise_orga(),
									item.getCivi_orga(), item.getNom_orga());
							setText(text);
						}
					}
				};
			}
		});

		/* valeures par defaut de l'onglet organisateur */
		annulerOrganisateur();

		tf_nb_pers_event.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*") && newValue.length() < 7) {
					NbPers = newValue;
				} else {
					tf_nb_pers_event.setText(oldValue);
				}
			}
		});
		tf_nb_pers_event.positionCaret(tf_nb_pers_event.getLength());

		tf_tel_orga.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*") && newValue.length() < 13) {
					telNumber = newValue;
				} else {
					tf_tel_orga.setText(oldValue);
				}
			}
		});
		tf_tel_orga.positionCaret(tf_tel_orga.getLength());

		tf_fax_orga.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*") && newValue.length() < 13) {
					faxNumber = newValue;
				} else {
					tf_fax_orga.setText(oldValue);
				}
			}
		});
		tf_fax_orga.positionCaret(tf_fax_orga.getLength());

		/* valeures par defaut de l'onglet représentation */
		annulerProg();

		/* formatte le tableau de représentation */
		col_groupe_prog.setCellValueFactory(cellData -> cellData.getValue().nom_groupeProperty());
//		col_titre_prog.setCellValueFactory(cellData -> cellData.getValue().nom_titreProperty());
		col_deb_prog.setCellValueFactory(cellData -> cellData.getValue().heure_debutProperty());
		col_fin_prog.setCellValueFactory(cellData -> cellData.getValue().heure_fintProperty());
		tbv_prog.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setProg());

		/* formatte la combobox pour qu'elle affiche le texte voulu */
		cmbox_groupe_event.setButtonCell(new ListCell<Groupe>() {
			@Override
			protected void updateItem(Groupe item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				if (!empty && item != null) {
					final String text = String.format("%s", item.getNom_groupe());
					setText(text);
				}
			}
		});
		cmbox_groupe_event.setCellFactory(new Callback<ListView<Groupe>, ListCell<Groupe>>() {
			@Override
			public ListCell<Groupe> call(ListView<Groupe> param) {
				return new ListCell<Groupe>() {

					@Override
					protected void updateItem(Groupe groupe, boolean empty) {
						super.updateItem(groupe, empty);
						setText(null);
						if (!empty && groupe != null) {
							final String text = String.format("%s", groupe.getNom_groupe());
							setText(text);
						}
					}
				};
			}
		});

		/* formatte la combobox pour qu'elle affiche le texte voulu */
		cmbox_titre_event.setButtonCell(new ListCell<Titre>() {
			@Override
			protected void updateItem(Titre item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				if (!empty && item != null) {
					final String text = String.format("%s", item.getTitre());
					setText(text);
				}
			}
		});
		cmbox_titre_event.setCellFactory(new Callback<ListView<Titre>, ListCell<Titre>>() {
			@Override
			public ListCell<Titre> call(ListView<Titre> param) {
				return new ListCell<Titre>() {

					@Override
					protected void updateItem(Titre titre, boolean empty) {
						super.updateItem(titre, empty);
						setText(null);
						if (!empty && titre != null) {
							final String text = String.format("%s ", titre.getTitre());
							setText(text);
						}
					}
				};
			}
		});

		cmbox_groupe_event.setItems(MainApp.getInstance().getGroupeData());
		cmbox_groupe_event.valueProperty().addListener(new ChangeListener<Groupe>() {
			@Override
			public void changed(ObservableValue<? extends Groupe> observable, Groupe oldValue, Groupe newValue) {
				if (cmbox_groupe_event.getSelectionModel().getSelectedItem() == null) {
					cmbox_titre_event.getSelectionModel().clearSelection();
				} else {
					cmbox_titre_event.getItems().clear();
					titreData2.addAll(CRUD.getAllWhere("Titre", "groupeId", newValue.getGroupeId()));
					cmbox_titre_event.setItems(titreData2);
				}
			}
		});

		btn_creer_event.setDisable(true);
		btn_creer_orga.setDisable(true);
		btn_creer_prog.setDisable(true);
		btn_supp_orga.setDisable(true);
		btn_supp_prog.setDisable(true);

		if (MainViewController.getInstance().connectAdmin) {
			btn_creer_event.setDisable(false);
			btn_creer_orga.setDisable(false);
			btn_creer_prog.setDisable(false);
			btn_supp_orga.setDisable(false);
			btn_supp_prog.setDisable(false);
		} else if (MainViewController.getInstance().connectUser) {
			btn_creer_event.setDisable(false);
			btn_creer_orga.setDisable(false);
			btn_creer_prog.setDisable(false);
		}
	}

	/**
	 * Methode pour afficher ou cacher les onglets autres que l'onglet
	 * information.
	 * 
	 * @param modif
	 */
	public void geleTab(boolean modif) {
		if (!modif) {
			tab_orga_event.setDisable(true);
			tab_prog_event.setDisable(true);
		} else {
			tab_orga_event.setDisable(false);
			tab_prog_event.setDisable(false);
		}
	}

	/**
	 * Sets the stage of this dialog.
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
	private Tab tab_infos_event;
	@FXML
	private TextField tf_ville_event;
	ObservableList<String> perio_event = FXCollections.observableArrayList("Annuel", "Semestriel", "Trimestriel",
			"Bimensuel", "Mensuel", "Hebdomadaire", "Journalier");
	@FXML
	private ComboBox<String> cmbox_perio_event = new ComboBox<>();
	@FXML
	private TextField tf_nom_event;
	@FXML
	private TextField tf_lieu_event;
	@FXML
	private DatePicker dt_debut_event;
	@FXML
	private DatePicker dt_fin_event;
	@FXML
	private TextField tf_nb_pers_event;
	@FXML
	private Button btn_creer_event;
	@FXML
	private Button btn_annuler_event;
	private Rencontre rencontre;

	/**
	 * Renseigne la fenetre d'édition de rencontre avec la rencontre passée en
	 * parametre.
	 * 
	 * @param rencontre
	 * @param modif
	 */
	public void setEvent(Rencontre rencontre, boolean modif, int tab) {
		tp_fiche_event.getSelectionModel().select(tab);
		this.rencontre = rencontre;
		tf_nom_event.setText(rencontre.getNom_renc());
		tf_ville_event.setText(rencontre.getVille_renc());
		tf_lieu_event.setText(rencontre.getLieu_renc());
		tf_nb_pers_event.setText(String.valueOf(rencontre.getNb_pers_attendues()));

		if (modif) {
			dt_debut_event.setValue(rencontre.getDate_deb_renc().toLocalDate());
		} else {
			dt_debut_event.setValue(LocalDate.now());
		}

		if (modif) {
			dt_fin_event.setValue(rencontre.getDate_fin_renc().toLocalDate());
		} else {
			dt_fin_event.setValue(LocalDate.now());
		}

		if (modif) {
			if (rencontre.getPeriodicite_renc().equals("")) {
				cmbox_perio_event.getSelectionModel().clearSelection();
			} else {
				cmbox_perio_event.getSelectionModel().select(rencontre.getPeriodicite_renc());
			}
		}
		cmbox_perio_event.getItems().addAll(perio_event);
		btn_creer_event.setText((modif) ? "Appliquer" : "Créer");
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Renseigne les attributs de l'objet rencontre et passe le booleen
	 * <code>okClicked</code> à true.
	 */
	@FXML
	private void creerModifierRencontre() {
		if (isInputValidRencontre()) {
			rencontre.setNom_renc(tf_nom_event.getText());
			rencontre.setVille_renc(tf_ville_event.getText());
			rencontre.setLieu_renc(tf_lieu_event.getText());
			rencontre.setNb_pers_attendues(Long.parseLong(NbPers));
			rencontre.setDate_deb_renc(java.sql.Date.valueOf(dt_debut_event.getValue()));
			rencontre.setDate_fin_renc(java.sql.Date.valueOf(dt_fin_event.getValue()));
			if (cmbox_perio_event.getSelectionModel().isEmpty()) {
				rencontre.setPeriodicite_renc("");
			} else {
				rencontre.setPeriodicite_renc(cmbox_perio_event.getSelectionModel().getSelectedItem().toString());
			}
			if (dialogStage.getTitle().equals("Nouvelle rencontre *")) {
				geleTab(true);
				MainApp.getInstance().getRencontreData().add(rencontre);
				CRUD.save(rencontre);
				MainViewController.getInstance().tv_planif.getSelectionModel().selectLast();
//				orgaData.clear();
//				repreData.clear();
//				orgaData.addAll(CRUD.getAllWhere("Organisateur", "rencontreId",
//						MainViewController.getInstance().tv_planif.getSelectionModel().getSelectedItem().getRencontreId()));
//				repreData.addAll(CRUD.getAllWhere("Representation", "rencontreId",
//						MainViewController.getInstance().tv_planif.getSelectionModel().getSelectedItem().getRencontreId()));
//				cmbox_orga.setItems(orgaData);
//				tbv_prog.setItems(repreData);
			} else {
				CRUD.update(rencontre);
				MainViewController.getInstance().showEventDetails(rencontre);
				MainApp.getInstance().getRencontreData().setAll(CRUD.getAll("Rencontre"));
			}
			dialogStage.setTitle(rencontre.getNom_renc());
			orgaData.clear();
			repreData.clear();
			orgaData.addAll(CRUD.getAllWhere("Organisateur", "rencontreId", rencontre.getRencontreId()));
			repreData.addAll(CRUD.getAllWhere("Representation", "rencontreId", rencontre.getRencontreId()));
			cmbox_orga.setItems(orgaData);
			tbv_prog.setItems(repreData);
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Annuler.
	 * Ferme la fenetre sans enregistrer les modifications.
	 */
	@FXML
	private void annulerRencontre() {
		dialogStage.close();
	}

	/**
	 * Methode de vérification des champs obligatoires.
	 *
	 * @return true si les entrée son valides
	 */
	private boolean isInputValidRencontre() {
		String errorMessage = "";
		if (tf_nom_event.getText() == null || tf_nom_event.getText().length() == 0) {
			errorMessage += "Veuillez entrer un nom d'événement!\n";
		}
		if (tf_ville_event.getText() == null || tf_ville_event.getText().length() == 0) {
			errorMessage += "Veuillez entrer un nom de ville!\n";
		}
		if (dt_debut_event.getValue().isAfter(dt_fin_event.getValue())
				|| dt_fin_event.getValue().isBefore(dt_debut_event.getValue())) {
			errorMessage += "Veuillez entrer une date valide!\n";
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
	 * ONGLET ORGANISATEURS
	 */
	@FXML
	private Tab tab_orga_event;
	@FXML
	private CheckBox ckbox_mme_civi_orga;
	@FXML
	private CheckBox ckbox_mr_civi_orga;
	@FXML
	private TextField tf_nom_orga;
	@FXML
	private TextField tf_prenom_orga;
	@FXML
	private TextField tf_adress_orga;
	@FXML
	private TextField tf_tel_orga;
	@FXML
	private TextField tf_fax_orga;
	@FXML
	private TextField tf_mail_orga;
	@FXML
	private TextField tf_entreprise_orga;
	private ObservableList<Organisateur> orgaData = FXCollections.observableArrayList();
	@FXML
	private ComboBox<Organisateur> cmbox_orga = new ComboBox<>(orgaData);
	@FXML
	private Button btn_creer_orga;
	@FXML
	private Button btn_annuler_orga;
	@FXML
	private Button btn_supp_orga;
	private Organisateur organisateur;

	/**
	 * Methode sur la checkBox Monsieur, si la case Monsieur est cochée, Madamme
	 * est décochée.
	 */
	@FXML
	private void setCiviliteMr() {
		if (ckbox_mr_civi_orga.isSelected()) {
			ckbox_mme_civi_orga.setSelected(false);
		}
	}

	/**
	 * Methode sur la checkBox Madamme, si la case Madamme est cochée, Monsieur
	 * est décochée.
	 */
	@FXML
	private void setCiviliteMme() {
		if (ckbox_mme_civi_orga.isSelected()) {
			ckbox_mr_civi_orga.setSelected(false);
		}
	}

	/**
	 * renseigne la fenetre d'édition de l'organisateur.
	 */
	@FXML
	private void setOrganisateur() {
		if (cmbox_orga.getSelectionModel().getSelectedItem() == null) {
			annulerOrganisateur();
		} else {
			organisateur = cmbox_orga.getSelectionModel().getSelectedItem();
			if (organisateur.getCivi_orga().equals("Monsieur")) {
				ckbox_mr_civi_orga.setSelected(true);
				ckbox_mme_civi_orga.setSelected(false);
			} else {
				ckbox_mme_civi_orga.setSelected(true);
				ckbox_mr_civi_orga.setSelected(false);
			}
			tf_nom_orga.setText(organisateur.getNom_orga());
			tf_prenom_orga.setText(organisateur.getPrenom_orga());
			tf_entreprise_orga.setText(organisateur.getEntreprise_orga());
			tf_adress_orga.setText(organisateur.getAdresse_entreprise_orga());

			if (organisateur.getTel_orga() == 0) {
				tf_tel_orga.clear();
			} else {
				tf_tel_orga.setText(String.valueOf(organisateur.getTel_orga()));
			}

			if (organisateur.getFax_orga() == 0) {
				tf_fax_orga.clear();
			} else {
				tf_fax_orga.setText(String.valueOf(organisateur.getFax_orga()));
			}

			tf_mail_orga.setText(organisateur.getMail_orga());
			btn_creer_orga.setText("Appliquer");
			btn_supp_orga.setDisable((MainViewController.getInstance().connectAdmin) ? false : true);
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Renseigne les attributs de l'objet Organisateur. Elle fait appel à la
	 * méthode <code>isInputValidOrganisateur</code> et la methode
	 * <code>annulerOrganisateur</code>
	 */
	@FXML
	private void creerModifierOrganisateur() {
		if (isInputValidOrganisateur()) {
			if (cmbox_orga.getSelectionModel().getSelectedItem() == null) {
				organisateur = new Organisateur();
			} else {
				organisateur = cmbox_orga.getSelectionModel().getSelectedItem();
			}
			if (ckbox_mr_civi_orga.isSelected()) {
				organisateur.setCivi_orga("Monsieur");
			} else {
				organisateur.setCivi_orga("Madamme");
			}
			organisateur.setNom_orga(tf_nom_orga.getText());
			organisateur.setPrenom_orga(tf_prenom_orga.getText());
			organisateur.setEntreprise_orga(tf_entreprise_orga.getText());
			organisateur.setAdresse_entreprise_orga(tf_adress_orga.getText());
			if (telNumber.equals("")) {
				organisateur.setTel_orga((long) 0);
			} else {
				organisateur.setTel_orga(Long.parseLong(telNumber));
			}
			if (faxNumber.equals("")) {
				organisateur.setFax_orga((long) 0);
			} else {
				organisateur.setFax_orga(Long.parseLong(faxNumber));
			}
			organisateur.setMail_orga(tf_mail_orga.getText());

			if (cmbox_orga.getSelectionModel().getSelectedItem() == null) {
				cmbox_orga.getItems().add(organisateur);
				Session s = HibernateSetUp.getSession();
				s.beginTransaction();
				Rencontre rencontreH = (Rencontre) s.load(Rencontre.class, MainViewController.getInstance().tv_planif
						.getSelectionModel().getSelectedItem().getRencontreId());
				organisateur.setRencontre(rencontreH);
				rencontreH.getListe_orga().add(organisateur);
				s.save(organisateur);
				s.getTransaction().commit();
				s.close();
			} else {
				cmbox_orga.getItems().set(cmbox_orga.getSelectionModel().getSelectedIndex(), organisateur);
				CRUD.update(organisateur);
			}
			annulerOrganisateur();
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Annuler.
	 * Réinitialise les champs et les composants.
	 */
	@FXML
	private void annulerOrganisateur() {
		tf_nom_orga.clear();
		tf_prenom_orga.clear();
		tf_entreprise_orga.clear();
		tf_adress_orga.clear();
		tf_tel_orga.clear();
		tf_fax_orga.clear();
		tf_mail_orga.clear();
		ckbox_mr_civi_orga.setSelected(true);
		ckbox_mme_civi_orga.setSelected(false);
		cmbox_orga.getSelectionModel().clearSelection();
		btn_supp_orga.setDisable(true);
		btn_creer_orga.setText("Créer");
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton supprimer. Supprime
	 * l'organisateur dans la liste à l'index sélectionné.
	 */
	@FXML
	private void supprimerOrganisateur() {
		int selectedIndex = cmbox_orga.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation d'action");
		alert.setHeaderText("Confirmation de suppression");
		alert.setContentText("Voulez-vous supprimer cet organisateur ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			CRUD.delete(cmbox_orga.getSelectionModel().getSelectedItem());
			cmbox_orga.getItems().remove(selectedIndex);
			annulerOrganisateur();
		}
	}

	/**
	 * Methode de vérification des champs obligatoires.
	 *
	 * @return true si les entrée son valides
	 */
	private boolean isInputValidOrganisateur() {
		String errorMessage = "";
		if (tf_nom_orga.getText() == null || tf_nom_orga.getText().length() == 0) {
			errorMessage += "Veuillez entrer un nom d'organisateur!\n";
		}
		if (tf_prenom_orga.getText() == null || tf_prenom_orga.getText().length() == 0) {
			errorMessage += "Veuillez entrer un prénom d'organisateur!\n";
		}
		if (tf_entreprise_orga.getText() == null || tf_entreprise_orga.getText().length() == 0) {
			errorMessage += "Veuillez entrer une entreprise d'organisateur!\n";
		}
		if (tf_mail_orga.getLength() > 0 && ((!tf_mail_orga.getText().contains("@")) || tf_mail_orga.getLength() < 6)) {
			errorMessage += "Veuillez entrer une adresse mail valide!\n";
		}
		if (telNumber.length() < 10) {
			errorMessage += "Veuillez entrer un numero de téléphone valide!\n";
		}
		if (faxNumber.length() > 0 && faxNumber.length() < 10) {
			errorMessage += "Veuillez entrer un numero de fax valide!\n";
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
	 * ONGLET REPRESENTATION
	 */
	@FXML
	private Tab tab_prog_event;
	@FXML
	private Button btn_creer_prog;
	@FXML
	private Button btn_annuler_prog;
	@FXML
	private Button btn_supp_prog;
	private ObservableList<Representation> repreData = FXCollections.observableArrayList();
	@FXML
	public TableView<Representation> tbv_prog = new TableView<>(repreData);
	@FXML
	private TableColumn<Representation, String> col_groupe_prog;
	@FXML
	private TableColumn<Representation, String> col_titre_prog;
	@FXML
	private TableColumn<Representation, Time> col_deb_prog;
	@FXML
	private TableColumn<Representation, Time> col_fin_prog;
	@FXML
	private ComboBox<Groupe> cmbox_groupe_event = new ComboBox<>(MainApp.getInstance().getGroupeData());
	private ObservableList<Titre> titreData2 = FXCollections.observableArrayList();
	@FXML
	private ComboBox<Titre> cmbox_titre_event = new ComboBox<>(titreData2);
	@FXML
	private LocalTimePicker ltp_h_deb_prog;
	@FXML
	private LocalTimePicker ltp_h_fin_prog;
	private Representation representation;

	/**
	 * Renseigne la fenetre d'édition de la représentation.
	 */
	@FXML
	public void setProg() {
		if (tbv_prog.getSelectionModel().getSelectedItem() == null) {
			annulerProg();
		} else {
			representation = tbv_prog.getSelectionModel().getSelectedItem();
			Session s = HibernateSetUp.getSession();
			s.beginTransaction();
			@SuppressWarnings("unchecked")
			List<Groupe> groupe = s
					.createQuery("from Groupe where nom_groupe = " + "'" + representation.getNom_Groupe() + "'").list();
			cmbox_groupe_event.getSelectionModel().select(groupe.get(0));// TODO
//			@SuppressWarnings("unchecked")
//			List<Titre> titre = s.createQuery("from Titre where titre = " + "'" + representation.getNom_Titre() + "'")
//					.list();
//			cmbox_titre_event.getSelectionModel().select(titre.get(0));// TODO
			s.getTransaction().commit();
			s.close();
			ltp_h_deb_prog.setLocalTime(representation.getHeure_debut().toLocalTime());
			ltp_h_fin_prog.setLocalTime(representation.getHeure_fin().toLocalTime());
			btn_creer_prog.setText("Appliquer");
			btn_supp_prog.setDisable((MainViewController.getInstance().connectAdmin) ? false : true);
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Créer.
	 * Renseigne les attributs de l'objet Représentation. Elle fait appel à la
	 * méthode <code>isInputValidProg</code> et la methode
	 * <code>annulerProg</code>
	 * 
	 * @throws ParseException
	 */
	@FXML
	private void creerModifierProg() throws ParseException {
		if (isInputValidProg()) {
			if (tbv_prog.getSelectionModel().getSelectedItem() == null) {
				representation = new Representation();
			} else {
				representation = tbv_prog.getSelectionModel().getSelectedItem();
			}
			representation.setNom_Groupe(cmbox_groupe_event.getSelectionModel().getSelectedItem().getNom_groupe());
//			representation.setNom_Titre(cmbox_titre_event.getSelectionModel().getSelectedItem().getTitre());
			representation.setHeure_debut(java.sql.Time.valueOf(ltp_h_deb_prog.getLocalTime()));
			representation.setHeure_fin(java.sql.Time.valueOf(ltp_h_fin_prog.getLocalTime()));

			if (tbv_prog.getSelectionModel().getSelectedItem() == null) {
				tbv_prog.getItems().add(representation);
				Session s = HibernateSetUp.getSession();
				s.beginTransaction();
				Rencontre rencontreH = (Rencontre) s.load(Rencontre.class, MainViewController.getInstance().tv_planif
						.getSelectionModel().getSelectedItem().getRencontreId());
				representation.setRencontre(rencontreH);
				rencontreH.getListe_repre().add(representation);
			
//				Groupe groupeH = (Groupe) s.load(Groupe.class, cmbox_groupe_event.getSelectionModel().getSelectedItem().getGroupeId());
//				rencontreH.setGroupe(groupeH);
//				groupeH.getListe_rencontre().add(rencontreH);
				
				Titre titreH = (Titre) s.load(Titre.class, cmbox_titre_event.getSelectionModel().getSelectedItem().getTitreId());
				representation.setTitre(titreH);
				titreH.getListe_representation().add(representation);
				
				s.save(representation);
				s.getTransaction().commit();
				s.close();
			} else {
				tbv_prog.getItems().set(tbv_prog.getSelectionModel().getSelectedIndex(), representation);
				CRUD.update(representation);
			}
			annulerProg();
		}
	}

	/**
	 * Methode executée lorsque l'utilisateur clique sur le bouton Annuler.
	 * Réinitialise les champs et les composants.
	 */
	@FXML
	private void annulerProg() {
		cmbox_titre_event.getItems().clear();
		cmbox_groupe_event.getSelectionModel().clearSelection();
		ltp_h_deb_prog.setLocalTime(MainApp.getInstance().def_time);
		ltp_h_fin_prog.setLocalTime(MainApp.getInstance().def_time);
		btn_supp_prog.setDisable(true);
		tbv_prog.getSelectionModel().clearSelection();
		btn_creer_prog.setText("Créer");
	}

	/**
	 * Appelé quand l'utilisateur clique sur le bouton supprimer. Supprime la
	 * représentation dans la liste à l'index sélectionné.
	 */
	@FXML
	private void supprimerProg() {
		int selectedIndex = tbv_prog.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation d'action");
		alert.setHeaderText("Confirmation de suppression");
		alert.setContentText("Voulez-vous supprimer cette représentation ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			CRUD.delete(tbv_prog.getSelectionModel().getSelectedItem());
			tbv_prog.getItems().remove(selectedIndex);
			annulerProg();
		}
	}

	/**
	 * Methode de vérification des champs obligatoires.
	 *
	 * @return true si les entrée son valides
	 */
	private boolean isInputValidProg() {
		String errorMessage = "";
		if (cmbox_groupe_event.getSelectionModel().getSelectedItem() == null) {
			errorMessage += "Veuillez selectionner un groupe!\n";
		}
		if (cmbox_titre_event.getSelectionModel().getSelectedItem() == null) {
			errorMessage += "Veuillez selectionner un titre!\n";
		}
		if (ltp_h_deb_prog.getLocalTime().equals(MainApp.getInstance().def_time)
				&& ltp_h_fin_prog.getLocalTime().equals(MainApp.getInstance().def_time)) {
			errorMessage += "Veuillez entrer l'heure de début!\n";
		}
		if (ltp_h_fin_prog.getLocalTime().isBefore(ltp_h_deb_prog.getLocalTime())
				|| ltp_h_deb_prog.getLocalTime().isAfter(ltp_h_fin_prog.getLocalTime())) {
			errorMessage += "Conflict dans les plages horaires!\n";
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
