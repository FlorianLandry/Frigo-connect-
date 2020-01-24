/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frigoconnecte;

/**
 *
 * @author p1801098
 */
public class Note {
    String contenu;
    String titre;

    /**
     * <b>Note()</b> : initialise une nouvelle note
     * @param titre le titre de la note
     * @param contenu le texte qui sera affiché sur la note
     */
    public Note(String titre, String contenu) {
        this.contenu = contenu;
        this.titre = titre;
    }

    /**
     * @return le titre de la note
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @param titre le titre à donner à la note
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    /**
     * @return le contenu de la note
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * @param contenu le texte à mettre dans le contenu de la note
     */
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    
}
