import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.bson.Document;
import redis.clients.jedis.Jedis;

import javax.print.Doc;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
//        MongoClient mongoClient = new MongoClient("localhost", 27017);
//        MongoDatabase database = mongoClient.getDatabase("news");
//        System.out.println("___________________");
//        if(!database.listCollectionNames().into(new ArrayList<String>()).contains("news"))
//            database.createCollection("news");
//        MongoCollection<Document> collection = database.getCollection("news");

        BufferedWriter writer = new BufferedWriter(new FileWriter("crawl.txt"));

        Jedis redis = new Jedis("localhost", 6379);

        redis.del("url");

        File crawlStorage = new File("src/test/resources/crawler4j");
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());
        config.setMaxDepthOfCrawling(3);

        int numCrawlers = 6;

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer= new RobotstxtServer(robotstxtConfig, pageFetcher);

        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed("https://vnexpress.net/");
//        controller.addSeed("https://vnexpress.net/nhung-diem-moi-trong-cac-quyet-dinh-nhan-su-cua-quoc-hoi-4260666.html");

//        CrawlController.WebCrawlerFactory<HtmlCrawler> factory = ()-> new HtmlCrawler(writer, collection);
        CrawlController.WebCrawlerFactory<HtmlCrawler> factory = ()-> new HtmlCrawler(writer, redis);
        controller.start(factory, numCrawlers);
        writer.close();
    }

    public static void testDB(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("news");
        if(!database.listCollectionNames().into(new ArrayList<String>()).contains("news"))
            database.createCollection("news");

        for (String name : database.listCollectionNames()){
            System.out.println(name);
        }

        MongoCollection<Document> collection = database.getCollection("news");
        FindIterable<Document> iterable = collection.find(new Document("link", "bla bla")).limit(1);
        if(iterable.first() != null)
            System.out.println("OK");


    }

}
