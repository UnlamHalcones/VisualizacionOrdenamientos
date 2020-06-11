package ar.edu.unlam.ordenamientos;

import java.util.List;

import ar.edu.unlam.graphics.Ordenador;

public class Insercion<T extends Comparable<T>> extends EstrategiaOrdenamiento<T> {

	public Insercion(Ordenador jFrameReference) {
		super(jFrameReference);
	}

	public void ordenar(List<T> arreglo) {
		this.setOperacion("Desplazamientos");
		for (int i = 1; i < arreglo.size(); i++) {
			T valorActual = arreglo.get(i);
			int j = i - 1;
			while (j >= 0 && arreglo.get(j).compareTo(valorActual) > 0 ) {
				this.cantComparaciones++;
				this.cantOperaciones++;
				arreglo.set(j + 1, arreglo.get(j));
				
				jFrameOrdenador.display(j,i, i-1);
				jFrameOrdenador.sleep();
				j = j - 1;
			}
			if(j+1!=0 && arreglo.get(j).compareTo(valorActual) <= 0 ) {
				this.cantComparaciones++;
				  }
			arreglo.set(j + 1, valorActual);
			
		}
	}

}