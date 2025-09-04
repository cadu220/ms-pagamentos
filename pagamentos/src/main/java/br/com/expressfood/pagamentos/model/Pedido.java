package br.com.expressfood.pagamentos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pedido {

    List<ItemDoPedido> itens;
}
