package tfipssf.assessment.Repository;

import java.io.Serializable;
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
        redTemplate.opsForValue().set(Key, Value);
    }
    public String retrieve(String Key){
        return redTemplate.opsForValue().get(Key);
    }


}
