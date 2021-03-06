package jp.asano.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.asano.todo.entity.Member;
import jp.asano.todo.dto.ToDoForm;
import jp.asano.todo.dto.MemberForm;
import jp.asano.todo.entity.ToDo;
import jp.asano.todo.service.MemberService;
import jp.asano.todo.service.ToDoService;

@Controller
@RequestMapping("/")
public class ToDoController {
    @Autowired
    ToDoService tService;
    @Autowired
    MemberService mService;

    /**
     * ログインページを表示 HTTP-GET /
     * @param model
     * @return
     */
    @GetMapping("/") 
    String showLoginForm(@ModelAttribute MemberForm form, Model model){
        //MemberID オブジェクトの生成，modelに格納
        String mid = new String();
        model.addAttribute("MemberID", mid);
        model.addAttribute("MemberForm",form);
        //index.htmlの表示
        return "index";
    }
    
    /**
     * ユーザのログイン処理 -> ToDoリストページ HTTP-POST /login
     * @param model
     * @param redirectAttrs
     * @return
     */ 
    @PostMapping("/login")
    String checkloginForm(@Validated @ModelAttribute(name = "MemberForm") MemberForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs ){
        // 入力チェックに引っかかった場合、ログイン画面に戻る
        if (bindingResult.hasFieldErrors("mid")) {
            // GETリクエスト用のメソッドを呼び出して、ログイン画面に戻る
            return showLoginForm(form, model);
        } 

        //formの更新
        model.addAttribute("MemberForm", form);
        //IDチェック
        mService.getMember(form.getMid());
        model.addAttribute("MemberID", form.getMid());
        //リダイレクト用
        redirectAttrs.addAttribute("mid",form.getMid());
        // /{mid}/todosへリダイレクト
        return "redirect:/{mid}/list";
    }

    /**
     * ユーザーのリスト表示 HTTP-GET /{mid}/list
     * @param mid
     * @param model
     * @return
     */
    @GetMapping("/{mid}/list")
    String showUserToDos(@PathVariable String mid, @ModelAttribute ToDoForm form ,Model model) {
        //ユーザのToDo，Doneリストを取得，modelに格納
        List<ToDo> todos = tService.getToDoList(mid);
        model.addAttribute("ToDoList", todos);
        List<ToDo> dones = tService.getDoneList(mid);
        model.addAttribute("DoneList", dones);
        //空フォームの生成，modelに格納
        model.addAttribute("ToDoForm", form);
        //MemberIDの更新
        model.addAttribute("MemberID", mid);
        //MemberNameオブジェクトの作成
        Member m = mService.getMember(mid);
        model.addAttribute("MemberName", m.getName());

        //lists.htmlの表示
        return "list";
    }
    
    /**
     * メンバー全員のリストの表示 HTTP-GET /{mid}/alllist
     * @param mid
     * @param model
     * @return
     */
    @GetMapping("/{mid}/alllist")
    String showAllMemberToDos(@PathVariable String mid, Model model){
        //全ToDo，Doneリストの取得，オブジェクトに格納
        List<ToDo> allToDo = tService.getToDoList();
        List<ToDo> allDone = tService.getDoneList();
        model.addAttribute("AllToDoList", allToDo);
        model.addAttribute("AllDoneList", allDone);
        //MemberIDオブジェクト用
        model.addAttribute("MemberID", mid);

        //alllist.htmlの表示
        return "alllist";
    }

    /**
     * ToDoの追加 HTTP-POST /{mid}/add
     * @param form
     * @param mid
     * @param model
     * @return
     */
    @PostMapping("/{mid}/add")
    String addToDo(@Validated @ModelAttribute(name = "ToDoForm") ToDoForm form, BindingResult bindingResult, @PathVariable String mid, Model model){
        // 入力チェックに引っかかった場合、ToDo画面に戻る
        if (bindingResult.hasFieldErrors("title")) {
            // GETリクエスト用のメソッドを呼び出して、ToDo画面に戻る
            return showUserToDos(mid, form ,model);
        }
        //フォームの更新
        model.addAttribute("ToDoForm", form);
        //ToDoの生成
        tService.createToDo(mid, form);

        // /{mid}/listにリダイレクト
        return "redirect:/{mid}/list";
    }

    /**
     * ToDoの完了 HTTP-GET /{mid}/{seq}/done
     * @param mid
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/{mid}/{seq}/done")
    String doneToDo(@PathVariable String mid, @PathVariable Long seq, Model model){
        
        //todoの更新
        tService.done(mid,seq);

        // /{mid}/list へリダイレクト
        return "redirect:/{mid}/list";
    }

}


