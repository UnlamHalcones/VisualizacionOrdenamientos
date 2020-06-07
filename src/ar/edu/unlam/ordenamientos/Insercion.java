package ar.edu.unlam.ordenamientos;

import java.util.List;

import ar.edu.unlam.graphics.Ordenador;

public class Insercion<T extends Comparable<T>> extends EstrategiaOrdenamiento<T> {

	public Insercion(Ordenador jFrameReference) {
		super(jFrameReference);
	}

	public void ordenar(List<T> arreglo) {
		for (int i = 1; i < arreglo.size(); i++) {
			T valorActual = arreglo.get(i);
			int j = i - 1;
			while (j >= 0 && arreglo.get(j).compareTo(valorActual) > 0) {
				arreglo.set(j + 1, arreglo.get(j));
				j = j - 1;
				jFrameOrdenador.display(j,i);
				jFrameOrdenador.sleep();
			}
			arreglo.set(j + 1, valorActual);
		}
	}

}