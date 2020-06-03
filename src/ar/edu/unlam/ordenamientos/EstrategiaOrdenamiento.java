package ar.edu.unlam.ordenamientos;

import java.util.List;
import ar.edu.unlam.entidades.*;
import ar.edu.unlam.graphics.Ordenador;

public abstract class EstrategiaOrdenamiento<Elemento extends Comparable<Elemento>> implements Runnable{

	List<Elemento> elementosAOdenar;
	Ordenador jFrameReference;
	boolean running;
	
	public EstrategiaOrdenamiento(List<Elemento> elementosAOrdenar, Ordenador jFrameReference ) {
		this.elementosAOdenar = elementosAOrdenar;
		this.jFrameReference = jFrameReference;
	}
	
	abstract void ordenar(List<Elemento> arreglo, Ordenador jFrameReference);

	protected void intercambiar(List<Elemento> arreglo, int i, int j) {
		Elemento temporal = arreglo.get(i);
		arreglo.set(i, arreglo.get(j));
		arreglo.set(j, temporal);
	}

	@Override
	public void run() {
		running = true;
		this.ordenar(elementosAOdenar, jFrameReference);
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
}