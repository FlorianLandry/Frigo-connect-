
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frigoconnecte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Le_Groupe
 */
public abstract class API_Recette {

    private static String key = "5dfb52cabfe8b7275d689aa1b6aff715";
    /**
     *<b>getRecette()</b> : Retourne l'id de la premiere recette resultante de la recherche via l'API Food2Fork
     *@param keywords Ce sont les mots clés de la recherche
     *@return idRecette du premier résultat 
    */
    public static String getRecette(String keywords) throws MalformedURLException, ProtocolException, JSONException, IOException {
        //ArrayList<String> recettes = new ArrayList<>();
        String recette = null;

        keywords = keywords.replaceAll(" ", "%20");

        URL url = new URL("https://www.food2fork.com/api/search?key=" + key + "&q=" + keywords);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //Read JSON response and print
        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray recipes = myResponse.getJSONArray("recipes");

        for (int i = 0; i < recipes.length(); i++) {
            JSONObject singleRecipe = recipes.getJSONObject(i);
            //String ID = singleRecipe.getString("recipe_id");
            //recettes.add(ID);

            recette = singleRecipe.getString("recipe_id");
            return recette;
        }
        return recette;
    }
    /**
     * <b>getIngredient()</b> : Retourne les ingredientsd'une recette à partir de son id
     * @param idRecette recuperé à partir de API_Recette::getRecette()
     * @return un String composé des ingrédients de la recette
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws JSONException
     * @throws IOException 
     */
    public static String getIngredient(String idRecette) throws MalformedURLException, ProtocolException, JSONException, IOException {
        String Ingredients = null;

        URL url = new URL("https://www.food2fork.com/api/get?key=" + key + "&rId=" + idRecette);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray ListeIngredients = myResponse.getJSONObject("recipe").getJSONArray("ingredients");
        
        for (int i = 0; i < ListeIngredients.length(); i++) {
            if(i==0 || i==ListeIngredients.length()){
                if(i==0) Ingredients = ListeIngredients.getString(i)+"\n";
                if(i==ListeIngredients.length()) Ingredients = Ingredients + ListeIngredients.getString(i);
            }else Ingredients = Ingredients + ListeIngredients.getString(i)+"\n";
            
        }
        
        return Ingredients;
    }
    
    /**
     * <b>getImage()</b> : Retourne l'image dune recette à partir de son id
     * @param idRecette recuperé à partir de API_Recette::getRecette()
     * @return un URL de l'image
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws JSONException
     * @throws IOException 
     */
    public static URL getImage(String idRecette) throws MalformedURLException, ProtocolException, JSONException, IOException {
        URL ImageURL = null;

        URL url = new URL("https://www.food2fork.com/api/get?key=" + key + "&rId=" + idRecette);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject myResponse = new JSONObject(response.toString());
        ImageURL = new URL(myResponse.getJSONObject("recipe").getString("image_url"));
        return ImageURL;
    }
}
