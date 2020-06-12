package ar.edu.unlam.generador.datos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class ManejadorArchivos {

	private static String directoryName = "Informacion_Estadistica";

	public static void agregarRegistro(String linea) {
		checkFolder();

		String path = directoryName + File.separator + "registro.csv";
		BufferedWriter out;

		try {
			out = new BufferedWriter(new FileWriter(path, true));
			out.write(linea + "\n");
			out.close();
		} catch (IOException e) {
			// error processing code
			System.out.println("Error al leer el archivo");

		}

	}

	private static void checkFolder() {
		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

}