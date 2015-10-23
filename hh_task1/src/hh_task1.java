import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import FromInfixToPostfixNotation.*;
import AdditionalClass.AdditionalClass;

/**
 * Created by user on 11.10.15.
 *
 *
 */
public class hh_task1 {

    public static void main(String[] args) {

        List<String> result = new ArrayList<>();
        List<String> buff = new ArrayList<>();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line;

            while((line = reader.readLine()) != null)
            {
                buff.add(line);
            }
            reader.close();

            for(String elem : buff)
            {
                String Infix = FromInfixToPostfixNotation.fromInfixToPostfixNotation(elem);
                String Postfix = reversePolishNotationCalcer(Infix);
                result.add(ReductionOfTerms(Postfix));
            }


        }catch (Exception e)
        {
            result.add(e.toString());
        }finally {

            try
            {
                FileWriter writer = new FileWriter("output.txt", false);
                for(String elem : result)
                    writer.write(elem);

                writer.close();
            }catch (IOException e) {
                System.err.print(e.getMessage());
            }
        }

    }

/**
 * Метод reversePolishNotationCalcer выполняет запись и возможный расчет(символьные переменные с символьными переменными
 * числовые с числовыми), выражения записанного в обратной польской натации в прямую запись.
 *
*/
    public static String reversePolishNotationCalcer(String expression)
    {
        String oper1 = "";
        String oper2 = "";
        Stack<String> St = new Stack<String>();
        String[] elems = expression.split("\\s");

        try {
            for (String elem : elems) {
                Integer num;

                if (elem.split("x").length == 2  || AdditionalClass.isNumber(elem)) {
                    St.add(elem);
                } else {

                    switch (elem) {
                        case "+":
                            oper2 = St.pop();
                            oper1 = St.pop();
                            St.push(sum(oper1, oper2));
                            break;
                        case "-":
                            oper2 = St.pop();
                            oper1 = St.pop();
                            St.push(deducation(oper1, oper2));
                            break;
                        case "*":
                            oper1 = St.pop();
                            oper2 = St.pop();
                            St.push(mult(oper1, oper2));
                            break;
                        case "^":
                            oper2 = St.pop();
                            oper1 = St.pop();
                            St.push(Pow(oper1, oper2));
                            break;
                        case "~":
                            oper1 = St.pop();
                            St.push(UnarySubstraction(oper1));
                            break;

                    }


                }

            }
        }catch (Exception e) {
            throw new RuntimeException("Ошибка при разборе строки обратной польской натации");
        }
        return St.pop();

    }

    // Метод возводит символьную переменную в степень
    public static String Pow(String oper1, String oper2)
    {
        String[] elems = oper1.split("x");
        return elems[0] + "x" + oper2;
    }

    // Метод выполняет суммирование двух переменных (или выражений)
    public static String sum(String oper1, String oper2)
    {
        if(AdditionalClass.isNumber(oper1) && AdditionalClass.isNumber(oper2))
        {
            return Double.toString(Double.parseDouble(oper1) + Double.parseDouble(oper2));

        }else
        {
            return oper1 + " + " + oper2;
        }
    }

    // Метод выполняет вычитание переменных (или выражений)
    public static String deducation(String oper1, String oper2)
    {
        String arr_sign = "";
        if(AdditionalClass.isNumber(oper1) && AdditionalClass.isNumber(oper2))
            return Double.toString(Double.parseDouble(oper1) - Double.parseDouble(oper2));
        else
        {
            int sign = 1;
            String[] arr_expression2= oper2.split("\\s");

            // Выполняем разбор одного из выражений на знаки и переменные для корректной записи
            // резкльтирующего выражения
            for(String elem : arr_expression2)
            {
                if(!elem.equals("+") && !elem.equals("-"))
                {
                    if(sign < 0)
                        arr_sign += " + " + elem;
                    else
                        arr_sign += " - " + elem;
                    sign = 1;
                }else
                {
                    switch (elem)
                    {
                        case "+":
                            sign = 1;
                            break;
                        case "-":
                            sign = -1;
                            break;
                        default:
                            throw new RuntimeException("Неверный формат вычитаемых выражений");
                    }
                }
            }
        }
            return oper1 + arr_sign;
    }

    /**
     *  Метод выполняет перемножение двух переменных  oper1 и oper2.
     *  В методе осуществляется проверка принадлежности переменных oper1 и oper2 той или иной
     *  группе переменных (числовые, символьные и выражения).В соответствии с этим выполняются
     *  методы:
     *  1. MultExpressionAndNumber - перемножение выражения(состовной переменной) и переменной(числовой или символьной)
     *  2. MultNumberAndCharactervariables - перемножений числовой и символьной
     *  3. MultExpressionAndExpression - перемножение выражения и выражения
 */
    public static String mult(String oper1, String oper2)
    {
        if(AdditionalClass.isNumber(oper1))
        {
            if(AdditionalClass.isNumber(oper2) )
            return Double.toString(Double.parseDouble(oper1) * Double.parseDouble(oper2));
            else if(cheakExpression(oper2))
            {
                return MultExpressionAndNumber(oper2, oper1);
            }else
            {
                return MultNumberAndCharactervariables(oper1, oper2);
            }

        }else if(cheakExpression(oper1))
        {
            if(AdditionalClass.isNumber(oper2) )
            {
                return MultExpressionAndNumber(oper1, oper2);
            }
            else if(cheakExpression(oper2))
            {
                return MultExpressionAndExpression(oper1, oper2);
            }else
            {
                return MultExpressionAndNumber(oper1, oper2);
            }

        }else
        {
            if(AdditionalClass.isNumber(oper2) )
            {
                return MultNumberAndCharactervariables(oper2, oper1);
            }
            else if(cheakExpression(oper2))
            {
                return  MultExpressionAndNumber(oper2, oper1);
            }else
            {
                return MultCharactervariablesAndCharactervariables(oper1, oper2);
            }
        }

    }


    // Умножение числа на символьную переменую
    // Переменная x приводится к виду AxB где A - коэффициент при x, B - степень.
    public static String MultNumberAndCharactervariables(String source_number, String char_var)
    {
        Double number = 1.0;
        Double factor = 1.0;
        String[] arr_char = char_var.split("x");
        number = Double.parseDouble(source_number);
        factor = Double.parseDouble(arr_char[0]);

        return Double.toString(number * factor) +  "x" + arr_char[1].toString();
    }

    // Перемножение симвальных переменных
    // В данном методе также используется схема AxB
    public static String MultCharactervariablesAndCharactervariables(String char_var1, String char_var2)
    {
        int number = 1;
        int factor = 1;
        String[] arr_char1 = char_var1.split("x");
        String[] arr_char2 = char_var2.split("x");

        return (Double.parseDouble(arr_char1[0]) * Double.parseDouble(arr_char2[0])) + "x"
                + (Double.parseDouble(arr_char1[1]) + Double.parseDouble(arr_char2[1]));
    }

    // Умножение выражения на переменную( символьную или числовую)
    public static String MultExpressionAndNumber(String expression, String number)
    {
        String[] arr_expression = expression.split("\\s");
        String result = "";

        // Выполняем запись выражения в упрощенном виде для дальнейшего расчета
       for(int i = 0; i < arr_expression.length; i++)
       {
           if(i % 2 == 0)
                result += arr_expression[i] + " * " + number;
           else
               result += " " + arr_expression[i] + " ";
       }

        String rex = ProcessPolishNotation(result);

        return rex;
    }


    /**
     *
     * Выполняется перемножение выражений
     *
     */
    public static String MultExpressionAndExpression(String expression1, String expression2)
    {
        int sign = 1;
        List<String> arr_sign = new ArrayList<String>();
        String[] arr_expression2= expression2.split("\\s");

        // Выполняем группировку выражений
        // Пример: (a + b) * (c + d) = a*c + a*d + b*c + b*d
        for(String elem : arr_expression2)
        {
            if(!elem.equals("+") && !elem.equals("-"))
            {
                if(sign < 0)
                    arr_sign.add("~ " + elem);
                else
                arr_sign.add(elem);
                sign = 1;
            }else
            {
                switch (elem)
                {
                    case "+":
                        sign = 1;
                        break;
                    case "-":
                        sign = -1;
                        break;
                    default:
                        throw new RuntimeException("Неверный формат перемножаемых выражений");
                }
            }
        }

        String notation = "";
        String resultExpression = "";

        // Упрощенные выражения высчитываются с помощью обратной польской записи
        // вызывая метод ProcessPolishNotation
        for(String var : arr_sign)
        {
            notation = "( " + expression1 + " ) * ( " + var + " )";
            resultExpression += " + ";
            resultExpression += ProcessPolishNotation(notation);
                /// здесь должен быть код для расчета обратной польской натации
        }
        return  resultExpression;
    }

    /**
     * В методе выполняется разбор выражения из приямой записи в обратную польскую нотаицю(Инфиксная)
     * Далее обратная запись вычисляется методом reversePolishNotationCalcer
     *
     */
    public static String ProcessPolishNotation(String expression)
    {
        String notation = FromInfixToPostfixNotation.fromInfixToPostfixNotation(expression, true);
        String result = reversePolishNotationCalcer(notation);

        return result;

    }


    // Обработка унарного минуса
    public static String UnarySubstraction(String oper)
    {
        if(AdditionalClass.isNumber(oper))
        return Double.toString(Double.valueOf(oper) * -1);
        else
        {
            String[] arr_exp = oper.split("x");
            return Double.toString(Double.valueOf(arr_exp[0]) * -1) + "x" + arr_exp[1];
        }
    }

    /**
     *
     * Данный метод приводит подобные члены
     * уже преобрахованного выражения(отсутствуют скобки и операции произведения)
     *
     */
    public static String ReductionOfTerms(String expression)
    {
        List<Integer> remove_elems = new ArrayList<>();
        List<String> array_token = new ArrayList<>(Arrays.asList(expression.split("\\s")));
        List<Double> arg = new ArrayList<Double>();
        String var = "";
        Integer sing = 1;
        String resultExpression = "";
        int position  = 0;

        //
        for(int j = 0; j < array_token.size(); j++)
        {
            var = array_token.get(j);
            if(AdditionalClass.isNumber(var))
            {
                double result = 0.0;
                int pos = 0;
                for(int i = 0; i < array_token.size(); i++)
                {
                    if(AdditionalClass.isNumber(array_token.get(i)))
                    {
                        result += Double.parseDouble(array_token.get(i)) * sing;
                        remove_elems.add(i);

                    }else if(array_token.get(i).equals("+")) {
                        sing = 1;
                        pos = i;
                    }
                    else if(array_token.get(i).equals("-")) {
                        sing = -1;
                        pos = i;
                    }
                }

                resultExpression += addNumberResultString(resultExpression, result);
                removeElems(array_token, remove_elems);
                remove_elems.clear();

                position = 0;

            }else if (isSymbolVar(var, arg))
            {
                List<Double> current_arg = new ArrayList<Double>();
                double result = 0.0;
                int pos = 0;

                for (int i = 0; i < array_token.size(); i++)
                {
                    if(isSymbolVar(array_token.get(i), current_arg))
                    {
                        if(arg.get(1).equals(current_arg.get(1)))
                        {
                            result += current_arg.get(0) * sing;
                            remove_elems.add(i);
                        }
                    }else if(array_token.get(i).equals("+")) {
                        sing = 1;
                        pos = i;
                    }
                    else if(array_token.get(i).equals("-")) {
                        sing = -1;
                        pos = i;
                    }

                    current_arg.clear();
                }

                List<Double> result_arg = new ArrayList<Double>();
                result_arg.add(result);
                result_arg.add(arg.get(1));
                resultExpression += addSymbolVarResultString(resultExpression, result_arg);
                result_arg.clear();

                removeElems(array_token, remove_elems);
                remove_elems.clear();

                position = 0;

            }else if(array_token.get(j).equals("+")) {
                sing = 1;
                position ++;
            }
            else if(array_token.get(j).equals("-")) {
                sing = -1;
                position ++;
            }
            else if(array_token.get(j).equals("")) {
            }
            else
                throw new RuntimeException("Недопустимый токен в выражении");


            remove_elems.clear();
            arg.clear();
        }
        return resultExpression;
    }

    public static boolean isSymbolVar(String oper, List<Double> arg)
    {
        String[] arr_char = oper.split("x");
        if(arr_char.length ==2)
        {
            if(AdditionalClass.isNumber(arr_char[0]) && AdditionalClass.isNumber(arr_char[1]))
            {
                arg.add(Double.parseDouble(arr_char[0]));
                arg.add(Double.parseDouble(arr_char[1]));
                return true;
            }
        }

        return false;
    }


    // Метод проверяет, явдяется ли oper выражением.
    public static boolean cheakExpression(String oper)
    {
        String[] elems = oper.split("\\s");
        if(elems.length == 1)
            return false;
        else
            return true;
    }


    // Метод формирует строку из числовой переменной(с учетом знака), которую далее добавляется к результирующей строке
    public static String addNumberResultString(String exp, Double number)
    {
        if(exp == "") {
                if( number > 0)
                    return  Double.toString(number);
                else if( number < 0)
                    return " - " + Double.toString(Math.abs(number));
        }else
        {
                if( number > 0)
                    return  " + " + Double.toString(number);
                else if (number < 0)
                    return " - " + Double.toString(Math.abs(number));
        }
        return  "";
    }

    //Метод формирует строку из символьной переменной(с учетом знака, коэффициента, степени),
    // которую далее добавляется к результирующей строке
    //
    public static String addSymbolVarResultString(String exp, List<Double> arg)
    {
        if(exp == "") {
            if( arg.get(0) > 0)
                return  Double.toString(arg.get(0)) + "x^" + Double.toString(arg.get(1));
            else if( arg.get(0) < 0)
                return " - " + Double.toString(Math.abs(arg.get(0))) + "x^" + Double.toString(arg.get(1));
        }else
        {
            if( arg.get(0) > 0)
                return  " + " + Double.toString(arg.get(0)) + "x^" + Double.toString(arg.get(1));
            else if (arg.get(0) < 0)
                return " - " + Double.toString(Math.abs(arg.get(0))) + "x^" + Double.toString(arg.get(1));
        }
        return "";
    }

    // Метод обнуляет использованные переменные
    public static void removeElems(List<String> array_token, List<Integer> remove_list)
    {
       for(Integer var : remove_list)
       {
           try {
               array_token.set(var.intValue(), "0");
           }catch (Exception e){};
       }
    }

}
