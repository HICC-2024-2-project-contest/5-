package hello.core.member.controller;

import hello.core.member.dto.MemberDTO;
import hello.core.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // ✅ 회원가입 폼
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    // ✅ 회원가입 처리
    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.save");
        System.out.println("memberDTO= " + memberDTO);
        memberService.save(memberDTO);
        return "redirect:/member/login"; // ✅ 회원가입 후 로그인 페이지로 이동
    }

    // ✅ 로그인 폼
    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "redirect:/mainpage";
        } else return "login";
    }
    // ✅ 전체 회원 리스트 조회
    @GetMapping("/member/list")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    @GetMapping("/category")
    public String category() {
        return "category";
    }
}