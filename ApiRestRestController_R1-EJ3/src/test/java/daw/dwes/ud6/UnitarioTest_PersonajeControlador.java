package daw.dwes.ud6;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.server.ResponseStatusException;

//@ExtendWith(MockitoExtension.class)
public class UnitarioTest_PersonajeControlador {
	
    List<Personaje> personajes;
    
    @InjectMocks
    private PersonajeControlador personajeControlador;

    @Mock
    private PersonajesRepositorio personajesRepositorio;
    
   // @SuppressWarnings("deprecation")
    @BeforeEach
    //método que se ejecuta antes de cada prueba:
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personajes = personajesRepositorio.findAll();
    }
    
    @Test
    void getTodosPersonajes() {
        // Lo que vamos a simular
    	
       /*personajes = Arrays.asList(
                new Personaje("Harry Potter", "Estudiante", "Gryffindor", "Mestizo"),
                new Personaje("Hermione Granger", "Estudiante", "Gryffindor", "Nacida de muggles"),
                new Personaje("Ron Weasley", "Estudiante", "Gryffindor", "Mestizo")
        );*/
        when(personajesRepositorio.findAll()).thenReturn(List.copyOf(personajes));

        // Test
        ResponseEntity<List<Personaje>> responseEntity = personajeControlador.getTodosPersonajes();

        // Comprobaciones
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode())
        );

        // Verificamos que se ha llamado al método
        //quito esta línea porque ya se está ejecuetando en el setup:
        //verify(personajesRepositorio, times(1)).findAll();
    }//getTodosPersonajes


	@Test
    void getPersonajeById_Si_ExisteId() {
        Long id = 1L;
        Personaje personaje = new Personaje();
        personaje.setId(id);
        when(personajesRepositorio.findById(id)).thenReturn(Optional.of(personaje));

        ResponseEntity<?> responseEntity = personajeControlador.getPersonajeById(id);

        // Assert: comprobaciones
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof Personaje),
                () -> assertEquals(personaje, responseEntity.getBody())
        );       
        
        // Verificamos que se ha llamado al método
        verify(personajesRepositorio, times(1)).findById(id);
    }//getSid
    
	@Test
    void getPersonajeById_No_ExisteId() {
		Long id = -100L;
		when(personajesRepositorio.findById(id)).thenReturn(Optional.empty());
        //ResponseEntity<?> responseEntity = personajeControlador.getPersonajeById(id);

		//salta la excepcion
		//assertThrows para verificar que se lanza una ResponseStatusException
	    var res = assertThrows(ResponseStatusException.class, () -> {
	        personajeControlador.getPersonajeById(id);
	    });

	    // Comprobamos que la excepción es la esperada
	    assert (res.getMessage().contains("El personaje con id " + id + " no existe"));
	    
        // Verificamos que se ha llamado al método
        verify(personajesRepositorio, times(1)).findById(id);
		
    }//getNoId

	/*
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
*/
}//main
