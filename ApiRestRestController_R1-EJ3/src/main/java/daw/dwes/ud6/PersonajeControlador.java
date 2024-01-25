package daw.dwes.ud6;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
// para que haya punto de entrada general:
@RequestMapping("/personajes")
public class PersonajeControlador {

	private final PersonajesRepositorio personajesRepositorio;

    @Autowired
    public PersonajeControlador(PersonajesRepositorio personajesRepositorio) {
        this.personajesRepositorio = personajesRepositorio;
    }

    @GetMapping
    public ResponseEntity<List<Personaje>> getTodosPersonajes() {
        return ResponseEntity.ok(personajesRepositorio.findAll());
    }//getAllPersonajes

    @GetMapping("/{id}")
    public ResponseEntity<Personaje> getPersonajeById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personajesRepositorio.findById(id).get());
    }//getId

    @PostMapping
    public ResponseEntity<Personaje> crearPersonaje(@RequestBody Personaje personaje) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personajesRepositorio.save(personaje));
    }//postCrearPersonaje

    @PutMapping("/{id}")
    public ResponseEntity<Personaje> actualizarPersonaje(
    		@PathVariable("id") Long id, 
    		@RequestBody Personaje actualizadoPersonaje) {
        Personaje existePersonaje = personajesRepositorio.findById(id).get();
        if (existePersonaje != null) {
            existePersonaje.setNombre(actualizadoPersonaje.getNombre());
            existePersonaje.setRol(actualizadoPersonaje.getRol());
            existePersonaje.setCasa(actualizadoPersonaje.getCasa());
            existePersonaje.setAscendencia(actualizadoPersonaje.getAscendencia());
            return ResponseEntity.ok(personajesRepositorio.save(existePersonaje));
        } else {
        	//si no existe el personaje:
            return ResponseEntity.notFound().build();
        }
    }//putActualizar

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarPersonaje(@PathVariable("id") Long id) {
    	personajesRepositorio.deleteById(id);
    	return ResponseEntity.noContent().build();
    }//deleteBorrar
}
