package hello.core.member.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/mainpage")
    public String mainPage(HttpSession session, Model model) {
        return "mainpage";  // ✅ 로그인된 경우 mainpage.html 렌더링
    }
}