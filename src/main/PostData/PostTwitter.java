//Import av twitter4j via maven
import twitter4j.*;
import twitter4j.conf.*; //För configurationbulder

public class PostTwitter {

    /*
        TODO: filen twitter4j.properties håller 0auth nycklarna för att kunna posta mot twitter. Detta får du via din twitter developer konto
        TODO: Här kan vi även ta upp säkerhetsbiten. Filen innehåller en privat token som man absolut inte ska dela ut, går denna fil att säkra ytterligare eller bara se till att ingen kan få tag på den?
     */

    public static void main(String[] args) {



        // The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();
        //Här kan vi posta en simpel text till tidslinjen på twitter
        int jobs = 0;
        String twitterUpdateText = "Igår inkom det totalt: " + jobs + " till arbetsförmedlingen.";
        Status status = null; //Initierar en variabel av typen Status

        //updateStatus() kan slänga ett TwitterException så vi måste hantera det
        try{
            status = twitter.updateStatus(twitterUpdateText);
        }catch (TwitterException tw) {
            System.out.println("Fel vid uppdatering av tweet " + tw);
        }
        System.out.println("Statusen på twitter uppdaterat till: " + status.getText());

    }

}