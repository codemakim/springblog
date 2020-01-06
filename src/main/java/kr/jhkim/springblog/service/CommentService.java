package kr.jhkim.springblog.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.jhkim.springblog.domain.Comment;
import kr.jhkim.springblog.domain.Post;
import kr.jhkim.springblog.repository.CommentRepository;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {
  private CommentRepository commentRepository;
  private PostService postService;

  /**
   * 전달받은 코멘트 ID에 해당하는 코멘트 정보를 찾아 반환합니다.
   * 
   * @param commentId
   * @return
   */
  public Comment getComment(Long commentId) {
    return commentRepository.getOne(commentId);
  }

  /**
   * 전달받은 코멘트 정보를 DB에 저장합니다. 저장에 성공하면 코멘트의 ID를 반환합니다.
   * 
   * @param comment
   * @return
   */
  public Long saveComment(Comment comment) {
    try {
      comment.setDisplay(true);
      comment.setChildCount(0);
      comment.setCreateDate(new Date());
      Long commentId = commentRepository.save(comment).getId();
      Post post = postService.getPost(comment.getPostId());
      post.setCommentCount(post.getCommentCount() + 1);
      return commentId;
    } catch (Exception e) {
      e.printStackTrace();
      return 0L;
    }
  }

  /**
   * 포스트 ID를 넘겨받아 depth 값이 1이며, display가 true 인 해당 포스트의 댓글 목록을 반환합니다.
   * 
   * @param postId
   * @return
   */
  @Transactional(readOnly = true)
  public List<Comment> getCommentListByPost(Long postId) {
    return commentRepository.findByPostIdAndDepthOrderByIdAsc(postId, 1);
  }

  /**
   * 전달받은 코멘트 ID에 해당하는 코멘트를 삭제합니다.
   * 
   * @param commentId
   * @return
   */
  public Long deleteComment(Long commentId) {
    try {
      Comment comment = getComment(commentId);
      comment.setDisplay(false);
      return commentRepository.save(comment).getId();
    } catch (Exception e) {
      e.printStackTrace();
      return 0L;
    }
  }
}