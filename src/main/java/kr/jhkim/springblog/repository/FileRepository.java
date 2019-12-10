package kr.jhkim.springblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jhkim.springblog.domain.UploadFile;

public interface FileRepository extends JpaRepository<UploadFile, Long> {
  public UploadFile findOneByFileName(String fileName);

  public UploadFile findOneById(Long id);

}