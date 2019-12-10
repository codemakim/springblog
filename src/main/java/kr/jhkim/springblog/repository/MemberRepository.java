package kr.jhkim.springblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jhkim.springblog.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByEmail(String email);
}