import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;




import org.json.JSONArray;
import org.json.JSONObject;

public class GetWeather {
    private String matchUrl = "http://api.openweathermap.org/data/2.5/weather?";
    private String forecast = "http://api.openweathermap.org/data/2.5/forecast?";
    private String sun = "https://api.sunrise-sunset.org/json?";
    private String API_key = weatherApiConfig.API;
    private String forecastURL;


    //Konstruktor som tar platsen som en inparameter
    public GetWeather(String location) {
        forecastURL = forecast + "q=" + location + "&appid=" + API_key;
    }


    /**
     * Get the document we are looking for..
     *
     */
    public String getDocument() {

        // Bygger ihop URLen vi skall skicka frågan till openweather API
        String url = forecastURL;
        String FinishedWeatherString = null;

        try {
            URL furl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) furl.openConnection();
            // Vi använder GET för att hämta URLen
            conn.setRequestMethod("GET");
            // Vi behöver sätta några request headers som skickas med i vårt
            // HTTP anrop, dessa kommer från den URL jag testade med i terminal
            // med kommandot curl
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("q", "1");
            conn.setRequestProperty("Accept-Language", "sv_SE");

            // responseCode är den HTTP kod vi får tillbaka från servern
            // här skall vi naturligtvis kontrollera om allt är OK, dvs code är 200
            int responseCode = conn.getResponseCode();
            System.out.println("Request : " + url);
            System.out.println("Code    : " + responseCode);

            // hämta hem dokumentet (json)
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            StringBuffer responseData = new StringBuffer();

            while ((line = in.readLine()) != null) {
               responseData.append(line);
            }
            in.close();
            
            
            JSONObject obj_JSONObject = new JSONObject(responseData.toString()); // Huvudobjektet
            
            // Gör ett JSON Objekt av hela JSON-strängen
            
            String jsonString = obj_JSONObject.toString();
            
            
            
            
            // Nivå 2 - listan över alla prognoser
            
            JSONArray list = obj_JSONObject.getJSONArray("list");
            
            // Objekt av alla dagar kl 12
            
            JSONObject day1 =  list.getJSONObject(0);
            JSONObject day2 =  list.getJSONObject(8);
            JSONObject day3 =  list.getJSONObject(16);
            JSONObject day4 =  list.getJSONObject(24);
            JSONObject day5 =  list.getJSONObject(32);
            
            
            // Sökväg till main-menyn (som innehåller temperaturen)
            
            JSONObject main_d1 = day1.getJSONObject("main");
            JSONObject main_d2 = day2.getJSONObject("main");
            JSONObject main_d3 = day3.getJSONObject("main");
            JSONObject main_d4 = day4.getJSONObject("main");
            JSONObject main_d5 = day5.getJSONObject("main");
            
            // Sökväg till datum och tid
            String date_day1 = (String) day1.get("dt_txt");
            String date_day2 = (String) day2.get("dt_txt");
            String date_day3 = (String) day3.get("dt_txt");
            String date_day4 = (String) day4.get("dt_txt");
            String date_day5 = (String) day5.get("dt_txt");
            
            
            
            // Omvandling av temperatur till Celsius men för många decimaler
            
            double tempDay1_Celsius= Kelvin_to_Celsius((Double) main_d1.get("temp"));
            double tempDay2_Celsius= Kelvin_to_Celsius((Double) main_d2.get("temp"));
            double tempDay3_Celsius= Kelvin_to_Celsius((Double) main_d3.get("temp"));
            double tempDay4_Celsius= Kelvin_to_Celsius((Double) main_d4.get("temp"));
            double tempDay5_Celsius= Kelvin_to_Celsius((Double) main_d5.get("temp"));
            
            // Sökväg till vädermenyn 
            
            JSONArray  weather_d1 = day1.getJSONArray("weather");
            JSONArray  weather_d2 = day2.getJSONArray("weather");
            JSONArray  weather_d3 = day3.getJSONArray("weather");
            JSONArray  weather_d4 = day4.getJSONArray("weather");
            JSONArray  weather_d5 = day5.getJSONArray("weather");
            
            // Sökväg till Objektet som håller vädervariabler
            
            JSONObject weather_d1_obj = weather_d1.getJSONObject(0);
            JSONObject weather_d2_obj = weather_d2.getJSONObject(0);
            JSONObject weather_d3_obj = weather_d3.getJSONObject(0);
            JSONObject weather_d4_obj = weather_d4.getJSONObject(0);
            JSONObject weather_d5_obj = weather_d5.getJSONObject(0);
            
            // Hämtar beskrivningen av väder som en textsträng
            
            String weather_day1 = (String) weather_d1_obj.get("description");
            String weather_day2 = (String) weather_d2_obj.get("description");
            String weather_day3 = (String) weather_d3_obj.get("description");
            String weather_day4 = (String) weather_d4_obj.get("description");
            String weather_day5 = (String) weather_d5_obj.get("description");
            

            // Gör ett Objekt över stad (som innehåller lat och long).

            JSONObject city =  obj_JSONObject.getJSONObject("city");
            JSONObject coord =  city.getJSONObject("coord");

            // Kordinater att skicka in i metoden för soluppgång
            Double lat = coord.getDouble("lat");
            Double lng = coord.getDouble("lon");

            System.out.println("kordinater " + lat + "   " + lng);



            // Skriver ut väder och temperatur
             FinishedWeatherString = "idag" + " väder: " + WeatherTranslation(weather_day1) + " TEMP: " + tempDay1_Celsius + "°C " +
                                           "imorn" + " väder: " + WeatherTranslation(weather_day2) + " TEMP: " + tempDay2_Celsius + "°C " +
                                           date_day3 + " väder: " + WeatherTranslation(weather_day3) + " TEMP: " + tempDay3_Celsius + "°C " +
                                           date_day4 + " väder: " + WeatherTranslation(weather_day4) + " TEMP: " + tempDay4_Celsius + "°C " +
                                           date_day5 + " väder: " + WeatherTranslation(weather_day5) + " TEMP: " + tempDay5_Celsius + "°C ";
           // System.out.println(date_day1 + " väder: " + WeatherTranslation(weather_day1) + " - temperatur: " + tempDay1_Celsius + " grader");
           // System.out.println(date_day2 + " väder: " + WeatherTranslation(weather_day2) + " - temperatur: " + tempDay2_Celsius + " grader");
           // System.out.println(date_day3 + " väder: " + WeatherTranslation(weather_day3) + " - temperatur: " + tempDay3_Celsius + " grader");
           // System.out.println(date_day4 + " väder: " + WeatherTranslation(weather_day4) + " - temperatur: " + tempDay4_Celsius + " grader");
           // System.out.println(date_day5 + " väder: " + WeatherTranslation(weather_day5) + " - temperatur: " + tempDay5_Celsius + " grader");





        } catch (Exception e) {
            e.printStackTrace();
        }

        return FinishedWeatherString;
    }
    // Oanvänd metod som bygger URL som hämtar soluppgång och solnedgång.
    // Kräver att man skickar in latitud och longitud från JSONobjektet/Staden från SMHI
    
    public String getFullUrl_sun(double lat, double lng) 
    {
    	
    	return sun + "lat=" + lat + "&lng=" + lng + "callback=mycallback";	
    }
    /*

    Denna metod används inte om vi specificerar staden för väder i kontruktorn

    public String getFullUrl() {
    	
    	System.out.println("Vilken Stad vill du hämta väderrapport från?: (åäö =aao)");
    	
    	Scanner scan = new Scanner(System.in);
    	String town = scan.nextLine();
    	
    	return forecast + "q=" + town + "&appid=" + API_key;

        
    }
    */
    public static double Kelvin_to_Celsius(double Kelvin) throws NumberFormatException, IOException
    
    {
    
    	double celsius;
        celsius = Kelvin - 273.0;
        int celsius_rounded = (int) celsius;
        return celsius_rounded;
    
    }


    //Metoden översätter det vi får från API på engelska till svenska
    public static String WeatherTranslation(String in_eng) 
    {
    	if (in_eng.equalsIgnoreCase("Clear sky")) {return "Klar himmel";}
    	if (in_eng.equalsIgnoreCase("Nearly clear sky")) {return "Nästan klar himmel";}
    	if (in_eng.equalsIgnoreCase("Variable cloudiness")) {return "Varierande molnighet";}
        if (in_eng.equalsIgnoreCase("few clouds")) {return "Lätt molnighet";}
    	if (in_eng.equalsIgnoreCase("Halfclear sky")) {return "Halvklar himmel";}
    	if (in_eng.equalsIgnoreCase("Cloudy sky")) {return "Molnig himmel";}
    	if (in_eng.equalsIgnoreCase("Overcast")) {return "Mulet";}
    	if (in_eng.equalsIgnoreCase("Fog")) {return "Dimma";}
    	if (in_eng.equalsIgnoreCase("Light rain showers")) {return "Lätta regnskurar";}
    	if (in_eng.equalsIgnoreCase("Moderate rain showers")) {return "måttliga regnskurar";}
    	if (in_eng.equalsIgnoreCase("Heavy rain showers")) {return "Kraftiga regnskurar";}
    	if (in_eng.equalsIgnoreCase("Thunderstorm")) {return "Åskoväder";}
    	if (in_eng.equalsIgnoreCase("Light sleet showers")) {return "Lätta snöblandade skurar";}
    	if (in_eng.equalsIgnoreCase("Moderate sleet showers")) {return "Måttliga snöblandade skurar";}
    	if (in_eng.equalsIgnoreCase("Heavy sleet showers")) {return "Kraftiga snöblandade skurar";}
    	if (in_eng.equalsIgnoreCase("Light snow showers")) {return "Lätta snöskurar";}
    	if (in_eng.equalsIgnoreCase("Moderate snow showers")) {return "Måttliga snöskurar";}
    	if (in_eng.equalsIgnoreCase("Heavy snow showers")) {return "Kraftiga snöskurar";}
    	if (in_eng.equalsIgnoreCase("Light rain")) {return "Lätt regn";}
    	if (in_eng.equalsIgnoreCase("Moderate rain")) {return "Måttligt regn";}
    	if (in_eng.equalsIgnoreCase("Heavy rain")) {return "Kraftigt regn";}
    	if (in_eng.equalsIgnoreCase("Thunder")) {return "Åska";}
    	if (in_eng.equalsIgnoreCase("Light sleet")) {return "Lätt snöblandat regn";}
    	if (in_eng.equalsIgnoreCase("Moderate sleet")) {return "Måttligt snöblandat regn";}
    	if (in_eng.equalsIgnoreCase("Heavy sleet")) {return "Kraftigt snöblandat regn";}
    	if (in_eng.equalsIgnoreCase("Light snowfall")) {return "Lätt snöfall";}
    	if (in_eng.equalsIgnoreCase("Moderate snowfall")) {return "Måttligt snöfall";}
    	if (in_eng.equalsIgnoreCase("Heavy snowfall")) {return "Kraftigt snöfall";}
    	
    	if (in_eng.equalsIgnoreCase("Scattered clouds")) {return "Lätt målnighet";}
    	if (in_eng.equalsIgnoreCase("Broken clouds")) {return "Lätt målnighet";}
    	// Returnerar utan översättning
    	return in_eng;
    	
    	
    }

}
