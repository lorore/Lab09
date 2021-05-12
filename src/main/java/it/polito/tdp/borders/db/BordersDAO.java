package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> idMap, int year) {

		String sql = "SELECT DISTINCT c.state1no, state1ab, StateNme "
				+ "FROM contiguity c, country co "
				+ "WHERE YEAR<=? AND co.CCode=c.state1no AND conttype=1 ";
		
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!idMap.containsKey(rs.getInt("state1no"))) {
					Country c=new Country(rs.getInt("state1no"), rs.getString("state1ab"), rs.getString("StateNme"));
					idMap.put(c.getcCode(), c);

				}

			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error Connection Database");
		}
	}

	
	
	public List<Border> getCountryPairs(Map<Integer, Country> idMap, int year) {
		String sql="SELECT * "
				+ "FROM contiguity c "
				+ "WHERE YEAR<=? AND conttype=1 "
				+ "ORDER BY state1no";
		List<Border> result=new ArrayList<>();
		

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
			int a=	rs.getInt("state1no");
			int b=	rs.getInt("state2no");
			Border border=new Border(idMap.get(a), idMap.get(b));
			
			result.add(border);
			
			}	
				

			
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error Connection Database");
		}
		
		return result;
	}
}
