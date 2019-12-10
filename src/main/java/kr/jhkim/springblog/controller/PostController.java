package kr.jhkim.springblog.controller;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.jhkim.springblog.domain.AuthUser;
import kr.jhkim.springblog.domain.Post;
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

  protected final Logger logger = LoggerFactory.getLogger(PostController.class);

  /**
   * 모든 포스트 목록을 반환합니다.
   * 
   * @param model
   * @return
   */
  @GetMapping
  public String list(@PageableDefault Pageable pageable, Model model) {
    model.addAttribute("postList", postService.getPostPage(pageable));
    model.addAttribute("tagList", tagService.getTagListAll());
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
   * 포스트 정보를 전달받아 테이블에 저장하고, 메인 화면으로 페이지를 리다이렉트하는 컨트롤러입니다.
   * 
   * @param post
   * @return
   */
  @PostMapping(value = "/write")
  public String postWrite(@ModelAttribute Post post) {
    logger.info("written post: " + post.toString());
    postService.savePost(post.setNowDate());
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
    model.addAttribute(post);
    model.addAttribute("hotTagList", tagService.getHotTagList());
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
    model.addAttribute("hotTagList", tagService.getHotTagList());
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

}