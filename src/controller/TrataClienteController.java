package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import model.AvaliacaoDao;
import model.CategoriaDao;
import model.UsuarioDao;
import modelDominio.Avaliacao;
import modelDominio.Categoria;
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
                    
                    Categoria mc = (Categoria) in.readObject();
                    
                    CategoriaDao ctdao = new CategoriaDao();
                    ctdao.excluir(mc);
                    out.writeObject("ok");                    
                }else if (comando.equals("CategoriaLista")){
                    out.writeObject("ok"); 
                    
                    CategoriaDao mcdao = new CategoriaDao();
                    ArrayList<Categoria> listaCategoria = mcdao.getListaCategorias();
                    out.writeObject(listaCategoria);                    
                }else if (comando.equals("CategpriaListaNome")){
                    out.writeObject("ok");
                    
                    String nome = (String) in.readObject();
                    
                    CategoriaDao ctdao = new CategoriaDao();
                    ArrayList<Categoria> listamarca = ctdao.getListaCategoriasNome(nome);
                    out.writeObject(listamarca);
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
