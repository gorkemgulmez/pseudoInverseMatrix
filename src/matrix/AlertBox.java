package matrix;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	public AlertBox(String warning) {
		VBox layout = new VBox();
		Text warning_text = new Text(warning);
		Button button = new Button("Ok");
		layout.getChildren().addAll(warning_text, button);
		layout.setSpacing(10);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout, 300, 80);
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Problem");
		window.setScene(scene);
		window.show();
		button.setOnAction(event-> window.close());
	}
}
