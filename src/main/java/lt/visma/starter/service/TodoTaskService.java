package lt.visma.starter.service;

import lt.visma.starter.model.TodoTask;

import java.util.List;

public interface TodoTaskService {
    List<TodoTask> getAllTasks();
    TodoTask getOne(int id);
    TodoTask saveTask(TodoTask todoTask);
}
