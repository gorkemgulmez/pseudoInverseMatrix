package test;

public class Matrix {
	
	public static int topC =0, carpC=0;
	
	public static double[][] transpose(double[][] matrix) {
		int i,j;
		double[][] result = new double[matrix[0].length][matrix.length];
		for(i=0; i<matrix.length; i++, topC++) {
			for(j=0; j<matrix[i].length; j++, topC++) {
				result[j][i] = matrix[i][j];
			}
		}
		
		return result;
	}
	
	public static double[][] multiplication(double matrix_1[][], double matrix_2[][]) {
		//[k][l] x [m][n] = [k][n]
		int i, j, a;
		double[][] result = new double[matrix_1.length][matrix_2[0].length];
		for(i=0; i<result.length; i++, topC++) {
			for(j=0; j<result[0].length; j++, topC++) {
				for(a=0; a<matrix_1[0].length; a++, topC++) {
					result[i][j] += matrix_1[i][a] * matrix_2[a][j];
					topC++; carpC++;
				}
			}
		}
		
		return result;
	}
	
	public static double[][] squareInverse(double[][] matrix) {
		// 1/det * adj(matrix)
		double det = det(1, matrix);
		if(det == 0) {
			System.err.println("Determinant 0");
			//return null;
		}
		double[][] adjugate = adjugate(matrix);
		double[][] result = new double[adjugate.length][adjugate[0].length];
		
		for(int i=0; i<adjugate.length; i++, topC++) {
			for(int j=0; j<adjugate[0].length; j++, topC++) {
				result[i][j] = (1/det) * adjugate[i][j];
				carpC++;
			}
		}
		return result;
	}
	
	public static double det(double multip, double[][] matrix) { //detN ->1
		if(matrix.length == 2) {
			// a*d - b*c
			carpC+=3; topC++;
			return multip * ((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]));
		}
		else if(matrix.length > 2) {
			// a* co(a) - b * co(b) + ...
			double result = 0;
			for(int j=0; j<matrix.length; j++, topC++) {
				double[][] subMatrix = subMatrix(j, matrix);
				if(j%2 != 0)
					result -= det(matrix[0][j], subMatrix);
				else
					result += det(matrix[0][j], subMatrix);
				topC++;
			}
			carpC++;
			return multip * result;
		}
		
		//if(matrix.length == 1)
		return matrix[0][0];
	}
	
	public static double[][] subMatrix(int col, double[][] matrix) {
		double[][] result = new double[matrix.length-1][matrix.length-1];
		
		for(int i=1; i<matrix.length; i++, topC++) {
			int sub_j= 0;
			for(int j=0; j<matrix.length; j++, topC++) {
				if(j!=col) {
					result[i-1][sub_j] = matrix[i][j];
					sub_j++;
					topC++;
				}
			}
		}
		
		return result;
	}
	
	public static double[][] cofactor(double[][] matrix) {
		double[][] result = new double[matrix.length][matrix.length];
		double[][] subMatrix = new double[matrix.length-1][matrix.length-1];
		
		for(int i=0; i<matrix.length; i++, topC++) {
			for(int j=0; j<matrix.length; j++, topC++) {
				
				int sub_i = 0, sub_j= 0;
				for(int m=0; m<matrix.length; m++, topC++) {
					for(int n=0; n<matrix.length; n++, topC++) {
						if(m!=i && n!=j) {
							subMatrix[sub_i][sub_j] = matrix[m][n];
							sub_j++;
							topC++;
							if(sub_j >= subMatrix.length) {
								sub_j = 0;
								sub_i++;
								topC++;
							}
						}
					}
				}
				//det(subMat)
				result[i][j] = det(1, subMatrix);
				if((i+j)%2 != 0) {
					result[i][j] = result[i][j]* -1;
					carpC++;
				}
			}
		}
		
		return result;
	}
	
	public static double[][] adjugate(double[][] matrix) {
		int length = matrix.length;
		double[][] result = new double[length][length]; 
		if (length == 1) {
			//return I
			result[0][0] = 1;
		}
		else if(length == 2) {
			// d,-b,
			// -c,a
			result[0][0] = matrix[1][1];
			result[1][1] = matrix[0][0];
			
			result[0][1] = -1 * matrix[0][1];
			result[1][0] = -1 * matrix[1][0];
			carpC+=2;
		}
		else {
			result = cofactor(matrix);
			result = transpose(result);
		}
		
		return result;
	}
	
	public static double[][] pseudoInverse(double[][] matrix) {
		System.out.println("Pseudo Results:");
		double[][] result;
		if(matrix.length > matrix[0].length) { //m >n 
			//(A(t) * A)^-1 * A(t)
			result = multiplication(squareInverse(multiplication(transpose(matrix),matrix)), transpose(matrix));
			
		}
		else { //n>m
			//A(t) * (A * A(t))^-1
			result = multiplication(transpose(matrix), squareInverse(multiplication(matrix, transpose(matrix) )));
		}
		
		for(int i=0; i<result.length; i++) {
			for(int j=0; j<result[0].length; j++) {
				System.out.print(result[i][j] + " ");
			}
			System.out.println();
		}
		return result;
	}
	
}
 
 
