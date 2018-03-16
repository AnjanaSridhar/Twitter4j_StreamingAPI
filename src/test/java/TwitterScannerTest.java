import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterScannerTest {
    @Mock
    private TwitterStreamFactory twitterStreamFactory;
    @Mock
    private TwitterStream twitterStream;
    @Mock
    private ConfigurationBuilder cb;
    @Mock
    private FilterQuery filterQuery;

    @Spy
    private TwitterScanner twitterScanner;

    @Before
    public void setUp(){
        cb = new ConfigurationBuilder();
        twitterStreamFactory = new TwitterStreamFactory(cb.build());
        twitterStream = twitterStreamFactory.getInstance();

    }

    @Test
    public void test(){
        //given
        twitterScanner = new TwitterScanner("facebook");
        //when
        twitterScanner.run();
    }
}
