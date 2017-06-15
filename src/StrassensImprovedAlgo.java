import java.util.Random;

public class StrassensImprovedAlgo {
	
	
	public void run(int breakPoint){
		int size = 1;
		Random rand = new Random();		
		while(size < 2500){
			
			size *= 2;
			
			int A[][] = new int[size][size];
			int B[][] = new int[size][size];
			int C[][];
			for(int i = 0; i < A.length; i++){
				for(int j = 0; j < A.length; j++){
					A[i][j] = rand.nextInt(200)+100;
					B[i][j] = rand.nextInt(200)+100;
				}
			}
			
			long nanos = System.currentTimeMillis();
			C = traditionalMatrixMultiplication(A, B);
			long t1 = System.currentTimeMillis() - nanos;
			
			nanos = System.currentTimeMillis();
			int requiredLength = getRequiredLengthFor(A.length);
			
//			if(size < (requiredLength * 0.75)){
//				C = traditionalMatrixMultiplication(A, B);
//			}else{
				if(requiredLength > A.length){
					A = paddMatrix(A, requiredLength);
					B = paddMatrix(B, requiredLength);
				}
				C = strassenForMatrix(A, B, size, breakPoint);
//			}
			long t3 = System.currentTimeMillis() - nanos;
			
			nanos = System.currentTimeMillis();
			C = strassenForMatrix(A, B, size, -1);
			long t2 = System.currentTimeMillis() - nanos;
			
			System.out.println("_________________\nat: "+size+"\ntime by traditional method: "+t1+"ms\ntime by Strassen's Algorithm: "+t2+"ms\ntime by Improved Algorithm: "+t3+"ms");
		}
		
	}
	
	public int[][] paddMatrix(int[][] A, int power){
		int B[][] = new int[power][power];
		
		for(int i = 0; i < B.length; i++){
			for(int j = 0; j< B.length; j++){
				if(i < A.length && j < A.length){
					B[i][j] = A[i][j];
				}else{
					B[i][j] = 0;
				}
			}
		}
		
		return B;
	}
	
	public int getRequiredLengthFor(int currentLength){
		int requiredLength = 0;
		
		int divisor = 1;
		while(currentLength % divisor != currentLength){
			divisor *= 2;
			if(divisor == currentLength)
				return divisor;
		}
		requiredLength = divisor;		
		return requiredLength;
	}
	
	public int[][] strassen(int[][] A, int[][] B, int halfLength, int breakPoint){
		int C[][] = new int[A.length][A.length];
		
		if(halfLength == 0){
			C[0][0] = A[0][0]*B[0][0];
			return C;
		}
		
		int a[][] = new int[halfLength][halfLength];
		int d[][] = new int[halfLength][halfLength];
		
		int e[][] = new int[halfLength][halfLength];
		int h[][] = new int[halfLength][halfLength];
		
		int S1[][] = new int[halfLength][halfLength];
		int S2[][] = new int[halfLength][halfLength];
		int S3[][] = new int[halfLength][halfLength];
		int S4[][] = new int[halfLength][halfLength];
		int S5[][] = new int[halfLength][halfLength];
		int S6[][] = new int[halfLength][halfLength];
		int S7[][] = new int[halfLength][halfLength];
		int S8[][] = new int[halfLength][halfLength];
		int S9[][] = new int[halfLength][halfLength];
		int S10[][] = new int[halfLength][halfLength];

		for(int i = 0; i < halfLength; i++){
			for(int j = 0; j < halfLength; j++){
				a[i][j] = A[i][j];
				d[i][j] = A[halfLength + i][halfLength + j];
				
				e[i][j] = B[i][j];
				h[i][j] = B[halfLength + i][halfLength + j];
				
				 S1[i][j] = B[i][halfLength + j] - B[halfLength + i][halfLength + j];
				 S2[i][j] = A[i][j] + A[i][halfLength + j];
				 S3[i][j] = A[halfLength + i][j] + A[halfLength + i][halfLength + j];
				 S4[i][j] = B[halfLength + i][j] - B[i][j];//g-e
				 S5[i][j] = A[i][j] + A[halfLength + i][halfLength + j];//a+d
				 S6[i][j] = B[i][j] + B[halfLength + i][halfLength + j];//e + h
				 S7[i][j] = A[i][halfLength + j] - A[halfLength + i][halfLength + j];//b-d
				 S8[i][j] = B[halfLength + i][j] + B[halfLength + i][halfLength + j]; //g+h
				 S9[i][j] = A[i][j] - A[halfLength + i][j];//a-c
				 S10[i][j] = B[i][j] + B[i][halfLength + j];//e+f
			}
		}
		
		int P1[][];// = strassen(a, S1, halfLength/2);
		int P2[][];// = strassen(S2, h, halfLength/2);
		int P3[][];// = strassen(S3, e, halfLength/2);
		int P4[][];// = strassen(d, S4, halfLength/2);
		int P5[][];// = strassen(S5, S6, halfLength/2);
		int P6[][];// = strassen(S7, S8, halfLength/2);
		int P7[][];// = strassen(S9, S10, halfLength/2);
		
		if(A.length <= breakPoint){
			P1 = traditionalMatrixMultiplication(a, S1); 
			P2 = traditionalMatrixMultiplication(S2, h);
			P3 = traditionalMatrixMultiplication(S3, e);
			P4 = traditionalMatrixMultiplication(d, S4);
			P5 = traditionalMatrixMultiplication(S5, S6);
			P6 = traditionalMatrixMultiplication(S7, S8);
			P7 = traditionalMatrixMultiplication(S9, S10);
		}else{
			 P1 = strassen(a, S1, halfLength/2, breakPoint);
			 P2 = strassen(S2, h, halfLength/2, breakPoint);
			 P3 = strassen(S3, e, halfLength/2, breakPoint);
			 P4 = strassen(d, S4, halfLength/2, breakPoint);
			 P5 = strassen(S5, S6, halfLength/2, breakPoint);
			 P6 = strassen(S7, S8, halfLength/2, breakPoint);
			 P7 = strassen(S9, S10, halfLength/2, breakPoint);
		}

		for(int i = 0; i < P1.length; i++){
			for(int j = 0; j < P1.length; j++){
				C[i][j] = P5[i][j] + P4[i][j] - P2[i][j] + P6[i][j];//C11[i][j];
				C[i][P1.length + j] = P1[i][j] + P2[i][j];//C12[i][j];
				C[P1.length + i][j] = P3[i][j] + P4[i][j];//C21[i][j];
				C[P1.length + i][P1.length + j] = P1[i][j] + P5[i][j] - P3[i][j] - P7[i][j];//C22[i][j];
			}
		}
		return C;
	}
	
	public int[][] strassenForMatrix(int[][] A, int[][] B, int orignalLength, int breakPoint){
		int D[][] = new int[orignalLength][orignalLength];
		
		int C[][] = strassen(A, B, A.length/2, breakPoint);
		for(int i = 0; i < D.length; i++){
			for(int j = 0; j < D.length; j++){
				D[i][j] = C[i][j];
			}
		}
		
		return D;
	}
	
	public int[][] traditionalMatrixMultiplication(int[][] A, int[][] B){
		int C[][] = new int[A.length][A.length];
		
		for(int i = 0; i < A.length; i++){
			for(int j = 0; j < A.length; j ++){
				for(int k = 0; k < A.length; k++){
					C[i][j] += A[i][k]*B[k][j];
				}
			}
		}
		
		return C;
	}
}
