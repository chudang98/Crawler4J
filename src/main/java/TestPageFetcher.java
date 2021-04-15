import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.crawler.exceptions.ParseException;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.*;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.HttpEntity;

import java.io.*;

public class TestPageFetcher {
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, NotAllowedContentException, ParseException {
        CrawlConfig config = new CrawlConfig();
        PageFetcher pageFetcher = new PageFetcher(config);

        WebURL url = new WebURL();

//        url.setURL("https://vietnamnet.vn/");
        url.setURL("https://vnexpress.net/");
        PageFetchResult fetchResult = null;

//        Parser parser = new Parser(config);
//        Page page = new Page(url);
        try {
            fetchResult = pageFetcher.fetchPage(url);

            HttpEntity entity = fetchResult.getEntity();
            InputStream stream = entity.getContent();

            File targetFile = new File("content.html");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(stream.readAllBytes());

            outStream.close();

//            fetchResult.fetchContent(page, 1048576);
//            parser.parse(page, url.getURL());
//
//            HtmlParseData html = (HtmlParseData) page.getParseData();
//            String content = html.getHtml();
//
//            BufferedWriter writer = new BufferedWriter(new FileWriter("content.html"));
//            writer.write(content);
//            writer.close();
        } catch (InterruptedException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (PageBiggerThanMaxSizeException e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            pageFetcher.shutDown();
        }
    }
}
