package com.example.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.entity.Produto;


public class ProdutoDAO implements IProdutoDAO{
//	private static final String DBURL="jdbc:mariadb://localhost:3306/EletronicosDB";
//	private static final String DBUSER="root";
//	private static final String DBPASS ="";
//
//	public ProdutoDAO() {
//
//		try {
//			Class.forName("org.mariadb.jdbc.Driver");
//
//		} catch (Exception e) {
//			e.printStackTrace();}
//		}

	private static final String DBURL = "jdbc:mysql://localhost:3306/EletronicosDB"; //Colocar sua conexão
	private static final String DBUSER = "pet"; //Colocar seu user
	private static final String DBPASS = "123456pet"; //Colocar sua senha

	public ProdutoDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); //Colocar sua conexão
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

		@Override
		public void adicionar(Produto p) {
			try {
				Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
				
				   String sql ="INSERT INTO TableProdutos (idProduto, nomeDoProduto, Descricao, quantidadeDePecas, precoUnitario)  "
				   + "VALUES (?, ?, ?, ?, ?)";
						   
                   PreparedStatement stmt = con.prepareStatement(sql);
                   
		               stmt.setInt(1, p.getIdProduto());
		               stmt.setString(2, p.getNomeProduto());
		               stmt.setString(3, p.getDescricaoProduto());
		               stmt.setInt(4, p.getQuantidadePeca());
		               stmt.setDouble(5, p.getPrecoProduto());
		               stmt.executeUpdate();
		               
		               
		         

			} catch (Exception e) {
				e.printStackTrace();}
			
		}

		@Override
	       public List<Produto> pesquisarPorNome(String nome) {
			 List<Produto> encontrados = new ArrayList<>();
		        try {
		            Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		            
		            String sql = "SELECT * FROM TableProdutos WHERE nomeDoProduto like '%" + nome + "%'";
//		            System.out.println(sql);
		            

		            PreparedStatement stmt = con.prepareStatement(sql);
		            ResultSet rs = stmt.executeQuery();

		            while(rs.next()) {

		   
	                
	            	Produto p = new Produto();
	                p.setIdProduto( rs.getInt("idProduto") );
	                p.setNomeProduto( rs.getString("nomeDoProduto") );
	                p.setQuantidadePeca(rs.getInt("quantidadeDePecas"));
	                p.setPrecoProduto( rs.getDouble("precoUnitario") );
	                p.setDescricaoProduto( rs.getString("descricao") );
	               
	             
	                encontrados.add(p);
		            }
		            con.close();
		            
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return encontrados;
		    }

		            
	


		@Override
		public void atualizar(int id, Produto p) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void remover(int idProduto) {
			  try (Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS)) {
		            String sql = "DELETE FROM TableProdutos WHERE idProduto = ?";
//		            System.out.println(sql);
		            PreparedStatement stmt = con.prepareStatement(sql);
		            stmt.setInt(1, idProduto);
		            stmt.executeUpdate();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
			
		}
		


