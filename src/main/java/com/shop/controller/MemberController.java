package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	public MemberController(MemberService memberService,
	                        PasswordEncoder passwordEncoder) {
		this.memberService = memberService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/new")
	public String memberForm(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}
	
	@PostMapping("/new")
	public String memberForm(MemberFormDto memberFormDto) {
		Member createMember = Member.createMember(memberFormDto, passwordEncoder);
		memberService.saveMember(createMember);
		return "redirect:/";
	}
}
