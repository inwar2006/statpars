
package logica;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Данный класс предназначен для выборки данных между заданными
 * разделителями и только при наличии нужных значений в строке,
 * заданных во внешнем файле.
 *
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class ExtractData {
    private final String razdelStr = "----------";
    private final LinkedList<String> statList = new LinkedList<>(); 
    
    public ExtractData(){
        
    }
    
    /**
     * Метод выбирает данные между строками разделения "-------"
     * и возвращает рабочий список только тех строк, где есть заданные
     * пользователем значения, например название отдела
     * (из внешнего файла filename).
     *
     * @param fileName
     * @param encodeStr
     * @return
     */
    public LinkedList<String> toExtractData(String fileName, String encodeStr){
        String lineStat;// текущая строка из файла
        int countLine = 0;// счетчик разделителей
        
        try(BufferedReader bfreadstat = new BufferedReader(
                            new InputStreamReader(
                                new FileInputStream(fileName), encodeStr))){
            while((lineStat = bfreadstat.readLine()) != null && (countLine < 3)){
                
                if(lineStat.contains(razdelStr)){
                    countLine++;
                    continue;
                }
                if((!lineStat.contains(razdelStr)) && (countLine >= 2)){
                    statList.add(lineStat);
                }                
            }
        }
        catch(FileNotFoundException fex){
            System.out.println("File not found!\nНе найден один"
                    + " из исходных файлов");
        }
        catch(IOException iex){
            System.out.println("IO exception!\n" +"Ошибка"
                    + " ввода/вывода " + iex);
        }        
        return statList;
    }
}
