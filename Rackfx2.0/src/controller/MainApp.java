package controller;

import java.io.IOException;
import java.time.LocalTime;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Groupe;
import model.Rencontre;
import model.User;
import sql.CRUD;
import sql.HibernateSetUp;

public final class MainApp extends Application {

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private ObservableList<Groupe> groupeData = FXCollections.observableArrayList();
	private ObservableList<Rencontre> rencontreData = FXCollections.observableArrayList();
	private ObservableList<User> userData = FXCollections.observableArrayList();
	public final LocalTime def_time = LocalTime.of(0, 0);

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
		getGroupeData().addAll(CRUD.getAll("Groupe"));
		getRencontreData().addAll(CRUD.getAll("Rencontre"));
		getUserData().addAll(CRUD.getAll("User"));
		INSTANCE_MAINAPP = this;
	}

	/**
	 * Retournent les données dans une observableList de groupes.
	 * 
	 * @return groupeData
	 */
	public ObservableList<Groupe> getGroupeData() {
		return groupeData;
	}

	/**
	 * Retournent les données dans une observableList de rencontres.
	 * 
	 * @return rencontreData
	 */
	public ObservableList<Rencontre> getRencontreData() {
		return rencontreData;
	}

	/**
	 * Retournent les données dans une observableList de users.
	 * 
	 * @return userData
	 */
	public ObservableList<User> getUserData() {
		return userData;
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
		
		primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				HibernateSetUp.shutdown();
			}
			
		});
	}

	/**
	 * Methode d'initialisation du rootLayout avec le menu.
	 */
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/MainView.fxml"));
			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout);
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
	public void showFicheGroupeEditDialog(Groupe groupe, boolean modif, int tab) {
		try {
			/* charge le fichier fxml et crée un nouveau stage pour le popup */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/FicheGroupeEdit.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			/* creation du stage dialog */
			Stage stageGroupe = new Stage();
			stageGroupe.setTitle((modif) ? groupe.getNom_groupe() : "Nouveau groupe *");
			stageGroupe.initModality(Modality.WINDOW_MODAL);
			stageGroupe.initOwner(primaryStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource("../view/style/fiche.css").toExternalForm());
			stageGroupe.setScene(scene);

			/* met le groupe dans le controller */
			if (!modif) {
				MainViewController.getInstance().tv_reper.getSelectionModel().clearSelection();
			}
			FicheGroupeEditController controller = loader.getController();
			controller.setDialogStage(stageGroupe);
			controller.setGroupe(groupe, modif, tab);
			controller.geleTab(modif);
			/* lance la dialogbox et attend que user ferme */
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
	public void showFicheEventEditDialog(Rencontre rencontre, boolean modif, int tab) {
		try {
			/* charge le fichier fxml et crée un nouveau stage pour le popup */
			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(MainApp.class.getResource("../view/FicheEventEdit.fxml"));
			AnchorPane page2 = (AnchorPane) loader2.load();

			/* creation du stage dialog */
			Stage stageEvent = new Stage();
			stageEvent.setTitle((modif) ? rencontre.getNom_renc() : "Nouvelle rencontre *");
			stageEvent.initModality(Modality.WINDOW_MODAL);
			stageEvent.initOwner(primaryStage);
			Scene scene2 = new Scene(page2);
			scene2.getStylesheets().add(getClass().getResource("../view/style/fiche.css").toExternalForm());
			stageEvent.setScene(scene2);

			/* met la rencontre dans le controller */
			if (!modif) {
				MainViewController.getInstance().tv_planif.getSelectionModel().clearSelection();
			}
			FicheEventEditController controller2 = loader2.getController();
			controller2.setDialogStage(stageEvent);
			controller2.setEvent(rencontre, modif, tab);
			controller2.geleTab(modif);

			/* lance la dialogbox et attend que user ferme */
			stageEvent.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode retournant le primaryStage.
	 * 
	 * @return primaryStage
	 */

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Méthode principale.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
