package jp.asano.todo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jp.asano.todo.entity.ToDo;

@Repository
public interface ToDoRepository extends CrudRepository<ToDo, Long>{
    List<ToDo> findAll();
    //midとdoneの値でToDoを検索するメソッド
    List<ToDo> findByMidAndDone(String mid,boolean done);
    //doneの値でToDoを検索するメソッド
    List<ToDo> findByDone(boolean done);
    //midの値でToDoを検索するメソッド
    List<ToDo> findByMid(String mid);
}