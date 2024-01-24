package daw.dwes.ud6;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonajeControlador {

	private final PersonajesRepositorio personajesRepositorio;

    @Autowired
    public PersonajeControlador(PersonajesRepositorio personajesRepositorio) {
        this.personajesRepositorio = personajesRepositorio;
    }

    @GetMapping("/personajes")
    public List<Personaje> getAllPersonajes() {
        return personajesRepositorio.findAll();
    }

    @GetMapping("/personajes/{id}")
    public Personaje getPersonajeById(@PathVariable Long id) {
        return personajesRepositorio.findById(id).orElse(null);
    }

//    @GetMapping("/buscar")
//    public List<Personaje> getPersonajesByNombre(@RequestParam String nombre) {
//        return personajesRepositorio.findByNombre(nombre);
//    }

    @PostMapping("/personajes")
    public Personaje createPersonaje(@RequestBody Personaje personaje) {
        return personajesRepositorio.save(personaje);
    }

    @PutMapping("/personajes/{id}")
    public Personaje updatePersonaje(@PathVariable Long id, @RequestBody Personaje updatedPersonaje) {
        Personaje existingPersonaje = personajesRepositorio.findById(id).orElse(null);
        if (existingPersonaje != null) {
            existingPersonaje.setNombre(updatedPersonaje.getNombre());
            existingPersonaje.setRol(updatedPersonaje.getRol());
            existingPersonaje.setCasa(updatedPersonaje.getCasa());
            existingPersonaje.setAscendencia(updatedPersonaje.getAscendencia());
            return personajesRepositorio.save(existingPersonaje);
        } else {
            return null; // Manejar el caso cuando no encuentra el personaje
        }
    }

    @DeleteMapping("/{id}")
    public void deletePersonaje(@PathVariable Long id) {
    	personajesRepositorio.deleteById(id);
    }
}
