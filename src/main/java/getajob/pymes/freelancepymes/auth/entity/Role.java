package getajob.pymes.freelancepymes.auth.entity;

import getajob.pymes.freelancepymes.auth.entity.enums.RoleName;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;

//@Data equivale a usar @Getter, @Setter, @RequiredArgsConstructor, @ToString y @EqualsAndHashCode 
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=20)
    private RoleName name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "permisos_role", // Esto crea la tabla intermedia
        joinColumns = @JoinColumn(name = "id_role"),
        inverseJoinColumns = @JoinColumn(name = "id_permiso")
    )
    private java.util.Set<Permiso> permisos;
}
