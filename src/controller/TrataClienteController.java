package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import model.AvaliacaoDao;
import model.CategoriaDao;
import model.CidadeDao;
import model.ClienteDao;
import model.EmpresaDao;
import model.EstadoDao;
import model.PedidoDao;
import model.PratoDao;
import model.PratoPedidoDao;
import model.UsuarioDao;
import modelDominio.Avaliacao;
import modelDominio.Categoria;
import modelDominio.Cidade;
import modelDominio.Cliente;
import modelDominio.Empresa;
import modelDominio.Estado;
import modelDominio.Pedido;
import modelDominio.Prato;
import modelDominio.PratoPedido;
import modelDominio.Usuario;
import view.utils.RecuperarSenha;

public class TrataClienteController extends Thread {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket s;
    private int idUnico;

    public TrataClienteController(Socket s, ObjectInputStream in, ObjectOutputStream out, int idUnico) {
        this.s = s;
        this.in = in;
        this.out = out;
        this.idUnico = idUnico;
        this.start();
    }

    @Override
    public void run() {
        String comando;
        System.out.println("Esperando comandos do cliente");
        try {
            comando = (String) in.readObject();
            
            while (!comando.equals("fim")) {
                System.out.println("Cliente "+idUnico+" enviou comando: "+comando);

                if (comando.equals("AvaliacaoInserir")){
                    out.writeObject("ok");

                    Avaliacao avaliacao = (Avaliacao) in.readObject();

                    AvaliacaoDao avldao = new AvaliacaoDao();
                    if (avldao.inserir(avaliacao) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                }else if (comando.equals("AvaliacaoLista")){
                    out.writeObject("ok");

                    AvaliacaoDao avldao = new AvaliacaoDao();
                    ArrayList<Avaliacao> listaAvaliacao = avldao.getListaAvaliacoes();
                    out.writeObject(listaAvaliacao);
                }else if (comando.equals("CategoriaInserir")){
                    out.writeObject("ok");

                    Categoria categoria = (Categoria) in.readObject();

                    CategoriaDao ctdao = new CategoriaDao();
                    ctdao.inserir(categoria);
                    out.writeObject("ok");
                }else if (comando.equals("CategoriaAlterar")){
                    out.writeObject("ok");

                    Categoria categoria = (Categoria) in.readObject();

                    CategoriaDao ctdao = new CategoriaDao();
                    ctdao.alterar(categoria);
                    out.writeObject("ok");
                }else if (comando.equals("CategoriaLista")){
                    out.writeObject("ok");

                    CategoriaDao ctdao = new CategoriaDao();
                    ArrayList<Categoria> listaCategoria = ctdao.getListaCategorias();
                    out.writeObject(listaCategoria);
                }else if (comando.equals("CategoriaListaNome")){
                    out.writeObject("ok");

                    String nome = (String) in.readObject();

                    CategoriaDao ctdao = new CategoriaDao();
                    ArrayList<Categoria> listaCategoria = ctdao.getListaCategoriasNome(nome);
                    out.writeObject(listaCategoria);
                }else if (comando.equals("BuscarUsuario")){
                    out.writeObject("ok");

                    Usuario usr = (Usuario) in.readObject();

                    UsuarioDao usrdao = new UsuarioDao();
                    Usuario usrselecionado = usrdao.buscarUsuario(usr);
                    
                    out.writeObject(usrselecionado);
                }else if (comando.equals("UsuarioAlterar")){
                    out.writeObject("ok");

                    Usuario usr = (Usuario) in.readObject();

                    UsuarioDao usrdao = new UsuarioDao();
                    
                    if (usrdao.alterar(usr) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                }else if (comando.equals("UsuarioInserir")){
                    out.writeObject("ok");

                    Usuario usr = (Usuario) in.readObject();

                    UsuarioDao usrdao = new UsuarioDao();
                    usrdao.inserir(usr);
                    out.writeObject("ok");
                }else if (comando.equals("RecuperarSenha")){
                    out.writeObject("ok");

                    String emailUsuario = (String) in.readObject();
                    
                    UsuarioDao usrdao = new UsuarioDao();
                    Usuario usuario = usrdao.buscarUsuarioPorEmail(emailUsuario);
                    
                    if (usuario != null) {
                        RecuperarSenha recuperarSenha = new RecuperarSenha();
                        boolean ok = recuperarSenha.enviarEmail(usuario.getEmailUsuario(), usuario.getSenhaUsuario());
                        if (ok) {
                            out.writeObject("ok");                            
                        } else {
                            out.writeObject("nok_recuperar");                            
                        }
                    } else {
                        out.writeObject("nok_usuario");
                    }
                    
                    
                }else if (comando.equals("PratoInserir")){
                    out.writeObject("ok");

                    Prato prato = (Prato) in.readObject();

                    PratoDao ptdao = new PratoDao();
                    if (ptdao.inserir(prato) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                }else if (comando.equals("PratoAlterar")){
                    out.writeObject("ok");

                    Prato prato = (Prato) in.readObject();

                    PratoDao ptdao = new PratoDao();
                    
                    if (ptdao.alterar(prato) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                }else if (comando.equals("PratoExcluir")){
                    out.writeObject("ok");

                    Prato prato = (Prato) in.readObject();

                    PratoDao ptdao = new PratoDao();
                    ptdao.excluir(prato);
                    out.writeObject("ok");
                }else if (comando.equals("PratoLista")){
                    out.writeObject("ok");
                    
                    PratoDao ptdao = new PratoDao();
                    
                    ArrayList<Prato> listaPrato = ptdao.getListaPratos();
                    out.writeObject(listaPrato);
                }else if (comando.equals("PratoListaEmpresa")){
                    out.writeObject("ok");

                    int codEmpresa = (int) in.readObject();

                    PratoDao ptdao = new PratoDao();
                    ArrayList<Prato> listaPrato = ptdao.getListaPratoEmpresa(codEmpresa);
                    out.writeObject(listaPrato);
                } else if (comando.equals("PratoListaEmpresaNome")){
                    out.writeObject("ok");

                    int codEmpresa = (int) in.readObject();
                    String nomePrato = (String) in.readObject();

                    PratoDao ptdao = new PratoDao();
                    ArrayList<Prato> listaPrato = ptdao.getListaPratoEmpresaNome(codEmpresa, nomePrato);
                    out.writeObject(listaPrato);
                } else if(comando.equals("EmpresaInserir")) {
                    out.writeObject("ok");

                    Empresa empresa = (Empresa) in.readObject();

                    EmpresaDao empDao = new EmpresaDao();

                    if (empDao.inserir(empresa) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                }  else if(comando.equals("EmpresaAbertaLista")) {
                    out.writeObject("ok");
                    
                    String nome = (String) in.readObject();
                    String codCategoria = (String) in.readObject();
                    
                    EmpresaDao empDao = new EmpresaDao();
                   
                    ArrayList<Empresa> listaEmpresa = empDao.getListaEmpresasAbertas(nome, codCategoria);
                    out.writeObject(listaEmpresa);
                }  else if(comando.equals("EmpresaFechadaLista")) {
                    out.writeObject("ok");
                    
                    String nome = (String) in.readObject();
                    String codCategoria = (String) in.readObject();

                    EmpresaDao empDao = new EmpresaDao();
                    
                    ArrayList<Empresa> listaEmpresa = empDao.getListaEmpresasFechadas(nome, codCategoria);

                    out.writeObject(listaEmpresa);
                } else if(comando.equals("EmpresaExiste")) {
                    out.writeObject("ok");
                    
                    Empresa empresa = (Empresa) in.readObject();
                    
                    EmpresaDao empDao = new EmpresaDao();

                    out.writeObject(empDao.empresaExiste(empresa));
                } else if(comando.equals("EmpresaEfetuarLogin")) {
                    out.writeObject("ok");

                    Usuario empresa = (Usuario) in.readObject();
                    EmpresaDao empDao = new EmpresaDao();

                    out.writeObject(empDao.efetuarLogin(empresa));
                } else if(comando.equals("ClienteExiste")) {
                    out.writeObject("ok");

                    Cliente cliente = (Cliente) in.readObject();

                    ClienteDao clDao = new ClienteDao();

                    out.writeObject(clDao.usuarioExiste(cliente));
                } else if(comando.equals("ClienteEfetuarLogin")) {
                    out.writeObject("ok");

                    Cliente cliente = (Cliente) in.readObject();

                    ClienteDao clDao = new ClienteDao();

                    out.writeObject(clDao.efetuarLogin(cliente));
                } else if(comando.equals("ClienteInserir")) {
                    out.writeObject("ok");

                    Cliente cliente = (Cliente) in.readObject();

                    ClienteDao clDao = new ClienteDao();

                    if (clDao.inserir(cliente) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                }else if (comando.equals("ClienteAlterar")){
                    out.writeObject("ok");

                    Cliente cliente = (Cliente) in.readObject();

                    ClienteDao clDao = new ClienteDao();
                    clDao.alterar(cliente);
                    out.writeObject("ok");
                } else if(comando.equals("ListaEstados")) {
                    out.writeObject("ok");

                    EstadoDao estDao = new EstadoDao();

                    out.writeObject(estDao.getListaEstados());
                } else if(comando.equals("ListaCidadesEstado")) {
                    out.writeObject("ok");

                    Estado est = (Estado) in.readObject();

                    CidadeDao cidDao = new CidadeDao();

                    out.writeObject(cidDao.getListaCidadesEstado(est));
                } else if (comando.equals("PedidoInserir")){
                    out.writeObject("ok");

                    Pedido pedido = (Pedido) in.readObject();

                    PedidoDao pdDao = new PedidoDao();
                    if (pdDao.inserir(pedido) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                } else if (comando.equals("PedidoAlterar")){
                    out.writeObject("ok");

                    Pedido pedido = (Pedido) in.readObject();

                    PedidoDao pdDao = new PedidoDao();
                    if (pdDao.alterar(pedido) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                } else if(comando.equals("CodigoPedido")) {
                    out.writeObject("ok");

                    PedidoDao pdDao = new PedidoDao();
                   
                    int codPedido = pdDao.getCodPedido();

                    out.writeObject(codPedido);
                } else if (comando.equals("PedidoExcluir")){
                    out.writeObject("ok");

                    int codPedido = (int) in.readObject();

                    PedidoDao pdDao = new PedidoDao();
                    if (pdDao.excluir(codPedido) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                } else if(comando.equals("PedidoAnaliseLista")) {
                    out.writeObject("ok");

                    PedidoDao pdDao = new PedidoDao();
                   
                    ArrayList<Pedido> listaPedido = pdDao.getListaPedidosAnaliseCliente();

                    out.writeObject(listaPedido);
                }  else if(comando.equals("PedidoAprovadoLista")) {
                    out.writeObject("ok");

                    PedidoDao pdDao = new PedidoDao();
                    
                    ArrayList<Pedido> listaPedido = pdDao.getListaPedidosAprovadosCliente();

                    out.writeObject(listaPedido);
                } else if(comando.equals("PedidoReprovadoLista")) {
                    out.writeObject("ok");

                    PedidoDao pdDao = new PedidoDao();
                    
                    ArrayList<Pedido> listaPedido = pdDao.getListaPedidosReprovadosCliente();

                    out.writeObject(listaPedido);
                } else if(comando.equals("PedidoEmpresaAnaliseLista")) {
                    out.writeObject("ok");

                    PedidoDao pdDao = new PedidoDao();
                    
                    int codEmpresa = (int) in.readObject();
                    
                    ArrayList<Pedido> listaPedido = pdDao.getListaPedidosAnaliseEmpresa(codEmpresa);

                    out.writeObject(listaPedido);
                } else if(comando.equals("PedidoEmpresaAprovadoLista")) {
                    out.writeObject("ok");

                    PedidoDao pdDao = new PedidoDao();
                    
                    int codEmpresa = (int) in.readObject();
                    
                    ArrayList<Pedido> listaPedido = pdDao.getListaPedidosAprovadosEmpresa(codEmpresa);

                    out.writeObject(listaPedido);
                } else if(comando.equals("PedidoEmpresaReprovadoLista")) {
                    out.writeObject("ok");

                    PedidoDao pdDao = new PedidoDao();
                    
                    int codEmpresa = (int) in.readObject();
                    
                    ArrayList<Pedido> listaPedido = pdDao.getListaPedidosReprovadosEmpresa(codEmpresa);

                    out.writeObject(listaPedido);
                } else if(comando.equals("ListaPratosPedido")) {
                    out.writeObject("ok");

                    int codPedido = (int) in.readObject();
                    int codEmpresa = (int) in.readObject();

                    PratoPedidoDao pdDao = new PratoPedidoDao();
                    
                    ArrayList<PratoPedido> listaPratoPedido = pdDao.getListaPratosPedido(codPedido, codEmpresa);

                    out.writeObject(listaPratoPedido);
                } else if(comando.equals("PratoPedidoCarrinhoLista")) {
                    out.writeObject("ok");

                    int codPedido = (int) in.readObject();

                    PratoPedidoDao pdDao = new PratoPedidoDao();
                    
                    ArrayList<PratoPedido> listaPratoPedido = pdDao.getListaPedidosCarrinho(codPedido);

                    out.writeObject(listaPratoPedido);
                } else if (comando.equals("PratoPedidoInserir")){
                    out.writeObject("ok");

                    PratoPedido pratoPedido = (PratoPedido) in.readObject();

                    PratoPedidoDao pdDao = new PratoPedidoDao();
                    if (pdDao.inserir(pratoPedido) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                } else if (comando.equals("PratoPedidoExcluir")){
                    out.writeObject("ok");

                    PratoPedido pratoPedido = (PratoPedido) in.readObject();

                    PratoPedidoDao pdDao = new PratoPedidoDao();
                    if (pdDao.excluir(pratoPedido) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                } else if(comando.equals("EmpresaAlterar")) {
                    out.writeObject("ok");

                    Empresa empresa = (Empresa) in.readObject();

                    EmpresaDao empDao = new EmpresaDao();

                    if (empDao.alterar(empresa) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                } else if(comando.equals("BuscarCidade")) {
                    out.writeObject("ok");

                    Cidade cid = (Cidade) in.readObject();

                    CidadeDao cidDao = new CidadeDao();
                    Cidade cidadeSelecionada = cidDao.buscarCidade(cid);
                    
                    out.writeObject(cidadeSelecionada);
                } else if(comando.equals("AbrirFecharempresa")) {
                    out.writeObject("ok");

                    Empresa empresa = (Empresa) in.readObject();

                    EmpresaDao empDao = new EmpresaDao();

                    if (empDao.abrirFecharEmpresa(empresa) == -1){
                        out.writeObject("ok");
                    }else{
                        out.writeObject("nok");
                    }
                } else if(comando.equals("ListaAvaliacoesEmpresa")) {
                    out.writeObject("ok");

                    int codEmpresa = (int) in.readObject();

                    AvaliacaoDao avlDao = new AvaliacaoDao();
                    
                    out.writeObject(avlDao.getListaAvaliacoesEmpresa(codEmpresa));
                } else if(comando.equals("BuscarPrecoMedioEmpresa")) {
                    out.writeObject("ok");

                    int codEmpresa = (int) in.readObject();

                    EmpresaDao empDao = new EmpresaDao();
                    
                    out.writeObject(empDao.buscarPrecoMedioEmpresa(codEmpresa));
                } else {
                    out.writeObject("nok");
                }
                comando = (String)in.readObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }

        try {
            System.out.println("Cliente "+idUnico+" finalizou a conexão");
            this.in.close();
            this.out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
