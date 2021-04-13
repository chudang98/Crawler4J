import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MyController extends CrawlController {
    private List<String> acceptDomain;

    public MyController(CrawlConfig config, PageFetcher pageFetcher, RobotstxtServer robotstxtServer) throws Exception {
        super(config, pageFetcher, robotstxtServer);
        acceptDomain = new ArrayList<String>();
    }

    public MyController(CrawlConfig config, PageFetcher pageFetcher, Parser parser, RobotstxtServer robotstxtServer) throws Exception {
        super(config, pageFetcher, parser, robotstxtServer);
        acceptDomain = new ArrayList<String>();
    }


    public void addAcceptDomain(String ...domains){
        for(String domain : domains)
            acceptDomain.add(domain);
    }

    public boolean isAcceptDomain(String url){
        for(String domain : acceptDomain){
            if(url.startsWith(domain))
                return true;
        }
        return false;
    }

}
