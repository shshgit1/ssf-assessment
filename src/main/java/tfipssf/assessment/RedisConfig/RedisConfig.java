package tfipssf.assessment.RedisConfig;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {
        
    
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    
        @Value("${spring.redis.host}")  //this pulls data from the application.properties
        private String redisHost;

        @Value("${spring.redis.port}") 
        private Optional<Integer> redisPort;

        //@Value("${spring.redis.password}") 
        private String redisPassword=System.getenv("RedisPassword");
        
        @Value("${spring.redis.database}")
        private Integer redisDatabase;
        
        @Bean ("start")
        //@Scope("singleton")
        public RedisTemplate<String, String> redisTemplate(){
                final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
                config.setHostName(redisHost);
                config.setPort(redisPort.get());

                if (redisPassword!=null){
                config.setPassword(redisPassword);
                }//end if, set redispassword
                config.setDatabase(redisDatabase);
                
                JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
                JedisConnectionFactory jedisFac =new JedisConnectionFactory(config, jedisClient); 
                jedisFac.afterPropertiesSet();
                logger.info("redis host port> {redisHost} {redisPort}", redisHost, redisPort);
                        
                final RedisTemplate<String, String> template = new RedisTemplate<>();
                template.setConnectionFactory(jedisFac);
                template.setKeySerializer(new StringRedisSerializer()); 
                template.setValueSerializer(new StringRedisSerializer());
                
                /* RedisSerializer<Object> serializer = 
                new JdkSerializationRedisSerializer(getClass().getClassLoader());
                template.setValueSerializer(serializer) ; */


                return template;
        }
    
}
