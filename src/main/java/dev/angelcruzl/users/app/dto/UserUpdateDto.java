package dev.angelcruzl.users.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    private String firstName;

    private String lastName;

    private String email;

    private String username;

}