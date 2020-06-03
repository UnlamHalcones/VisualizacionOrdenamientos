package ar.edu.unlam.ordenamientos;

import java.util.List;

import javax.swing.JFrame;

import ar.edu.unlam.graphics.Ordenador;

public class Seleccion<T extends Comparable<T>> extends EstrategiaOrdenamiento<T> {

	public Seleccion(List<T> elementosAOrdenar, Ordenador jFrameReference) {
		super(elementosAOrdenar, jFrameReference);
	}

	public void ordenar(List<T> arreglo, Ordenador jFrameReference) {
		T menor;
		int i, j, posicionDelMenor;

		for (i = 0; i < arreglo.size() - 1; i++) {
			menor = arreglo.get(i);
			posicionDelMenor = i;
			for (j = i + 1; j < arreglo.size(); j++) {
				if (arreglo.get(j).compareTo(menor) < 0) {
					menor = arreglo.get(j);
					posicionDelMenor = j;
				}
			}
			if (posicionDelMenor != i) {
				intercambiar(arreglo, i, posicionDelMenor);
			}
			try {
				Thread.sleep(jFrameReference.getTiempoDemoraEntreOperacion());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jFrameReference.display();
		}
	}

}