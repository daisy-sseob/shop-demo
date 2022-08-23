package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService implements UserDetailsService {

	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Member> findMember = memberRepository.findByEmail(email);

		findMember.orElseThrow(() -> {
			throw new UsernameNotFoundException(email);
		});
		
		Member member = findMember.get();

		return User.builder()
				.username(member.getName())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
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
