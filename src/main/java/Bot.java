import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Bot {

    public static Twitter twitter;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static long daysLeft = 999;


    private static void configureTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(System.getenv("h_consumerKey")) //Twitter API key
                .setOAuthConsumerSecret(System.getenv("h_consumerSecret")) //Twitter API secret key
                .setOAuthAccessToken(System.getenv("h_accessToken")) //Twitter Access token
                .setOAuthAccessTokenSecret(System.getenv("h_accessTokenSecret")); //Twitter Access token secret
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public static void main(String[] args) {
        configureTwitter();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 17);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        // every day at 17:00 you run your task
        Timer timer = new Timer();
        timer.schedule(birthdayCounter, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 1 day
    }

    private static TimerTask birthdayCounter = new TimerTask() {
        @Override
        public void run() {

            while (daysLeft > 0) {
                daysLeft = getDayCount(simpleDateFormat.format(new Date()), "26.10.2020");
                if (daysLeft == 1) {
                    sendTweet(daysLeft + " day left until my birthday");
                } else if (daysLeft == 0) {
                    sendTweet("Today is my 21st birthday!");
                } else {
                    sendTweet(daysLeft + " days left until my birthday");
                }
            }
            sendTweet("Today is my 21st birthday!");
        }
    };

    private static void sendTweet(String text) {
        try {
            twitter.updateStatus(text);
            System.out.println("New tweet has been sent: " + text);
        } catch (TwitterException e) {
            System.out.println("Twitter Exception:");
            e.printStackTrace();
        }
    }

    //https://stackoverflow.com/a/24409106
    public static long getDayCount(String start, String end) {
        long diff = -1;
        try {
            Date dateStart = simpleDateFormat.parse(start);
            Date dateEnd = simpleDateFormat.parse(end);

            //time is always 00:00:00, so rounding should help to ignore the missing hour when going from winter to summer time, as well as the extra hour in the other direction
            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
        } catch (Exception e) {
            //handle the exception according to your own situation
        }
        return diff;
    }
}