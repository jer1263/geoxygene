package fr.ign.cogit.appli.commun.schema.shp.bdPaysShape2D.activite_bati;

import fr.ign.cogit.appli.commun.schema.shp.bdPaysShape2D.ElementBDPays;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;

public abstract class Batiment extends ElementBDPays {

	/** Renvoie la g�om�trie de l'objet, cast�e plus pr�cis�ment qu'avec la m�thode getGeom() */
	public GM_Polygon getGeometrie() {return (GM_Polygon)geom;}
	/** D�finit la g�om�trie de l'objet, cast�e plus pr�cis�ment qu'avec la m�thode setGeom() */
	public void setGeometrie(GM_Polygon G) {this.geom = G;}

	protected String nature;
	public String getNature() {return this.nature; }
	public void setNature (String Nature) {nature = Nature; }

	protected String categorie;
	public String getCategorie() {return this.categorie; }
	public void setCategorie (String Categorie) {categorie = Categorie; }

	protected double hauteur;
	public double getHauteur() {return this.hauteur; }
	public void setHauteur (double Hauteur) {hauteur = Hauteur; }

	protected double z_min;
	public double getZ_min() {return this.z_min; }
	public void setZ_min (double Z_min) {z_min = Z_min; }

	protected double z_max;
	public double getZ_max() {return this.z_max; }
	public void setZ_max (double Z_max) {z_max = Z_max; }

}
