package com.constructiveactivists.usermodule.entities;

import com.constructiveactivists.usermodule.entities.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ROL", schema = "MODULO_GESTION_USUARIOS")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ROL", length = 22, nullable = false)
    private RoleType roleType;
}
