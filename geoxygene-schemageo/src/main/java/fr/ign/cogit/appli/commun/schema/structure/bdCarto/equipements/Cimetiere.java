package fr.ign.cogit.appli.commun.schema.structure.bdCarto.equipements;

import fr.ign.cogit.appli.commun.schema.structure.bdCarto.ElementBDCarto;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;


public abstract class Cimetiere extends ElementBDCarto {

	/////////////// GEOMETRIE //////////////////
	/** Attention: on peut avoir des g�om�tries multiples (plusieurs tron�ons) */
	//	  private GM_Curve geometrie = null;
	/** Renvoie le GM_Polygon qui d�finit la g�om�trie de self */
	public GM_Polygon getGeometrie() {return (GM_Polygon)geom;}
	/** D�finit le GM_Polygon qui d�finit la g�om�trie de self */
	public void setGeometrie(GM_Polygon geometrie) {this.geom = geometrie;}

	/////////////// ATTRIBUTS //////////////////
	protected String nature;
	public String getNature() {return this.nature; }
	public void setNature (String Nature) {nature = Nature; }

	protected String toponyme;
	public String getToponyme() { return toponyme; }
	public void setToponyme(String toponyme) { this.toponyme = toponyme; }

}