package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import model.AvaliacaoDao;
import model.CategoriaDao;
import model.EnderecoDao;
import model.PratoDao;
import model.UsuarioDao;
import modelDominio.Avaliacao;
import modelDominio.Categoria;
import modelDominio.Endereco;
import modelDominio.Prato;
import modelDominio.Usuario;

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
                }else if (comando.equals("AvaliacaoAlterar")){
                    out.writeObject("ok"); 
                    
                    Avaliacao avaliacao = (Avaliacao) in.readObject();
                    
                    AvaliacaoDao avldao = new AvaliacaoDao();
                    avldao.alterar(avaliacao);
                    out.writeObject("ok");
                }else if (comando.equals("AvaliacaoExcluir")){
                    out.writeObject("ok"); 
                    
                    Avaliacao avaliacao = (Avaliacao) in.readObject();
                    
                    AvaliacaoDao avldao = new AvaliacaoDao();
                    avldao.excluir(avaliacao);
                    out.writeObject("ok");
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
                }else if (comando.equals("CategoriaExcluir")){
                    out.writeObject("ok"); 
                    
                    Categoria categoria = (Categoria) in.readObject();
                    
                    CategoriaDao ctdao = new CategoriaDao();
                    ctdao.excluir(categoria);
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
                }else if (comando.equals("UsuarioEfetuarLogin")){
                    out.writeObject("ok"); 
                    
                    Usuario usr = (Usuario) in.readObject();
                    
                    UsuarioDao usrdao = new UsuarioDao(); 
                    Usuario usrselecionado = usrdao.efetuarLogin(usr);
                    out.writeObject(usrselecionado);
                }else if (comando.equals("UsuarioAlterar")){
                    out.writeObject("ok"); 
                    
                    Usuario usr = (Usuario) in.readObject();
                    
                    UsuarioDao usrdao = new UsuarioDao();
                    usrdao.alterar(usr);
                    out.writeObject("ok");                    
                }else if (comando.equals("UsuarioInserir")){
                    out.writeObject("ok"); 
                    
                    Usuario usr = (Usuario) in.readObject();
                    
                    UsuarioDao usrdao = new UsuarioDao();
                    usrdao.inserir(usr);
                    out.writeObject("ok");                    
                }else if (comando.equals("UsuarioExcluir")){
                    out.writeObject("ok"); 

                    Usuario usr = (Usuario) in.readObject();
                    
                    UsuarioDao usrdao = new UsuarioDao();
                    usrdao.excluir(usr);
                    out.writeObject("ok");                    
                }else if (comando.equals("UsuarioLista")){
                    out.writeObject("ok");
                    
                    UsuarioDao usrdao = new UsuarioDao();
                    
                    ArrayList<Usuario> listausr = usrdao.getListaUsuarios();
                    out.writeObject(listausr);
                } else if (comando.equals("PratoInserir")){
                    out.writeObject("ok");
                    
                    Prato prato = (Prato) in.readObject();
                    
                    PratoDao ptdao = new PratoDao();
                    ptdao.inserir(prato);
                    out.writeObject("ok");                    
                }else if (comando.equals("PratoAlterar")){
                    out.writeObject("ok"); 
                    
                    Prato prato = (Prato) in.readObject();
                    
                    PratoDao ptdao = new PratoDao();
                    ptdao.alterar(prato);
                    out.writeObject("ok");                    
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
                }else if (comando.equals("PratoListaNome")){
                    out.writeObject("ok");
                    
                    String nome = (String) in.readObject();
                    
                    PratoDao ptdao = new PratoDao();
                    ArrayList<Prato> listaPrato = ptdao.getListaPratosNome(nome);
                    out.writeObject(listaPrato);
                }else if (comando.equals("PratoListaEmpresa")){
                    out.writeObject("ok");
                    
                    String nome = (String) in.readObject();
                    
                    PratoDao ptdao = new PratoDao();
                    ArrayList<Prato> listaPrato = ptdao.getListaPratoEmpresa(nome);
                    out.writeObject(listaPrato);
                } else if (comando.equals("EnderecoInserir")){
                    out.writeObject("ok");
                    
                    Endereco endereco = (Endereco) in.readObject();
                    
                    EnderecoDao enddao = new EnderecoDao();
                    enddao.inserir(endereco);
                    out.writeObject("ok");                    
                }else if (comando.equals("EnderecoAlterar")){
                    out.writeObject("ok"); 
                    
                    Endereco endereco = (Endereco) in.readObject();
                    
                    EnderecoDao enddao = new EnderecoDao();
                    enddao.alterar(endereco);
                    out.writeObject("ok");                       
                }else if (comando.equals("EnderecoExcluir")){
                    out.writeObject("ok"); 
                    
                    Endereco endereco = (Endereco) in.readObject();
                    
                    EnderecoDao enddao = new EnderecoDao();
                    enddao.excluir(endereco);
                    out.writeObject("ok");                    
                }else{
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
