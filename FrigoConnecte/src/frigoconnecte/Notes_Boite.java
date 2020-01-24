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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author ragna
 */
public class Notes_Boite extends JDialog implements ActionListener{

    JTextField nom;
    JTextArea contenu;
    JButton annuler;   
    JButton valider;
    maFenetre f;
    Note note = null;
    
    public Notes_Boite(maFenetre fen){
        f = fen;
        initialisation();
    }
    
    public void initialisation(){
        nom = new JTextField("Titre");
        contenu = new JTextArea("Contenu");
        annuler = new JButton("Annuler");
        valider = new JButton("Valider");
        valider.addActionListener(this);
        annuler.addActionListener(this);

        Container c = getContentPane();
        c.setLayout(new GridBagLayout());
        
        GridBagConstraints cont = new GridBagConstraints();
        cont.fill=GridBagConstraints.BOTH;
        
        cont.gridx = 0;
        cont.gridy = 0;
        c.add(nom, cont);
        
        cont.gridx = 0;
        cont.gridy = 1;
        c.add(contenu, cont);
        
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
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == annuler){
            this.dispose();
        }
        if(e.getSource() == valider){
            note = new Note(nom.getText(), contenu.getText());
            f.addNote(note);
            f.init_Notes();
            this.setVisible(false);
            
        }
    }
    
}
