package paqueteVeterinaria;

public class Mascotas {
	
	private String nombre;
	private String especie;
	private Dueño dueñoMascota;
	private float edad;
	public Mascotas(String nombre, String especie, Dueño dueñoMascota, float edad) {
		super();
		this.nombre = nombre;
		this.especie = especie;
		this.dueñoMascota = dueñoMascota;
		this.edad = edad;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEspecie() {
		return especie;
	}
	public void setEspecie(String especie) {
		this.especie = especie;
	}
	public Dueño getDueñoMascota() {
		return dueñoMascota;
	}
	public void setDueñoMascota(Dueño dueñoMascota) {
		this.dueñoMascota = dueñoMascota;
	}
	public float getEdad() {
		return edad;
	}
	public void setEdad(float edad) {
		this.edad = edad;
	}
	@Override
	public String toString() {
		return "Mascotas [nombre=" + nombre + ", especie=" + especie + ", dueñoMascota=" + dueñoMascota + ", edad="
				+ edad + "]";
	}

}
