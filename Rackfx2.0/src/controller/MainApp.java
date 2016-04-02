package controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Groupe;
import model.Parametres;
import model.Rencontre;
import model.User;
import sql.CRUD;
import sql.HibernateSetUp;
import utilities.Validateur;

public final class MainApp extends Application {

	protected Stage primaryStage;
	protected ObservableList<Groupe> groupeData = FXCollections.observableArrayList();
	protected ObservableList<Rencontre> rencontreData = FXCollections.observableArrayList();
	protected ObservableList<User> userData = FXCollections.observableArrayList();
	protected ObservableList<Parametres> parametresData = FXCollections.observableArrayList();
	protected final LocalTime def_time = LocalTime.of(0, 0);
	protected ResourceBundle Lang_bundle;

	/* Singleton */
	/** Instance unique pré-initialisée */
	private static MainApp INSTANCE_MAINAPP;

	/** Point d'accès pour l'instance unique du singleton */
	public static MainApp getInstance() {
		return INSTANCE_MAINAPP;
	}

	/**
	 * Constructeur
	 */
	public MainApp() {
		groupeData.addAll(CRUD.getAll("Groupe"));
		rencontreData.addAll(CRUD.getAll("Rencontre"));
		userData.addAll(CRUD.getAll("User"));
		parametresData.addAll(CRUD.getAll("Parametres"));

		if (parametresData.isEmpty()) {
			choise_lang("Français");
		} else {
			choise_lang(parametresData.get(0).getLangue());
		}

		INSTANCE_MAINAPP = this;
	}

	/**
	 * Methode pour appliquer le choix de la langue et redémarrer l'application
	 * 
	 * @param langue
	 */
	protected void choise_lang(String langue) {
		switch (langue) {
		case "Français":
			Lang_bundle = ResourceBundle.getBundle("bundles.lang", Locale.FRANCE);
			break;
		case "English":
			Lang_bundle = ResourceBundle.getBundle("bundles.lang", Locale.ENGLISH);
			break;
		}
	}

	/**
	 * Méthode principale.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Methode de lancement de l'application. Fait appel à la methode
	 * <code>initRootLayout</code> Fait appel à la methode
	 * <code>showMainView</code>
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(false);
		this.primaryStage.setTitle("RackFx");
		this.primaryStage.getIcons().add(new Image("file:src/img/mediator.png"));
		initRootLayout();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				MainViewController.getInstance().result = Validateur.showPopup(AlertType.CONFIRMATION,
						Lang_bundle.getString("Quitter"), Lang_bundle.getString("Confirmation.de.fermeture"),
						Lang_bundle.getString("Etes-vous.sûr.de.vouloir.quitter.?")).showAndWait();
				if (MainViewController.getInstance().result.get() == ButtonType.OK) {
					HibernateSetUp.shutdown();
				} else {
					event.consume();
				}
			}
		});
	}

	/**
	 * Methode d'initialisation du rootLayout avec le menu.
	 */
	protected void initRootLayout() {
		try {
			Scene scene = new Scene(FXMLLoader.load(MainApp.class.getResource("../view/MainView.fxml"), Lang_bundle));
			scene.getStylesheets().add(getClass().getResource("../view/style/mainview.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ouvre une fenetre de dialogue avec les détails de l'objet selectionné. Si
	 * l'utilisateur clique sur le bouton Créer ou Appliquer, les modifications
	 * sont sauvegardées dans l'objet en question et la valeure true est
	 * retournée. Elle fait appel à la méthode <code>setGroupe</code>
	 * 
	 * @param groupe
	 * @param modifGroupe
	 * @param tab
	 * @return true si l'utilisateur valide, sinon false.
	 */
	protected void showFicheGroupeEditDialog(Groupe groupe, boolean modif, int tab) {
		try {
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("../view/FicheGroupeEdit.fxml"), Lang_bundle);
			Stage stageGroupe = new Stage();
			stageGroupe.setTitle((modif) ? groupe.getNom_groupe() : Lang_bundle.getString("Nouveau.groupe"));
			stageGroupe.initModality(Modality.WINDOW_MODAL);
			stageGroupe.initOwner(primaryStage);
			Scene scene = new Scene((AnchorPane) loader.load());
			scene.getStylesheets().add(getClass().getResource("../view/style/fiche.css").toExternalForm());
			stageGroupe.setScene(scene);
			if (!modif) {
				MainViewController.getInstance().tv_reper.getSelectionModel().clearSelection();
			}
			FicheGroupeEditController controller = loader.getController();
			controller.setDialogStage(stageGroupe);
			controller.setGroupe(groupe, modif, tab);
			controller.geleTab(modif);
			stageGroupe.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ouvre une fenetre de dialogue avec les détails de l'objet selectionné. Si
	 * l'utilisateur clique sur le bouton Créer ou Appliquer, les modifications
	 * sont sauvegardées dans l'objet en question et la valeure true est
	 * retournée. Elle fait appel à la méthode <code>setEvent</code>
	 * 
	 * @param rencontre
	 * @param modifGroupe
	 * @return true si l'utilisateur valide, sinon false.
	 */
	protected void showFicheEventEditDialog(Rencontre rencontre, boolean modif, int tab) {
		try {
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("../view/FicheEventEdit.fxml"), Lang_bundle);
			Stage stageEvent = new Stage();
			stageEvent.setTitle((modif) ? rencontre.getNom_renc() : Lang_bundle.getString("Nouvelle.rencontre"));
			stageEvent.initModality(Modality.WINDOW_MODAL);
			stageEvent.initOwner(primaryStage);
			Scene scene2 = new Scene((AnchorPane) loader.load());
			scene2.getStylesheets().add(getClass().getResource("../view/style/fiche.css").toExternalForm());
			stageEvent.setScene(scene2);
			if (!modif) {
				MainViewController.getInstance().tv_planif.getSelectionModel().clearSelection();
			}
			FicheEventEditController controller2 = loader.getController();
			controller2.setDialogStage(stageEvent);
			controller2.setEvent(rencontre, modif, tab);
			controller2.geleTab(modif);
			stageEvent.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ouvre la fenetre de configuration de la langue du programme
	 */
	protected void showLangueDialog(Parametres param) {
		try {
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("../view/Langue.fxml"), Lang_bundle);
			Scene scene = new Scene((AnchorPane) loader.load());
			scene.getStylesheets().add(getClass().getResource("../view/style/fiche.css").toExternalForm());
			Stage stageLang = new Stage();
			stageLang.setTitle(Lang_bundle.getString("Menu.aide.langue"));
			stageLang.initModality(Modality.WINDOW_MODAL);
			stageLang.initOwner(primaryStage);
			stageLang.setScene(scene);
			LangueController controller2 = loader.getController();
			controller2.setDialogStage(stageLang);
			controller2.setParametres(param);
			stageLang.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
