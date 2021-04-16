package module;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import redis.clients.jedis.Jedis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class HtmlCrawler extends WebCrawler {
    private final static Pattern EXCLUSIONS
            = Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");

    private final static Pattern NEWS = Pattern.compile(".*((-)\\d+(\\.(html)))");

    public HtmlCrawler(){

    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL();
        return !EXCLUSIONS.matcher(urlString).matches()
                && NEWS.matcher(urlString).matches()
                && urlString.startsWith("https://vnexpress.net/");
    }

    @Override
    public void visit(Page page) {
//        String url = page.getWebURL().getURL();
//        short depth = page.getWebURL().getDepth();
//        if (page.getParseData() instanceof HtmlParseData) {
//            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//            String title = htmlParseData.getTitle();
//            String text = htmlParseData.getText();
//            String html = htmlParseData.getHtml();
//            Set<WebURL> links = htmlParseData.getOutgoingUrls();
//            // Xử lý data thu được ở đây
//        }
    }
}
