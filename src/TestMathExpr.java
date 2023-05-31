import java.util.*;
import java.math.*;

public class TestMathExpr {
    public static double parse(String str) {
        if (str.charAt(0) == '-') {
            str = "0" + str;
        }
        //find sin cos tan sqrt in the string
        //if there is sin cos tan sqrt in the string we need to calculate it first
        int index_greater1 = Math.max(str.indexOf("sin"), str.indexOf("cos"));
        int index_greater2 = Math.max(str.indexOf("tan"), str.indexOf("sqrt"));
        int index_greater = Math.max(index_greater1, index_greater2);
        if (index_greater != -1) {
            // go further to find the matched ()
            int counter = 0;
            int last_position = 0;
            for (int j = str.indexOf('(', index_greater); j < str.length(); j++) {
                //if '(' we add 1 to the counter
                if (str.charAt(j) == '(') {
                    counter++;
                }
                if (str.charAt(j) == ')') {
                    counter--;
                }
                if (counter == 0) {
                    last_position = j;
                    break;
                }
            }
            String media = "";
            if (str.indexOf("sqrt") != index_greater) {
                media = str.substring(index_greater + 4, last_position);
            } else {
                media = str.substring(index_greater + 5, last_position);
            }
            double median = 0;
            //if ((media.indexOf('+') == -1 && media.indexOf('-') == -1 && media.indexOf('*') == -1 && media.indexOf('/') == -1)){
            try {
                if (str.indexOf("sin") == index_greater) {
                    median = Math.sin(Double.parseDouble(media));
                }
                if (str.indexOf("cos") == index_greater) {
                    median = Math.cos(Double.parseDouble(media));
                }
                if (str.indexOf("tan") == index_greater) {
                    median = Math.tan(Double.parseDouble(media));
                }
                if (str.indexOf("sqrt") == index_greater) {
                    if (Double.parseDouble(media) < 0) {
                        throw new EmptyStackException();
                    }
                    median = Math.sqrt(Double.parseDouble(media));
                }
            } catch (Exception e) {
                if (str.indexOf("sin") == index_greater) {
                    median = Math.sin(parse(media));
                }
                if (str.indexOf("cos") == index_greater) {
                    median = Math.cos(parse(media));
                }
                if (str.indexOf("tan") == index_greater) {
                    median = Math.tan(parse(media));
                }
                if (str.indexOf("sqrt") == index_greater) {
                    if (parse(media) < 0) {
                        throw new EmptyStackException();
                    }
                    median = Math.sqrt(parse(media));
                }
            }
            String front_part = "";
            String back_part = "";
            if (index_greater > 0) {
                front_part = str.substring(0, index_greater);
            }
            if (last_position < str.length()) {
                back_part = str.substring(last_position + 1);
            }
            String together = front_part + '(' + String.valueOf(median) + ')' + back_part;
            //System.out.println(together);
            return parse(together);
        }

        Stack<String> String_ca = new Stack();
        Stack<String> Str_new = new Stack();
        int priority = -3;
        String temp = "";
        //System.out.println(str);
        //boolean space_flag = true;
        while (str.charAt(0) == ' ') {
            str = str.substring(1, str.length());
        }
        while (str.charAt(str.length()-1)==' '){
            str = str.substring(0,str.length()-1);
        }
        for (int i = 0; i < str.length(); i++) {
            //System.out.println(i);
            // check the blank space
            if (str.charAt(i) == ' ' && i != str.length() - 1) {
                //System.out.println("fef");
                int pos = i;
                int front = i - 1;
                //check the str if +-*/ is in the back
                while (str.charAt(pos) == ' ' && pos!=str.length()) {
                    pos++;
                }
                while(str.charAt(front)==' ' && pos!=-1){
                    front--;
                }
                boolean op_front = (front==-1)|| (str.charAt(front) == '+' || str.charAt(front) == '-' || str.charAt(front) == '*' || str.charAt(front) == '/');
                boolean op_back =  (pos==str.length())||(str.charAt(pos) == '+' || str.charAt(pos) == '-' || str.charAt(pos) == '*' || str.charAt(pos) == '/');
                //check the str if +-*/ is in the back
                if ((op_back == false) && (op_front == false)) {
                    //System.out.println(front);
                    //System.out.println(pos);
                    throw new EmptyStackException();
                }
                continue;
            }
            if (str.charAt(i) == '(' || str.charAt(i) == ')') {
                if (temp != "") {
                    Str_new.push(temp);
                    temp = "";
                }
                if (str.charAt(i) == '(') {
                    String_ca.push(String.valueOf(str.charAt(i)));
                    //System.out.println(str.charAt(i));
                } else {
                    while (String_ca.peek().charAt(0) != '(') {
                        Str_new.push(String_ca.pop());
                        //System.out.println(Str_new.peek());
                        //System.out.println(String_ca);
                        //String_ca.pop();
                    }
                    String_ca.pop();

                }
            } else if (str.charAt(i) == '+' || str.charAt(i) == '-' || str.charAt(i) == '*' || str.charAt(i) == '/') {
                //System.out.println(temp);
                if (temp != "") {
                    Str_new.push(temp);
                    temp = "";
                    ;
                }
                if (str.charAt(i) == '-' && (str.charAt(i - 1) == '(')) {
                    temp = temp + str.charAt(i);
                    //System.out.println(temp);
                    continue;
                }
                temp = "";
                if (!String_ca.empty()) {
                    priority = priority(String_ca.peek().charAt(0));
                }
                int new_value = priority(str.charAt(i));
                if (new_value > priority) {
                    //if the op priority is higher than the previous one we push it into the stack
                    priority = new_value;
                    String_ca.push(String.valueOf(str.charAt(i)));
                } else {
                    //if the op priority is greater than the previous one we pop the previous one until it is larger than others and calculate it
                    while (priority >= new_value) {
                        String op = String_ca.pop();
                        Str_new.push(op);
                        if (String_ca.empty()) {
                            priority = -3;
                        } else {
                            priority = priority(String_ca.peek().charAt(0));
                        }
                    }
                    String_ca.push(String.valueOf(str.charAt(i)));
                }
            } else {
                temp = temp + str.charAt(i);
                //System.out.println(str.length());
                //System.out.println("fef");
                //System.out.println(temp);
                if (i == str.length() - 1 && temp.charAt(0) != ' ') {
                    Str_new.push(temp);
                    //System.out.println(temp);
                }
            }
        }
        while (!String_ca.empty()) {
            Str_new.push(String_ca.pop());
        }
        //System.out.println(Str_new);
        //print out the every element in the stack
        //while (!Str_new.empty()) {
        //System.out.println(Str_new.pop());
        //}
        //now we use the stack to calculate the result
        //convert the order of the stack
        Stack<String> Str_new2 = new Stack();
        //System.out.println(Str_new);
        while (Str_new.peek() == " ") {
            Str_new.pop();
        }
        while (!Str_new.empty()) {
            Str_new2.push(Str_new.pop());
        }
        //System.out.println(Str_new2);
        Stack<String> Str_pool = new Stack();
        double result = 0;
        if (String_ca.empty()) {
            result = Double.parseDouble(Str_new2.peek());
        }
        while (!Str_new2.empty()) {
            //System.out.println("hello0");
            String temp2 = Str_new2.pop();
            if (temp2.equals("+") || temp2.equals("-") || temp2.equals("*") || temp2.equals("/")) {
                double back = Double.parseDouble(Str_pool.pop());
                double front = Double.parseDouble(Str_pool.pop());
                result = calculate(front, back, temp2.charAt(0));
                Str_pool.push(String.valueOf(result));
                //System.out.println("hello");
            } else {
                // System.out.println("hello1");
                Str_pool.push(temp2);
            }
        }
        //double result = Double.parseDouble(Str_pool.pop());
        return result;
    }

    //write a function to calculate the Arithmetic
    //the function should be
    private static double calculate(double front, double back, char op) {
        switch (op) {
            case '+':
                return front + back;
            case '-':

                return front - back;
            case '*':
                return front * back;
            case '/':
                if (back == 0) {
                    throw new EmptyStackException();
                }
                return front / back;
            default:
                return 0.0d;
        }
    }


    //write a function to calculate the priority of the operator
    private static int priority(char op) {
        switch (op) {
            case '(':
                return -2;
            case ')':
                return -1;
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -3;
        }
    }


    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            double result = 0;
            try {
                result = parse(input.nextLine());
                System.out.println(String.valueOf(Math.round(result)));
            } catch (Exception e) {
                System.out.println("invalid");
            }
        }
    }
}