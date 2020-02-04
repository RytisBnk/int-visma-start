package lt.visma.starter.service.impl;

import lt.visma.starter.model.TodoTask;
import lt.visma.starter.repository.TodoTaskRepository;
import lt.visma.starter.service.TodoTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoTaskServiceImpl implements TodoTaskService {
    private TodoTaskRepository todoTaskRepository;

    @Autowired
    public TodoTaskServiceImpl(TodoTaskRepository todoTaskRepository) {
        this.todoTaskRepository = todoTaskRepository;
    }

    @Override
    public List<TodoTask> getAllTasks() {
        return todoTaskRepository.findAll();
    }

    @Override
    public TodoTask getOne(int id) {
        Optional<TodoTask> todoTask = todoTaskRepository.findById(id);
        return todoTask.orElse(null);
    }

    @Override
    public TodoTask saveTask(TodoTask todoTask) {
        return todoTaskRepository.save(todoTask);
    }
}
