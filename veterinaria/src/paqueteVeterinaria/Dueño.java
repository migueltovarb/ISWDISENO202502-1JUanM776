package paqueteVeterinaria;

public class Dueño {
	
	private String nombreCompleto;
	private String documento;
	private String telefono;
	public Dueño(String nombreCompleto, String documento, String telefono) {
		super();
		this.nombreCompleto = nombreCompleto;
		this.documento = documento;
		this.telefono = telefono;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	@Override
	public String toString() {
		return "Dueño [nombreCompleto=" + nombreCompleto + ", documento=" + documento + ", telefono=" + telefono + "]";
	}
	public void registrar() {
		System.out.println("Dueño Registrado");
	}
}
