package kr.jhkim.springblog.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@ToString(exclude = "parent")
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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "n_parentid", referencedColumnName = "id")
  private Comment parent;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
  private List<Comment> children = new ArrayList<Comment>();

  @Column(name = "n_depth", nullable = false)
  private int depth;

  @Column(name = "n_childcount")
  private int childCount = 0;

  @Column(name = "d_createdate", nullable = false)
  private Date createDate;

  @Column(name = "n_postid", nullable = false)
  private Long postId;

  @Builder
  public Comment(Long id, String name, String password, String content, boolean display, String ip, Comment parent,
      int depth, int childCount, Date createDate, Long postId) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.content = content;
    this.display = display;
    this.ip = ip;
    this.parent = parent;
    this.depth = depth;
    this.childCount = childCount;
    this.createDate = createDate;
    this.postId = postId;
  }
}