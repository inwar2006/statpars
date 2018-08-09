
package dialoguser;

import java.util.LinkedHashMap;

/**
 * Интерфейс, описывающий действия для опроса
 * пользователя через карту и возвращения от него
 * значений в такой-же карте.
 * 
 * @version 0.1
 * @author Игорь Пожалуев (Igor Pozhaluev)
 */
public interface AskResponse {
    /**
     *
     * @param progdatamap
     * @return
     */
    LinkedHashMap<String, String> setAsk(LinkedHashMap<String,
            String> progdatamap);    
}