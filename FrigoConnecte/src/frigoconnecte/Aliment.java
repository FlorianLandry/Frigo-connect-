/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frigoconnecte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author p1801098
 */
public class Aliment {
    String nom;
    String type;
    Double prix;
    ListeDeCourse liste;
    
    /**
     * <b>Aliment()</b> : initialise un nouvel aliment inconnu
     */
    public Aliment() throws SQLException {
        this.nom = "inconnu";
        this.type = "inconnu";
        this.prix = 0.0;
    }
/**
     * <b>Aliment()</b> : initialise un nouvel aliment
     * @param nom le nom de l'aliment
     * @param type le type de l'aliment
     * @param liste la liste à laquelle 
     */
    public Aliment(String nom, String type, ListeDeCourse liste) throws SQLException {
        this.nom = nom;
        this.type = type;
        this.prix = 0.0;
        this.liste = liste;
        
         Connection cnx = null;
        ResultSet rs;
        Statement lien;
        String url = "jdbc:mysql://localhost:3306/SS21?useUnicode=true&useJDCCompliantTimeZoneShift=true&useLegacyDatetimeCode=false&serveurTimezone=UTC";
        String utilisateur = "Frigo";
        String motDePasse = "pi";
        //On essaie
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(url, utilisateur, motDePasse);
            System.out.println("Connexion effective !");
            Statement statement = cnx.createStatement();
        //On affiche l'éventuel erreur ici
        } catch (Exception e) {
            System.out.println("Erreur de connexion BDD"+e.getMessage());
        }

        if (cnx != null) {
            lien = cnx.createStatement();
            //Ajout à la Base de Données le type et le nom
            lien.execute("INSERT INTO AlimentLDC VALUES ('" + this.nom + "' , '"+this.type+"','"+this.getListe().getIdLDC()+"');");
        }
    }

    /**
     * @param prix le prix à assigner à l'aliment
     */
    public void setPrix(Double prix){
        this.prix = prix;
    }

    /**
     * @return le type de l'aliment
     */
    public String getType() {
        return type;
    }

    /**
     * @param type le type à assigner à l'aliment
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * @return le prix de l'aliment
     */
    public Double getPrix() {
        return prix;
    }
    /**
     * @return  le nom de l'aliment
     */
    public String getNom(){
        return nom;
    }

    public ListeDeCourse getListe() {
        return liste;
    }
    
    
}
