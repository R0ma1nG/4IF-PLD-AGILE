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

Classe représentant l'état de l'app lorsque la tournée a été calculé.
@author 4104
 */
package controleur;

import java.io.IOException;

import modele.ExceptionPlanCo;
import modele.Intersection;
import modele.Livraison;
import modele.Plan;
import modele.Tournee;
import vue.CharteGraphique;
import vue.Fenetre;
import vue.Textes;

public class EtatCalcule extends EtatPlanOuvert {

	@Override 
	public void obtenirPlusProcheIntersection(Fenetre vue, Plan p ,double x, double y) {
		Intersection i = p.obtenirPlusProcheIntersection(x, y);
		vue.ajouterIcone(i);
	}

	@Override 
	public void commencerChoixIntersection(Fenetre vue) {
		vue.commencerChoixIntersection();
	}

	@Override
	public void creerLivraison(Fenetre fenetre) {
		fenetre.getVueTournee().creerLivraison();
	} 
	
	@Override
	public void creerLivraisonApres(Fenetre fenetre, int position) {
		//fenetre.setEtatCourant(fenetre.etatAjoutLivraison);
		//fenetre.goToVue();
		fenetre.getVueTournee().creerLivraisonApres(position);
	} 

	@Override
	public void ajouterLivraison(Fenetre fenetre, Plan p, Livraison l, ListeCommande listeCmd, int position) {
		try {
			listeCmd.ajoute(new CommandeAjouter(p, l, position));
			fenetre.changeNotification(Textes.NOTIF_LIVRAISON_AJOUTEE, CharteGraphique.NOTIFICATION_COLOR);
		}catch (ExceptionPlanCo ex){
			fenetre.changeNotification(ex.getMessage(), CharteGraphique.NOTIFICATION_FORBIDDEN_COLOR);
			// TODO : traiter l'exception
		}finally {
			fenetre.setEtatCourant(fenetre.etatModifie);
			fenetre.goToVue();
		}
	}

	@Override
	public void deplacerLivraison() {
		// TODO Auto-generated method stub
	}

	@Override
	public void supprimerLivraison(Fenetre fenetre, Plan p, Livraison l, ListeCommande listeCmd, int position) {
		try {
			listeCmd.ajoute(new CommandeSupprimer(p, l, position));
			fenetre.changeNotification(Textes.NOTIF_LIVRAISON_SUPPRIMEE, CharteGraphique.NOTIFICATION_COLOR);
		}catch (ExceptionPlanCo ex){
			fenetre.changeNotification(ex.getMessage(), CharteGraphique.NOTIFICATION_FORBIDDEN_COLOR);
		}finally {
			fenetre.setEtatCourant(fenetre.etatModifie);
			fenetre.goToVue();
		}
	}

	@Override
	public void undo(ListeCommande listeCommande, Fenetre fenetre) {
		try {
			listeCommande.undo();
			fenetre.changeNotification(Textes.NOTIF_UNDO, CharteGraphique.NOTIFICATION_COLOR);
			fenetre.goToVue();
		} catch (ExceptionPlanCo e) {
			// TODO Gérer exception
		}
	}
	
	@Override
	public void redo(ListeCommande listeCommande, Fenetre fenetre) {
		try {
			listeCommande.redo();
			fenetre.changeNotification(Textes.NOTIF_REDO, CharteGraphique.NOTIFICATION_COLOR);
			fenetre.goToVue();
		} catch (ExceptionPlanCo e) {
			// TODO Gérer exception
		}
	}

	@Override
	public void exporterFeuilleDeRoute(Fenetre fenetre, Tournee tournee) {
		try {
			tournee.exportFeuilleDeRoute();
			fenetre.changeNotification(Textes.NOTIF_FDR_EXPORTEE, CharteGraphique.NOTIFICATION_COLOR);

		} catch (IOException e) {
			fenetre.changeNotification(e.getMessage(), CharteGraphique.NOTIFICATION_FORBIDDEN_COLOR);
		} catch (ExceptionPlanCo e) {
			fenetre.changeNotification(e.getMessage(), CharteGraphique.NOTIFICATION_FORBIDDEN_COLOR);
		}
	}

	@Override
	public void afficherNotif(Fenetre fenetre) {
		fenetre.changeNotification(Textes.NOTIF_TOURNEE_CALCULE, CharteGraphique.NOTIFICATION_COLOR);
	}

	@Override
	public void afficherFenetre(Fenetre fenetre) {
		fenetre.setEtatCourant(fenetre.etatCalcule);
		fenetre.goToVue();
	}

	@Override
	public void appuiEntree(Controleur controleur, Plan plan, Fenetre fenetre, ListeCommande listeCommande) {

	}

	@Override 
	public void annulerCreation(Fenetre fenetre) {
		fenetre.annulerCreation();
	}

}
