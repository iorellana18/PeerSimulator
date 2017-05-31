package cl.usach.sd;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.WireKOut;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;

public class Layer implements Cloneable, EDProtocol {
	private static final String PAR_TRANSPORT = "transport";
	private static String prefix = null;
	private int transportId;
	private int layerId;

	/**
	 * M�todo en el cual se va a procesar el mensaje que ha llegado al Nodo
	 * desde otro Nodo. Cabe destacar que el mensaje va a ser el evento descrito
	 * en la clase, a trav�s de la simulaci�n de eventos discretos.
	 */
	@Override
	public void processEvent(Node myNode, int layerId, Object event) {
		/**Este metodo trabajar� sobre el mensaje*/
		/**A modo de ejemplo, elegiremos cualquier nodo, y a ese nodo le enviaremos el mensaje 
		 * con las siguientes condiciones de ejemplo: 
		 * Si el nodo actual es del tipo 0: suma 1 a su valor
		 * Si el nodo actual es del tipo 1: resta 1 a su valor
		 * */	
		Message message = (Message) event;
		//Imprimie mensaje
		//System.out.println(message.getMensaje());
		

		sendmessage(myNode, layerId, message);
		getStats();
	}

	private void getStats() {
		Observer.message.add(1);
	}

	public void sendmessage(Node currentNode, int layerId, Object message) {
		/**Con este m�todo se enviar� el mensaje de un nodo a otro
		 * CurrentNode, es el nodo actual
		 * message, es el mensaje que viene como objeto, por lo cual se debe trabajar sobre �l
		 */
		// Castear mensaje
		Message mensaje = (Message)message;
		// Casteo de nodo
		Peer nodoActual = (Peer)currentNode;
		
		// Obtener datos de mensaje
		String contenido = mensaje.getMensaje();
		long receptor = mensaje.getReceptor();
		long dato = mensaje.getDato();
		ArrayList<Integer> camino = mensaje.getCamino();
		
		// Datos Peer actual
		long id = nodoActual.getID();
		long vecino = ((Linkable) nodoActual.getProtocol(0)).getNeighbor(0).getID();
		
		if(mensaje.getRecibido()==false){
			// Nodo actual recibe mensaje?
			if(id == receptor){
				System.out.println("\t"+contenido+" Nodo "+id+" tiene respuesta a consulta "+dato);
				mensaje.setRecibido(true);
				System.out.println("\t"+contenido+" Nodo "+id+" ha pasado por los nodos"+mensaje.getCamino());
				// Obtener último nodo
				int envioDeVuelta = camino.get(camino.size()-1);
				camino.remove(camino.size()-1);
				mensaje.setCamino(camino);
				System.out.println("\t"+contenido+" Enviando respuesta a nodo "+envioDeVuelta);
				((Transport) nodoActual.getProtocol(transportId)).send(nodoActual, Network.get(envioDeVuelta), mensaje, layerId);
			}else{
				// Revisar si cache está vacío
				if(true){
					System.out.println("\t"+contenido+" Nodo "+id+" no tiene consulta "+dato+" en cache");
					// Nodo es vecino?
					if(vecino==receptor){
						System.out.println("\t"+contenido+" Nodo "+id+" envía consulta a nodo vecino "+receptor);
						// Se añade nodo al camino por el que pasa
						camino.add((int)id);
						mensaje.setCamino(camino);
						((Transport) nodoActual.getProtocol(transportId)).send(nodoActual, Network.get((int) vecino), mensaje, layerId);
					}else{
						// Si no, calcula distancia de vecino con DHT para ver quien está más cerca
						int peerCercano = nodoActual.calcularDistancias(contenido,receptor);
						System.out.println("\t"+contenido+" Nodo "+id+" envía consulta a nodo "+peerCercano);
						// Se añade nodo al camino por el que pasa
						camino.add((int)id);
						mensaje.setCamino(camino);
						((Transport) nodoActual.getProtocol(transportId)).send(nodoActual, Network.get(peerCercano), mensaje, layerId);
					}
				}else{
					// Buscar en cache
					
				}
			}
		}else{
			// Si mensaje ya fue recibido se envía de vuelta hasta el nodo inicial
			if(camino.size()>0){
				// Guardar en cache
				
				if(nodoActual.getCache().size()<nodoActual.getTamanoCache()){
					Cache cache = new Cache((int)receptor,(int)dato);
					ArrayList<Cache> mainCache = nodoActual.getCache();
					mainCache.add(cache);
					nodoActual.setCache(mainCache);
					System.out.print("\t"+contenido+" Nodo : "+id+" Caché actualizado: ");
					for(int i=0;i<nodoActual.getCache().size();i++){
						nodoActual.getCache().get(i).printCache();
					}
					System.out.println(" ");
				}
				// Enviar mensaje a nodo anterior y luego lo borra
				int enviar = camino.get(camino.size()-1);
				camino.remove(camino.size()-1);
				// Actualiza camino que queda por recorrer y envía
				mensaje.setCamino(camino);
				System.out.println("\t"+contenido+" Nodo "+id+" envía respuesta a nodo "+enviar);
				((Transport) nodoActual.getProtocol(transportId)).send(nodoActual, Network.get(enviar), mensaje, layerId);
			}else{
				// Nodo emisor que envió la consulta inicialmente
				System.out.println("\t"+contenido+" Nodo "+id+" Recibió respuesta");
			}
			
			
		}

	}
	
	

	public Layer(String prefix) {
		/**
		 * Inicialización del Nodo
		 */
		Layer.prefix = prefix;
		transportId = Configuration.getPid(prefix + "." + PAR_TRANSPORT);
		/**
		 * Siguiente capa del protocolo
		 */
		layerId = transportId + 1;
	}

	

	/**
	 * Definir Clone() para la replicacion de protocolo en nodos
	 */
	public Object clone() {
		Layer dolly = new Layer(Layer.prefix);
		return dolly;
	}
}
