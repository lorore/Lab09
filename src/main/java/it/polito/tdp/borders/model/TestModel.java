package it.polito.tdp.borders.model;

import java.util.Map;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();

		
//		System.out.println("Creo il grafo relativo al 2000");
//		model.createGraph(2000);
		
//		List<Country> countries = model.getCountries();
//		System.out.format("Trovate %d nazioni\n", countries.size());

//		System.out.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents());
		
//		Map<Country, Integer> stats = model.getCountryCounts();
//		for (Country country : stats.keySet())
//			System.out.format("%s %d\n", country, stats.get(country));	
		model.creaGrafo(2000);
		Country a=model.trovaCountry("Brazil");
	//	System.out.println(model.getStatiRaggiungibiliRicorsione(a));
		//System.out.println(model.getStatiRaggiungibili(a));
	//	System.out.println(model.getStatiRaggiungibiliIterativo(a));
	}

}
