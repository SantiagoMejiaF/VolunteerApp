package com.constructiveactivists.usermanagementmodule.entities.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "ROLE_SEQ", sequenceName = "SEQ_ROLE", allocationSize = 1)
@Table(name = "ROLE")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "NAME", length = 50, unique = true, nullable = false)
    private String nameRole;
}
