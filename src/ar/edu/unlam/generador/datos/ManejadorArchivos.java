package ar.edu.unlam.generador.datos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class ManejadorArchivos {

	public static void agregarRegistro(String linea) {

		String path = "src\\Informacion Estadistica\\registro.csv";
		BufferedWriter out;
		
		try {   
		    out = new BufferedWriter(new FileWriter(path, true));   
		    out.write(linea+"\n");   
		    out.close();   
		} catch (IOException e) {   
		    // error processing code   
			System.out.println("Error al leer el archivo");
			
		    }   
			
		System.out.println(linea);
	}

}