package matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MatrixScreenController {

	@FXML Canvas canvas;
	@FXML Button ileri;
	
	private GraphicsContext gc;
	private String[][] matrix;
	private String[][] trans;
	private String[][] squareI;
	private String[][] result;
	
	private int iterator=0;
	
	public void setMatrix(String[][] matrix, int row, int col) {
		this.matrix = new String[row][col];
		for(int i=0; i<row; i++) {
			for(int j=0; j<col; j++) {
				this.matrix[i][j] = matrix[i][j];
			}
		}
	}
	
	public void drawMatrix(double x, double width, String[][] mat) {
		double line_height = 40,  line_width = 110;
		double height = canvas.getHeight();
		int col = mat[0].length, row = mat.length;
		double pos_x = 1+ x + width/2 - col*line_width/2, pos_y = height/2 - row*line_height/2; 
		
		Font font = new Font(25);
		gc.setFont(font);
		//gc.clearRect(0, 0, width, height);
		gc.setFill(Color.rgb(93, 93, 93));
		gc.setLineWidth(2);
		
		for(int i=0; i<mat.length; i++) {
			// |
			gc.strokeLine(pos_x, pos_y, pos_x, pos_y+line_height);
			for(int j=0; j<mat[0].length; j++) {
				//gc.strokeText("0", pos_x+line_width/2-1, pos_y+line_height/2+1);
				gc.fillText(mat[i][j], pos_x+12, pos_y+29);
				// -
				gc.strokeLine(pos_x, pos_y, pos_x+line_width, pos_y);
				// |
				gc.strokeLine(pos_x+line_width, pos_y, pos_x+line_width, pos_y+line_height);
				pos_x += line_width;
			}
			pos_y += line_height;
			pos_x -= col*(line_width);
		}

		for(int J=0; J<col; J++) {
			//-
			gc.strokeLine(pos_x, pos_y, pos_x+line_width, pos_y);
			pos_x += line_width;
		}
	}
	
	public void drawText(String text, double x, double y) {
		Font font = new Font(25);
		gc.setFont(font);
		
		gc.fillText(text, x, y);
	}
	
	public void clearScreen() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public void ileri() {
		if(iterator == 0) {
			ileri.setText("Ileri");
			gc = canvas.getGraphicsContext2D();
			drawText("Matrisimiz", canvas.getWidth()/2 -50, 100);
			drawMatrix(0, canvas.getWidth(), matrix);
			iterator++;
			Matrix.carpC =0; Matrix.topC =0;
			return;
		}
		//m>n
		if(matrix.length> matrix[0].length) {
			m();
		}
		//n>m
		else {
			n();
		}
	}
	
	public void m() {
		if(iterator==1) {
			clearScreen();
			//trans(a)
			double[][] temp = stringToDouble(matrix);
			double[][] result = Matrix.transpose(temp);
			trans = doubleToString(result);
			drawMatrix(0, canvas.getWidth(), trans);
			drawText("Matris Transpozisyonu", canvas.getWidth()/2 -170, 100);
			drawText("[A]ij = [B]ji", canvas.getWidth()/2 -300, 420);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}else if(iterator == 2) {
			clearScreen();
			//trans(a) * a
			drawMatrix(0, canvas.getWidth()/2, trans);
			drawMatrix(canvas.getWidth()/2, canvas.getWidth()/2, matrix);
			drawText("Transpose Matris * Matris", canvas.getWidth()/2 -150, 100);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450); 
		}else if(iterator == 3) {
			clearScreen();
			//result
			double[][] result = Matrix.multiplication(stringToDouble(trans),stringToDouble(matrix));
			drawMatrix(0, canvas.getWidth(), doubleToString(result));
			drawText("Sonu�", canvas.getWidth()/2 -50, 100);
			drawText("Determinant s�f�r olusa matrisin tersi bulunamaz", canvas.getWidth()/2 -300, 420);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}else if(iterator == 4) {
			clearScreen();
			//result)^-1 -> 
			double det = Matrix.det(1,Matrix.multiplication(stringToDouble(trans),stringToDouble(matrix)));
			if(det == 0) {
				new AlertBox("Determinant S�f�r Pseudo Inverse Bulunamaz");
				return ;
			}
			double[][] result = Matrix.squareInverse(Matrix.multiplication(stringToDouble(trans),stringToDouble(matrix)));
			squareI = doubleToString(result);
			drawMatrix(0, canvas.getWidth(), squareI);
			drawText("Kare Matrisin Tersi", canvas.getWidth()/2 -100, 100);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}else if(iterator == 5) {
			clearScreen();
			//result * trans(a)
			drawMatrix(0, canvas.getWidth()/2, squareI);
			drawMatrix(canvas.getWidth()/2, canvas.getWidth()/2, trans);
			drawText("Kare Matris * Transpose Matris", canvas.getWidth()/2 -150, 100);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}
		else if(iterator == 6) {
			clearScreen();
			//result
			double[][] resultD = Matrix.pseudoInverse(stringToDouble(matrix));
			result = doubleToString(resultD);
			drawMatrix(0, canvas.getWidth(), result);
			drawText("��z�m", canvas.getWidth()/2 -50, 100);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}
		iterator++;
	}
	
	public void n() {
		if(iterator == 1) {
			clearScreen();
			//trans(a)
			double[][] temp = stringToDouble(matrix);
			double[][] result = Matrix.transpose(temp);
			trans = doubleToString(result);
			drawMatrix(0, canvas.getWidth(), trans);
			drawText("Matris Transpozisyonu", canvas.getWidth()/2 -170, 100);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}else if(iterator == 2) {
			clearScreen();
			//a * trans(a)
			drawMatrix(0, canvas.getWidth()/2, matrix);
			drawMatrix(canvas.getWidth()/2, canvas.getWidth()/2, trans);
			drawText("Matris * Transpose Matris", canvas.getWidth()/2 -150, 100);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}else if(iterator == 3) {
			clearScreen();
			//result
			double[][] result = Matrix.multiplication(stringToDouble(matrix), stringToDouble(trans));
			drawMatrix(0, canvas.getWidth(), doubleToString(result));
			drawText("Sonu�", canvas.getWidth()/2 -50, 100);
			drawText("Determinant s�f�r olusa matrisin tersi bulunamaz", canvas.getWidth()/2 -300, 420);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}else if(iterator == 4) {
			clearScreen();
			//result)^-1
			double det = Matrix.det(1,Matrix.multiplication(stringToDouble(matrix),stringToDouble(trans)));
			if(det == 0) {
				new AlertBox("Determinant S�f�r Pseudo Inverse Bulunamaz");
				return ;
			}
			double[][] result = Matrix.squareInverse(Matrix.multiplication(stringToDouble(matrix), stringToDouble(trans)));
			squareI = doubleToString(result);
			drawMatrix(0, canvas.getWidth(), squareI);
			drawText("Kare Matrisin Tersi", canvas.getWidth()/2 -100, 100);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -250, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -250, 450);
		}else if(iterator == 5) {
			clearScreen();
			//trans(a) * result
			drawMatrix(0, canvas.getWidth()/2, trans);
			drawMatrix(canvas.getWidth()/2, canvas.getWidth()/2, squareI);
			drawText("Kare Matris * Transpose Matris", canvas.getWidth()/2 -150, 100);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}
		else if(iterator == 6) {
			clearScreen();
			//result
			double[][] resultD = Matrix.pseudoInverse(stringToDouble(matrix));
			result = doubleToString(resultD);
			drawMatrix(0, canvas.getWidth(), result);
			drawText("��z�m", canvas.getWidth()/2 -50, 100);
			drawText("Toplam: " + Matrix.topC, canvas.getWidth() -200, 420);
			drawText("�arp�m: " + Matrix.carpC, canvas.getWidth() -200, 450);
		}
		iterator++;
	}
	
	public static double round(double number, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(number);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public double[][] stringToDouble(String[][] matrix) {
		double[][] result = new double[matrix.length][matrix[0].length];
		for(int i=0; i<matrix.length; i++) {
			for(int j=0; j<matrix[0].length; j++) {
				result[i][j] = Double.parseDouble(matrix[i][j]);
			}
		}
		return result;
	}
	
	public String[][] doubleToString(double[][] matrix) {
		String[][] result = new String[matrix.length][matrix[0].length];
		for(int i=0; i<matrix.length; i++) {
			for(int j=0; j<matrix[0].length; j++) {
				result[i][j] = Double.toString(round(matrix[i][j], 3));
			}
		}
		return result;
	}
}
