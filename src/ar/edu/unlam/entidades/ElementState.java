package ar.edu.unlam.entidades;

import java.awt.Color;

public enum ElementState {
	ACTUAL(Color.RED), COMPARADOR(Color.YELLOW), ORDENADO(Color.GREEN), INICIAL(Color.WHITE);

	private Color color;

	private ElementState(Color color) {
		this.color = color;
	}

	public static Color getColorByState(ElementState state) {
		Color returnValue = INICIAL.color;
		for (ElementState stateInEnum : values()) {
			if (stateInEnum.equals(state)) {
				returnValue = stateInEnum.color;
			}
		}
		return returnValue;
	}

}
