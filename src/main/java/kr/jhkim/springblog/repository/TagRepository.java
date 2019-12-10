package kr.jhkim.springblog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jhkim.springblog.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
  List<Tag> findByMemberId(Long memberId);

  Tag findByName(String name);

  List<Tag> findTop10ByOrderByUseCountDesc();
}