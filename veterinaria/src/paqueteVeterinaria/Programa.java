package paqueteVeterinaria;

import java.util.ArrayList;
import java.util.List;

public class Programa {
public static void main(String[]args) {
		
		ControlesVeterinarios vacuna = new ControlesVeterinarios("Vacuna", TipoControl.VACUNA);
		ControlesVeterinarios chequeo = new ControlesVeterinarios("Chequeo", TipoControl.CHEQUEO);
		ControlesVeterinarios desparasitacion = new ControlesVeterinarios("Desparasitacion", TipoControl.DESPARASITACION);
		
		Dueño manuel = new Dueño("Manuel", "3173742559", null);
		
		List<TipoControl> tipoControl= new ArrayList<TipoControl>();
		tipoControl.add(chequeo);
 		
 		List<TipoControl> tipoControl1= new ArrayList<TipoControl>();
 		ControlesVeterinarios.add(chequeo);
 		ControlesVeterinarios numeroUno = new ControlesVeterinarios(tipoControl1, manuel);
 		
 		numeroUno.registrarControl();
 		numeroUno.mostrarResumenMascota();
	}

}
