package daw.dwes.ud6;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
    private PersonajeControlador personajeControlador;
	
    @Test
    void getTodosPersonajes() {
        //var list = personajesRepositorio.findAll();
        ResponseEntity<List<Personaje>> response = 
        		personajeControlador.getTodosPersonajes();

        assertAll(
          () -> assertNotNull(response),
          () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );
    }//findAll

	@Test
    void getPersonajeById_Si_ExisteId() {
        Long id = 1L;

        ResponseEntity<?> responseEntity = 
        		personajeControlador.getPersonajeById(id);

        // Assert: comprobaciones
        assertAll(
          () -> assertNotNull(responseEntity),
          () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
          () -> assertTrue(responseEntity.getBody() instanceof Personaje)
        );       
    }//getSiId
	
	@Test
    void getPersonajeById_No_ExisteId() {
		Long id = -100L;

		//assertThrows para verificar que se lanza una ResponseStatusException
	    var res = assertThrows(ResponseStatusException.class, () -> {
	        personajeControlador.getPersonajeById(id);
	    });

	    // Comprobamos que la excepción es la esperada
	    assert (res.getMessage().contains("El personaje con id " +
	    		id + " no existe"));	
    }//getNoId

   

}//main
