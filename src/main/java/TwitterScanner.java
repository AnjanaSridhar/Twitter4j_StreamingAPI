import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class TwitterScanner {
    private String companyName;
    private TSValue prev;

    private int count;

    private static String SEARCH_STRING = "facebook";
    private static int INITIAL_TWEET_COUNT = 100;
    private static int MONITOR_INTERVAL_IN_SECONDS = 30;
    private static int TOTAL_MONITOR_DURATION_IN_SECONDS = 180;
    private static Instant APP_UP_TIME = Instant.now().plus(Duration.ofSeconds(TOTAL_MONITOR_DURATION_IN_SECONDS));

    public static void main(String[] args) {
        TwitterScanner scanner = new TwitterScanner(SEARCH_STRING);
        scanner.run();
    }

    public static class TSValue {
        private Instant timestamp;
        private double val;

        public TSValue(Instant timestamp, double val) {
            this.timestamp = timestamp;
            this.val = val;
        }

        public double getVal() {
            return val;
        }
    }

    public TwitterScanner(String companyName) {
        this.companyName = companyName;
    }

    public void run() {

        final StatusListener listener = new StatusListener() {
            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onStatus(Status status) {
                count++;
                if (Instant.now().isAfter(prev.timestamp)) {
                    storeValue(new TSValue(setToNextHour(), count));
                    count = 0;
                }
                if (Instant.now().isAfter(APP_UP_TIME)) {
                    System.out.println("Exiting from application");
                    System.exit(1);
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int i) {

            }

            @Override
            public void onScrubGeo(long l, long l1) {

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }
        };

        final ConfigurationBuilder cb = new ConfigurationBuilder();
        Properties prop = new Properties();
        try {
            InputStream inputStream =
                    TwitterScanner.class.getClassLoader().getResourceAsStream("twitter4j.properties");
            prop.load(inputStream);

            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(prop.getProperty("CONSUMER_KEY"))
                    .setOAuthConsumerSecret(prop.getProperty("CONSUMER_SECRET"))
                    .setOAuthAccessToken(prop.getProperty("ACCESS_TOKEN"))
                    .setOAuthAccessTokenSecret(prop.getProperty("ACCESS_TOKEN_SECRET"));


        } catch (IOException e) {
            e.getLocalizedMessage();
        }

        TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
        final TwitterStream twitterStream = tf.getInstance();
        twitterStream.addListener(listener);

        FilterQuery filter = new FilterQuery();
        String[] keywordsArray = {this.companyName};
        filter.track(keywordsArray);
        twitterStream.filter(filter);

        prev = new TSValue(setToNextHour(), INITIAL_TWEET_COUNT);
    }

    private void storeValue(TSValue tsValue) {
        calculatePercentageDecrease(tsValue, prev);
        prev = tsValue;
    }

    private Instant setToNextHour() {
        //return Instant.now().truncatedTo(ChronoUnit.HOURS).plus(Duration.ofSeconds(INTERVAL_IN_SECONDS));
        return Instant.now().plus(Duration.ofSeconds(MONITOR_INTERVAL_IN_SECONDS));
    }

    private void calculatePercentageDecrease(TSValue newVal, TSValue prevVal) {
        String message;
        double percentage;

        if (newVal.getVal() > prevVal.getVal()) {
            percentage = (newVal.getVal() - prevVal.getVal()) / (prevVal.getVal() == 0 ? 1 : prevVal.getVal());
            message = "Increase by ";
        } else {
            percentage = (prevVal.getVal() - newVal.getVal()) / (prevVal.getVal() == 0 ? 1 : prevVal.getVal());
            message = "Decrease by ";
        }
        System.out.println(message + NumberFormat.getPercentInstance().format(percentage));
    }
}
