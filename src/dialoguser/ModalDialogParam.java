
package dialoguser;

import java.util.LinkedHashMap;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import controlblock.ControlBlock;

/**
 * Класс вывода GUI-диалога для выбора параметров
 * пользователем, если не устраивают по умолчанию
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class ModalDialogParam {
    static Stage dialogStage;
    private TextArea txtArea = new TextArea();
    private ControlBlock controlBlock = new ControlBlock();
    // список посредник для обработки
    private LinkedHashMap<String, String> userDatamap = new 
        LinkedHashMap<>();
    private String ugFile = ""; //файл торговой статистики
    private String otdelFile = "";//файл отдела
    private String resultFile = "";//файл результата
    private String patternFile = "";//файл шаблона проверки
    private String encode = "";
    private final FileChooser fileDialog = new FileChooser();
    
    public ModalDialogParam(){
              
    }

     public LinkedHashMap<String, String> askParam(LinkedHashMap<String, 
            String> defaultdatamap){

        //заполнение рабочей карты значениями по умолчанию
        //через переданный параметр метода (первоначально)
        userDatamap.putAll(defaultdatamap);
            
        ugFile = userDatamap.get("Файл статистики");
        otdelFile = userDatamap.get("Файл ОТД");
        resultFile = userDatamap.get("Файл результата");
        encode = userDatamap.get("Кодировка");
        patternFile = userDatamap.get("Шаблон");
        
        Label label = new Label();
        label.setText("Выбор параметров");
       
        Button btnYes = new Button();
        btnYes.setText("Подтвердить");
        btnYes.setMinWidth(120);

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
        btnNo.setText("Выйти");
        btnNo.setMinWidth(120);
        btnNo.setOnMouseClicked(e -> btnNo_Click());
       
        btnNo.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(event.getCode() == KeyCode.ENTER){
                        btnNo_Click();
                }
            }
        });       
       
        Label lblParam0 = new Label();
        lblParam0.setText("Файл статистики");
        lblParam0.setMinWidth(30);
        Label lblParam1 = new Label();
        lblParam1.setText("Файл ОТД");
        lblParam1.setMinWidth(30);
        Label lblParam2 = new Label();
        lblParam2.setText("Файл результата");
        lblParam2.setMinWidth(30);
        Label lblParam3 = new Label();        
        lblParam3.setText("Файл шаблона");
        lblParam3.setMinWidth(30);        
        Label lblParam4 = new Label();
        lblParam4.setText("Кодировка");
        lblParam4.setMinWidth(30);        
        
        Button btnParam0 = new Button("Выбрать");
        btnParam0.setMinWidth(120);        
        
        Button btnParam1 = new Button("Выбрать");
        btnParam1.setMinWidth(120);          
        
        Button btnParam2 = new Button("Выбрать");
        btnParam2.setMinWidth(120);
        
        Button btnParam3 = new Button("Выбрать");
        btnParam3.setMinWidth(120);
        
        ComboBox<String> cbEncode = new ComboBox<>();
        cbEncode.setMinWidth(120);
        cbEncode.setMaxWidth(120);
        cbEncode.setPromptText("Выбрать");
        cbEncode.getItems().addAll("cp1251", "UTF-8");        
        
        TextField fieldParam0 = new TextField();
        TextField fieldParam1 = new TextField();
        TextField fieldParam2 = new TextField();
        TextField fieldParam3 = new TextField();        
        
        String enc = cbEncode.getValue();
        if(enc != null) encode = enc;
        Label encodeParam = new Label("Кодировка");
        
        cbEncode.setOnAction((ActionEvent e) -> {
            encode = cbEncode.getValue();
            lblParam3.setText(encode);
        });
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5));
        grid.setHgap(5);
        grid.setVgap(10);
        grid.setMinWidth(650);
        grid.setPrefWidth(650);
        grid.setMaxWidth(650);
        
        grid.addRow(0, lblParam0, btnParam0, fieldParam0);
        grid.addRow(1, lblParam1, btnParam1, fieldParam1);
        grid.addRow(2, lblParam2, btnParam2, fieldParam2);
        grid.addRow(3, lblParam3, btnParam3, fieldParam3);
        grid.addRow(4, encodeParam, cbEncode, lblParam4);
        
        GridPane.setHalignment(lblParam0, HPos.LEFT);
        GridPane.setHalignment(lblParam1, HPos.LEFT);
        GridPane.setHalignment(lblParam2, HPos.LEFT);
        GridPane.setHalignment(lblParam3, HPos.LEFT);
        GridPane.setHalignment(encodeParam, HPos.LEFT);
        GridPane.setHalignment(btnParam0, HPos.LEFT);
        GridPane.setHalignment(btnParam1, HPos.LEFT);
        GridPane.setHalignment(btnParam2, HPos.LEFT);
        GridPane.setHalignment(btnParam3, HPos.LEFT);
        GridPane.setHalignment(cbEncode, HPos.LEFT);
        GridPane.setHalignment(fieldParam0, HPos.LEFT);
        GridPane.setHalignment(fieldParam1, HPos.LEFT);
        GridPane.setHalignment(fieldParam2, HPos.LEFT);
        GridPane.setHalignment(fieldParam3, HPos.LEFT);
        GridPane.setHalignment(lblParam4, HPos.LEFT);
        
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(25);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(55);
        grid.getColumnConstraints().addAll(col0, col1, col2);      
        
        HBox paneBtn = new HBox(250);
        paneBtn.setAlignment(Pos.CENTER);
        paneBtn.getChildren().addAll(btnYes, btnNo);

        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.getChildren().addAll(label, grid, paneBtn);
        pane.setAlignment(Pos.CENTER);
        
        btnParam0.setOnMouseClicked(e -> {
               ugFile = controlBlock.chooseF(fileDialog, dialogStage);
               fieldParam0.setText(ugFile);
        });
        btnParam0.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(event.getCode() == KeyCode.ENTER){
                    ugFile = controlBlock.chooseF(fileDialog, dialogStage);
                    fieldParam0.setText(ugFile);
                }
            }
        });
        
        btnParam1.setOnMouseClicked(e -> { 
            otdelFile = controlBlock.chooseF(fileDialog, dialogStage);
            fieldParam1.setText(otdelFile);
        });
        btnParam1.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(event.getCode() == KeyCode.ENTER){
                    otdelFile = controlBlock.chooseF(fileDialog, dialogStage);
                    fieldParam1.setText(otdelFile);
                }
            }
        });      
        
        btnParam2.setOnMouseClicked(e -> {
            resultFile = controlBlock.chooseF(fileDialog, dialogStage);
            fieldParam2.setText(resultFile);
        });
        btnParam2.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(event.getCode() == KeyCode.ENTER){
                    resultFile = controlBlock.chooseF(fileDialog, dialogStage);
                    fieldParam2.setText(resultFile);                    
                }
            }
        });
        
        btnParam3.setOnMouseClicked(e -> {
            patternFile = controlBlock.chooseF(fileDialog, dialogStage);
            fieldParam3.setText(patternFile);
        });
        btnParam3.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(event.getCode() == KeyCode.ENTER){
                    patternFile = controlBlock.chooseF(fileDialog, dialogStage);
                    fieldParam2.setText(patternFile);                    
                }
            }
        });

        Scene scene = new Scene(pane);
        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Выбор параметров");
        dialogStage.setMinWidth(650);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
        return userDatamap;
    }

    private void btnYes_Click(){
        dialogStage.close();
        userDatamap.put("Файл статистики", ugFile);
        userDatamap.put("Файл ОТД", otdelFile);
        userDatamap.put("Файл результата", resultFile);
        userDatamap.put("Кодировка", encode);
        userDatamap.put("Шаблон", patternFile);
    }

    private static void btnNo_Click(){
        dialogStage.close();
    }
}
