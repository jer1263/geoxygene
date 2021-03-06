package fr.ign.cogit.geoxygene.appli.plugin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.measure.quantity.RadiationDoseAbsorbed;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.commons.io.FileUtils;

import fr.ign.cogit.cartagen.core.genericschema.road.IRoadLine;
import fr.ign.cogit.cartagen.software.dataset.CartAGenDoc;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.feature.type.GF_AttributeType;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.appli.GeOxygeneApplication;
import fr.ign.cogit.geoxygene.appli.Viewport;
import fr.ign.cogit.geoxygene.appli.api.ProjectFrame;
import fr.ign.cogit.geoxygene.appli.plugin.GeOxygeneApplicationPlugin;
import fr.ign.cogit.geoxygene.appli.render.RenderUtil;
import fr.ign.cogit.geoxygene.osm.schema.OsmGeneObj;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOJeu.AttributeType;
import fr.ign.cogit.geoxygene.schemageo.api.routier.TronconDeRoute;
import fr.ign.cogit.geoxygene.style.LabelPlacement;
import fr.ign.cogit.geoxygene.style.Layer;
import fr.ign.cogit.geoxygene.style.LinePlacement;
import fr.ign.cogit.geoxygene.style.Placement;
import fr.ign.cogit.geoxygene.style.PointPlacement;
import fr.ign.cogit.geoxygene.style.Symbolizer;
import fr.ign.cogit.geoxygene.style.TextSymbolizer;
import fr.ign.cogit.geoxygene.util.ColorUtil;
import fr.ign.cogit.geoxygene.util.algo.JtsAlgorithms;
import java_cup.sym;

public class BlindPlugin implements GeOxygeneApplicationPlugin, ActionListener {
	public final static boolean IS_BLIND_ENABLE = true;

	/** Logger. */
	private final static Logger LOGGER = Logger.getLogger(BlindPlugin.class.getName());

	/** GeOxygeneApplication */
	private GeOxygeneApplication application = null;

	/** Noms des sous menus */
	private static String itemBrailleName = "Noms braille";
	private static String itemGreyLevelMap = "Map niveau gris";
	private static String itemExtractHtmlLegend = "Générer légende HTML";
	private static String itemParameters = "Parametres";

	private static String itemNomComplet = "Noms complets";
	private static String itemSimplificationVoies = "Simplication voies";
	private static String itemSimplificationMaximum = "Simplification max";

	private static int Choice = 0; // 0 : Nom complet ; 1 : Simplification noms
									// voies ; 2 : Simplification maximum
									// (initiales) ;

	private StringBuilder htmlStringBuilder;

	public BlindPlugin() {
	}

	/**
	 * Initialize the plugin.
	 * 
	 * @param application
	 *            the application
	 */
	@Override
	public final void initialize(final GeOxygeneApplication application) {
		this.application = application;

		JMenuBar menuBar = application.getMainFrame().getMenuBar();
		JMenu blindMenu = new JMenu("Blind");

		JMenu menuBrailleName = new JMenu(itemBrailleName);
		menuBrailleName.add(new JMenuItem(itemNomComplet));
		menuBrailleName.add(new JMenuItem(itemSimplificationVoies));
		menuBrailleName.add(new JMenuItem(itemSimplificationMaximum));

		menuBrailleName.getItem(0).addActionListener(this);
		menuBrailleName.getItem(1).addActionListener(this);
		menuBrailleName.getItem(2).addActionListener(this);

		JMenuItem menuGreyLevelMap = new JMenuItem(itemGreyLevelMap);
		menuGreyLevelMap.addActionListener(this);

		JMenuItem menuExtractHtmlLegend = new JMenuItem(itemExtractHtmlLegend);
		menuExtractHtmlLegend.addActionListener(this);

		JMenuItem menuParameters = new JMenuItem(itemParameters);
		menuParameters.addActionListener(this);

		blindMenu.add(menuBrailleName);
		blindMenu.add(menuGreyLevelMap);
		blindMenu.add(menuExtractHtmlLegend);
		blindMenu.addSeparator();
		blindMenu.add(menuParameters);

		menuBar.add(blindMenu, menuBar.getMenuCount() - 1);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(final ActionEvent e) {

		if (e.getActionCommand() == itemGreyLevelMap) {
			actionItemGreyLevelMap();
		} else if (e.getActionCommand() == itemExtractHtmlLegend) {
			actionItemExtractHtmlLegend();
		} else if (e.getActionCommand() == itemParameters) {
			actionItemParameters();
		} else if (e.getActionCommand() == itemNomComplet) {
			actionSimplificationNomComplet();
		} else if (e.getActionCommand() == itemSimplificationVoies) {
			actionSimplificationVoies();
		} else if (e.getActionCommand() == itemSimplificationMaximum) {
			actionSimplificationMaximum();
		}

		application.getMainFrame().getSelectedProjectFrame().getLayerViewPanel().repaint();

	}

	/** 
	 * On affiche le nom complet des rues en récupérant le fullname (Rue Fontgieve)
	 */
	private void actionSimplificationNomComplet() {
		Choice = 1;

		for (IRoadLine road : CartAGenDoc.getInstance().getCurrentDataset().getRoads()) {

			if (road.getAttribute("name") != null) {
				if (road.getAttribute("fullname") == null) {
					((OsmGeneObj) road).addTag("fullname", road.getAttribute("name").toString());
				}
				((OsmGeneObj) road).addTag("name", road.getAttribute("fullname").toString());
			}
		}

		System.out.println(
				"Taille de la liste Cartagen : " + CartAGenDoc.getInstance().getCurrentDataset().getRoads().size());

	}

	/**
	 *  On affiche les noms en simplifiant uniquement le nom des voies (Rue -> R, etc)
	 */
	private void actionSimplificationVoies() {

		Choice = 2;

		// CartAGenDoc.getInstance().getCurrentDataset().getRoads().getElements().clear();
		// CartAGenDoc.getInstance().getCurrentDataset().getRoads().getElements().addAll(saveRoads);

		for (IRoadLine road : CartAGenDoc.getInstance().getCurrentDataset().getRoads()) {

			if (road.getAttribute("name") != null) {
				if (road.getAttribute("fullname") == null) {
					((OsmGeneObj) road).addTag("fullname", road.getAttribute("name").toString());
				}
				((OsmGeneObj) road).addTag("name", simplifyNomVoies(String.valueOf(road.getAttribute("fullname"))));
			}

		}
	}

	/**
	 *  Remplace les noms des voies avec leur simplification, liste non exhaustive amenée à évoluer avec le temps
	 * @param name Nom à simplifier
	 * @return
	 */
	private String simplifyNomVoies(String name) {
		System.out.println(name);
		// On simplifie le nom des voies de circulation (Avenue -> Av, etc)
		String simplify = name;
		simplify = simplify.replace("Avenue", "Av");
		simplify = simplify.replace("avenue", "Av");
		simplify = simplify.replace("Place", "Pl");
		simplify = simplify.replace("place", "Pl");
		simplify = simplify.replace("Boulevard", "Pl");
		simplify = simplify.replace("boulevard", "Pl");
		simplify = simplify.replace("Impasse", "Imp");
		simplify = simplify.replace("impasse", "Imp");
		simplify = simplify.replace("Rue", "R");
		simplify = simplify.replace("rue", "R");
		return simplify;
	}
	
	/**
	 *  On affiche les noms des rues simplifiés au maximum (Rue Fontgieve -> RF)
	 */
	private void actionSimplificationMaximum() {
		Choice = 3;

		for (IRoadLine road : CartAGenDoc.getInstance().getCurrentDataset().getRoads()) {
			if (road.getAttribute("name") != null) {
				if (road.getAttribute("fullname") == null) {
					((OsmGeneObj) road).addTag("fullname", road.getAttribute("name").toString());
				}
				((OsmGeneObj) road).addTag("name", getFirstLetters(road.getAttribute("fullname").toString()));
			}
		}
	}

	/**
	 * Méthode qui retourne les premières lettres de chaque mot du string en parametre
	 * @param text
	 * @return
	 */
	private String getFirstLetters(String text) {
		String firstLetters = "";
		text = text.replaceAll("[.,]", "");
		text = text.replaceAll("'", " ");
		for (String s : text.split(" ")) {
			firstLetters += s.charAt(0);
		}
		return firstLetters.trim();
	}

	private void actionItemGreyLevelMap() {

	}

	/**
	 * Méthode appelée au clic sur la génération de la légende HTML
	 */
	private void actionItemExtractHtmlLegend() {
		initializeHtmlBlindLegend();
		for (IRoadLine road : CartAGenDoc.getInstance().getCurrentDataset().getRoads()) {
			if (road.getAttribute("fullname") == null && road.getAttribute("name") != null) {
				((OsmGeneObj) road).addTag("fullname", road.getAttribute("name").toString());
			}
			if (road.getAttribute("fullname") != null) {
				addItemHtmlBlindLegend(road.getAttribute("fullname").toString());
			}
		}
		closeAndSaveHtmlBlindLegend();

	}

	private void actionItemParameters() {

	}

	/**
	 * Method to initialize HTML File to make a legend of simplify words in
	 * Braille
	 */
	private void initializeHtmlBlindLegend() {
		htmlStringBuilder = new StringBuilder();
		htmlStringBuilder.append(
				"<!doctype html><html lang=\"fr\"><head><meta charset=\"UTF-8\"><title>Blind Legend</title></head>");
		htmlStringBuilder.append("<body>");
		htmlStringBuilder.append("<table border=\"1\" bordercolor=\"#000000\">");
		htmlStringBuilder
				.append("<tr><td><b>Simplify Braille</b></td><td><b>Braille</b></td><td><b>Name</b></td></tr>");
	}

	/**
	 * Method to add an item in the HTML blind legend
	 * 
	 * @param name
	 *            Name to add to HTML legend
	 */
	private void addItemHtmlBlindLegend(String name) {
		// On filtre pour ne pas stocker 2 fois le meme nom (cas où la route est
		// en plusieurs segments par exemple)
		if (!htmlStringBuilder.toString().contains(name) && name != "null") {
			htmlStringBuilder.append("<tr><td><span style=\"font-family:Braille TBFr2007 INS HEA thermo;\">"
					+ simplifyNameWithChoice(name)
					+ "</span></td><td><span style=\"font-family:Braille TBFr2007 INS HEA thermo;\">" + name
					+ "</span></td><td>" + name + "</td></tr>");
		}
	}

	private String simplifyNameWithChoice(String name) {
		switch (Choice) {
		case 1:
			return name;
		case 2:
			return simplifyNomVoies(name);
		case 3:
			return getFirstLetters(name);
		}
		return name;
	}

	/**
	 * Method to close and save the Html Legend in an extern file
	 */
	private void closeAndSaveHtmlBlindLegend() {
		try {
			htmlStringBuilder.append("</table></body></html>");
			File file = new File("Blind_Legend.html");
			FileUtils.write(file, htmlStringBuilder.toString());
			System.out.println("Légende HTML exportée avec succès : " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Essais sur la simplification des noms de rues / Non fonctionnel 
	
	private List<String> list_roads = new ArrayList<>();

	private List<String> simplifyRoadsNameDoublons(List<String> list_roads) {
		List<String> list_roads_simplified = new ArrayList<>();
		// On peuple la liste des noms de route (à faire avant de la passer en
		// paramètre en condition réel)
		list_roads.add("Rue Fontgiève");
		list_roads.add("Rue Fontaine");
		list_roads.add("Boulevard Fontaine");
		list_roads.add("Boulevard François Mitterand");

		for (String name : list_roads) {
			String name_simp = simplifyNameWithChoice(name);
			if (list_roads_simplified.contains(name_simp)) {
				deepSimplify(name_simp);
			}
			list_roads_simplified.add(name_simp);
		}

		List<String> sameName = new ArrayList<>();
		for (String name : list_roads) {

		}

		System.out.println(list_roads);
		System.out.println(list_roads_simplified);

		return list_roads_simplified;
	}

	private String deepSimplify(String name){
		String namesimp = "";
		
		return namesimp;
	}
}
