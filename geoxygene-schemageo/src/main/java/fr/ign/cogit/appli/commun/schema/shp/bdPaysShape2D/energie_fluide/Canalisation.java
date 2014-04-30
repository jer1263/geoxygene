package fr.ign.cogit.appli.commun.schema.shp.bdPaysShape2D.energie_fluide;


import fr.ign.cogit.appli.commun.schema.shp.bdPaysShape2D.ElementBDPays;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;

public abstract class Canalisation extends ElementBDPays {

	/** Renvoie la g�om�trie de l'objet, cast�e plus pr�cis�ment qu'avec la m�thode getGeom() */
	public GM_LineString getGeometrie() {return (GM_LineString)geom;}
	/** D�finit la g�om�trie de l'objet, cast�e plus pr�cis�ment qu'avec la m�thode setGeom() */
	public void setGeometrie(GM_LineString G) {this.geom = G;}

	protected String nature;
	public String getNature() {return this.nature; }
	public void setNature (String Nature) {nature = Nature; }

	protected double posit_sol;
	public double getPosit_sol() {return this.posit_sol; }
	public void setPosit_sol (double Posit_sol) {posit_sol = Posit_sol; }

	protected double z_ini;
	public double getZ_ini() {return this.z_ini; }
	public void setZ_ini (double Z_ini) {z_ini = Z_ini; }

	protected double z_fin;
	public double getZ_fin() {return this.z_fin; }
	public void setZ_fin (double Z_fin) {z_fin = Z_fin; }

}
