import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.bson.Document;
import redis.clients.jedis.Jedis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class HtmlCrawler extends WebCrawler {

    private static MongoCollection<Document> collection;

    private BufferedWriter writer;
    private Jedis redis;

    private final static Pattern EXCLUSIONS
            = Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");

    private final static Pattern NEWS = Pattern.compile(".*(\\.(html))$");

    public HtmlCrawler(BufferedWriter writer, Jedis redis){
        this.writer = writer;
        this.redis = redis;
    }

    @Override
    public synchronized boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        referringPage.getParseData();
        boolean check = !EXCLUSIONS.matcher(urlString).matches()
                && NEWS.matcher(urlString).matches()
                && urlString.startsWith("https://vnexpress.net/");
        if(redis.sismember("url", urlString))
            return false;
        if(check){
            redis.sadd("url", urlString);
            url.setPriority((byte) referringPage.getWebURL().getDepth());
        }
        return check;
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        short depth = page.getWebURL().getDepth();
        try {
            writer.write(url + " --- " + depth + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
//            saveURL(url);
            // do something with the collected data
        }
    }

    private static void saveURL(String url){
        FindIterable<org.bson.Document> iterable = collection.find(new Document("link", url)).limit(1);
        if(iterable.first() == null)
            collection.insertOne(new Document("link", url));
    }
}
