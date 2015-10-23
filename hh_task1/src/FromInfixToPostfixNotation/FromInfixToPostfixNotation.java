package FromInfixToPostfixNotation;

import AdditionalClass.AdditionalClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by user on 17.10.15.
 *
 * Класс для перевода прямой записи в обратну польскую запись
 */
public  class FromInfixToPostfixNotation {


    /**
     *
     * Метод осуществляет перевод из прямой записи в обратную польскую
     *
     */
    public static String fromInfixToPostfixNotation(String input)
    {
        // Получаем массив элементов выражения
        String[] elems = input.split("\\s");
        Stack<String> OpStack = new Stack<String>(); // стэк в котором будут хранится числа
        List<String> outputList = new ArrayList<String>(); // выходной массив

        for(String elem : elems) // для каждого элемента выполняем операцию
        {
            Integer num;
            // Проверяем является элемент числом или операцией
            if(elem.charAt(0) == 'x' || AdditionalClass.isNumber(elem))
            {
                // если число или x то помещаем в выходной массив
                if(elem.charAt(0) == 'x')
                    outputList.add("1" + elem + "1");
                    else
                    outputList.add(elem);
            }else // если является скобкой или операцией, то нужно обработать
            {
                Integer priorityElem;

                switch (elem)
                {
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                        priorityElem = getprioriti(elem); // получаем приоритет операции

                        while((OpStack.size() > 0) && (priorityElem <= getprioriti(OpStack.peek())))
                        {
                            outputList.add(OpStack.pop());
                        }

                        OpStack.push(elem);
                        break;

                    case "^":
                        priorityElem = getprioriti(elem);
                        while((OpStack.size() > 0) && priorityElem < getprioriti(OpStack.peek()))
                        {
                            outputList.add(OpStack.pop());
                        }

                        OpStack.push(elem);
                        break;

                    case "(":
                        OpStack.push(elem);
                        break;

                    case ")":
                        while((OpStack.size() > 0) && (OpStack.peek().charAt(0) != '('))
                        {
                            outputList.add(OpStack.pop());
                        }

                        if(OpStack.size() > 0)
                        {
                            OpStack.pop();
                        }else
                        {
                            throw new RuntimeException("Несбалансированные скобки");
                        }
                        break;

                    default:
                        break;

                }
            }
        }

        while(OpStack.size() > 0)
        {
            if(OpStack.peek().charAt(0) != '(')
                outputList.add(OpStack.pop());
            else
                throw new RuntimeException("Несбалансированные скобки");
        }

        return join(outputList);
    }

    /**
     *
     * Метод осуществляет перевод из прямой записи в обратную польскую,
     * используется в рекурсивных вызовах
     *
     */
    public static String fromInfixToPostfixNotation(String input, boolean fl)
    {
        String[] elems = input.split("\\s");
        Stack<String> OpStack = new Stack<String>();
        List<String> outputList = new ArrayList<String>();

        for(String elem : elems)
        {
            Integer num;
            // | elem.matches("^-?\\d+$")
            if(elem.split("x").length == 2 || AdditionalClass.isNumber(elem))
            {
                outputList.add(elem);
            }else
            {
                Integer priorityElem;

                switch (elem)
                {
                    case "~":
                    case "+":
                    case "-":
                    case "*":
                        priorityElem = getprioriti(elem);

                        while((OpStack.size() > 0) && (priorityElem <= getprioriti(OpStack.peek())))
                        {
                            outputList.add(OpStack.pop());
                        }

                        OpStack.push(elem);
                        break;

                    case "^":
                        priorityElem = getprioriti(elem);
                        while((OpStack.size() > 0) && priorityElem < getprioriti(OpStack.peek()))
                        {
                            outputList.add(OpStack.pop());
                        }

                        OpStack.push(elem);
                        break;

                    case "(":
                        OpStack.push(elem);
                        break;

                    case ")":
                        while((OpStack.size() > 0) && (OpStack.peek().charAt(0) != '('))
                        {
                            outputList.add(OpStack.pop());
                        }

                        if(OpStack.size() > 0)
                        {
                            OpStack.pop();
                        }else
                        {
                            throw new RuntimeException("Несбалансированные скобки");
                        }
                        break;

                    default:
                        throw new RuntimeException("Неверный формат входного выражения");

                }
            }
        }

        while(OpStack.size() > 0)
        {
            if(OpStack.peek().charAt(0) != '(')
                outputList.add(OpStack.pop());
            else
                throw new RuntimeException("Несбалансированные скобки");
        }

        return join(outputList);
    }

    private static int getprioriti(String operand)
    {
        int priority;
        switch (operand)
        {
            case "~":
            case "+":
            case "-":
                priority = 0;
                break;
            case "*":
            case "/":
                priority = 1;
                break;
            case "^":
                priority = 2;
                break;
            default:
                priority = -1;
                break;
        }
        return priority;
    }

    private static String join(List<String> arr)
    {
        String result = "";
        for(String elem : arr)
        {
            result += elem + " ";
        }
        return  result;
    }

}
