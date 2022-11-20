package com.ncourses.authuser.user.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends RepresentationModel<UserDto> {

    public interface UserView {
        interface RegistrationPost {
        }

        interface UserPut {
        }

        interface PasswordPut {
        }

        interface ImagePut {
        }

        interface BasicView {
        }
    }

    @JsonView(UserView.BasicView.class)
    private UUID userId;

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String username;

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Email(groups = UserView.RegistrationPost.class)
    @JsonView({UserView.RegistrationPost.class, UserView.BasicView.class})
    private String email;

    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 20, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @NotBlank(groups = UserView.PasswordPut.class)
    @Size(min = 6, max = 20, groups = UserView.PasswordPut.class)
    @JsonView({UserView.PasswordPut.class})
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class, UserView.BasicView.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class, UserView.BasicView.class})
    private String phoneNumber;

    @NotBlank(groups = UserView.ImagePut.class)
    @JsonView({UserView.ImagePut.class, UserView.BasicView.class})
    private String imageUrl;

    @JsonView(UserView.BasicView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

}
