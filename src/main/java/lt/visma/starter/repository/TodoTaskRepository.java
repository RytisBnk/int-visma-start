package lt.visma.starter.repository;

import lt.visma.starter.model.TodoTask;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface TodoTaskRepository extends JpaRepository<TodoTask, Integer> {

}
