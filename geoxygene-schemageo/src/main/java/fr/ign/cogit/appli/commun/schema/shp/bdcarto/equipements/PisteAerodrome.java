package fr.ign.cogit.appli.commun.schema.shp.bdcarto.equipements;

import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;


/** Classe geographique. Classe generee automatiquement par le chargeur de la plate-forme Oxygene*/

public abstract class PisteAerodrome extends fr.ign.cogit.appli.commun.schema.shp.bdcarto.ElementBDCarto {

	/** Renvoie la g�om�trie de l'objet, cast�e plus pr�cis�ment qu'avec la m�thode getGeom() */
	public GM_LineString getGeometrie() {return (GM_LineString)geom;}
	/** D�finit la g�om�trie de l'objet, cast�e plus pr�cis�ment qu'avec la m�thode setGeom() */
	public void setGeometrie(GM_LineString G) {this.geom = G;}


}
