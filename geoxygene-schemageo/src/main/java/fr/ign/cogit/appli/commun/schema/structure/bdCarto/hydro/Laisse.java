package fr.ign.cogit.appli.commun.schema.structure.bdCarto.hydro;

import fr.ign.cogit.appli.commun.schema.structure.bdCarto.ElementBDCarto;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;


/** Zone plate au drainage complexe dans laquelle circule un ensemble de portions de cours d'eau
 *  formant un entrelac de bras d'�gale importance. */
public abstract class Laisse extends ElementBDCarto {

	/////////////// GEOMETRIE //////////////////
	/** Attention: on peut avoir des g�o�mtries multiples (plusieurs tron�ons) */
	//    private GM_Curve geometrie = null;
	/** Renvoie la g�om�trie de self */
	public GM_LineString getGeometrie() {return (GM_LineString)geom;}
	/** D�finit la g�om�trie de self */
	public void setGeometrie(GM_LineString G) {this.geom = G;}

	/////////////// ATTRIBUTS //////////////////
	protected String nature;
	public String getNature() {return this.nature; }
	public void setNature (String i) {nature = i; }



}
