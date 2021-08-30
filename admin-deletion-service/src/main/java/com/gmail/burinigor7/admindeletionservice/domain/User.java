package com.gmail.burinigor7.admindeletionservice.domain;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "usr")
public class User {
    @Id
    private Long id;
}
