package ar.edu.unlam.ordenamientos;

import java.util.List;

import ar.edu.unlam.graphics.Ordenador;

public class QuickSort<T extends Comparable<T>> extends EstrategiaOrdenamiento<T> {

	public QuickSort(Ordenador jFrameReference) {
		super(jFrameReference);
	}

	@Override
	public void ordenar(List<T> arreglo) {
		ordenar(arreglo, 0, arreglo.size() - 1);
	}

	private void ordenar(List<T> arreglo, int inferior, int superior) {
		if (inferior < superior) {
			int pivot = partition(arreglo, inferior, superior);

			ordenar(arreglo, inferior, pivot - 1);
			ordenar(arreglo, pivot + 1, superior);
		}
	}

	private int partition(List<T> arreglo, int inferior, int superior) {
		T pivot = arreglo.get(superior);
		int i = (inferior - 1);
		
		jFrameOrdenador.display(inferior, superior); 
		jFrameOrdenador.sleep();
		
		for (int j = inferior; j < superior; j++) {
			cantComparaciones++;
			if (arreglo.get(j).compareTo(pivot) < 0) {
				i++;
				intercambiar(arreglo, i, j);
				cantOperaciones++;
			}
		}
		intercambiar(arreglo, i + 1, superior);

		return i + 1;
	}
}
