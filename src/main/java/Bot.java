import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Bot {

    public static Twitter twitter;

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

        //sendTweet("Tweet");
    }

    private static void sendTweet(String text) {
        try {
            twitter.updateStatus(text);
            System.out.println("New tweet has been sent: " + text);
        } catch (TwitterException e) {
            System.out.println("Twitter Exception:");
            e.printStackTrace();
        }
    }
}