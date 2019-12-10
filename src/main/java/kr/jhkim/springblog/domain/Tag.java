package kr.jhkim.springblog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import groovy.transform.ToString;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@ToString
@Table(name = "sb_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "s_name", nullable = false)
  private String name;

  @Column(name = "n_usecount", nullable = false)
  private int useCount;

  @Column(name = "n_memberid")
  private Long memberId;

  @Builder
  public Tag(Long id, String name, int useCount, Long memberId) {
    this.id = id;
    this.name = name;
    this.useCount = useCount;
    this.memberId = memberId;
  }
}