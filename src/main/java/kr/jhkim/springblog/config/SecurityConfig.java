package kr.jhkim.springblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import kr.jhkim.springblog.service.MemberService;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final MemberService memberService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(final WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
  }

  /**
   * TODO 현재 여러 블로그를 운영하는 기능은 넣지 않을 것이므로, 포스트 작성 삭제 등의 기능도 ADMIN 권한이 있어야 가능하도록 함.
   */

  @Override
  protected void configure(final HttpSecurity http) throws Exception {

    http.authorizeRequests()
        // 페이지 권한 설정
        .antMatchers("/admin/**", "/post/write", "/post/update/**", "/post/delete/**", "/category", "/category/**",
            "/image/delete/**")
        .hasRole("ADMIN").antMatchers("/member/myinfo", "/member/logout", "/image").hasAnyRole("MEMBER", "ADMIN")
        .antMatchers("/", "/**").permitAll().and()
        // 로그인 설정
        .formLogin().loginPage("/member/login").defaultSuccessUrl("/").permitAll().and()
        // 로그아웃 설정
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")).logoutSuccessUrl("/")
        .invalidateHttpSession(true).and()
        // 403 예외 처리 핸들링
        .exceptionHandling().accessDeniedPage("/member/denied");
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
  }
}