package kr.jhkim.springblog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import kr.jhkim.springblog.domain.AuthUser;
import kr.jhkim.springblog.domain.Post;
import kr.jhkim.springblog.domain.Tag;
import kr.jhkim.springblog.service.PostService;
import kr.jhkim.springblog.service.TagService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MainController {
  private PostService postService;
  private TagService tagService;

  protected final Logger logger = LoggerFactory.getLogger(MainController.class);

  @ModelAttribute("tagList")
  public List<Tag> getTagList() {
    return tagService.getTagListAll();
  }

  @GetMapping(value = "/")
  public String getMethodName(@AuthenticationPrincipal AuthUser authUser, @PageableDefault Pageable pageable,
      @RequestParam(defaultValue = "") String tag, Model model) {

    Page<Post> postList = postService.getPostListByTagDesc(tag, pageable);

    model.addAttribute("postList", postList);
    model.addAttribute("selectTag", tag);

    logger.info("getTotalElements: " + postList.getTotalElements() + "  getTotalPage: " + postList.getTotalPages()
        + "  getNumber: " + postList.getNumber() + "  getNumberOfElement: " + postList.getNumberOfElements());

    return "post/list";
  }

}