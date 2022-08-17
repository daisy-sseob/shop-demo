package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.print.attribute.standard.PrinterURI;

@Entity
@Getter @Setter
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String name;

	@Column(unique = true)
	private String email;

	private String password;

	private String address;

	@Enumerated(EnumType.STRING)
	private Role role;

	/*
		Member 생성 util 메소드
	 */
	public static Member createMember(MemberFormDto memberFormDto,
	                                  PasswordEncoder passwordEncoder) {

		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setAddress(memberFormDto.getAddress());
		member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
		member.setRole(Role.USER);
		
		return member;
	}
}
