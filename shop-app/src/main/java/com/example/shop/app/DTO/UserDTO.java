package com.example.shop.app.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("phone_number")
    @NotBlank(message = "PHONE NUMBER is required")
    private String phoneNumber;
    private String address;
    @NotBlank(message = "password cannot be blank")
    private String password;

    @JsonProperty("retype_password")
    private String reTypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAcooutId;

    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private Long roleId;


}
