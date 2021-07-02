package jp.asano.todo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.asano.todo.dto.ToDoForm;
import jp.asano.todo.entity.ToDo;
import jp.asano.todo.repository.ToDoRepository;
import jp.asano.todo.exception.ToDoAppException;

@Service
public class ToDoService {
    @Autowired
    ToDoRepository tRepo;
    /**
     * メンバーmidが新しくToDoを作成する (C)
     * @param mid
     * @param form
     * @return
     */
    public ToDo createToDo(String mid, ToDoForm form) {
        //formをEntityに
        ToDo t = form.toEntity();
        //各値のセット
        t.setMid(mid);
        t.setDone(false);
        t.setCreatedAt(new Date());
        
        //登録
        return tRepo.save(t);
    }

    /**
     * 番号を指定してToDoを取得 (R)
     * @param seq
     * @return
     */
    public ToDo getToDo(Long seq){
        //seqによる検索
        ToDo t = tRepo.findById(seq).orElseThrow(
          () -> new ToDoAppException(ToDoAppException.NO_SUCH_TODO_EXISTS, seq + ": No such todo exist"));

        return t;
    }

    /**
     * midのToDoリストを取得(R)
     * @param mid
     * @return
     */
    public List<ToDo> getToDoList(String mid){
        //mid,done=falseによる検索
        List<ToDo> t = tRepo.findByMidAndDone(mid,false);

        return t;
    }

    /**
     *  midのDoneリストを取得 (R)
     * @param mid
     * @return
     */
    public List<ToDo> getDoneList(String mid){
        //mid,done=trueによる検索
        List<ToDo> t = tRepo.findByMidAndDone(mid, true);
       
        return t;
    }

    /**
     * 全員のToDoリストを取得 (R)
     * @return
     */
    public List<ToDo> getToDoList() {
        //done=falseによる検索
        List<ToDo> t = tRepo.findByDone(false);

        return t;
    }

    /**
     * 全員のDoneリストを取得 (R)
     * @return
     */
    public List<ToDo> getDoneList(){
        //done=trueによる検索
        List<ToDo> t = tRepo.findByDone(true);

        return t;
    }

    /**
     * ToDoの完了
     * @param todo
     * @return
     */
    public ToDo done(String mid, Long seq){
        //ToDoの検索
        ToDo todo = this.getToDo(seq);
        if(todo.getMid().equals(mid)){
            //Done，DoneAtの更新
            todo.setDone(true);
            todo.setDoneAt(new Date());
        }
        return tRepo.save(todo);
    }

    /**
     * ToDoの更新 (U)
     * @param mid
     * @param seq
     * @param form
     * @return
     */
    public ToDo updateToDo(String mid, Long seq, ToDoForm form){
        ToDo todo = this.getToDo(seq);
        todo.setMid(mid);
        todo.setTitle(form.toEntity().getTitle());
        return tRepo.save(todo);
    }

    /**
     * ToDoの削除 (D)
     * @param mid
     * @param seq
     */
    public void deleteToDo(String mid, Long seq){
        ToDo todo = getToDo(seq);
        if(todo.getMid().equals(mid)){
            tRepo.delete(todo);
        }
    }
}
