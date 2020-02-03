package lt.visma.starter.repository.impl;

import lt.visma.starter.model.TodoTask;
import lt.visma.starter.repository.TodoTaskRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryTodoTaskRepository implements TodoTaskRepository {
    private List<TodoTask> tasks;

    public InMemoryTodoTaskRepository() {
        tasks = new ArrayList<>();
    }

    @Override
    public List<TodoTask> findAll() {
        return tasks;
    }

    @Override
    public TodoTask findOne(int id) {
        TodoTask result;
        try {
            result = tasks.get(id);
        }
        catch(IndexOutOfBoundsException exc) {
            result =  null;
        }
        return result;
    }

    @Override
    public TodoTask save(TodoTask todoTask) {
        todoTask.setId(tasks.size());
        tasks.add(todoTask);
        return todoTask;
    }
}
