package com.bruce.mq.user.controller;

import com.bruce.mq.user.service.UserServiceImpl;
import com.bruce.mq.shared.user.dto.UserRegisterRequest;
import com.bruce.mq.shared.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author BruceXuK
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 发送验证码
     *
     * @param email 邮箱地址
     * @return 发送结果
     */
    @GetMapping("/send-code")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 检查邮箱是否已被注册
            if (userService.existsByEmail(email)) {
                response.put("success", false);
                response.put("message", "该邮箱已被注册");
                return ResponseEntity.badRequest().body(response);
            }

            // 发送验证码
            String code = userService.sendVerificationCode(email);

            response.put("success", true);
            response.put("message", "验证码已发送至您的邮箱，请注意查收");
            // 注意：生产环境中不应返回验证码，这里仅用于测试
//            response.put("code", code);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "发送验证码失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 用户注册
     *
     * @param registerRequest 用户注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegisterRequest registerRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userService.register(registerRequest);

            response.put("success", true);
            response.put("message", "注册成功");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "注册失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 检查邮箱是否已被注册
     *
     * @param email 邮箱地址
     * @return 检查结果
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        boolean exists = userService.existsByEmail(email);
        response.put("exists", exists);
        response.put("message", exists ? "该邮箱已被注册" : "该邮箱可以使用");

        return ResponseEntity.ok(response);
    }
}
