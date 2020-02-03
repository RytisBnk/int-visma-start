package lt.visma.starter.controller;

import lt.visma.starter.model.TodoTask;
import lt.visma.starter.service.TodoTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tasks")
public class TodoTaskController {
    private TodoTaskService todoTaskService;

    @Autowired
    public TodoTaskController(TodoTaskService todoTaskService) {
        this.todoTaskService = todoTaskService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TodoTask> getTasks() {
        return todoTaskService.getAllTasks();
    }

    @RequestMapping(method = RequestMethod.POST)
    TodoTask saveTask(@RequestBody TodoTask todoTask) {
        return todoTaskService.saveTask(todoTask);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    TodoTask getById(@PathVariable int id) {
        return todoTaskService.getOne(id);
    }
}
