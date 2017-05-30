package cl.usach.sd;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
public class TrafficGenerator implements Control {
	private final static String PAR_PROT = "protocol";
	private final int layerId;
	String prefix;

	public TrafficGenerator(String prefix) {
		layerId = Configuration.getPid(prefix + "." + PAR_PROT);
		setPrefix(prefix);
	}

	@Override
	public boolean execute() {
		
		// Consideraremos cualquier nodo de manera aleatoria de la red para comenzar
		Node initNode = Network.get(CommonState.r.nextInt(Network.size())); 
		System.out.println("Nodo: "+initNode.getID());
		//System.out.println("NeighborNode: "+((Linkable) initNode.getProtocol(0)).getNeighbor(0).getID());
		
		// Tamaño de base de datos
		long tamanoBD = ((Peer)initNode).getSizeDB();
		
		//Parámetros del mensaje
		// Emisor (actual)
		long emisor = initNode.getID();
		// Dato a buscar (Determinado de forma aleatoria en la base de datos)
		long dato = CommonState.r.nextLong(Network.size()*(tamanoBD));
		// Receptor dependiendo del dato
		long receptor = buscarReceptor(dato, tamanoBD);
		
		// Se añade mensaje
		// Parámetros: ID nodo emisor, ID nodo receptor, dato que se busca
		//Message message = new Message();
		/// EDSimulator.add(0, message, initNode, layerId);
		return false;
	}
	
	public long buscarReceptor(long dato, long tamanoBD){
		long nodo = dato/tamanoBD;
		return nodo;
	}
	public void setPrefix(String prefix){this.prefix=prefix;}

}
