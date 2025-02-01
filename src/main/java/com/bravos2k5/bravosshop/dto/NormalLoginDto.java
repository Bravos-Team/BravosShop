package com.bravos2k5.bravosshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link com.bravos2k5.bravosshop.model.user.User}
 */
@Value
public class NormalLoginDto implements Serializable {
    @NotNull(message = "Username không được bỏ trống")
    @NotBlank(message = "Username không được bỏ trống")
    @Length(message = "Username phải >= 6 và <= 30 kí tự", min = 6, max = 30)
    String username;
    String password;
}