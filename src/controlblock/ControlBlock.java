
package controlblock;

import java.io.BufferedReader;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import dialoguser.ModalDialogExit;

/**
 * Utility class to store of methods for 
 * processing some standart events
 * Вспомогательный класс для хранения методов
 * обработки некоторых стандартных событий
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class ControlBlock {
    
    /**
     * Method for choosing of file by using standart
     * class javafx.FileChooser and filter of file
     * extension javafx.FileChooser.ExtensiontFilter
     * Метод выбора файла с использованием стандартного
     * класса javafx.FileChooser и фильтра по расширению
     * javafx.FileChooser.ExtensionFilter.
     * 
     * @param fdialog
     * @param stage
     * @return
     */
    public String chooseF(FileChooser fdialog, Stage stage){
        fdialog.setTitle("Выбор файла");
        fdialog.getExtensionFilters().add(
                new ExtensionFilter("Text Files", "*.txt"));
        String filename = "";
        File file = fdialog.showOpenDialog(stage);
        if(file == null){
            return filename;
        }
        filename = file.getPath();
        return filename;
    }
    
    public void clear(TextArea txtArea){
        txtArea.clear();
    }
    
    /**
     * Метод чтения текстового файла заданной кодировки
     * и вывода текста на экран
     * Method for reading text file with defined encoding
     * and text output to the screen
     * 
     * @param name
     * @param txtArea
     * @param encoding
     */
    public void readF(String name, TextArea txtArea, String encoding){
        String stroka;
        String separ = System.getProperty("line.separator");
        if(!"".equals(name)){
            try(BufferedReader br = new BufferedReader(new 
                InputStreamReader(new FileInputStream(name), encoding))){
                txtArea.appendText(separ);
                while((stroka = br.readLine()) != null){
                    txtArea.appendText(stroka + separ);
                }
            }
            catch(FileNotFoundException fex){
                System.out.println("File not found! " + fex);
            }
            catch(IOException iex){
                System.out.println(iex);
            }
        }
        else{
            txtArea.appendText("Вам следует выбрать файл!" + separ);
        }
    }
    
    /**
     * Метод закрытия GUI-приложения на javafx.
     * Method for closing javafx GUI-application 
     *
     * @param st
     */
    public void close(Stage st){
        boolean confirm;
        confirm = ModalDialogExit.show("Действительно хотите выйти?",
            "Подтвердите действие", "Да", "Нет");
        if(confirm){
            st.close();
        }
    }
}
