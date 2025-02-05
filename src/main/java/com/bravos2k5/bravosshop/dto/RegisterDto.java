package com.bravos2k5.bravosshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * DTO for {@link com.bravos2k5.bravosshop.model.user.User}
 */

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RedisHash
public class RegisterDto implements Serializable {
    @Size
    @NotBlank(message = "Tên đăng nhập không được bỏ trống")
    @Length(message = "Tên đăng nhập phải 6 - 31 ký tự", min = 6, max = 31)
    String username;
    @Setter
    @Size
    @NotBlank(message = "Mật khẩu không được bỏ trống")
    @Length(message = "Mật khẩu phải 8 - 36 kí tự", min = 8, max = 36)
    String password;
    @NotBlank(message = "Tên không được bỏ trống")
    @Length(message = "Tên không quá 50 kí tự", min = 1, max = 50)
    String displayName;
    @Email(message = "Email không hợp lệ")
    String email;
}