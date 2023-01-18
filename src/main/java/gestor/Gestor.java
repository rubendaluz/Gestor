/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package gestor;

import org.json.simple.JSONObject;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
//import java.util.Timer;

/**
 *
 * @authors 28891 - Romilson Monteiro 28915 - Ruben Da Luz
 */
public class Gestor {

    private static HashMap<String, Utilizador> utilizadores = new HashMap<>();
    private static HashMap<Integer, Backup> backups = new HashMap<>();
    private static HashMap<Integer, Impressao> impressoes = new HashMap<>();

    private static int IdServico = 1, IdPedido = 1;

    public static void main(String[] args) {

        recuperarDadosSalvos();
        Utilizador utilizador;
        do {
            try {
                utilizador = Login();
                switch (utilizador.getTipo()) {
                    case ADIMINISTRADOR ->
                        Administrador();
                    case CONSUMIDOR ->
                        Consumidor();
                    default ->
                        System.out.println("Erro !!!");
                }
            } catch (Exception e) {
                System.out.println("Erro !!!");
            }
        } while (true);

    }

    private static void Administrador() {
        int opcao = 7;
        do {
            try {
                opcao = menuAdministrador();
                switch (opcao) {
                    case 1 ->
                        CRUDutilizador();
                    case 2 ->
                        CRUDserviscos();
                    case 3 ->
                        requisicaoServico();
                    case 4 ->
                        listarServicos();
                    case 0 ->
                        Salvar_Sair();
                    default ->
                        System.out.println("Opcao indisponivel !!!");
                }

            } catch (Exception e) {
                System.out.println("Erro !!!");
            }
        } while (opcao != 5);
    }

    private static void Consumidor() {
        int opcao = 3;

        do {
            try {
                opcao = menuConsumidor();
                switch (opcao) {
                    case 1 ->
                        System.out.println("----------- SERVICOS Disponiveis----------");
                    case 2 ->
                        menuRequisicaoServico();
                    case 0 ->
                        Salvar_Sair();
                    default ->
                        System.out.println("Opcao invalida !!!");
                }
            } catch (Exception e) {
                System.out.println("Erro !!!");
            }

        } while (opcao != 3);
    }

    private static void CRUDutilizador() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        String email;
        do {
            opcao = menuCRUDutilizador();
            switch (opcao) {
                case 1 -> {
                    System.out.println("/---------------   Registo de Utilizador  -------------/");

                    Utilizador user = ReceberDadosUtil();
                    if (user != null) {
                        utilizadores.put(user.getEmail(), user);
                    }

                    System.out.println("/------------------------------------------------------/");
                }
                case 2 -> {
                    System.out.println("/------------- Alteracao dos dados do utilizador --------------/");
                    System.out.print("Email do utilizador:\n\t»");
                    email = scanner.next();

                    if (utilizadores.get(email) != null) {
                        alterarDadosUser(email);
                        System.out.println("Dados do utilizador alterado com Sucesso");
                    } else {
                        System.out.println("Email invalido!!!");
                    }
                    System.out.println("/--------------------------------------------------------------/\n");
                }

                case 3 -> {

                    System.out.println("/-----------------  Remocao de Utilizador  -------------/\n");
                    System.out.print("Email:");
                    email = scanner.next();

                    if (utilizadores.get(email) != null) {
                        removerUser(email);
                        System.out.println("Utilizador removido com Sucesso");
                    } else {
                        System.out.println("Email invalido!!!");
                    }
                    System.out.println("/-------------------------------------------------------/\n");
                }

                case 4 -> {
                    System.out.println("/-------------------- Lista de Utilizadores -----------------------/");
                    for (String email1 : utilizadores.keySet()) {
                        Utilizador user = utilizadores.get(email1);
                        user.print();
                    }
                    System.out.println("/--------------------------------------------------------------/\n");
                }

                case 0 ->
                    Salvar_Sair();
                default ->
                    System.out.println("Erro !!!");
            }
        } while (opcao != 5);

    }

    private static void CRUDserviscos() {
        int opcao, op;
        Scanner scanner = new Scanner(System.in);

        do {
            opcao = menuCRUDserviscos();

            switch (opcao) {
                case 1 -> {
                    System.out.println("/--------- Registro de Servico ----------/");
                    outerLoop:

                    do {
                        op = menuTipoServico();

                        switch (op) {
                            case 1 -> {
                                criarServico(Servico.Tipos.IMPRESSAO);
                                break outerLoop;
                            }
                            case 2 -> {
                                criarServico(Servico.Tipos.BACKUP);
                                break outerLoop;
                            }

                            case 0 ->
                                Salvar_Sair();
                            default ->
                                System.out.println("Tipo de Servico invalido !!!");
                        }
                    } while (op != 3);
                    System.out.println("/-----------------------------------------/\n");
                }
                case 2 -> {

                    System.out.println("/--------- Alteracao dos dados de um Servico ----------/");
                    if (impressoes.isEmpty() && backups.isEmpty()) {
                        System.out.println("Ainda nao foi insirido nenhum Servico");
                    } else {
                        alterarServico();
                    }
                    System.out.println("/-----------------------------------------------------/\n");
                }
                case 3 -> {
                    System.out.println("/-------- Remocao de Servico -------------/\n");
                    System.out.print("ID >> ");
                    int id = scanner.nextInt();
                    if (impressoes.containsKey(id)) {
                        System.out.println("Servico Removido!!");
                        impressoes.remove(id);
                    } else if (backups.containsKey(id)) {
                        backups.remove(id);
                        System.out.println("Servico Removido!!");
                    } else {
                        System.out.println("Servisco nao encontrado!!!");
                    }
                    System.out.println("/------------------------------------------/\n");
                    break;
                }
                case 5 -> {
                    System.out.println("/-------------- Procura de servico  ------------------/\n");
                    System.out.print("ID >>");
                    int id = scanner.nextInt();
                    Impressao imp = impressoes.get(id);
                    if (imp != null) {
                        imp.ptintImp();
                        imp.ptint();
                    } else {
                        System.out.println("Servico com id-[], nao foi encontrado!");
                    }
                    System.out.println("/-----------------------------------------------------/\n");
                    break;
                }
                case 4 -> {
                    System.out.println("/--------- Lista de todos Servico ----------/");
                    System.out.println("Servicos De Impressao :");

                    for (int idImp : impressoes.keySet()) {
                        Impressao imp = impressoes.get(idImp);
                        imp.ptint();
                        imp.ptintImp();
                    }

                    System.out.println("Servicos De Backup :");
                    for (int idBac : backups.keySet()) {
                        Backup ba = backups.get(idBac);
                        ba.ptint();
                        ba.ptintCap();
                    }
                    System.out.println("/-------------------------------------------/\n");
                }
                case 0 ->
                    Salvar_Sair();
                default ->
                    System.out.println("Erro !!!");
            }
        } while (opcao != 6);
    }

    //-----------------------------------------------------------------------------
    //       Funcoes relacionadas ao Utilizador
    //-----------------------------------------------------------------------------
    private static Utilizador Login() {
        Scanner scanner = new Scanner(System.in);
        String email;
        String password;
        if (utilizadores.isEmpty()) {
            Utilizador userA = new Utilizador("Utilizador Administrador", "308706471", "va", "1234", Utilizador.Tipos.ADIMINISTRADOR, "Departamento Tecnico", "Chefe", 1224);
            Utilizador userC = new Utilizador("Utilizador Consumidor", "0000", "exemploC@ipvc.pt", "1234", Utilizador.Tipos.CONSUMIDOR, "Recursos Humanos", "Chefe", 1224);
            utilizadores.put(userA.getEmail(), userA);
            utilizadores.put(userC.getEmail(), userC);

        }

        int opcao;
        Utilizador utilizador;
        do {
            opcao = menuIncial();
            switch (opcao) {

                case 1 -> {
                    System.out.println("/--------------->>> Login <<<<---------------/");
                    System.out.print("[] Email    : ");
                    email = scanner.next();
                    Utilizador user = utilizadores.get(email);
                    if (user != null) {
                        System.out.print("[] Password : ");
                        password = scanner.next();
                        if (user.getPassword().equals(password)) {
                            System.out.println("Ben vindo, " + user.getNome() + "!");
                            utilizador = user;
                            System.out.println("/-------------------------------------------/\n");
                            return utilizador;
                        } else {
                            System.out.println("Password incoreto !!!");
                        }
                    } else {
                        System.out.println("Utilizador com o respectivo email nao encontrado !!!!!!");
                    }

                    System.out.println("/-------------------------------------------/");
                }
                case 0 -> {
                    Salvar_Sair();
                }
                default ->
                    System.out.println("Opção inválida!");
            }
        } while (true);

    }

    // Funcao para receber dados de um utilizador 
    private static Utilizador ReceberDadosUtil() {
        Scanner scanner = new Scanner(System.in);
        Utilizador utilizador;
        Utilizador.Tipos tipo;
        String email, password, nif, nome, departamento, cargo;
        int opcao, numCC;

        System.out.print("Nome: ");
        nome = scanner.next();
        System.out.print("Numero de CC: ");
        numCC = scanner.nextInt();
        System.out.print("Nif: ");
        nif = scanner.next();
        System.out.println("Tipo de Utilizador: ");
        OUTER:
        while (true) {
            System.out.println("\t1 - Adiministrador ");
            System.out.println("\t2 - Consumidor");
            System.out.print(" »»  ");
            opcao = scanner.nextInt();
            switch (opcao) {
                case 1 -> {
                    tipo = Utilizador.Tipos.ADIMINISTRADOR;
                    break OUTER;
                }
                case 2 -> {
                    tipo = Utilizador.Tipos.CONSUMIDOR;
                    break OUTER;
                }
                default ->
                    System.out.println("Opcao invalida!!!");
            }
        }
        System.out.print("Cargo: ");
        cargo = scanner.next();
        System.out.print("Departamento:");
        departamento = scanner.next();
        System.out.println("[] Email:");

        while (true) {
            System.out.print(" »  ");
            email = scanner.next();
            if (utilizadores.containsKey(email)) {
                System.out.println("Esse email ja esta registrado! Incira um diferente");
                System.out.println("Deseja tentar um outro email?");
                System.out.println("\t s - sim");
                System.out.println("\t n - nao");
                System.out.print(" \t»  ");
                char op = scanner.next().charAt(0);
                if (op == 'n') {
                    return null;
                }
            } else {
                break;
            }
        }
        System.out.print("Password: ");
        password = scanner.next();
        utilizador = new Utilizador(nome, nif, email, password, tipo, departamento, cargo, numCC);
        return utilizador;
    }

    //funcao para remover o utilizador de um map
    public static void removerUser(String email) {
        utilizadores.remove(email);
    }

    //funcao para remover o utilizador de um map
    public static void alterarDadosUser(String email) {
        Utilizador user = ReceberDadosUtil();
        utilizadores.put(email, user);
    }

    //-----------------------------------------------------------------------------
    //       Funcoes para guardar e recupear estado de utilizacao da aplicacao
    //-----------------------------------------------------------------------------
    // Funcao que guarda o estado  da aplicacao num ficheiro binario
    public static void recuperarDadosSalvos() {
        try {
            File f1 = new File("utilizadores.bin");
            if (f1.exists()) {
                try ( FileInputStream fis1 = new FileInputStream("utilizadores.bin");  ObjectInputStream ois1 = new ObjectInputStream(fis1)) {
                    utilizadores = (HashMap<String, Utilizador>) ois1.readObject();
                }
            } else {
                utilizadores = new HashMap<>();
            }

            File f2 = new File("impressoes.bin");
            if (f2.exists()) {
                try ( FileInputStream fis2 = new FileInputStream("impressoes.bin");  ObjectInputStream ois2 = new ObjectInputStream(fis2)) {
                    impressoes = (HashMap<Integer, Impressao>) ois2.readObject();
                }
            } else {
                impressoes = new HashMap<>();
            }

            File f3 = new File("backups.bin");
            if (f3.exists()) {
                FileInputStream fis3 = new FileInputStream("backups.bin");
                try ( ObjectInputStream ois3 = new ObjectInputStream(fis3)) {
                    backups = (HashMap<Integer, Backup>) ois3.readObject();
                }
            } else {
                backups = new HashMap<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro na memória !!");
        }
    }

    // Funcao para recuperar e repor o estado da ultima utilizacao da aplicacao que se encontra salvo num ficheiro binario
    public static void Salvar_Sair() {
        try {
            FileOutputStream fos = new FileOutputStream("utilizadores.bin", false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(utilizadores);
            oos.close();
            fos.close();

            FileOutputStream fos1 = new FileOutputStream("impressoes.bin", false);
            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
            oos1.writeObject(impressoes);
            oos1.close();
            fos1.close();

            FileOutputStream fos2 = new FileOutputStream("backups.bin", false);
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(backups);
            oos2.close();
            fos2.close();
        } catch (IOException e) {
            System.out.println("Erro na memória !!");
        }
        // Fechar o progama
        System.out.println("Adeus !!");
        System.exit(0);
    }

    /**
     * Funcoes relacionadas ao servicos
     */
    // Criar um servico e colocar numa map
    public static void criarServico(Servico.Tipos tipo) {
        Scanner scanner = new Scanner(System.in);
        String nome;
        String impressora;
        int capacidade;

        System.out.print("Nome: ");
        nome = scanner.next();

        switch (tipo) {
            case IMPRESSAO -> {
                System.out.print("Nome da impressora: ");
                impressora = scanner.next();
                impressoes.put(IdServico, new Impressao(nome, IdServico, Servico.Estados.DISPONIVEL, impressora));
                System.out.println("\n Servico inserido com sucesso!!!");
                IdServico++;
            }
            case BACKUP -> {
                System.out.print("Capacidade: ");
                capacidade = scanner.nextInt();
                backups.put(IdServico, new Backup(nome, IdServico, Servico.Estados.DISPONIVEL, capacidade));
                System.out.println("\n Servico inserido com sucesso !!!!");
                IdServico++;
            }
        }
    }

    //funcao para alterar dados de um utilizador que esta num map
    public static void alterarServico() {
        Scanner scanner = new Scanner(System.in);
        int op, id;
        outerLoo:
        do {
            op = menuTipoServico();
            switch (op) {
                case 1 -> {
                    if (!impressoes.isEmpty()) {
                        System.out.print("ID do Servico:\n\t»");
                        id = scanner.nextInt();

                        if (impressoes.containsKey(id)) {
                            alterarDadosServico(id, Servico.Tipos.IMPRESSAO);
                            System.out.println("Dados alterado com sucesso!");

                            break outerLoo;
                        } else {
                            System.out.println("Servico  nao existe!");
                        }
                    } else {
                        System.out.println("Nao existe nenhun servico desse tipo!");
                    }
                }
                case 2 -> {
                    if (!backups.isEmpty()) {
                        System.out.print("ID do Servico:\n\t»");
                        id = scanner.nextInt();
                        if (backups.containsKey(id)) {
                            alterarDadosServico(id, Servico.Tipos.BACKUP);
                            break outerLoo;

                        } else {
                            System.out.println("Servico  nao existe!");
                        }
                    } else {
                        System.out.println("Nao existe nenhun servico desse tipo!");
                    }
                }
                case 0 ->
                    Salvar_Sair();
                default ->
                    System.out.println("Tipo de Servico invalido !!!");
            }
        } while (op != 3);
    }

    // Funcao para procurar servico
    public static void procurarServico() {
        Scanner scanner = new Scanner(System.in);
        int id;

        System.out.print("ID do Servico:\n\t»");
        id = scanner.nextInt();
        // procurar na lista de servicos de impressoes
        if (impressoes.containsKey(id)) {
            impressoes.get(id).ptint();
            impressoes.get(id).ptintImp();
        } else if (backups.containsKey(id)) {
            backups.get(id).ptint();
            backups.get(id).ptintCap();
        } else {
            System.out.println("Servisco nao encontrado!!!");
        }

    }

    // funcao responsavel pela requisicao dos servicos
    private static void requisicaoServico() {
        Scanner scanner = new Scanner(System.in);
        int idServico, opcao;

        opcao = menuRequisicaoServico();
        switch (opcao) {
            case 1 -> {
                System.out.print("Id do serviço:");
                idServico = scanner.nextInt();
                if (impressoes.containsKey(idServico)) {
                    impressoes.get(idServico).setEstados(Servico.Estados.OCUPADO);
                    requisitarImpressao(idServico);
                    IdPedido++;
                } else if (backups.containsKey(idServico)) {
                    requisitarBackup(idServico);
                    backups.get(idServico).setEstados(Servico.Estados.OCUPADO);
                    IdPedido++;
                } else {
                    System.out.println("Servico inexistente!!!");
                }
            }
            case 2 -> {
                requisitarServicoNome();
            }

        }
    }

    //funcao para alterar informacoes ou dados de um Servico
    public static void alterarDadosServico(int i, Servico.Tipos tipo) {
        Scanner scanner = new Scanner(System.in);
        String nome;
        String impressora;
        String capacidade;
        System.out.println("Incira os novos dados ou prima enter para manter");
        switch (tipo) {
            case IMPRESSAO -> {
                Impressao oldImpressao = impressoes.get(i);
                System.out.print("Nome do Servico[ " + oldImpressao.getNome() + " ]: ");
                nome = scanner.nextLine();
                if (!nome.equals("")) {
                    impressoes.get(i).setNome(nome);
                }
                System.out.print("Nome da impressora  [ " + oldImpressao.getImpressora() + " ]: ");
                impressora = scanner.nextLine();
                if (!impressora.equals("")) {
                    impressoes.get(i).setImpressora(impressora);
                }

            }
            case BACKUP -> {
                Backup oldBakcup = backups.get(i);
                System.out.print("Nome do Servico  [ " + oldBakcup.getNome() + " ]: ");
                nome = scanner.nextLine();
                if (!nome.equals("")) {
                    backups.get(i).setNome(nome);
                }
                System.out.print("Capacidade  [ " + oldBakcup.getCapacidade() + " ]: ");
                capacidade = scanner.nextLine();
                if (!capacidade.equals("")) {
                    backups.get(i).setCapacidade(Integer.parseInt(capacidade));
                }
            }
        }

    }

    private static int verificarNomeServico(String nome, Servico.Tipos tipo) {
        int n = 0;
        switch (tipo) {
            case IMPRESSAO -> {
                for (int idImp : impressoes.keySet()) {
                    Impressao impressao = impressoes.get(idImp);
                    if (impressao.getNome() == null ? nome == null : impressao.getNome().equals(nome)) {
                        n++;
                    }
                }
            }
            case BACKUP -> {
                for (int idBac : backups.keySet()) {
                    Backup backup = backups.get(idBac);
                    if (backup.getNome() == null ? nome == null : backup.getNome().equals(nome)) {
                        n++;
                    }
                }
            }
        }

        return n;
    }

    public static int procurarServicoNome(String nome, Servico.Tipos tipo) {
        int i = 0;

        switch (tipo) {
            case IMPRESSAO -> {
                for (int idImp : impressoes.keySet()) {
                    Impressao impressao = impressoes.get(idImp);
                    if (impressao.getNome() == null ? nome == null : impressao.getNome().equals(nome)) {
                        return impressao.getID();
                    }
                    i++;
                }
            }
            case BACKUP -> {
                for (int idBac : impressoes.keySet()) {
                    Backup backup = backups.get(idBac);
                    if (backup.getNome() == null ? nome == null : backup.getNome().equals(nome)) {
                        return backup.getID();
                    }
                }
                i++;
            }
        }

        return i;
    }

    // funcao para fazer a requisicao de um servico de empresao
    public static void requisitarImpressao(int idServico) {
        Scanner scanner = new Scanner(System.in);
        enum TipoPag {
            A5, A4, A3, A2, A1
        }
        enum Disposicao {
            HORIZONTAL, VERTICAL
        }
        enum Cor {
            COR, PRETO_BRANCO
        }
        String ficheiro;
        int numCopias;

        JSONObject descricao = new JSONObject();
        char op;

        System.out.println("Nome ou Localizacao do ficheiro:");
        ficheiro = scanner.next();
        descricao.put("ficheiro", ficheiro);

        System.out.println("Numero de Copias: ");
        numCopias = scanner.nextInt();
        descricao.put("numCopias", numCopias);

        System.out.println("Cor: ");
        System.out.print("\t c - cor \n\t p - preto e branco\n >>>");
        op = scanner.next().charAt(0);
        if (op == 'c') {
            descricao.put("cor", Cor.COR);
        } else {
            descricao.put("cor", Cor.PRETO_BRANCO);
        }
        System.out.println("Tipo de Pagina: ");
        System.out.println("\t 1- A1 \t 3- A3 \t 5- A5");
        System.out.print("\t 2- A2 \t 4- A4\n  >>>");
        op = scanner.next().charAt(0);
        switch (op) {
            case 1 ->
                descricao.put("tipoPag", TipoPag.A1);
            case 2 ->
                descricao.put("tipoPag", TipoPag.A2);
            case 3 ->
                descricao.put("tipoPag", TipoPag.A3);
            case 5 ->
                descricao.put("tipoPag", TipoPag.A5);
            default ->
                descricao.put("tipoPag", TipoPag.A4);
        }

        System.out.println("Disposiçao: ");
        System.out.println("\t h - horizontal \n\t v - vertical\n  >>>");
        op = scanner.next().charAt(0);
        if (op == 'h') {
            descricao.put("disposicao", Disposicao.HORIZONTAL);
        } else {
            descricao.put("disposicao", Disposicao.VERTICAL);
        }

        Pedidos pedido = new Pedidos(IdPedido, descricao);
        impressoes.get(idServico).addPedido(pedido);
    }

    //  
    public static void requisitarBackup(int idServico) {
        Scanner scanner = new Scanner(System.in);
        String ficheiro;
        int espacoOcupar;
        JSONObject descricao = new JSONObject();

        System.out.print("Informe o nome do ficheiro: ");
        ficheiro = scanner.next();
        System.out.print("Dimensao do ficheiro(GB): ");
        espacoOcupar = scanner.nextInt();
        descricao.put("ficheiro", ficheiro);
        descricao.put("dimensao", espacoOcupar);

        Pedidos pedido = new Pedidos(IdPedido, descricao);
        backups.get(idServico).addPedido(pedido);
    }

    public static void requisitarServicoNome() {
        Scanner scanner = new Scanner(System.in);
        int n, op;

        System.out.print("Introduza o nome do serviço que deseja:");
        String nomeServico = scanner.next();
        op = menuTipoServico();
        switch (op) {
            case 1 -> {
                n = verificarNomeServico(nomeServico, Servico.Tipos.IMPRESSAO);
                if (n == 1) {
                    for (int id : impressoes.keySet()) {
                        Impressao impressao = impressoes.get(id);
                        if (impressao.getNome() == null ? nomeServico == null : impressao.getNome().equals(nomeServico)) {
                            impressoes.get(id).setEstados(Servico.Estados.OCUPADO);
                            requisitarImpressao(id);
                            IdPedido++;
                        }
                    }
                } else if (n > 1) {
                    System.out.println("Existe mais de um servico com esse nome. Escolha um servico:");
                    for (int id : impressoes.keySet()) {
                        Impressao impressao = impressoes.get(id);
                        if (impressao.getNome() == null ? nomeServico == null : impressao.getNome().equals(nomeServico)) {
                            impressao.ptint();
                        }
                    }
                    System.out.print("id --->>");
                    int id = scanner.nextInt();
                    if (impressoes.containsKey(id)) {
                        impressoes.get(id).setEstados(Servico.Estados.OCUPADO);
                        requisitarImpressao(id);
                        IdPedido++;
                    } else {
                        System.out.println("Servico inexistente!!!");
                    }

                } else {
                    System.out.println("Servico inexistente!!!");
                }
            }

            case 2 -> {
                n = verificarNomeServico(nomeServico, Servico.Tipos.IMPRESSAO);
                if (n == 1) {
                    for (int id : impressoes.keySet()) {
                        Backup backup = backups.get(id);
                        if (backup.getNome() == null ? nomeServico == null : backup.getNome().equals(nomeServico)) {
                            backups.get(id).setEstados(Servico.Estados.OCUPADO);
                            requisitarBackup(id);
                            IdPedido++;
                        }
                    }
                } else if (n > 1) {
                    System.out.println("Existe mais de um servico com esse nome. Escolha um servico:");
                    for (int id : impressoes.keySet()) {
                        Backup backup = backups.get(id);
                        if (backup.getNome() == null ? nomeServico == null : backup.getNome().equals(nomeServico)) {
                            backup.ptint();
                        }
                    }
                    System.out.print("id --->>");
                    int id = scanner.nextInt();
                    if (backups.containsKey(id)) {
                        backups.get(id).setEstados(Servico.Estados.OCUPADO);
                        requisitarBackup(id);
                        IdPedido++;
                    } else {
                        System.out.println("Servico inexistente!!!");
                    }

                } else {
                    System.out.println("Servico inexistente!!!");
                }

            }

        }
    }

    private static int probabilidade() {
        Random random = new Random();
        int chance = random.nextInt(100);
        return chance;
    }

    // Função timer
    private static boolean timer(int tempo) {
        Timer timer = new Timer();
        timer.countDown(tempo * 1000); // conta o tempo necessário para executar o pedido
        return timer.getFlag();
    }

    // Apresentar todos as estatistica
    private static void estatistica() {
        int T_bac = 0, T_imp = 0; // t_bac total de backups efetuadas , T_imp total de impressoes efetuadas/
        int total; //total - no. de utilizacao de todos servico efetuadas
        Servico MaiorUtilizacao = null;
        Servico MenorUtilizacao = null;

        for (int idBac : backups.keySet()) {
            T_bac = T_bac + backups.get(idBac).getNumUtilizacao();
        }
        for (int idImp : impressoes.keySet()) {
            T_imp = T_imp + impressoes.get(idImp).getNumUtilizacao();
        }
        total = T_bac + T_imp;

        int op = menuEstatistica();
        do {
            switch (op) {
                case 1 -> {
                    for (int id : backups.keySet()) {
                        Backup bac = backups.get(id);
                        System.out.println("Backup # " + bac.getNome() + "[" + bac.getID() + "]");
                        System.out.println("/tQuantidade:" + bac.getNumUtilizacao());
                        System.out.println("/tPercentagem:" + bac.getNumUtilizacao() * 100 / total + "%");
                    }
                    for (int id : impressoes.keySet()) {
                        Impressao imp = impressoes.get(id);
                        System.out.println("Impressao  # " + imp.getNome() + "[" + imp.getID() + "]");
                        System.out.println("/tQuantidade:" + imp.getNumUtilizacao());
                        System.out.println("/tPercentagem:" + imp.getNumUtilizacao() * 100 / total + "%");
                    }
                }

                case 2 -> {
                    for (int id : backups.keySet()) {
                        if (MaiorUtilizacao == null || MaiorUtilizacao.getNumUtilizacao() < backups.get(id).getNumUtilizacao()) {
                            MaiorUtilizacao = backups.get(id);
                        }
                        if (MenorUtilizacao == null || MenorUtilizacao.getNumUtilizacao() > backups.get(id).getNumUtilizacao()) {
                            MenorUtilizacao = backups.get(id);
                        }
                    }
                    for (int id : impressoes.keySet()) {
                        if (MaiorUtilizacao == null || MaiorUtilizacao.getNumUtilizacao() < impressoes.get(id).getNumUtilizacao()) {
                            MaiorUtilizacao = impressoes.get(id);
                        }
                        if (MenorUtilizacao == null || MenorUtilizacao.getNumUtilizacao() > impressoes.get(id).getNumUtilizacao()) {
                            MenorUtilizacao = impressoes.get(id);
                        }
                    }
                    System.out.println("Servico com maior Utilizacao:");
                    MaiorUtilizacao.ptint();
                    System.out.println("Servico com menor Utilizacao:");
                    MenorUtilizacao.ptint();
                }
                case 3 -> {
                    System.out.println("Impressao:");
                    System.out.println("/tQuantidade:" + T_imp);
                    System.out.println("/tPercentagem:" + T_imp * 100 / total + "%");

                    System.out.println("Backup:");
                    System.out.println("/tQuantidade:" + T_bac);
                    System.out.println("/tPercentagem:" + T_bac * 100 / total + "%");
                }
                case 4 -> {
                }
                case 0 ->
                    Salvar_Sair();
            }

        } while (op != 4);

    }
    
    private static void atenderPedido(Servico servico){
        int tempo;
        JSONObject descricao = new JSONObject();
        Pedidos pedido = servico.getPrimereiroPedido();
        descricao = pedido.getDescricao();
        if(servico.getTipo() == Servico.Tipos.BACKUP){
            tempo = (int)descricao.get("dimensao");
        }else{
            tempo = (int)descricao.get("numCopias");
        }
        int chance = probabilidade();
        servico.setEstados(Servico.Estados.OCUPADO);
        if(timer(tempo) == true){
            //Terminar atendimento
            if(chance <= 75){
                servico.setEstados(Servico.Estados.DISPONIVEL);
            }else{
                servico.setEstados(Servico.Estados.ERRO);
            }
            servico.apagarPedido();
        }
    }
    
        private static void ordenarServicoNome(){
        Map<Integer, Impressao> sortedMap1 = new TreeMap<>(impressoes);
        sortedMap1.forEach((nome, value) -> {
            sortedMap1.get(nome).ptint();
            sortedMap1.get(nome).ptintImp();
        });
        Map<Integer, Backup> sortedMap2 = new TreeMap<>(backups);
        sortedMap1.forEach((nome, value) -> {
            sortedMap2.get(nome).ptintCap();
            sortedMap2.get(nome).ptint();
        });
    }
    
    private static void ordenarServicoId(){
        Map<Integer, Impressao> sortedMap1 = new TreeMap<>(impressoes);
        sortedMap1.forEach((id, value) -> {
            sortedMap1.get(id).ptint();
            sortedMap1.get(id).ptintImp();
        });
        Map<Integer, Backup> sortedMap2 = new TreeMap<>(backups);
        sortedMap1.forEach((id, value) -> {
            sortedMap2.get(id).ptintCap();
            sortedMap2.get(id).ptint();
        });
    }
    
    private static void ordenarServicoDisponibilidade(){
        Map<Integer, Impressao> sortedMap1 = new TreeMap<>(impressoes);
        sortedMap1.forEach((id, value) -> {
            if(sortedMap1.get(id).getEstado() == Servico.Estados.DISPONIVEL){
                sortedMap1.get(id).ptint();
                sortedMap1.get(id).ptintImp();
            }
            
        });
        Map<Integer, Backup> sortedMap2 = new TreeMap<>(backups);
        sortedMap1.forEach((id, value) -> {
            if(sortedMap2.get(id).getEstado() == Servico.Estados.DISPONIVEL){
                sortedMap2.get(id).ptintCap();
                sortedMap2.get(id).ptint();
            }
            
        });
    }
    
    private static void listarServicos(){
        int op = menuListar();
        switch(op){
            case 1 ->{
                System.out.println("Servicos ordenados por codigo");
                ordenarServicoId();
            }
            case 2 ->{
                System.out.println("Servicos ordenados por nome");
                ordenarServicoNome();
            }
            case 4 ->{
                System.out.println("Servicos ordenados por disponibilidade");
                ordenarServicoDisponibilidade();
            }
        }
    }
    //*****************************************************************************
    //                  Funcoes Menus
    //*****************************************************************************
    private static int menuIncial() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("+============================+");
        System.out.println("|        Menu Inicial        |");
        System.out.println("+============================+");
        System.out.println("| 1 -  Login                 |");
        System.out.println("| 0 -  Fechar o progama      |");
        System.out.println("+----------------------------+");
        System.out.print("Opção? >> ");
        opcao = scanner.nextInt();
        return opcao;
    }

    private static int menuAdministrador() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("+============================================+");
        System.out.println("|            > > > Menu < < <                |");
        System.out.println("+============================================+");
        System.out.println("| 1  -  Operacoes CRUD sobre Utilizador      |");
        System.out.println("| 2  -  Operacoes CRUD sobre Servicos        |");
        System.out.println("| 3  -  Requisitar serviços                  |");
        System.out.println("| 4  -  Consultar Serviços                   |");
        System.out.println("| 5  -  Logout                               |");
        System.out.println("| 0  -  Fechar o progama                     |");
        System.out.println("+--------------------------------------------+");
        System.out.print("Opção? >> ");
        opcao = scanner.nextInt();
        return opcao;
    }

    private static int menuConsumidor() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("+==============================+");
        System.out.println("|      > > > Menu < <          |");
        System.out.println("+==============================+");
        System.out.println("| 1 -  Consultar Serviços      |");
        System.out.println("| 2 -  Requisitar serviços     |");
        System.out.println("| 3 -  Logout                  |");
        System.out.println("| 0 -  Fechar o progama        |");
        System.out.println("+------------------------------+");
        System.out.print("Opção? >> ");
        opcao = scanner.nextInt();
        return opcao;
    }

    private static int menuCRUDserviscos() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("+===========================+");
        System.out.println("|      > > > Menu < <       |");
        System.out.println("+===========================+");
        System.out.println("| 1 -  Insirir Servicos     |");
        System.out.println("| 2 -  Alterar Servicos     |");
        System.out.println("| 3 -  Remover Servicos     |");
        System.out.println("| 4 -  Listar Servicos      |");
        System.out.println("| 5 -  Pesquisar Servicos   |");
        System.out.println("| 6 -  Voltar               |");
        System.out.println("| 0 -  Fechar o progama     |");
        System.out.println("+---------------------------+");
        System.out.print("Opção? >> ");
        opcao = scanner.nextInt();
        return opcao;
    }

    private static int menuCRUDutilizador() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("+==================================+");
        System.out.println("|         > > > Menu < <           |");
        System.out.println("+==================================+");
        System.out.println("| 1 -  Insirir Utilizador          |");
        System.out.println("| 2 -  Alterar dados do Utilizador |");
        System.out.println("| 3 -  Remover Utilizador          |");
        System.out.println("| 4 -  Listar Utilizador           |");
        System.out.println("| 5 -  Voltar                      |");
        System.out.println("| 0 -  Fechar o progama            |");
        System.out.println("+----------------------------------+");
        System.out.print("Opção? >> ");
        opcao = scanner.nextInt();
        return opcao;
    }

    private static int menuRequisicaoServico() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("+===================================+");
        System.out.println("|    > > Requisitar servicos < <    |");
        System.out.println("+===================================+");
        System.out.println("| 1  -  Por codigo;                 |");
        System.out.println("| 2  -  Por nome                    |");
        System.out.println("| 3  -  Por tipo                    |");
        System.out.println("| 4  -  Por mais disponivel         |");
        System.out.println("| 5  -  Voltar                      |");
        System.out.println("+-----------------------------------+");
        System.out.print("Opção? >>");
        opcao = scanner.nextInt();
        return opcao;
    }

    private static int menuListar() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("+===================================+");
        System.out.println("| > > Listar e Ordenar Serviços < < |");
        System.out.println("+===================================+");
        System.out.println("| 1  -  Por codigo;                 |");
        System.out.println("| 2  -  Por nome                    |");
        System.out.println("| 3  -  Por tipo                    |");
        System.out.println("| 4  -  Por mais disponivel         |");
        System.out.println("| 5  -  Voltar                      |");
        System.out.println("+-----------------------------------+");
        System.out.print("Opção? >>");
        opcao = scanner.nextInt();
        return opcao;
    }

    private static int menuTipoServico() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("+===================================+");
        System.out.println("|    Selecione o Tipo de serviço    |");
        System.out.println("+-----------------------------------+");
        System.out.println("| 1 -  Impressao                    |");
        System.out.println("| 2 -  Backup                       |");
        System.out.println("| 3 -  Voltar                       |");
        System.out.println("| 0 -  Fechar o progama             |");
        System.out.println("+-----------------------------------+");
        System.out.print(">> ");
        opcao = scanner.nextInt();
        return opcao;
    }

    private static int menuEstatistica() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("+============================================+");
        System.out.println("|           Estatisticas Disponiveis         |");
        System.out.println("+============================================+");
        System.out.println("| 1 -  Utilização de cada serviço            |");
        System.out.println("| 2 -  Serviços com maior e menor utilização;|");
        System.out.println("| 3 -  Serviços com maior e menor utilização;|");
        System.out.println("| 4 -  Voltar                                |");
        System.out.println("| 0 -  Fechar o progama                      |");
        System.out.println("+--------------------------------------------+");
        System.out.print(">> ");
        opcao = scanner.nextInt();
        return opcao;
    }
}
