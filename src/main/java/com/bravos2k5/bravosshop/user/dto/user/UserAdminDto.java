package com.bravos2k5.bravosshop.user.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminDto {

    Long id;
    String username;
    String displayName;
    String email;
    Boolean enabled;
}
