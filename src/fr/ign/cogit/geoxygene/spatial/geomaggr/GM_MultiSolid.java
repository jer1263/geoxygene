/*
 * This file is part of the GeOxygene project source files.
 * GeOxygene aims at providing an open framework which implements OGC/ISO
 * specifications for
 * the development and deployment of geographic (GIS) applications. It is a open
 * source
 * contribution of the COGIT laboratory at the Institut Géographique National
 * (the French
 * National Mapping Agency).
 * See: http://oxygene-project.sourceforge.net
 * Copyright (C) 2005 Institut Géographique National
 * This library is free software; you can redistribute it and/or modify it under
 * the terms
 * of the GNU Lesser General Public License as published by the Free Software
 * Foundation;
 * either version 2.1 of the License, or any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with
 * this library (see file LICENSE if present); if not, write to the Free
 * Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package fr.ign.cogit.geoxygene.spatial.geomaggr;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.spatial.geomcomp.GM_CompositeSolid;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;

/**
 * Agrégation de solides.
 * 
 * @author Thierry Badard & Arnaud Braun & Mickael Brasebin
 * @version 1.0
 */

public class GM_MultiSolid<GM_Sol extends GM_Solid> extends
        GM_MultiPrimitive<GM_Solid> {
    static Logger logger = Logger.getLogger(GM_MultiSolid.class.getName());

    /**
     * NON IMPLEMENTE (renvoie 0.0).
     * Volume total.
     */
    // Dans la norme, ceci est un attribut et non une méthode.
    // Dans la norme, cet attribut est de type Volume et non double
    public double volume() {
        logger
                .error("Non implémentée, utiliser : return CalculSansJava3D.CalculVolume(this); (renvoie 0.0)"); //$NON-NLS-1$
        return 0.0;
    }

    /**
     * NON IMPLEMENTE (renvoie 0.0).
     * Aire totale.
     */
    // Dans la norme, ceci est un attribut et non une méthode.
    // Dans la norme, cet attribut est de type Area et non double
    @Override
    public double area() {
        logger
                .error("Non implémentée, utiliser : return CalculSansJava3D.CalculAire(this); (renvoie 0.0)"); //$NON-NLS-1$
        return 0.0;
    }

    /** Constructeur par défaut. */
    public GM_MultiSolid() {
        this.element = new ArrayList<GM_Solid>();
    }

    /** Constructeur à partir d'un GM_CompositeSolid. */
    public GM_MultiSolid(GM_CompositeSolid compSolid) {
        this.element = new ArrayList<GM_Solid>();
        this.addAll(compSolid.getGenerator());
    }

    /** Constructeur à partir d'une liste de GM_Solid. */
    public GM_MultiSolid(List<GM_Solid> lOS) {
        this.element = new ArrayList<GM_Solid>();
        this.element.addAll(lOS);
    }

    @Override
    public Object clone() {
        GM_MultiSolid<GM_Sol> agg = new GM_MultiSolid<GM_Sol>();
        for (GM_Solid elt : this.element)
            agg.add((GM_Solid) elt.clone());
        return agg;
    }
}
