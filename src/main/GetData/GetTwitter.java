import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetTwitter {
    List<String> tweetersTimeout = new ArrayList<String>(); //Detta skulle man kunna lägga in i en database för att få lite mer statistik

    //A user will get temporary banned after asking for a status update
    public static void CheckForBannedUser() {

    }

    //Metoden tar strängen som användaren skriver in ex "@vadforvader Malmö" och rensar bort allt utom just malmö. Om användaren skulle skriva ex "@vadforvader Malmö hej på dig"
    public static String SplitString(String userInput) {
        String splitString = userInput;
        String[] tempArray;

        /* delimiter */
        String delimiter = " ";

        /* given string will be split by the argument delimiter provided. */
        tempArray = splitString.split(delimiter);

        /* print substrings */
        for (int i = 0; i < tempArray.length; i++) {
        }
        System.out.println(tempArray[1]);

        return tempArray[1];
    }

    //Metoden använder vi för att hämta statusuppdateringar som innehåller @vadforvader
    public static void GetStatusUpdates() {

        //En ny Twitterlyssnare, detta interface implementerar flera metoder vi behöver ta in.. Bara onStatus() använder vi dock
        StatusListener listener = new StatusListener(){

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg) {
                System.out.println("Got a status deletion notice id:" + arg.getStatusId());
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            //När vi får in en statusuppdatering som innehåller @vadforvader.
            @Override
            public void onStatus(Status status) {
                String fromUser = status.getText();
                GetWeather getweather = new GetWeather(SplitString(fromUser));
                String weatherdata = getweather.getDocument();
                PostTwitter postToTwitter = new PostTwitter(status.getInReplyToScreenName().toString(), weatherdata);
                System.out.println("ANVÄNDAREN: " + status.getUser() + " frågade efter vädret");

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }
        };

        //Vi skapar en ny twitterström. Denna ska köras i sin egna Tråd, detta tar paketet hand om
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);

        //Startar twitterströmmen med filter för att filtrera på Tweets som innehåller @vadforvader
        twitterStream.filter("@vadforvader");


    }

    }






