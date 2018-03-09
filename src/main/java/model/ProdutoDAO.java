package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

	private Connection connection;

	public ProdutoDAO() {
		connection = new ConnectionFactory().getConnection();
	}

	public void cadastrar(Produto produto) {

		String sql = "insert into produto ("
				+ "\"nomeProduto\", \"dataFabricacaoProduto\", \"dataValidadeProduto\", \"qtdeProduto\", \"precoUnitarioProduto\") values (?,?,?,?,?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, produto.getNome());
			statement.setDate(2, produto.getDataFabricacao());
			statement.setDate(3, produto.getDataValidade());
			statement.setInt(4, produto.getQuantidade());
			statement.setDouble(5, produto.getPrecoUnitario());

			statement.execute();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public void atualizar(Produto produto) {

		String sql = "update produto set \"nomeProduto\"=?,\"dataFabricacaoProduto\"=?,"
				+ "\"dataValidadeProduto\"=?, \"qtdeProduto\"=?, \"precoUnitarioProduto\"=? where \"idProduto\"=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, produto.getNome());
			statement.setDate(2, produto.getDataFabricacao());
			statement.setDate(3, produto.getDataValidade());
			statement.setInt(4, produto.getQuantidade());
			statement.setDouble(5, produto.getPrecoUnitario());
			statement.setInt(6, produto.getId());

			statement.execute();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public void deletar(Integer idProduto) {

		String sql = "delete from produto where \"idProduto\"=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, idProduto);

			statement.execute();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public List<Produto> consultar(String nomeProduto) {
		List<Produto> produtos = new ArrayList<Produto>();

		String sql = "select * from produto where \"nomeProduto\" like '%" + nomeProduto + "%'";

		try {
			PreparedStatement statement = connection.prepareStatement(sql);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				Produto produto = new Produto();
				produto.setId(resultSet.getInt("idProduto"));
				produto.setNome(resultSet.getString("nomeProduto"));
				produto.setDataFabricacao(resultSet.getDate("dataFabricacaoProduto"));
				produto.setDataValidade(resultSet.getDate("dataValidadeProduto"));
				produto.setQuantidade(resultSet.getInt("qtdeProduto"));
				produto.setPrecoUnitario(resultSet.getDouble("precoUnitarioProduto"));

				produtos.add(produto);

			}

			resultSet.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return produtos;

	}

}
