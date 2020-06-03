package ar.edu.unlam.entidades;

public class Elemento implements Comparable<Elemento> {

	private int valor;
	private ElementState state;

	public Elemento(int valor) {
		super();
		this.valor = valor;
		this.state = ElementState.INICIAL;
	}

	public int getValor() {
		return valor;
	}

	@Override
	public int compareTo(Elemento otroElemento) {
		return this.valor - otroElemento.getValor();
	}

	public ElementState getState() {
		return state;
	}

	public void setState(ElementState state) {
		this.state = state;
	}

}
