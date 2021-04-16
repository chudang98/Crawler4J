package module;

import com.sleepycat.je.*;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.frontier.WorkQueues;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestTakeAllLink {
    public static void main(String[] args) throws Exception {

        // Todo: Thực hiện việc Crawl như bình thường để đẩy các link vào DB
        CrawlConfig config = new CrawlConfig();
        File crawlStorage = new File("src/test/resources/test_link");

        config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());
        config.setMaxDepthOfCrawling(2);

        int numCrawlers = 6;

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);


        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        controller.addSeed("https://vnexpress.net/");

        CrawlController.WebCrawlerFactory<HtmlCrawler> factory = () -> new HtmlCrawler();

        controller.start(factory, numCrawlers);

        BufferedWriter writer = new BufferedWriter(new FileWriter("linkCrawled.txt"));


        // TODO: Config database DocIds
        boolean resumable = false;

        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setTransactional(resumable);
        envConfig.setLocking(resumable);
        envConfig.setLockTimeout(config.getDbLockTimeout(), TimeUnit.MILLISECONDS);

        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(config.isResumableCrawling());
        dbConfig.setDeferredWrite(!config.isResumableCrawling());

        /*
            Thư mục chứa DB được config trong Controller
              nên phải setup đúng thư mục đó mới có thể lấy dữ liệu ra đúng.
         */
        File envHome = new File(config.getCrawlStorageFolder() + "/frontier");

        Environment env = new Environment(envHome, envConfig);

        Database docIdsDB = env.openDatabase(null, "DocIDs", dbConfig);

        Cursor cursor = docIdsDB.openCursor(null, null);

        DatabaseEntry foundKey = new DatabaseEntry();
        DatabaseEntry foundData = new DatabaseEntry();

        // TODO : Lấy các link đã được lưu vào DB rồi lưu vào file "linkCrawled.txt"
        while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
                OperationStatus.SUCCESS) {
            writer.write(new String(foundKey.getData()) + "\n");
        }
        writer.close();
    }
}
