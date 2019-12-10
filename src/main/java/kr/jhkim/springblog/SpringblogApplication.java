package kr.jhkim.springblog;

import java.util.Date;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import kr.jhkim.springblog.domain.Member;
import kr.jhkim.springblog.domain.Post;
import kr.jhkim.springblog.domain.Tag;
import kr.jhkim.springblog.repository.MemberRepository;
import kr.jhkim.springblog.repository.PostRepository;
import kr.jhkim.springblog.service.TagService;

@SpringBootApplication
public class SpringblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringblogApplication.class, args);
	}

	/**
	 * 154 개의 사용자와 게시물을 생성합니다. 페이지네이션, 태그 기능 작동 테스트를 위한 메소드입니다.
	 * 
	 * @param userRepository
	 * @param postRepository
	 * @return
	 */
	// @Bean
	public CommandLineRunner initData(MemberRepository memberRepository, PostRepository postRepository,
			TagService tagService) {
		Tag tag1 = Tag.builder().name("플러터").useCount(31).memberId(1L).build();
		tagService.saveTag(tag1);
		Tag tag2 = Tag.builder().name("리엑트").useCount(31).memberId(1L).build();
		tagService.saveTag(tag2);
		return args -> IntStream.rangeClosed(1, 154).forEach(i -> {
			Member member = Member.builder().email("code" + i + "@gmail.com").password("password").nickName("nickName")
					.build();
			memberRepository.save(member);

			Post post = Post.builder().title("title").content("content").memberEmail("code" + i + "@gmail.com")
					.tag((i % 2 == 0) ? "플러터" : "리엑트").memberId(Long.parseLong(i + "")).createDate(new Date()).build();
			postRepository.save(post);

		});
	}

}
