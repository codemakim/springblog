package kr.jhkim.springblog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jhkim.springblog.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  /**
   * 포스트 ID, Depth 정보를 넘겨받아, Display 값이 true인 댓글 목록을 반환합니다.
   * 
   * @param postId
   * @param depth
   * @return
   */
  List<Comment> findByPostIdAndDepthAndDisplayTrueOrderByIdAsc(Long postId, int depth);
}