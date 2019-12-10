package kr.jhkim.springblog.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.jhkim.springblog.domain.AuthUser;
import kr.jhkim.springblog.domain.Tag;
import kr.jhkim.springblog.service.TagService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/tag")
public class TagController {
  private TagService tagService;

  /**
   * 태그 관리 페이지를 반환하는 컨트롤러입니다.
   * 
   * @param authUser
   * @param model
   * @return
   */
  @GetMapping(value = "")
  public String managePage(@AuthenticationPrincipal AuthUser authUser, Model model) {
    List<Tag> tagList = tagService.getTagList(authUser.getMember().getId());
    model.addAttribute(tagList);
    return "tag/manage";
  }

  /**
   * 태그를 추가하기 위한 컨트롤러입니다.
   * 
   * @param tag
   * @return
   */
  @PostMapping(value = "/insert")
  public String insertTag(Tag tag) {
    tagService.saveTag(tag);
    return "redirect:/tag";
  }

  /**
   * 태그를 수정하기 위한 컨트롤러입니다.
   * 
   * @param tag
   * @return
   */
  @PostMapping(value = "/update")
  public String updateTag(Tag tag) {
    tagService.updateTag(tag);
    return "redirect:/tag";
  }

  /**
   * 태그를 삭제하기 위한 엔드포인트입니다.
   * 
   * @param id
   * @return
   */
  @ResponseBody
  @GetMapping(value = "/delete/{id}")
  public int deleteTag(@PathVariable Long id) {
    return tagService.deleteTag(id);
  }

}