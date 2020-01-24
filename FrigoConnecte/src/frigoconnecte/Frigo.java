/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frigoconnecte;

import grovepi.GrovePi;
import grovepi.PinMode;
import grovepi.sensors.Led;
import grovepi.sensors.TemperatureAndHumiditySensor;
import java.util.ArrayList;

/**
 *
 * @author p1801098
 */
public class Frigo {
    ArrayList<Note> listeNotes = new ArrayList();
    ArrayList<Aliment> listeAliments = new ArrayList();
    ArrayList<ListeDeCourse> listeLDC = new ArrayList();
    int timer;
    
    /**
     * <b>Frigo()</b> : initialise le frigo
     */
    public Frigo() {
        timer = 0;
    }
    
    /**
     * <b>porteOuverte</b> : vérifie si la porte du frigo est ouverte
     * @param pin le numero du branchement du capteur pour la porte
     * @return true si la porte est ouverte<br>false si la porte est fermée
     */
    public boolean porteOuverte(int pin) {
        GrovePi grovePi = new GrovePi();
        if(grovePi.digitalRead(pin) == 0) {
            return false;
        }
        return true;
    }
    
    /**
     * <b>checkPorte()</b> : vérifie si il faut faire sonner l'alarme de la porte
     * @param pinBuzz le numero du branchement du buzzer 
     * @param pinSwitch le numero du branchement du capteur de la porte
     */
    public void checkPorte(int pinBuzz, int pinSwitch) {
        GrovePi grovePi = new GrovePi();
        Led DEVICE = grovePi.getDeviceFactory().createLed(pinBuzz);
        if(porteOuverte(pinSwitch) == true) {
            if(timer < 15) {
                DEVICE.setState(0);
                timer++;
            }
            else {               
                DEVICE.setState(1);
            }
        }
        else {
            timer = 0;
            DEVICE.setState(0);
        }
    }
    
    /**
     * <b>addAliment()</b> : ajoute un aliment dans le frigo
     * @param a l'aliment à ajouter
     */
    public void addAliment(Aliment a){
        listeAliments.add(a);
    }
    
    /**
     * <b>deleteAliment()</b> : supprime un aliment du frigo
     * @param a l'aliment à retirer
     */
    public void deleteAliment(Aliment a) {
        listeAliments.remove(a);
    }
    
    /**
     * <b>addNote()</b> : ajoute une note dans le frigo
     * @param n la note à ajouter
     */
    public void addNote(Note n) {
        listeNotes.add(n);
    }
    
    /**
     * <b>deleteNote()</b> : retire une note du frigo
     * @param n la note à retirer
     */
    public void deleteNote(int n) {
        listeNotes.remove(n);
    }
    
    /**
     * <b>modifyNote()</b> : modifie une note déjà présente dans le frigo
     * @param i l'index de la note à modifier dans la liste
     * @param n la nouvelle note à intégrer
     */
    public void modifyNote(int i, Note n) {
        listeNotes.remove(i);
        listeNotes.add(i, n);
    }
    
    
    public ListeDeCourse getListeCourse(String src){
        for (int i=0;i<listeLDC.size();i++){
            if(listeLDC.get(i).getNom().equals(src))
                return listeLDC.get(i);
        }
        return null;
    } 

    /**
     * <b>getListeCourse</b> : récupère une liste de course
     * @return la liste récupérée
     */
    public ArrayList<ListeDeCourse> getListeLDC() {
        return listeLDC;
    }
    
    /**
     * <b>getTemperature()</b> : récupère la température sur le capteur
     * @param grovepi le controleur de capteur
     * @param pinTemp le numero de branchement du capteur
     * @param temperature le capteur
     * @return la température en °C
     */
    public float getTemperature(GrovePi grovepi, int pinTemp, TemperatureAndHumiditySensor temperature) {
        grovepi.pinMode(pinTemp, PinMode.INPUT);
        temperature.update();
        return temperature.getTemperature();
    }

    /**
     * <b>addListeDeCourse()</b> : ajoute une liste de course dans la liste du frigo
     * @param l la liste à ajouter
     */
    void addListeDeCourse(ListeDeCourse l) {
        this.listeLDC.add(l);
    }
}
