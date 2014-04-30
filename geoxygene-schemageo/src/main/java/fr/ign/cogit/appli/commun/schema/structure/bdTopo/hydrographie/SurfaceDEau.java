package fr.ign.cogit.appli.commun.schema.structure.bdTopo.hydrographie;


import fr.ign.cogit.appli.commun.schema.structure.bdTopo.ElementBDTopo;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;

public abstract class SurfaceDEau extends ElementBDTopo {

	/** Renvoie la g�om�trie de l'objet, cast�e plus pr�cis�ment qu'avec la m�thode getGeom() */
	public GM_Polygon getGeometrie() {return (GM_Polygon)geom;}
	/** D�finit la g�om�trie de l'objet, cast�e plus pr�cis�ment qu'avec la m�thode setGeom() */
	public void setGeometrie(GM_Polygon G) {this.geom = G;}

	protected String nature;
	public String getNature() {return this.nature; }
	public void setNature (String Nature) {nature = Nature; }

	protected String regime;
	public String getRegime() {return this.regime; }
	public void setRegime (String Regime) {regime = Regime; }

	protected double z_min;
	public double getZ_min() {return this.z_min; }
	public void setZ_min (double Z_min) {z_min = Z_min; }

	protected double z_max;
	public double getZ_max() {return this.z_max; }
	public void setZ_max (double Z_max) {z_max = Z_max; }

}
