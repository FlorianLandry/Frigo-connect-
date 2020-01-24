/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frigoconnecte;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <b>ListeDeCourse</b> : Classe permettant de retenir divers aliments avec une quantité qui leurs sont propres dans la Base de Données
 * @author p1800646
 */
public class ListeDeCourse {
    
    private Frigo frigo;
    private String nom;
    private ArrayList<Aliment> liste;
    private ArrayList<Integer> qtt;
    int idLDC;
    
    /**
     * <b>ListeDeCourse</b> : Initialise une liste selon <b>nom</b> et <b>f</b> et tente la connexion entre la Base de Données et le programme
     * @param nom : String simple pour le nom de la liste
     * @param f : Associe à la liste un Frigo afin d'acceder à ses données
     * @throws SQLException 
     */
    public ListeDeCourse(String nom, Frigo f) throws SQLException {
        String base = nom;
        nom = this.verifNom(base, nom, f, 1);
        this.frigo = f;
        this.nom = nom;
        liste = new ArrayList<>();
        qtt = new ArrayList<>();
        
        //Initialise la connexion avec la Base de Données
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
            //Ajout à la Base de Données le nom de la liste
            lien.execute("INSERT INTO ListeDeCourse VALUES ('" + nom + "', null);");
               //On vérifie que la Liste soit bien inscrite
            try {
                rs = lien.executeQuery("SELECT idLDC from ListeDeCourse WHERE nom LIKE '" + nom + "';");
                idLDC = Integer.parseInt(rs.getString("idLDC"));
            } catch (NumberFormatException | SQLException e) {
                System.out.println("Erreur commande");
            }
        }

    }
    /**
     * <b>addAliment</b> : Ajoute un aliment crée à la Base de Données
     * @param a : Aliment a ajouté à la Base de Données
     * @return un 1 ou un 0 pour voir si l'ajout a été fonctionnel
     * @throws SQLException 
     * @see frigoconnecte.ListeDeCourse_Boite
     */
    public int addAliment(Aliment a) throws SQLException {
        //On tente la connexion
        Connection cnx = null;
        ResultSet rs;
        Statement lien;
        String url = "jdbc:mysql://localhost:3306/SS21?useUnicode=true&useJDCCompliantTimeZoneShift=true&useLegacyDatetimeCode=false&serveurTimezone=UTC";
        String utilisateur = "Frigo";
        String motDePasse = "pi";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(url, utilisateur, motDePasse);
            System.out.println("Connexion effective !");
            Statement statement = cnx.createStatement();
        } catch (Exception e) {
            System.out.println("Erreur de connexion BDD");
        }

        if (cnx != null) {
            try {
                //On essaie d'ajouter l'aliment à la Base de Données
                lien = cnx.createStatement();
                lien.execute("INSERT INTO AlimentLDC VALUES ('" + a.getNom() + "','" + a.getType() + "','" + idLDC + "');");
                System.out.println("Aliment ajouté");
                liste.add(a);
                return 1;
            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }      

        }else return 0;
    }

    /**
     * <b>deleteAliment</b> : Supprime un aliment de la Base de Données
     * @param a : aliment à supprimer
     * @return un 1 ou un 0 pour voir si l'ajout a été fonctionnel
     * @throws SQLException 
     * @see frigoconnecte.maFenetre#supprAliment
     */
    public int deleteAliment(Aliment a) throws SQLException {
        if (liste.contains(a)) {
            Connection cnx = null;
            ResultSet rs;
            Statement lien;
            String url = "jdbc:mysql://localhost:3306/SS21?useUnicode=true&useJDCCompliantTimeZoneShift=true&useLegacyDatetimeCode=false&serveurTimezone=UTC";
            String utilisateur = "Frigo";
            String motDePasse = "pi";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                cnx = DriverManager.getConnection(url, utilisateur, motDePasse);
                System.out.println("Connexion effective !");
                Statement statement = cnx.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (cnx != null) {
                lien = cnx.createStatement();
                lien.execute("DELETE FROM AlimentLDC "
                        + "WHERE idLDC = '" + idLDC + "'AND nom LIKE'" + a.getNom() + "';");
            }
            liste.remove(a);
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * <b>verifNom()</b> : Fonction récursive qui vérifie si le nom d'une liste est deja utilisé, concatène son nom avec avec chiffre
     * @param base
     * @param nom
     * @param f
     * @param rn
     * @return un String, correspondant au prochain nom utilisable
     */
    public String verifNom(String base, String nom, Frigo f, int rn) {
        for (int i = 0; i < f.listeLDC.size(); i++) {
            if (f.listeLDC.get(i).getNom().equals(nom)) {
                nom = base + Integer.toString(rn);
                rn++;
                verifNom(base, nom, f, rn);
            }
        }
        return nom;
    }

    public int getQtt(int index) {
        return qtt.get(index);
    }

    /**
     * 
     * @return Le prix total de la liste
     */
    public Double prixTotal() {
        double TTL = 0;
        for (int i = 0; i < liste.size(); i++) {
            TTL += liste.get(i).getPrix();
        }
        return TTL;
    }

    public String getNom() {
        return nom;
    }

    public void setQtt(int index, int nb) {
        qtt.set(index, nb);
    }

    public ArrayList getListeQtt() {
        return qtt;
    }
    
    public ArrayList<Aliment> getListe() {
        return liste;
    }

    /**
     * <b>verifNomAliment()</b> : Vérifie si un aliment est deja présent dans une liste 
     * @param nomAliment
     * @return true si l'aliment est dea présent dans la liste
     */
    boolean verifNomAliment(String nomAliment) {
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).getNom().equals(nom)) {
                return false;
            }
        }
        return true;
    }
    
    public int getIdLDC(){
        return idLDC;
    }

}
