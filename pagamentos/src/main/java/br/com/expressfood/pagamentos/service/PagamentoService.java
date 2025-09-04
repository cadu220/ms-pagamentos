package br.com.expressfood.pagamentos.service;

import br.com.expressfood.pagamentos.dto.PagamentoDto;
import br.com.expressfood.pagamentos.http.PedidoClient;
import br.com.expressfood.pagamentos.model.Pagamento;
import br.com.expressfood.pagamentos.model.Status;
import br.com.expressfood.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Optional;


@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ModelMapper modelMapperp;

    @Autowired
    private PedidoClient pedido;

    public Page<PagamentoDto> obterTodos(Pageable paginacao){
        return pagamentoRepository.findAll(paginacao).map(p -> modelMapperp.map(p, PagamentoDto.class));
    }

    public PagamentoDto obterPorId(Long id){
        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        PagamentoDto dto = modelMapperp.map(pagamento, PagamentoDto.class);
        dto.setPedidos(pedido.obterPedidos(pagamento.getPedidoId()).getItens());
        return dto;
    }

    public PagamentoDto create(PagamentoDto dto){
        Pagamento pagamento = modelMapperp.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        pagamentoRepository.save(pagamento);
        return modelMapperp.map(pagamento, PagamentoDto.class);
    }

    public PagamentoDto update(Long id, PagamentoDto dto){
        Pagamento pagamento = modelMapperp.map(dto, Pagamento.class);
        pagamento.setId(id);
        pagamentoRepository.save(pagamento);
        return modelMapperp.map(pagamento, PagamentoDto.class);
    }
    public void delete(Long id){
        pagamentoRepository.deleteById(id);
    }

    public void confirmarPagamento(Long id){
        Optional<Pagamento> pagamento= pagamentoRepository.findById(id);
        if(!pagamento.isPresent()){
            throw new EntityNotFoundException();

        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        pagamentoRepository.save(pagamento.get());
        pedido.atualizaPagamento(pagamento.get().getPedidoId());
    }

    public void alteraStatus(Long id) {
        Optional<Pagamento> pagamento= pagamentoRepository.findById(id);
        if(!pagamento.isPresent()){
            throw new EntityNotFoundException();

        }

        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        pagamentoRepository.save(pagamento.get());
    }
}
