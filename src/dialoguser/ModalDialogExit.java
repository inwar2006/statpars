
package dialoguser;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
/**
 * Класс вывода модального окна для подтверждения
 * закрытия GUI-интерфейса javafx. программы
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class ModalDialogExit {
    static Stage messageStage;
    static boolean btnYesClick = false;

    public static boolean show(String message, String title,
        String textYes, String textNo){

        Label label = new Label();
        label.setText(message);

        Image imgQuest = new Image("/image/Question.png");
        ImageView ivwQuest = new ImageView(imgQuest);

        Button btnYes = new Button();
        btnYes.setText(textYes);
        btnYes.setMinWidth(100);

        btnYes.setOnMouseClicked(e -> btnYes_Click());
        
        //обработка события нажатия клавиши Enter на кнопку btnYes
        //обрабатывается нажатие только(!) клавиши Enter на кнопку bntYes
        //и только(!) при фокусе на ней - это важно(!)
        btnYes.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(event.getCode() == KeyCode.ENTER){
                        btnYes_Click();
                }
            }
        });

        Button btnNo = new Button();
        btnNo.setText(textNo);
        btnNo.setMinWidth(100);

        btnNo.setOnMouseClicked(e -> btnNo_Click());
        
        //обработка события нажатия клавиши Enter на кнопку btnNo
        //обрабатывается нажатие только(!) клавиши Enter на кнопку bntNo
        //и только(!) при фокусе на ней - это важно(!)
        btnNo.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(event.getCode() == KeyCode.ENTER){
                        btnNo_Click();
                }
            }
        });

        HBox paneBtn = new HBox(100);
        paneBtn.setAlignment(Pos.CENTER);
        paneBtn.getChildren().addAll(btnYes, btnNo);

        VBox pane = new VBox(20);
        pane.setPadding(new Insets(20));
        pane.getChildren().addAll(label, ivwQuest, paneBtn);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane);
        messageStage = new Stage();
        messageStage.initModality(Modality.APPLICATION_MODAL);
        messageStage.setTitle(title);
        messageStage.setMinWidth(300);
        messageStage.setScene(scene);
        messageStage.showAndWait();
        return btnYesClick;
    }

    private static void btnYes_Click(){
        messageStage.close();
        btnYesClick = true;
    }

    private static void btnNo_Click(){
        messageStage.close();
        btnYesClick = false;
    }
}
