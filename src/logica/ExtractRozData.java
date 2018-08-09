
package logica;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Данный класс предназначен для выборки необходимых данных
 * из текстового файла между разделяющими строками типа ---------
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class ExtractRozData {
    private final String razdelStr = "----------";
    private final LinkedList<String> statList = 
            new LinkedList<>(); 
    
    /**
     * конструктор
     */
    public ExtractRozData(){
        
    }
    
    /**
     * Метод выбирает данные между двумя строками разделения "-------" в начале
     * и одной в конце таблицы, а затем возвращает только необходимые строки
     * @param fileName
     * @param encodeStr
     * @return statList
     */
    public LinkedList<String> toExtractData(String fileName, String encodeStr){
        String lineStat;// текущая строка из файла
        String subLineStat; //промежуточная подстрока для замены
        int countline = 0;// счетчик разделителей
        
        try(BufferedReader bfreadStat = new BufferedReader(
                            new InputStreamReader(
                                new FileInputStream(fileName), encodeStr))){
            while((lineStat = bfreadStat.readLine()) != null && (countline < 3)){
                
                if(lineStat.contains(razdelStr)){
                    countline++;
                    continue;
                }
                if((!lineStat.contains(razdelStr)) && (countline >= 2)){
                    if(lineStat.contains("ОПЕ из РЕ")){
                        subLineStat = lineStat.replace("ОПЕ из РЕ", "ОПЕизРЕ  ");
                        statList.add(subLineStat);
                    }
                    else{
                        statList.add(lineStat);
                    }                   
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
