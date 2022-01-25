package exam.redis.repositories;

import exam.repositories.BlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class BlackListRepositoryRedisTemplateImpl implements BlackListRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String token) {
        redisTemplate.opsForValue().set(token, "", 3600000);
    }

    @Override
    public boolean exists(String token) {
        Boolean hasToken = redisTemplate.hasKey(token);
        return hasToken != null && hasToken;
    }
}
