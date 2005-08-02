/*
 * This file is part of the GeOxygene project source files. 
 * 
 * GeOxygene aims at providing an open framework which implements OGC/ISO specifications for 
 * the development and deployment of geographic (GIS) applications. It is a open source 
 * contribution of the COGIT laboratory at the Institut G�ographique National (the French 
 * National Mapping Agency).
 * 
 * See: http://oxygene-project.sourceforge.net 
 *  
 * Copyright (C) 2005 Institut G�ographique National
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with 
 * this library (see file LICENSE if present); if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *  
 */

package fr.ign.cogit.geoxygene.spatial.geomaggr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.geomroot.GM_Object;


/**
  * Agr�gation quelconque d'objets g�om�triques. Il n'y a aucune structure interne.
  * On aurait pu faire un set d'elements au lieu d'une liste ? 
  *
  * @author Thierry Badard & Arnaud Braun
  * @version 1.0
  * 
  */


public class GM_Aggregate extends GM_Object {
    
    /** Liste des �l�ments constituant self. */
    protected List element;
    
    /** It�rateur sur laliste des �l�ments. */
    protected Iterator iElement;
    
    /** Renvoie l'attribut element = liste de GM_Object. */
    public List getList () {return this.element;}
    
    /** Affecte une liste de GM_Object. */
    public void setList (List L) {element = L; }    
    
    /** Renvoie l'�l�ment de rang i */
    public GM_Object get (int i) {return (GM_Object)this.element.get(i);}
    
    /** Affecte un �l�ment au i-�me rang de la liste */
    public void set (int i, GM_Object value) {this.element.set(i, value);}
    
    /** Ajoute un �l�ment en fin de liste */
    public void add (GM_Object value) {this.element.add(value);}
    
    /** Ajoute une liste d'�l�ments en fin de liste */
    public void addAll (List theList) {this.element.addAll(theList);}
    
    /** Ajoute un �l�ment au i-�me rang de la liste */
    public void add (int i, GM_Object value) {this.element.add(i, value);}
    
    /** Efface de la liste le (ou les) �l�ments pass� en param�tre */
    public void remove (GM_Object value)  {this.element.remove(value);}
    
    /** Efface le i-�me �l�ment de la liste */
    public void remove (int i)  {this.element.remove(i);}
    
    /** Efface toute la liste */
    public void clear() {this.element.clear();}
    
    /** Initialise l'it�rateur de la liste. */
    public void initIterator() {iElement = this.element.iterator();}
    
    /** Renvoie true s'il reste des �l�ments avec l'it�rateur, false sinon. */
    public boolean hasNext() {
        if (iElement.hasNext()) return true;
        else return false;
    }
    
    /** Renvoie le prochain �l�ment avec l'iterateur. */
    public GM_Object next() {return (GM_Object)iElement.next();}
    
    /** Renvoie le nombre de �l�ments */
    public int size () {return this.element.size();}
    
    /** Convertit l'agr�gat en un tableau de GM_Obect. */
    public GM_Object[] toArray() {
        GM_Object[] result = new GM_Object[size()];
        initIterator();
        int i=0;
        while (hasNext()) {
            result[i] = next();
            i++;
        }   
        return result;     
    }

    /** NON IMPLEMENTE (renvoie null).
     * Collection de GM_Object repr�sentant la fronti�re de self.
     * Cette collection d'objets a une structure de GM_Boundary, qui est un sous-type de GM_Complex. 
     */
    /*public GM_Boundary boundary() {
        return null;
    }   */ 
    
    /** Constructeur par d�faut. */
    public GM_Aggregate() {
        element = new ArrayList();
    }
    
    /** Constructeur � partir d'un GM_Object. */
    public GM_Aggregate (GM_Object anObject) {
        element = new ArrayList();
        this.add(anObject);
    }
    
    /**Constructeur � partir d'une liste de GM_Object. */
    public GM_Aggregate (List fromSet) {
        element = new ArrayList();
        int n = fromSet.size();
        if (n > 0) 
            for (int i=0; i<n; i++)
                this.add((GM_Object)fromSet.get(i));
    }
    
                    
    /** Renvoie une DirectPositionList de tous les points de l'objet. */
    public DirectPositionList coord() {
        DirectPositionList result = new DirectPositionList();
        initIterator();
        while (hasNext())
            result.addAll(next().coord());
        return result;
    }
    
    
    /** Pour l'affichage des coordonnees. */
    /*public String toString() {
        String result = new String();
        initIterator();
        int i=0;
        while (hasNext()) {
            result = result+"\nGM_Aggregate - element "+i+"\n";
            result = result+next().toString();
        }
        return result;
    }*/
        
}
