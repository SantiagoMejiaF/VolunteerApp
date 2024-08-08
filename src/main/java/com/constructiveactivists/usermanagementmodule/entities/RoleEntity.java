package com.constructiveactivists.usermanagementmodule.entities;

import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "ROLE_SEQ", sequenceName = "SEQ_ROLE", allocationSize = 1)
@Table(name = "ROLE", schema = "USER_MANAGEMENT_MODULE")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ROL", length = 12, nullable = false)
    private RoleType roleType;
}
