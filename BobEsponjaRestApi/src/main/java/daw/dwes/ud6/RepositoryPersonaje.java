package daw.dwes.ud6;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface RepositoryPersonaje extends CrudRepository<Personaje,Long> {
	public Optional<Personaje> findByNombre(String nombre);

}
