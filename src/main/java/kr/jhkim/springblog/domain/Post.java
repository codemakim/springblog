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
@Table(name = "sb_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "s_title", nullable = false)
  private String title;

  @Column(name = "s_preview", length = 200)
  private String preview;

  @Column(name = "s_content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "n_memberId", nullable = false)
  private Long memberId;

  @Column(name = "s_memberEmail", nullable = false)
  private String memberEmail;

  @Column(name = "d_createdate", nullable = false)
  private Date createDate;

  @Column(name = "d_updatedate")
  private Date updateDate;

  @Column(name = "s_tag")
  private String tag;

  @Column(name = "s_visible")
  private String visible;

  @Column(name = "n_commentcount")
  private int commentCount;

  @Builder
  public Post(Long id, String title, String preview, String content, Long memberId, String memberEmail, Date createDate,
      Date updateDate, String tag, int commentCount, String visible) {
    this.id = id;
    this.title = title;
    this.preview = preview;
    this.content = content;
    this.memberId = memberId;
    this.memberEmail = memberEmail;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.tag = tag;
    this.commentCount = commentCount;
    this.visible = visible;
  }

  public Post setNowDate() {
    this.createDate = new Date();
    return this;
  }
}