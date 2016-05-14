import java.io.*;
import java.util.*;
import java.math.*;

class Main{

	public static final double[] theta = {-1.0, -0.5, 0.0, 0.5, 1.0}; // Theta dos alunos 1 ao 5

	public static int N; // Numero de repetições

	public static List<Double> a = new ArrayList<Double>(); // Parâmetro de discriminação
  public static List<Double> b = new ArrayList<Double>(); // Parâmetro de dificuldade

	public static int[][] respostas = new int[2000][100];

	// Métodos auxiliares
	// Escrever arquivos
	private static void escreveArquivo(String arquivo, List<String[]> dados) {
		Iterator<String[]> it = dados.iterator();
		String[] dt;
		String linha;

		try(FileWriter fw = new FileWriter(arquivo, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw))
		{
			while(it.hasNext()) {
				dt = it.next();
				linha = "";
				for(String d : dt) {
					linha += d + " ";
				}
				out.println(linha);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	// Calcula a probabilidade
	public static double prob(double theta, double a, double b){
		double aux = Math.pow(Math.E, a*(theta-b));
		return aux/(1+aux);
	}

	// Ve se acertou a questão
	public static boolean acertou(double theta, double a, double b){
		double prob = prob(theta, a, b);
		double aux = Math.random();
		if(aux <= prob) return true;
		else return false;
	}

	// Ve que nota (quantidade de questões acertadas) o aluno tirou
	public static int nota(Integer[] prova, double theta){

		int cont = 0;
		for (int i : prova){
			if (acertou(theta, (double)a.get(i), (double)b.get(i))) cont++;
		}
		return cont;

	}

	// Retorna melhor prova para o aluno "x" ser melhor que o aluno "y"
	public static Integer[] prova(int quant, double thX, double thY){

		// Quantidade máxima de questões é 100
		if (quant > 100) return new Integer[0];

		HashMap probsX = new HashMap(); // Probabilidades de x acertar as questões
		HashMap probsY = new HashMap(); // Probabilidades de y acertar as questões

		// Mapa com diferença das probabilidades (TreeMap ordena automaticamente)
		Map<Double, Integer> diff = new TreeMap<Double, Integer>();

		// Calcula as probabilidades e coloca nos mapas
		for (int i = 0; i < a.size(); i++){
			probsX.put(i, prob(thX, (double)a.get(i), (double)b.get(i)));
			probsY.put(i, prob(thY, (double)a.get(i), (double)b.get(i)));
		}

		// Calcula a diferença de probabilidades e coloca no TreeMap (Já ordena)
		for (int i = 0; i < probsX.size(); i++){
			diff.put((double)probsX.get(i)-(double)probsY.get(i), i);
		}

		// Encontra a prova com "quant" quantidade de questões (max: 100)
		Integer aux[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), (100-quant), 99);

		return aux;

	}
	// Métodos auxiliares

	// Métodos funcionais
	public static void melhorAluno(){

		// Array com as 100 questões
		Integer[] arr = new Integer[100];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}

		// Cria 4 provas aleatórias de 10, 20, 50 e 100 questões
		Collections.shuffle(Arrays.asList(arr));
		Integer p10[] = Arrays.copyOfRange(arr, 0, 9);
		Collections.shuffle(Arrays.asList(arr));
		Integer p20[] = Arrays.copyOfRange(arr, 0, 19);
		Collections.shuffle(Arrays.asList(arr));
		Integer p50[] = Arrays.copyOfRange(arr, 0, 49);
		Collections.shuffle(Arrays.asList(arr));
		Integer p100[] = Arrays.copyOfRange(arr, 0, 99);

		// Array para calculo das probabilidades
		double[] prob10 = new double[4];
		double[] prob20 = new double[4];
		double[] prob50 = new double[4];
		double[] prob100 = new double[4];

		int notaP10 = nota(p10, theta[4]);
		int notaP20 = nota(p20, theta[4]);
		int notaP50 = nota(p50, theta[4]);
		int notaP100 = nota(p100, theta[4]);

		// Para um numero consideravel de vezes, ve se o aluno "t"[0 a 3] for melhor que o aluno 5[4]
		for(int i = 0; i < N; i++){
			for (int t = 0; t < 4; t++){
				if(notaP10 < nota(p10, theta[t])) prob10[t]++;
				if(notaP20 < nota(p20, theta[t])) prob20[t]++;
				if(notaP50 < nota(p50, theta[t])) prob50[t]++;
				if(notaP100 < nota(p100, theta[t])) prob100[t]++;
			}
		}

		// Calcula as probabilidades: (Total de provas - Quantas provas for melhor) / Total de provas
		for (int teste = 0; teste < 4; teste++){
			prob10[teste] = (N-prob10[teste])/N;
			prob20[teste] = (N-prob20[teste])/N;
			prob50[teste] = (N-prob50[teste])/N;
			prob100[teste] = (N-prob100[teste])/N;
		}

		// Escreve resposta no arquivo
		List<String[]> probList = new ArrayList<String[]>();
		probList.add(Arrays.toString(prob10).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob20).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob50).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob100).split("[\\[\\]]")[1].split(", "));
		escreveArquivo("res/I1.txt", probList);

		System.out.println("5 em relacao a 4, 3, 2 e 1 para "+N+" interacoes:");
		System.out.println("Linha -> Prova[10, 20, 50, 100]");
		System.out.println("Coluna -> Aluno[1, 2, 3, 4]");
		System.out.println(prob10[0] + " , " + prob10[1] + " , " + prob10[2] + " , " + prob10[3]);
		System.out.println(prob20[0] + " , " + prob20[1] + " , " + prob20[2] + " , " + prob20[3]);
		System.out.println(prob50[0] + " , " + prob50[1] + " , " + prob50[2] + " , " + prob50[3]);
		System.out.println(prob100[0] + " , " + prob100[1] + " , " + prob100[2] + " , " + prob100[3]);

	}

	public static void melhorProva(){

		// Seleciona a prova de "x" questões onde o aluno "a" é melhor que o "b"
		Integer p10V[] = prova(10, theta[4], theta[3]);
		Integer p20V[] = prova(20, theta[4], theta[3]);
		Integer p50V[] = prova(50, theta[4], theta[3]);

		// Array para probabilidades
		double[] prob10 = new double[4];
		double[] prob20 = new double[4];
		double[] prob50 = new double[4];

		int notaP10V = nota(p10V, theta[4]);
		int notaP20V = nota(p20V, theta[4]);
		int notaP50V = nota(p50V, theta[4]);

		// Para um numero consideravel de vezes, ve se o aluno "t"[0 a 3] for melhor que o aluno 5[4]
		for(int i = 0; i < N; i++){
			for (int t = 0; t < 4; t++){
				if(notaP10V < nota(p10V, theta[t])) prob10[t]++;
				if(notaP20V < nota(p20V, theta[t])) prob20[t]++;
				if(notaP50V < nota(p50V, theta[t])) prob50[t]++;
			}
		}

		// Calcula as probabilidades
		for (int teste = 0; teste < 4; teste++){
			prob10[teste] = (N-prob10[teste])/N;
			prob20[teste] = (N-prob20[teste])/N;
			prob50[teste] = (N-prob50[teste])/N;
		}

		// Arruma o numero da questão (de 0-99 para 1-100)
		for(int i = 0; i < p10V.length; i++) p10V[i]++;
		for(int i = 0; i < p20V.length; i++) p20V[i]++;
		for(int i = 0; i < p50V.length; i++) p50V[i]++;

		// Escreve o arquivo
		List<String[]> probList = new ArrayList<String[]>();
		probList.add(Arrays.toString(p10V).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob10).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(p20V).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob20).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(p50V).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob50).split("[\\[\\]]")[1].split(", "));
		escreveArquivo("res/I2.txt", probList);


		System.out.println("5 em relacao a 4, 3, 2 e 1 para "+N+" interacoes na melhor prova:");
		System.out.println("Linha -> Prova[10, 20, 50]");
		System.out.println("Coluna -> Aluno[1, 2, 3, 4]");
		System.out.println(prob10[0] + " , " + prob10[1] + " , " + prob10[2] + " , " + prob10[3]);
		System.out.println(prob20[0] + " , " + prob20[1] + " , " + prob20[2] + " , " + prob20[3]);
		System.out.println(prob50[0] + " , " + prob50[1] + " , " + prob50[2] + " , " + prob50[3]);

	}

	public static void intervaloDeConfianca() {

		/*** Mesmo procedimento de melhor prova ***/
		// Seleciona a prova de "x" questões onde o aluno "a" é melhor que o "b"
		Integer p10V[] = prova(10, theta[4], theta[3]);
		Integer p20V[] = prova(20, theta[4], theta[3]);
		Integer p50V[] = prova(50, theta[4], theta[3]);
		Integer p100V[] = prova(100, theta[4], theta[3]);
		/*** Mesmo procedimento de melhor prova ***/

		int[][] notas10  = new int[5][N];
		int[][] notas20  = new int[5][N];
		int[][] notas50  = new int[5][N];
		int[][] notas100 = new int[5][N];

		// Para um numero consideravel de vezes, adiciona notas para cada aluno em cada prova
		for(int i = 0; i < N; i++){
			for (int t = 0; t < 5; t++){
				notas10[t][i] = nota(p10V, theta[t]);
				notas20[t][i] = nota(p20V, theta[t]);
				notas50[t][i] = nota(p50V, theta[t]);
				notas100[t][i] = nota(p100V, theta[t]);
			}
		}

		// Ordena as notas
		for (int i = 0; i < 5; i++){
			Arrays.sort(notas10[i]);
			Arrays.sort(notas20[i]);
			Arrays.sort(notas50[i]);
			Arrays.sort(notas100[i]);
		}


		// Calcula o intervalo de confiança
		double alpha = 0.1;

		int limiteInferior = (int)Math.ceil(N*(alpha/2));
		int limiteSuperior = (int)Math.floor(N-(N*(alpha/2)));

		// Array com as notas somente no intervalo (desnesessário)
		/*
		int[][] intervalo10  = new int[5][N];
		int[][] intervalo20  = new int[5][N];
		int[][] intervalo50  = new int[5][N];
		int[][] intervalo100 = new int[5][N];

		for (int i = 0; i < 5; i++){
			intervalo10[i] = Arrays.copyOfRange(notas10[i], limiteInferior, limiteSuperior);
			intervalo20[i] = Arrays.copyOfRange(notas20[i], limiteInferior, limiteSuperior);
			intervalo50[i] = Arrays.copyOfRange(notas50[i], limiteInferior, limiteSuperior);
			intervalo100[i] = Arrays.copyOfRange(notas100[i], limiteInferior, limiteSuperior);
		}
		*/

		// Acha os valores dos limites para cada prova e para cada aluno
		int[] intervalo10 = new int[10];
		int[] intervalo20 = new int[10];
		int[] intervalo50 = new int[10];
		int[] intervalo100 = new int[10];

		for(int i = 0; i < 9; i=i+2){
			intervalo10[i]   = notas10[i/2][limiteInferior];
			intervalo10[i+1] = notas10[i/2][limiteSuperior];

			intervalo20[i]   = notas20[i/2][limiteInferior];
			intervalo20[i+1] = notas20[i/2][limiteSuperior];

			intervalo50[i]   = notas50[i/2][limiteInferior];
			intervalo50[i+1] = notas50[i/2][limiteSuperior];

			intervalo100[i]   = notas100[i/2][limiteInferior];
			intervalo100[i+1] = notas100[i/2][limiteSuperior];
		}
		// Fim do calculo

		// Escreve o arquivo
		List<String[]> probList = new ArrayList<String[]>();
		probList.add(Arrays.toString(intervalo10).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(intervalo20).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(intervalo50).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(intervalo100).split("[\\[\\]]")[1].split(", "));
		escreveArquivo("res/I3.txt", probList);

		System.out.println("Limite de confianca em provas de 10, 20, 50 e 100 questoes para os 5 alunos:");
		System.out.println("Linha -> Prova[10, 20, 50, 100]");
		System.out.println("Coluna -> Aluno[1(Inf-Sup), 2(Inf-Sup), 3(Inf-Sup), 4(Inf-Sup), 5(Inf-Sup)]");
		System.out.println(intervalo10[0] + " - " + intervalo10[1] + " , " + intervalo10[2] + " - " + intervalo10[3] + " , " + intervalo10[4] + " - " + intervalo10[5] + " , " + intervalo10[6] + " - " + intervalo10[7] + " , " + intervalo10[8] + " - " + intervalo10[9]);
		System.out.println(intervalo20[0] + " - " + intervalo20[1] + " , " + intervalo20[2] + " - " + intervalo20[3] + " , " + intervalo20[4] + " - " + intervalo20[5] + " , " + intervalo20[6] + " - " + intervalo20[7] + " , " + intervalo20[8] + " - " + intervalo20[9]);
		System.out.println(intervalo50[0] + " - " + intervalo50[1] + " , " + intervalo50[2] + " - " + intervalo50[3] + " , " + intervalo50[4] + " - " + intervalo50[5] + " , " + intervalo50[6] + " - " + intervalo50[7] + " , " + intervalo50[8] + " - " + intervalo50[9]);
		System.out.println(intervalo100[0] + " - " + intervalo100[1] + " , " + intervalo100[2] + " - " + intervalo100[3] + " , " + intervalo100[4] + " - " + intervalo100[5] + " , " + intervalo100[6] + " - " + intervalo100[7] + " , " + intervalo100[8] + " - " + intervalo100[9]);

	}

	public static double bissecao (	){
		return -1;
	}

	public static void estimadorPontual (){

		System.out.println(respostas.length);
		System.out.println(respostas[0].length);

		for (int i = 0; i < respostas.length; i++){

		}

	}
	// Métodos funcionais

  public static void main (String[] args) throws FileNotFoundException{
		System.out.println("**********************************************************************");
		System.out.println("Teoria de Resposta ao Item");
		System.out.println("por Kaic Bastidas e Matheus Canon");
		System.out.println("Primeiro semestre de 2016");
		System.out.println("**********************************************************************");

		// Lendo o arquivo de questões e preenchendo lista de parâmetros
		long tempo_inicial = System.nanoTime(); // Informações de duração dos calculos
		Scanner s = new Scanner(new File("questoes.txt"));
		int cont = 0;
		while(s.hasNext()) {
			cont++;
			BigDecimal bd = new BigDecimal(s.next());
			double val = bd.doubleValue();
			if(cont%2 == 0){
				b.add(val);
			} else a.add(val);
		}
		s.close();
		// Fim da leitura

		double diferenca = (System.nanoTime() - tempo_inicial)/1e6;
		System.out.println("Tempo de leitura do arquivo 'questoes.txt' em nanosegundos: " + diferenca);

		// Le o parametro de quantas interações serão feitas para os calculos
		if(args.length == 0) N = 10;
		else N = Integer.parseInt(args[0]);

		// Chamada de Métodos da primeira parte
		System.out.println("**********************************************************************");
		System.out.println("Calculando melhor aluno...");
		tempo_inicial = System.nanoTime();
		melhorAluno(); // I
		diferenca = (System.nanoTime() - tempo_inicial)/1e6;
		System.out.println("Duração do calculo em nanosegundos: " + diferenca);

		System.out.println("**********************************************************************");
		System.out.println("Calculando melhor prova...");
		tempo_inicial = System.nanoTime();
		melhorProva(); // II
		diferenca = (System.nanoTime() - tempo_inicial)/1e6;
		System.out.println("Duração do calculo em nanosegundos: " + diferenca);

		System.out.println("**********************************************************************");
		System.out.println("Calculando intervalo de confianca...");
		tempo_inicial = System.nanoTime();
		intervaloDeConfianca(); // III
		diferenca = (System.nanoTime() - tempo_inicial)/1e6;
		System.out.println("Duração do calculo em nanosegundos: " + diferenca);

		System.out.println("**********************************************************************");


		// Lendo o arquivo de respostas e populando array bidimensional com os acertos/erros
		tempo_inicial = System.nanoTime();
		Scanner s2 = new Scanner(new File("respostas.txt"));
		int aluno = 0;
		int resposta = 0;
		while(s2.hasNext()) {
			if (resposta == 100) break;
			BigDecimal bd = new BigDecimal(s2.next());
			int val = bd.intValue();
			respostas[aluno][resposta] = val;
			aluno++;
			if(aluno == 2000){
				aluno = 0;
				resposta++;
			}
		}
		s2.close();
		// Fim da leitura
		diferenca = (System.nanoTime() - tempo_inicial)/1e6;
		System.out.println("Tempo de leitura do arquivo 'respostas.txt' em nanosegundos: " + diferenca);


		estimadorPontual();

		System.out.println(respostas[1999][99]);


		/*

		http://www.derivative-calculator.net/#expr=log%28%28%28y%29%28%28e%5E%28a%28t-b%29%29%29%2F%28%28e%5E%28a%28t-b%29%29%29%20%2B%201%29%29%29%20%2B%20%28%281-y%29%28%281%29-%28%28e%5E%28a%28t-b%29%29%29%2F%28%28e%5E%28a%28t-b%29%29%20%20%2B%201%29%29%29%29%29%29&diffvar=t


		(a(2y-1)(e^(a(t-b))))/(((e^(a(t-b)))+1)((y(e^(a(t-b))))-y+1))

		(a*(2y-1)*(Math.pow(Math.E, (a(t-b)))))/(((Math.pow(Math.E, (a(t-b))))+1)*((y*(Math.pow(Math.E, (a(t-b)))))-y+1))

		*/

  }
}
