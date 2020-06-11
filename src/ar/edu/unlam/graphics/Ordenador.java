package ar.edu.unlam.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ar.edu.unlam.entidades.CasoOrdenamiento;
import ar.edu.unlam.entidades.ElementState;
import ar.edu.unlam.entidades.Elemento;
import ar.edu.unlam.entidades.MetodoOrdenamiento;
import ar.edu.unlam.generador.datos.GeneradorDeDatos;
import ar.edu.unlam.ordenamientos.*;

@SuppressWarnings("rawtypes")
public class Ordenador extends JFrame implements Runnable {

	private final int SECOND = 1000;
	private final int TICKS_PER_SECOND = 1000;
	private final int SKIP_TICKS = SECOND / TICKS_PER_SECOND;
	private final int WIDTH = 1200;
	private final int HEIGHT = 450;

	private int loops = 0;

	private List<Elemento> elementosAOrdenar;
	private PanelOrdenador panelOrdenador;

	private int tiempoDemoraEntreOperacion;

	private EstrategiaOrdenamiento estrategiaOrdenamiento;

	private Map<MetodoOrdenamiento, EstrategiaOrdenamiento> mapaEstrategiaOrdenamiento;

	public Ordenador(int cantidadElementos, int tiempoDemoraEntreOperacion, CasoOrdenamiento casoOrdenamiento,
			MetodoOrdenamiento metodoOrdenamiento) {

		mapaEstrategiaOrdenamiento = new HashMap<>();
		mapaEstrategiaOrdenamiento.put(MetodoOrdenamiento.QUICKSORT, new QuickSort(this));
		mapaEstrategiaOrdenamiento.put(MetodoOrdenamiento.BURBUJEO, new Burbujeo(this));
		mapaEstrategiaOrdenamiento.put(MetodoOrdenamiento.INSERCION, new Insercion(this));
		mapaEstrategiaOrdenamiento.put(MetodoOrdenamiento.SELECCION, new Seleccion(this));

		this.tiempoDemoraEntreOperacion = tiempoDemoraEntreOperacion;

		this.estrategiaOrdenamiento = mapaEstrategiaOrdenamiento.get(metodoOrdenamiento);

		this.elementosAOrdenar = GeneradorDeDatos.generarDatos(casoOrdenamiento, cantidadElementos);

		setSize(WIDTH, HEIGHT);
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
			g2.drawString(estrategiaOrdenamiento.getOperacion() + ": " + String.format("%6s", estrategiaOrdenamiento.getCantOperaciones()), 820, 25);

			double i = 0;

			double anchoBarra = currentDimension.getWidth() / elementosAOrdenar.size();
			
			for (Elemento elemento : elementosAOrdenar) {
				AffineTransform old = g2.getTransform();

				g2.translate(0, getHeight() - 1);
				g2.scale(1, -1);

				g2.setColor(ElementState.getColorByState(elemento.getState()));
				Rectangle2D.Double rect = new Rectangle2D.Double(i, 0, anchoBarra, elemento.getValor());
				g2.fill(rect);

				g2.setTransform(old);

				i += anchoBarra;
			}

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(1200, 800);
		}

	}

	public int getTiempoDemoraEntreOperacion() {
		return tiempoDemoraEntreOperacion;
	}

	public static void main(String[] args) throws Exception {
		Ordenador ordenador = new Ordenador(500, 10, CasoOrdenamiento.ALEATORIO, MetodoOrdenamiento.INSERCION);
		ordenador.init();
		ordenador.run();
	}
}