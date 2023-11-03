package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Optional;

import modelo.Departamento;
import modelo.Empleado;

public class Empresa {
	private Connection conn = null;

	/**
	 * Constructor
	 */
	
	private List<Empleado> empleados;

	public Empresa() {
		// System.out.println("Constructor de Empresa");
		conn = BaseDatos.getConnection();
		createTables();
	}
	
	/**
	 * Cierra la agenda
	 */
	public void close() {
		BaseDatos.close();
	}

	public boolean addEmpleado(Empleado em) {

	    if (em == null) {
	        throw new IllegalArgumentException("Empleado cannot be null.");
	    }

	    String sql = """
	        INSERT INTO empleado (id, nombre, salario, departamento)
	        VALUES (?, ?, ?, ?)
	        """;

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, em.getId());
	        ps.setString(2, em.getNombre());
	        ps.setDouble(3, em.getSalario());
	        ps.setString(4, em.getDepartamento().getNombre());
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        if (e.getErrorCode() == 1062) {
	            throw new DuplicateFormatFlagsException("Empleado with ID " + em.getId() + " already exists.");
	        } else {
	            e.printStackTrace();
	        }
	        return false;
	    }
	}

	/**
	 * Añade un nuevo departamento a la tabla Departamento
	 * 
	 * @param c
	 * @return true si ha sido añadido, false en caso contrario
	 */

	public boolean addDepartamento(Departamento d) {

		String sql = """
				INSERT INTO departamento (id, nombre,jefe)
				VALUES (?, ?, ?)
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, d.getId());
			ps.setString(2, d.getNombre());
			// Si el jefe es nulo, establece el valor de la columna 'jefe' a nulo en la base de datos.
			if (d.getJefe() == null) {
				ps.setNull(3, Types.NULL);
			} else {
				ps.setString(3, d.getJefe().getNombre());
			}
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
		}
		return false;
	}
	
	public Object getEmpleado(String nombre) {
	    for (Empleado empleado : empleados) {
	        if (empleado.getNombre().equals(nombre)) {
	            return Optional.of(empleado);
	        }
	    }
	    return Optional.empty();
	}

	/**
	 * Borra un empleado conociendo su identificador
	 * 
	 * @param identificador
	 * @return true si es borrado, false en caso contrario
	 */
	
	public boolean deleteEmpleado(Empleado empleado) {
		String sqlEliminaEmpleado = "DELETE FROM empleado WHERE id = ?";
		String sqlActualizaDepartamento = "UPDATE departamento SET jefe = NULL WHERE jefe = ?";
	    
		try {

	        PreparedStatement psEliminaEmpleado = conn.prepareStatement(sqlEliminaEmpleado);
	        psEliminaEmpleado.setInt(1, empleado.getId());
	        int filasEliminadas = psEliminaEmpleado.executeUpdate();
	        
	        if (empleado.getDepartamento() != null && empleado.getDepartamento().getJefe() != null && empleado.getDepartamento().getJefe().getId() == empleado.getId()){
	            PreparedStatement psActualizaDepartamento = conn.prepareStatement(sqlActualizaDepartamento);
	            psActualizaDepartamento.setInt(1, empleado.getId());
	            psActualizaDepartamento.executeUpdate();
	        }
	        
	        return filasEliminadas > 0;
	        
	    } catch (SQLException e) {
	        return false;
	    }
	}

	/**
	 * Borra un departamento conociendo su identificador
	 * 
	 * @param identificador
	 * @return true si es borrado, false en caso contrario
	 */
	public boolean deleteDepartamento(Integer id) {
		String sqlEliminaDepartamento = """
				DELETE FROM departamento WHERE id = ?
				""";
		String sqlActualizaEmpleados = """
				UPDATE empleado SET departamento = NULL WHERE departamento = ?
				""";
		
		try {
			
			PreparedStatement psEliminaDepartamento = conn.prepareStatement(sqlEliminaDepartamento);
			psEliminaDepartamento.setInt(1, id);
			int filasEliminadas = psEliminaDepartamento.executeUpdate();
			
			if (filasEliminadas > 0) {
				
				PreparedStatement psActualizaEmpleados = conn.prepareStatement(sqlActualizaEmpleados);
				psActualizaEmpleados.setInt(1, id);
				psActualizaEmpleados.executeUpdate();
			}
			
			return filasEliminadas > 0;
			
		} catch (SQLException e) {
		}
		return false;
	}

	 /*
	 * Actualiza la informacion de un departamento
  	 * @return false si no se llega a ejecutar la actualización
	 */
	
		public boolean updateDepartamento(Departamento departamento) {
	    String sql = "UPDATE departamento SET nombre = ?, jefe = ? WHERE id = ?";
	    
	    try {
	    	
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, departamento.getNombre());
	        ps.setInt(2, departamento.getJefe().getId());
	        ps.setInt(3, departamento.getId());
	        
	        int filasActualizadas = ps.executeUpdate();
	        
	        return filasActualizadas > 0;
	        
	    } catch (SQLException e) {
	    }
	    return false;
	}
	
	/**
	 * Mostrar los departamentos
	 * 
	 * @return cadena con los departamentos
	 */
	
	public String showDepart() {
		String sql = """
				SELECT *
				FROM departamento
				""";
		try {
			StringBuffer sb = new StringBuffer();
			ResultSet rs = conn.createStatement().executeQuery(sql);
			if (!rs.first()) {
				return "La base de datos está vacía.";
			}
			while (rs.next()) {
				Departamento d = readDep(rs);
				sb.append(d.toString());
				sb.append("\n");
			}
			return sb.toString();
			
		}catch(SQLException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * Leer un departamento <b>Ver en clase</b>
	 * 
	 * @param descriptor de un random access file
	 * @return departamento
	 * 
	 */
	private Departamento readDep(ResultSet rs) throws SQLException {
	    Integer id = rs.getInt("id");
	    String nombre = rs.getString("nombre");
	    Empleado jefe = rs.getObject("jefe", Empleado.class);
	    return new Departamento(id, nombre, jefe == null ? null : jefe);
	}
	
	/**
	 * Mostrar los empleados
	 * 
	 * @return cadena con los departamentos
	 */
	public String showEmpl() {
		String sql = """
				SELECT id, nombre, salario, departamento
				FROM empleado
				""";
		try {
			StringBuffer sb = new StringBuffer();
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				Empleado em = readEmpl(rs);
				sb.append(em.toString());
				sb.append("\n");
			}
			return sb.toString();
			
		}catch(SQLException e) {
			
		}
		return "";
	}
	
	/**
	 * Leer un empleado <b>Ver en clase</b>
	 * 
	 * @param descriptor de un random access file
	 * @return empleado
	 * 
	 */
	
	private Empleado readEmpl(ResultSet rs) throws SQLException {
	    if (rs.next()) {
	        Integer id = rs.getInt("id");
	        String nombre = rs.getString("nombre");
	        Double salario = rs.getDouble("salario");
	        Departamento departamento= rs.getObject("departamento", Departamento.class);
	        return new Empleado(id, nombre, salario, departamento );
	    } else {
	        throw new SQLException("El ResultSet está vacío.");
	    }
	}
	


	/**
	 * Crea el esquema de la base de datos
	 * 
	 * @throws SQLException
	 */
	private void createTables() {
		String sql1 = null;
		String sql2 = null;

		if (BaseDatos.dbType.equalsIgnoreCase("sqlite")) {
			sql1 = """
						CREATE TABLE IF NOT EXISTS departamento (
							id INTEGER PRIMARY KEY AUTOINCREMENT,
							nombre TEXT NOT NULL,
							jefe INTEGER
						)
					""";
		}
		if (BaseDatos.dbType.equalsIgnoreCase("sqlite")) {
			sql2 = """
						CREATE TABLE IF NOT EXISTS empleado (
							id INTEGER PRIMARY KEY AUTOINCREMENT,
							nombre TEXT NOT NULL,
							salario REAL DEFAULT 0.0,
							departamento INTEGER
						)
					""";
		}
		if (BaseDatos.dbType.equalsIgnoreCase("mariadb")) {
			sql1 = """
						CREATE TABLE IF NOT EXISTS departamento (
						  id INT PRIMARY KEY AUTO_INCREMENT,
						  nombre VARCHAR(255) NOT NULL,
						  jefe INTEGER
						)
					""";
			
			System.out.println("If table1");
		}
		if (BaseDatos.dbType.equalsIgnoreCase("mariadb")) {
			sql2 = """
						CREATE TABLE IF NOT EXISTS empleado (
						  id INTEGER PRIMARY KEY AUTO_INCREMENT,
						  nombre VARCHAR(255) NOT NULL,
						  salario DECIMAL(10,2) DEFAULT 0.0,
						  departamento INT
						)
					""";
			System.out.println("If table2");

		}
		try {
			conn.createStatement().executeUpdate(sql1);
			conn.createStatement().executeUpdate(sql2);
		} catch (SQLException e) {
		}
	}

	

}