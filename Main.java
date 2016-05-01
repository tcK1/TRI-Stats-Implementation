import java.io.*;
import java.util.*;
import java.math.*;

class Main{
	
	public static final double[] theta = {-1.0, -0.5, 0.0, 0.5, 1.0};
	
	public static final int N = 1000000;
	
	// Métodos auxiliares
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

	public static double prob(double theta, double a, double b){
		double aux = Math.pow(Math.E, a*(theta-b));
		return aux/(1+aux);
	}
	
	public static boolean acertou(double theta, double a, double b){
		double prob = prob(theta, a, b);
		double aux = Math.random();
		//System.out.println(aux +" , "+ prob);
		if(aux <= prob) return true;
		else return false;
	}
	// Métodos auxiliares
	
	// Métodos funcionais
	public static void melhorAluno(List a, List b){
				
		Integer[] arr = new Integer[100];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}

		Collections.shuffle(Arrays.asList(arr));		
		Integer p10[] = Arrays.copyOfRange(arr, 0, 9);
		Collections.shuffle(Arrays.asList(arr));
		Integer p20[] = Arrays.copyOfRange(arr, 0, 19);
		Collections.shuffle(Arrays.asList(arr));
		Integer p50[] = Arrays.copyOfRange(arr, 0, 49);
		Collections.shuffle(Arrays.asList(arr));
		Integer p100[] = Arrays.copyOfRange(arr, 0, 99);

		int[] acertos10 = new int[5];
		int[] acertos20 = new int[5];
		int[] acertos50 = new int[5];
		int[] acertos100 = new int[5];
		
		double[] prob10 = new double[4];
		double[] prob20 = new double[4];
		double[] prob50 = new double[4];
		double[] prob100 = new double[4];
		
		for(int i = 0; i < N; i++){
			// Para N = 10 //
			for (int j : p10){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos10[th]++;
				}
			}
			for (int t = 0; t < 4; t++){
				if(acertos10[4] < acertos10[t]) prob10[t]++;
				acertos10[t] = 0;
			} acertos10[4] = 0;
			// Para N = 10 //
			// Para N = 20 //
			for (int j : p20){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos20[th]++;
				}
			}
			for (int t = 0; t < 4; t++){
				if(acertos20[4] < acertos20[t]) prob20[t]++;
				acertos20[t] = 0;
			} acertos20[4] = 0;
			// Para N = 20 //
			// Para N = 50 //
			for (int j : p50){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos50[th]++;
				}
			}
			for (int t = 0; t < 4; t++){
				if(acertos50[4] < acertos50[t]) prob50[t]++;
				acertos50[t] = 0;
			} acertos50[4] = 0;
			// Para N = 50 //
			// Para N = 100 //
			for (int j : p100){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos100[th]++;
				}
			}
			for (int t = 0; t < 4; t++){
				if(acertos100[4] < acertos100[t]) prob100[t]++;
				acertos100[t] = 0;
			} acertos100[4] = 0;
			// Para N = 100 //
			
		}

		List<String[]> probList = new ArrayList<String[]>();
		
		for (int teste = 0; teste < 4; teste++){
			prob10[teste] = (N-prob10[teste])/N; 
			prob20[teste] = (N-prob20[teste])/N; 
			prob50[teste] = (N-prob50[teste])/N;
			prob100[teste] = (N-prob100[teste])/N;
		}		
		
		probList.add(Arrays.toString(prob10).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob20).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob50).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob100).split("[\\[\\]]")[1].split(", "));

		escreveArquivo("I1.txt", probList);

		System.out.println("5 em relacao a 4, 3, 2 e 1 para "+N+" interacoes:");
		for (int teste = 0; teste < 4; teste++){
			System.out.println(	prob10[teste] +", "+ 
								prob20[teste] +", "+ 
								prob50[teste] +", "+
								prob100[teste]);
		}
		
	}
	
	public static void melhorProva(List a, List b){
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
		Double p10K[] = Arrays.copyOfRange(diff.keySet().toArray(new Double[100]), 0, 9);

		Integer p20V[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), 0, 19);
		Double p20K[] = Arrays.copyOfRange(diff.keySet().toArray(new Double[100]), 0, 19);

		Integer p50V[] = Arrays.copyOfRange(diff.values().toArray(new Integer[100]), 0, 49);
		Double p50K[] = Arrays.copyOfRange(diff.keySet().toArray(new Double[100]), 0, 49);

		int[] acertos10 = new int[5];
		int[] acertos20 = new int[5];
		int[] acertos50 = new int[5];
		
		double[] prob10 = new double[4];
		double[] prob20 = new double[4];
		double[] prob50 = new double[4];
		
		for(int i = 0; i < N; i++){
			// Para N = 10 //
			for (int j : p10V){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos10[th]++;
				}
			}
			for (int t = 0; t < 4; t++){
				if(acertos10[4] < acertos10[t]) prob10[t]++;
				acertos10[t] = 0;
			} acertos10[4] = 0;
			// Para N = 10 //
			// Para N = 20 //
			for (int j : p20V){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos20[th]++;
				}
			}
			for (int t = 0; t < 4; t++){
				if(acertos20[4] < acertos20[t]) prob20[t]++;
				acertos20[t] = 0;
			} acertos20[4] = 0;
			// Para N = 20 //
			// Para N = 50 //
			for (int j : p50V){
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos50[th]++;
				}
			}
			for (int t = 0; t < 4; t++){
				if(acertos50[4] < acertos50[t]) prob50[t]++;
				acertos50[t] = 0;
			} acertos50[4] = 0;
			// Para N = 50 //
		}

		List<String[]> probList = new ArrayList<String[]>();
		
		for (int teste = 0; teste < 4; teste++){
			prob10[teste] = (N-prob10[teste])/N; 
			prob20[teste] = (N-prob20[teste])/N; 
			prob50[teste] = (N-prob50[teste])/N;
		}

		for(int i = 0; i < p10V.length; i++)
			p10V[i]++;

		for(int i = 0; i < p20V.length; i++)
			p20V[i]++;

		for(int i = 0; i < p50V.length; i++)
			p50V[i]++;

		probList.add(Arrays.toString(p10V).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob10).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(p20V).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob20).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(p50V).split("[\\[\\]]")[1].split(", "));
		probList.add(Arrays.toString(prob50).split("[\\[\\]]")[1].split(", "));

		escreveArquivo("I2.txt", probList);

		System.out.println("5 em relacao a 4, 3, 2 e 1 para "+N+" interacoes na melhor prova:");
		for (int teste = 0; teste < 4; teste++){
			System.out.println(	prob10[teste] +", "+ 
								prob20[teste] +", "+ 
								prob50[teste]);
		}

		//System.out.println(diff);
		
	}

	public static void intervaloDeConfianca(List a, List b) {
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
		System.out.println(limiteInferior + " , " + limiteSuperior);
		
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
		List<Double> a = new ArrayList<Double>(); // Parâmetro de discriminação
        List<Double> b = new ArrayList<Double>(); // Parâmetro de dificuldade
		
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
		melhorAluno(a, b); // I
		melhorProva(a, b); // II
		intervaloDeConfianca(a, b); // III
    }
}
