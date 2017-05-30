package cl.usach.sd;

import java.util.ArrayList;

/**
 * Clase la cual vamos a utilizar para enviar datos de un Peer a otro
 */
public class Message {
	// ID de quien envía mensaje
	private int emisor;
	// ID de quien debe recibir mensaje
	private int receptor;
	// Arreglo que guarda IDs de nodos por donde pasa para llegar
	private ArrayList<Integer> camino;
	// Para conocer si mensaje va de ida o vuelta
	private Boolean recibido;
	// Dato que se busca
	private int dato;
	
	// Método para iniciar mensaje, sólo hace falta el emisor y receptor
	public Message(int emisor, int receptor, int dato){
		setEmisor(emisor);
		setReceptor(receptor);
		setCamino(new ArrayList<Integer>());
		// Recibido se inicia como falso cuando se envía
		setRecibido(false);
		setDato(dato);
	}

	// Constructores
	public void setEmisor(int emisor){this.emisor=emisor;}
	public int getEmisor(){return emisor;}
	public void setReceptor(int receptor){this.receptor=receptor;}
	public int getReceptor(){return receptor;}
	public void setCamino(ArrayList<Integer> camino){this.camino=camino;}
	public ArrayList<Integer> getCamino(){return camino;}
	public void setRecibido(Boolean recibido){this.recibido=recibido;}
	public Boolean getRecibido(){return recibido;}
	public void setDato(int dato){this.dato=dato;}
	public int getDato(){return dato;}
	
}
