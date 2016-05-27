import java.util.*;
import java.math.*;
import java.io.*;

class teste{

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
		
		Arrays.sort(array);

		// Define um tamanho para as 10 partições
		double tamanhoPart = (array[array.length-1]-array[0])/(double)10;
		double[] particoes = {	array[0], 
								array[0]+tamanhoPart,
								array[0]+(tamanhoPart*2),
								array[0]+(tamanhoPart*3),
								array[0]+(tamanhoPart*4),
								array[0]+(tamanhoPart*5),
								array[0]+(tamanhoPart*6),
								array[0]+(tamanhoPart*7),
								array[0]+(tamanhoPart*8),
								array[0]+(tamanhoPart*9),
								array[0]+(tamanhoPart*10)
								};
								
		
		int[] qntd = new int[particoes.length-1];
		for(double i : array){
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
			qntdNorm[j] = qntd[j]/50;
		}

		// Começo do print do gráfico
		int[] qntdSort = Arrays.copyOf(qntdNorm, qntd.length);
		Arrays.sort(qntdSort);
        int max = qntdSort[qntdSort.length-1];
		System.out.println(max);
		
		//String saida = "";
		
		for (int i = max; i >= 0; i--){
			//saida += "\n";
			
			for (int j = 0; j < qntdNorm.length; j++){
				if(qntdNorm[j] >= i){
					System.out.printf(" %-7s","*");
					//saida += "*\t"; 
				} else System.out.printf(" %-7s"," "); 
					//saida += " \t";
			}
			
			//System.out.println(saida);
			//saida = "";
		}
		
		//System.out.println(saida);
		 System.out.printf(" %-7s%-7s%-7s%-7s%-7s%-7s%-7s%-7s%-7s%s",qntd[0],qntd[1],
            qntd[2],qntd[3],qntd[4],qntd[5],qntd[6],qntd[7],qntd[8],qntd[9]);

		//System.out.println(qntd[0] + "\t" + qntd[1] + "\t" + qntd[2] + "\t" + qntd[3] + "\t" + qntd[4] + "\t" + qntd[5] + "\t" + qntd[6] + "\t" + qntd[7] + "\t" + qntd[8] + "\t" + qntd[9]);
		// Fim do print do gráfico
				
	}
}