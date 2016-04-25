import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.math.BigDecimal;
import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;

class Main{
	
	private static void writeFile(String fileName, List<Double[]> data) {
		Iterator<Double[]> it = data.iterator();
		Double[] dt;
		String line;

		try(FileWriter fw = new FileWriter(fileName, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw))
		{
			while(it.hasNext()) {
				dt = it.next();
				line = "";			
				for(Double d : dt) {
					line += String.valueOf(d) + " ";
				}
				out.println(line);
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
		if(aux < prob) return true;
		else return false;
	}
	
	public static void melhorAluno(List a, List b){
		double[] theta = {-1.0, -0.5, 0.0, 0.5, 1.0};
		
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
		
		for(int i = 0; i <= 100000; i++){
			for (int j : p10){ // Para N = 10
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos10[th]++;
				}
			}
			for (int j : p20){ // Para N = 20
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos20[th]++;
				}
			}
			for (int j : p50){ // Para N = 50
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos50[th]++;
				}
			}
			for (int j : p100){ // Para N = 100
				for (int th = 0; th < 5; th++){
					if(acertou(theta[th], (double)a.get(j), (double)b.get(j))) acertos100[th]++;
				}
			}
		}

		Double[] prob10 = new Double[4];
		Double[] prob20 = new Double[4];
		Double[] prob50 = new Double[4];
		Double[] prob100 = new Double[4];

		// Calcula a probabilidade do aluno 5 ser melhor que os alunos 1, 2, 3 e 4

		for(int i = 0; i < 4; i++) {
			prob10[i] = 1D - ((double) acertos10[i])/((double) acertos10[4]);
		}

		for(int i = 0; i < 4; i++) {
			prob20[i] = 1D - ((double) acertos20[i])/((double) acertos20[4]);
		}

		for(int i = 0; i < 4; i++) {
			prob50[i] = 1D - ((double) acertos50[i])/((double) acertos50[4]);
		}

		for(int i = 0; i < 4; i++) {
			prob100[i] = 1D - ((double) acertos100[i])/((double) acertos100[4]);
		}

		List<Double[]> probList = new ArrayList<Double[]>();

		probList.add(prob10);
		probList.add(prob20);
		probList.add(prob50);
		probList.add(prob100);

		writeFile("I1.txt", probList);
	
		for (int teste = 0; teste < 5; teste++){
			System.out.println(	acertos10[teste] +", "+ 
								acertos20[teste] +", "+ 
								acertos50[teste] +", "+
								acertos100[teste]);
		}
	}
	
    public static void main (String[] args) throws FileNotFoundException{       
		List<Double> a = new ArrayList<Double>(); // Parâmetro de discriminação
        List<Double> b = new ArrayList<Double>(); // Parâmetro de dificuldade
		Scanner s = new Scanner(new File("questoes.txt"));
		int cont = 0;
		while(s.hasNext()) {
			cont++;
			BigDecimal bd = new BigDecimal(s.next());
			//System.out.println(bd);
			double val = bd.doubleValue();
			//System.out.println(val);
			if(cont%2 == 0){
				b.add(val);
			} else a.add(val);
		}
		s.close();
				
		melhorAluno(a, b);
    }
}
