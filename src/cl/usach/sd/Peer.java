package cl.usach.sd;

import java.util.ArrayList;

import peersim.core.GeneralNode;

public class Peer extends GeneralNode{
	
	private int id;
	// Lista que guarda peers en cache
	private ArrayList<Cache> cache;
	// Lista que guarda peers en DHT
	private ArrayList<Integer> DHT;
	// Lista que guarda valores en base de datos
	private ArrayList<Integer> DB;
	// Dato que indica si mensaje va de ida o de vuelta // Ida = true // Vuelta = false
	private Boolean mensajeIda;
	// Lista que guarda los peers por los que se ha pasado para llegar al destino
	private ArrayList<Integer> camino;
	// Vecino
	private int vecino;
	// Tamaño de base de datos
	private int sizeDB;
	// Tamaño red
	private int tamanoRed;
	// Tamaño cache
	private int tamanoCache;

	public Peer(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}


	public void initPeer(int id,int tamanoCache, ArrayList<Integer> DHT, int tamanoRed, ArrayList<Integer> DB,
			int sizeDB){
		setId(id);
		setCache(new ArrayList<Cache>());
		setDHT(DHT);
		setMensajeIda(true);
		setCamino(new ArrayList<Integer>());
		setVecino(id,tamanoRed);
		setDB(DB);
		setTamanoRed(tamanoRed);
		setTamanoCache(tamanoCache);
		setSizeDB(sizeDB);
		System.err.print("Nodo: "+id);
		System.out.println("\t\tVecino: "+vecino+"\tDHT: "+DHT+"\t\tCache: "+cache+"\t\tBD: "+DB+"\n");
	}
	
	// Métodos
	
	public int calcularDistancias(String mensaje, long receptor){
		ArrayList<Integer> lista = new ArrayList<Integer>();
		System.out.print("\t"+mensaje+" ");
		System.out.print("Nodo "+id+" calcula distancias con "+vecino);
		lista.add(vecino);
		for(int i=0;i<DHT.size();i++){
			System.out.print(", "+DHT.get(i));
			lista.add(DHT.get(i));
		}
		System.out.println(" ");
		return lista.get(distanciador((int)receptor,lista,mensaje));
	}
	
	public int distanciador(int objetivo, ArrayList<Integer> actual, String contenido){
		
		int menor = 0;
		int min = tamanoRed;
		for(int i=0;i<actual.size();i++){
			int distancia;
			if(objetivo>actual.get(i)){
				distancia = objetivo - actual.get(i);
			}else if(objetivo == actual.get(i)){
				System.out.println("\t"+contenido+" Nodo objetivo se encuentra en DHT");
				return i;
			}else{
				distancia = (tamanoRed - actual.get(i)) + objetivo;
			}
			if(distancia<min){
				menor = i;
				min = distancia;
			}
		}
		
		return menor;
	}
	
	
	public int getVecino(){return vecino;}
	public void setVecino(int id, int tamanoRed){if(id==(tamanoRed-1)){this.vecino=0;}else{this.vecino=id+1;}}
	public int getId(){return id;}
	public void setId(int id){this.id=id;}
	public ArrayList<Cache> getCache() {return cache;}
	public void setCache(ArrayList<Cache> cache) {this.cache = cache;}
	public ArrayList<Integer> getDHT() {return DHT;}
	public void setDHT(ArrayList<Integer> dHT) {DHT = dHT;}
	public ArrayList<Integer> getDB() {return DB;}
	public void setDB(ArrayList<Integer> dB) {DB = dB;}
	public ArrayList<Integer> getCamino() {return camino;}
	public void setCamino(ArrayList<Integer> camino) {this.camino = camino;}
	public Boolean getMensajeIda() {return mensajeIda;}
	public void setMensajeIda(Boolean mensajeIda) {this.mensajeIda = mensajeIda;}
	public void setSizeDB(int sizeDB){this.sizeDB=sizeDB;}
	public int getSizeDB(){return sizeDB;}
	public void setTamanoRed(int tamanoRed){this.tamanoRed=tamanoRed;}
	public int getTamanoRed(){return tamanoRed;}
	public void setTamanoCache(int tamanoCache){this.tamanoCache=tamanoCache;}
	public int getTamanoCache(){return tamanoCache;}
}
