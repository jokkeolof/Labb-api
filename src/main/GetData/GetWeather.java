import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetWeather {
    private String forecast = "http://api.openweathermap.org/data/2.5/forecast?";
    private String API_key = weatherApiConfig.API;
    private String forecastURL;
    StringBuffer responseData = new StringBuffer();
    String FinishedWeatherString = null;
    String location= "";
    
    public GetWeather(String location) 
    {
        this.location=location;
        forecastURL = forecast + "q=" + location + "&appid=" + API_key;

    }
    
    public String getDocument() 
    {

        

       try {
	            URL furl = new URL(forecastURL);
	            HttpURLConnection conn = (HttpURLConnection) furl.openConnection();
	            
	            conn.setRequestMethod("GET");
	            conn.setRequestProperty("accept", "application/json");
	            conn.setRequestProperty("charset", "utf-8");
	            
	
	            int responseCode = 0;
	            responseCode = conn.getResponseCode();
		     
	            System.out.println("Request : " + forecastURL);
	            System.out.println("Code    : " + responseCode);
	            
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line = null;
	           
	
	            while ((line = in.readLine()) != null) {
	               responseData.append(line);
	            }
	            in.close();
            
	            	

        	} 
       catch 	(Exception e) 
       		{
    	   		System.err.println("Hittar ingen prognos!");
       		}

        
        return JSONtoString(location);
    }
    
    
    public String JSONtoString(String location) 
    {
    	 // Huvudobjektet
        
        JSONObject obj_JSONObject = new JSONObject(responseData.toString()); 
          
        // Nivå 2 - listan över alla prognoser
        
        JSONArray list = obj_JSONObject.getJSONArray("list");
        
        // Objekt av alla dagar kl 15
        
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
        
        // Sökväg till datum och tid (idag och imorg utesluts)
        
        String date_day3 = (String) day3.get("dt_txt");
        String date_day4 = (String) day4.get("dt_txt");
        String date_day5 = (String) day5.get("dt_txt");
        
        // Omvandling av temperatur till Celsius men för många decimaler
        
        double tempDay1_Celsius=0;
        double tempDay2_Celsius=0;
        double tempDay3_Celsius=0;
        double tempDay4_Celsius=0;
        double tempDay5_Celsius=0;
        
       
		try {
				tempDay1_Celsius = Kelvin_to_Celsius((Double) main_d1.get("temp"));
				tempDay2_Celsius= Kelvin_to_Celsius((Double) main_d2.get("temp"));
		        tempDay3_Celsius= Kelvin_to_Celsius((Double) main_d3.get("temp"));
		        tempDay4_Celsius= Kelvin_to_Celsius((Double) main_d4.get("temp"));
		        tempDay5_Celsius= Kelvin_to_Celsius((Double) main_d5.get("temp"));
			} 
		
		catch (NumberFormatException e) {e.printStackTrace();}
		catch (JSONException e) 		{e.printStackTrace();} 	
		catch (IOException e) 			{e.printStackTrace();}
        
        
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
       

        // Skriver ut väder och temperatur
        
         FinishedWeatherString = "Väderprognos "+ " " + location + " |" +  " idag" + " väder: " + WeatherTranslation(weather_day1) + " TEMP: " + tempDay1_Celsius + "°C " +
         "imorn" + " väder: " + WeatherTranslation(weather_day2) + " TEMP: " + tempDay2_Celsius + "°C " +
         date_day3 + " väder: " + WeatherTranslation(weather_day3) + " TEMP: " + tempDay3_Celsius + "°C " +
         date_day4 + " väder: " + WeatherTranslation(weather_day4) + " TEMP: " + tempDay4_Celsius + "°C " +
         date_day5 + " väder: " + WeatherTranslation(weather_day5) + " TEMP: " + tempDay5_Celsius + "°C ";
         location = "";
         return FinishedWeatherString;
         
         
       
    }
    public static double Kelvin_to_Celsius(double Kelvin) throws NumberFormatException, IOException
    
    {
    
    	double celsius;
        celsius = Kelvin - 273.0;
        int celsius_rounded = (int) celsius;
        return celsius_rounded;
    
    }


    
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
    	if (in_eng.equalsIgnoreCase("few clouds")) {return "Lätt målnighet";}
    	if (in_eng.equalsIgnoreCase("overcast clouds")) {return "Lätt målnighet";}
    	
    	// Returnerar utan översättning
    	return in_eng;
    	
    	
    }

}