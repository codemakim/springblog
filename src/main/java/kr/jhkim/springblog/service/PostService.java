package kr.jhkim.springblog.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import kr.jhkim.springblog.domain.Post;
import kr.jhkim.springblog.repository.PostRepository;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PostService {
  private PostRepository postRepository;

  /**
   * 전달받은 포스트 정보를 테이블에 저장합니다.
   * 
   * @param post
   */
  public Long savePost(Post post) {
    return postRepository.save(post).getId();
  }

  /**
   * 모든 포스트 목록을 검색하여 반환합니다.
   * 
   * @return
   */
  public List<Post> getPostList() {
    return postRepository.findAll();
  }

  /**
   * 모든 포스트 목록을 ID를 기준으로 내림차순으로 정렬하여 반환합니다.
   * 
   * @return
   */
  public Page<Post> getPostPage(Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

    // return postRepository.findAllByOrderByIdDesc(pageable);
    return postRepository.findAll(pageable);
  }

  /**
   * 전달받은 태그가 포함되어 있는 포스트 목록을 검색하여, 내림차순으로 정렬해 반환합니다.
   * 
   * @param tag
   * @return
   */
  public Page<Post> getPostListByTagDesc(String tag, Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
    return postRepository.findByTagContaining(tag, pageable);
  }

  /**
   * 전달받은 ID값에 해당하는 포스트를 검색하여 반환합니다.
   * 
   * @param id
   * @return
   */
  public Post getPost(Long id) {
    return postRepository.findById(id).orElse(null);
  }

  /**
   * 전달받은 포스트 정보로 해당 포스트를 업데이트하고, 해당 포스트의 id를 반환합니다.
   * 
   * @param post
   * @return
   */
  public Long updatePost(Post post) {
    Post currentPost = getPost(post.getId());
    currentPost.setTitle(post.getTitle());
    currentPost.setContent(post.getContent());
    currentPost.setUpdateDate(new Date());
    return postRepository.save(currentPost).getId();
  }

  /**
   * id 값을 전달받아 해당 포스트를 테이블에서 삭제합니다.
   * 
   * @param id
   */
  public int deletePost(Long id) {
    try {
      postRepository.deleteById(id);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
    return 1;
  }
}