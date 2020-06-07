package ar.edu.unlam.ordenamientos;

import java.util.List;

import ar.edu.unlam.graphics.Ordenador;

public class Seleccion<T extends Comparable<T>> extends EstrategiaOrdenamiento<T> {

	public Seleccion(Ordenador jFrameReference) {
		super(jFrameReference);
	}

	public void ordenar(List<T> arreglo) {
		T menor;
		int i, j, posicionDelMenor;

		for (i = 0; i < arreglo.size() - 1; i++) {
			menor = arreglo.get(i);
			posicionDelMenor = i;
			for (j = i + 1; j < arreglo.size(); j++) {
				cantComparaciones++;
				if (arreglo.get(j).compareTo(menor) < 0) {
					menor = arreglo.get(j);
					posicionDelMenor = j;
				}
				jFrameOrdenador.display(posicionDelMenor, j);
				jFrameOrdenador.sleep();
			}
			if (posicionDelMenor != i) {
				intercambiar(arreglo, i, posicionDelMenor);
				cantOperaciones++;
			}
		}
	}

}