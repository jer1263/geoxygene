package fr.ign.cogit.appli.commun.schema.structure.bdCarto.franchissement;

import fr.ign.cogit.appli.commun.schema.structure.bdCarto.ElementBDCarto;

/** Cette classe repr�sente une relation dans le sch�ma HBDS de la BDCarto.
 *  Elle est sous forme de classe car java ne connait pas les attributs de relation.
 *  NB: l'attribut id_bdcarto, obtenu par heritage, n'a pas de sens pour cette "classe relation",
 *      il n'est d'ailleurs pas persistant.
 */
public abstract class PassePar extends ElementBDCarto {

	/////////////// ATTRIBUTS //////////////////
	protected String mode;
	public String getMode() {return this.mode; }
	public void setMode (String S) {this.mode= S; }

	protected int niveau;
	public int getNiveau() {return this.niveau; }
	public void setNiveau (int i) {this.niveau= i; }

	/////////////// RELATIONS //////////////////
	/** Troncon concern� par la relation.
	 *
	 *  Les methodes ...ID sont utiles uniquement au mapping et ne doivent pas �tre utilis�es
	 *
	 *  NB : si il n'y a pas d'objet en relation, getObjet renvoie null.
	 *  Pour casser une relation: faire setObjet(null);
	 */

	protected Troncon troncon;
	/** R�cup�re le tron�on en relation. */
	public Troncon getTroncon() {return troncon;  }
	/** D�finit le tron�on en relation, et met � jour la relation inverse. */
	public void setTroncon(Troncon O) {
		Troncon old = troncon;
		troncon = O;
		if ( old  != null ) old.getPassentPar().remove(this);
		if ( O != null ) {
			tronconID = O.getId();
			if ( !(O.getPassentPar().contains(this)) ) O.getPassentPar().add(this);
		} else tronconID = 0;
	}
	/** Pour le mapping avec OJB, dans le cas d'une relation 1-n, du cote 1 de la relation */
	protected int tronconID;
	/** Ne pas utiliser, necessaire au mapping OJB */
	public void setTronconID(int I) {tronconID = I;}
	/** Ne pas utiliser, necessaire au mapping OJB */
	public int getTronconID() {return tronconID;}

	/** Franchissement concern� par la relation.
	 *
	 *  Les methodes ...ID sont utiles uniquement au mapping et ne doivent pas �tre utilis�es
	 *
	 *  NB : si il n'y a pas d'objet en relation, getObjet renvoie null.
	 *  Pour casser une relation: faire setObjet(null);
	 */
	/** Ne pas utiliser. Pour le mapping avec OJB, dans le cas d'une relation 1-n, du cote 1 de la relation */
	protected Franchissement franchissement;
	/** R�cup�re le franchissement en relation. */
	public Franchissement getFranchissement() {return franchissement;  }
	/** D�finit le franchissement en relation, et met � jour la relation inverse. */
	public void setFranchissement(Franchissement O) {
		Franchissement old = franchissement;
		franchissement = O;
		if ( old  != null ) old.getPassentPar().remove(this);
		if ( O != null ) {
			franchissementID = O.getId();
			if ( !(O.getPassentPar().contains(this)) ) O.getPassentPar().add(this);
		} else franchissementID = 0;
	}
	/** Pour le mapping avec OJB, dans le cas d'une relation 1-n, du cote 1 de la relation */
	protected int franchissementID;
	/** Ne pas utiliser, necessaire au mapping OJB */
	public void setFranchissementID(int I) {franchissementID = I;}
	/** Ne pas utiliser, necessaire au mapping OJB */
	public int getFranchissementID() {return franchissementID;}
}

