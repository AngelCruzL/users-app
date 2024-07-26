package dev.angelcruzl.users.app.dto;

import dev.angelcruzl.users.app.models.IUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto implements IUser {

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private boolean admin;

}