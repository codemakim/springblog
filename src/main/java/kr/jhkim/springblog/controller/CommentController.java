package kr.jhkim.springblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/comment")
public class CommentController {

  /**
   * 전달받은 코멘트의 ID를 이용해 해당하는 코멘트를 삭제합니다.
   * 
   * @param commentId
   * @return
   */
  @GetMapping(value = "/delete/{commentId}")
  public String deleteComment(@PathVariable(value = "commentId") Long commentId) {
    // TODO: process GET request
    return "comment/comment_list";
  }

  /**
   * 전달받은 코멘트의 ID를 이용해 해당하는 코멘트를 업데이트합니다.
   * 
   * @param commentId
   * @return
   */
  @PostMapping(value = "/update/{commentId}")
  public String updateComment(@PathVariable(value = "commentId") Long commentId) {
    // TODO: process POST request
    return "comment/comment_list";
  }

}