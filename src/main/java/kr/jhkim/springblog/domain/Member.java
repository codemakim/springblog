package kr.jhkim.springblog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "sb_member")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 30, nullable = false, name = "s_email")
  private String email;

  @Column(length = 16, nullable = false, name = "s_nickname")
  private String nickName;

  @Column(length = 100, nullable = false, name = "s_password")
  private String password;

  @Column(nullable = true, name = "s_role")
  private String role;

  @Builder
  public Member(Long id, String email, String nickName, String password, String role) {
    this.id = id;
    this.email = email;
    this.nickName = nickName;
    this.password = password;
    this.role = role;
  }
}