//Import av twitter4j via maven
import twitter4j.*;


public class PostTwitter {

    /*
        TODO: filen twitter4j.properties håller 0auth nycklarna för att kunna posta mot twitter. Detta får du via din twitter developer konto
        TODO: Här kan vi även ta upp säkerhetsbiten. Filen innehåller en privat token som man absolut inte ska dela ut, går denna fil att säkra ytterligare eller bara se till att ingen kan få tag på den?
     */

    public PostTwitter(String username, String weatherInfo) {



        // The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();

        //Bygger upp texten som vi skickar till användaren
        String twitterUpdateText = "@" + username + " " + weatherInfo;
        Status status = null; //Initierar en variabel av typen Status

        //updateStatus() kan slänga ett TwitterException så vi måste hantera det

        try{
            status = twitter.updateStatus(twitterUpdateText);
        }catch (TwitterException tw) {
            System.out.println("Fel vid uppdatering av tweet " + tw);
        }
        System.out.println("Skickat väder till användare");

    }

}