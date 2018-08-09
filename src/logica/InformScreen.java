package logica;

import javafx.scene.control.TextArea;

/**
 *
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public class InformScreen{

    private final String separ = System.getProperty("line.separator");
    
    public InformScreen(){
              
    }

    /**
     * Вывод на экран краткой инструкции о работе программы
     * 
     * @param encode
     * @param txtArea
     */
    public void getInform(String encode, TextArea txtArea){
        txtArea.setText(separ + 
            "Главный информационный экран программы" + separ +
            separ + "В программе 4 режима обработки статистики: " +
            separ + separ +                         
            "1. Торговые дела." + separ +
            "2. Поиск." + separ +
            "3. Административный контроль." + separ +
            "4. Формальный контроль." + separ + separ +

            "Для начала работы Вам следует выбрать: " + separ + separ + 
            "1. Параметры (по умолчанию или изменить)" + separ +
            "в блоке \"Исходные параметры\"" + separ + separ +
            "2. Кодировку (по умолчанию " + encode + ")" + separ + 
            "в блоке \"Кодировка\"" + separ + separ +            
            "3. Необходимый режим работы в блоке \"Режим работы\"."+ separ +
            separ + "При переходе в определенный режим работы будет " + 
            separ + "представлена информация для данного режима." + separ +
            separ + "Для проверки заданных параметров нажмите " + separ +
            "\"Проверить данные\"." + separ +                
            separ + "Для начала обработки статистики нажмите " + separ +
            "\"Обработать файл\"." + separ +
            separ + "Для очистки экрана нажмите " + separ +
            "\"Очистить экран\"" + separ +
            separ + "Для завершения работы и выхода нажмите " + separ +
            "\"Выход\"" + separ 
        );
    }
}