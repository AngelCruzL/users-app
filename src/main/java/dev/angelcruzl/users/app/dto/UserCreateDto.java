package dev.angelcruzl.users.app.dto;

import dev.angelcruzl.users.app.models.IUser;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto implements IUser {

    @NotEmpty(message = "{firstName.required}")
    private String firstName;

    @NotEmpty(message = "{lastName.required}")
    private String lastName;

    @NotEmpty(message = "{email.required}")
    private String email;

    @NotEmpty(message = "{username.required}")
    private String username;

    @NotEmpty(message = "{password.required}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "{password.pattern}")
    private String password;

    private boolean admin;
}