
package cliente.flota.sockets;
import java.net.*;
import java.io.*;

import comun.flota.sockets.*;

/**
 * Esta clase implementa el intercambio de mensajes
 * asociado a cada una de las operaciones basicas que comunican cliente y servidor
 */

public class AuxiliarClienteFlota {

   private MyStreamSocket mySocket;
   private InetAddress serverHost;
   private int serverPort;

	/**
	 * Constructor del objeto auxiliar del cliente
	 * Crea un socket de tipo 'MyStreamSocket' y establece una conexión con el servidor
	 * 'hostName' en el puerto 'portNum'
	 * @param	hostName	nombre de la máquina que ejecuta el servidor
	 * @param	portNum		numero de puerto asociado al servicio en el servidor
	 * 
	 * final = "0"
	 * nuevaPartida = "1#filas#cols#barco"
	 * pruebaCasilla = "2#fila#col"
	 * getBarco = "3#id"
	 * getSol="4"
	 * 
	 */
   AuxiliarClienteFlota(String hostName,
                     String portNum) throws SocketException,
                     UnknownHostException, IOException {
	   
  	   try {
  		   mySocket =  new MyStreamSocket(InetAddress.getByName(hostName),Integer.parseInt(portNum));
  		   System.out.println("Estableciendo conexión...");
  		   String message = mySocket.receiveMessage();
  		   
<<<<<<< Updated upstream
  		  
  		   
  	   }catch(Exception ex) {ex.printStackTrace();}
=======
  		   // Miramos que tipo de mensage se le envía para llamar a los métodos
  		   if(message.length()==1) {
  			   if(message.equals("0")) { fin();}
  			   if(message.contentEquals("4")) {
  				   String[] solucion= getSolucion();
  				   mySocket.sendMessage(Integer.toString(solucion.length));
  				   
  				   for(int i = 0; i<solucion.length;i++) {
  					   mySocket.sendMessage(solucion[i]);
  				   }
  			   }else{
  				   
  				   
  				   String[] mensaje =  message.split("#");
  				   
  				   if(mensaje[0].equals("3")) {
  					   String barco = getBarco(Integer.parseInt(mensaje[1]));
  					   mySocket.sendMessage(barco);
  				   }
  				   int nf= Integer.parseInt(mensaje[1]);
  				   int nc = Integer.parseInt(mensaje[2]);
  				   
  				   if(mensaje[0].equals("1")) {
  					   int nb = Integer.parseInt(mensaje[3]);
  					   nuevaPartida(nf,nc,nb);
  				   }else if(mensaje[0].equals("2")) {
  					   int casilla = pruebaCasilla(nf,nc);
  					   mySocket.sendMessage(Integer.toString(casilla));
  				   }
  			   }
  				   
  		   }
  		  
  	   }catch(SocketException e) {
  		   System.out.println("ERROR: Error en la creación de socket");
  	   }catch(UnknownHostException e) {
  		   System.out.println("ERROR: Host no reconocido");
  	   }catch(IOException e) {
  		   System.out.println("ERROR: Error en la comunicación");
  	   }
>>>>>>> Stashed changes
	   
   } // end constructor
   
   /**
	 * Usa el socket para enviar al servidor una petición de fin de conexión
	 * con el formato: "0"
	 * @throws	IOException
	 */
   public void fin( ) throws IOException {
	   try {
		   mySocket.close();
	   }catch(Exception ex) {System.out.println("ERROR: No se puede cerrar la conexión");}
	   
   } // end fin 
  
   /**
    * Usa el socket para enviar al servidor una petición de creación de nueva partida 
    * con el formato: "1#nf#nc#nb"
    * @param nf	número de filas de la partida
    * @param nc	número de columnas de la partida
    * @param nb	número de barcos de la partida
    * @throws IOException
    */
   public void nuevaPartida(int nf, int nc, int nb)  throws IOException {
	   
	   // Por implementar
	   
   } // end nuevaPartida

   /**
    * Usa el socket para enviar al servidor una petición de disparo sobre una casilla 
    * con el formato: "2#f#c"
    * @param f	fila de la casilla
    * @param c	columna de la casilla
    * @return	resultado del disparo devuelto por la operación correspondiente del objeto Partida
    * 			en el servidor.
    * @throws IOException
    */
   public int pruebaCasilla(int f, int c) throws IOException {
	   
	   // Por implementar
	   return 0; // cambiar por el retorno correcto
	   
    } // end pruebaCasilla
   
   /**
    * Usa el socket para enviar al servidor una petición de los datos de un barco
    * con el formato: "3#idBarco"
    * @param idBarco	identidad del Barco
    * @return			resultado devuelto por la operación correspondiente del objeto Partida
    * 					en el servidor.
    * @throws IOException
    */
   public String getBarco(int idBarco) throws IOException {
	   
	   // Por implementar
	   return null; // cambiar por el retorno correcto
	   
    } // end getBarco
   
   /**
    * Usa el socket para enviar al servidor una petición de los datos de todos los barcos
    * con el formato: "4"
    * @return	resultado devuelto por la operación correspondiente del objeto Partida
    * 			en el servidor
    * @throws IOException
    */
   public String[] getSolucion() throws IOException {
	   
	   // Por implementar
	   return null; // cambiar por el retorno correcto
	   
    } // end getSolucion
   


} //end class
