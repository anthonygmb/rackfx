package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Indexed
@AnalyzerDef(name = "customanalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class) , filters = {
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		@TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
				@Parameter(name = "language", value = "English") }) })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "groupeId") )
public class Groupe {

	private long groupeId;
	private StringProperty nom_groupe;
	private StringProperty carac_groupe;
	private StringProperty pays_groupe;
	private StringProperty region_groupe;
	private Set<Personne> liste_personne = new HashSet<Personne>();
	private Set<Titre> liste_titre = new HashSet<Titre>();
	private Set<Rencontre> liste_rencontre = new HashSet<Rencontre>();

	public Groupe() {
		this(null, null, null, null);
	}

	public Groupe(String nomGroupe, String carac_groupe, String pays_groupe, String region_groupe) {
		this.nom_groupe = new SimpleStringProperty(nomGroupe);
		this.carac_groupe = new SimpleStringProperty("");
		this.pays_groupe = new SimpleStringProperty("");
		this.region_groupe = new SimpleStringProperty("");
	}

	// =================================================================================================
	@Id
	@DocumentId
	@GeneratedValue
	public long getGroupeId() {
		return groupeId;
	}

	@SuppressWarnings("unused")
	private void setGroupeId(long groupeId) {
		this.groupeId = groupeId;
	}

	// =================================================================================================
	@NotNull
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Analyzer(definition = "customanalyzer")
	public String getNom_groupe() {
		return nom_groupe.get();
	}

	public void setNom_groupe(String nom_groupe) {
		this.nom_groupe.set(nom_groupe);
	}

	public StringProperty nom_groupeProperty() {
		return nom_groupe;
	}

	// =================================================================================================
	public String getCarac_groupe() {
		return carac_groupe.get();
	}

	public void setCarac_groupe(String carac_groupe) {
		this.carac_groupe.set(carac_groupe);
	}

	public StringProperty carac_groupeProperty() {
		return carac_groupe;
	}

	// =================================================================================================
	public String getPays_groupe() {
		return pays_groupe.get();
	}

	public void setPays_groupe(String pays_groupe) {
		this.pays_groupe.set(pays_groupe);
	}

	public StringProperty pays_groupeProperty() {
		return pays_groupe;
	}

	// =================================================================================================
	public String getRegion_groupe() {
		return region_groupe.get();
	}

	public void setRegion_groupe(String region_groupe) {
		this.region_groupe.set(region_groupe);
	}

	public StringProperty region_groupeProperty() {
		return region_groupe;
	}

	// =================================================================================================
	@IndexedEmbedded
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "groupe")
	public Set<Personne> getListe_personne() {
		return liste_personne;
	}

	public void setListe_personne(Set<Personne> liste_personne) {
		this.liste_personne = liste_personne;
	}

	// =================================================================================================
	@IndexedEmbedded
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "groupe")
	public Set<Titre> getListe_titre() {
		return liste_titre;
	}

	public void setListe_titre(Set<Titre> liste_titre) {
		this.liste_titre = liste_titre;
	}
	
	// =================================================================================================
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "groupe")
	public Set<Rencontre> getListe_rencontre() {
		return liste_rencontre;
	}

	public void setListe_rencontre(Set<Rencontre> liste_rencontre) {
		this.liste_rencontre = liste_rencontre;
	}
}
