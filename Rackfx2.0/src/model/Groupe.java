package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Indexed
@AnalyzerDef(name = "customanalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class) , filters = {
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		@TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
				@Parameter(name = "language", value = "English") }) })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "nom_groupe") )
public class Groupe {

	private long groupeId;
	private StringProperty nom_groupe;
	private String carac_groupe;
	private String pays_groupe;
	private String region_groupe;
	private Set<Personne> liste_personne = new HashSet<Personne>();
	private Set<Titre> liste_titre = new HashSet<Titre>();
	private Set<Representation> liste_representation = new HashSet<Representation>();
	private byte[] image;

	public Groupe() {
		this(null, null, null, null);
	}

	public Groupe(String nomGroupe, String carac_groupe, String pays_groupe, String region_groupe) {
		this.nom_groupe = new SimpleStringProperty(nomGroupe);
		this.carac_groupe = carac_groupe;
		this.pays_groupe = pays_groupe;
		this.region_groupe = region_groupe;
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
	@NotEmpty
	@Length(max = 50)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Analyzer(definition = "customanalyzer")
	public String getNom_groupe() {
		return nom_groupe.get();
	}

	public void setNom_groupe(String nom_groupe) {
		this.nom_groupe.set(nom_groupe);
	}

	public final StringProperty nom_groupeProperty() {
		return nom_groupe;
	}

	// =================================================================================================
	@Length(max = 50)
	public String getCarac_groupe() {
		return carac_groupe;
	}

	public void setCarac_groupe(String carac_groupe) {
		this.carac_groupe = carac_groupe;
	}

	// =================================================================================================
	public String getPays_groupe() {
		return pays_groupe;
	}

	public void setPays_groupe(String pays_groupe) {
		this.pays_groupe = pays_groupe;
	}

	// =================================================================================================
	@Length(max = 50)
	public String getRegion_groupe() {
		return region_groupe;
	}

	public void setRegion_groupe(String region_groupe) {
		this.region_groupe = region_groupe;
	}

	// =================================================================================================
	@IndexedEmbedded
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "groupe")
	public Set<Personne> getListe_personne() {
		return liste_personne;
	}

	public void setListe_personne(Set<Personne> liste_personne) {
		this.liste_personne = liste_personne;
	}

	// =================================================================================================
	@IndexedEmbedded
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "groupe")
	public Set<Titre> getListe_titre() {
		return liste_titre;
	}

	public void setListe_titre(Set<Titre> liste_titre) {
		this.liste_titre = liste_titre;
	}

	// =================================================================================================
	@IndexedEmbedded
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "groupe")
	public Set<Representation> getListe_representation() {
		return liste_representation;
	}

	public void setListe_representation(Set<Representation> liste_representation) {
		this.liste_representation = liste_representation;
	}

	// =================================================================================================
	@Lob
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
