package kr.jhkim.springblog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.jhkim.springblog.domain.AuthUser;
import kr.jhkim.springblog.domain.Comment;
import kr.jhkim.springblog.domain.Post;
import kr.jhkim.springblog.domain.Tag;
import kr.jhkim.springblog.service.CommentService;
import kr.jhkim.springblog.service.PostService;
import kr.jhkim.springblog.service.TagService;
import lombok.AllArgsConstructor;

@Controller
@Transactional
@RequestMapping(value = "/post")
@AllArgsConstructor
public class PostController {
  private PostService postService;
  private TagService tagService;
  private CommentService commentService;

  protected final Logger logger = LoggerFactory.getLogger(PostController.class);

  @ModelAttribute("tagList")
  public List<Tag> getTagList() {
    return tagService.getTagListAll();
  }

  /**
   * 모든 포스트 목록을 반환합니다.
   * 
   * @param model
   * @return
   */
  @GetMapping
  public String list(@PageableDefault Pageable pageable, Model model) {
    model.addAttribute("postList", postService.getPostPage(pageable));
    return "post/list";
  }

  /**
   * 포스트 작성 페이지를 반환합니다.
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/write")
  public String getWritePage(Model model) {
    model.addAttribute("hotTagList", tagService.getHotTagList());
    return "post/write";
  }

  /**
   * 포스트 정보를 전달받아 데이터베이스에 저장하고, 메인 화면으로 페이지를 리다이렉트하는 컨트롤러입니다.
   * 
   * @param post
   * @return
   */
  @PostMapping(value = "/write")
  public String postWrite(@ModelAttribute Post post) {
    logger.info("written post: " + post.toString());
    postService.savePost(post);
    tagService.savePostTag(post);
    return "redirect:/";
  }

  /**
   * 포스트 ID를 전달받아 포스트 상세보기 페이지를 반환하는 컨트롤러입니다.
   * 
   * @param id
   * @param model
   * @return
   */
  @GetMapping(value = "/{id}")
  public String view(@PathVariable(value = "id") Long id, Model model) {
    Post post = postService.getPost(id);
    if (post == null)
      return "error_404";
    List<Comment> commentList = commentService.getCommentListByPost(id);

    model.addAttribute(post);
    model.addAttribute(commentList);
    return "post/view";
  }

  /**
   * 포스트 ID를 전달받아 해당하는 포스트의 수정 페이지를 반환하는 컨트롤러입니다.
   * 
   * @param id
   * @param model
   */
  @GetMapping(value = "/update/{id}")
  public String getUpdatePage(@PathVariable Long id, Model model) {
    Post post = postService.getPost(id);
    model.addAttribute(post);
    return "post/update";
  }

  /**
   * 전달받은 포스트 정보를 업데이트하고, 업데이트된 포스트 상세보기 페이지로 리다이렉트하는 컨트롤러입니다.
   * 
   * @param post
   */
  @PostMapping(value = "/update/{id}")
  public String postUpdate(Post post) {
    logger.info("포스트: " + post.toString());
    Long id = postService.updatePost(post);
    tagService.savePostTag(post);
    return "redirect:/post/" + id;
  }

  /**
   * 전달받은 포스트 ID에 해당하는 포스트를 제거합니다. 해당하는 포스트가 없거나, 로그인 한 회원이 없으면 포스트는 삭제되지 않습니다.
   * 
   * @param id
   * @param authUser
   */
  @ResponseBody
  @GetMapping(value = "/delete/{id}")
  public int postDelete(@PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
    Post post = postService.getPost(id);
    int result = 0;
    if (post != null && post.getMemberId() == authUser.getMember().getId()) {
      result = postService.deletePost(id);
    }
    return result;
  }

  /**
   * 코멘트 정보를 전달받아 데이터베이스에 저장합니다. 성공적으로 저장하면, 코멘트 목록을 반환합니다.
   * 
   * @param postId
   * @param comment
   * @param request
   * @return
   */
  @PostMapping(value = "{postId}/comment")
  public String writeComment(@PathVariable(value = "postId") Long postId, Comment comment, Model model,
      HttpServletRequest request) {
    comment.setPostId(postId);
    comment.setIp(request.getRemoteAddr());
    commentService.saveComment(comment);
    List<Comment> commentList = commentService.getCommentListByPost(postId);
    model.addAttribute(commentList);
    return "redirect:/post/" + postId;
  }

  /**
   * 포스트 ID를 넘겨받아 해당 포스트의 코멘트 목록을 반환합니다.
   * 
   * @param postId
   * @return
   */
  @GetMapping(value = "{postId}/comment")
  public String getCommentList(@PathVariable(value = "postId") Long postId, Model model) {
    List<Comment> commentList = commentService.getCommentListByPost(postId);
    model.addAttribute(commentList);
    return "comment/comment_list";
  }

  /**
   * 코멘트 ID와 비밀번호를 입력받습니다. 입력받은 ID의 코멘트 정보와 비밀번호가 일치하면, 코멘트를 삭제합니다.
   * 
   * @param postId
   * @param commentId
   * @param pwd
   * @return
   */
  @ResponseBody
  @GetMapping(value = "{postId}/comment/{commentId}/delete")
  public ResponseEntity<?> deleteComment(@PathVariable(value = "postId") Long postId,
      @PathVariable(value = "commentId") Long commentId, @RequestParam(value = "pwd") String pwd) {

    try {
      Comment comment = commentService.getComment(commentId);
      if (pwd == comment.getPassword()) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
      } else {
        return ResponseEntity.badRequest().build();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

}