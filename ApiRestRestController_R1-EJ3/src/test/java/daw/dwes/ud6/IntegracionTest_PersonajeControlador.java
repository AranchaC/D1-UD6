package daw.dwes.ud6;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

@SpringBootTest
public class IntegracionTest_PersonajeControlador {

	@Autowired
	private PersonajesRepositorio personajesRepositorio;
	
    @Test
    void findAll() {
        //var list = personajesRepositorio.findAll();
        ResponseEntity<List<Personaje>> response = (ResponseEntity<List<Personaje>>) personajesRepositorio.findAll();


        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonajeById(@PathVariable("id") Long id) {
        Personaje existePersonaje = personajesRepositorio.findById(id).orElse(null);
        if (existePersonaje != null) {
        	return ResponseEntity.ok(existePersonaje);
        } else {
        	//return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El personaje con id " + id + " no existe");
            //cambio la linea:
        	//nos aseguramos de que lance una ResponseStatusException:
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El personaje con id " + id + " no existe");

        }
    }//getId

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
    public ResponseEntity<?> actualizarPersonaje(
    		@PathVariable("id") Long id, 
    		@RequestBody Personaje actualizadoPersonaje) {
        Personaje existePersonaje = personajesRepositorio.findById(id).orElse(null);
        if (existePersonaje != null) {
            existePersonaje.setNombre(actualizadoPersonaje.getNombre());
            existePersonaje.setRol(actualizadoPersonaje.getRol());
            existePersonaje.setCasa(actualizadoPersonaje.getCasa());
            existePersonaje.setAscendencia(actualizadoPersonaje.getAscendencia());
            return ResponseEntity.ok(personajesRepositorio.save(existePersonaje));
        } else {
        	//si no existe el personaje:
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            		.body("El personaje con id " + id + " no existe");
        }
    }//putActualizar

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarPersonaje(@PathVariable("id") Long id) {
        Personaje existePersonaje = personajesRepositorio.findById(id).orElse(null);
        if (existePersonaje != null) {
            personajesRepositorio.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("El personaje con id " + id + " ha sido borrado.");
        } else {
            // Si no existe el personaje:
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El personaje con id " + id + " no existe");
        }
    }//deleteBorrar
    
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
