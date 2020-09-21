package org.javaboy.vhr.controller;

import org.javaboy.vhr.config.VerificationCode;
import org.javaboy.vhr.model.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录验证相关
 */
@RestController
public class LoginController {
    @GetMapping("/login")
    public RespBean login() {
        return RespBean.error("尚未登录，请登录!");
    }

    /**
     * 验证码
     *
     * @param request
     * @param resp
     * @throws IOException
     */
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verify_code", text);
        VerificationCode.output(image, resp.getOutputStream());
    }
}
