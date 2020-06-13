package ar.edu.unlam.graphics;

import ar.edu.unlam.entidades.CasoOrdenamiento;
import ar.edu.unlam.entidades.ElementState;
import ar.edu.unlam.entidades.Elemento;
import ar.edu.unlam.entidades.MetodoOrdenamiento;
import ar.edu.unlam.generador.datos.GeneradorDeDatos;
import ar.edu.unlam.generador.datos.ManejadorArchivos;
import ar.edu.unlam.ordenamientos.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Ordenador extends JFrame implements Runnable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6289028896008795642L;
	private static Ordenador _instance;
    private final int SECOND = 1000;
    private final int TICKS_PER_SECOND = 1000;
    private final int SKIP_TICKS = SECOND / TICKS_PER_SECOND;
    private final int WIDTH = 1200;
    private final int HEIGHT = 450;
    private int loops = 0;


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

    public Ordenador() {

        mapaEstrategiaOrdenamiento = new HashMap<>();
        mapaEstrategiaOrdenamiento.put(MetodoOrdenamiento.QUICKSORT, new QuickSort(this));
        mapaEstrategiaOrdenamiento.put(MetodoOrdenamiento.BURBUJEO, new Burbujeo(this));
        mapaEstrategiaOrdenamiento.put(MetodoOrdenamiento.INSERCION, new Insercion(this));
        mapaEstrategiaOrdenamiento.put(MetodoOrdenamiento.SELECCION, new Seleccion(this));

        this.tiempoDemoraEntreOperacion = 100;
        this.cantidadElementos = 250;
        this.metodoOrdenamiento = MetodoOrdenamiento.BURBUJEO;
        this.casoDeOrdenamiento = CasoOrdenamiento.ALEATORIO;
        this.estrategiaOrdenamiento = mapaEstrategiaOrdenamiento.get(this.metodoOrdenamiento);
        this.elementosAOrdenar = GeneradorDeDatos.generarDatos(this.casoDeOrdenamiento, this.cantidadElementos);

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
    }

    public static Ordenador getInstance() {

        if (_instance == null) {
            _instance = new Ordenador();
        }

        return _instance;
    }

    public static void main(String[] args) throws Exception {

        Ordenador ordenador = Ordenador.getInstance();
        ordenador.init();

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
        panelConfigurador.disableComponents();

        long next_game_tick = System.currentTimeMillis();

        Thread hiloOrdenamiento = new Thread(() -> estrategiaOrdenamiento.ordenar(elementosAOrdenar));
        hiloOrdenamiento.start();

        do {
            if (System.currentTimeMillis() > next_game_tick) {
                loops++;
                next_game_tick += SKIP_TICKS;
            }
        } while (hiloOrdenamiento.isAlive());

        panelConfigurador.enableComponents();
        display();
        guardarRegistro();
    }

    public void display() {
        elementosAOrdenar.forEach(elemento -> elemento.setState(ElementState.ORDENADO));
        panelOrdenador.repaint();
    }

    /**
     * Se cambia el color de elemento actual y el elemento contra el que se compara
     * y se repinta el panel
     */
    public void display(int actual, int comparado) {
        resetElementsColor();
        elementosAOrdenar.get(actual).setState(ElementState.ACTUAL);
        elementosAOrdenar.get(comparado).setState(ElementState.COMPARADOR);
        panelOrdenador.repaint();
    }

    /**
     * Se reinicializan todos los elementos a su estado inicial
     */
    private void resetElementsColor() {
        elementosAOrdenar.forEach(element -> element.setState(ElementState.INICIAL));
    }

    public void sleep() {
        try {
            Thread.sleep(this.tiempoDemoraEntreOperacion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Se guarda un registro en un archivo CSV con la informacion del proceso de ordenamiento
     */
    public void guardarRegistro() {

        String linea = this.estrategiaOrdenamiento.getClass().toString().substring(33).toLowerCase() + ";" + this.casoDeOrdenamiento.toString().toLowerCase() + ";" + this.elementosAOrdenar.size() + ";" + loops * SKIP_TICKS * 0.001 + " seg";
        ManejadorArchivos.agregarRegistro(linea);
    }

    /**
     * Clase usada para dibujar las barras de ordenamiento
     */
    private class PanelOrdenador extends JPanel {

        /**
		 * 
		 */
		private static final long serialVersionUID = -7342941346106790043L;

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
            return new Dimension(1000, 600);
        }

    }

    private class PanelConfigurador extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = -7153792617729819826L;
		private final int VEL_MAX = 500;
        private final int VEL_MIN = 1;
        private final int VEL_DEF = 100;
        private final int TAM_MAX = 500;
        private final int TAM_MIN = 1;
        private final int TAM_DEF = 250;
        JButton runButton;
        JButton runAllButton;
        private JSlider velocidadSlider;
        private JSlider elementosSlider;
        private JLabel velocidadDef;
        private JLabel tamanioDef;


        private JComboBox<String> algoritmoCB;
        private JComboBox<String> casoOrdenamientoCB;


        public PanelConfigurador(JPanel panelOrdenador) {
            this.setBackground(Color.white);

            creacionComboCasosOrdenamiento(panelOrdenador);
            creacionSliderCantElementos(panelOrdenador);
            creacionComboOrdenamientos();
            creacionSliderTiempo();

            creacionBotonStart();

            creacionBotonStartAll();

            this.add(algoritmoCB);
            this.add(casoOrdenamientoCB);
            this.add(tamanioDef);
            this.add(elementosSlider);
            this.add(velocidadDef);
            this.add(velocidadSlider);
            this.add(runButton);
            this.add(runAllButton);
        }

        /**
         * Se crea el boton encargado de disparar el proceso de TODAS las estrategias de ordenamiento disponibles
         * con el Caso de ordenamiento seleccionado
         */
        private void creacionBotonStartAll() {
            runAllButton = new JButton("Start All");
            runAllButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        public void run() {
                            for (MetodoOrdenamiento s : MetodoOrdenamiento.values()) {
                                iniciarOrdenador(s);
                            }
                        }
                    }).start();
                }
            });
        }

        /**
         * Se crear el boton encargado de disparar el proceso de ordenamiento
         */
        private void creacionBotonStart() {
            runButton = new JButton("Start");
            runButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        public void run() {
                            iniciarOrdenador(Ordenador.getInstance().metodoOrdenamiento);
                        }
                    }).start();
                }
            });
        }

        /**
         * Se generan los datos a ordenar y se inicializa el thread
         *
         * @param metodoOrdenamiento metodo de ordenamiento que se va a realizar
         */
        private void iniciarOrdenador(MetodoOrdenamiento metodoOrdenamiento) {
            validate();
            Ordenador ordenador = Ordenador.getInstance();
            ordenador.metodoOrdenamiento = metodoOrdenamiento;
            ordenador.estrategiaOrdenamiento = ordenador.mapaEstrategiaOrdenamiento.get(ordenador.metodoOrdenamiento);
            ordenador.elementosAOrdenar = GeneradorDeDatos
                    .generarDatos(ordenador.casoDeOrdenamiento, ordenador.cantidadElementos);
            Ordenador.getInstance().run();
        }

        /**
         * Se habilita la modificacion de todas las configuraciones
         */
        private void enableComponents() {
            this.runButton.setEnabled(true);
            this.runAllButton.setEnabled(true);
            this.algoritmoCB.setEnabled(true);
            this.elementosSlider.setEnabled(true);
            this.velocidadSlider.setEnabled(true);
            this.casoOrdenamientoCB.setEnabled(true);
        }

        /**
         * Se deshabilitan todas las configuraciones
         */
        private void disableComponents() {
            runButton.setEnabled(false);
            runAllButton.setEnabled(false);
            elementosSlider.setEnabled(false);
            velocidadSlider.setEnabled(false);
            algoritmoCB.setEnabled(false);
            casoOrdenamientoCB.setEnabled(false);
            loops = 0;
            estrategiaOrdenamiento.setCantComparaciones(0);
            estrategiaOrdenamiento.setCantOperaciones(0);
        }

        /**
         * Se crea el slider con el que se puede cambiar la cantidad de elementos a ordenar
         *
         * @param panelOrdenador
         */
        private void creacionSliderCantElementos(JPanel panelOrdenador) {
            tamanioDef = new JLabel("Tama\u00F1o: " + TAM_DEF + " elementos");
            elementosSlider = new JSlider(TAM_MIN, TAM_MAX, TAM_DEF);
            elementosSlider.setMinorTickSpacing(10);
            elementosSlider.setMajorTickSpacing(100);
            elementosSlider.setPaintTicks(true);
            elementosSlider.setOpaque(false);

            elementosSlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent arg0) {
                    tamanioDef.setText(("Tama\u00F1o: " + elementosSlider.getValue() + " elementos"));
                    validate();
                    Ordenador ordenador = Ordenador.getInstance();
                    ordenador.cantidadElementos = elementosSlider.getValue();
                    ordenador.elementosAOrdenar = GeneradorDeDatos.generarDatos(ordenador.casoDeOrdenamiento, ordenador.cantidadElementos);
                    panelOrdenador.repaint();
                }
            });
        }

        /**
         * Se crea el slider con el que se puede cambiar el tiempo de sleep entre operacion de ordenamiento
         */
        private void creacionSliderTiempo() {
            velocidadSlider = new JSlider(VEL_MIN, VEL_MAX, VEL_DEF);
            velocidadSlider.setMinorTickSpacing(10);
            velocidadSlider.setMajorTickSpacing(100);
            velocidadSlider.setPaintTicks(true);
            velocidadSlider.setOpaque(false);

            velocidadDef = new JLabel("Velocidad: " + VEL_DEF + " ms");

            velocidadSlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent arg0) {
                    velocidadDef.setText(("Velocidad: " + velocidadSlider.getValue() + "ms"));
                    validate();
                    Ordenador.getInstance().tiempoDemoraEntreOperacion = velocidadSlider.getValue();
                }
            });
        }

        /**
         * Se crea el combo con todos los casos de ordenamiento disponibles
         */
        private void creacionComboCasosOrdenamiento(JPanel panelOrdenador) {
            casoOrdenamientoCB = new JComboBox<>();
            for (CasoOrdenamiento s : CasoOrdenamiento.values()) casoOrdenamientoCB.addItem(s.name());
            casoOrdenamientoCB.setSelectedItem("ALEATORIO");
            casoOrdenamientoCB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Ordenador ordenador = Ordenador.getInstance();
                    ordenador.casoDeOrdenamiento = CasoOrdenamiento.valueOf(casoOrdenamientoCB.getSelectedItem().toString());
                    ordenador.elementosAOrdenar = GeneradorDeDatos.generarDatos(ordenador.casoDeOrdenamiento, ordenador.cantidadElementos);
                    panelOrdenador.repaint();
                }
            });
        }

        /**
         * Se crea el combo con todas las estrategias de ordenaimento disponibles y ademas
         */
        private void creacionComboOrdenamientos() {
            algoritmoCB = new JComboBox<>();
            for (MetodoOrdenamiento s : MetodoOrdenamiento.values()) {
                algoritmoCB.addItem(s.name());
            }
            algoritmoCB.setSelectedItem("Burbujeo");
            algoritmoCB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Ordenador ordenador = Ordenador.getInstance();
                    ordenador.metodoOrdenamiento = MetodoOrdenamiento.valueOf(algoritmoCB.getSelectedItem().toString());
                }
            });
        }
    }
}
