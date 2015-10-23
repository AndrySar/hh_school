/**
 * Created by user on 18.10.15.
 *
 *
 * Используется алгоритм Бойера-Мура-Хорспула.
 * Описание Алгоритма в файле README.txt
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class hh_task2 {

    public static void main(String[] args) {

        List<Integer> result = new ArrayList<>();
        List<String> buff = new ArrayList<>();

        try{

            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line;

            while((line = reader.readLine()) != null)
            {
                buff.add(line);
            }
            reader.close();

            // Вызываем метод getFirstEntry для поиска номера позиции первых вхождений строк листа buf
            result = getFirstEntry(buff.toArray(new String[buff.size()]));


        }catch (Exception e)
        {
            System.out.print(e.getMessage());
        }finally {

            try
            {
                FileWriter writer = new FileWriter("output.txt", false);
                for(Integer elem : result)
                    writer.write(elem.toString() + "\n");

                writer.close();
            }catch (IOException e) {
                System.err.print(e.getMessage());
            }
        }
    }


    public static List<Integer> getFirstEntry(String[] templates) {
        String source = "123456789";
        List<Integer> result_arr = new ArrayList<>();
        int sourceLen = source.length();
        int q = 10; // используется для заполнения баховой строки source

        for(String template : templates){

            int templateLen = template.length();

            // таблица смещений
            HashMap<Character, Integer> offsetTable = new HashMap<Character, Integer>();

            //Заполнение таблицы смещений для цифр (0 1 2 .. 9) базовыми значениями
            for (int i = 0; i <= 9; i++) {
                offsetTable.put(Integer.toString(i).charAt(0), templateLen);
            }

            //Переопределение талицы смещений
            for (int i = 0; i < templateLen - 1; i++) {
                offsetTable.put(template.charAt(i), templateLen - i - 1);
            }

            int i = templateLen - 1;
            int j = i;
            int k = i;

            while (j >= 0) {

                // Дополнение исходной строки до необходимой размерности на данном шаге
                while (source.length() <= i) {
                    source += q;
                    q++;
                }

                j = templateLen - 1;
                k = i;
                // Сравнение исходной строки и шаблона, сравнение по последнему символу
                while (j >= 0 && source.charAt(k) == template.charAt(j)) {
                    k--;
                    j--;
                }
                // Получение смещения
                i += offsetTable.get(source.charAt(i));

            }

            sourceLen = source.length();
            if (k >= sourceLen - templateLen) {
                result_arr.add(-1);
            } else {
                result_arr.add(k + 2);
            }
        }
        return  result_arr;
    }
}


