package com.example.boundary;

import com.example.main.CommandProducer;
import com.example.main.StrategyBoundary;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.control.*;
import com.example.entity.*;

public class PedidoBoundary extends CommandProducer implements StrategyBoundary {
    private ComboBox<String> cmbFormaPagamento = new ComboBox<>();
    private ComboBox<String> cmbIdFuncionario = new ComboBox<>();
    private ComboBox<String> cmbIdCliente = new ComboBox<>();

    private PedidoControl control = new PedidoControl();

    private TextField txtIdPedido = new TextField();
    private TextField txtIdCarrinho = new TextField();
    private TextField txtTotal = new TextField();
    private TextField txtDataPedido = new TextField();
    private TextField txtPrevisaoEntrega = new TextField();
    private TextField txtCodigoTransportadora = new TextField();
    private TextField txtFrete = new TextField();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Button btnFinalizarPedido = new Button("Finalizar Pedido");
    private Button btnCancelarPedido = new Button("Cancelar Pedido");
    private Button btnCadastrarCliente = new Button("Cadastrar Cliente");
    private Button btnCadastrarFuncionario = new Button("Cadastrar Funcionario");
    CarrinhoControl carrinhoControl = new CarrinhoControl();

    private void criarSelect() {
        ObservableList<String> formapagamentos =
                FXCollections.observableArrayList("Dinheiro", "Débito", "Crédito", "Pix", "Boleto");
        cmbFormaPagamento.setItems(formapagamentos);

        ObservableList<String> clientes =
                FXCollections.observableArrayList(control.localizarCliente());
        cmbIdCliente.setItems(clientes);

        ObservableList<String> funcionarios =
                FXCollections.observableArrayList(control.localizarFuncionario());
        cmbIdFuncionario.setItems(funcionarios);
    }

    private void setarDadosDoCarrinho() {
        int codigoCarrinho = carrinhoControl.verificarCodigoCarrinho() - 1;

        double total = control.calcularPrecoTotal(codigoCarrinho);
        double frete = (total * 0.10);


        txtIdPedido.setText(String.valueOf(codigoCarrinho));
        txtFrete.setText(String.valueOf(frete));
        txtTotal.setText(String.valueOf(total));

        LocalDate dateTime = LocalDate.now().plusDays(5);
        String previsao = dateTime.format(this.dtf);
        txtPrevisaoEntrega.setText(previsao);
    }

    @Override
    public Pane render () {
        BorderPane panPrincipal = new BorderPane();
        GridPane panCampos = new GridPane();
        panCampos.setPadding(new Insets(0, 10, 10, 10));
        panCampos.setHgap(10.0);
        panCampos.setVgap(20.0);

        txtIdPedido.setEditable(false);
        txtIdCarrinho.setEditable(false);
        txtDataPedido.setEditable(false);
        txtPrevisaoEntrega.setEditable(false);
        txtTotal.setEditable(false);
        txtFrete.setEditable(false);

        txtPrevisaoEntrega.setDisable(true);
        txtTotal.setDisable(true);
        txtFrete.setDisable(true);
        txtIdPedido.setDisable(true);
        txtIdCarrinho.setDisable(true);
        txtDataPedido.setDisable(true);

        Bindings.bindBidirectional(txtPrevisaoEntrega.textProperty(), control.previsaoDeEntrega, new LocalDateStringConverter());
        Bindings.bindBidirectional(cmbFormaPagamento.valueProperty(), control.formaPagamento);
        Bindings.bindBidirectional(txtFrete.textProperty(), control.custoFrete, new NumberStringConverter());
        Bindings.bindBidirectional(txtCodigoTransportadora.textProperty(), control.codigoTransportadora);
        Bindings.bindBidirectional(txtIdPedido.textProperty(), control.idPedido, new NumberStringConverter());
        Bindings.bindBidirectional(cmbIdCliente.valueProperty(), control.idCliente);
        Bindings.bindBidirectional(cmbIdFuncionario.valueProperty(), control.idFuncionario);
        Bindings.bindBidirectional(txtIdCarrinho.textProperty(), control.idCarrinho, new NumberStringConverter());
        Bindings.bindBidirectional(txtDataPedido.textProperty(), control.dataDoPedido, new LocalDateStringConverter());


        panCampos.add(new Label("Frete: "), 1, 4);
        panCampos.add(txtFrete, 2, 4);

        panCampos.add(new Label("Previsão de Entrega: "), 1, 5);
        panCampos.add(txtPrevisaoEntrega, 2, 5);

        panCampos.add(new Label("Código de Transportadora: "), 3, 5);
        panCampos.add(txtCodigoTransportadora, 4, 5);

        panCampos.add(new Label("Total à pagar: "), 3, 4);
        panCampos.add(txtTotal, 4, 4);

        panCampos.add(new Label("Forma de Pagamento: "), 1, 6);
        panCampos.add(cmbFormaPagamento, 2, 6);
        cmbFormaPagamento.setPrefWidth(250);

        panCampos.add(btnCancelarPedido, 2, 9);
        btnCancelarPedido.setOnAction((e) -> {
            carrinhoControl.limparCarrinho();
            executeCommand("BOUNDARY-CARRINHO");
        });
        btnCancelarPedido.setPrefWidth(250);

        panCampos.add(btnFinalizarPedido, 4, 9);
        btnFinalizarPedido.setOnAction((e)  -> {
            control.salvarPedido();
            executeCommand("BOUNDARY-LOGIN");
        });
        btnFinalizarPedido.setPrefWidth(250);

        panCampos.add(btnCadastrarCliente, 3, 2);
        btnCadastrarCliente.setOnAction((e) -> {
            executeCommand("BOUNDARY-USUARIO");
        });

        panCampos.add(btnCadastrarFuncionario, 3, 3);
        btnCadastrarFuncionario.setOnAction((e) -> {
            executeCommand("BOUNDARY-USUARIO");
        });

        panCampos.add(new Label("Pedido Nº: "), 1, 1);
        panCampos.add(txtIdPedido, 2, 1);


        panCampos.add(new Label("Cliente: "), 1, 2);
        panCampos.add(cmbIdCliente, 2, 2);
        cmbIdCliente.setPrefWidth(250);

        panCampos.add(new Label("Funcionário: "), 1, 3);
        panCampos.add(cmbIdFuncionario, 2, 3);
        cmbIdFuncionario.setPrefWidth(250);

        panCampos.add(new Label("Data do Pedido: "), 3, 1);
        panCampos.add(txtDataPedido, 4, 1);

        this.setarDadosDoCarrinho();
        this.criarSelect();
        panPrincipal.setTop(panCampos);
        return (panPrincipal);
    }
}
