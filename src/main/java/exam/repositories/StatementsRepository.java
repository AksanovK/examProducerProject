package exam.repositories;

import exam.models.Statements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatementsRepository extends JpaRepository<Statements, Long> {
}
