
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    @FXML // fx:id="boxCategoria"
    private ComboBox<Country> boxCountry; // Value injected by FXMLLoader
    @FXML
    private Button btnStati;


    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	this.txtResult.clear();
    	String input=this.txtAnno.getText();
    	if(input.isBlank()) {
    		this.txtResult.setText("Inserire un anno per favore");
    		return;
    	}
    	int anno=0;
    	try {
    		anno=Integer.parseInt(input);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("L'anno deve essere scritto in cifre");
    		return;
    	}
    	if(anno<1816 || anno>2016) {
    		this.txtResult.setText("L'anno deve essere compreso tra 1816 e 2016");
    		return;
    	}
    
    	Graph<Country, DefaultEdge> result=this.model.creaGrafo(anno);
    	int componenti=this.model.getNumeroComponentiConnesse();
    	Map<Country,Integer> stampa=this.model.stampaStati();
    	this.txtResult.appendText("Num vertici: "+result.vertexSet().size()+" Num archi: "+result.edgeSet().size()+"\n");
    	this.txtResult.appendText("Numero componenti connesse: "+componenti+"\n");
    	StringBuilder sb=new StringBuilder();
    	for(Country c: stampa.keySet()) {
    		sb.append(c.getName()+" "+stampa.get(c)+"\n");
    	}
    	this.txtResult.appendText(sb.toString());
    	this.boxCountry.getItems().addAll(this.model.getStati());
    }
    
    @FXML
    void doStatiRaggiungibili(ActionEvent event) {
    	this.txtResult.clear();
    	Country c=this.boxCountry.getValue();
    	if(c==null) {
    		this.txtResult.setText("Nessuno stato inserito, ricordarsi anche di selezionare prima un anno e di calcolarne i confini");
    		return;
    	}
    	
    	List<Country> result=this.model.getStatiRaggiungibili(c);
    	StringBuilder sb=new StringBuilder();
    	for(Country s: result) {
    		sb.append(s.getName()+"\n");
    	}
    	this.txtResult.appendText(sb.toString());

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	 assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
         assert boxCountry != null : "fx:id=\"boxCountry\" was not injected: check your FXML file 'Scene.fxml'.";
         assert btnStati != null : "fx:id=\"btnStati\" was not injected: check your FXML file 'Scene.fxml'.";
         assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";


    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    }
}
