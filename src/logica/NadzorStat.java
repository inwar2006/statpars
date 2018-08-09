
package logica;

import dialoguser.ModalDialogParamNadzor;
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
import java.util.Set;
import javafx.scene.control.TextArea;

/**
 * Класс для обработки статистики адм.контоля
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class NadzorStat{
    //карта данных от пользователя
    private LinkedHashMap<String, String> userDataMap = new LinkedHashMap<>();
    //карта рабочих параметров
    private LinkedHashMap<String, String> askMap = new LinkedHashMap<>();
    //карта лиц, присутствующих в БД
    private TreeMap<String, Integer> otdelMapIn = new TreeMap<>();
    //карта лиц, отсутствующих в БД
    private TreeMap<String, Integer> otdelMapOut = new TreeMap<>();
    //список записей из рабочего файла статистики
    private LinkedList<String> statList = new LinkedList<>();
    //рабочая директория указывается без 1-го слэша
    private final String workDir = "workdir/";
    //"перевод строк" в данной ОС (для записи в файл)
    private final String separ = System.getProperty("line.separator"); 
    //кодировка (по умолчанию cp1251)
    private String encodeStr = "cp1251";
    //рабочий файл статистики по умолчанию
    private String nadStat = workDir + "nadstat.txt";
    //файл выгрузки из БД по умолчанию с расширением .txt
    private String baza = workDir + "baza.txt";
    //файл список отделов, определяется пользователем
    private String otdel = workDir + "otdelnad.txt";
    //файл для вывода результатов обработки
    private String result = workDir + "nadresult.txt";
    //файл шаблона для проверки
    private String patternFile = workDir + "nadpattern.txt";
    //модуль автоматической проверки параметров
    private CheckData chData = new CheckData();
    //модуль диалога для выбора параметров пользователя
    private final ModalDialogParamNadzor modalDialogParamNadzor = 
            new ModalDialogParamNadzor();
           
    public NadzorStat(){        
             
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
        askMap.put("Файл статистики", nadStat);
        askMap.put("Файл БАЗА", baza);
        askMap.put("Файл ОТД", otdel);
        askMap.put("Файл результата", result);
        askMap.put("Кодировка", "cp1251");
        askMap.put("Шаблон", patternFile);
        
        txtArea.clear();
        txtArea.appendText(separ + "Выбран режим \"" + mode + "\"" +
            separ + separ);        
        
        if(parameters.equals("userParam")){            
            userDataMap = modalDialogParamNadzor.askParam(askMap);
            askMap.putAll(userDataMap);
            txtArea.appendText(("Выбраны параметры пользователя:" + separ));
            printMap(askMap, txtArea);
            txtArea.appendText(separ + separ +
                    "Для проверки файла нажмите \"Проверить данные\"");
            txtArea.appendText(separ +
            "Для обработки файла нажмите \"Обработать файл\"" + separ);
        }
        else{
            txtArea.appendText("Выбраны параметры по умолчанию: "
            + separ + separ); 
            printMap(askMap, txtArea);
            txtArea.appendText(separ + separ +
                    "Для проверки файла нажмите \"Проверить данные\"" 
            );
            txtArea.appendText(separ + separ +
                "Для обработки файла нажмите \"Обработать файл\"" + separ 
                + separ
            );
        }
        
        //обратный прием параметров после выбора умолчание/пользовательские
        nadStat = askMap.get("Файл статистики");
        baza = askMap.get("Файл БАЗА");
        otdel = askMap.get("Файл ОТД");
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
     * сопоставление с заданными шаблонными значениями
     * @param txtArea
     * @return checkParam
     */
    public boolean checkStat(TextArea txtArea){
        boolean checkParam;
        //txtArea.appendText(patternFile);
        
        checkParam = chData.toCheckData(nadStat, patternFile, encodeStr);
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
     * Метод проверки и обработки основного рабочего файла статистики
     * @param txtArea
     * @return checkParam
     */
    public boolean checkGetStat(TextArea txtArea){
        boolean checkParam;
        checkParam = chData.toCheckData(nadStat, patternFile, encodeStr);
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
        ExtractNadzorData extData = new ExtractNadzorData();
        statList = extData.toExtractData(nadStat, otdel, encodeStr);
        
        //создаем 2 промежуточных списка для тех, кто есть и кого нет в БД
        LinkedList<String> vSove = new LinkedList<>();
        LinkedList<String> nevSove = new LinkedList<>();
        
        //создаем карту для учета результатов по отдельным отделам
        FillMap fillMapIn = new FillMap();
        otdelMapIn = fillMapIn.toFillMap(otdel, encodeStr);
        FillMap fillMapOut = new FillMap();
        otdelMapOut = fillMapOut.toFillMap(otdel, encodeStr);
        
        Set<Map.Entry<String, Integer>> mapSetIn = otdelMapIn.entrySet();
        Set<Map.Entry<String, Integer>> mapSetOut = otdelMapOut.entrySet();
        
        String bazastr; //строка из БД
        String newbazastr; //строка из БД, приведенная к общему виду
        String onlinestr; //строка из стат., приведенная к общему виду
        String otdelStrIn; //название отдела, который есть в БД
        String otdelStrOut; //название отдела, которого нет в БД
        String[] arr; //промежуточный массив для разбивки по колонкам
        
        int countvsove = 0; //счетчик количества внесенных в БД
        int countnevsove = 0; //счетчик количества не внесенных в БД
        
         
        //читаем 1 строку из списка статистики, приводим эту строку к общему
        //виду (для сравнения со строкой из БД): Ф И О ДД.ММ.ГГГГ 
        //(перевернем, т.к.дата в статистике записана наоборот). Строку из БД
        //также приведем к общему виду (табуляции в БД заменим пробелами)
        for(String s: statList){
            arr = s.split("\\|", -1);// парсим по вертикальной черте, со 2-м
            //параметром -1, который допускает последний разделитель!
            onlinestr = arr[2].trim() + " " + arr[3].trim() +
                    " " + arr[4].trim() + " " + arr[5].trim();
            otdelStrIn = arr[1].trim();
            otdelStrOut = arr[1].trim();
                                    
            try(BufferedReader bfreadbaza = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(baza), encodeStr));){
                
                while((bazastr = bfreadbaza.readLine()) != null){
                    newbazastr = bazastr.replace("\t", " ");
                    
                    //если нашли совпадение - пишем в список "есть в БД" и
                    //выходим из цикла, больше в нем не надо проверять                    
                    if(newbazastr.equals(onlinestr)){
                        vSove.add(s);
                        otdelMapIn.put(otdelStrIn, otdelMapIn.get(otdelStrIn) + 1);
                        break;
                    }           
                }
                //дошли до конца списка из БД и не нашли совпадение - 
                //пишем в список "нет в БД", продолжаем поиск с новой
                //строкой из статистики                
                if(bazastr == null){
                    nevSove.add(s);
                    otdelMapOut.put(otdelStrOut, otdelMapOut.get(otdelStrOut) + 1);
                }
            }
            catch(FileNotFoundException fex){
                System.out.println("File not found exception! " + fex);
            }
            catch(IOException iex){
                System.out.println("IO exception " + iex);
            }            
        }
                
        try(BufferedWriter bfwritestat = new BufferedWriter(
                new FileWriter(result))){                     
            
            bfwritestat.write(separ);
            bfwritestat.write("Результат обработки статистики " +
                    "Административный контроль: " + separ + separ);
                                                
            bfwritestat.write("Работники, внесенные в базу:" + separ + separ);
            for(String s: vSove){
                bfwritestat.write(s + separ);
                countvsove++;
            }
            bfwritestat.write(separ);
            for(Map.Entry<String, Integer> me: mapSetIn){
                bfwritestat.write(me.getKey()+ " : " + me.getValue() +
                        separ);
            }
            bfwritestat.write(separ + "Внесено работников в базу: "+
                    countvsove + separ + separ);
                                    
            bfwritestat.write("Работники, не внесенные в базу:" +
                    separ + separ);
            
            for(String s: nevSove){
                bfwritestat.write(s + separ);
                countnevsove++;
            }
            bfwritestat.write(separ);
            for(Map.Entry<String, Integer> me: mapSetOut){
                bfwritestat.write(me.getKey()+ " : " + me.getValue() + separ);
            }
            bfwritestat.write(separ + "Не внесено лиц, данной категории"
                    + " в базу: " + countnevsove + separ);           
        }
        catch(FileNotFoundException fex){
            System.out.println("File not found!\nНе найден один"
                    + " из исходных файлов");
        }
        catch(IOException iex){
            System.out.println("IO exception!\n" +"Ошибка"
                    + " ввода/вывода " + iex);
        }     
        
        txtArea.appendText("Результат обработки: " + separ);
        txtArea.appendText(separ + "Внесены в Базу Данных:" + separ + separ);
        
        for(Map.Entry<String, Integer> me: mapSetIn){
                txtArea.appendText(me.getKey()+ " : " + me.getValue() + separ);
            }
        txtArea.appendText(separ + "Всего внесено лиц, данной категории"
                + " в базу: " + countvsove + separ);
        txtArea.appendText(separ + "Не внесены в Базу Данных:" + separ + separ);
        for(Map.Entry<String, Integer> me: mapSetOut){
                txtArea.appendText(me.getKey()+ " : " + me.getValue() + separ);
            }
        txtArea.appendText(separ + "Всего не внесено лиц, данной категории"
                + " в базу: " + countnevsove);      
    }           
}
