
package logica;

import dialoguser.ModalDialogParamRoz;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.control.TextArea;

/**
 * Класс для обработки оборотной статистики
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class RozyskStat{
    //карта данных от пользователя
    private LinkedHashMap<String, String> userDataMap = new LinkedHashMap<>();
    //карта рабочих параметров
    private LinkedHashMap<String, String> askMap = new LinkedHashMap<>();
    //карта результатов по отделам
    private TreeMap<String, Integer> otdelMap = new TreeMap<>();
    //основной рабочий список статистики
    private LinkedList<String> statList = new LinkedList<>();
    //"перевод строк" в данной ОС (для записи в файл)
    private final String separ = System.getProperty("line.separator");
    //рабочая директория указывается без 1-го слэша
    private final String workDir = "workdir/";
    //кодировка (по умолчанию cp1251)
    private String encodeStr = "cp1251";
    //рабочий файл статистики по умолчанию
    private String rozStat = workDir + "rozstat.txt";
    //файл список из БД по умолчанию
    private String baza = workDir + "baza.txt";
    //файл результата обработки
    private String result = workDir + "rozresult.txt";
    //файл шаблона для проверки
    private String patternFile = workDir + "rozpattern.txt";
    //модуль диалога для выбора параметров пользователя
    private final ModalDialogParamRoz modalDialogParamRoz = 
            new ModalDialogParamRoz();
    //модуль автоматической проверки параметров
    private final CheckData chData = new CheckData();
           
    public RozyskStat(){
         
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
        askMap.put("Файл статистики", rozStat);
        askMap.put("Файл БАЗА", baza);
        askMap.put("Файл результата", result);
        askMap.put("Кодировка", "cp1251");
        askMap.put("Шаблон", patternFile);
        
        txtArea.clear();
        txtArea.appendText(separ + "Выбран режим \"" + mode + "\"" +
            separ            
        );      
                                
        if(parameters.equals("userParam")){            
            userDataMap = modalDialogParamRoz.askParam(askMap);
            askMap.putAll(userDataMap);
            txtArea.appendText(("Выбраны параметры пользователя:" + separ));
            //вывод на экране параметров, полученных от пользователя
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
        rozStat = askMap.get("Файл статистики");
        baza = askMap.get("Файл БАЗА");
        result = askMap.get("Файл результата");
        encodeStr = askMap.get("Кодировка");
        patternFile = askMap.get("Шаблон");
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
     * сопоставление с заданными шаблонными словами
     * @param txtArea
     * @return checkParam
     */
    public boolean checkStat(TextArea txtArea){
        boolean checkParam;
        checkParam = chData.toCheckData(rozStat, patternFile, encodeStr);
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
     * Метод и проверки и обработки основного рабочего файла статистики
     * @param txtArea
     * @return checkParam
     */
    public boolean checkGetStat(TextArea txtArea){
        boolean checkParam;
        checkParam = chData.toCheckData(rozStat, patternFile, encodeStr);
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
        //toExtractData класса ExtractData и помещаем в список statList
        //statList = new LinkedList<>();  
        ExtractRozData extData = new ExtractRozData();
        statList = extData.toExtractData(rozStat, encodeStr);
        
        //создаем 2 промежуточных списка для тех, кто есть и кого нет в БД
        LinkedList<String> vSove = new LinkedList<>();
        LinkedList<String> nevSove = new LinkedList<>();
        
        String bazaStr; //строка из БД
        String newSovaStr; //строка из БД, приведенная к общему виду
        String onlineStr; //строка из о.статистики, приведенная к общему виду
        String[] arr; // промежуточный массив для рабочей строки
        int countRoz = 0; //счетчик общего количества в обороте
        int countvSove = 0; //счетчик количества внесенных в БД
        int countnevSove = 0; //счетчик количества не внесенных в БД
        int countHardRoz = 0; //счетчик общего количества в обороте по продажам
        int countHardvSove = 0;//счетчик внесенных в базу по продажам
        int countHardnevSove = 0;//счетчик не внесенных в базу по продажам
        
        /*
        читаем 1 строку из списка статистики, приводим эту строку к общему
        виду (для сравнения со строкой из БД): Ф И О ДД.ММ.ГГГГ 
        (перевернем, т.к.дата в статистике записана наоборот). Строку из БД
        также приведем к общему виду (табуляции заменим пробелами!)
        */
        for(String s: statList){            
            arr = s.split(" +");
            if(arr.length > 16){
                System.out.println("Stroka: " + s);
                txtArea.appendText(separ + "Внимание! Нестандартные" + 
                        " данные!" + s + separ + separ);
                continue;
            }
            onlineStr = arr[6] + " " + arr[7] + " " + arr[8] + " " +
                    arr[9].substring(6) + "." + 
                    arr[9].substring(4, 6) + "." +
                    arr[9].substring(0, 4);
                                   
            try(BufferedReader bfReadSova = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(baza), encodeStr));){
                
                while((bazaStr = bfReadSova.readLine()) != null){
                    newSovaStr = bazaStr.replace("\t", " ");
                    //если нашли совпадение - пишем в список "есть в БД" и
                    //выходим из цикла, больше в нем не надо смотреть
                    if(newSovaStr.equals(onlineStr)){
                        //txtArea.appendText(s);
                        vSove.add(s);
                        break;
                    }            
                }
                //дошли до конца БД и не нашли совпадение - пишем в список
                //"нет в БД", продолжаем поиск с новой строкой из статистики
                if(bazaStr == null){
                    nevSove.add(s);
                }
            }
            catch(FileNotFoundException fex){
                System.out.println("File not found exception! " + fex);
            }
            catch(IOException iex){
                System.out.println("IO exception " + iex);
            }
        }    
        
        // подсчет оборотной статистики
        for(String s: statList){
            countRoz++;
            arr = s.split(" +");
            //подсчет по "продажам" (коды продаж "1" или "4")
            if(arr[12].contains("1") || arr[12].contains("4")){
                countHardRoz++;
            }
        }
        //вывод результатов на экран
        txtArea.appendText("Общий итог:" + separ);
        txtArea.appendText("Всего в обороте: " + countRoz + separ);
        txtArea.appendText("Из них по продажам: " + countHardRoz + separ + separ);
        txtArea.appendText("Оборотные, внесенные в базу:");
        
        for(String vs: vSove){
            txtArea.appendText(vs + "!" + separ);
            countvSove++;
            arr = vs.split(" +");
            if(arr[12].contains("1") || arr[12].contains("4")){
                countHardvSove++;
            }
        }
        txtArea.appendText(separ);
        txtArea.appendText("Внесено оборотных в базу: " +
                countvSove + separ);
        txtArea.appendText("Из них по продажам: " + countHardvSove +
                separ + separ);
        
        txtArea.appendText("Оборотные, не внесенные в базу:" + separ);
        for(String ns: nevSove){
            txtArea.appendText(ns + "?" + separ);
            countnevSove++;
            arr = ns.split(" +");
            if(arr[12].contains("1") || arr[12].contains("4")){
                countHardnevSove++;
            }
        }
        txtArea.appendText(separ);
        txtArea.appendText("Не внесено оборотных в базу: " +
                countnevSove + separ);
        txtArea.appendText("Из них по продажам: " +
                countHardnevSove + separ);
             
        try(BufferedWriter bfwritestat = new BufferedWriter(
                new FileWriter(result, true))){                     
            
            bfwritestat.write(separ);
            bfwritestat.write("Результат обработки оборотной статистики: " +
                    separ);
            bfwritestat.write(separ);
            bfwritestat.write("Всего в обороте: " + countRoz + separ);
            bfwritestat.write("Из них по продажам: " + countHardRoz +
                    separ);
            bfwritestat.write(separ);
            bfwritestat.write("Оборотные, внесенные в базу:" + separ);
            for(String s: vSove){
                bfwritestat.write(s.substring(5) + separ);
            }
            bfwritestat.write(separ);
            bfwritestat.write("Внесено оборотных в базу: "+ countvSove +
                    separ);
            bfwritestat.write("Из них по продажам: "+ countHardvSove + separ);
            bfwritestat.write(separ);
            
            bfwritestat.write("Оборотные, не внесенные в базу:" + separ);
            for(String s: nevSove){
                bfwritestat.write(s.substring(5) + separ);
            }
            bfwritestat.write(separ);
            bfwritestat.write("Не внесено оборотных в базу: " 
                    + countnevSove + separ);
            bfwritestat.write("Из них по продажам: "+ countHardnevSove + separ);
            bfwritestat.write(separ);        
        }
        catch(FileNotFoundException fex){
            System.out.println("File not found!\nНе найден один"
                    + " из исходных файлов");
        }
        catch(IOException iex){
            System.out.println("IO exception!\n" +"Ошибка"
                    + " ввода/вывода " + iex);
        }        
    }   
}