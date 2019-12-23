package kr.jhkim.springblog.domain;

import java.util.Date;

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

@Getter
@Setter
@Entity
@ToString
@Table(name = "sb_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "s_name", nullable = false)
  private String name;

  @Column(name = "s_password", nullable = false)
  private String password;

  @Column(name = "s_content", nullable = false)
  private String content;

  @Column(name = "b_display", nullable = false)
  private boolean display;

  @Column(name = "b_ip", nullable = false)
  private String ip;

  @Column(name = "n_depth", nullable = false)
  private int depth;

  @Column(name = "d_createdate", nullable = false)
  private Date createDate;

  @Builder
  public Comment(Long id, String name, String password, String content, boolean display, String ip, int depth,
      Date createDate) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.content = content;
    this.display = display;
    this.ip = ip;
    this.depth = depth;
    this.createDate = createDate;
  }
}