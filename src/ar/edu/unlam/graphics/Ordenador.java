package ar.edu.unlam.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ar.edu.unlam.entidades.CasoOrdenamiento;
import ar.edu.unlam.entidades.ElementState;
import ar.edu.unlam.entidades.Elemento;
import ar.edu.unlam.entidades.MetodoOrdenamiento;
import ar.edu.unlam.ordenamientos.Burbujeo;
import ar.edu.unlam.ordenamientos.EstrategiaOrdenamiento;

@SuppressWarnings("rawtypes")
public class Ordenador extends JFrame implements Runnable {

	// OJO: Los valores de SKIP son un resultado de una división entera!
	private final int SECOND = 1000;
	private final int TICKS_PER_SECOND = 1000;
	private final int SKIP_TICKS = SECOND / TICKS_PER_SECOND;

	private int loops = 0;

	private List<Elemento> elementosAOrdenar;
	private PanelOrdenador panelOrdenador;

	private Integer cantidadComparaciones;
	private Integer cantidadIntercambios;

	private int tiempoDemoraEntreOperacion;
	private CasoOrdenamiento casoOrdenamiento;

	private EstrategiaOrdenamiento estrategiaOrdenamiento;
	
	private int cantidadElementos;

	public Ordenador(int cantidadElementos, int tiempoDemoraEntreOperacion, CasoOrdenamiento casoOrdenamiento,
			MetodoOrdenamiento metodoOrdenamiento) {

		this.cantidadElementos = cantidadElementos;

		this.tiempoDemoraEntreOperacion = tiempoDemoraEntreOperacion;
		this.casoOrdenamiento = casoOrdenamiento;
		
		generarDatosAOrdernar();

		if (metodoOrdenamiento.equals(MetodoOrdenamiento.BURBUJEO))
			this.estrategiaOrdenamiento = new Burbujeo(this);

		setSize(1200, 450);
		setLocationRelativeTo(null);
	}

	public void init() {
		panelOrdenador = new PanelOrdenador();
		add(panelOrdenador);

		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setFocusable(true);
		requestFocusInWindow();
	}

	@Override
	public void run() {

		long next_game_tick = System.currentTimeMillis();

		Thread hiloOrdenamiento = new Thread(() -> estrategiaOrdenamiento.ordenar(elementosAOrdenar));
		// Corro el hilo de ordenamiento de manera asincrónica
		CompletableFuture<Void> futureOrdenamiento = CompletableFuture.runAsync(hiloOrdenamiento);

		do {
			if (System.currentTimeMillis() > next_game_tick) {
				loops++;
				next_game_tick += SKIP_TICKS;
			}
		} while (!futureOrdenamiento.isDone());
		display();
	}

	public void display(){
		elementosAOrdenar.forEach(elemento -> elemento.setState(ElementState.ORDENADO));
		panelOrdenador.repaint();
	}
	
	private void generarDatosAOrdernar() {
		elementosAOrdenar = new LinkedList<>();
		
		switch (casoOrdenamiento) {
			
			case ALEATORIO:
			default:
				//En vez de generarlo con un random, meto los elementos en la lista y hago un shuffle. Es una forma facil de hacer que no tengamos repetidos
				for (int i = 1; i < cantidadElementos + 1; ++i) {
					elementosAOrdenar.add(new Elemento(i));
				}
				
				Collections.shuffle(elementosAOrdenar);
				break;
			case CASI_INVERTIDO:
				
				for (int i = cantidadElementos + 1; i > 0; --i) {
					
					//Con estos nos aseguramos que est� un poco desordenado. Se supone que va a estar 33% deordenado
					if (ThreadLocalRandom.current().nextInt(0, 3) == 1) {
						elementosAOrdenar.add(new Elemento((i-1)));
						elementosAOrdenar.add(new Elemento(i));
						i--;
					}
					else {
						elementosAOrdenar.add(new Elemento(i));	
					}
				}
	
				break;
			case CASI_ORDENADO:
				for (int i = 1; i < cantidadElementos + 1 ; ++i) {
					
					//Con estos nos aseguramos que est� un poco desordenado. Se supone que va a estar 33% deordenado
					if (ThreadLocalRandom.current().nextInt(0, 3) == 1) {
						elementosAOrdenar.add(new Elemento((i+1)));
						elementosAOrdenar.add(new Elemento(i));
						i++;
					}
					else {
						elementosAOrdenar.add(new Elemento(i));	
					}
				}
				
				break;
			case INVERTIDO:
				for (int i = cantidadElementos + 1; i > 0; --i) {
					elementosAOrdenar.add(new Elemento(i));
				}
				break;
			case ORDENADO:
				for (int i = 1; i < cantidadElementos + 1; ++i) {
					elementosAOrdenar.add(new Elemento(i));
				}	
				break;			
		
		}
	}

	public void display(int actual, int comparado) {
		resetElementsColor();
		elementosAOrdenar.get(actual).setState(ElementState.ACTUAL);
		elementosAOrdenar.get(comparado).setState(ElementState.COMPARADOR);
		panelOrdenador.repaint();
	}

	private void resetElementsColor() {
		elementosAOrdenar.forEach(element -> element.setState(ElementState.INICIAL));
	}

	public void sleep() {
		try {
			Thread.sleep(this.getTiempoDemoraEntreOperacion());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Clase usada para dibujar las barras de ordenamiento
	 */
	private class PanelOrdenador extends JPanel {

		public PanelOrdenador() {
			this.setBackground(Color.BLACK);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;

			Dimension currentDimension = getContentPane().getSize();

			g2.setFont(new Font("Dialog", Font.BOLD, 24));
			g2.drawString("Time: " + String.format("%6s", loops * SKIP_TICKS) + "ms", 20, 25);

			g2.setFont(new Font("Dialog", Font.BOLD, 24));
			g2.drawString("Comparaciones: " + String.format("%6s", estrategiaOrdenamiento.getCantComparaciones()), 420, 25);

			g2.setFont(new Font("Dialog", Font.BOLD, 24));
			g2.drawString("Intercambios: " + String.format("%6s", estrategiaOrdenamiento.getCantOperaciones()), 820, 25);

			int i = 0;

			int anchoBarra = (int) currentDimension.getWidth() / elementosAOrdenar.size();
			for (Elemento elemento : elementosAOrdenar) {
				g2.setColor(ElementState.getColorByState(elemento.getState()));
				AffineTransform old = g2.getTransform();

				g2.translate(0, getHeight() - 1);
				g2.scale(1, -1);

				g2.fillRect(i, 0, anchoBarra, elemento.getValor());

				g2.setTransform(old);

				i += anchoBarra;
			}

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(1200, 450);
		}

	}

	public int getTiempoDemoraEntreOperacion() {
		return tiempoDemoraEntreOperacion;
	}

	public static void main(String[] args) throws Exception {
		Ordenador ordenador = new Ordenador(100, 10, CasoOrdenamiento.INVERTIDO, MetodoOrdenamiento.BURBUJEO);
		ordenador.init();
		ordenador.run();
	}
}
