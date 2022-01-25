package exam.services;

public interface JwtBlackListService {
    void add(String token);

    boolean exists(String token);

    void deleteToken(String header);
}
