package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Empleado {
	
	Integer id;
	String nombre;
	Double salario;
	Departamento departamento;
	
	// Constructor con tres parámetros
    public Empleado(String nombre, Double salario, Departamento departamento) {
        this.nombre = nombre;
        this.salario = salario;
        this.departamento = departamento;
    }

    // Constructor con dos parámetros
    public Empleado(String nombre, Double salario) {
        this.nombre = nombre;
        this.salario = salario;
        this.departamento = null;
    }

}
