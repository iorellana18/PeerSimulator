package cl.usach.sd;

public class Cache {
	private int dato;
	private int nodo;
	
	public Cache(int dato, int nodo){
		setDato(dato);
		setNodo(nodo);
	}
	
	public void printCache(){
		System.out.print("["+nodo+","+dato+"]");
	}
	
	
	public void setDato(int dato){this.dato=dato;}
	public int getDato(){return dato;}
	public void setNodo(int nodo){this.nodo=nodo;}
	public int getNodo(){return nodo;}
}
