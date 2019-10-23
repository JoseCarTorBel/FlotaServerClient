
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
	   serverHost = InetAddress.getByName(hostName);
	   serverPort = Integer.parseInt(portNum);
	   
  	   try {
  		   mySocket =  new MyStreamSocket(serverHost,serverPort);
  		   System.out.println("Estableciendo conexión...");  		   
  	   }catch(SocketException e) {
  		   System.out.println("ERROR: Error en la creación de socket");
  	   }catch(UnknownHostException e) {
  		   System.out.println("ERROR: Host no reconocido");
  	   }catch(IOException e) {
  		   System.out.println("ERROR: Error en la comunicación");
  	   }

   } // end constructor
   
   /**
	 * Usa el socket para enviar al servidor una petición de fin de conexión
	 * con el formato: "0"
	 * @throws	IOException
	 */
   public void fin( ) throws IOException {
	   try {
		   mySocket.sendMessage("0");
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
	   try {
		   mySocket.sendMessage("1#"+nf+"#"+nc+"#"+nb);
	   }catch(IOException e) {
		   System.out.println("ERROR -> Error enviando mensaje "+e);
	   } 
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
	   
	   mySocket.sendMessage("2#"+f+"#"+c);
	   int resultado= Integer.parseInt(mySocket.receiveMessage());
	   
	   return resultado;
	   
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
	   try {
		   mySocket.sendMessage("3#"+idBarco);
		   String barco = mySocket.receiveMessage();
		   return barco; 
	   }catch(IOException e) {
		   System.out.println("ERROR. Conexión getBarco"+e);
	   }
	   return null;	//TODO Mirar que devolver en caso de exception
	   
    } // end getBarco
   
   /**
    * Usa el socket para enviar al servidor una petición de los datos de todos los barcos
    * con el formato: "4"
    * @return	resultado devuelto por la operación correspondiente del objeto Partida
    * 			en el servidor
    * @throws IOException
    */
   public String[] getSolucion() throws IOException {
	   try {
		   mySocket.sendMessage("4");
		   int nBarcos = Integer.parseInt(mySocket.receiveMessage());
		   String[] barcos = new String[nBarcos];
		   
		   for(int i=0;i<nBarcos;i++) {
			   barcos[i]=mySocket.receiveMessage();
		   }
		   return barcos; // cambiar por el retorno correcto
	   }catch(IOException e) {
		   System.out.println("ERROR. Conexión solución "+e);
	   }
	   return null;	//TODO Mirar que devolver en caso de exception
    } // end getSolucion
   


} //end class
