import java.io.*;
import java.util.*;
import java.math.*;

class Main{
	
	public static final double[] theta = {-1.0, -0.5, 0.0, 0.5, 1.0};
	
	public static final int N = 1000; // Numero de repetições
	
	public static List<Double> a = new ArrayList<Double>(); // Parâmetro de discriminação
    public static List<Double> b = new ArrayList<Double>(); // Parâmetro de dificuldade
	
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
		
		// Para um numero consideravel de vezes, ve se o aluno "t"[0 a 3] for melhor que o aluno 5[4]
		for(int i = 0; i < N; i++){
			for (int t = 0; t < 4; t++){
				if(nota(p10, theta[4]) < nota(p10, theta[t])) prob10[t]++;
				if(nota(p20, theta[4]) < nota(p20, theta[t])) prob20[t]++;
				if(nota(p50, theta[4]) < nota(p50, theta[t])) prob50[t]++;
				if(nota(p100, theta[4]) < nota(p100, theta[t])) prob100[t]++;
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
		escreveArquivo("I1.txt", probList);

		System.out.println("5 em relacao a 4, 3, 2 e 1 para "+N+" interacoes:");
		System.out.println("Linha -> Prova[10, 20, 50, 100] / Coluna -> Aluno[1, 2, 3, 4]");
		System.out.println(prob10[0] + " , " + prob10[1] + " , " + prob10[2] + " , " + prob10[3]);
		System.out.println(prob20[0] + " , " + prob20[1] + " , " + prob20[2] + " , " + prob20[3]);
		System.out.println(prob50[0] + " , " + prob50[1] + " , " + prob50[2] + " , " + prob50[3]);
		System.out.println(prob100[0] + " , " + prob100[1] + " , " + prob100[2] + " , " + prob100[3]);
		
	}
	
	public static void melhorProva(){
		
		// Probabilidade de acertos do aluno 5 e 4
		HashMap probs5 = new HashMap();
		HashMap probs4 = new HashMap();
		
		Map<Double, Integer> diff = new TreeMap<Double, Integer>(); // Tree map manter ordenado pelo valor da diferença
	
		// Cria um mapa com a probabilidade de acerto para cada questão para o aluno 5 e 4
		for (int i = 0; i < a.size(); i++){
			probs5.put(i, prob(1, (double)a.get(i), (double)b.get(i)));
			probs4.put(i, prob(0.5, (double)a.get(i), (double)b.get(i)));
		}

		// Calcula a diferença entre as probabilidades
		for (int i = 0; i < probs5.size(); i++){
			diff.put((double)probs5.get(i)-(double)probs4.get(i), i);
		}
/*
		for (Map.Entry<Double, Integer> entry : diff.entrySet()) {
			System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
		}
*/		
		// Seleciona as 10, 20 e 50 primeiras questões (maiores diferenças)
		Integer p10V[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), 90, 99);
		Integer p20V[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), 80, 99);
		Integer p50V[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), 50, 99);

		// Array para probabilidades
		double[] prob10 = new double[4];
		double[] prob20 = new double[4];
		double[] prob50 = new double[4];
		
		// Para um numero consideravel de vezes, ve se o aluno "t"[0 a 3] for melhor que o aluno 5[4]
		for(int i = 0; i < N; i++){
			for (int t = 0; t < 4; t++){
				if(nota(p10V, theta[4]) < nota(p10V, theta[t])) prob10[t]++;
				if(nota(p20V, theta[4]) < nota(p20V, theta[t])) prob20[t]++;
				if(nota(p50V, theta[4]) < nota(p50V, theta[t])) prob50[t]++;
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
		escreveArquivo("I2.txt", probList);
		
		
		System.out.println("5 em relacao a 4, 3, 2 e 1 para "+N+" interacoes na melhor prova:");
		System.out.println("Linha -> Prova[10, 20, 50] / Coluna -> Aluno[1, 2, 3, 4]");
		System.out.println(prob10[0] + " , " + prob10[1] + " , " + prob10[2] + " , " + prob10[3]);
		System.out.println(prob20[0] + " , " + prob20[1] + " , " + prob20[2] + " , " + prob20[3]);
		System.out.println(prob50[0] + " , " + prob50[1] + " , " + prob50[2] + " , " + prob50[3]);
	
	}

	public static void intervaloDeConfianca() {
		HashMap probs5 = new HashMap();
		HashMap probs4 = new HashMap();
		Map<Double, Integer> diff = new TreeMap<Double, Integer>();
	
		for (int i = 0; i < a.size(); i++){
			probs5.put(i, prob(1, (double)a.get(i), (double)b.get(i)));
			probs4.put(i, prob(0.5, (double)a.get(i), (double)b.get(i)));
		}
		
		for (int i = 0; i < probs5.size(); i++){
			diff.put((double)probs5.get(i)-(double)probs4.get(i), i);
		}

		Integer p10V[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), 0, 9);
		Integer p20V[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), 0, 19);
		Integer p50V[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), 0, 49);
		Integer p100V[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), 0, 99);
		
		
		int[][] notas10  = new int[5][N];
		int[][] notas20  = new int[5][N];
		int[][] notas50  = new int[5][N];
		int[][] notas100 = new int[5][N];
		
		for(int i = 0; i < N; i++){
			// Para N = 10 //
			for (int j : p10V){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) notas10[th][i]++;
				}
			}
			// Para N = 10 //
			// Para N = 20 //
			for (int j : p20V){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) notas20[th][i]++;
				}
			}
			// Para N = 20 //
			// Para N = 50 //
			for (int j : p50V){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) notas50[th][i]++;
				}
			}
			// Para N = 50 //
			// Para N = 100 //
			for (int j : p100V){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) notas100[th][i]++;
				}
			}
			// Para N = 100 //
		}
		
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
		
		List<String[]> probList = new ArrayList<String[]>();
		
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
		
		probList.add(Arrays.toString(intervalo10).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(intervalo20).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(intervalo50).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(intervalo100).split("[\\[\\]]")[1].split(", "));
		
		escreveArquivo("I3.txt", probList);
		
	}
	// Métodos funcionais
	
    public static void main (String[] args) throws FileNotFoundException{       
		//List<Double> a = new ArrayList<Double>(); // Parâmetro de discriminação
        //List<Double> b = new ArrayList<Double>(); // Parâmetro de dificuldade
		
		// Lendo o arquivo de questões e preenchendo lista de parâmetros
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
		
		// Métodos
		System.out.println("**********************************************************************");
		System.out.println("Calculando melhor aluno...");
		melhorAluno(); // I
		System.out.println("**********************************************************************");
		System.out.println("Calculando melhor prova...");
		melhorProva(); // II
		System.out.println("**********************************************************************");
		System.out.println("Calculando intervalo de confianca...");
		intervaloDeConfianca(); // III
		System.out.println("**********************************************************************");
    }
}
