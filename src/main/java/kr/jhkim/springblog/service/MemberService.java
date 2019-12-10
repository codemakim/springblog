package kr.jhkim.springblog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.jhkim.springblog.domain.AuthUser;
import kr.jhkim.springblog.domain.Member;
import kr.jhkim.springblog.repository.MemberRepository;
import kr.jhkim.springblog.utils.Role;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class MemberService implements UserDetailsService {
  protected final Logger logger = LoggerFactory.getLogger(MemberService.class);

  private MemberRepository memberRepository;

  public Long joinMember(Member member) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    member.setPassword(passwordEncoder.encode(member.getPassword()));
    return memberRepository.save(member).getId();
  }

  public boolean checkEmail(String email) {
    return memberRepository.findByEmail(email).isEmpty() ? true : false;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<Member> memberWrapper = memberRepository.findByEmail(email);
    Member member = memberWrapper.get();

    logger.info("::: member info: " + member.toString());

    List<GrantedAuthority> authorities = new ArrayList<>();

    // if (member.getEmail().equals("codemakim@gmail.com"))
    if (member.getRole() != null && member.getRole().contains("ADMIN"))
      authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
    else
      authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));

    logger.info("::: authorities: " + authorities);
    // return new User(member.getEmail(), member.getPassword(), authorities);
    return new AuthUser(member, authorities);
  }
}