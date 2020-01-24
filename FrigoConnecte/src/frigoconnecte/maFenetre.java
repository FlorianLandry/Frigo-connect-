/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frigoconnecte;

import static com.pi4j.wiringpi.Gpio.delay;
import grovepi.GrovePi;
import grovepi.Pin;
import grovepi.sensors.Led;
import grovepi.sensors.TemperatureAndHumiditySensor;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.json.JSONException;

/**
 *<b>Classe maFenetre</b> C'est la fenetre du logiciel
 * Elle contient tous les différents JPanel et implémente tous les ActionListener
 * 
 * @author Le_Groupe
 */
public class maFenetre extends JFrame implements ActionListener, FocusListener {
    //Elements du menu principal

    JPanel Photo = new JPanel();
    JPanel Recette = new JPanel();
    JPanel Notes = new JPanel();
    //Elements de ListeDeCourses
    private ListeDeCourse liste_aliment;

    JButton ajouterLDC;
    ListeDeCourse_Boite boite_ajout_aliment;
    ListeDeCourse_Boite_Liste boite_ajout_liste_de_course;
    JComboBox selection_Liste;
    JButton retour_liste_Course;
    JButton ajouter_aliment;
    JLabel nom_aliment;
    ArrayList<JTextField> nombre_aliment;
    ArrayList<JButton> plus_aliment;
    ArrayList<JButton> moins_aliment;
    ArrayList<JButton> supprAliment;

    JPanel Liste_De_Course = new JPanel();
    
    public int index = 0;
    private Frigo frigo;
    //Elements des notes
    private ArrayList<Note> liste_notes;
    ArrayList<JTextArea> texte_notes = new ArrayList();
    ArrayList<JLabel> titre_notes = new ArrayList();
    ArrayList<JButton> supprimer_note = new ArrayList();
    
    Notes_Boite boite_note;
    
    JButton retour_note;
    JButton ajout_note;
    JButton delete_note;
    //Elements de la photo
    JButton retour_photo;
    JLabel image;
    
    int pinTemp = Pin.DIGITAL_PIN_7;
   GrovePi grovePi = new GrovePi();
   TemperatureAndHumiditySensor temp = new TemperatureAndHumiditySensor(grovePi, pinTemp, TemperatureAndHumiditySensor.Model.DHT22);
    
   /**
    * <b>majAccueil()</b> : Met à jour les affichages de l'écran d'acceuil comme la température, l'heure et la date
    */
    public void majAccueil() {
        Date date = new Date();
        temp.update();
        textTemperature.setText(temp.getTemperature() + "°C");
        String dateEnString = "";
        String heureEnString = "";
        switch(date.getDay()) {
            case 0:
                dateEnString += "Dimanche ";
                break;
            case 1:
                dateEnString += "Lundi ";
                break;
            case 2:
                dateEnString += "Mardi ";
                break;
            case 3:
                dateEnString += "Mercredi ";
                break;
            case 4:
                dateEnString += "Jeudi ";
                break;
            case 5:
                dateEnString += "Vendredi ";
                break;
            case 6:
                dateEnString += "Samedi ";
                break;
            default:
                dateEnString += "??? ";
                break;
        }
        dateEnString += date.getDate();
        switch(date.getMonth()) {
            case 0:
                dateEnString += " Janvier";
                break;
            case 1:
                dateEnString += " Février";
                break;
            case 2:
                dateEnString += " Mars";
                break;
            case 3:
                dateEnString += " Avril";
                break;
            case 4:
                dateEnString += " Mai";
                break;
            case 5:
                dateEnString += " Juin";
                break;
            case 6:
                dateEnString += " Juillet";
                break;
            case 7:
                dateEnString += " Août";
                break;
            case 8:
                dateEnString += " Septembre";
                break;
            case 9:
                dateEnString += " Octobre";
                break;
            case 10:
                dateEnString += " Novembre";
                break;
            case 11:
                dateEnString += " Décembre";
                break;
            default:
                dateEnString += " ???";
                break;
        }
        
        heureEnString += date.getHours();
        heureEnString += ":";
        heureEnString += date.getMinutes();
        
        textDate.setText(dateEnString);
        textHeure.setText(heureEnString);
    }
    
    /**
     * <b>maFenetre()</b> : initialise la fenêtre du programme
     * @param titre le titre de la fenetre
     * @param liste_course la liste de courses d'initialisation
     * @param liste_notes la liste de notes d'initialisation
     * @param frigo le frigo à utiliser pour afficher les bonnes informations
     * @throws IOException 
     */
    public maFenetre(String titre, ListeDeCourse liste_course, ArrayList liste_notes, Frigo frigo) throws IOException {
        this.frigo = frigo;
        this.liste_aliment = liste_course;
        this.liste_notes = liste_notes;
        this.setTitle(titre);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        URL temp = new URL("https://www.papillesetpupilles.fr/wp-content/uploads/2005/07/Cookies-aux-pe%CC%81pites-de-chocolat-%C2%A9beats1.-shutterstock.jpg");

        init_Liste_de_Courses();
        init_Notes();
        init_Recette("\nVanille\n150g de farine\n100g de chocolat noir\n1 cuillère à café de sel\n1 cuillère à café de levure chimique\nCookies :\n\n85g de beurre mou\n1 oeuf\n85g de sucre", temp);
        init_Accueil();
        
        this.setDefaultLookAndFeelDecorated(true);
        this.setExtendedState(this.MAXIMIZED_BOTH);
        this.setAlwaysOnTop(false);
        //this.setUndecorated(true);
    }

    /**
     * <b>init_Accueil()</b> : initialise le panneau "Accueil" qui va apparaitre sur la fenêtre principale
     */
    public void init_Accueil() {
        temp.update();
        Accueil = new javax.swing.JPanel();
        textTemperature = new javax.swing.JLabel();
        textDate = new javax.swing.JLabel();
        textHeure = new javax.swing.JLabel();
        textFieldRecherche = new javax.swing.JTextField();
        butListeCourse = new javax.swing.JButton();
        butCamera = new javax.swing.JButton();
        butNotes = new javax.swing.JButton();
        butMicro = new javax.swing.JButton();
        ImageIcon micro = new ImageIcon("micro.png");
        butMicro.setIcon(micro);

        Accueil.setVisible(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textTemperature.setText(temp.getTemperature() + "°C");

        textFieldRecherche.setText("Recherche...");

        butListeCourse.setText("Liste de course");

        butCamera.setText("Caméra");

        butNotes.setText("Notes");
        
        setPreferredSize(new java.awt.Dimension(800, 480));
        
        butNotes.setBackground(new Color(255, 204, 0));
        butCamera.setBackground(new Color(255, 204, 0));
        butListeCourse.setBackground(new Color(255, 204, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(butNotes, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(88, 88, 88)
                                    .addComponent(butCamera, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                                    .addComponent(butListeCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(169, 169, 169)
                                    .addComponent(textFieldRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(butMicro, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(textTemperature)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textDate))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(textHeure)))
                            .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textTemperature)
                            .addComponent(textDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textHeure)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFieldRecherche)
                            .addComponent(butMicro, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(butListeCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(butCamera, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(butNotes, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
        );

        butCamera.addActionListener(this);
        butListeCourse.addActionListener(this);
        butNotes.addActionListener(this);
        butMicro.addActionListener(this);

    }

    /**
     * <b>init_Liste_de_Course()</b> : initialise le panneau "Liste de courses" qui va apparaitre sur la fenêtre principale
     */
    public void init_Liste_de_Courses() {
        Liste_De_Course.removeAll();
        Liste_De_Course.setLayout(new GridBagLayout());
        ajouterLDC = new JButton("Ajouter une Liste De Courses");
        selection_Liste = new JComboBox();
        for (int i = 0; i < frigo.listeLDC.size(); i++) {
            selection_Liste.addItem(frigo.listeLDC.get(i).getNom());
        }
        selection_Liste.setSelectedIndex(index);
        retour_liste_Course = new JButton("RETOUR");
        ajouter_aliment = new JButton("Ajouter un aliment");

        GridBagConstraints cont = new GridBagConstraints();
        cont.fill = GridBagConstraints.BOTH;

        cont.gridx = 0;
        cont.gridy = 0;
        Liste_De_Course.add(retour_liste_Course, cont);

        cont.gridx = 1;
        cont.gridy = 0;
        cont.gridwidth = 3;
        Liste_De_Course.add(selection_Liste, cont);
        selection_Liste.addActionListener(this);

        cont.gridwidth = 2;
        cont.gridx = 2;
        cont.gridy = 1;
        Liste_De_Course.add(ajouter_aliment, cont);
        
        cont.gridwidth = 1;
        cont.gridx = 0;
        cont.gridy = 1;
        Liste_De_Course.add(ajouterLDC, cont);
        ajouterLDC.addActionListener(this);

        plus_aliment = new ArrayList<>();
        moins_aliment = new ArrayList<>();
        nombre_aliment = new ArrayList<>();
        supprAliment = new ArrayList<>();

        for (int i = 0; i < frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).getListe().size(); i++) {
            nom_aliment = new JLabel(frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).getListe().get(i).getNom());

            plus_aliment.add(new JButton("+"));
            moins_aliment.add(new JButton("-"));
            supprAliment.add(new JButton("SUPPR"));
            
            JTextField tempField = new JTextField(Integer.toString(frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).getQtt(i)));
            tempField.setColumns(2);
            tempField.setHorizontalAlignment(JTextField.CENTER);
            nombre_aliment.add(tempField);

            cont.gridwidth = 1;
            cont.gridx = 0;
            cont.gridy = i + 2;
            Liste_De_Course.add(nom_aliment, cont);

            cont.gridwidth = 1;
            cont.gridx = 1;
            cont.gridy = i + 2;
            Liste_De_Course.add(moins_aliment.get(i), cont);
            cont.gridwidth = 1;

            cont.gridwidth = 1;
            cont.gridx = 2;
            cont.gridy = i + 2;
            Liste_De_Course.add(nombre_aliment.get(i), cont);
            
            cont.gridwidth = 1;
            cont.gridx = 4;
            cont.gridy = i + 2;
            Liste_De_Course.add(supprAliment.get(i), cont);

            cont.gridx = 3;
            cont.gridy = i + 2;
            Liste_De_Course.add(plus_aliment.get(i), cont);
            
            plus_aliment.get(i).addActionListener(this);
            moins_aliment.get(i).addActionListener(this);
            supprAliment.get(i).addActionListener(this);
        }

        retour_liste_Course.addActionListener(this);
        ajouter_aliment.addActionListener(this);
        //this.pack();
        this.setBackground(new Color(236,236,236));
        this.repaint();
        this.revalidate();
    }

    /**
     * <b>addAliment()</b> : ajoute un aliment à la liste de course sélectionné
     * @param a l'aliment à ajouter
     */
    public void addAliment(Aliment a) {
        frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).getListe().add(a);
        frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).getListeQtt().add(0);
        init_Liste_de_Courses();
    }

    /**
     * <b>addListe()</b> : ajoute une nouvelle liste de course
     * @param l la liste à ajouter
     */
    public void addListe(ListeDeCourse l) {
        frigo.addListeDeCourse(l);
        init_Liste_de_Courses();
    }

    
/**
     * <b>init_Notes()</b> : On initialise et on crée l'affichage des notes, qui se modifie suivant leur nombre, leur contenu et titre
     * Il y a d'abord un bouton "retour" situé sur la gauche, à part, pour + de facilités d'utilisation
     * Chacune des notes est affichée sous la forme d'un post-it, de couleur jaune, avec des bordures pour les séparer
     * Enfin, un bouton "+" de largeur variant suivant le nombre de notes et affiché en bas de fenetre
     * Il nous permet d'ajouter une note
     * 
     * 
     */
    public void init_Notes() {        
        int fin = 0;
        Notes.removeAll();
        Notes.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        cont.fill = GridBagConstraints.BOTH;
        for(int i = 0; i < this.liste_notes.size(); i++){
            
        }
        for(int i = 0; i < this.texte_notes.size(); i++){
            this.texte_notes.get(i).setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
            this.texte_notes.get(i).setBackground(new Color(255, 230, 128));
            this.texte_notes.get(i).setOpaque(true);
        }
        for(int i = 0; i < this.titre_notes.size(); i++){
            this.titre_notes.get(i).setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
            this.titre_notes.get(i).setBackground(new Color(255, 230, 128));
            this.titre_notes.get(i).setOpaque(true);
        }
        cont.gridx = 0;
        cont.gridy = 0;
        cont.gridheight = 1;
        retour_note = new JButton("RETOUR");
        Notes.add(retour_note, cont);
        
        
        cont.gridheight = 1;
        for(int i = 0; i < this.liste_notes.size(); i++, fin = i){
            cont.gridx = i%3+1;
            cont.gridy = i/3+(3*(i/3));
            Notes.add(titre_notes.get(i), cont);
            cont.gridx = i%3+1;
            cont.gridy = i/3+1+(3*(i/3));
            Notes.add(texte_notes.get(i), cont);
            cont.gridx = i%3+1;
            cont.gridy = i/3+2+(3*(i/3));
            Notes.add(supprimer_note.get(i), cont);
        }
        for(int i =0; i < this.supprimer_note.size(); i++, fin = i){
            this.supprimer_note.get(i).addActionListener(this);
        }
        
        if(this.liste_notes.size() == 9){
            
        } else {
            ajout_note = new JButton();
            ImageIcon pluuus = new ImageIcon("plus.png");
            ajout_note.setIcon(pluuus);
            ajout_note.setBackground(new Color(222, 205, 135));
            ajout_note.setMargin(new Insets(20, 20, 20, 20));
            cont.gridx = 1;
            cont.gridy = (fin / 3 + 2 + (3 * (fin / 3))) + 1;
            cont.gridwidth = 4;
            Notes.add(ajout_note, cont);
        }
        retour_note.addActionListener(this);
        ajout_note.addActionListener(this);
        this.repaint();
        this.revalidate();
    }
    
    /**
     * <b>addNote()</b> : On ajoute un élément Note crée à la liste associée, et on recrée le panneau;
     * @param n note à ajouter
     */
    public void addNote(Note n){
        if(this.liste_notes.size() == 9){
            
        } else {
            this.liste_notes.add(n);
            this.supprimer_note.add(new JButton("Supprimer"));
            this.texte_notes.add(new JTextArea(n.getContenu()));
            this.titre_notes.add(new JLabel(n.getTitre()));
        }

    }
    /**
   
     * <b>init_Photo()</b> : Création du panneau et affichage du panneau Photo qui va apparaitre sur la fenêtre principale;
     * L'écran affiche une image de l'intérieur du réfrigérateur
     */
    public void init_Photo() throws IOException {
        Photo.removeAll();
        GrovePi grovePi = new GrovePi();
        Led device = grovePi.getDeviceFactory().createLed(Pin.DIGITAL_PIN_6);
        device.setState(0b010);
        Runtime.getRuntime().exec("sudo raspistill -o photo_frigo.jpg -t 1");
        
        retour_photo = new JButton("Retour");
        delay(1000);
        device.setState(0);
        ImageIcon icon = new ImageIcon(new ImageIcon("photo_frigo.jpg").getImage().getScaledInstance(700, 385, Image.SCALE_DEFAULT));
        image = new JLabel(icon);
        Photo.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        cont.fill = GridBagConstraints.BOTH;     
        cont.gridx=0;
        cont.gridy=0;
        Photo.add(retour_photo, cont);
        cont.gridy=1;
        Photo.add(image, cont);
        retour_photo.addActionListener(this);
        this.pack();       
          
    }

    /**
     * <b>init_Liste_de_Course()</b> : initialise le panneau "Recette" qui va apparaitre sur la fenêtre principale;
     * Le panneau se compose de 2 zones de texte et d'une image
     */
    public void init_Recette(String ingredients, URL imageURL) throws IOException {
        Recette.removeAll();
        textTitre = new javax.swing.JLabel();
        retour_recette = new javax.swing.JButton();
        ingredients_recette = new javax.swing.JScrollPane();
        textIngredient = new javax.swing.JTextArea();
        image_recette = new javax.swing.JScrollPane();
        imageArea = new javax.swing.JLabel();
        etapes_recette = new javax.swing.JScrollPane();
        textEtapes = new javax.swing.JTextArea();

        textTitre.setText("Recette");

        retour_recette.setText("Retour");

        textIngredient.setColumns(20);
        textIngredient.setRows(5);
        ingredients_recette.setViewportView(textIngredient);
        
        textIngredient.setText("Cookies :\n\n85g de beurre mou\n1 oeuf\n85g de sucre"
                + "\nVanille\n150g de farine\n100g de chocolat noir\n1 cuillère à café de sel"
                + "\n1 cuillère à café de levure chimique");
        if(ingredients != null) textIngredient.setText(ingredients);
        textIngredient.setEditable(false);

        /*imageArea.setColumns(20);
        imageArea.setRows(5);*/
        image_recette.setViewportView(imageArea);
        ImageIcon icon = new ImageIcon(new ImageIcon("cookies.jpg").getImage().getScaledInstance(180, 120, Image.SCALE_DEFAULT));
        //if(imageURL != null) icon = new ImageIcon(new ImageIcon(imageURL).getImage().getScaledInstance(180, 120, Image.SCALE_DEFAULT));
        imageArea.setIcon(icon);
        //Image image = ImageIO.read(new File("cookies.jpg"));
        //imageArea.imageUpdate(image, NORMAL, 0, 0, 240, 400);
        
        

        textEtapes.setColumns(20);
        textEtapes.setRows(5);
        etapes_recette.setViewportView(textEtapes);
        textEtapes.setText("Étape 1 :\nDétailler le chocolat en pépites\n\n"
                + "Étape 2 :\nPréchauffer le four à 180°C. Dans un saladier, mettre\n"
                + "75g de beurre, le sucre, l'oeuf entier, la vanille et mélanger le tout.\n\n"
                + "Étape 3 :\nAjouter petit à petit la farine mélangée à la levure, le sel et le chocolat."
                + "\n\nÉtape 4 :\nBeurrer une plaque allant au four et former les cookies sur la plaque.\n\n"
                + "Étape 5 :\nPour former les cookies, utiliser 2 cuillères à soupe et faire des petits\n"
                + "tas espacés les uns des autres; ils grandiront à la cuisson.\n\nÉtape 6 :\n"
                + "Enfourner pour 10 minutes de cuisson.");
        textEtapes.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(Recette);
        Recette.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(etapes_recette)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(ingredients_recette, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(image_recette, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(retour_recette)
                                                .addGap(103, 103, 103)
                                                .addComponent(textTitre)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(textTitre)
                                        .addComponent(retour_recette))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(ingredients_recette, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                        .addComponent(image_recette))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(etapes_recette, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                .addContainerGap())
        );
        retour_recette.addActionListener(this);
    }

    /**
     * <b>actionPerformed</b>Crée toutes les actions réalisables avec les boutons de chaque JPanel
     * <br>
     * butCamera ► Prend la photo
     * <br>
     * retour_note ► Retourne sur l'écran d'Accueil à partir des Notes
     * <br>
     * retour_photo ► Retourne sur l'écran d'Accueil à partir de la Photo
     * <br>
     * ajout_note ► Crée une boite de dialogue pour ajouter une Note
     * <br>
     * retour_recette ► Retourne sur l'écran d'Accueil à partir de la Recette
     * <br>
     * supprimer_note ► Supprime la Note au dessus du bouton
     * <br>
     * butListeCourse ► Nous envoie sur l'écran de la Liste de Course
     * <br>
     * butNotes ► Nous envoie sur l'écran des Notes
     * <br>
     * butMicro ► Nous envoie sur l'écran de la Recette
     * <br>
     * retour_liste_course ► Retourn sur l'écran d'Accueil
     * <br>
     * ajouter_aliment ► Ajoute un Aliment dans la Liste_de_Course sélectionnée par la ComboBox "selection_Liste"
     * <br>
     * ajouter_LDC ► Ajoute une Liste de Course à la liste de Listes de Course
     * <br>
     * moins_aliment ► Réduit le nombre de l'Aliment, à gauche du bouton, de un
     * <br>
     * plus_aliment ► Augmente le nombre de l'Aliment, à gauche du bouton, de un
     * <br>
     * supprAliment ► Supprime l'Aliment à gauche du bouton
     * <br>
     * selection_Liste ► Combobox permettant de selectionner une Liste de Course
     * <br>
     * @param e Action lorsque l'on clique sur un élément
     */
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //ActionListener de Notes
        if(e.getSource() == butCamera){
            try {
                this.setContentPane(Photo);                
                this.init_Photo(); 
                this.setContentPane(Photo);
            } catch (IOException ex) {
                Logger.getLogger(maFenetre.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        if(e.getSource() == retour_note){
            for(int j = 0; j < this.liste_notes.size(); j++){
               this.liste_notes.get(j).contenu = this.texte_notes.get(j).getText();
            }
            this.setContentPane(Accueil);
            this.init_Accueil();    
        }
        if(e.getSource() == retour_photo){
            this.setContentPane(Accueil);
            this.init_Accueil();  
        }       
        if (e.getSource() == ajout_note) {
            boite_note = new Notes_Boite(this);
            boite_note.setVisible(true);
            this.revalidate();
        }
        if(e.getSource() == retour_recette){
            this.setContentPane(Accueil);
            this.init_Accueil();
        }
        for (int i = 0; i < supprimer_note.size(); i++){
            if(e.getSource() == supprimer_note.get(i)){
                this.liste_notes.remove(i);
                this.supprimer_note.remove(i);
                this.titre_notes.remove(i);
                this.texte_notes.remove(i);              
                this.init_Notes();
            }           
        }
        if (e.getSource() == butListeCourse) {

            this.setContentPane(Liste_De_Course);
            this.init_Liste_de_Courses();
            this.revalidate();
        }
        if (e.getSource() == butNotes) {

            this.setContentPane(Notes);
            this.init_Notes();
            this.revalidate();
        }
        if (e.getSource() == butMicro) {
            String requete = textFieldRecherche.getText();
            String ingredients = null;
            try {
                ingredients = API_Recette.getIngredient(API_Recette.getRecette(requete));
            } catch (ProtocolException ex) {
                Logger.getLogger(maFenetre.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(maFenetre.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(maFenetre.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            URL imageURL=null;
            try {
                imageURL = API_Recette.getImage(API_Recette.getRecette(requete));
            } catch (ProtocolException ex) {
                Logger.getLogger(maFenetre.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(maFenetre.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(maFenetre.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setContentPane(Recette);
            try {
                this.init_Recette(ingredients, imageURL);
            } catch (IOException ex) {
                Logger.getLogger(maFenetre.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.revalidate();
        }
        //ActionListener de Liste de Courses
        if (e.getSource() == retour_liste_Course) {
            this.setContentPane(Accueil);
            this.init_Accueil();
        }
        if (e.getSource() == ajouter_aliment) {
            boite_ajout_aliment = new ListeDeCourse_Boite(this);
            
            boite_ajout_aliment.setVisible(true);
        }
        if (e.getSource() == ajouterLDC) {
            boite_ajout_liste_de_course = new ListeDeCourse_Boite_Liste(this);
            boite_ajout_liste_de_course.setVisible(true);
        }

            for (int i = 0; i < moins_aliment.size(); i++) {
                if (e.getSource() == moins_aliment.get(i)) {
                    if (Integer.parseInt(nombre_aliment.get(i).getText())> 0) {
                        nombre_aliment.get(i).setText(Integer.toString(Integer.parseInt(nombre_aliment.get(i).getText()) - 1));
                        frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).setQtt(i, Integer.parseInt(nombre_aliment.get(i).getText()));
                        break;
                    }
                    
                }
                if (e.getSource() == plus_aliment.get(i)) {
                    if (Integer.parseInt(nombre_aliment.get(i).getText())< 10){
                        nombre_aliment.get(i).setText(Integer.toString(Integer.parseInt(nombre_aliment.get(i).getText()) + 1));
                        frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).setQtt(i, Integer.parseInt(nombre_aliment.get(i).getText()));
                        break;
                    }
                   
                }
                if (e.getSource() == supprAliment.get(i)) {
                    System.out.println(frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).getListe().size());
                    frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).getListe().remove(i);
                    System.out.println(frigo.getListeCourse(selection_Liste.getSelectedItem().toString()).getListe().size());
                    init_Liste_de_Courses();
                }
            }
        if (e.getSource() == selection_Liste) {
            index = selection_Liste.getSelectedIndex();
            System.out.println(index);
            liste_aliment = frigo.getListeCourse(selection_Liste.getSelectedItem().toString());
            init_Liste_de_Courses();
        }
        if(e.getSource()==textFieldRecherche) {
            textFieldRecherche.setText("");
        }
    }
    
    /**
     * 
     * @param e 
     */
    @Override
    public void focusGained(FocusEvent e) {
        if(e.getSource()==textFieldRecherche) {
            textFieldRecherche.setText("");
        }
    }
    
    private javax.swing.JButton butMicro;
    private javax.swing.JButton butListeCourse;
    private javax.swing.JButton butCamera;
    private javax.swing.JButton butNotes;
    private javax.swing.JPanel Accueil;
    private javax.swing.JLabel textTemperature;
    private javax.swing.JLabel textDate;
    private javax.swing.JLabel textHeure;
    private javax.swing.JTextField textFieldRecherche;
    private javax.swing.JScrollPane ingredients_recette;
    private javax.swing.JScrollPane image_recette;
    private javax.swing.JScrollPane etapes_recette;
    private javax.swing.JTextArea textEtapes;
    private javax.swing.JTextArea textIngredient;
    private javax.swing.JLabel textTitre;
    public javax.swing.JButton retour_recette;
    private javax.swing.JLabel imageArea;

    
    /* Utilisé pour construire les listes de courses*/
    Frigo getFrigo() {
        return frigo;
    }

    @Override
    public void focusLost(FocusEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
