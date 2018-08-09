
package logica;

import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Данный класс предназначен для считывания данных (строк) из файла
 * и заполнения ими карты (словаря). Ключами карты являются сами 
 * строки, а значения целыми числами (по умолчанию нулями) - для
 * использования как счетчик в последующих стат.вычислениях.
 *  
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class FillMap {
    
    private final TreeMap<String, Integer> treeMap = new TreeMap<>();    
    
    /**
     * Метод заполнения карты (словаря) строками из файла. Ключами
     * являются сами строки, а значения предназначены для калькуляции
     * статистики. Возвращает карту с нулевыми значениями. В качестве
     * параметров принимает название исходного файла с данными, а
     * также кодировку системы.
     *
     * @param file
     * @param encode
     * @return treeMap
     */
    public TreeMap<String, Integer> toFillMap(String file, String encode){
        
        // строка названия отдела из заданного файла
        String strovd;
        
        try(BufferedReader bfreader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(file), encode))){
            while((strovd = bfreader.readLine()) != null ){
                treeMap.put(strovd.trim(), 0);
            }           
        }
        catch(FileNotFoundException fex){
            System.out.println("File not found! " + fex);
        }
        catch(IOException iex){
            System.out.println("Error io " + iex);
        }
        return treeMap;
    }
}

