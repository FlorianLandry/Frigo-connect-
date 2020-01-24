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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

/**
 * <b>ListeDeCourse_Boite_Liste</b> : Boite de dialogue permettant d'ajouter une nouvelle liste de courses
 * @author Malik_WILLEMY
 */
public class ListeDeCourse_Boite_Liste extends JDialog implements ActionListener{
    
    JTextField nom;
    JButton annuler;
    JButton valider;
    ListeDeCourse liste = null;
    maFenetre f;
    /**
     * 
     * @param fen : Fenêtre à associer pour utiliser les fonctions de celle-ci
     */
    public ListeDeCourse_Boite_Liste(maFenetre fen){
        f = fen;
        initialisation();
    }
    /**
     * <b>initialisation</b> : Mise en place des boutons et textfields de la boite de dialogue pour ajouter des aliments
     */
    public void initialisation(){
        //Initialisation des éléments
        nom = new JTextField("Nom");
        annuler = new JButton("Annuler");
        valider = new JButton("Valider");
        valider.addActionListener(this);
        annuler.addActionListener(this);
       //Création du panneau
        Container c = getContentPane();
        c.setLayout(new GridBagLayout());
        
        GridBagConstraints cont = new GridBagConstraints();
        cont.fill=GridBagConstraints.BOTH;
        //Placement des éléments
        cont.gridx = 0;
        cont.gridy = 0;
        c.add(nom, cont);
        
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
     * @see frigoconnecte.maFenetre#addListe(frigoconnecte.ListeDeCourse) 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == annuler){
            this.dispose();
        }
        //Lorsque l'on clique sur le textfield, le contenu de celle-ci est vidé
        if(e.getSource() == nom){
            nom.setText("");
        }
        //Nous créons un nouvel objet de type ListeDeCourse que nous envoyons à la fonction de la fenêtre
        if(e.getSource() == valider){
            try {
                liste = new ListeDeCourse(nom.getText(),f.getFrigo());
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            f.addListe(liste);
            this.setVisible(false);
        }
    }
}
