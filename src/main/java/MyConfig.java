import com.mongodb.client.MongoDatabase;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import redis.clients.jedis.Jedis;

public class MyConfig extends CrawlConfig {
    private MongoDatabase database;
    private Jedis redis;

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public void setRedis(Jedis redis) {
        this.redis = redis;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public Jedis getRedis() {
        return redis;
    }
}
