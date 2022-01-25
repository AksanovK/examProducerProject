package exam.repositories;

import exam.models.TypeOfStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeOfStatementsRepository extends JpaRepository<TypeOfStatement, Long> {
}
