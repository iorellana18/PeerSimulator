package cl.usach.sd;

import java.util.ArrayList;

public class Cache {
	private int tamanoCache;
	private ArrayList<Integer> nodos;
	private ArrayList<Integer> datos;
	
	public Cache(int tamanoCache){
		setTamanoCache(tamanoCache);
	}
	
	
	public void setNodos(ArrayList<Integer> nodos){this.nodos=nodos;}
	public ArrayList<Integer> getNodos(){return nodos;}
	public void setDatos(ArrayList<Integer> datos){this.datos=datos;}
	public ArrayList<Integer> getDatos(){return datos;}
	public void setTamanoCache(int tamanoCache){this.tamanoCache=tamanoCache;}
	public int getTamanoCache(){return tamanoCache;}
}
