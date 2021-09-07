package com.gmail.burinigor7.userscrudservice.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.gmail.burinigor7.userscrudservice.util.JacksonViews;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "usr")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(name = "usr_seq", sequenceName = "usr_id_seq", allocationSize = 1,
            initialValue = 2)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_seq")
    @JsonView(JacksonViews.Admin.class)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "'firstName' must not be blank")
    @JsonView(JacksonViews.User.class)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "'lastName' must not be blank")
    @JsonView(JacksonViews.User.class)
    private String lastName;

    @JsonView(JacksonViews.User.class)
    private String patronymic;

    @Column(name = "phone_number", nullable = false, unique = true)
    @Pattern(regexp = "^\\+7\\d{10}$", message = "incorrect 'phoneNumber'")
    @NotNull
    @JsonView(JacksonViews.User.class)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_role", nullable = false)
    @NotNull
    @JsonView(JacksonViews.Admin.class)
    private Role role;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "'email' must not be blank")
    @Email(message = "incorrect 'email'")
    @JsonView(JacksonViews.User.class)
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "'login' must not be blank")
    @Size(min = 4, message = "'login' must be longer then 3 characters")
    @JsonView(JacksonViews.User.class)
    private String login;

    @Column(nullable = false)
    @NotBlank(message = "'password' must not be blank")
    @Size(min = 4, message = "'password' must be longer then 3 characters")
    @JsonView(JacksonViews.Admin.class)
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonView(JacksonViews.Admin.class)
    private Status status;
}
