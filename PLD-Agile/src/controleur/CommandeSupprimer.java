/**
 * This file has been created by vbonin, on 11 oct. 2017
 * Authors : 
 * romain.goutte-fangeas@insa-lyon.fr
 * lucas.ouaniche-herbin@insa-lyon.fr
 * lucas.marie@insa-lyon.fr
 * clara.pourcel@insa-lyon.fr
 * pierrick.chauvet@insa-lyon.fr
 * bastien.guiraudou@insa-lyon.fr
 * victor.bonin@insa-lyon.fr

_____   _   _____   __   _   _     _   _____   __   _   _   _   _____  
|  _  \ | | | ____| |  \ | | | |   / / | ____| |  \ | | | | | | | ____| 
| |_| | | | | |__   |   \| | | |  / /  | |__   |   \| | | | | | | |__   
|  _  { | | |  __|  | |\   | | | / /   |  __|  | |\   | | | | | |  __|  
| |_| | | | | |___  | | \  | | |/ /    | |___  | | \  | | |_| | | |___  
|_____/ |_| |_____| |_|  \_| |___/     |_____| |_|  \_| \_____/ |_____| 

Classe représentant la commande de suppression d'un point de livraison.
Une commande peut être exécutée et annulée.
@author 4104
 */
package controleur;

import modele.Livraison;
import modele.Plan;

public class CommandeSupprimer implements Commande {

	private Plan plan;
	private Livraison livraison;
	
	/**
	 * Cree la commande qui supprime la livraison l du plan p
	 * @param p
	 * @param l
	 */
	public CommandeSupprimer(Plan p, Livraison l) {
		plan = p;
		livraison = l;
	}
	
	@Override
	public void doCde() {
		// Appeller plan.supprimerLivraison(Livraison)		
	}

	@Override
	public void undoCde() {
		// Appeller plan.ajouterLivraison(Livraison)
	}

}
