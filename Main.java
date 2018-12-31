package test;

import java.io.IOException;
import java.util.Random;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application{

	@FXML Canvas canvas;
	private GraphicsContext gc;
	
	@FXML Spinner<Integer> rowNumber;
	@FXML Spinner<Integer> colNumber;
	ObservableList<Integer> size = FXCollections.observableArrayList(1,2,3,4,5);
	SpinnerValueFactory<Integer> rowList = new SpinnerValueFactory.ListSpinnerValueFactory<>(size);
	SpinnerValueFactory<Integer> colList = new SpinnerValueFactory.ListSpinnerValueFactory<>(size);
	
	@FXML Button compute;
	@FXML Button random;
	@FXML Button edit;
	
	String[][] labels = new String[5][5];
	
	public void initialize() {
		rowNumber.setValueFactory(rowList);
		colNumber.setValueFactory(colList);
		
		for(int i=0; i<labels.length; i++) for(int j=0; j<labels[0].length; j++) labels[i][j] = "1";
		
		gc = canvas.getGraphicsContext2D();
		drawMatrix();
		
		rowNumber.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				drawMatrix();
			}
		});
		
		colNumber.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				drawMatrix();
			}
		});
	}
	
	public void drawMatrix() {
		int row = rowNumber.getValue(), col = colNumber.getValue();
		double line_height = 40,  line_width = 60;
		double width = canvas.getWidth(),  height = canvas.getHeight();
		double pos_x = width/2 - col*line_width/2, pos_y = height/2 - row*line_height/2; 
		
		Font font = new Font(25);
		gc.setFont(font);
		gc.clearRect(0, 0, width, height);
		gc.setFill(Color.rgb(93, 93, 93));
		gc.setLineWidth(2);
		
		for(int i=0; i<row; i++) {
			// |
			gc.strokeLine(pos_x, pos_y, pos_x, pos_y+line_height);
			for(int j=0; j<col; j++) {
				//gc.strokeText("0", pos_x+line_width/2-1, pos_y+line_height/2+1);
				gc.fillText(labels[i][j], pos_x+12, pos_y+29);
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
	
	public void random() {
		Random rand = new Random();
		for(int i=0; i<labels.length; i++)
			for(int j=0; j<labels[0].length; j++){	
			int random = rand.nextInt(9)+1;
			labels[i][j] = Integer.toString(random);
		}
		drawMatrix();
	}
	
	private boolean isDigit(String s) {
		double num;
		try {
			 num = Double.parseDouble(s);	
		}catch(Exception e) {
			return false;
		}
		
		if(num>=0 && num<10)
			return true;
		return false;
	}
	
	public void edit() {
		TextField[][] fields = new TextField[rowNumber.getValue()][colNumber.getValue()];
		VBox root = new VBox();
		GridPane fieldPane = new GridPane();
		Button button = new Button("Degistir");
		Stage stage = new Stage();
		button.setAlignment(Pos.CENTER);
		button.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				boolean error = false;
				for(int i=0; i<rowNumber.getValue(); i++) {
					for(int j=0; j<colNumber.getValue(); j++) {
						if(!isDigit(fields[i][j].getText())) 
							error = true;
					}
				}
				
				if(!error) {
					for(int i=0; i<rowNumber.getValue(); i++) {
						for(int j=0; j<colNumber.getValue(); j++) {
							/*if(fields[i][j].getText().equals("")) {
								labels[i][j] = "0";
							}*/
							labels[i][j] = fields[i][j].getText();
						}
					}
					drawMatrix();
					stage.close();
				}else {
					new AlertBox("Lutfen sadece rakam giriniz(Tek Haneli)");
				}
				
			}

		});
		
		for(int i=0; i<rowNumber.getValue(); i++) {
			for(int j=0; j<colNumber.getValue(); j++) {
				fields[i][j] = new TextField(labels[i][j]);
				fields[i][j].setMaxWidth(30);
				fieldPane.add(fields[i][j], j, i);
			}
		}
		
		root.getChildren().addAll(fieldPane, button);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Duzenle");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}
	
	public void compute() {
		if(rowNumber.getValue() == colNumber.getValue()) {
			new AlertBox("Matris Kare Olamaz");
			return;
		}
		Parent root;
		Stage window = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MatrixScreen.fxml"));
			
			loader.load();
			root = loader.getRoot();
			
			MatrixScreenController controller = loader.<MatrixScreenController>getController();
			controller.setMatrix(labels, rowNumber.getValue(), colNumber.getValue());
			
			Scene scene = new Scene(root);
			window.setScene(scene);
		}catch(IOException e) {
			System.out.println("MatrixScreen yuklenemedi");
			e.printStackTrace();
		}
		window.setTitle("Matrix Hesapla");
		//window.initModality(Modality.APPLICATION_MODAL);
		window.show();
		//window.setResizable(false);
	}
	
	@Override
	public void start(Stage window) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
			Scene scene = new Scene(root);
			window.setScene(scene);
		}catch(IOException e) {
			System.out.println("MainScreen yuklenemedi");
			e.printStackTrace();
		}
		
		window.setTitle("Pseudo Inverse");
		window.show();
		window.setResizable(false);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
