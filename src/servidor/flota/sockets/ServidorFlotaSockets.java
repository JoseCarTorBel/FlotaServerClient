package servidor.flota.sockets;

import java.net.ServerSocket;

import comun.flota.sockets.MyStreamSocket;

/**
 * Este modulo contiene la logica de aplicacion del servidor del juego Hundir la flota
 * Utiliza sockets en modo stream para llevar a cabo la comunicacion entre procesos.
 * Puede servir a varios clientes de modo concurrente lanzando una hebra para atender a cada uno de ellos.
 * Se le puede indicar el puerto del servidor en linea de ordenes.
 */


public class ServidorFlotaSockets {
   
   public static void main(String[] args) throws InterruptedException {
	   
	  // Acepta conexiones vía socket de distintos clientes.
	  // Por cada conexión establecida lanza una hebra de la clase HiloServidorFlota.
	   
	   // OJO: El servidor no se cierra nunca, siempre está escuchando
	   	
	   Thread partidaJugador;
	    try {
	    	ServerSocket connectionSocket = new ServerSocket(1004);
	    	System.out.println("Esperando a aceptar conexion");
	    	MyStreamSocket dataSocket = new MyStreamSocket(connectionSocket.accept());
	    	System.out.println("Conexion aceptada");
	    	
	    	// Creamos la partida paralela
	    	partidaJugador = new Thread(new HiloServidorFlota(dataSocket));
	    	partidaJugador.run();
	    	
	    	
	    	//TODO Este join no se si está bien aquí. 
	    	
	    }catch(Exception ex){ 
	    	System.out.println("Error conexion servidor");
	    	ex.printStackTrace();
	    	
	    }
	    
	    
	   
	    
	    
	   

	  // Revisad el apartado 5.5 del libro de Liu
 
   } //fin main
} // fin class
