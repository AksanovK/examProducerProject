package exam.services;

import exam.models.Token;
import exam.models.User;
import exam.models.dto.EmailPasswordDto;
import exam.models.dto.TokenDto;
import exam.redis.services.RedisUsersService;
import exam.repositories.TokensRepository;
import exam.repositories.UsersRepository;
import exam.security.token.TokensUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokensRepository tokensRepository;

    @Autowired
    private RedisUsersService redisUsersService;

    @Override
    public TokenDto login(EmailPasswordDto emailPassword) throws Throwable {
        System.out.println("DAWDAWDAWD");
        User user = usersRepository.findByEmail(emailPassword.getEmail())
                .orElseThrow((Supplier<Throwable>) () -> new UsernameNotFoundException("User not found"));
        if (passwordEncoder.matches(emailPassword.getPassword(), user.getHashPassword())) {
            TokensUtil tokensUtil = new TokensUtil(user);
            Token token = Token.builder()
                    .refreshToken(tokensUtil.getRefreshToken())
                    .user(user)
                    .tokensUtil(tokensUtil)
                    .time_of_creating(tokensUtil.getRefreshTime())
                    .build();
            tokensRepository.save(token);
            redisUsersService.addTokenToUser(user, tokensUtil.getRefreshToken());
            return TokenDto.builder()
                    .accessToken(tokensUtil.getAccessToken())
                    .refreshToken(tokensUtil.getRefreshToken())
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

}
