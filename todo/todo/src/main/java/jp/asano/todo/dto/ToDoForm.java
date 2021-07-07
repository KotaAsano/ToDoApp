package jp.asano.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import jp.asano.todo.entity.ToDo;
import lombok.Data;

@Data
public class ToDoForm {
    @NotBlank
    @Size(min = 1, max = 64)
    String title; //題目
    
    public ToDo toEntity(){
        ToDo t = new ToDo(null, title, null, false, null, null);
        
        return t;
    }
}
