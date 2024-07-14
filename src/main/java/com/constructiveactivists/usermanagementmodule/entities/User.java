package com.constructiveactivists.usermanagementmodule.entities;

import com.constructiveactivists.usermanagementmodule.entities.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Table(name = "USUARIO")
@Getter
@Setter
@SequenceGenerator( name =  "USUARIO_SEQ", sequenceName = "SEQ_USUARIO", allocationSize = 1)
public class User {

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

    @Column(name = "CONTRASEÑA", length = 16, nullable = false)
    @Comment("Contraseña del usuario")
    private String password;

    @Column(name = "EDAD", length = 3, nullable = false)
    @Comment("Edad del usuario")
    private String age;

    @Column(name = "FECHA_REGISTRO", columnDefinition = "DATE", nullable = false)
    @Comment("Fecha de registro del usuario")
    private LocalDate registrationDate;

    @Column(name = "CELULAR", length = 10, nullable = false)
    @Comment("Numero de celular del usuario")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROL", length = 12, nullable = false)
    @Comment("Rol del usuario")
    private Role role;
}
