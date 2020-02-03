package lt.visma.starter.repository;

import lt.visma.starter.model.TodoTask;

import java.util.List;

public interface TodoTaskRepository {
    List<TodoTask> findAll();
    TodoTask findOne(int id);
    TodoTask save(TodoTask todoTask);
}
