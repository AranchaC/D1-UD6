package daw.dwes.ud6;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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




//@ExtendWith(MockitoExtension.class)
public class PersonajeControladorTest {
	
    @InjectMocks
    private PersonajeControlador personajeControlador;

    @Mock
    private PersonajesRepositorio personajesRepositorio;
    
    //List<Personaje> personajes = personajesRepositorio.findAll();
    
    @SuppressWarnings("deprecation")
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    

    @Test
    void getTodosPersonajes() {
        // Lo que vamos a simular
       List<Personaje> personajes = Arrays.asList(
                new Personaje("Harry Potter", "Estudiante", "Gryffindor", "Mestizo"),
                new Personaje("Hermione Granger", "Estudiante", "Gryffindor", "Nacida de muggles"),
                new Personaje("Ron Weasley", "Estudiante", "Gryffindor", "Mestizo")
        );
        when(personajesRepositorio.findAll()).thenReturn(personajes);

        // Test
        ResponseEntity<List<Personaje>> responseEntity = personajeControlador.getTodosPersonajes();
       // List<Personaje> listaPersonajes = responseEntity.getBody();

        // Comprobaciones
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode())

        );

        // Verificamos que se ha llamado al método
        verify(personajesRepositorio, times(1)).findAll();
    }


    private Object assertEquals(HttpStatus ok, HttpStatusCode statusCode) {
		// TODO Auto-generated method stub
		return null;
	}


	private Object assertNotNull(ResponseEntity<List<Personaje>> responseEntity) {
		// TODO Auto-generated method stub
		return null;
	}


	@GetMapping("/{id}")
    public ResponseEntity<?> getPersonajeById(@PathVariable("id") Long id) {
        Personaje existePersonaje = personajesRepositorio.findById(id).orElse(null);
        if (existePersonaje != null) {
        	return ResponseEntity.ok(existePersonaje);
        } else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
            		.body("El personaje con id " + id + " no existe");
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
