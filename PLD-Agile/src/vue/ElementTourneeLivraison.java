package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

import controleur.Controleur;
import modele.Intersection;
import modele.Livraison;
import modele.LivraisonPlageHoraire;

/**
 * <pre>
 * Extension de ElementTournee affichant un element d'une tournee de type Livraison
 * @see modele.Livraison
 * @see ElementTournee
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
 *         \| 'o . . o.  |||
 *         \\    / \      )|
 *          \\   .| )    |_/
 *           |  :_____: :|
 *            \  '==="  /|
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
public class ElementTourneeLivraison extends ElementTournee{
	private static final long serialVersionUID = 1475692407722873597L;

	private Livraison livraison;

	private Calendar date;
	private int position;

	private JLabel dureeLivraisonLabel;
	private JButton boutonAction;
	private JLabel heureLabel;
	private JSpinner dureeModification;

	/**
	 * Contructeur d'un ElementTourneeLivraison a partir d'une livraison existante
	 * @param ctrl : le controleur associe a la vue
	 * @param livraison : la livraison a afficher
	 * @param nom : le nom de la livraison
	 * @param p : la place de la livraison dans la liste de livraisons de la tournee
	 */
	public ElementTourneeLivraison(Controleur ctrl, Livraison livraison, int nom, int p) {
		super(ctrl);

		position = p;
		this.livraison = livraison;

		initialiserLivraison();

		nomLabel.setText(Textes.TOURNEE_LIVRAISON + nom + " - ");
		idLabel.setText(" " + livraison.getId());
		dureeLivraisonLabel.setText(Textes.TOURNEE_DUREE + (int)(livraison.getDuree()/60) + " min");

		// Recuperation de l'icone
		try {
			BufferedImage img = ImageIO.read(new File(CharteGraphique.ICONE_SUPPRIMER));
			Image scaledSupprimer = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
			ImageIcon supprimerIcon = new ImageIcon(scaledSupprimer);
			boutonAction.setIcon(supprimerIcon);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Recuperation de la liste des troncons passant par l'intersection
		Set<String> listeTronconsIntersection = ctrl.nomsTronconsIntersection(livraison);
		JPanel nomsTronconsIntersection = new JPanel();
		nomsTronconsIntersection.setLayout(new BoxLayout(nomsTronconsIntersection, BoxLayout.PAGE_AXIS));
		nomsTronconsIntersection.setBackground(Color.WHITE);
		for(String nomTroncon : listeTronconsIntersection){
			if (nomTroncon.equals("")){
				nomTroncon = "Rue Inconnue";
			}
			JLabel labelNomTroncon = new JLabel (nomTroncon);
			nomsTronconsIntersection.add(labelNomTroncon);
			labelNomTroncon.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		
		// Affichage eventuel de la plage horaire
		String plageHoraire = Textes.TOURNEE_PLAGE;
		if(livraison instanceof LivraisonPlageHoraire) {
			LivraisonPlageHoraire livraisonHoraire = (LivraisonPlageHoraire)livraison;
			if(livraisonHoraire.getDebut()!= null)
				plageHoraire+=  livraisonHoraire.getDebut().get(Calendar.HOUR_OF_DAY) + "h";
			else
				plageHoraire+= ".";
			plageHoraire+= " - ";
			if(livraisonHoraire.getFin()!= null)
				plageHoraire+= livraisonHoraire.getFin().get(Calendar.HOUR_OF_DAY) + "h";
			else
				plageHoraire+= ".";
			JLabel labelPlageHoraire = new JLabel (plageHoraire);
			details.add(labelPlageHoraire, BorderLayout.WEST);
		}
		details.add(nomsTronconsIntersection, BorderLayout.NORTH);
		details.setVisible(false);

		// Creation de la popup de description de la livraison
		String description = composeToolTipString(livraison, listeTronconsIntersection);
		setToolTipText("<html>" + description + "</html>");

		if (livraison.getHeurePassage() != null) {
			date = livraison.getHeurePassage();
			String texte = Textes.TOURNEE_PASSAGE + date.get(Calendar.HOUR_OF_DAY) + "h";
			if(date.get(Calendar.MINUTE)<10) {
				texte += "0";
			}
			texte += date.get(Calendar.MINUTE);
			heureLabel.setText(texte);
		}

		ecouteurBoutons = new EcouteurDeBoutonsElementTournee(ctrl, this);
		boutonAction.addActionListener(ecouteurBoutons);
		boutonAction.setActionCommand("supprimer-livraison");

		// Creation du menu contextuel pour ajouter apres
		menu = new JPopupMenu("Popup");
		JMenuItem item = new JMenuItem("Nouvelle livraison");
		menu.add(item);
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.creerLivraisonApres(position);
			}
		});
		
		// Si la plage horaire de la livraison est tendue, on affiche une pastille rouge a cote de l'incone
		if(livraison instanceof LivraisonPlageHoraire) {
			int retard = ((LivraisonPlageHoraire)livraison).getRetardPossible();
			if(retard <= 0) {
				JPanel indicationPlageTendue = new JPanel() {
					@Override
				    protected void paintComponent(Graphics g) {
						Graphics2D g2d = (Graphics2D)g;
						g2d.setColor(CharteGraphique.NOTIFICATION_INTERDIT_COULEUR);
						Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, g.getClipBounds().width, g.getClipBounds().height);
						g2d.fill(circle);
				    }
				};
				GridBagConstraints c = new GridBagConstraints();
				c.anchor = GridBagConstraints.NORTHWEST;
				c.gridx = 0;
			    c.gridy = 0;
			    c.weighty = 1;
			    c.gridheight = 2;
			    c.insets = new Insets(0,0,0,10);
				add(indicationPlageTendue, c);
				indicationPlageTendue.setBackground(CharteGraphique.LIVRAISON_RETARD);
			}
		}
	}

	/**
	 * Contructeur d'un ElementTourneeLivraison destinee a la creation d'une nouvelle livraison
	 * @param ctrl : controleur associe a la vue
	 * @param p : place ou inserer la nouvelle livraison dans la liste de livraisons de la tournee
	 */
	public ElementTourneeLivraison(Controleur ctrl, int p) {
		super(ctrl);
		
		position = p;
		
		initialiserLivraison();
		nomLabel.setText(Textes.TOURNEE_LIVRAISON_NOUVELLE);
		idLabel.setText("");
		dureeLivraisonLabel.setText(Textes.TOURNEE_DUREE + "- min");
		heureLabel.setText(Textes.TOURNEE_PASSAGE + "-");

		JButton boutonAnnuler = new JButton();
		boutonAnnuler.setFocusPainted(false);
		boutonAnnuler.setBackground(CharteGraphique.BG_COULEUR);
		boutonAnnuler.setBorder(null);
		try {
			BufferedImage img = ImageIO.read(new File(CharteGraphique.ICONE_ANNULER));
			Image scaledAnnuler = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
			ImageIcon annulerIcon = new ImageIcon(scaledAnnuler);
			boutonAnnuler.setIcon(annulerIcon);
			nomPanel.add(boutonAnnuler, BorderLayout.EAST);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedImage img = ImageIO.read(new File(CharteGraphique.ICONE_VALIDER));
			Image scaledValider = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
			ImageIcon validerIcon = new ImageIcon(scaledValider);
			boutonAction.setIcon(validerIcon);

		} catch (IOException e) {
			e.printStackTrace();
		}
		boutonAction.setEnabled(false);

		JPanel choixIntersec = new JPanel();
		choixIntersec.setLayout(new BorderLayout());
		choixIntersec.setBackground(CharteGraphique.BG_COULEUR);
		JLabel texteChoixIntersec = new JLabel(Textes.TOURNEE_INTERSECTION);
		JButton boutonChoixIntersec = new JButton();
		texteChoixIntersec.setFont(CharteGraphique.TEXT_SECONDAIRE_POLICE);
		boutonChoixIntersec.setFocusPainted(false);
		boutonChoixIntersec.setBackground(CharteGraphique.BG_COULEUR);
		try {
			BufferedImage bouton = ImageIO.read(new File(CharteGraphique.ICONE_LIVRAISON_BOUTON));
			Image scaledBouton = bouton.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
			ImageIcon boutonIcon = new ImageIcon(scaledBouton);
			boutonChoixIntersec.setIcon(boutonIcon);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creation d'un JSpinner pour entrer la duree
		JPanel choixDuree = new JPanel();
		choixDuree.setLayout(new BorderLayout());
		choixDuree.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		choixDuree.setBackground(CharteGraphique.BG_COULEUR);
		SpinnerModel modele =
				new SpinnerNumberModel(0, 
						0, 
						10000, 
						1); 
		dureeModification = new JSpinner(modele);
		JFormattedTextField duree = ((JSpinner.NumberEditor) dureeModification.getEditor()).getTextField();
		((NumberFormatter) duree.getFormatter()).setAllowsInvalid(false);

		JLabel texteModifDuree = new JLabel(Textes.TOURNEE_DUREE_CREATION);
		texteModifDuree.setFont(CharteGraphique.TEXT_SECONDAIRE_POLICE);

		choixIntersec.add(texteChoixIntersec, BorderLayout.WEST);
		choixIntersec.add(boutonChoixIntersec, BorderLayout.EAST);
		choixDuree.add(texteModifDuree, BorderLayout.WEST);
		choixDuree.add(dureeModification, BorderLayout.EAST);
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		pan.add(choixIntersec, BorderLayout.PAGE_START);
		pan.add(choixDuree, BorderLayout.PAGE_END);
		details.add(pan, BorderLayout.PAGE_START);
		details.add(boutonAction, BorderLayout.EAST);

		ecouteurBoutons = new EcouteurDeBoutonsElementTournee(ctrl, this);
		boutonChoixIntersec.addActionListener(ecouteurBoutons);
		boutonChoixIntersec.setActionCommand("choisir-intersection");
		boutonAction.addActionListener(ecouteurBoutons);
		boutonAction.setActionCommand("valider-creation");
		boutonAnnuler.addActionListener(ecouteurBoutons);
		boutonAnnuler.setActionCommand("annuler-creation");

	}
	
	/**
	 * Initialisation des JComponents de l'element pour une liraison existante et une nouvelle livraison
	 */
	private void initialiserLivraison() {

		dureeLivraisonLabel = new JLabel();

		nomLabel.setFont(CharteGraphique.TEXTE_GRAND_POLICE);
		idLabel.setFont(CharteGraphique.TEXT_SECONDAIRE_POLICE);
		idLabel.setForeground(CharteGraphique.TEXT_SECONDAIRE_COULEUR);
		dureeLivraisonLabel.setFont(CharteGraphique.TEXTE_PETIT_POLICE);

		heureLabel = new JLabel();
		heureLabel.setFont(CharteGraphique.TEXTE_PETIT_POLICE);

		boutonAction = new JButton();
		boutonAction.setFocusPainted(false);
		boutonAction.setBackground(CharteGraphique.BG_COULEUR);
		boutonAction.setBorder(null);

		try {
			BufferedImage imgNorm = ImageIO.read(new File(CharteGraphique.ICONE_LIVRAISON));
			Image scaledImageNormal = imgNorm.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
			imageIconeNormal = new ImageIcon(scaledImageNormal);
			BufferedImage imgSurvol = ImageIO.read(new File(CharteGraphique.ICONE_LIVRAISON_SURVOL));
			Image scaledImageSurvol = imgSurvol.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
			imageIconeSurvol = new ImageIcon(scaledImageSurvol);
		} catch (IOException e) {
		}

		imageLabel.setIcon(imageIconeNormal);

		infos.add(dureeLivraisonLabel, BorderLayout.PAGE_START );
		infos.add(heureLabel, BorderLayout.WEST );

	}

	public Livraison getLivraison() {
		return livraison;
	}

	public void setDuree() {
		livraison.setDuree((Integer)dureeModification.getValue()*60);
	}

	/**
	 * Modifie l'attibut livraison en lui donnant une intersection et la valeur de la duree inscrite dans le JSpinner
	 * @param i : intersection a utiliser pour la nouvelle intersection
	 * @see modele.Livraison#Livraison(Intersection, int)
	 */
	public void setIntersection(Intersection i) {
		boutonAction.setEnabled(true);
		livraison = new Livraison(i, (Integer)dureeModification.getValue()*60);
	}
	
	/**
	 * Affiche le JButton de suppression de l'element
	 */
	public void afficherBoutonSupprimer()
	{
		details.add(boutonAction, BorderLayout.EAST);
	}

	public int getPosition() {
		return position;
	}
}
