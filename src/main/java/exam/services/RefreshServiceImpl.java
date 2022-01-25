package exam.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import exam.models.Token;
import exam.models.User;
import exam.redis.services.RedisUsersService;
import exam.repositories.TokensRepository;
import exam.repositories.UsersRepository;
import exam.security.token.TokensUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RefreshServiceImpl implements RefreshService {

    @Autowired
    public TokensRepository tokensRepository;

    @Autowired
    private RedisUsersService redisUsersService;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public boolean timeCheck(DecodedJWT decodedJWT, long limit) {
        Date time = decodedJWT.getExpiresAt();
        Date nowTime = new Date();
        return (nowTime.getTime() - time.getTime()) <= limit;
    }

    @Override
    public boolean accessCheck(String token) {
        try {
            DecodedJWT decodedJWT =  JWT.require(Algorithm.HMAC256("secret"))
                    .build()
                    .verify(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (TokenExpiredException e) {
            return false;
        }
    }

    @Override
    public boolean refreshCheck(String token) {
        try {
            DecodedJWT decodedJWT =  JWT.require(Algorithm.HMAC256("secret"))
                    .build()
                    .verify(token);
//            Token check_token = tokensRepository.findByRefreshToken(token)
//                    .orElseThrow(() -> new UsernameNotFoundException(decodedJWT.getSubject()));
            User user = usersRepository.findById(Long.parseLong(decodedJWT.getSubject()))
                    .orElseThrow(IllegalArgumentException::new);
            if (!redisUsersService.checkToken(token, user)) {
                return false;
            }
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (TokenExpiredException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public List<String> generateTokens(User user) {
        Token refresh_token = tokensRepository.findByUser_Id(user.getId()).orElseThrow(() -> new UsernameNotFoundException(user.getId().toString()));
        TokensUtil tokensUtil = new TokensUtil(user);
        String new_refresh_token = tokensUtil.getRefreshToken();
        tokensRepository.deleteByRefreshToken(refresh_token.getRefreshToken());
        Token new_token = new Token(new_refresh_token, new Date(), user);
        tokensRepository.save(new_token);

        redisUsersService.addTokenToUser(user, new_refresh_token);

        List<String> list = new ArrayList<>();
        list.add(tokensUtil.getAccessToken());
        list.add(tokensUtil.getRefreshToken());
        return list;
    }

}
