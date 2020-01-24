/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frigoconnecte;
import grovepi.GrovePi;
import grovepi.Pin;
import grovepi.common.Delay;
import grovepi.sensors.Led;
import grovepi.sensors.TemperatureAndHumiditySensor;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.json.JSONException;

/**
 *
 * @author p1801098
 */
public class FrigoConnecte {

	/**
	 * @param args the command line arguments
	 * @throws java.sql.SQLException
         * @throws java.io.IOException
         * @throws org.json.JSONException
	 */
	public static void main(String[] args) throws SQLException, IOException, JSONException {
                Connection cnx = null;
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
                    //Suppression de la base [SOLUTION TEMPORAIRE]
                    lien.execute("DELETE FROM AlimentLDC;");
                 }
                
		int pinTemp = Pin.DIGITAL_PIN_7;
		int pinBuzz = Pin.DIGITAL_PIN_3;
		int pinSwitch = Pin.DIGITAL_PIN_5;
                
		GrovePi grovePi = new GrovePi();
		TemperatureAndHumiditySensor temp = new TemperatureAndHumiditySensor(grovePi, pinTemp, TemperatureAndHumiditySensor.Model.DHT22);
                
		temp.update();
		//Delay.milliseconds(100);
                //API_Recette.getRecette("chicken");
                //System.out.println(API_Recette.getIngredient(API_Recette.getRecette("chicken")));
                //System.out.println(API_Recette.getImage(API_Recette.getRecette("chicken")));
                
                
		Frigo f = new Frigo();

		ListeDeCourse tempLDS = new ListeDeCourse("Liste",f);
		tempLDS.addAliment(new Aliment("Pomme","Fruit",tempLDS));
		tempLDS.addAliment(new Aliment("Donut","Patisserie", tempLDS));
		tempLDS.addAliment(new Aliment("Colin","Poisson", tempLDS));
		
		ArrayList <Note> liste_notes = new ArrayList();

		f.listeLDC.add(tempLDS);

		//API_Recette.getRecette("chicken tomato");

		maFenetre fen = new maFenetre("Réfrigérateur connecté", tempLDS, liste_notes, f);
		fen.setVisible(true);
		Led DEVICE = grovePi.getDeviceFactory().createLed(pinBuzz);
		
		while(true) {
			Delay.milliseconds(1000);
			fen.majAccueil();
			f.checkPorte(pinBuzz, pinSwitch);
                       
		}

	}	
	
}
