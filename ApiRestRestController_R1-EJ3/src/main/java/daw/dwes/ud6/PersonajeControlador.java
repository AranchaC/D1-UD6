package daw.dwes.ud6;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<?> getPersonajeById(@PathVariable("id") Long id) {
        Optional<Personaje> existePersonaje = personajesRepositorio.findById(id);
        if (existePersonaje != null) {
        	return ResponseEntity.ok(existePersonaje);
        } else {
        	//ej3 - línea return:
        	return ResponseEntity.status(HttpStatus.CONFLICT).body("El personaje con id " + id + " no existe");
            
        	//ej5 - test - cambio la linea por:
        	//nos aseguramos de que lance una ResponseStatusException:
        	//throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El personaje con id " + id + " no existe");
        }
    }//getId
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getPersonajeByNombre(@PathVariable("nombre") String nombre){
		Personaje existe = personajesRepositorio.findByNombre(nombre);
		if (existe != null) {
			return ResponseEntity.ok(existe);
		}
    	
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personaje con nombre " + nombre 
    			+ "no existe");   	
    }
    
    @PostMapping
    public ResponseEntity<?> crearPersonaje(@RequestBody Personaje personaje) {
    	Personaje existePersonaje = personajesRepositorio
    			.findByNombre(personaje.getNombre());
        if (existePersonaje != null) {
        	return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            		.body("El personaje con nombre " + personaje.getNombre() + 
            				" ya existe.");
        }
    	return ResponseEntity.status(HttpStatus.CREATED)
        		.body(personajesRepositorio.save(personaje));
    }//postCrearPersonaje

    @PutMapping("/{id}")
    public ResponseEntity<?> putMethodName(@PathVariable("id") Long id, @RequestBody Personaje personaje) {
        Personaje existePersonaje = personajesRepositorio.findById(id).orElse(null);
        if (existePersonaje != null) {
        	existePersonaje.setAscendencia(personaje.getAscendencia());
        	existePersonaje.setCasa(personaje.getCasa());
        	existePersonaje.setNombre(personaje.getNombre());
        	existePersonaje.setRol(personaje.getRol());
        	return ResponseEntity.ok(personajesRepositorio.save(existePersonaje));
        } else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el persona con id " + id);
        }        
    }
    
//    @PutMapping("/{id}")
//    public ResponseEntity<?> actualizarPersonaje(
//    		@PathVariable("id") Long id, 
//    		@RequestBody Personaje actualizadoPersonaje) {
//        Personaje existePersonaje = personajesRepositorio.findById(id).orElse(null);
//        if (existePersonaje != null) {
//            existePersonaje.setNombre(actualizadoPersonaje.getNombre());
//            existePersonaje.setRol(actualizadoPersonaje.getRol());
//            existePersonaje.setCasa(actualizadoPersonaje.getCasa());
//            existePersonaje.setAscendencia(actualizadoPersonaje.getAscendencia());
//            return ResponseEntity.ok(personajesRepositorio.save(existePersonaje));
//        } else {
//        	//si no existe el personaje:
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//            		.body("El personaje con id " + id + " no existe");
//        }
//    }//putActualizar

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarPerson(@PathVariable("id") Long id){
    	Personaje existePersonaje = personajesRepositorio.findById(id).orElse(null);
    	if (existePersonaje != null) {
    		personajesRepositorio.delete(existePersonaje);
    		return ResponseEntity.status(HttpStatus.OK).body("Personaje con nombre "+ existePersonaje.getNombre() +" ha sido borrado");
    	} else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El personaje con id " + id + " no existe");
    	}
    }
    
    
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> borrarPersonaje(@PathVariable("id") Long id) {
//        Personaje existePersonaje = personajesRepositorio.findById(id).orElse(null);
//        if (existePersonaje != null) {
//            personajesRepositorio.deleteById(id);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body("El personaje con id " + id + " ha sido borrado.");
//        } else {
//            // Si no existe el personaje:
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("El personaje con id " + id + " no existe");
//        }
//    }//deleteBorrar
    
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarAtributoPersonaje(
    		@PathVariable("id") Long id, 
    		@RequestBody Personaje actualizadoPersonaje) {
        Personaje existePersonaje = personajesRepositorio.findById(id).orElse(null);
        if (existePersonaje != null) {
            if (actualizadoPersonaje.getNombre() != null) {
                existePersonaje.setNombre(actualizadoPersonaje.getNombre());
            }
            if (actualizadoPersonaje.getRol() != null) {
                existePersonaje.setRol(actualizadoPersonaje.getRol());
            }
            if (actualizadoPersonaje.getCasa() != null) {
                existePersonaje.setCasa(actualizadoPersonaje.getCasa());
            }
            if (actualizadoPersonaje.getAscendencia() != null) {
                existePersonaje.setAscendencia(actualizadoPersonaje.getAscendencia());
            }
            return ResponseEntity.ok(personajesRepositorio.save(existePersonaje));

        } else {
            // Si no existe el personaje:
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El personaje con id " + id + " no existe");
        }
    }//patch

}//main
