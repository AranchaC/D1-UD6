package daw.dwes.ud6;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "personajes", path = "personajes")
public interface PersonajesRepositorio extends 
	PagingAndSortingRepository<Personaje, Long>, 
	CrudRepository<Personaje,Long>{
	
	List<Personaje> findByNombre(@Param("nombre") String nombre);

}
