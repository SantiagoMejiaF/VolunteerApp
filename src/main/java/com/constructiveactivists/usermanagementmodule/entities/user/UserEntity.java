package com.constructiveactivists.usermanagementmodule.entities.user;

import com.constructiveactivists.usermanagementmodule.entities.user.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;


import java.time.LocalDate;

@Entity
@Getter
@Setter
@SequenceGenerator(name =  "USUARIO_SEQ", sequenceName = "SEQ_USUARIO", allocationSize = 1)
@Table(name = "USUARIO")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    @Comment("Llave primaria de la tabla usuario autoincrementada por secuencia")
    private Integer id;

    @Column(name = "NOMBRE", length = 20, nullable = false)
    @Comment("Nombre del usuario")
    private String firstName;

    @Column(name = "APELLIDO", length = 50, nullable = false)
    @Comment("Apellido del usuario")
    private String lastName;

    @Column(name = "CORREO", length = 50, unique = true, nullable = false)
    @Comment("Correo electronico del usuario")
    private String email;

    @Column(name = "CONTRASEÑA", length = 16)
    @Comment("Contraseña del usuario")
    private String password;

    @Column(name = "FECHA_REGISTRO", columnDefinition = "DATE", nullable = false)
    @Comment("Fecha de registro del usuario")
    private LocalDate registrationDate;

    @Column(name = "CELULAR", length = 10)
    @Comment("Numero de celular del usuario")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROL", length = 12, nullable = false)
    @Comment("Rol del usuario")
    private Role role;
}
