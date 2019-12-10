package kr.jhkim.springblog.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthUser extends User {
  private static final long serialVersionUID = 1L;

  private Member member;

  public AuthUser(String username, String password, boolean enabled, boolean accountNonExpired,
      boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

  }

  public AuthUser(Member member, Collection<? extends GrantedAuthority> authorities) {
    super(member.getEmail(), member.getPassword(), true, true, true, true, authorities);
    this.member = member;
  }

}