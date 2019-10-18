package cliente.flota.sockets;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
/*
import partida.Partida;
import tablero.Juego;
import tablero.Juego.ButtonListener;
import tablero.Juego.GuiTablero;
import tablero.Juego.MenuListener;
*/
public class ClienteFlotaSockets {
	
	// Sustituye esta clase por tu versión de la clase Juego de la práctica 1
	
	// Modifícala para que instancie un objeto de la clase AuxiliarClienteFlota en el método 'ejecuta'
	
	// Modifica todas las llamadas al objeto de la clase Partida
	// por llamadas al objeto de la clase AuxiliarClienteFlota.
	// Los métodos a llamar tendrán la misma signatura.
	
	
	public boolean fin = false;
	/**
	 * Implementa el juego 'Hundir la flota' mediante una interfaz gráfica (GUI)
	 */

	/** Parametros por defecto de una partida */
	public static final int NUMFILAS=8, NUMCOLUMNAS=8, NUMBARCOS=6, AGUA=-1, TOCADO=-2, HUNDIDO=-3;

	private GuiTablero guiTablero = null;			// El jug se encarga de crear y modificar la interfaz gráfica
	private AuxiliarClienteFlota partida;                 // Objeto con los datos de la partida en juego
	
	/** Atributos de la partida guardados en el juego para simplificar su implementación */
	private int quedan = NUMBARCOS, disparos = 0;
	
	/**
	 * Programa principal. Crea y lanza un nuevo juego
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ClienteFlotaSockets juego = new ClienteFlotaSockets();
		juego.ejecuta();
	} // end main

	/**
	 * Lanza una nueva hebra que crea la primera partida y dibuja la interfaz grafica: tablero
	 * @throws IOException 
	 */
	private void ejecuta() throws IOException {
		// Instancia la primera partida
		partida = new AuxiliarClienteFlota("localhost","1004");
		partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
				guiTablero.dibujaTablero();
			}
		});
	} // end ejecuta

	/******************************************************************************************/
	/*********************  CLASE INTERNA GuiTablero   ****************************************/
	/******************************************************************************************/
	private class GuiTablero {

		private int numFilas, numColumnas;

		private JFrame frame = null;        // Tablero de juego
		private JLabel estado = null;       // Texto en el panel de estado
		private JButton buttons[][] = null; // Botones asociados a las casillas de la partida

		private JMenuBar barra;
		private JMenu menu;
		
		private JMenuItem muestraSol,nuevaPartida,salir;

		private String letras = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";

		private JPanel panelTablero;
		/**
         * Constructor de una tablero dadas sus dimensiones
         */
		GuiTablero(int numFilas, int numColumnas) {
			this.numFilas = numFilas;
			this.numColumnas = numColumnas;
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		}

		/**
		 * Dibuja el tablero de juego y crea la partida inicial
		 */
		public void dibujaTablero() {
			anyadeMenu();
			anyadeGrid(numFilas, numColumnas);		
			anyadePanelEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);		
			frame.setSize(300, 300);
			frame.setVisible(true);
		} // end dibujaTablero

		/**
		 * Anyade el menu de opciones del juego y le asocia un escuchador
		 */
		private void anyadeMenu() {
			MenuListener menuList= new MenuListener();
			
			barra=new JMenuBar();
			menu=new JMenu("Opciones");
			frame.setJMenuBar(barra);
			barra.add(menu);
			muestraSol=new JMenuItem("Mostrar solución");
			muestraSol.addActionListener(menuList);
						
			nuevaPartida=new JMenuItem("Nueva Partida");
			nuevaPartida.addActionListener(menuList);
			
			salir=new JMenuItem("Salir");
			salir.addActionListener(menuList);
			
			menu.add(muestraSol);
			menu.add(nuevaPartida);
			menu.add(salir);
		} // end anyadeMenu

		/**
		 * Anyade el panel con las casillas del mar y sus etiquetas.
		 * Cada casilla sera un boton con su correspondiente escuchador
		 * @param nf	numero de filas
		 * @param nc	numero de columnas
		 */

		private void anyadeGrid(int nf, int nc) {
			panelTablero = new JPanel();
			panelTablero.setLayout(new GridLayout(nf+1,nc+2));
			buttons = new JButton[nf][nc];
			panelTablero.add(new JLabel(""));
			ButtonListener escuchador = new ButtonListener();
			for (int i=1;i<=nc;i++){
				panelTablero.add(new JLabel(""+i));
			}
			panelTablero.add(new JLabel(""));
			for (int i=0;i<nf;i++){
				panelTablero.add(new JLabel(""+letras.charAt(i)));
				for (int j=0;j<nc;j++){
					buttons[i][j]=new JButton();
					int[] coordenadas = {i,j};	// Le añadimos la etiqueta al propio botón
					buttons[i][j].putClientProperty("coord",coordenadas);
					buttons[i][j].addActionListener(escuchador);
					panelTablero.add(buttons[i][j]);
				}
				panelTablero.add(new JLabel(""+letras.charAt(i)));
			}
			frame.add(panelTablero,BorderLayout.CENTER);
		} // end anyadeGrid


		/**
		 * Anyade el panel de estado al tablero
		 * @param cadena	cadena inicial del panel de estado
		 */
		private void anyadePanelEstado(String cadena) {	
			JPanel panelEstado = new JPanel();
			estado = new JLabel(cadena);
			panelEstado.add(estado);
			// El panel de estado queda en la posición SOUTH del frame
			frame.getContentPane().add(panelEstado, BorderLayout.SOUTH);
		} // end anyadePanel Estado

		/**
		 * Cambia la cadena mostrada en el panel de estado
		 * @param cadenaEstado	nuevo estado
		 */
		public void cambiaEstado(String cadenaEstado) {
			estado.setText(cadenaEstado);
		} // end cambiaEstado

		/**
		 * Muestra la solucion de la partida y marca la partida como finalizada
		 * @throws IOException 
		 */
		
		
		
		public void muestraSolucion() throws IOException {
            String[] solucion = partida.getSolucion();
            int filaIni;
            int colIni;
            char orientacion;
            int tamanyo;
            String[] cadena;
            Color color = Color.red;	// Color para pintar el barco
            Color colorMar = Color.blue;
            
            
            
            for(int f=0; f<numFilas; f++) {		// Pintamos todo el mar de azul
            	for( int c=0; c<numColumnas; c++) {
            		pintaBoton(buttons[f][c],colorMar);
            	}
            }
            
            for(int i=0; i<solucion.length; i++) {
            	cadena = solucion[i].split("#");
            	filaIni=Integer.parseInt(cadena[0]);
            	colIni=Integer.parseInt(cadena[1]);
            	orientacion= cadena[2].charAt(0);
            	tamanyo= Integer.parseInt(cadena[3]);
            	
            	for(int j=0; j<tamanyo; j++) {        		
            		if (orientacion=='H') { 
            			pintaBoton(buttons[filaIni][colIni++],color);
            		}else { 
            			pintaBoton(buttons[filaIni++][colIni],color);
            		}
               	}
            }
		} // end muestraSolucion

		//private void daCodigo()

		/**
		 * Pinta un barco como hundido en el tablero
		 * @param cadenaBarco	cadena con los datos del barco codifificados como
		 *                      "filaInicial#columnaInicial#orientacion#tamanyo"
		 */
		public void pintaBarcoHundido(String cadenaBarco) {
			int filaIni;
            int colIni;
            char orientacion;
            int tamanyo;
            
            quedan--;

            Color color = Color.red;	// Color para pintar el barco

          
            String[] barc = cadenaBarco.split("#");
            filaIni=Integer.parseInt(barc[0]);
        	colIni=Integer.parseInt(barc[1]);
        	orientacion= barc[2].charAt(0);
        	tamanyo= Integer.parseInt(barc[3]);
        	
        	for(int j=0; j<tamanyo; j++) {
        		if (orientacion=='H') {
        			pintaBoton(buttons[filaIni][colIni++],color);
        		}else {
        			pintaBoton(buttons[filaIni++][colIni],color);
        		}
           	}
		} // end pintaBarcoHundido

		private void pintaCoord(int i, int j, Color color){
			pintaBoton(buttons[i][j],color);
		}

		/**
		 * Pinta un botón de un color dado
		 * @param b			boton a pintar
		 * @param color		color a usar
		 */
		public void pintaBoton(JButton b, Color color) {
			b.setBackground(color);
			// El siguiente código solo es necesario en Mac OS X
			b.setOpaque(true);
			b.setBorderPainted(false);
		} // end pintaBoton

		/**
		 * Limpia las casillas del tablero pintándolas del gris por defecto
		 */
		public void limpiaTablero() {
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < numColumnas; j++) {
					buttons[i][j].setBackground(null);
					buttons[i][j].setOpaque(true);
					buttons[i][j].setBorderPainted(true);
				}
			}
		} // end limpiaTablero

		/**
		 * 	Destruye y libera la memoria de todos los componentes del frame
		 */
		public void liberaRecursos() {
			frame.dispose();
		} // end liberaRecursos


	} // end class GuiTablero

	/******************************************************************************************/
	/*********************  CLASE INTERNA MenuListener ****************************************/
	/******************************************************************************************/

	/**
	 * Clase interna que escucha el menu de Opciones del tablero
	 * 
	 */
	private class MenuListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem boton = (JMenuItem)e.getSource();
	        String texto = boton.getText();
	        
			if(texto.equals("Mostrar solución")) {
				fin=true;
				try {
					guiTablero.muestraSolucion();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			if(texto.equals("Nueva Partida")) {
				System.out.println("Iniciamos nueva partida");
				quedan = 6;
				disparos = 0;
				fin = false;
				guiTablero.limpiaTablero();

				try {
					partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
				} catch (IOException e1) {
					System.out.println("ERROR. Error creando nueva partida"+e);
				}

				disparos=0;
				quedan = NUMBARCOS;
				guiTablero.cambiaEstado("Disparos: "+disparos+"  Barcos restantes: "+quedan);
			}
			
			if(texto.equals("Salir")) {
				System.out.println("Salimos");
				guiTablero.liberaRecursos();


			}
	
		} // end actionPerformed

	} // end class MenuListener



	/******************************************************************************************/
	/*********************  CLASE INTERNA ButtonListener **************************************/
	/******************************************************************************************/
	/**
	 * Clase interna que escucha cada uno de los botones del tablero
	 * Para poder identificar el boton que ha generado el evento se pueden usar las propiedades
	 * de los componentes, apoyandose en los metodos putClientProperty y getClientProperty
	 */
	private class ButtonListener implements ActionListener {

		public ButtonListener(){
			
		}
		//TODO Comprobar si es así
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton boton = (JButton) e.getSource();
			int[] coordenada = (int[]) boton.getClientProperty("coord");
			int i = coordenada[0];
			int j = coordenada[1];
			if (!fin){
				disparos++;
				int id=0;
				try {
					id = partida.pruebaCasilla(i,j);
				} catch (IOException e1) {
					System.out.println("ERROR. Error probando casilla"+e1);
					//return;
				}
				switch (id){
					case AGUA:
						guiTablero.pintaCoord(i,j,Color.blue);
						break;
					case TOCADO:
						guiTablero.pintaCoord(i,j,Color.yellow);
						break;
					case HUNDIDO:
						break;
					default:
					try {
						guiTablero.pintaBarcoHundido(partida.getBarco(id));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
						break;

				}

				if (quedan==0){
					guiTablero.cambiaEstado("GAME OVER en "+disparos+" disparos");
					fin = true;
				}
				else
					guiTablero.cambiaEstado("Disparos: "+disparos+"	 Barcos restantes: "+quedan);
			}

        } // end actionPerformed

	} // end class ButtonListener

	
	

}
