package com.camerinfolks.todo.service.impl;

import com.camerinfolks.todo.dto.TodoDto;
import com.camerinfolks.todo.entity.Todo;
import com.camerinfolks.todo.exception.ResourceNotFoundException;
import com.camerinfolks.todo.repository.TodoRepository;
import com.camerinfolks.todo.service.TodoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;

    private ModelMapper modelMapper;

    @Override
    public TodoDto addTodo(TodoDto todoDto) {
//        Convert TodoDto into Todo Jpa entity
  Todo todo = modelMapper.map(todoDto,Todo.class);

//        Todo Jpa entity
       Todo savedTodo = todoRepository.save(todo);
//         Convert saved Todo Jpa entity object into TodoDto object

    TodoDto savedTodoDto = modelMapper.map(savedTodo,TodoDto.class);

        return savedTodoDto;
    }

    @Override
    public TodoDto getTodo(Long id) {
        Todo todo = todoRepository.findById(id).
                orElseThrow(() -> new
                        ResourceNotFoundException("Todo Not found with id: "+ id));

        return modelMapper.map(todo,TodoDto.class);
    }

    @Override
    public List<TodoDto> getAllTodo() {

        List<Todo> todos = todoRepository.findAll();
        return todos.stream().map((todo -> modelMapper.map(todo,TodoDto.class)))
                .collect(Collectors.toList());
    }

    @Override
    public TodoDto updateTodo(TodoDto todoDto, Long id) {
         Todo todo = todoRepository.findById(id)
                 .orElseThrow(() ->
                         new ResourceNotFoundException("Todo not found with id: " +id));
         todo.setTitle(todoDto.getTitle());
         todo.setCompleted(todo.isCompleted());
         todo.setDescription(todo.getDescription());

         Todo updatedTodo  =  todoRepository.save(todo);
        return modelMapper.map(updatedTodo,TodoDto.class);
    }

    @Override
    public void deleteTodo(Long id) {
      Todo todo =  todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not found with id: " + id));

      todoRepository.deleteById(id);

    }

    @Override
    public TodoDto completeTodo(Long id) {

        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not found with id: " +id));

        todo.setCompleted(Boolean.TRUE);

        Todo updatedTOdo = todoRepository.save(todo);

        return modelMapper.map(updatedTOdo, TodoDto.class);
    }

    @Override
    public TodoDto inCompleteTodo(Long id) {

        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not found with id: " +id));

        todo.setCompleted(Boolean.FALSE);

        Todo updatedTodo = todoRepository.save(todo);

        return modelMapper.map(updatedTodo,TodoDto.class);
    }
}
