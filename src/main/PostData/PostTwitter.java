//Import av twitter4j via maven
import twitter4j.*;


public class PostTwitter {


    //Metoden tar användarnamnet + väderinfo som ska skickas
    public PostTwitter(String username, String weatherInfo) {



        // The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();

        //Bygger upp texten som vi skickar till användaren
        String twitterUpdateText = "@" + username + " " + weatherInfo;
        Status status; //Initierar en variabel av typen Status

        //Skickar statusuppdateringen

        try{
            status = twitter.updateStatus(twitterUpdateText);
        }catch (TwitterException tw) {
            System.out.println("Fel vid uppdatering av tweet " + tw);
        }
        System.out.println("Skickat väder till användare");

    }

}