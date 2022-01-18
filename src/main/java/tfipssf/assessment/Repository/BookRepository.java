package tfipssf.assessment.Repository;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository implements Serializable {
    @Autowired
    @Qualifier("start")
    private RedisTemplate<String,String> redTemplate;

    Logger logger=Logger.getLogger("from redis repo");

    public void save(String Key, String Value){
        redTemplate.opsForValue().set(Key, Value,10,TimeUnit.MINUTES);
    }
    public String retrieve(String Key){
        return redTemplate.opsForValue().get(Key);
    }

    public boolean checkifcache(String works_id){
        if (redTemplate.hasKey(works_id)){
        return true;
        }
        else return false;
    }


}
