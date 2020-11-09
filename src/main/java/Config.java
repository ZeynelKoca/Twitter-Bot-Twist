import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

class Config {

    private static TwitterFactory tf;

    Config() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(System.getenv("h_consumerKey")) //Twitter API key
                .setOAuthConsumerSecret(System.getenv("h_consumerSecret")) //Twitter API secret key
                .setOAuthAccessToken(System.getenv("h_accessToken")) //Twitter Access token
                .setOAuthAccessTokenSecret(System.getenv("h_accessTokenSecret")); //Twitter Access token secret
        tf = new TwitterFactory(cb.build());
    }

    Twitter getTwitterInstance(){
        return tf.getInstance();
    }
}
