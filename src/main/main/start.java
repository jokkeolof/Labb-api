

public class start {

    //Main entry point
    public static void main (String[] Args)
    {


        //Skapar en ny instans av GetTwitter.java som tar in namnet på våran bot som inparameter(OBS, TA INTE MED @ DETTA GÖR PROGRAMMET SJÄLV)
        GetTwitter bot = new GetTwitter("vadforvader");
        bot.GetStatusUpdates(); //Callar på metoden som startar en lyssnare på twitter.


    }
}
