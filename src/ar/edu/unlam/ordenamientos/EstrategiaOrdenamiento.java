package ar.edu.unlam.ordenamientos;

import ar.edu.unlam.graphics.Ordenador;

import java.util.List;

public abstract class EstrategiaOrdenamiento<T extends Comparable<T>> {

	Ordenador jFrameOrdenador;
	int cantComparaciones;
	int cantOperaciones;
	private String operacion = "Intercambios";

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

	public int getCantOperaciones() {
		return cantOperaciones;
	}
	
	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
}