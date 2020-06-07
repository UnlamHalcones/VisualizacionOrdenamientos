package ar.edu.unlam.ordenamientos;

import java.util.List;

import ar.edu.unlam.graphics.Ordenador;

public class Burbujeo<T extends Comparable<T>> extends EstrategiaOrdenamiento<T> {

	public Burbujeo(Ordenador jFrameOrdenamiento) {
		super(jFrameOrdenamiento);
	}

	public void ordenar(List<T> arreglo) {
		boolean huboCambio;
		do {
			huboCambio = false;
			for (int i = 0; i < arreglo.size() - 1; i++) {
				T actual = arreglo.get(i);
				T comparador = arreglo.get(i+1);
				cantComparaciones++;
				if (actual.compareTo(comparador) > 0) {
					intercambiar(arreglo, i, i + 1);
					huboCambio = true;
					cantOperaciones++;
				}
				jFrameOrdenador.display(i, i + 1);
				jFrameOrdenador.sleep();
			}
		} while (huboCambio);
	}

}