
package logica;

import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import javafx.scene.control.TextArea;
import dialoguser.ModalDialogParam;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;


/**
 * Класс для обработки торговой статистики
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class UgolStat {
    
    //"перевод строк" в данной ОС(для вывода на экран и записи в файл)
    private final String separ = System.getProperty("line.separator"); 
    //рабочая директория указывается без 1-го слэша
    private final String workDir = "workdir/";
    //карта данных от пользователя
    private LinkedHashMap<String, String> userDataMap = new LinkedHashMap<>();
    //карта рабочих параметров
    private LinkedHashMap<String, String> askMap = new LinkedHashMap<>();
    //карта результатов по отделам
    private TreeMap<String, Integer> otdelMap = new TreeMap<>();
    //основной рабочий список статистики
    private LinkedList<String> statList = new LinkedList<>();
    //кодировка (по умолчанию cp1251)
    private String encodeStr = "cp1251";
    //рабочий файл статистики по умолчанию
    private String ugStat = workDir + "ugstat.txt";
    //файл список отделов, определяется пользователем
    private String otdel = workDir + "otdel.txt";
    //файл результатов обработки
    private String result = workDir + "result.txt";
    //файл шаблона для проверки
    private String patternFile = workDir + "ugpattern.txt";
    //модуль диалога для выбора параметров пользователя
    private final ModalDialogParam modalDialogParam = new ModalDialogParam();
    //модуль автоматической проверки параметров
    private final CheckData chData = new CheckData();

    public UgolStat(){
      
    }    
    
    /**
     *
     * @param parameters
     * @param txtArea
     * @param encode
     * @param mode
     */
    public void getParam(String parameters, TextArea txtArea,
            String encode, String mode){ 
        
        encodeStr = encode;
        
        //загрузка параметров по умолчанию
        askMap.put("Файл статистики", ugStat);
        askMap.put("Файл ОТД", otdel);
        askMap.put("Файл результата", result);
        askMap.put("Кодировка", encodeStr);
        askMap.put("Шаблон", patternFile);
        txtArea.clear();
        txtArea.appendText(separ + "Выбран режим \"" + mode + "\"" +
            separ            
        );
        
        if(parameters.equals("userParam")){            
            userDataMap = modalDialogParam.askParam(askMap);
            askMap.putAll(userDataMap);
            txtArea.appendText(("Выбраны параметры пользователя:" + separ));
            //вывод на экран параметров, полученных от пользователя
            printMap(askMap, txtArea);
            txtArea.appendText(separ + separ +
                    "Для проверки файла нажмите \"Проверить данные\"" 
            );
            txtArea.appendText(separ + separ +
            "Для обработки файла нажмите \"Обработать файл\"");
        }
        else{
            txtArea.appendText(separ + "Выбраны параметры по умолчанию: "
            + separ + separ);
            //вывод на экран параметров по умолчанию
            printMap(askMap, txtArea);
            txtArea.appendText(separ + separ +
                    "Для проверки файла нажмите \"Проверить данные\"" 
            );
            txtArea.appendText(separ + separ +
                    "Для обработки файла нажмите \"Обработать файл\"" 
            );
        }
        //обратный прием параметров после выбора умолчание/пользовательские
        ugStat = askMap.get("Файл статистики");
        otdel = askMap.get("Файл ОТД");
        result = askMap.get("Файл результата");
        encodeStr = askMap.get("Кодировка");
        patternFile = askMap.get("Шаблон");
        
        FillMap fillMap = new FillMap();
        otdelMap = fillMap.toFillMap(otdel, encodeStr);   
    }
    
    /**
     * Метод вывода на экран выбранных параметров для метода обработки getStat
     * @param userMap
     * @param txtArea 
     */
    private void printMap(Map<String, String> userMap, TextArea txtArea){
        userMap.keySet().forEach((String key) -> {
            txtArea.appendText(key + " : " + userMap.get(key) + separ);
        });
    }      
       
    /**
     * Метод проверки основного рабочего файла статистики через
     * сопоставление с заданными шаблонными значениями
     * @param txtArea
     * @return checkParam
     */
    public boolean checkStat(TextArea txtArea){
        boolean checkParam;
        checkParam = chData.toCheckData(ugStat, patternFile, encodeStr);
        if(checkParam){
            txtArea.appendText(separ + "Проверка пройдена успешно! " + separ);
        }
        else{
            txtArea.appendText(separ + "Проверка не пройдена!" + separ +
                "Проверьте исходные данные!" + separ);
        }
        return checkParam;        
    }
    
    /**
     * Метод проверки, и обработки основного рабочего файла статистики
     * @param txtArea
     * @return checkParam
     */
    public boolean checkGetStat(TextArea txtArea){
        boolean checkParam;
        checkParam = chData.toCheckData(ugStat, patternFile, encodeStr);
        if(checkParam){
            txtArea.appendText(separ + "Проверка пройдена успешно! " + separ);
            getStat(txtArea);
        }
        else{
            txtArea.appendText(separ + "Проверка не пройдена!" + separ +
                    "Проверьте исходные данные!" + separ);
        }
        return checkParam;        
    }
    
    /**
     * Основной метод обработки статистики данного модуля (логика)
     * @param txtArea
     */
    public void getStat(TextArea txtArea){        
        
        //создаем рабочий список строк из файла статистики через метод 
        //toExtractData класса ExtractData и помещаем в список statlist
        ExtractData extdata = new ExtractData();
        statList = extdata.toExtractData(ugStat, encodeStr);
        txtArea.appendText(separ);
        
        //промежуточная строка ключа из карты для сравнения
        String mapStr; 
        //счетчик общего результата карты
        int i = 0; 
        Set<Map.Entry<String, Integer>> mapSet = otdelMap.entrySet();
        
        try(BufferedWriter bfwritestat = new BufferedWriter(
                new FileWriter(result, true))){            
            bfwritestat.write(separ);
            bfwritestat.write("Результат обработки оборотной статистики: "
                    + separ);
            bfwritestat.write(separ);
            
            //процесс записи результата в файл результата
            for(String str: statList){
                String[] arr = str.split(" +");//разбиваем по пробелам в массив
                String strotdel = arr[2]; // название отдела
                String sfull = str.substring(5); //пропуск 5 позиций от
                //начала строки, т.к. то, что до названия отдела нам не надо
                
                for(Map.Entry<String, Integer> me: mapSet){
                    mapStr = me.getKey();
                    if(mapStr.equals(strotdel)){
                        bfwritestat.write(sfull + separ);                        
                        otdelMap.put(mapStr, otdelMap.get(mapStr) + 1); 
                        i++;
                    }                    
                }                
            }
            txtArea.appendText(separ);
            txtArea.appendText("Общий итог: " + separ + separ);
            bfwritestat.write("" + separ);
            bfwritestat.write("Общий итог: "+ separ);
            bfwritestat.write(separ);
            for(Map.Entry<String, Integer> me: mapSet){
                bfwritestat.write(me.getKey()+ " : " + me.getValue() + separ);
                txtArea.appendText(me.getKey()+ " : " + me.getValue() + separ);
            }
            bfwritestat.write(separ);
            bfwritestat.write("Всего: " + i);        
        }
        catch(FileNotFoundException fex){
            System.out.println("File not found!\nНе найден один"
                    + " из исходных файлов");
        }
        catch(IOException iex){
            System.out.println("IO exception!\n" +"Ошибка"
                    + " ввода/вывода " + iex);
        }              
        txtArea.appendText(separ);
        txtArea.appendText("Всего: " + i);
    }
}