package com.ssafy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.ssafy.util.DBUtil;
import com.ssafy.util.FoodNutritionSAXHandler;
import com.ssafy.util.FoodSAXHandler;
import com.ssafy.util.FoodSaxParser;
import com.ssafy.vo.Food;
import com.ssafy.vo.FoodPageBean;
import com.ssafy.vo.SafeFoodException;

public class FoodDaoImpl implements FoodDao{

	public FoodDaoImpl() {
		loadData();
	}
	/**
	 * 식품 영양학 정보와 식품 정보를  xml 파일에서 읽어온다.
	 * @throws SQLException 
	 */
	public void loadData()  {
		
	  //  FoodNutritionSaxPaser를 이용하여 Food 데이터들을 가져온다
		FoodSaxParser fsp = new FoodSaxParser();
		List<Food> foods = fsp.getFoods();
		/*for (Food food : foods) {
			System.out.println(food);
		}*/
	  //  가져온 Food 리스트 데이터를 DB에 저장한다.	
		Connection con = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			con = DBUtil.getConnection();
			
			String sql = " insert into food(code,name,supportpereat,calory,"
					+ "carbo,protein,fat,sugar,natrium,chole,fattyacid,transfat, maker, material,img, allergy) "
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?) ";

			stmt1 = con.prepareStatement(sql);
			
			for(int i=0;i<foods.size();i++) {
				int idx =1;
				stmt1.setInt(idx++, foods.get(i).getCode());
				stmt1.setString(idx++, foods.get(i).getName());
				
				stmt1.setDouble(idx++, foods.get(i).getSupportpereat());
				stmt1.setDouble(idx++, foods.get(i).getCalory());
				stmt1.setDouble(idx++, foods.get(i).getCarbo());
				stmt1.setDouble(idx++, foods.get(i).getProtein());
				stmt1.setDouble(idx++, foods.get(i).getFat());
				
				stmt1.setDouble(idx++, foods.get(i).getSugar());
				stmt1.setDouble(idx++, foods.get(i).getNatrium());
				stmt1.setDouble(idx++, foods.get(i).getChole());
				stmt1.setDouble(idx++, foods.get(i).getFattyacid());
				stmt1.setDouble(idx++, foods.get(i).getTransfat());
				
				stmt1.setString(idx++, foods.get(i).getMaker());
				stmt1.setString(idx++, foods.get(i).getMaterial());
				stmt1.setString(idx++, foods.get(i).getImg());
				stmt1.setString(idx++, foods.get(i).getAllergy());
				
				stmt1.executeUpdate();
			}
			
			
		} catch (Exception e) {
			//e.printStackTrace();
		}finally {
			DBUtil.close(stmt1);
			DBUtil.close(con);
		}	
		
				
	}
	
	
	/**
	 * 검색 조건(key) 검색 단어(word)에 해당하는 식품 정보(Food)의 개수를 반환. 
	 * web에서 구현할 내용. 
	 * web에서 페이징 처리시 필요 
	 * @param bean  검색 조건과 검색 단어가 있는 객체
	 * @return 조회한  식품 개수
	 */
	public int foodCount(FoodPageBean  bean){

		//구현하세요.
		
		return 0;
	}
	
	/**
	 * 검색 조건(key) 검색 단어(word)에 해당하는 식품 정보(Food)를  검색해서 반환.  
	 * @param bean  검색 조건과 검색 단어가 있는 객체
	 * @return 조회한 식품 목록
	 */
	public List<Food> searchAll(FoodPageBean  bean){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		List<Food> finds = new LinkedList<Food>();
		if(bean !=null) {
			String key = bean.getKey();	//이름, 제조사, 재료
			String word = bean.getWord();	//진라면
				
				
				try {
					con = DBUtil.getConnection();
					if(!key.equals("all") && word!=null && !word.trim().equals("")) {
						String sql = null;
						stmt = con.prepareStatement(sql);
						if(key.equals("name")) {
							 sql = " select * from food where name='?' ";
						}else if(key.equals("maker")){
							 sql = " select * from food where maker='?' ";
						}else if(key.equals("material")){
							sql = " select * from food where material like '%?%' ";
							// select * from food where material like '%육수%'   ;
						}
						
						rs = stmt.executeQuery();
						stmt.setString(1, word);
						
						
					}else {//all
						String sql = " select * from food ";
						stmt = con.prepareStatement(sql);
						rs = stmt.executeQuery();
						
					}
					while (rs.next()) {
						finds.add(new Food( rs.getInt("code"), rs.getString("name"), rs.getString("maker"),rs.getString("material") ));
					}
					return finds;
				} catch (SQLException e) {
					//e.printStackTrace();
				} finally {
					DBUtil.close(rs);
					DBUtil.close(stmt);
					DBUtil.close(con);
				}
				
		}
		return null;
	}
	
	/**
	 * 식품 코드에 해당하는 식품정보를 검색해서 반환. 	
	 * @param code	검색할 식품 코드
	 * @return	식품 코드에 해당하는 식품 정보, 없으면 null이 리턴됨
	 */
	public Food search(int code) {
		
	
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			String sql = " select * from food where code =? ";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, code);
			rs = stmt.executeQuery();

			if (rs.next()) {
				return new Food( 
						rs.getInt("code"),
						rs.getString("name"),
						
						rs.getDouble("supportpereat"),
						rs.getDouble("calory"),
						rs.getDouble("carbo"),
						rs.getDouble("protein"),
						rs.getDouble("fat"),
						rs.getDouble("sugar"),
						rs.getDouble("natrium"),
						rs.getDouble("chole"),
						rs.getDouble("fattyacid"),
						rs.getDouble("transfat"),
						
						rs.getString("maker"),
						rs.getString("material") ,
						rs.getString("img") ,
						rs.getString("allergy") 
						);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(stmt);
			DBUtil.close(con);
		}
		
		return null;
	}

	/**
	 * 가장 많이 검색한 Food  정보 리턴하기 
	 * web에서 구현할 내용.  
	 * @return
	 */
	public List<Food> searchBest() {
		return null;
	}
	
	public List<Food> searchBestIndex() {
		return null;
	}
	
	public static void main(String[] args) {
		FoodDaoImpl dao = new FoodDaoImpl();
		dao.loadData();
		System.out.println(dao.search(1));
		System.out.println("===========================material로 검색=================================");
		print(dao.searchAll(new FoodPageBean("material", "감자전분", null, 0)));
		System.out.println("===========================maker로 검색=================================");
		print(dao.searchAll(new FoodPageBean("maker", "빙그레", null, 0)));
		System.out.println("===========================name으로 검색=================================");
		print(dao.searchAll(new FoodPageBean("name", "라면", null, 0)));
		System.out.println("============================================================");
		print(dao.searchAll(null));
		System.out.println("============================================================");
	}
	
	public static void print(List<Food> foods) {
		for (Food food : foods) {
			System.out.println(food);
		}
	}
}
