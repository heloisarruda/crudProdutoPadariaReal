package controller;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Produto;
import model.ProdutoDAO;

public class crudProdutoController implements Initializable {

	@FXML
	private DatePicker validadeProduto;

	@FXML
	private TextField qtdeNovoProduto;

	@FXML
	private TextField precoNovoProduto;

	@FXML
	private TextField precoProduto;

	@FXML
	private TextField atualizarNomeProduto;

	@FXML
	private TableColumn<Produto, Date> validadeConsultaProduto;

	@FXML
	private TableColumn<Produto, Integer> qtdeConsultaProduto;

	@FXML
	private TextField qtdeProduto;

	@FXML
	private TextField nomeNovoProduto;

	@FXML
	private TableColumn<Produto, Double> precoConsultaProduto;

	@FXML
	private Tab cadastrar;

	@FXML
	private DatePicker fabricacaoProduto;

	@FXML
	private DatePicker validadeNovoProduto;

	@FXML
	private TextField consultarNomeProduto;

	@FXML
	private Tab atualizar;

	@FXML
	private TableView<Produto> tabelaConsultaProdutos;

	@FXML
	private Tab consultar;

	@FXML
	private TableColumn<Produto, Date> fabricacaoConsultaProduto;

	@FXML
	private DatePicker fabricacaoNovoProduto;

	@FXML
	private TableColumn<Produto, Integer> idConsultaProduto;

	@FXML
	private TableColumn<Produto, String> nomeProduto;

	private ProdutoDAO dao;
	
	private Produto produtoSelecionado;

	public void initialize(URL arg0, ResourceBundle arg1) {

		dao = new ProdutoDAO();

		idConsultaProduto.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("id"));
		nomeProduto.setCellValueFactory(new PropertyValueFactory<Produto, String>("nome"));
		fabricacaoConsultaProduto.setCellValueFactory(new PropertyValueFactory<Produto, Date>("dataFabricacao"));
		validadeConsultaProduto.setCellValueFactory(new PropertyValueFactory<Produto, Date>("dataValidade"));
		qtdeConsultaProduto.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("quantidade"));
		precoConsultaProduto.setCellValueFactory(new PropertyValueFactory<Produto, Double>("precoUnitario"));

	}

	@FXML
	private TabPane abas;

	@FXML
	void gerenciarAbas() {
		
		if (cadastrar.isSelected() || consultar.isSelected()){
			atualizar.setDisable(true);
			limparCadastroAtualizacaoProduto();
		}

	}

	@FXML
	void salvarCadastroNovoProduto() {
		
		Produto produto = new Produto();
		
		produto.setNome(nomeNovoProduto.getText());
		produto.setDataFabricacao(Date.valueOf(fabricacaoNovoProduto.getValue()));
		produto.setDataValidade(Date.valueOf(validadeNovoProduto.getValue()));
		produto.setQuantidade(new Integer (qtdeNovoProduto.getText()));
		produto.setPrecoUnitario(new Double(precoNovoProduto.getText()));
		
		try {
			dao.cadastrar(produto);
			exibirDialogoInformacao("Produto cadastrado com sucesso!");
			limparCadastroNovoProduto();
		} catch (Exception e) {
			exibirDialogoErro("Falha ao cadastrar o produto");
			e.printStackTrace();
		}
		

	}

	@FXML
	void limparCadastroNovoProduto() {
		nomeNovoProduto.clear();
		fabricacaoNovoProduto.setValue(null);
		validadeNovoProduto.setValue(null);
		qtdeNovoProduto.clear();
		precoNovoProduto.clear();
		
	}
	
	private void limparCadastroAtualizacaoProduto(){
		nomeNovoProduto.clear();
		fabricacaoNovoProduto.setValue(null);
		validadeNovoProduto.setValue(null);
		qtdeNovoProduto.clear();
		precoNovoProduto.clear();
		
	}

	@FXML
	void consultarProdutos() {
		
		try {
			List<Produto> resultadoConsulta = dao.consultar(consultarNomeProduto.getText());
			
			if(resultadoConsulta.isEmpty()){
				exibirDialogoInformacao("Nenhum produto foi encontrado");
				
			} else {
				
				tabelaConsultaProdutos.setItems(FXCollections.observableList(resultadoConsulta));
			}
		} catch (Exception e) {
			exibirDialogoErro("Falha ao realizar a consulta");
			e.printStackTrace();
		}
	

	}

	@FXML
	void exibirAbaAtualizar() {
		
		produtoSelecionado = tabelaConsultaProdutos.getSelectionModel().getSelectedItem();
		
		if (produtoSelecionado == null){
			exibirDialogoErro("Não há produto selecionado");
		}else {
			atualizar.setDisable(false);
			abas.getSelectionModel().select(atualizar);
			atualizarNomeProduto.setText(produtoSelecionado.getNome());
			fabricacaoProduto.setValue(produtoSelecionado.getDataFabricacao().toLocalDate());
			validadeProduto.setValue(produtoSelecionado.getDataValidade().toLocalDate());
			qtdeProduto.setText(produtoSelecionado.getQuantidade().toString());
			precoProduto.setText(produtoSelecionado.getPrecoUnitario().toString());
			
			
			
		}

	}

	@FXML
	void deletarProduto() {
		
		if (tabelaConsultaProdutos.getSelectionModel().getSelectedItem() == null){
			exibirDialogoErro("Não há produto selecionado");
		} else{
			
			if(exibirDialogoConfirmacao("Confirma a exclusão do produto selecionado?")){
				
				try {
					dao.deletar(tabelaConsultaProdutos.getSelectionModel().getSelectedItem().getId());
					exibirDialogoInformacao("Produto deletado com sucesso!");
					consultarProdutos();
				} catch (Exception e) {
					exibirDialogoErro("Falha ao deletar o produto");
					e.printStackTrace();
				}
			}
		}

	}

	@FXML
	void salvarCadastroProduto() {
		
		produtoSelecionado.setNome(atualizarNomeProduto.getText());
		produtoSelecionado.setDataFabricacao(Date.valueOf(fabricacaoProduto.getValue()));
		produtoSelecionado.setDataValidade(Date.valueOf(validadeProduto.getValue()));
		produtoSelecionado.setQuantidade(new Integer(qtdeProduto.getText()));
		produtoSelecionado.setPrecoUnitario(new Double(precoProduto.getText()));
		
		try {
			
			dao.atualizar(produtoSelecionado);
			exibirDialogoInformacao("O produto foi atualizado com sucesso!");
			abas.getSelectionModel().select(consultar);
			consultarProdutos();
			atualizar.setDisable(true);
			
		} catch (Exception e) {
			exibirDialogoErro("Falha ao atualizar o produto");
			e.printStackTrace();
		}
		

	}
	
	private void exibirDialogoInformacao(String informacao){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Informação");
		alert.setHeaderText(null);
		alert.setContentText(informacao);
		
		alert.showAndWait();
		
	}
	
	private void exibirDialogoErro(String erro){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erro");
		alert.setHeaderText(null);
		alert.setContentText(erro);
		
		alert.showAndWait();
		
	}
	
	private boolean exibirDialogoConfirmacao(String confirmacao){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmação");
		alert.setHeaderText(null);
		alert.setContentText(confirmacao);
		
		Optional <ButtonType> opcao = alert.showAndWait();
		
		if (opcao.get() == ButtonType.OK){
			return true;
		}else
			return false;			
	}
}
