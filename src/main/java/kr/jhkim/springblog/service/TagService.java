package kr.jhkim.springblog.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.jhkim.springblog.domain.Post;
import kr.jhkim.springblog.domain.Tag;
import kr.jhkim.springblog.repository.PostRepository;
import kr.jhkim.springblog.repository.TagRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {
  private TagRepository tagRepository;
  private PostRepository postRepository;

  protected final Logger logger = LoggerFactory.getLogger(TagService.class);

  /**
   * 전달받은 태그 정보를 테이블에 저장합니다.
   * 
   * @param tag
   * @return 저장한 태그의 ID 값을 반환합니다.
   */
  public Long saveTag(Tag tag) {
    return tagRepository.save(tag).getId();
  }

  /**
   * ID를 전달받아 그에 해당하는 태그 정보를 반환합니다.
   * 
   * @param id
   * @return Tag
   */
  public Tag getTag(Long id) {
    return tagRepository.findById(id).orElse(null);
  }

  /**
   * 태그 테이블 내 모든 태그 목록을 반환합니다.
   * 
   * @return List<Tag>
   */
  public List<Tag> getTagListAll() {
    logger.info("tagService.getTagListAll :: 모든 태그 몰고 반환 메소드 진입");
    return tagRepository.findAll();
  }

  /**
   * 가장 useCount가 높은 10개의 태그를 반환합니다.
   * 
   * @return
   */
  public List<Tag> getHotTagList() {
    return tagRepository.findTop10ByOrderByUseCountDesc();
  }

  /**
   * 멤버 ID를 전달받아 그에 해당하는 태그 목록을 반환합니다.
   * 
   * @param memberId
   * @return List<Tag>
   */
  public List<Tag> getTagList(Long memberId) {
    return tagRepository.findByMemberId(memberId);
  }

  /**
   * 태그 정보를 전달받아 해당 태그 정보를 업데이트합니다.
   * 
   * @param tag
   * @return 업데이트한 id 값을 반환합니다.
   */
  public Long updateTag(Tag tag) {
    Tag currentTag = getTag(tag.getId());
    currentTag.setName(tag.getName());
    return tagRepository.save(currentTag).getId();
  }

  /**
   * 전달받은 포스트의 Tag를 이용해 Tag 테이블의 정보를 갱신합니다. 이미 존재하는 태그의 경우, 해당 태그를 포함하고 있는 포스트 수를
   * selection한 결과를 useCount에 반영합니다. 처음 작성된 태그의 경우, 테이블에 정보를 추가하고 useCount를 1로
   * 지정합니다.
   * 
   * @param post
   */
  public void savePostTag(Post post) {
    String[] stringList = post.getTag().split(",");
    for (int i = 0; i < stringList.length; i++) {
      if (stringList[i].isEmpty()) {
        continue;
      }
      Tag tag = tagRepository.findByName(stringList[i]);
      if (tag != null) {
        tag.setUseCount(postRepository.countByTagContaining(stringList[i]));
        saveTag(tag);
      } else {
        Tag newTag = Tag.builder().memberId(post.getMemberId()).name(stringList[i]).useCount(1).build();
        saveTag(newTag);
      }
    }
  }

  /**
   * 전달받은 id값에 해당하는 태그를 제거합니다.
   * 
   * @param id
   * @return select: 1 / failure: 0
   */
  public int deleteTag(Long id) {
    int result = 0;
    try {
      Tag tag = getTag(id);
      if (tag.getUseCount() <= 0) {
        tagRepository.deleteById(id);
        result = 1;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
}