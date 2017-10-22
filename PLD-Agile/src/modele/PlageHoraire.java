package modele;

import java.util.Calendar;

/**
 * Intervalle de temps dans lequel doit s’effectuer une livraison
 * @author 4104
 */
public class PlageHoraire {
	private Calendar debutPlage;
	private Calendar finPlage;
	
	public Calendar getDebut() {
		return debutPlage;
	}
	
	public Calendar getFin() {
		return finPlage;
	}
	
}
