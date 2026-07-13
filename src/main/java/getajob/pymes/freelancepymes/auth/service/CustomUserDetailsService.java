package getajob.pymes.freelancepymes.auth.service;

import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + username));

        // Se inicializan los proxies perezosos de rol/permisos dentro de esta
        // transacción.
        // El JwtAuthenticationFilter llama a getAuthorities() fuera de cualquier sesión
        // de
        // Hibernate (corre antes de que open-in-view abra el EntityManager de la
        // vista),
        // así que si no se fuerza aquí, esa llamada lanza LazyInitializationException.
        if (user.getRole() != null) {
            user.getRole().getName();
            if (user.getRole().getPermisos() != null) {
                user.getRole().getPermisos().size();
            }
        }

        return user;
    }
}
