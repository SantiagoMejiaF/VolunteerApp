package com.constructiveactivists.usermodule.entities;

import com.constructiveactivists.notificationsmodule.entities.NotificationEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "USUARIO", schema = "MODULO_GESTION_USUARIOS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    @Comment("Llave primaria de la tabla usuario autoincrementada por secuencia")
    private Integer id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ROL_ID", referencedColumnName = "ID", nullable = false)
    private RoleEntity role;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_AUTORIZACION", nullable = false)
    @Comment("Estado de autorización del usuario")
    private AuthorizationStatus authorizationType;

    @Column(name = "NOMBRE", length = 20)
    @Comment("Nombre del usuario")
    private String firstName;

    @Column(name = "APELLIDO", length = 20)
    @Comment("Apellido del usuario")
    private String lastName;

    @Column(name = "CORREO", length = 50, unique = true, nullable = false)
    @Comment("Correo electronico del usuario")
    private String email;

    @Column(name = "IMAGEN")
    @Comment("Imagen del usuario")
    private String image;

    @Column(name = "FECHA_REGISTRO", columnDefinition = "DATE", nullable = false)
    @Comment("Fecha de registro del usuario")
    private LocalDate registrationDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    private List<NotificationEntity> notifications;

}
