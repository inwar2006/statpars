
package logica;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Данный класс предназначен для выборки данных из текстового файла
 * статистики между строками-разделителями и с учетом необходимых отделов.
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class ExtractNadzorData {
    //строка-разделитель, используемая в текстовом файле статистики
    private final String razdelStr = "------------";
    //рабочий список данных из файла статистики
    private final LinkedList<String> statList = new LinkedList<>();
    //сет необходимых отделов
    private final TreeSet<String> otdNadSet = new TreeSet<>();
   
    public ExtractNadzorData(){
        
    }    
    
    /**
     * Метод выбирает данные между двумя строками разделения "-------" в начале
     * и одной в конце таблицы, а затем возвращает только необходимые строки,
     * причем только для необходимых отделов.
     * @param nadStat
     * @param otdNadzor
     * @param encodeStr
     * @return statList
     */
    public LinkedList<String> toExtractData(String nadStat, 
            String otdNadzor, String encodeStr){
        String lineStat;// текущая строка из файла
        String subLineStat; //промежуточная подстрока для замены
        int countLine = 0;// счетчик разделителей
        String[] arr; // промежуточный массив для обработки текущей строки
        
        //заполняем сет ovdnadzorset необходимыми отделами
        getNadzorOvd(otdNadzor, encodeStr);
        
        try(BufferedReader bfReadStat = new BufferedReader(
                            new InputStreamReader(
                                new FileInputStream(nadStat), encodeStr))){
            while((lineStat = bfReadStat.readLine()) != null && 
                    (countLine < 3)){                
                if(lineStat.contains(razdelStr)){
                    countLine++;
                    continue;
                }
                if((!lineStat.contains(razdelStr)) && (countLine >= 2)){
                    arr = lineStat.split("\\|", -1);
                    subLineStat = arr[1].trim();
                    if(otdNadSet.contains(subLineStat)){
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
    
    private void getNadzorOvd(String fileSource, String encodeStr){
        String str;
        
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileSource), encodeStr))){
            while((str = br.readLine()) != null){
                otdNadSet.add(str);
            }
        }
        catch(FileNotFoundException fex){
            System.out.println("File not found exception " + fex);
        }
        catch(IOException iex){
            System.out.println("IO error " + iex);
        }        
    }
}
