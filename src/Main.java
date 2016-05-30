import java.io.*;
import java.util.*;
import java.math.*;
import java.text.*;

class Main{

	public static final double[] theta = {-1.0, -0.5, 0.0, 0.5, 1.0}; // Theta dos alunos 1 ao 5

	public static int N; // Quantidade de iterações

	public static List<Double> a = new ArrayList<Double>(); // Parâmetro de discriminação
  public static List<Double> b = new ArrayList<Double>(); // Parâmetro de dificuldade

	public static int[][] respostas = new int[2000][100];

  public static double[] th = new double[2000];

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

		HashMap<Integer, Double> probsX = new HashMap<Integer, Double>(); // Probabilidades de x acertar as questões
		HashMap<Integer, Double> probsY = new HashMap<Integer, Double>(); // Probabilidades de y acertar as questões

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
		Integer aux[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), (100-quant), 100);

		return aux;

	}

	// Retorna um array de 0's e 1's (respostas do aluno)
	public static Integer[] provaResp(Integer[] prova, double theta){

		Integer[] aux = new Integer[prova.length];

		int qst = 0;
		for (int i : prova){
			if (acertou(theta, (double)a.get(i), (double)b.get(i))) aux[qst] = 1;
			else aux[qst] = 0;
			qst++;
		}

		return aux;

	}

	// Encontrar o maximo da função por bisseção (para um aluno "al")
	public static double bissecao(int al){

		double t = 0; // Theta do aluno
		double bis = 5; // Range que o theta pode tomar
		double aux = 1; // Valor inicial da somatoria (para poder iterar o for)

		// Para um numero z de iterações maximas, tenta encontrar o valor em que a equação aproxima-se a zero
		for (int z = 0; z < 100 || aux == 0; z++) {

			aux = 0;

			// Derivada
			for (int i = 0; i < a.size(); i++){
				aux += (a.get(i)*(2*respostas[al][i]-1)*(Math.pow(Math.E, (a.get(i)*(t-b.get(i))))))/(((Math.pow(Math.E, (a.get(i)*(t-b.get(i)))))+1)*((respostas[al][i]*(Math.pow(Math.E, (a.get(i)*(t-b.get(i))))))-respostas[al][i]+1));
			}

			bis /= 2;
			t = aux > 0 ? t + bis : t - bis;

			//System.out.println("Aux: " + aux + " | " + "Bis: " + bis + " | " + "T: " + t + " | " + "Al: " + al);

			if(aux == 0) break;

		}

		return t;

	}

	// Encontrar o maximo da função por bisseção (para uma prova [questões e respostas])
	public static double bissecaoProva(Integer[] quest, Integer[] resp){

		double t = 0; // Theta do aluno
		double bis = 5; // Range que o theta pode tomar
		double aux = 1; // Valor inicial da somatoria (para poder iterar o for)

		// Para um numero z de iterações maximas, tenta encontrar o valor em que a equação aproxima-se a zero
		for (int z = 0; z < 100 || aux == 0; z++) {

			aux = 0;

			// Derivada
			for (int i = 0; i < quest.length; i++){
				aux += (a.get(quest[i])*(2*resp[i]-1)*(Math.pow(Math.E, (a.get(quest[i])*(t-b.get(quest[i]))))))/(((Math.pow(Math.E, (a.get(quest[i])*(t-b.get(quest[i])))))+1)*((resp[i]*(Math.pow(Math.E, (a.get(quest[i])*(t-b.get(quest[i]))))))-resp[i]+1));
			}

			bis /= 2;
			t = aux > 0 ? t + bis : t - bis;

			//System.out.println("Aux: " + aux + " | " + "Bis: " + bis + " | " + "T: " + t);

			if(aux == 0) break;

		}

		return t;

	}

	// Desenha um histograma com 10 colunas
	public static void histograma (double[] thetas){

		int norm = 50; // Valor para normalizar o histograma

		Arrays.sort(thetas);

		// Define um tamanho para as 10 parti��es
		double tamanhoPart = (thetas[thetas.length-1]-thetas[0])/(double)10;
		double[] particoes = {	thetas[0],
								thetas[0]+tamanhoPart,
								thetas[0]+(tamanhoPart*2),
								thetas[0]+(tamanhoPart*3),
								thetas[0]+(tamanhoPart*4),
								thetas[0]+(tamanhoPart*5),
								thetas[0]+(tamanhoPart*6),
								thetas[0]+(tamanhoPart*7),
								thetas[0]+(tamanhoPart*8),
								thetas[0]+(tamanhoPart*9),
								thetas[0]+(tamanhoPart*10)
								};

		// Array que vai armazenar quantos valores existem em cada parti��o
		int[] qntd = new int[particoes.length-1];
		// Populando as parti��es
		for(double i : thetas){
			if (i >= particoes[0] && i < particoes[1])   qntd[0]++;
			if (i >= particoes[1] && i < particoes[2])   qntd[1]++;
			if (i >= particoes[2] && i < particoes[3])   qntd[2]++;
			if (i >= particoes[3] && i < particoes[4])   qntd[3]++;
			if (i >= particoes[4] && i < particoes[5])   qntd[4]++;
			if (i >= particoes[5] && i < particoes[6])   qntd[5]++;
			if (i >= particoes[6] && i < particoes[7])   qntd[6]++;
			if (i >= particoes[7] && i < particoes[8])   qntd[7]++;
			if (i >= particoes[8] && i < particoes[9])   qntd[8]++;
			if (i >= particoes[9] && i <= particoes[10]) qntd[9]++;
		}

		// Normaliza os dados para n�o printar um gr�fico gigantesco
		int[] qntdNorm = new int[qntd.length];
		for (int j = 0; j < qntd.length; j++){
			qntdNorm[j] = (qntd[j]/norm);
		}

		System.out.println("Histograma com linhas de " + norm + " de amplitude:");
		System.out.println();

		// Come�o do print do gr�fico
		// Acha a quantidade m�xima de valores em um conjunto (tamanho m�ximo de uma coluna)
		int[] qntdSort = Arrays.copyOf(qntdNorm, qntd.length);
		Arrays.sort(qntdSort);
				int max = qntdSort[qntdSort.length-1];

		// For entre max e 1, se colocar 0 mostra a linha com 0 elementos
		for (int i = max; i >= 1; i--){

			System.out.printf("%-6s", i*norm); // Printa a amplitude

			for (int j = 0; j < qntdNorm.length; j++){
				if(qntdNorm[j] >= i){
					System.out.printf(" %-6s","#"); // "#" Se tiver valor
				} 	else System.out.printf(" %-6s"," "); // " " Se n�o
			}

			System.out.println();

		}

		System.out.printf("%-7s%-7d%-7d%-7d%-7d%-7d%-7d%-7d%-7d%-7d%d","Qtd ->",qntd[0],
																				qntd[1],
																				qntd[2],
																				qntd[3],
																				qntd[4],
																				qntd[5],
																				qntd[6],
																				qntd[7],
																				qntd[8],
																				qntd[9]);

		// Define quantas casas decimais mostrar nos limites do histograma (recomendado: entre 0 e 3)
		//                                      |||
		DecimalFormat df = new DecimalFormat("#.###");

		System.out.println();
		System.out.printf("%-7s%-7s%-7s%-7s%-7s%-7s%-7s%-7s%-7s%-7s%s","a   ->",
																	df.format(particoes[0]),
																	df.format(particoes[1]),
																	df.format(particoes[2]),
																	df.format(particoes[3]),
																	df.format(particoes[4]),
																	df.format(particoes[5]),
																	df.format(particoes[6]),
																	df.format(particoes[7]),
																	df.format(particoes[8]),
																	df.format(particoes[9]));
		System.out.println();
		System.out.printf("%-7s%-7s%-7s%-7s%-7s%-7s%-7s%-7s%-7s%-7s%s","b   ->",
																	df.format(particoes[1]),
																	df.format(particoes[2]),
																	df.format(particoes[3]),
																	df.format(particoes[4]),
																	df.format(particoes[5]),
																	df.format(particoes[6]),
																	df.format(particoes[7]),
																	df.format(particoes[8]),
																	df.format(particoes[9]),
																	df.format(particoes[10]));

		System.out.println();
		// Fim do print do gr�fico

	}

	// Recebe um inicio e fim em nanosegundos e retorna o tempo em HH:MM:SS
	public static String duracao (double inicio, double fim){

		double diferenca = (fim - inicio)/60000000000D; // Transforma para minutos

		double emHoras = diferenca / 60;
		int soHoras = (int)emHoras;

		double emMinutos = ((emHoras - soHoras) * 60);
		int soMinutos = (int)emMinutos;

		int soSegundos = (int)Math.round(((emMinutos - soMinutos) * 60));

		String str = (String.format("%s(h) %s(m) %s(s)", soHoras, soMinutos, soSegundos));

		return(str);

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

		// Notas do aluno 5
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

		// Escreve o arquivo
		List<String[]> probList = new ArrayList<String[]>();
		probList.add(Arrays.toString(prob10).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob20).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob50).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob100).split("[\\[\\]]")[1].split(", "));
		escreveArquivo("out/I1.txt", probList);

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
		escreveArquivo("out/I2.txt", probList);


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
		escreveArquivo("out/I3.txt", probList);

		System.out.println("Limite de confianca em provas de 10, 20, 50 e 100 questoes para os 5 alunos:");
		System.out.println("Linha -> Prova[10, 20, 50, 100]");
		System.out.println("Coluna -> Aluno[1(Inf-Sup), 2(Inf-Sup), 3(Inf-Sup), 4(Inf-Sup), 5(Inf-Sup)]");
		System.out.println(intervalo10[0] + " - " + intervalo10[1] + " , " + intervalo10[2] + " - " + intervalo10[3] + " , " + intervalo10[4] + " - " + intervalo10[5] + " , " + intervalo10[6] + " - " + intervalo10[7] + " , " + intervalo10[8] + " - " + intervalo10[9]);
		System.out.println(intervalo20[0] + " - " + intervalo20[1] + " , " + intervalo20[2] + " - " + intervalo20[3] + " , " + intervalo20[4] + " - " + intervalo20[5] + " , " + intervalo20[6] + " - " + intervalo20[7] + " , " + intervalo20[8] + " - " + intervalo20[9]);
		System.out.println(intervalo50[0] + " - " + intervalo50[1] + " , " + intervalo50[2] + " - " + intervalo50[3] + " , " + intervalo50[4] + " - " + intervalo50[5] + " , " + intervalo50[6] + " - " + intervalo50[7] + " , " + intervalo50[8] + " - " + intervalo50[9]);
		System.out.println(intervalo100[0] + " - " + intervalo100[1] + " , " + intervalo100[2] + " - " + intervalo100[3] + " , " + intervalo100[4] + " - " + intervalo100[5] + " , " + intervalo100[6] + " - " + intervalo100[7] + " , " + intervalo100[8] + " - " + intervalo100[9]);

	}

	public static void estimadorPontual (){

		int[] sextos = {respostas.length/6,
			              respostas.length/3,
										respostas.length/2,
										(2*respostas.length)/3,
										(5*respostas.length)/6,
										respostas.length};

		Thread thr1 = new Thread() {
			public void run() {
				for (int i = 0; i < sextos[0]; i++){
					th[i] = bissecao(i);
					//System.out.println("Aluno " + i + " | Theta: " + th[i]);
				}
			}
		};

		Thread thr2 = new Thread() {
			public void run() {
				for (int i = sextos[0]; i < sextos[1]; i++){
					th[i] = bissecao(i);
					//System.out.println("Aluno " + i + " | Theta: " + th[i]);
				}
			}
		};

		Thread thr3 = new Thread() {
			public void run() {
				for (int i = sextos[1]; i < sextos[2]; i++){
					th[i] = bissecao(i);
					//System.out.println("Aluno " + i + " | Theta: " + th[i]);
				}
			}
		};

		Thread thr4 = new Thread() {
			public void run() {
				for (int i = sextos[2]; i < sextos[3]; i++){
					th[i] = bissecao(i);
					//System.out.println("Aluno " + i + " | Theta: " + th[i]);
				}
			}
		};

		Thread thr5 = new Thread() {
			public void run() {
				for (int i = sextos[3]; i < sextos[4]; i++){
					th[i] = bissecao(i);
					//System.out.println("Aluno " + i + " | Theta: " + th[i]);
				}
			}
		};

		Thread thr6 = new Thread() {
			public void run() {
				for (int i = sextos[4]; i < sextos[5]; i++){
					th[i] = bissecao(i);
					//System.out.println("Aluno " + i + " | Theta: " + th[i]);
				}
			}
		};

		thr1.start();
		thr2.start();
		thr3.start();
		thr4.start();
		thr5.start();
		thr6.start();

		try {
			thr1.join();
			thr2.join();
			thr3.join();
			thr4.join();
			thr5.join();
			thr6.join();
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}

		// Escreve o arquivo
		List<String[]> probList = new ArrayList<String[]>();
		for(int i = 0; i < th.length; i++){
			String[] esc = new String[1];
			esc[0] = Double.toString(th[i]);
			probList.add(Arrays.toString(esc).split("[\\[\\]]")[1].split(", "));
		}
		escreveArquivo("out/II1.txt", probList);

		double[] pp = Arrays.copyOf(th, th.length);
		Arrays.sort(pp);
		histograma(th);
		System.out.println("Habilidade do aluno:");
		System.out.println("Pior aluno: " + pp[0] + " | Melhor aluno: " + pp[pp.length-1]);
		double sum = 0;
		for (double d : pp) sum += d;
		System.out.println("Media: " + sum/(double)pp.length);

		// int alu = 0;
		// for (double i : th) {
		// 	System.out.println("Theta aluno " + alu + ": " + i);
		// 	alu++;
		// }

	}

	public static void melhorAlunoHab(){

		// Seleciona a prova de "x" questões onde o aluno "a" é melhor que o "b"
		Integer p10V[] = prova(10, theta[4], theta[3]);
		Integer p20V[] = prova(20, theta[4], theta[3]);
		Integer p50V[] = prova(50, theta[4], theta[3]);
		Integer p100V[] = prova(100, theta[4], theta[3]);

		// Array para calculo das probabilidades
		double[] prob10 = new double[4];
		double[] prob20 = new double[4];
		double[] prob50 = new double[4];
		double[] prob100 = new double[4];

		// Para um numero consideravel de vezes, ve se o aluno "t"[0 a 3] for melhor que o aluno 5[4]
		Thread thr1 = new Thread() {
			public void run() {
				for (int i = 0; i < N; i++){
					if(bissecaoProva(p10V,  provaResp(p10V, theta[4]))  < bissecaoProva(p10V, provaResp(p10V, theta[0]))) prob10[0]++;
					if(bissecaoProva(p20V,  provaResp(p20V, theta[4]))  < bissecaoProva(p20V, provaResp(p20V, theta[0]))) prob20[0]++;
					if(bissecaoProva(p50V,  provaResp(p50V, theta[4]))  < bissecaoProva(p50V, provaResp(p50V, theta[0]))) prob50[0]++;
					if(bissecaoProva(p100V, provaResp(p100V, theta[4])) < bissecaoProva(p100V, provaResp(p100V, theta[0]))) prob100[0]++;
				}
			}
		};

		Thread thr2 = new Thread() {
			public void run() {
				for (int i = 0; i < N; i++){
					if(bissecaoProva(p10V,  provaResp(p10V, theta[4]))  < bissecaoProva(p10V, provaResp(p10V, theta[1]))) prob10[1]++;
					if(bissecaoProva(p20V,  provaResp(p20V, theta[4]))  < bissecaoProva(p20V, provaResp(p20V, theta[1]))) prob20[1]++;
					if(bissecaoProva(p50V,  provaResp(p50V, theta[4]))  < bissecaoProva(p50V, provaResp(p50V, theta[1]))) prob50[1]++;
					if(bissecaoProva(p100V, provaResp(p100V, theta[4])) < bissecaoProva(p100V, provaResp(p100V, theta[1]))) prob100[1]++;
				}
			}
		};

		Thread thr3 = new Thread() {
			public void run() {
				for (int i = 0; i < N; i++){
					if(bissecaoProva(p10V,  provaResp(p10V, theta[4]))  < bissecaoProva(p10V, provaResp(p10V, theta[2]))) prob10[2]++;
					if(bissecaoProva(p20V,  provaResp(p20V, theta[4]))  < bissecaoProva(p20V, provaResp(p20V, theta[2]))) prob20[2]++;
					if(bissecaoProva(p50V,  provaResp(p50V, theta[4]))  < bissecaoProva(p50V, provaResp(p50V, theta[2]))) prob50[2]++;
					if(bissecaoProva(p100V, provaResp(p100V, theta[4])) < bissecaoProva(p100V, provaResp(p100V, theta[2]))) prob100[2]++;
				}
			}
		};

		Thread thr4 = new Thread() {
			public void run() {
				for (int i = 0; i < N; i++){
					if(bissecaoProva(p10V,  provaResp(p10V, theta[4]))  < bissecaoProva(p10V, provaResp(p10V, theta[3]))) prob10[3]++;
					if(bissecaoProva(p20V,  provaResp(p20V, theta[4]))  < bissecaoProva(p20V, provaResp(p20V, theta[3]))) prob20[3]++;
					if(bissecaoProva(p50V,  provaResp(p50V, theta[4]))  < bissecaoProva(p50V, provaResp(p50V, theta[3]))) prob50[3]++;
					if(bissecaoProva(p100V, provaResp(p100V, theta[4])) < bissecaoProva(p100V, provaResp(p100V, theta[3]))) prob100[3]++;
				}
			}
		};

		thr1.start();
		thr2.start();
		thr3.start();
		thr4.start();

		try {
			thr1.join();
			thr2.join();
			thr3.join();
			thr4.join();
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}

		// Calcula as probabilidades: (Total de provas - Quantas provas for melhor) / Total de provas
		for (int teste = 0; teste < 4; teste++){
			prob10[teste] = (N-prob10[teste])/N;
			prob20[teste] = (N-prob20[teste])/N;
			prob50[teste] = (N-prob50[teste])/N;
			prob100[teste] = (N-prob100[teste])/N;
		}

		// Escreve o arquivo
		List<String[]> probList = new ArrayList<String[]>();
		probList.add(Arrays.toString(prob10).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob20).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob50).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob100).split("[\\[\\]]")[1].split(", "));
		escreveArquivo("out/II2.txt", probList);

		System.out.println("5 em relacao a 4, 3, 2 e 1 para "+N+" interacoes:");
		System.out.println("Linha -> Prova[10, 20, 50, 100]");
		System.out.println("Coluna -> Aluno[1, 2, 3, 4]");
		System.out.println(prob10[0] + " , " + prob10[1] + " , " + prob10[2] + " , " + prob10[3]);
		System.out.println(prob20[0] + " , " + prob20[1] + " , " + prob20[2] + " , " + prob20[3]);
		System.out.println(prob50[0] + " , " + prob50[1] + " , " + prob50[2] + " , " + prob50[3]);
		System.out.println(prob100[0] + " , " + prob100[1] + " , " + prob100[2] + " , " + prob100[3]);

	}

	public static void intervaloDeConfiancaHab(){

		/*** Mesmo procedimento de melhor prova ***/
		// Seleciona a prova de "x" questões onde o aluno "a" é melhor que o "b"
		Integer p10V[] = prova(10, theta[4], theta[3]);
		Integer p20V[] = prova(20, theta[4], theta[3]);
		Integer p50V[] = prova(50, theta[4], theta[3]);
		Integer p100V[] = prova(100, theta[4], theta[3]);
		/*** Mesmo procedimento de melhor prova ***/

		double[][] notas10  = new double[5][N];
		double[][] notas20  = new double[5][N];
		double[][] notas50  = new double[5][N];
		double[][] notas100 = new double[5][N];

		// Para um numero consideravel de vezes, ve se o aluno "t"[0 a 3] for melhor que o aluno 5[4]
		Thread thr1 = new Thread() {
			public void run() {
				for (int i = 0; i < N; i++){
					notas10[0][i]  = bissecaoProva(p10V, provaResp(p10V, theta[0]));
					notas20[0][i]  = bissecaoProva(p20V, provaResp(p20V, theta[0]));
					notas50[0][i]  = bissecaoProva(p50V, provaResp(p50V, theta[0]));
					notas100[0][i] = bissecaoProva(p100V, provaResp(p100V, theta[0]));
				}
			}
		};

		Thread thr2 = new Thread() {
			public void run() {
				for (int i = 0; i < N; i++){
					notas10[1][i]  = bissecaoProva(p10V, provaResp(p10V, theta[1]));
					notas20[1][i]  = bissecaoProva(p20V, provaResp(p20V, theta[1]));
					notas50[1][i]  = bissecaoProva(p50V, provaResp(p50V, theta[1]));
					notas100[1][i] = bissecaoProva(p100V, provaResp(p100V, theta[1]));
				}
			}
		};

		Thread thr3 = new Thread() {
			public void run() {
				for (int i = 0; i < N; i++){
					notas10[2][i]  = bissecaoProva(p10V, provaResp(p10V, theta[2]));
					notas20[2][i]  = bissecaoProva(p20V, provaResp(p20V, theta[2]));
					notas50[2][i]  = bissecaoProva(p50V, provaResp(p50V, theta[2]));
					notas100[2][i] = bissecaoProva(p100V, provaResp(p100V, theta[2]));
				}
			}
		};

		Thread thr4 = new Thread() {
			public void run() {
				for (int i = 0; i < N; i++){
					notas10[3][i]  = bissecaoProva(p10V, provaResp(p10V, theta[3]));
					notas20[3][i]  = bissecaoProva(p20V, provaResp(p20V, theta[3]));
					notas50[3][i]  = bissecaoProva(p50V, provaResp(p50V, theta[3]));
					notas100[3][i] = bissecaoProva(p100V, provaResp(p100V, theta[3]));
				}
			}
		};

		Thread thr5 = new Thread() {
			public void run() {
				for (int i = 0; i < N; i++){
					notas10[4][i]  = bissecaoProva(p10V, provaResp(p10V, theta[4]));
					notas20[4][i]  = bissecaoProva(p20V, provaResp(p20V, theta[4]));
					notas50[4][i]  = bissecaoProva(p50V, provaResp(p50V, theta[4]));
					notas100[4][i] = bissecaoProva(p100V, provaResp(p100V, theta[4]));
				}
			}
		};

		thr1.start();
		thr2.start();
		thr3.start();
		thr4.start();
		thr5.start();

		try {
			thr1.join();
			thr2.join();
			thr3.join();
			thr4.join();
			thr5.join();
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
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
		double[] intervalo10 = new double[10];
		double[] intervalo20 = new double[10];
		double[] intervalo50 = new double[10];
		double[] intervalo100 = new double[10];

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
		escreveArquivo("out/II3.txt", probList);

		DecimalFormat df = new DecimalFormat("#.##");

		System.out.println("Limite de confianca em provas de 10, 20, 50 e 100 questoes para os 5 alunos:");
		System.out.println("Linha -> Prova[10, 20, 50, 100]");
		System.out.println("Coluna -> Aluno[1(Inf-Sup), 2(Inf-Sup), 3(Inf-Sup), 4(Inf-Sup), 5(Inf-Sup)]");
		System.out.println(df.format(intervalo10[0])  + " - " + df.format(intervalo10[1])  + " , " + df.format(intervalo10[2])  + " - " + df.format(intervalo10[3])  + " , " + df.format(intervalo10[4])  + " - " + df.format(intervalo10[5])  + " , " + df.format(intervalo10[6])  + " - " + df.format(intervalo10[7])  + " , " + df.format(intervalo10[8])  + " - " + df.format(intervalo10[9]));
		System.out.println(df.format(intervalo20[0])  + " - " + df.format(intervalo20[1])  + " , " + df.format(intervalo20[2])  + " - " + df.format(intervalo20[3])  + " , " + df.format(intervalo20[4])  + " - " + df.format(intervalo20[5])  + " , " + df.format(intervalo20[6])  + " - " + df.format(intervalo20[7])  + " , " + df.format(intervalo20[8])  + " - " + df.format(intervalo20[9]));
		System.out.println(df.format(intervalo50[0])  + " - " + df.format(intervalo50[1])  + " , " + df.format(intervalo50[2])  + " - " + df.format(intervalo50[3])  + " , " + df.format(intervalo50[4])  + " - " + df.format(intervalo50[5])  + " , " + df.format(intervalo50[6])  + " - " + df.format(intervalo50[7])  + " , " + df.format(intervalo50[8])  + " - " + df.format(intervalo50[9]));
		System.out.println(df.format(intervalo100[0]) + " - " + df.format(intervalo100[1]) + " , " + df.format(intervalo100[2]) + " - " + df.format(intervalo100[3]) + " , " + df.format(intervalo100[4]) + " - " + df.format(intervalo100[5]) + " , " + df.format(intervalo100[6]) + " - " + df.format(intervalo100[7]) + " , " + df.format(intervalo100[8]) + " - " + df.format(intervalo100[9]));

	}
	// Métodos funcionais

  public static void main (String[] args) throws FileNotFoundException{
		System.out.println("**********************************************************************");
		System.out.println("Teoria de Resposta ao Item");
		System.out.println("por Kaic Bastidas e Matheus Canon");
		System.out.println("Primeiro semestre de 2016");
		System.out.println("**********************************************************************");

		long inicio = System.nanoTime(); // Tempo incial de execução do programa

		// Lendo o arquivo de questões e preenchendo lista de parâmetros
		long tempoInicial = System.nanoTime(); // Informações de duração dos calculos
		Scanner s = new Scanner(new File("res/questoes.txt"));
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
		System.out.println("Tempo de leitura do arquivo 'questoes.txt': " + duracao(tempoInicial, System.nanoTime()));

		// Le o parametro de quantas interações serão feitas para os calculos
		if(args.length == 0) N = 10;
		else N = Integer.parseInt(args[0]);

		// Chamada de Métodos da primeira parte
		System.out.println("**********************************************************************");
		System.out.println("Calculando melhor aluno...");
		tempoInicial = System.nanoTime();
		melhorAluno(); // I
		System.out.println("Duração do calculo: " + duracao(tempoInicial, System.nanoTime()));

		System.out.println("**********************************************************************");
		System.out.println("Calculando melhor prova...");
		tempoInicial = System.nanoTime();
		melhorProva(); // II
		System.out.println("Duração do calculo: " + duracao(tempoInicial, System.nanoTime()));

		System.out.println("**********************************************************************");
		System.out.println("Calculando intervalo de confianca...");
		tempoInicial = System.nanoTime();
		intervaloDeConfianca(); // III
		System.out.println("Duração do calculo: " + duracao(tempoInicial, System.nanoTime()));

		System.out.println("**********************************************************************");

		// Lendo o arquivo de respostas e populando array bidimensional com os acertos/erros
		tempoInicial = System.nanoTime();
		Scanner s2 = new Scanner(new File("res/respostas.txt"));
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
		System.out.println("Tempo de leitura do arquivo 'respostas.txt': " + duracao(tempoInicial, System.nanoTime()));

		/// Chamando métodos da segunda parte
		System.out.println("**********************************************************************");
		System.out.println("Calculando estimador do Theta...");
		tempoInicial = System.nanoTime();
		estimadorPontual(); // IV
		System.out.println("Duração do calculo: " + duracao(tempoInicial, System.nanoTime()));

		System.out.println("**********************************************************************");
		System.out.println("Calculando melhor aluno usando a Habilidade...");
		tempoInicial = System.nanoTime();
		melhorAlunoHab(); // V
		System.out.println("Duração do calculo: " + duracao(tempoInicial, System.nanoTime()));

		System.out.println("**********************************************************************");
		System.out.println("Calculando intervalo de confianca da Habilidade...");
		tempoInicial = System.nanoTime();
		intervaloDeConfiancaHab(); // VI
		System.out.println("Duração do calculo: " + duracao(tempoInicial, System.nanoTime()));

		System.out.println("**********************************************************************");

		// Tempo final da execução do programa
		System.out.println("Duracao da execucao do programa: " + duracao(inicio, System.nanoTime()));

  }
}
