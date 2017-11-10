package controleur;

import modele.ExceptionPlanCo;
import modele.Livraison;
import modele.Plan;

/**
 * <pre>
 * Commande qui ajoute une livraison a la demande de livraison actuelle.
 * 
 * Authors : 
 * romain.goutte-fangeas@insa-lyon.fr
 *               ____
 *           __--    --_
 *          /   -        -
 *         / /-- ------\  \
 *        / /           \  |
 *        | |           ?  |
 *        | ? _--   -== \ /?
 *         \| 'o > < o>  |||
 *         \\    / \      )|
 *          \\   .| )    |_/
 *           |  :_____: :|
 *            \  <==="  /|
 *             \      .: /|\
 *             )\_   .: / |:"--___
 *         __-:|\ """ _-  |:::::::
 *       _-::::\ "-_.-   /::::::::
 *    _--:::::::| .|"-_  |::::::::
 *  -"::::::::::\  | { -_|::::::::
 * lucas.ouaniche-herbin@insa-lyon.fr
 * lucas.marie@insa-lyon.fr
 * clara.pourcel@insa-lyon.fr
 * pierrick.chauvet@insa-lyon.fr
 * bastien.guiraudou@insa-lyon.fr
 * victor.bonin@insa-lyon.fr
 * </pre>
 * 
 *  
 * @author 4104
 */
public class CommandeAjouter implements Commande {

	private Plan plan;
	private Livraison livraison;
	private int position;
	
	/**
	 * Cree la commande qui ajoute la livraison l au plan p
	 * @param plan : Plan	
	 * @param livraison : Livraison
	 */
	public CommandeAjouter(Plan plan, Livraison livraison, int positionDansListe) {
		this.plan = plan;
		this.livraison = livraison;
		position = positionDansListe;
	}
	
	/** {@inheritDoc}  */
	@Override
	public void doCde() throws ExceptionPlanCo {
		plan.ajouterPointLivraison(livraison, position);
	}

	/** {@inheritDoc}  */
	@Override
	public void undoCde() throws ExceptionPlanCo {
		plan.supprimerPointLivraison(livraison);
	}

}
