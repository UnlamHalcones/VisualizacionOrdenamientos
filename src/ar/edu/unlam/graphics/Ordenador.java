package ar.edu.unlam.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

	private CasoOrdenamiento casoDeOrdenamiento;
	private int tiempoDemoraEntreOperacion;
	private int cantidadElementos;
	private EstrategiaOrdenamiento estrategiaOrdenamiento;

	private Map<MetodoOrdenamiento, EstrategiaOrdenamiento> mapaEstrategiaOrdenamiento;

	JMenu algorithmsMenu;
	JMenu complexityMenu;
	JMenu itemsMenu;
	

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
		this.casoDeOrdenamiento = casoOrdenamiento;

		this.elementosAOrdenar = GeneradorDeDatos.generarDatos(casoOrdenamiento, cantidadElementos);
		
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
	}
	
	public static Ordenador getInstance() {

		      if(_instance == null) {
		         _instance = new Ordenador(500, 100, CasoOrdenamiento.ALEATORIO, MetodoOrdenamiento.BURBUJEO);
		      }

		       return _instance;
		   }
	
	public void init() {
		this.panelOrdenador = new PanelOrdenador();
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
    	 algorithmsMenu = new JMenu("Algoritmos");
    	
    	ButtonGroup algorithmsGroup = new ButtonGroup();

    	JRadioButtonMenuItem burbujeoRMenuItem = new JRadioButtonMenuItem(MetodoOrdenamiento.BURBUJEO.name());
        burbujeoRMenuItem.setSelected(true);
        algorithmsMenu.add(burbujeoRMenuItem);

        burbujeoRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.estrategiaOrdenamiento = this.mapaEstrategiaOrdenamiento.get(MetodoOrdenamiento.BURBUJEO);
            }
        });

        JRadioButtonMenuItem insercionRMenuItem = new JRadioButtonMenuItem(MetodoOrdenamiento.INSERCION.name());
        algorithmsMenu.add(insercionRMenuItem);

        insercionRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.estrategiaOrdenamiento = this.mapaEstrategiaOrdenamiento.get(MetodoOrdenamiento.INSERCION);
            }
        });

        JRadioButtonMenuItem seleccionRMenuItem = new JRadioButtonMenuItem(MetodoOrdenamiento.SELECCION.name());
        algorithmsMenu.add(seleccionRMenuItem);

        seleccionRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.estrategiaOrdenamiento = this.mapaEstrategiaOrdenamiento.get(MetodoOrdenamiento.SELECCION);
            }
        });
        
        JRadioButtonMenuItem quicksortRMenuItem = new JRadioButtonMenuItem(MetodoOrdenamiento.QUICKSORT.name());
        algorithmsMenu.add(quicksortRMenuItem);

        quicksortRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.estrategiaOrdenamiento = this.mapaEstrategiaOrdenamiento.get(MetodoOrdenamiento.QUICKSORT);
            }
        });

        algorithmsGroup.add(burbujeoRMenuItem);
        algorithmsGroup.add(insercionRMenuItem);
        algorithmsGroup.add(seleccionRMenuItem);
        algorithmsGroup.add(quicksortRMenuItem);

        menuBar.add(algorithmsMenu);

        
    	//Complexity Menu
    	complexityMenu = new JMenu("Complejidad");

    	ButtonGroup complexityGroup = new ButtonGroup();

    	JRadioButtonMenuItem aleatorioRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.ALEATORIO.name());
    	aleatorioRMenuItem.setSelected(true);
    	complexityMenu.add(aleatorioRMenuItem);

    	aleatorioRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.casoDeOrdenamiento = CasoOrdenamiento.ALEATORIO;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint();
            }
        });

        JRadioButtonMenuItem ordenadoRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.ORDENADO.name());
        complexityMenu.add(ordenadoRMenuItem);

        ordenadoRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.casoDeOrdenamiento = CasoOrdenamiento.ORDENADO;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint(); 
            }
        });

        JRadioButtonMenuItem invertidoRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.INVERTIDO.name());
        complexityMenu.add(invertidoRMenuItem);

        invertidoRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.casoDeOrdenamiento = CasoOrdenamiento.INVERTIDO;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint();
            }
        });
        
        JRadioButtonMenuItem casiOrdenadoRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.CASI_ORDENADO.name());
        complexityMenu.add(casiOrdenadoRMenuItem);

        casiOrdenadoRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.casoDeOrdenamiento = CasoOrdenamiento.CASI_ORDENADO;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint();
            }
        });

        JRadioButtonMenuItem casiInvertidioRMenuItem = new JRadioButtonMenuItem(CasoOrdenamiento.CASI_INVERTIDO.name());
        complexityMenu.add(casiInvertidioRMenuItem);

        casiInvertidioRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.casoDeOrdenamiento = CasoOrdenamiento.CASI_INVERTIDO;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint();
            }
        });
        
        complexityGroup.add(aleatorioRMenuItem);
        complexityGroup.add(ordenadoRMenuItem);
        complexityGroup.add(invertidoRMenuItem);
        complexityGroup.add(casiOrdenadoRMenuItem);
        complexityGroup.add(casiInvertidioRMenuItem);

        menuBar.add(complexityMenu);

      //ItemsQty Menu
    	itemsMenu = new JMenu("Elementos");

    	ButtonGroup itemsGroup = new ButtonGroup();

    	JRadioButtonMenuItem diezRMenuItem = new JRadioButtonMenuItem("10");
    	itemsMenu.add(diezRMenuItem);

    	diezRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.cantidadElementos = 10;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint();
            }
        });

    	JRadioButtonMenuItem cincuentaRMenuItem = new JRadioButtonMenuItem("50");
    	itemsMenu.add(cincuentaRMenuItem);

    	cincuentaRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.cantidadElementos = 50;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint();
            }
        });
    	
    	JRadioButtonMenuItem cienRMenuItem = new JRadioButtonMenuItem("100");
    	itemsMenu.add(cienRMenuItem);

    	cienRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.cantidadElementos = 100;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint();
            }
        });
    	
    	JRadioButtonMenuItem dcRMenuItem = new JRadioButtonMenuItem("250");
    	itemsMenu.add(dcRMenuItem);

    	dcRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.cantidadElementos = 250;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint();
            }
        });

    	JRadioButtonMenuItem quinientosRMenuItem = new JRadioButtonMenuItem("500");
    	quinientosRMenuItem.setSelected(true);
    	itemsMenu.add(quinientosRMenuItem);

    	quinientosRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.cantidadElementos = 500;
            	this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);
            	this.panelOrdenador.repaint();
            }
        });
        
    	itemsGroup.add(diezRMenuItem);
        itemsGroup.add(cincuentaRMenuItem);
        itemsGroup.add(cienRMenuItem);
        itemsGroup.add(dcRMenuItem);
        itemsGroup.add(quinientosRMenuItem);
        
        menuBar.add(itemsMenu);
        
        //ItemsQty Menu
    	JMenu operationsMenu = new JMenu("Tiempo entre Operaciones");

    	ButtonGroup operationsGroup = new ButtonGroup();

    	JRadioButtonMenuItem aRMenuItem = new JRadioButtonMenuItem("10ms");
    	operationsMenu.add(aRMenuItem);

    	aRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.tiempoDemoraEntreOperacion = 10;
            }
        });
    	
    	JRadioButtonMenuItem bRMenuItem = new JRadioButtonMenuItem("50ms");
    	operationsMenu.add(bRMenuItem);

    	bRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.tiempoDemoraEntreOperacion = 50;
            }
        });
    	
    	JRadioButtonMenuItem cRMenuItem = new JRadioButtonMenuItem("100ms");
    	cRMenuItem.setSelected(true);
    	operationsMenu.add(cRMenuItem);

    	cRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.tiempoDemoraEntreOperacion = 100;
            }
        });
    	
    	JRadioButtonMenuItem dRMenuItem = new JRadioButtonMenuItem("200ms");
    	operationsMenu.add(dRMenuItem);

    	dRMenuItem.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            	this.tiempoDemoraEntreOperacion = 200;
            }
        });
        
    	operationsGroup.add(aRMenuItem);
    	operationsGroup.add(bRMenuItem);
    	operationsGroup.add(cRMenuItem);
    	operationsGroup.add(dRMenuItem);
        
        menuBar.add(operationsMenu);
    	
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
		enableItems();
		display();
	}

	private void enableItems() {
		panelOrdenador.runButton.setEnabled(true);
		algorithmsMenu.setEnabled(true);
		complexityMenu.setEnabled(true);
		itemsMenu.setEnabled(true);
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

		JButton runButton;
		
		public PanelOrdenador() {
			this.setBackground(Color.BLACK);		

		      runButton = new JButton("Start");
	            runButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    new Thread(new Runnable() {
	                        public void run() {       	
	                        	restartComponents();
	                            Ordenador.getInstance().run();
	                        }

							private void restartComponents() {
								runButton.setEnabled(false);
	                    		algorithmsMenu.setEnabled(false);
	                    		complexityMenu.setEnabled(false);
	                    		itemsMenu.setEnabled(false);
	                        	loops = 0;
	                        	estrategiaOrdenamiento.setCantComparaciones(0);
	                        	estrategiaOrdenamiento.setCantOperaciones(0);
							}
	                    }).start();
	                }
	            });
	            this.add(runButton);	
			
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
		
		Ordenador ordenador = Ordenador.getInstance();
		ordenador.init();

	}
	
	

	
}
