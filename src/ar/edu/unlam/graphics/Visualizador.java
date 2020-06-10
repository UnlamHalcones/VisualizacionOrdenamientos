package ar.edu.unlam.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ar.edu.unlam.entidades.CasoOrdenamiento;
import ar.edu.unlam.entidades.MetodoOrdenamiento;

@SuppressWarnings("serial")
public class Visualizador extends JFrame {

	private final int VEL_MAX = 1000;
	private final int VEL_MIN = 1;
	private final int TAM_MAX = 500;
	private final int TAM_MIN = 1;
	private final int VEL_DEF = 20;
	private final int TAM_DEF = 100;
	
	private int sizeModifier;

	private JPanel panel;
	private JPanel arraypanel;
	private JPanel buttonpanel;
	private JPanel[] vecPanels;
	private JButton start;
	private JComboBox<String> tipoOrd;
	private JComboBox<String> ordenElem;
	private JSlider velocidad;
	private JSlider tamanio;
	private JLabel velocidadDef;
	private JLabel tamanioDef;
	private GridBagConstraints c;
	
	public Visualizador(){
		super("Sorting Visualizer");
		
		start = new JButton("Start");
		buttonpanel = new JPanel();
		arraypanel = new JPanel();
		panel = new JPanel();
		tipoOrd = new JComboBox<String>();
		ordenElem = new JComboBox<String>();
		velocidad = new JSlider(VEL_MIN, VEL_MAX, VEL_DEF);
		tamanio = new JSlider(TAM_MIN, TAM_MAX, TAM_DEF);
		velocidadDef = new JLabel("Velocidad: 20 ms");
		tamanioDef = new JLabel("Tamanio: 100 elementos");
		c = new GridBagConstraints();
		
		for(MetodoOrdenamiento metOrd : MetodoOrdenamiento.values()) tipoOrd.addItem(metOrd.toString());
		for(CasoOrdenamiento casOrd : CasoOrdenamiento.values()) ordenElem.addItem(casOrd.toString());
		
		arraypanel.setLayout(new GridBagLayout());
		panel.setLayout(new BorderLayout());

		c.insets = new Insets(0,1,0,1);
		c.anchor = GridBagConstraints.SOUTH;
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ordenador.startSort((String) tipoOrd.getSelectedItem());
				Ordenador.startSort((String) ordenElem.getSelectedItem());
			}
		});
		
		velocidad.setMinorTickSpacing(10);
		velocidad.setMajorTickSpacing(100);
		velocidad.setPaintTicks(true);
		
		velocidad.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				velocidadDef.setText(("Velocidad: " + Integer.toString(velocidad.getValue()) + "ms"));
				validate();
				Ordenador.sleep = velocidad.getValue();
			}
		});
		
		tamanio.setMinorTickSpacing(10);
		tamanio.setMajorTickSpacing(100);
		tamanio.setPaintTicks(true);
		
		tamanio.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				tamanioDef.setText(("Tamanio: " + Integer.toString(tamanio.getValue()) + " elementos"));
				validate();
				Ordenador.valorSort = tamanio.getValue();
			}
		});

		buttonpanel.add(velocidadDef);
		buttonpanel.add(velocidad);
		buttonpanel.add(tamanioDef);
		buttonpanel.add(tamanio);
		buttonpanel.add(tipoOrd);
		buttonpanel.add(ordenElem);
		buttonpanel.add(start);
		
		panel.add(buttonpanel, BorderLayout.SOUTH);
		panel.add(arraypanel);
		
		add(panel);

		setExtendedState(JFrame.MAXIMIZED_BOTH );
		
		addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				sizeModifier = (int) ((getHeight()*0.9)/(vecPanels.length));
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
			
		});
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	//reinicia el array de panels. Depende del tamaño de la ventana.
	public void PintarArray(Integer[] squares){
		vecPanels = new JPanel[Ordenador.valorSort];
		arraypanel.removeAll();
		// 90% del tamanio de la pantalla
		sizeModifier =  (int) ((getHeight()*0.9)/(vecPanels.length));
		for(int i = 0; i<Ordenador.valorSort; i++){
			vecPanels[i] = new JPanel();
			vecPanels[i].setPreferredSize(new Dimension(Ordenador.blockWidth, squares[i]*sizeModifier));
			vecPanels[i].setBackground(Color.blue);
			arraypanel.add(vecPanels[i], c);
		}
		repaint();
		validate();
	}
	
	
}

