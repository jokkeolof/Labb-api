import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetTwitter {

    private String botValue;

    //Konstruktor som sätter vilket @namn vi ska kolla på
    public GetTwitter(String botName) {
        botValue = botName;
    }

    //Metoden tar strängen som användaren skriver in ex "@vadforvader Malmö" och rensar bort allt utom just malmö. Om användaren skulle skriva ex "@vadforvader Malmö hej på dig"
    public String SplitString(String userInput) {
        int index = 0;
        String bot = "@" + botValue;
        String inputFromTwitterUser = userInput; //Input strängen vi får från twitter
        String[] tempArray;

        String delimiter = "\\s+"; //Ett regex expression som kollar efter spaces i en sträng (e.g " "). Detta expression tar då bort alla spaces. Alltså även (e.g "  ")

        tempArray = inputFromTwitterUser.split(delimiter);//Splitar strängen som vi får från twittar till en temporär array, tar bort alla spaces


        //for loopen går igenom arrayen och letar efter @namn (alltså namnet på botten
        for (int i = 0; i <= tempArray.length - 1; i++) {

            if (bot.contains(tempArray[i])) {
                index = i;
            }
        }
        return tempArray[index + 1]; //Returna värdet @vadförvader men lägg till +1 för att få orten som kommer efter man frågat boten
    }

    //Metoden använder vi för att hämta statusuppdateringar som innehåller @vadforvader
    public void GetStatusUpdates() {

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






