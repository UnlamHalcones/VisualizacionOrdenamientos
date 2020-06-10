package ar.edu.unlam.ordenamientos;

import ar.edu.unlam.graphics.Ordenador;

import java.util.List;

public abstract class EstrategiaOrdenamiento<T extends Comparable<T>> {



	Ordenador jFrameOrdenador;
	int cantComparaciones;
	int cantOperaciones;

	public EstrategiaOrdenamiento(Ordenador jFrameOrdenador) {
		this.jFrameOrdenador = jFrameOrdenador;
	}

	public abstract void ordenar(List<T> arreglo);

	protected void intercambiar(List<T> arreglo, int i, int j) {
		T temporal = arreglo.get(i);
		arreglo.set(i, arreglo.get(j));
		arreglo.set(j, temporal);
	}

	public int getCantComparaciones() {
		return cantComparaciones;
	}

	public void setCantComparaciones(int cantComparaciones) {
		this.cantComparaciones = cantComparaciones;
	}

	public int getCantOperaciones() {
		return cantOperaciones;
	}

	public void setCantOperaciones(int cantOperaciones) {
		this.cantOperaciones = cantOperaciones;
	}
}