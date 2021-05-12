package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private BordersDAO dao;
	private Map<Integer, Country> idMap;
	private Graph<Country, DefaultEdge> grafo;


	public Model() {
		
		dao=new BordersDAO();
		

	}
	
	public Graph<Country, DefaultEdge> creaGrafo(int year) {
		this.grafo=new SimpleGraph<>(DefaultEdge.class);
		//devo filtrare i vertici
		idMap=new HashMap<>();
		this.dao.loadAllCountries(idMap, year);
		Graphs.addAllVertices(this.grafo, idMap.values());
		List<Border> confini=this.dao.getCountryPairs(idMap, year);
		for(Border b: confini) {
			Country c1=b.getState1();
			Country c2=b.getState2();
			DefaultEdge e=this.grafo.getEdge(c1, c2);
			//se esiste, non lo aggiungo
			//se non esiste tocca aggiungerlo
			if(e==null) {
				this.grafo.addEdge(c1, c2);
			}
		}
	
		return grafo;

	}
	
	public Collection<Country> getStati() {
		return this.idMap.values();
	}
	
	
	public Map<Country, Integer> stampaStati(){
		Map<Country, Integer> result=new HashMap<>();
		for(Country v: this.grafo.vertexSet()) {
			result.put(v,this.grafo.degreeOf(v) );
		}
		return result;
		
	}
	
	public int getNumeroComponentiConnesse() {
		ConnectivityInspector<Country, DefaultEdge> i=new ConnectivityInspector<>(this.grafo);
		return (i.connectedSets().size());
		//che qui sono in effetti 5, come i continenti
		//le componenti connesse sono separate dal mare
	}
	
	public List<Country> getStatiRaggiungibili(Country partenza) {
		
		BreadthFirstIterator<Country, DefaultEdge> bfv=new BreadthFirstIterator<>(this.grafo, partenza);
		List<Country> result=new ArrayList<>();
		while(bfv.hasNext()) {
			Country c = bfv.next() ;
			result.add(c) ;
		}
		return result;
		
	}
	
	public Country trovaCountry(String s) {
		for(Country c: idMap.values()) {
			if(s.equals(c.getName()))
				return c;
		}
		return null;
	}
	
	
	public List<Country> getStatiRaggiungibiliRicorsione(Country partenza) {
		List<Country> parziale=new ArrayList<>();
		parziale.add(partenza);
		ricorsione(parziale, 0);
		return parziale;
	}
	
	public void ricorsione(List<Country> parziale, int livello) {
		
		Country ultimoVisitato=parziale.get(parziale.size()-1);
		List<Country> vicini=Graphs.neighborListOf(this.grafo, ultimoVisitato);
	//nessun caso terminale.Si finisce quando si sarà esaurito l'ultimo for e 
	//quindi avro analizzato in profondita tutto il grafo
		
		for(Country c: vicini) {
			//se non ho gia visitato..
			if(!parziale.contains(c)) {
				parziale.add(c);
				ricorsione(parziale, livello+1);
				//qui nessun backtracking, la parziale dovra avere tutti i vertici che vado ad analizzare tramite la ricorsione
			}
		}
		
	}
	
	public List<Country> getStatiRaggiungibiliIterativo(Country partenza){
		List<Country> visitati=new ArrayList<Country>();
		List<Country> daVisitare=new ArrayList<Country>();

		daVisitare.add(partenza);
		
		
		while(!daVisitare.isEmpty()) {
			Country c=daVisitare.get(0);
			if(!visitati.contains(c))
				visitati.add(c);
			daVisitare.remove(c);
		for(Country p: Graphs.neighborListOf(grafo, c)){
			if(!visitati.contains(p) && !daVisitare.contains(p))
				daVisitare.add(p);
		}
		}
		
		return visitati;
		
	}
	
	
	

}
