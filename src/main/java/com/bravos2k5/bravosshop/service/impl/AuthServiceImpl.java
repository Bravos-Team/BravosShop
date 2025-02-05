package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.dto.RegisterDto;
import com.bravos2k5.bravosshop.dto.TokenInfo;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.service.*;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CookieService cookieService;
    private final RedisService redisService;
    private final EmailService emailService;

    @Autowired
    public AuthServiceImpl(JwtService jwtService, UserService userService,
                           BCryptPasswordEncoder passwordEncoder, CookieService cookieService, RedisService redisService, EmailService emailService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.cookieService = cookieService;
        this.redisService = redisService;
        this.emailService = emailService;
    }

    @Override
    public void sendVerifyEmailUrl(RegisterDto registerDto) {
        String registerToken = UUID.randomUUID().toString();
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        redisService.save(registerToken,registerDto,900, TimeUnit.SECONDS);
        String confirmUrl = "http://localhost:9898/p/register/verify?token=" + registerToken;
        String emailContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Xác nhận đăng ký</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background: #e2e0ef;
                            padding: 20px;
                            border-radius: 10px;
                            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
                        }
                        .header {
                            text-align: center;
                            padding: 10px 0;
                            font-size: 24px;
                            color: #333;
                        }
                        .content {
                            font-size: 16px;
                            color: #555;
                            line-height: 1.6;
                        }
                        .button {
                            display: block;
                            width: 200px;
                            text-align: center;
                            background: #007bff;
                            padding: 10px;
                            border-radius: 5px;
                            text-decoration: none;
                            font-weight: bold;
                            margin: 20px auto;
                        }
                        .footer {
                            text-align: center;
                            font-size: 12px;
                            color: #777;
                            margin-top: 20px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">Xác nhận đăng ký</div>
                        <div class="content">
                            <p>Chào <strong>{USERNAME}</strong>,</p>
                            <p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>Bravos Shop</strong>. Vui lòng nhấn vào nút bên dưới để xác nhận địa chỉ email của bạn:</p>
                            <a style="color: #ffffff" href="{VERIFICATION_LINK}" class="button">Xác nhận email</a>
                            <p>Link xác nhận có hiệu lực 15 phút. Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này.</p>
                            <p>Trân trọng,<br>Đội ngũ <strong>Bravos Shop</strong></p>
                        </div>
                        <div class="footer">
                            &copy; 2025 Bravos Shop. Mọi quyền được bảo lưu.
                        </div>
                    </div>
                </body>
                </html>
                """;
        emailContent = emailContent.replace("{USERNAME}",registerDto.getUsername());
        emailContent = emailContent.replace("{VERIFICATION_LINK}",confirmUrl);
        emailService.sendEmail(registerDto.getEmail(),"Xác nhận đăng ký tài khoản", emailContent);
    }

    @Override
    public String authenticateVerifyToken(String token) {
        RegisterDto registerDto = redisService.getAndDelete(token);
        if(registerDto == null) return null;
        boolean exist = userService.existByUsernameOrEmail(registerDto.getUsername(),registerDto.getEmail());
        if(exist) return "exist";
        User user = User
                .builder()
                .username(registerDto.getUsername())
                .displayName(registerDto.getDisplayName())
                .password(registerDto.getPassword())
                .email(registerDto.getEmail())
                .enabled(true)
                .build();
        user = userService.createNewUser(user);
        if(user == null) {
            log.error("Error when create user. Username: {}. Email: {}", registerDto.getUsername(), registerDto.getEmail());
            return "error";
        }
        return user.getUsername();
    }

    @Override
    public void addTokenToCookie(String token, int exp) {
        Cookie cookie = new Cookie("token",token);
        cookie.setMaxAge(exp);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookieService.addCookie(cookie);
    }

    @Override
    public void removeToken() {
        cookieService.deleteCookie("token");
    }

    @Override
    public String getToken() {
        Cookie cookie = cookieService.getCookie("token");
        if(cookie == null) return "";
        return cookie.getValue();
    }

    @Override
    public User authenticateUser(String username, String password) {
        User user = userService.findByUsername(username);
        if(user == null) return null;
        return passwordEncoder.matches(password,user.getPassword()) ? user : null;
    }

    @Override
    public String generateAccessToken(TokenInfo tokenInfo) {
        return jwtService.generateToken(tokenInfo,24 * 60 * 60 * 1000);
    }

}
