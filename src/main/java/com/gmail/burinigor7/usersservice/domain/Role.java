package com.gmail.burinigor7.usersservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "usr_role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @SequenceGenerator(name = "role_seq", sequenceName = "usr_role_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "'title' must not be blank")
    @Size(min = 2, message = "'title' must be longer then 1 characters")
    private String title;
}
