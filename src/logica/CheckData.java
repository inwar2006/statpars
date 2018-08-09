
package logica;

import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Данный класс предназначен для проверки файла на предмет
 * содержания в нём необходимых строк из файла шаблона.
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class CheckData {
    //проверяемый файл статистики
    private String checkFile;
    //файл-шаблон образцов строк для проверки
    private String patternFile;
    //кодировка
    private String encode;
    //количество совпадений, необходимых для прохождения проверки
    private int checkValue;
    //количество проверяемых строк в исх.файле
    private int countLines;
    //список проверяемых строк исхдного файла
    private final LinkedList<String> checkList = new LinkedList<>();
    //объект класса для заполнения списка из файла (в д.сл.шаблона)
    private final FillList fillList = new FillList();
                  
    public CheckData(){        
        
    }
    
    /**
     * Данный метод проверяет строки из рабочего файла статистики
     * на предмет содержания в нём необходимих строк из файла
     * шаблона. При наличии всех заданных строк возвращает true
     * 
     * @param chFile
     * @param ptrnFile
     * @param enCode
     * @return checkValue
     */
    public boolean toCheckData(String chFile, String ptrnFile, 
            String enCode){
        checkFile = chFile;
        patternFile = ptrnFile;
        encode = enCode;
        String str;//временная строка чтения из файла
                
        //заполняем список строками из файла шаблона
        fillList.toFillList(patternFile, checkList, encode);
                    
        if(checkList.isEmpty()) return false;
        checkValue = checkList.size();
        countLines = checkValue * 3;
                
        try(BufferedReader bfReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(checkFile), encode))){
            for(int i = 0; i < countLines; i++){
                str = bfReader.readLine();                             
                for(String s: checkList){
                    if(str.contains(s)) checkValue--;             
                }
            }                            
        }
        catch(FileNotFoundException fex){
            System.out.println("File not found! " + fex);
        }
        catch(IOException iex){
            System.out.println("IOException! " + iex);
        }
           
        return checkValue <= 0; //результат проверки
    }
}
