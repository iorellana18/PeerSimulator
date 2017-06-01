package cl.usach.sd;

import java.util.ArrayList;
import java.util.Stack;

import peersim.core.GeneralNode;

public class Peer extends GeneralNode{
	
	private int id;
	// Objeto que guarda peers en cache
	private Stack<String> cache;
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


	public void initPeer(int id,int tamanoCache, int tamanoRed,int d,
			int sizeDB){
		setId(id);
		myDHT(tamanoRed,d,id);
		setMensajeIda(true);
		setCamino(new ArrayList<Integer>());
		setVecino(id,tamanoRed);
		obtenerBD(id,sizeDB);
		setTamanoRed(tamanoRed);
		setTamanoCache(tamanoCache);
		setSizeDB(sizeDB);
		setCache(new Stack<String>());
		System.err.print("Nodo: "+id);
		System.out.println("\t\tVecino: "+vecino+"\tDHT: "+DHT+"\t\tCache: "+cache+"\t\tBD: "+DB+"\n");
	}
	//////////////////////////////////////////////////
	// Métodos
	/////////////////////////////////////////////////
	
	// -----------------------------------
	// Método que inicia la base de datos para cada nodo ingresando b cantidad de datos
	// -------------------------------------
	public void obtenerBD(int id, int tamanoBD){
		// Se inicia una lista vacía
		ArrayList<Integer> base = new ArrayList<Integer>();
		// Se obtiene el número del id del nodo, el cual se multiplica por el tamaño de la base 
		// de datos para obtener el número inicial
		int inicial = id * tamanoBD;
		// Se realiza un ciclo para obtener los b números siguientes y añadirlos a la lista
		for(int i=0;i<tamanoBD;i++){
			base.add(inicial);
			inicial++;
		}
		// Se setea la lista obtenida para cada nodo
		setDB(base);
	}
	
	// -----------------------------------
	// Método que calcula el nodo resultante dado su posición y distancia (para DHT)
	// -------------------------------------
	public int calculaSuma(int base, int suma, int tamanoRed){
		if(base+suma>=tamanoRed){
			// Si el id del nodo mas la distancia es mayor al tamaño de la red
			// se le resta el tamaño de la red
			return (base+suma)-tamanoRed;
		}else{
			// Si no se ingresa la suma simple
			return base+suma;
		}
	}
	
	// -----------------------------------
	// Método que calcula el nodo resultante (en sentido inverso) dado su posición y distancia (para DHT)
	// -------------------------------------
	public int calculaResta(int base, int suma, int tamanoRed){
		if(base-suma<0){
			// Si el id del nodo menos la distancia hacia atrás es menor a 0
			// Se le resta al tamaño de la red
			return tamanoRed-(suma-base);
		}else{
			// Si no se ingresa la resta
			return base-suma;
		}
	}
	// -----------------------------------
	// Método que obtiene el DHT para cada nodo dado su id y distancia d
	// -------------------------------------
	public void myDHT(int tamanoRed, int d,int id){
		// Se inicia la lista que guardará los DHT
		ArrayList<Integer> DHT = new ArrayList<Integer>();
		if(tamanoRed%2==0){
			// Si la red tiene una cantidad de nodos par se le asigna
			// el nodo que se encuentra en la mitad de la red
			DHT.add(calculaSuma(id,tamanoRed/2,tamanoRed));
		}else{
			// SI la red tien una cantidad de nodos impar se le asigna
			// el nodo que se encuentra a la mitad + 1
			DHT.add(calculaSuma(id,(tamanoRed/2)+1,tamanoRed));
		}
		// Número que corresponde a 2^2 ya que 2^1 se añade anteriormente
		int numeroMagico = 4; 
		for(int i=0;i<d;i++){
			// Para cada nodo se calcula la distancia n/2^x y se asignan sus DHT
			int distancia = tamanoRed/numeroMagico;
			DHT.add(calculaSuma(id,distancia,tamanoRed));
			DHT.add(calculaResta(id,distancia,tamanoRed));
			// Siguiente denominador
			numeroMagico=numeroMagico*2;
		}

		setDHT(DHT);
	}
	
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
	
	public String imprimeCache(){
		String imprime = "";
		for(int i=0;i<cache.size();i++){
			imprime=imprime+"["+cache.get(i)+"] ";
		}
		return imprime;
	}
	
	public Boolean compruebaDato(int dato){
		if(cache.isEmpty()){
			return false;
		}else{
			for(int i=0;i<cache.size();i++){
				String separador[] = cache.get(i).split(",");
				String comparador = String.valueOf(dato);
				if(separador[1].compareTo(comparador)==0){
					return true;
				}
			}
			return false;
		}
	}
	
	
	public int getVecino(){return vecino;}
	public void setVecino(int id, int tamanoRed){if(id==(tamanoRed-1)){this.vecino=0;}else{this.vecino=id+1;}}
	public int getId(){return id;}
	public void setId(int id){this.id=id;}
	public Stack<String> getCache() {return cache;}
	public void setCache(Stack<String> cache) {this.cache = cache;}
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
