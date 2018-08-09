
package logica;

import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Данный класс предназначен для считывания строк
 * из файла и заполнения ими любого списка.
 *
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class FillList{
        
    /**
     * Метод заполнения списка строками из файла. В качестве
     * параметров принимает строку с полным именем исходного
     * файла с данными, объект любого списка, а также кодировку
     * системы. Заполняет принятый список и возвращает его же.
     *
     * @param fileName
     * @param listName
     * @param encode
     * @return listName
     */
    public List<String> toFillList(String fileName,
            List<String> listName, String encode){
        
        String str;
                        
        try(BufferedReader bfReader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(fileName), encode))){
            while((str = bfReader.readLine()) != null ){        
                listName.add(str.trim());
            }           
        }
        catch(FileNotFoundException fex){
            System.out.println("File not found! " + fex);
        }
        catch(IOException iex){
            System.out.println("Error io " + iex);
        }
        return listName;
    }
}

