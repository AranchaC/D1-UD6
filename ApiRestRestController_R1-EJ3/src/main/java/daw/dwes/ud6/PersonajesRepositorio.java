package daw.dwes.ud6;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PersonajesRepositorio extends JpaRepository<Personaje,Long>{
	
	Personaje findByNombre(String nombre);

}
