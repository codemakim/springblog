package kr.jhkim.springblog.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.jhkim.springblog.domain.AuthUser;
import kr.jhkim.springblog.domain.Member;
import kr.jhkim.springblog.service.MemberService;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping(value = "member")
@AllArgsConstructor
public class MemberController {
  private MemberService memberService;

  @GetMapping(value = "signup")
  public String signupPage() {
    return "member/signup";
  }

  @PostMapping(value = "signup")
  public String signup(Member member) {
    if (memberService.checkEmail(member.getEmail())) {
      memberService.joinMember(member);
      return "redirect:/member/login";
    } else {
      return "redirect:/member/signup";
    }
  }

  @GetMapping(value = "/login")
  public String loginPage() {
    return "member/login";
  }

  @GetMapping("/login/result")
  public String loginResultPage() {
    return "member/login-success";
  }

  @GetMapping("/logout/result")
  public String logoutPage() {
    return "member/logout";
  }

  @GetMapping(value = "denied")
  public String deniedPage() {
    return "member/denied";
  }

  @GetMapping(value = "myinfo")
  public String myinfoPage(@AuthenticationPrincipal AuthUser authUser, Model model) {
    model.addAttribute("member", authUser.getMember());
    return "member/myinfo";
  }

  @GetMapping(value = "admin")
  public String getMethodName() {
    return "member/admin";
  }

}