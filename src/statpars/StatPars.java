
package statpars;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.geometry.Insets;
import javafx.event.EventHandler;
import controlblock.ControlBlock;
import logica.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class StatPars extends Application {
   
    private Stage stage;
    private TextArea textAreaOut;
    private String encode = "cp1251";
    private final String fileName = "workdir/nadstat.txt";
    private String parameters = "defParam";
    private String mode = "Главное меню";
    private final String separ = System.getProperty("line.separator");
    private final ControlBlock controlBlock = new ControlBlock();
    private final UgolStat ugStat = new UgolStat();
    private final RozyskStat rozStat = new RozyskStat();
    private final NadzorStat nadStat = new NadzorStat();
    private final InformScreen informScreen = new InformScreen();
    
    public StatPars(){
               
    }
    
    @Override
    public void start(Stage primaryStage) {
               
        stage = primaryStage;
        HBox root = new HBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        VBox vbRight = new VBox();
        vbRight.setMinWidth(830);
        vbRight.setMaxWidth(830);
        
        textAreaOut = new TextArea();
        textAreaOut.setPromptText("Информационный экран");
        textAreaOut.setWrapText(true);
        textAreaOut.setStyle(
            "-fx-border-color: grey;" +
            "-fx-border-radius: 3;" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: black;"             
            );
        
        textAreaOut.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(textAreaOut, Priority.ALWAYS);
        vbRight.getChildren().add(textAreaOut);
        
        VBox vbLeft = new VBox();
        vbLeft.setSpacing(10);
        vbLeft.setMaxHeight(Double.MAX_VALUE);          

        VBox vbMode = new VBox();
        vbMode.setMinWidth(230);
        vbMode.setMaxWidth(230);
        vbMode.setMinHeight(194);
        vbMode.setStyle(
            "-fx-border-color: grey;" +
            "-fx-border-radius: 3;"
            );
        vbMode.setSpacing(10);
        vbMode.setPadding(new Insets(10));
        Label lblMode = new Label("Режим работы");
        RadioButton rbtnMenu = new RadioButton("Главное меню");
        rbtnMenu.setUserData("Главное меню");
        rbtnMenu.setOnMouseClicked(e -> selectInform());
        
        RadioButton rbtnUgstat = new RadioButton("Торговая статистика");
        rbtnUgstat.setUserData("Торговая статистика");
        rbtnUgstat.setOnMouseClicked(e -> selectUgolStat());
        
        RadioButton rbtnRozysk = new RadioButton("Оборотная статистика");
        rbtnRozysk.setUserData("Оборотная статистика");
        rbtnRozysk.setOnMouseClicked(e -> selectRozyskStat());
        
        RadioButton rbtnNadzor = new RadioButton("Админ. контроль");
        rbtnNadzor.setUserData("Административный контроль");
        rbtnNadzor.setOnMouseClicked(e -> selectNadzorStat());
        
        RadioButton rbtnFormal = new RadioButton("Федерал. контроль");
        rbtnFormal.setUserData("Федеральный контроль");
        rbtnFormal.setOnMouseClicked(e -> selectNadzorStat());
        
        rbtnMenu.setSelected(true);
        ToggleGroup modeGroup = new ToggleGroup();
        rbtnMenu.setToggleGroup(modeGroup);
        rbtnUgstat.setToggleGroup(modeGroup);
        rbtnRozysk.setToggleGroup(modeGroup);
        rbtnNadzor.setToggleGroup(modeGroup);
        rbtnFormal.setToggleGroup(modeGroup);
        
        modeGroup.selectedToggleProperty().addListener(
            new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle old_toggle, Toggle new_toggle) {
                    if (modeGroup.getSelectedToggle() != null) {
                        mode = modeGroup.getSelectedToggle().
                                getUserData().toString();
                    }                                 
                }
        });

        vbMode.getChildren().addAll(lblMode, rbtnMenu, rbtnUgstat,
                rbtnRozysk, rbtnNadzor, rbtnFormal);

        VBox vbDefault = new VBox();
        vbDefault.setMinWidth(230);
        vbDefault.setMaxWidth(230);
        vbDefault.setMinWidth(100);
        vbDefault.setStyle(
                "-fx-border-color: grey;" +
                "-fx-border-radius: 3;"
            );
        vbDefault.setSpacing(10);
        vbDefault.setPadding(new Insets(10));
        Label lblDefault = new Label("Исходные параметры");
        RadioButton rbtnDefault = new RadioButton("По умолчанию");
        rbtnDefault.setUserData("defParam");
        RadioButton rbtnUserMode = new RadioButton("Изменить параметры");
        rbtnUserMode.setUserData("userParam");
        rbtnDefault.setSelected(true);
        ToggleGroup defaultGroup = new ToggleGroup();
        rbtnDefault.setToggleGroup(defaultGroup);
        rbtnUserMode.setToggleGroup(defaultGroup);
        
        defaultGroup.selectedToggleProperty().addListener(
            new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle old_toggle, Toggle new_toggle) {
                    if (defaultGroup.getSelectedToggle() != null) {
                        parameters = defaultGroup.getSelectedToggle().
                                getUserData().toString();                
                    }
                    selectInform();//автоматический возврат на главный экран
                    rbtnMenu.setSelected(true);//возврат радиокнопки на гл.реж.                 
                }
        });
        
        vbDefault.getChildren().addAll(lblDefault, rbtnDefault, rbtnUserMode);
        
        VBox vbEncode = new VBox();
        vbEncode.setMinWidth(230);
        vbEncode.setMaxWidth(230);
        vbEncode.setMinHeight(80);
        vbEncode.setStyle(
            "-fx-border-color: grey;" +
            "-fx-border-radius: 3;"
            );
        vbEncode.setSpacing(10);
        vbEncode.setPadding(new Insets(10));
        Label lblEncode = new Label("Кодировка");
        ComboBox<String> cbEncode = new ComboBox<>();
        cbEncode.setMinWidth(120);
        cbEncode.setMaxWidth(120);
        cbEncode.setPromptText("cp1251");
        cbEncode.getItems().addAll("cp1251", "UTF-8");
        cbEncode.setOnAction(e -> {
            encode = cbEncode.getValue();
            textAreaOut.appendText(separ + separ + "Выбрана кодировка: " +
                    encode + separ);
            });
        vbEncode.getChildren().addAll(lblEncode, cbEncode);
                
        VBox vbControl = new VBox();
        vbControl.setMinWidth(230);
        vbControl.setMaxWidth(230);
        vbControl.setMaxHeight(Double.MAX_VALUE);
        vbControl.setStyle(
            "-fx-border-color: grey;" +
            "-fx-border-radius: 3;"
            );
        vbControl.setSpacing(10);
        vbControl.setPadding(new Insets(10));
        VBox.setVgrow(vbControl, Priority.ALWAYS);
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Label lblControl = new Label("Блок управления");
        Button btnChoose = new Button("Проверить данные");
        Button btnParse = new Button("Обработать файл");
        Button btnClear = new Button("Очистить экран");
        Button btnClose = new Button("Выход");
       
        btnChoose.setMaxWidth(Double.MAX_VALUE);
        btnChoose.setMinHeight(50);
        btnChoose.setOnMouseClicked(e -> checkFile());
        btnChoose.setOnKeyPressed(e -> checkFile());

        btnParse.setMaxWidth(Double.MAX_VALUE);
        btnParse.setMinHeight(50);
        btnParse.setOnMouseClicked(e -> processFile());
        btnParse.setOnKeyPressed(new EventHandler<KeyEvent>(){
           @Override
           public void handle(KeyEvent event){
               if(event.getCode() == KeyCode.ENTER){
                   processFile();
               }
           }
        });    
        
        btnClear.setMaxWidth(Double.MAX_VALUE);
        btnClear.setMinHeight(50);
        btnClear.setOnMouseClicked(e -> clearTextArea());
        btnClear.setOnKeyPressed(e -> clearTextArea());

        btnClose.setMaxWidth(Double.MAX_VALUE);
        btnClose.setMinHeight(50);
        btnClose.setOnMouseClicked(e -> btnClose_Click());
        
        btnClose.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(event.getCode() == KeyCode.ENTER){
                        btnClose_Click();
                }
            }
        });
        
        vbControl.getChildren().addAll(lblControl, btnChoose,
        btnParse, btnClear, spacer, btnClose);

        vbLeft.getChildren().addAll(vbMode, vbDefault, vbEncode, vbControl);
        root.getChildren().addAll(vbLeft, vbRight);

        if(rbtnMenu.isSelected()){
            informScreen.getInform(encode, textAreaOut);
        }          
               
        Scene scene = new Scene(root, 1090, 837);
        primaryStage.setTitle("R.O.B.O.S.A.L.E.");
        primaryStage.setScene(scene);
        primaryStage.show();		
    }
        
    private void selectInform(){
        informScreen.getInform(encode, textAreaOut);
    }
    
    private void selectUgolStat(){
        ugStat.getParam(parameters, textAreaOut, encode, mode);
    }
    
    private void selectRozyskStat(){
        rozStat.getParam(parameters, textAreaOut, encode, mode);
    }
    
    private void selectNadzorStat(){
        nadStat.getParam(parameters, textAreaOut, encode, mode);
    }

    private void checkFile(){
        switch(mode){
            case("Главное меню"):
                textAreaOut.appendText(separ + separ +
                        "Не выбран режим работы!" + separ);
                break;
            case("Торговая статистика"):
                ugStat.checkStat(textAreaOut);
                break;
            case("Оборотная статистика"):
                rozStat.checkStat(textAreaOut);
                break;
            case("Административный контроль"):
                nadStat.checkStat(textAreaOut);
                break;
            case("Федеральный контроль"):
                nadStat.checkStat(textAreaOut);
        }        
    }
    
    private void processFile(){                
        switch(mode){
            case("Главное меню"):
                textAreaOut.appendText(separ + separ +
                        "Не выбран режим работы!" + separ);
                break;
            case("Торговая статистика"):
                ugStat.checkGetStat(textAreaOut);
                break;        
            case("Оборотная статистика"):                
                rozStat.checkGetStat(textAreaOut);
                break;
            case("Административный контроль"):
                nadStat.checkGetStat(textAreaOut);
                break;
            case("Федеральный контроль"):
                nadStat.checkGetStat(textAreaOut);
        }                
    }
    
    private void readFile(){
        textAreaOut.clear();
        controlBlock.readF(fileName, textAreaOut, encode);
    }
    
    private void clearTextArea(){
        textAreaOut.clear();
    }

    private void btnClose_Click(){
        controlBlock.close(stage);
    }      

    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args) {
        Application.launch(args);
    }
}