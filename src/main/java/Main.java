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
import java.util.List;

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
        redis.flushAll();
        File crawlStorage = new File("src/test/resources/crawler4j");
        MyConfig config = new MyConfig();
        config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());
        config.setMaxDepthOfCrawling(2);
        config.setRedis(redis);

        int numCrawlers = 6;

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer= new RobotstxtServer(robotstxtConfig, pageFetcher);

        MyController controller = new MyController(config, pageFetcher, robotstxtServer);

        controller.addSeed("https://vnexpress.net/");
//        controller.addSeed("https://vietnamnet.vn/");

        controller.addAcceptDomain("https://vnexpress.net/", "https://vietnamnet.vn/");

        CrawlController.WebCrawlerFactory<HtmlCrawler> factory = ()-> new HtmlCrawler(writer);

//        boolean isBlocking = false;
        controller.start(factory, numCrawlers);
        writer.close();
    }

//    public static void testDB(){
//        MongoClient mongoClient = new MongoClient("localhost", 27017);
//        MongoDatabase database = mongoClient.getDatabase("news");
//        if(!database.listCollectionNames().into(new ArrayList<String>()).contains("news"))
//            database.createCollection("news");
//
//        for (String name : database.listCollectionNames()){
//            System.out.println(name);
//        }
//
//        MongoCollection<Document> collection = database.getCollection("news");
//        FindIterable<Document> iterable = collection.find(new Document("link", "bla bla")).limit(1);
//        if(iterable.first() != null)
//            System.out.println("OK");
//
//
//    }

}
