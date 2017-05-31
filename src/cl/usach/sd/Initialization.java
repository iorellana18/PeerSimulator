package cl.usach.sd;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;

public class Initialization implements Control {
	String prefix;

	int idLayer;
	int idTransport;

	//Valores que sacaremos del archivo de configuraci�n	
	int argExample;
	int initValue;

	public Initialization(String prefix) {
		this.prefix = prefix;
		this.idLayer = Configuration.getPid(prefix + ".protocol");
		this.idTransport = Configuration.getPid(prefix + ".transport");
		
	}

	/**
	 * Ejecuci�n de la inicializaci�n en el momento de crear el overlay en el
	 * sistema
	 */
	@Override
	public boolean execute() {
		System.out.println("Construyendo red");
		/**
		 * Para comenzar tomaremos un nodo cualquiera de la red, a trav�s de un random
		 */
		//int nodoInicial = CommonState.r.nextInt(Network.size());
		
		//Valores iniciales
		int tamanoRed = Network.size();
		int tamanoBD = Configuration.getInt(prefix + ".BD");
		int Cache = Configuration.getInt(prefix + ".Cache");
		int d = Configuration.getInt(prefix + ".d");
		int tamanoDHT = 1+2*d;
		
		System.out.println("Valores iniciales");
		System.out.println("n:\t"+tamanoRed+"\t [Tamaño de la red]");
		System.out.println("b:\t"+tamanoBD+"\t [Tamaño de la base de datos]");
		System.out.println("c:\t"+Cache+"\t [Tamaño de caché]");
		System.out.println("d:\t"+d);
		System.out.println("---");
		System.out.println("Cálculos iniciales");
		System.out.println("DHT:\t\t"+tamanoDHT);
		ArrayList<Integer> distancias = obtenerDistancias(tamanoRed);
		System.out.println("Distancias:\t"+distancias);
		
		System.out.println("---");
		System.out.println("Inicializamos los nodos:");
		
		
		for (int i = 0; i < Network.size(); i++) {	
			//Método que inicializa listas para publicar/suscribirse
			ArrayList<Integer> BD = obtenerBD(i,tamanoBD);
			ArrayList<Integer> DHT = myDHT(i,distancias,tamanoRed,tamanoDHT);
			((Peer)Network.get(i)).initPeer(i,Cache,DHT,tamanoRed,BD,tamanoBD);
			int linkableID = FastConfig.getLinkable(this.idLayer);
			Linkable linkable = (Linkable)((Peer) Network.get(i)).getProtocol(linkableID);
			if(i < Network.size()-1){
				linkable.addNeighbor((Peer)Network.get(i+1));
			}else{
				linkable.addNeighbor((Peer)Network.get(0));
			}
		}
		
		return true;
	}
	
	public ArrayList<Integer> obtenerDistancias(int tamanoRed){
		ArrayList<Integer> distancias = new ArrayList<Integer>();
		/// 2^x
		ArrayList<Integer> denominadores = new ArrayList<Integer>();
		// Valores x
		ArrayList<Integer> x = new ArrayList<Integer>();
		// 2^0
		int inicial = 1;
		int valorX = 1;
		// Obtiene denominadores hasta completar tamaño de la red
		while(inicial<tamanoRed){
			inicial = inicial * 2;
			if(inicial<tamanoRed){
				denominadores.add(inicial);
				x.add(valorX);
				valorX++;
			}
		}
		
		for(int i=0;i<denominadores.size();i++){
			distancias.add(tamanoRed/denominadores.get(i));
		}
		System.out.println("x:\t\t"+x+"\t [Valores de x, 2^x<n]");
		System.out.println("2^x:\t\t"+denominadores);
		return distancias;
	}
	
	public ArrayList<Integer> obtenerBD(int id, int tamanoBD){
		ArrayList<Integer> base = new ArrayList<Integer>();
		int inicial = id * tamanoBD;
		for(int i=0;i<tamanoBD;i++){
			base.add(inicial);
			inicial++;
		}
		return base;
	}
	
	public ArrayList<Integer> myDHT(int id, ArrayList<Integer> distancias,int tamanoRed, int tamanoDHT){
		ArrayList<Integer> DHT = new ArrayList<Integer>();
		for(int i=0;i<tamanoDHT;i++){
			
			if(DHT.size()<tamanoDHT && i<distancias.size()){
				int vecinoDht = id+distancias.get(i);
				if(vecinoDht<tamanoRed){
					DHT.add(vecinoDht);
				}else{
					vecinoDht = vecinoDht - tamanoRed;
					DHT.add(vecinoDht);
				}
			}
		}
		return DHT;
	}

}
