import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.bson.Document;
import redis.clients.jedis.Jedis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class HtmlCrawler extends WebCrawler {
    private final static Pattern EXCLUSIONS
            = Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");

    private final static Pattern NEWS = Pattern.compile(".*((-)\\d+(\\.(html)))");


    private BufferedWriter writer;

    public HtmlCrawler(BufferedWriter writer){
        this.writer = writer;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        boolean check = !EXCLUSIONS.matcher(urlString).matches()
                && NEWS.matcher(urlString).matches();
        if(check) {
            MyController controller = (MyController) this.myController;
            if (!controller.isAcceptDomain(urlString))
                return false;
            MyConfig config = (MyConfig) controller.getConfig();
            Jedis redis = config.getRedis();
            String domain = url.getDomain();
            if (redis.sismember(domain, urlString))
                return false;
            redis.sadd(domain, urlString);
            url.setPriority((byte) referringPage.getWebURL().getDepth());
            return true;
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        short depth = page.getWebURL().getDepth();
        try {
            writer.write(url + "----" + depth +"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            // Xử lý data thu được ở đây
        }
    }
}
