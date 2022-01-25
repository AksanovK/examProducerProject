package exam.redis.repositories;

import exam.redis.models.RedisUser;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@EnableRedisRepositories
public interface RedisUsersRepository extends KeyValueRepository<RedisUser, String> {
}
