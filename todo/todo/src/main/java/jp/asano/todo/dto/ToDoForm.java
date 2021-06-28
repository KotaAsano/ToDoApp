package jp.asano.todo.dto;

import jp.asano.todo.entity.ToDo;
import lombok.Data;

@Data
public class ToDoForm {
    String title; //題目
    
    public ToDo toEntity(){
        ToDo t = new ToDo(null, title, null, false, null, null);
        
        return t;
    }
}
