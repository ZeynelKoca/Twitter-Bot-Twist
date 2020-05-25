import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

    public static void main(String[] args) {

        System.out.println("consumerkey: " + System.getenv("h_consumerKey"));
        System.out.println("consumersecret: " + System.getenv("h_consumerSecret"));
        System.out.println("accesstoken: " + System.getenv("h_accessToken"));
        System.out.println("accesstokensecret: " + System.getenv("h_accessTokenSecret"));

        ConfigurationBuilder cb = new ConfigurationBuilder();
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            Thread.sleep(5000);
            twitter.updateStatus("Tweet sent from java 25.5.2020");
        } catch (TwitterException e) {
            e.printStackTrace();
            System.out.println("Twitter Exception");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Worker woke up");
        }
    }
}