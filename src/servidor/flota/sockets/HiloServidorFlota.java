package servidor.flota.sockets;


import java.io.IOException;
import java.net.SocketException;

import partida.flota.sockets.*;
import comun.flota.sockets.MyStreamSocket;

/**
 * Clase ejecutada por cada hebra encargada de servir a un cliente del juego Hundir la flota.
 * El metodo run contiene la logica para gestionar una sesion con un cliente.
 */

 // Revisar el apartado 5.5. del libro de Liu

class HiloServidorFlota implements Runnable {
   MyStreamSocket myDataSocket;
   
   public static final int FIN=0, NUEVA_PARTIDA=1, PROBAR_CASILLA=2, OBTENER_BARCO=3,SOLUCION=4;
   private Partida partida = null;

	/**
	 * Construye el objeto a ejecutar por la hebra para servir a un cliente
	 * @param	myDataSocket	socket stream para comunicarse con el cliente
	 */
   HiloServidorFlota(MyStreamSocket myDataSocket) {
	   
	   this.myDataSocket=myDataSocket;
      
	   
   }
 
   /**
	* Gestiona una sesion con un cliente	
	* 
	* Opciones mensaje según se recibe:
	* 
	*  final = "0"
	 * nuevaPartida = "1#filas#cols#barco"
	 * pruebaCasilla = "2#fila#col"
	 * getBarco = "3#id"
	 * getSol="4"
	* 
   */
   public void run( ) {
      int operacion = 0;
      boolean done = false;
      String[] message;
      
      try {
         while (!done) {
        	 // Recibe una peticion del cliente
        	 // Extrae la operación y los argumentos
        	 
        	 message = myDataSocket.receiveMessage().split("#");
        	 operacion=Integer.parseInt(message[0]);
     
        	 int nf,nc,nb;	
                      
             switch (operacion) {
             case FIN:  // fin de conexión con el cliente
            	myDataSocket.close();
            	done = true;
            	break;

             case NUEVA_PARTIDA: { // Crea nueva partida	"1#filas#cols#barco"
            	 nf=Integer.parseInt(message[1]);
            	 nc=Integer.parseInt(message[2]);
            	 nb=Integer.parseInt(message[3]);
            	 partida = new Partida(nf,nc,nb);
            	 break;
             }             
             case PROBAR_CASILLA: { // Prueba una casilla y devuelve el resultado al cliente
            	 nf=Integer.parseInt(message[1]);
            	 nc = Integer.parseInt(message[2]);
            	 
            	 // Enviamos el resultado al cliente.	"2#fila#col"
            	 myDataSocket.sendMessage(Integer.toString(partida.pruebaCasilla(nf, nc)));
                 break;
             }
             case OBTENER_BARCO: { // Obtiene los datos de un barco y se los devuelve al cliente	"3#id"
            	 int id = Integer.parseInt(message[1]);
            	 myDataSocket.sendMessage(partida.getBarco(id));
                 break;
             }
             case SOLUCION: { // Devuelve al cliente la solucion en forma de vector de cadenas	   getSol="4"
        	   // Primero envia el numero de barcos 
            	 String[] barcos = partida.getSolucion();
            	 myDataSocket.sendMessage(Integer.toString(barcos.length));
            	 
               // Despues envia una cadena por cada barco
            	 for(int barco =0; barco<barcos.length;barco++) {
            		 myDataSocket.sendMessage(barcos[barco]);   		 
            	 }
                 break;
             }
         } // fin switch
       } // fin while   
     } // fin try
      
      
     catch (Exception ex) {
        System.out.println("Exception caught in thread: "+ex);
        ex.printStackTrace();
     } // fin catch
   } //fin run
   
} //fin class 
