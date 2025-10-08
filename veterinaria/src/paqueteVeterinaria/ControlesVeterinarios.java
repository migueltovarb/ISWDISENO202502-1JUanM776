package paqueteVeterinaria;

import java.util.List;

public class ControlesVeterinarios {
	
	private String fecha;
	private List<TipoControl> TipoControl;
	public ControlesVeterinarios(String string, paqueteVeterinaria.TipoControl vacuna) {
		super();
		this.fecha = string;
		vacuna = vacuna;
	}
	public ControlesVeterinarios(List<paqueteVeterinaria.TipoControl> tipoControl2, Dueño manuel) {
		// TODO Auto-generated constructor stub
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public List<TipoControl> getTipoControl() {
		return getTipoControl();
	}
	public void setTipoControl(List<TipoControl> tipoControl) {
		tipoControl = tipoControl;
	}
	@Override
	public String toString() {
		return "ControlesVeterinarios [fecha=" + fecha + "]";
	}
	public void registrarControl() {
		// TODO Auto-generated method stub
		
	}
	public void mostrarResumenMascota() {
		// TODO Auto-generated method stub
		
	}
	public static void add(ControlesVeterinarios chequeo) {
		// TODO Auto-generated method stub
		
	}
	
}
