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
		
		System.out.println(array.length);
			
		Arrays.sort(array);

		System.out.println(array[0]+" , "+array[array.length-1]);
		
		double tamanhoPart = (array[array.length-1]-array[0])/(double)10;
		
		System.out.println(tamanhoPart);
		
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
								
		System.out.println(particoes[0]+" , "+particoes[particoes.length-1]);
		
		for(double i : particoes){
			//System.out.print(i+" ");
		}
		System.out.println();
		
		int[] qntd = new int[particoes.length-1];
		System.out.println(qntd.length);
		
		for(double i : array){
			//System.out.print(i+" ");
			if (i >= particoes[0] && i < particoes[1]){
				//System.out.println("Entrou 1");
				qntd[0]++;
			}  
			if (i >= particoes[1] && i < particoes[2]){
				//System.out.println("Entrou 2");
				qntd[1]++;
			}  
			if (i >= particoes[2] && i < particoes[3]){
				//System.out.println("Entrou 3");
				qntd[2]++;
			}  
			if (i >= particoes[3] && i < particoes[4]){
				//System.out.println("Entrou 4");
				qntd[3]++;
			}  
			if (i >= particoes[4] && i < particoes[5]){
				//System.out.println("Entrou 5");
				qntd[4]++;
			}  
			if (i >= particoes[5] && i < particoes[6]){
				//System.out.println("Entrou 6");
				qntd[5]++;
			}  
			if (i >= particoes[6] && i < particoes[7]){
				//System.out.println("Entrou 7");
				qntd[6]++;
			}  
			if (i >= particoes[7] && i < particoes[8]){
				//System.out.println("Entrou 8");
				qntd[7]++;
			}  
			if (i >= particoes[8] && i < particoes[9]){
				//System.out.println("Entrou 9");
				qntd[8]++;
			}  
			if (i >= particoes[9] && i <= particoes[10]){
				//System.out.println("Entrou 10");
				qntd[9]++;
			} 
			
		}
		System.out.println();
		
		String output = "Element\tValue\tHistogram";
		
		/* Format histogram */
		// For each array element, output a bar in histogram
		for ( int counter = 0; counter < qntd.length; counter++ ) {
			output += "\n" + (counter+1) + "\t" + qntd[ counter ] + "\t";

			// Print bar of asterisks                               
			for ( int stars = 0; stars < qntd[ counter ]/25; stars++ ) {
				output += "*";   
			}
		}

		/* Print histogram */
		System.out.println(output);
		
		
		System.out.println(Arrays.toString(qntd));
		
		int[] qntdNorm = new int[qntd.length];
		for (int j = 0; j < qntd.length; j++){
			qntdNorm[j] = qntd[j]/50;
		}

		int[] qntdSort = Arrays.copyOf(qntdNorm, qntd.length);
		Arrays.sort(qntdSort);
        int max = qntdSort[qntdSort.length-1];
		System.out.println(max);
		
		String minhavez = "";
		
		for (int i = max; i >= 0; i--){
			minhavez += "\n";
			
			for (int j = 0; j < qntdNorm.length; j++){
				if(qntdNorm[j] >= i){
					minhavez += "*\t"; 
				} else minhavez += " \t";
			}
			
		}
		
		System.out.println(minhavez);
		
		
				
	}
}