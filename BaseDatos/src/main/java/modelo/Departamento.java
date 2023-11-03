package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Departamento {
	
	Integer id;
	String nombre;
	Empleado jefe;
	
	// Constructor con tres parámetros
    public Departamento(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.jefe = null;
    }

    // Constructor con dos parámetros
    public Departamento(String nombre) {
        this.nombre = nombre;
        this.id = null;
        this.jefe = null;
    }

	
}
