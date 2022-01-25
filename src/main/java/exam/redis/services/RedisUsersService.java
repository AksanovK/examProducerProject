package exam.redis.services;

import exam.models.User;


public interface RedisUsersService {
    void addTokenToUser(User user, String token);

    void addAllTokensToBlackList(User user);

    boolean checkToken(String token, User user);
}
