package ar.edu.unlam.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

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

	 private static Ordenador _instance;
	
	private int loops = 0;

	private List<Elemento> elementosAOrdenar;
	private PanelOrdenador panelOrdenador;

	private int tiempoDemoraEntreOperacion;
	private int cantidadElementos;
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
		this.cantidadElementos = cantidadElementos;
		this.estrategiaOrdenamiento = mapaEstrategiaOrdenamiento.get(metodoOrdenamiento);

		this.elementosAOrdenar = GeneradorDeDatos.generarDatos(casoOrdenamiento, cantidadElementos);
		
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
	}
	
	   public static Ordenador getInstance() {

		      if(_instance == null) {
		         _instance = new Ordenador(450, 100, CasoOrdenamiento.ALEATORIO, MetodoOrdenamiento.QUICKSORT);
		      }

		       return _instance;
		   }


	
	public void init() {
		panelOrdenador = new PanelOrdenador();
		add(panelOrdenador);
		
		createMenuBar();
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setFocusable(true);
		requestFocusInWindow();
	}
	
    private void createMenuBar() {

    	JMenuBar menuBar = new JMenuBar();
    	
    	//Algorithm Menu
    	JMenu algorithmsMenu = new JMenu("Sorting Algorithms");

    	ButtonGroup algorithmsGroup = new ButtonGroup();

    	JRadioButtonMenuItem burbujeoRMenuItem = new JRadioButtonMenuItem(MetodoOrdenamiento.BURBUJEO.name());
        burbujeoRMenuItem.setSelected(true);
        algorithmsMenu.add(burbujeoRMenuItem);

        burbujeoRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                estrategiaOrdenamiento = mapaEstrategiaOrdenamiento.get(MetodoOrdenamiento.BURBUJEO);
            }
        });

        JRadioButtonMenuItem insercionRMenuItem = new JRadioButtonMenuItem(MetodoOrdenamiento.INSERCION.name());
        algorithmsMenu.add(insercionRMenuItem);

        insercionRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	estrategiaOrdenamiento = mapaEstrategiaOrdenamiento.get(MetodoOrdenamiento.INSERCION);
            }
        });

        JRadioButtonMenuItem seleccionRMenuItem = new JRadioButtonMenuItem(MetodoOrdenamiento.SELECCION.name());
        algorithmsMenu.add(seleccionRMenuItem);

        seleccionRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	estrategiaOrdenamiento = mapaEstrategiaOrdenamiento.get(MetodoOrdenamiento.SELECCION);
            }
        });
        
        JRadioButtonMenuItem quicksortRMenuItem = new JRadioButtonMenuItem(MetodoOrdenamiento.QUICKSORT.name());
        algorithmsMenu.add(quicksortRMenuItem);

        quicksortRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	estrategiaOrdenamiento = mapaEstrategiaOrdenamiento.get(MetodoOrdenamiento.QUICKSORT);
            }
        });

        algorithmsGroup.add(burbujeoRMenuItem);
        algorithmsGroup.add(insercionRMenuItem);
        algorithmsGroup.add(seleccionRMenuItem);
        algorithmsGroup.add(quicksortRMenuItem);

        menuBar.add(algorithmsMenu);

        
    	//Complexity Menu
    	JMenu complexityMenu = new JMenu("Complexity");

    	ButtonGroup complexityGroup = new ButtonGroup();

    	JRadioButtonMenuItem aleatorioRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.ALEATORIO.name());
    	aleatorioRMenuItem.setSelected(true);
    	complexityMenu.add(aleatorioRMenuItem);

    	aleatorioRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	elementosAOrdenar = GeneradorDeDatos.generarDatos(CasoOrdenamiento.ALEATORIO, cantidadElementos);
            	panelOrdenador.repaint();
            }
        });

        JRadioButtonMenuItem ordenadoRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.ORDENADO.name());
        complexityMenu.add(ordenadoRMenuItem);

        ordenadoRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	elementosAOrdenar = GeneradorDeDatos.generarDatos(CasoOrdenamiento.ORDENADO, cantidadElementos);
            	panelOrdenador.repaint();            	
            }
        });

        JRadioButtonMenuItem invertidoRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.INVERTIDO.name());
        complexityMenu.add(invertidoRMenuItem);

        invertidoRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	elementosAOrdenar = GeneradorDeDatos.generarDatos(CasoOrdenamiento.INVERTIDO, cantidadElementos);
            	panelOrdenador.repaint();
            }
        });
        
        JRadioButtonMenuItem casiOrdenadoRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.CASI_ORDENADO.name());
        complexityMenu.add(casiOrdenadoRMenuItem);

        casiOrdenadoRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	elementosAOrdenar = GeneradorDeDatos.generarDatos(CasoOrdenamiento.CASI_ORDENADO, cantidadElementos);
            	panelOrdenador.repaint();
            }
        });

        JRadioButtonMenuItem casiInvertidioRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.CASI_INVERTIDO.name());
        complexityMenu.add(casiInvertidioRMenuItem);

        casiInvertidioRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	elementosAOrdenar = GeneradorDeDatos.generarDatos(CasoOrdenamiento.CASI_INVERTIDO, cantidadElementos);
            	panelOrdenador.repaint();
            }
        });
        
        complexityGroup.add(aleatorioRMenuItem);
        complexityGroup.add(ordenadoRMenuItem);
        complexityGroup.add(invertidoRMenuItem);
        complexityGroup.add(casiOrdenadoRMenuItem);
        complexityGroup.add(casiInvertidioRMenuItem);

        menuBar.add(complexityMenu);

        setJMenuBar(menuBar);
        
        
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
			g2.drawString("Intercambios: " + String.format("%6s", estrategiaOrdenamiento.getCantOperaciones()), 820, 25);

	   
			
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
			return new Dimension(1200, 450);
		}

	}

	public int getTiempoDemoraEntreOperacion() {
		return tiempoDemoraEntreOperacion;
	}

	public static void main(String[] args) throws Exception {

		//		Ordenador ordenador =  new Ordenador(450, 100, CasoOrdenamiento.ALEATORIO, MetodoOrdenamiento.QUICKSORT);
//		ordenador.init();
//		ordenador.run();
		
		Ordenador ordenador = Ordenador.getInstance();
		ordenador.init();
//		ordenador.run();

	}
	
	

	
}
