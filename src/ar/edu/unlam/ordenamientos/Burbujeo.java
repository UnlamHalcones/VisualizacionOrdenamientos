package ar.edu.unlam.ordenamientos;

import java.util.List;

import ar.edu.unlam.graphics.Ordenador;

public class Burbujeo<T extends Comparable<T>> extends EstrategiaOrdenamiento<T> {

	public Burbujeo(Ordenador jFrameOrdenamiento) {
		super(jFrameOrdenamiento);
	}

	public void ordenar(List<T> arreglo) {
		boolean huboCambio;
		int i;
		int j;
		for(i = 0; i < arreglo.size() -1; i++) {
			huboCambio = false;
			for ( j = 0; j < arreglo.size() - 1 - i; j++) {
				T actual = arreglo.get(j);
				T comparador = arreglo.get(j+1);
				cantComparaciones++;
				if (actual.compareTo(comparador) > 0) {
					intercambiar(arreglo, j, j + 1);
					huboCambio = true;
					cantOperaciones++;
				}
				jFrameOrdenador.display(j, j + 1);
				jFrameOrdenador.sleep();				
			}
			if(!huboCambio) break;
		}
	}

}