package kr.jhkim.springblog.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "sb_uploadfile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

  @Id
  @GeneratedValue
  Long id;

  @Column
  String fileName;

  @Column
  String saveFileName;

  @Column
  String filePath;

  @Column
  String contentType;

  @Column
  long size;

  Date regDate;

  @Builder
  public UploadFile(Long id, String fileName, String saveFileName, String contentType, String filePath, long size,
      Date regDate) {
    this.id = id;
    this.fileName = fileName;
    this.saveFileName = saveFileName;
    this.contentType = contentType;
    this.filePath = filePath;
    this.size = size;
    this.regDate = regDate;
  }
}