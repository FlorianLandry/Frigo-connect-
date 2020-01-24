/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frigoconnecte;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

/**
 * <b>ListeDeCourse_Boite</b> : Boite de dialogue permettant d'ajouter un nouvel aliment à la liste de course
 * @author Malik_WILLEMY
 */
public class ListeDeCourse_Boite extends JDialog implements ActionListener{
    
    JTextField nom;
    JTextField type;
    JButton annuler;
    JButton valider;
    Aliment aliment = null;
    maFenetre f;
    /**
     * 
     * @param fen : Fenêtre à associer pour utiliser les fonctions de celle-ci
     */
    public ListeDeCourse_Boite(maFenetre fen){
        f = fen;
        initialisation();
    }
    /**
     * <b>initialisation</b> : Mise en place des boutons et textfields de la boite de dialogue pour ajouter des aliments
     */
    public void initialisation(){
        //Initialisation des boutons et textfields
        nom = new JTextField("Nom");
        type = new JTextField("Type");
        annuler = new JButton("Annuler");
        valider = new JButton("Valider");
        valider.addActionListener(this);
        annuler.addActionListener(this);
        //Création d'un panneau
        Container c = getContentPane();
        c.setLayout(new GridBagLayout());
        
        GridBagConstraints cont = new GridBagConstraints();
        cont.fill=GridBagConstraints.BOTH;
        //Placement des éléments
        cont.gridx = 0;
        cont.gridy = 0;
        c.add(nom, cont);
        
        cont.gridx = 0;
        cont.gridy = 1;
        c.add(type, cont);
        
        cont.gridx = 0;
        cont.gridy = 3;
        c.add(annuler, cont);
        
        cont.gridx = 1;
        cont.gridy = 3;
        c.add(valider, cont);
        
        this.setContentPane(c);
        this.pack();
        this.revalidate();
    }
    /**
     * 
     * @param e : Element cliqué
     * @see frigoconnecte.maFenetre#addAliment(frigoconnecte.Aliment) 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Si le bouton annuler est utilisé, alors on ferme la boite de dialogue
        if(e.getSource() == annuler){
            this.dispose();
        }
        //Lorsque l'on clique sur les textfields, le contenu de ceux-ci est vidé
        if(e.getSource() == nom){
            nom.setText("");
        }
        if(e.getSource() == type){
            type.setText("");
        }
        //Nous créons un nouvel objet de type aliment que nous envoyons à la fonction de la fenêtre
        if(e.getSource() == valider){
            try {
                aliment = new Aliment(nom.getText(), type.getText(), f.getFrigo().getListeCourse(f.selection_Liste.getSelectedItem().toString()));
            } catch (SQLException ex) {
                Logger.getLogger(ListeDeCourse_Boite.class.getName()).log(Level.SEVERE, null, ex);
            }
            f.addAliment(aliment);
            this.setVisible(false);
        }
    }
}
