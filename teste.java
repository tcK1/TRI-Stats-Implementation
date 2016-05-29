import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;

class teste{

	public static void histograma (double[] thetas){
		
		int norm = 50; // Valor para normalizar o histograma

		Arrays.sort(thetas);

		// Define um tamanho para as 10 partições
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
								
		// Array que vai armazenar quantos valores existem em cada partição
		int[] qntd = new int[particoes.length-1];
		// Populando as partições
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
		
		// Normaliza os dados para não printar um gráfico gigantesco
		int[] qntdNorm = new int[qntd.length];
		for (int j = 0; j < qntd.length; j++){
			qntdNorm[j] = (qntd[j]/norm);
		}

		System.out.println("Histograma com linhas de " + norm + " de amplitude:");
		System.out.println();
				
		// Começo do print do gráfico
		// Acha a quantidade máxima de valores em um conjunto (tamanho máximo de uma coluna)
		int[] qntdSort = Arrays.copyOf(qntdNorm, qntd.length);
		Arrays.sort(qntdSort);
        int max = qntdSort[qntdSort.length-1];
		
		// For entre max e 1, se colocar 0 mostra a linha com 0 elementos
		for (int i = max; i >= 1; i--){
			
			System.out.printf("%-6s", i*norm); // Printa a amplitude
			
			for (int j = 0; j < qntdNorm.length; j++){
				if(qntdNorm[j] >= i){
					System.out.printf(" %-6s","#"); // "#" Se tiver valor
				} 	else System.out.printf(" %-6s"," "); // " " Se não
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
		// Fim do print do gráfico
				
	}
	
	public static void main (String[] args) throws FileNotFoundException{

		double[] array	= new double[2000];
				
		Scanner s = new Scanner(new File("II1.txt"));
		int cont = 0;
		while(s.hasNext()) {
			BigDecimal bd = new BigDecimal(s.next());
			double val = bd.doubleValue();
			array[cont] = val;
			cont++;
		}
		s.close();
		
		histograma(array);
	
	}
	
}