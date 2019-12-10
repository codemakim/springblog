package kr.jhkim.springblog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.jhkim.springblog.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findByTagContaining(String tag, Pageable pageable);

  List<Post> findAllByOrderByIdDesc(Pageable pageable);

  int countByTagContaining(String tag);
}