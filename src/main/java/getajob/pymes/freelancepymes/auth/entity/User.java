package getajob.pymes.freelancepymes.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;

@Data //genera getter y setters, tostring, equal y hashcode
@NoArgsConstructor //constructor vacio (jpa)
@AllArgsConstructor //constructor con todos los campos
@Builder //Permite crear objetos con User.builder().email("...").build()
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique=true)
    private String email;

    @Column(name="password")
    private String password;

    @Builder.Default // Evita que builder() ponga este campo en null por defecto
    @Column(name="is_active",nullable=false)
    private Boolean isActive = true;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="role_id")
    private Role role;

    
    
}
