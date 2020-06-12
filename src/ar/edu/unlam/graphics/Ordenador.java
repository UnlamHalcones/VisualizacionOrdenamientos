package ar.edu.unlam.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ar.edu.unlam.entidades.CasoOrdenamiento;
import ar.edu.unlam.entidades.ElementState;
import ar.edu.unlam.entidades.Elemento;
import ar.edu.unlam.entidades.MetodoOrdenamiento;
import ar.edu.unlam.generador.datos.GeneradorDeDatos;
import ar.edu.unlam.ordenamientos.*;
import ar.edu.unlam.generador.datos.ManejadorArchivos;

@SuppressWarnings("rawtypes")
public class Ordenador extends JFrame implements Runnable {

	private final int SECOND = 1000;
	private final int TICKS_PER_SECOND = 1000;
	private final int SKIP_TICKS = SECOND / TICKS_PER_SECOND;
	private final int WIDTH = 1200;
	private final int HEIGHT = 450;

	private static Ordenador _instance;
	Thread hilo;
	Thread hiloOrdenamiento;
	
	private int loops = 0;
	
	private CasoOrdenamiento casoOrdenamiento;
	
	private List<Elemento> elementosAOrdenar;
	
	private JSplitPane panelContainer;
	private PanelOrdenador panelOrdenador;
	private PanelConfigurador panelConfigurador;

	private CasoOrdenamiento casoDeOrdenamiento;
	private MetodoOrdenamiento metodoOrdenamiento;
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
		this.casoDeOrdenamiento = casoOrdenamiento;
		this.metodoOrdenamiento = metodoOrdenamiento;
		this.elementosAOrdenar = GeneradorDeDatos.generarDatos(casoOrdenamiento, cantidadElementos);
		
		this.casoOrdenamiento=casoOrdenamiento;
		
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
	}
	
	public static Ordenador getInstance() {

		      if(_instance == null) {
		         _instance = new Ordenador(250, 100, CasoOrdenamiento.ALEATORIO, MetodoOrdenamiento.BURBUJEO);
		      }

		       return _instance;
		   }
	
	public void init() {
		
		panelContainer = new JSplitPane();
		
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(panelContainer);            

        panelContainer.setOrientation(JSplitPane.VERTICAL_SPLIT); 
        panelContainer.setDividerLocation(600);  
        panelContainer.setEnabled(false);
		
		this.panelOrdenador = new PanelOrdenador();
		panelContainer.setTopComponent(panelOrdenador);
		
		this.panelConfigurador = new PanelConfigurador(panelOrdenador);
		panelContainer.setBottomComponent(panelConfigurador);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setFocusable(true);
		setResizable(false);
		requestFocusInWindow();
	}

	@Override
	public void run() {

		long next_game_tick = System.currentTimeMillis();
		
		hiloOrdenamiento = new Thread(() -> estrategiaOrdenamiento.ordenar(elementosAOrdenar));
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
		guardarRegistro();
	}

	private void enableItems() {
		panelConfigurador.runButton.setEnabled(true);
		panelConfigurador.runAllButton.setEnabled(true);
		panelConfigurador.stopButton.setEnabled(false);
		panelConfigurador.algoritmoCB.setEnabled(true);
		panelConfigurador.elementosSlider.setEnabled(true);
		panelConfigurador.velocidadSlider.setEnabled(true);
		panelConfigurador.casoOrdenamientoCB.setEnabled(true);
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
	
	public void guardarRegistro () {
		
		String linea = this.estrategiaOrdenamiento.getClass().toString().substring(33).toLowerCase()+";"+this.casoOrdenamiento.toString().toLowerCase()+";"+this.elementosAOrdenar.size()+";"+loops * SKIP_TICKS*0.001+" seg";
		ManejadorArchivos.agregarRegistro(linea);
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
			g2.drawString("Tiempo: " + String.format("%6s", loops * SKIP_TICKS) + "ms", 20, 25);

			g2.setFont(new Font("Dialog", Font.BOLD, 24));
			g2.drawString("Comparaciones: " + String.format("%6s", estrategiaOrdenamiento.getCantComparaciones()), 420, 25);

			g2.setFont(new Font("Dialog", Font.BOLD, 24));
			g2.drawString("Intercambios: " + String.format("%6s", estrategiaOrdenamiento.getCantOperaciones()), 820, 25);

	   
			g2.setFont(new Font("Dialog", Font.BOLD, 24));
			g2.drawString("Algoritmo: " + String.format("%-9s", metodoOrdenamiento), 20, 50);
			
			g2.setFont(new Font("Dialog", Font.BOLD, 24));
			g2.drawString("Caso: " + String.format("%-9s", casoDeOrdenamiento), 420, 50);

			
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
			return new Dimension(1500, 600);
		}

	}
	
	private class PanelConfigurador extends JPanel {
		JButton runButton;
		JButton runAllButton;
		JButton stopButton;
		
		private JSlider velocidadSlider;
		private final int VEL_MAX = 500;
		private final int VEL_MIN = 1;
		private final int VEL_DEF = 100;
		
		private JSlider elementosSlider;
		private final int TAM_MAX = 500;
		private final int TAM_MIN = 1;
		private final int TAM_DEF = 250;
		
		private JLabel velocidadDef;
		private JLabel tamanioDef;
		

		private JComboBox<String> algoritmoCB;
		private JComboBox<String> casoOrdenamientoCB;
		

		public PanelConfigurador(JPanel panelOrdenador){
			this.setBackground(Color.white);	
//			this.panelOrdenador = panelOrdenador;
			

			algoritmoCB = new JComboBox<String>();
			for(MetodoOrdenamiento s : MetodoOrdenamiento.values()) algoritmoCB.addItem(s.name());
			algoritmoCB.setSelectedItem("Burbujeo");
			algoritmoCB.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			        Ordenador ordenador = Ordenador.getInstance();
			        ordenador.metodoOrdenamiento = MetodoOrdenamiento.valueOf(algoritmoCB.getSelectedItem().toString());
			        ordenador.estrategiaOrdenamiento = ordenador.mapaEstrategiaOrdenamiento.get(ordenador.metodoOrdenamiento);
			        panelOrdenador.repaint();
			    }
			});
			
			casoOrdenamientoCB = new JComboBox<String>();
			for(CasoOrdenamiento s : CasoOrdenamiento.values()) casoOrdenamientoCB.addItem(s.name());
			casoOrdenamientoCB.setSelectedItem("ALEATORIO");
			casoOrdenamientoCB.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			        Ordenador ordenador = Ordenador.getInstance();
			        ordenador.casoDeOrdenamiento = CasoOrdenamiento.valueOf(casoOrdenamientoCB.getSelectedItem().toString());
					ordenador.elementosAOrdenar = GeneradorDeDatos.generarDatos(ordenador.casoDeOrdenamiento, ordenador.cantidadElementos);
					panelOrdenador.repaint();
			    }
			});
						
			velocidadSlider = new JSlider(VEL_MIN, VEL_MAX, VEL_DEF);
			elementosSlider = new JSlider(TAM_MIN, TAM_MAX, TAM_DEF);
			
			velocidadSlider.setMinorTickSpacing(10);
			velocidadSlider.setMajorTickSpacing(100);
			velocidadSlider.setPaintTicks(true);
			velocidadSlider.setOpaque(false);
			
			velocidadDef = new JLabel("Velocidad: "+ VEL_DEF +" ms");
			tamanioDef = new JLabel("Tama\F1o: "+ TAM_DEF +" elementos");
			
			velocidadSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					velocidadDef.setText(("Velocidad: " + Integer.toString(velocidadSlider.getValue()) + "ms"));
					validate();
					Ordenador.getInstance().tiempoDemoraEntreOperacion = velocidadSlider.getValue();
				}
			});
			
			elementosSlider.setMinorTickSpacing(10);
			elementosSlider.setMajorTickSpacing(100);
			elementosSlider.setPaintTicks(true);
			elementosSlider.setOpaque(false);
			
			elementosSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					tamanioDef.setText(("Tamanio: " + Integer.toString(elementosSlider.getValue()) + " elementos"));
					validate();
					Ordenador ordenador = Ordenador.getInstance();
					ordenador.cantidadElementos = elementosSlider.getValue();
					ordenador.elementosAOrdenar = GeneradorDeDatos.generarDatos(ordenador.casoDeOrdenamiento, ordenador.cantidadElementos);
					panelOrdenador.repaint();
				}
			});
			
		     runButton = new JButton("Start");
	            runButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                	
						Thread hilo = new Thread(new Runnable() {

							public void run() {
								restartComponents();
								validate();
								Ordenador ordenador = Ordenador.getInstance();
								ordenador.elementosAOrdenar = GeneradorDeDatos
										.generarDatos(ordenador.casoDeOrdenamiento, ordenador.cantidadElementos);
								Ordenador.getInstance().run();
							}

							private void restartComponents() {
								runButton.setEnabled(false);
								stopButton.setEnabled(true);
								elementosSlider.setEnabled(false);
								velocidadSlider.setEnabled(false);
								algoritmoCB.setEnabled(false);
								casoOrdenamientoCB.setEnabled(false);
								loops = 0;
								estrategiaOrdenamiento.setCantComparaciones(0);
								estrategiaOrdenamiento.setCantOperaciones(0);
							}
						});
						hilo.start();
	                }
	            });
	            
	            runAllButton = new JButton("Start All");
	            	runAllButton.addActionListener(new ActionListener() {
		                public void actionPerformed(ActionEvent e) {
		                	
							Thread hilo = new Thread(new Runnable() {

								public void run() {
									for(MetodoOrdenamiento s : MetodoOrdenamiento.values()) {
										restartComponents();
										validate();
										Ordenador ordenador = Ordenador.getInstance();
										ordenador.metodoOrdenamiento = s; 
										ordenador.elementosAOrdenar = GeneradorDeDatos
												.generarDatos(ordenador.casoDeOrdenamiento, ordenador.cantidadElementos);
										Ordenador.getInstance().run();
									}
								}

								private void restartComponents() {
									runAllButton.setEnabled(false);
									runButton.setEnabled(false);
									stopButton.setEnabled(true);
									elementosSlider.setEnabled(false);
									velocidadSlider.setEnabled(false);
									algoritmoCB.setEnabled(false);
									casoOrdenamientoCB.setEnabled(false);
									loops = 0;
									estrategiaOrdenamiento.setCantComparaciones(0);
									estrategiaOrdenamiento.setCantOperaciones(0);
								}
							});
							hilo.start();
		                }
		            });
	            
	            stopButton = new JButton("Stop");
	            stopButton.setEnabled(false);
	            stopButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							hiloOrdenamiento.sleep(100);
							hilo.sleep(100);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
	            
	            this.add(algoritmoCB);
	            this.add(casoOrdenamientoCB);
	    		this.add(tamanioDef);
	    		this.add(elementosSlider);
	    		this.add(velocidadDef);
	    		this.add(velocidadSlider);
	            this.add(runButton);
	            this.add(runAllButton);
	            this.add(stopButton);
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
