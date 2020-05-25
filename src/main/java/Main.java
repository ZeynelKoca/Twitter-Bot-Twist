import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

    public static void main(String[] args) {
        Twitter twitter = configureTwitter();

        try {
            Thread.sleep(5000);
            twitter.updateStatus("Tweet sent from java 25.5.2020-3");
        } catch (TwitterException e) {
            e.printStackTrace();
            System.out.println("Twitter Exception");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Worker woke up");
        }
    }

    private static Twitter configureTwitter(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(System.getenv("h_consumerKey"))
                .setOAuthConsumerSecret(System.getenv("h_consumerSecret"))
                .setOAuthAccessToken(System.getenv("h_accessToken"))
                .setOAuthAccessTokenSecret(System.getenv("h_accessTokenSecret"));
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }
}