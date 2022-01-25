package exam.repositories;

import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository {
    void save(String token);

    boolean exists(String token);
}
