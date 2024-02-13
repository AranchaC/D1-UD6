package daw.dwes.ud6;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("personajes") // ojo, poner lo mismo en endPoing
public class PersonajeController {
	static final String endPoint = "http://localhost:8080/personajes";

	@Autowired
	private RepositoryPersonaje repositorio;
	
	@GetMapping()
	public ResponseEntity<Iterable<Personaje>> getAll() {
		return ResponseEntity.ok(repositorio.findAll());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Personaje> getOne(@PathVariable(name="id") Long id) {
		Optional<Personaje> res = repositorio.findById(id);
		if (res.isEmpty()) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(res.get());
	}
	
	@PostMapping 
	public ResponseEntity<Personaje> save(@RequestBody Personaje personaje) throws URISyntaxException{
		if (repositorio.findByNombre(personaje.getNombre()).isPresent()) {
			// Error si intento de insertar nombre duplicado
			return ResponseEntity.badRequest().build();
		}
			
		return ResponseEntity.created(new URI(endPoint + "/" + personaje.getId()))
				.body(repositorio.save(personaje));
	}
	
	@PutMapping("{id}") 
	public ResponseEntity<Personaje> replace(@PathVariable(name="id") Long id, @RequestBody Personaje p) {
		// Si llega ID y no coinciden con el del Path, no es válida
		if (p.getId() != null && p.getId() != id) return ResponseEntity.badRequest().build();
		// Si no encuentra el Id, error:
		if (repositorio.findById(id).isEmpty()) return ResponseEntity.notFound().build();
		// Si intenta cambiar a un nombre que ya exista, error:
		// faltaría mandar de alguna manera el error
		if ( repositorio.findByNombre(p.getNombre()).isPresent() &&
			repositorio.findByNombre(p.getNombre()).get().getId() != id) {
			return ResponseEntity.badRequest().build();
		}
		p.setId(id);
		return ResponseEntity.accepted().body(repositorio.save(p));
	}
	
	@PatchMapping("{id}") 
	public ResponseEntity<Personaje> modify(@PathVariable(name="id") Long id, @RequestBody Personaje p) {
		// Si llega id y no coincide con el del Path, no es válida
		if (p.getId() != null && p.getId() != id) return ResponseEntity.badRequest().build();
		// Si no encuentra el Id, error:
		if (! repositorio.findById(id).isPresent()) return ResponseEntity.notFound().build();
		// Si intenta cambiar a un nombre que ya exista, error:
		// faltaría mandar de alguna manera el error
		if ( repositorio.findByNombre(p.getNombre()).isPresent() &&
				repositorio.findByNombre(p.getNombre()).get().getId() != id) {
			return ResponseEntity.badRequest().build();
		}
		
		Personaje personajeBD = repositorio.findById(id).get();
		if (p.getNombre() != null) personajeBD.setNombre(p.getNombre());
		if (p.getDescripción() != null) personajeBD.setDescripción(p.getDescripción());
		return ResponseEntity.accepted().body(repositorio.save(personajeBD));
	}
	
	@DeleteMapping("{id}") 
	public ResponseEntity<String> delete(@PathVariable(name="id") Long id) {
		Optional<Personaje> res = repositorio.findById(id);
		if (res.isEmpty()) return ResponseEntity.notFound().build();
		
		repositorio.deleteById(id);
		return ResponseEntity.ok().body("Entity deleted");
	}
	
}
