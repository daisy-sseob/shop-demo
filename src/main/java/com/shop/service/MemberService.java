package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public Member saveMember(Member member) {
		validateDuplicateMember(member);
		return memberRepository.save(member);
	}
	
	private void validateDuplicateMember(Member member) {
		Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
		findMember.ifPresent((thisMember) -> {
			throw new IllegalStateException(thisMember.getEmail() + " 해당 이메일로 이미 가입된 회원입니다.");
		});
	}
}
