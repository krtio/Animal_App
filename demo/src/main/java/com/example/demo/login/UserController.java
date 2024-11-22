package com.example.demo.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        if (isUserInvalid(user)) {
            return ResponseEntity.badRequest().body("모든 필드를 채워주세요.");
        }
        if (userDao.existsById(user.getId())) {
            return ResponseEntity.badRequest().body("중복된 ID입니다.");
        }
        userDao.save(user);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userDao.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/login") // POST 방식으로 변경
    public ResponseEntity<User> loginUser(@RequestBody User loginUser) {
        User foundUser = userDao.findById(loginUser.getId());
        if (foundUser != null && foundUser.getPassword().equals(loginUser.getPassword())) {
            return ResponseEntity.ok(foundUser); // 로그인 성공 시 User 객체 반환
        }
        return ResponseEntity.status(401).body(null); // 로그인 실패 시 null 반환
    }

    private boolean isUserInvalid(User user) {
        return user.getId() == null || user.getPassword() == null || user.getName() == null ||
                user.getEmail() == null || user.getPhone() == null || user.getBirthday() == null;
    }
}

