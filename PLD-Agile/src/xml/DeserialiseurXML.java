package xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import modele.DemandeLivraison;
import modele.Plan;


public class DeserialiseurXML {
	/**
	 * Ouvre un fichier xml et cree plan et demandeLivraison a partir du contenu du fichier
	 * @param plan
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ExceptionXML
	 */
	public static void charger(Plan plan) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		File xml = OuvreurDeFichierXML.getInstance().ouvre(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("reseau")) {
           construireAPartirDeDOMXML(racine, plan);
        }
        else
        		throw new ExceptionXML("Document non conforme");
	}
	
	public static void charger(DemandeLivraison demandeLivraison)throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		File xml = OuvreurDeFichierXML.getInstance().ouvre(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("reseau")) {
           construireAPartirDeDOMXML(racine, demandeLivraison);
        }
        else
        		throw new ExceptionXML("Document non conforme");
	}


    private static void construireAPartirDeDOMXML(Element noeudDOMRacine, Plan plan) throws ExceptionXML, NumberFormatException
    {
    		NodeList listeNoeuds = noeudDOMRacine.getElementsByTagName("noeud");
       	for (int i = 0; i < listeNoeuds.getLength(); i++) {
       		Element xmlNoeud = (Element) listeNoeuds.item(i);
       		plan.ajoute(Integer.parseInt(xmlNoeud.getAttribute("x")), Integer.parseInt(xmlNoeud.getAttribute("y")), Long.parseLong(xmlNoeud.getAttribute("id")));
       	}
       	NodeList listeTroncons = noeudDOMRacine.getElementsByTagName("troncon");
       	for (int i = 0; i < listeTroncons.getLength(); i++) {
       		Element xmlTroncon = (Element) listeTroncons.item(i);
          	try {
				plan.ajoute(Long.parseLong(xmlTroncon.getAttribute("origine")), Long.parseLong(xmlTroncon.getAttribute("destination")), Float.parseFloat(xmlTroncon.getAttribute("longueur")), xmlTroncon.getAttribute("nomRue"));
			} catch (Exception e) {
				throw new ExceptionXML(e.getMessage());
			}
       	}
       	System.out.println("Fin");
    }
    
    private static void construireAPartirDeDOMXML(Element noeudDOMRacine, DemandeLivraison demandeLivraison) throws ExceptionXML, NumberFormatException
    {
    NodeList listeAdresse = noeudDOMRacine.getElementsByTagName("livraison");
    NodeList listeAdresse = noeudDOMRacine.getElementsByTagName("livraison");
    Element xmlEntrepot = (Element) listeAdresse.item(0);
       	for (int i = 1; i < listeAdresse.getLength(); i++) {
       		Element xmlAdresse = (Element) listeAdresse.item(i);
          	try {
          		demandeLivraison.ajoute(Long.parseLong(xmlAdresse.getAttribute("id")), Integer.parseInt(xmlAdresse.getAttribute("duree")));
			} catch (Exception e) {
				throw new ExceptionXML(e.getMessage());
			}
       	}
       	System.out.println("Fin");
    }
}
