package ar.edu.unlam.ordenamientos;

import java.util.List;

import ar.edu.unlam.entidades.Elemento;
import ar.edu.unlam.graphics.Ordenador;

public class Burbujeo<T extends Comparable<T>> extends EstrategiaOrdenamiento<Elemento> {

	public Burbujeo(List<Elemento> elementosAOrdenar, Ordenador jFrameReference) {
		super(elementosAOrdenar, jFrameReference);
	}

	void ordenar(List<Elemento> arreglo, Ordenador jFrameReference) {
		boolean huboCambio = false;
		do {
			huboCambio = false;
			for (int i = 0; i < arreglo.size() - 1; i++) {
				Elemento actual = arreglo.get(i);
				Elemento comparador = arreglo.get(i+1);
				if (actual.compareTo(comparador) > 0) {
					intercambiar(arreglo, i, i + 1);
					huboCambio = true;
				}
				jFrameReference.display();
			}
			try {
				Thread.sleep(jFrameReference.getTiempoDemoraEntreOperacion());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} while (huboCambio);
	}

}