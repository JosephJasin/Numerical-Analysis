//Made by Jsoeph Jasin 201910779
package Main;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Convert a String to a Function , so you can get the value f(x) at any point.
//How this class Work :
//Step 1 - convert all charaters to lower case , so x and X , sin and Sin , COS , cos ..., will be the same.
//Step 2 - remove any spaces before ,after or between the characters.
//Step 3 - validate the function , so if the function have any variable other than x ,
//         or if the number of opened and closed brackets are not the same , the program will stop.
//Step 4 - reWrite the function in a specific way.
//Step 5 - replace x with the value of x.
//Step 6 - evalute the expressions.
public class Function {

    //The string that will be converted.
    public final String function;

    //Number of sginficat digit
    private final int digits;

    public Function(String function, int digits) {
        if (digits != 0) {
            this.digits = digits;
        } else {
            this.digits = 20;
        }

        function = function.toLowerCase();

        function = removeSpaces(function);

        validate(function);

        function = reWrite(function);

        this.function = function;

        //TEMP:
        //System.err.println("Func = " + function);
    }

    /**
     * Step 2 - remove any space(s) before,after or between the characters of
     * the string.
     */
    private static String removeSpaces(String function) {
        // \\s : Matches a single white space character. This includes space, tab...
        // + : Matches the preceding character 1 or more times.
        return function.replaceAll("\\s+", "");
    }

    /**
     * Step 3 - validate the function , so if the function have any variable
     * other than x , or if the number of opened and closed brackets are not the
     * same , the program will stop.
     */
    private static void validate(String string) {

        //The number of open , close brackets.
        int openBrackets = 0, closeBrackets = 0;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '(') {
                openBrackets++;
            } else if (string.charAt(i) == ')') {
                closeBrackets++;
            }
        }

        if (openBrackets != closeBrackets) {
            System.err.println("Sorry this function is not valid :(\nThe number of opened brackets must equal the number of closed ,please restart the program.");
            System.exit(0);
        }

        //Temporarily remove sin,cos,tan,log,ln,e,pi
        //to test if the function have any variable other than x
        string = string.replaceAll("sin|cos|tan|log|ln|e|pi", "");

        if (Pattern.compile("[a-wyz]").matcher(string).find()) {
            System.err.println("Sorry this function is not valid :(\nOnly the x variable is allowed ,please restart the program.");
            System.exit(0);
        }

    }

    /**
     * Step 4 - reWrite the function : examples: 4x will become 4*x , 4sin will
     * become 4*sin , --5 will become 5 , (1 + 5 )(x + 2) will become (1 + 5)*(x
     * + 2)...
     */
    private String reWrite(String function) {

        String reWritten = "";

        //replace pi , e with 3.14... , 2.71 ... .
        //replace -- with +
        //reolace more than one + with one +
        function = function.replaceAll("pi", toString(Math.PI)).replaceAll("e", toString(Math.E)).replaceAll("--", "+").replaceAll("\\++", "+").replaceAll("\\-\\+", "-").replaceAll("\\+\\-", "-");

        //Adding the * character to the missing places such that : 
        //between 10 and x in 10x so it will become 10*x ...
        for (int i = 0; i < function.length(); i++) {
            char c = function.charAt(i);

            //first character
            if (i == 0) {
                reWritten += c;
            } //number x , ) x , x x
            else if (c == 'x' && (Character.isDigit(function.charAt(i - 1)) || function.charAt(i - 1) == ')' || function.charAt(i - 1) == 'x')) {
                reWritten += "*x";
            } // x number , x ( , x log , x sin ...
            else if ((Character.isDigit(c) || c == '(' || c == 's' || c == 'c' || c == 't' || c == 'l') && function.charAt(i - 1) == 'x') {
                reWritten += "*" + c;
            } // ) anything  , number anything
            else if ((c == '(' || c == 's' || c == 'c' || c == 't' || c == 'l') && (function.charAt(i - 1) == ')' || Character.isDigit(function.charAt(i - 1)))) {
                reWritten += "*" + c;
            } //  ) number
            else if (Character.isDigit(c) && function.charAt(i - 1) == ')') {
                reWritten += "*" + c;
            } else {
                reWritten += c;
            }

        }

        //replace the ^ character with $ and add breackets , in any string that consist of a : 
        //character x followed by ^ followed by or more numbers 
        //exmaple : x^2 will become x$2
        //The different between ^ and $ is that 
        //^ takes the number without it's sign and perform the power operation
        //example : -1^2 = -1
        //but $ takes the number with it sign and perform the power operation
        //example : (-1)^2 will become (-1)$2 = 1
        Pattern p1 = Pattern.compile("x\\^\\d+");
        Matcher m1 = p1.matcher(reWritten);
        while ((m1.find())) {
            String temp = reWritten.substring(m1.start(), m1.end()).replace("^", "$");
            reWritten = reWritten.replace(reWritten.substring(m1.start(), m1.end()), "(" + temp + ")");
            m1 = p1.matcher(reWritten);
        }

        //replace the ^ character with $ and add breackets ,in any string that consist of a : 
        //character ( folowed by one or more characters followed by ) followed by ^ followed by or more numbers 
        Pattern p2 = Pattern.compile("\\(.+?\\)\\^\\d+");
        Matcher m2 = p2.matcher(reWritten);

        //replace the ^ character with $ and add breackets , in any string that consist of a : 
        //character x followed by ^ followed by (...)
        while (m2.find()) {
            String temp = reWritten.substring(m2.start(), m2.end()).replace("^", "$");
            reWritten = reWritten.replace(reWritten.substring(m2.start(), m2.end()), "(" + temp + ")");
            m2 = p2.matcher(reWritten);
        }

        Pattern p3 = Pattern.compile("x\\^\\(.+?\\)");
        Matcher m3 = p3.matcher(reWritten);
        while ((m3.find())) {
            String temp = reWritten.substring(m3.start(), m3.end()).replace("^", "$");
            reWritten = reWritten.replace(reWritten.substring(m3.start(), m3.end()), "(" + temp + ")");
            m3 = p3.matcher(reWritten);

        }

        //replace the ^ character with $ and add breackets ,in any string that consist of a : 
        //character ( folowed by one or more characters followed by ) followed by ^ followed by (...) 
        Pattern p4 = Pattern.compile("\\(.+?\\)\\^\\(.+?\\)");
        Matcher m4 = p4.matcher(reWritten);
        while ((m4.find())) {
            String temp = reWritten.substring(m4.start(), m4.end()).replace("^", "$");
            reWritten = reWritten.replace(reWritten.substring(m4.start(), m4.end()), "(" + temp + ")");
            m4 = p4.matcher(reWritten);
        }

        return reWritten;
    }

    /**
     * Test if the giving function (or part of a function) is simple. simple :
     * means that it's only contains one number or -number without any other
     * operation
     */
    private boolean isSimple(String Fx) {
        try {
            Double.parseDouble(Fx);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private int findNumberOfMatches(String regex, String str) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        int result = 0;

        while (matcher.find()) {
            result++;
        }

        return result;
    }

    /**
     * Perform Multiplication , Addition , Subtraction , Division or power.
     */
    private String perform(char opertor, String Fx) {
        //TEMP:
        //System.err.println("op : " + opertor);

        while (Fx.contains(String.valueOf(opertor))) {
            //after each iteartion the old part will be replaced with the new one
            //example:
            //old = "5+3"
            //new = "8"
            String Old, New;

            String before = "", after = "";

            //Finding the first operand (the operand before the operator)
            loop1:
            for (int i = Fx.lastIndexOf(opertor) - 1; i >= 0; i--) {

                //if the - or + or - or / or ^ ... sign is found it's mean that we find the first operand 
                //because loop1 move from right to left.
                switch (Fx.charAt(i)) {

                    case '-':
                        if (opertor != '^') {
                            before = Fx.charAt(i) + before;
                        }

                    case '+':
                    case '*':
                    case '/':
                    case '^':
                    case 'n':
                    case 's':
                    case 'g':
                    case '$':
                        break loop1;
                }
                before = Fx.charAt(i) + before;
            }

            if (before.equals("-") || before.equals("+")) {
                before = "";
            }

            //Finding the second operand (the operand after the operator)
            loop2:
            for (int i = Fx.lastIndexOf(opertor) + 1; i < Fx.length(); i++) {

                //the first operand must begin with + or - or a number.
                if (after.equals("") && (Fx.charAt(i) == '+' || Fx.charAt(i) == '-')) {
                    after += Fx.charAt(i);
                    continue;
                }

                //if the - or + or - or / or ^ ... sign is found it's mean that we find the second operand 
                //because loop2 move from left to right.
                switch (Fx.charAt(i)) {
                    case '-':
                    case '+':
                    case '*':
                    case '/':
                    case '^':
                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case '$':
                        break loop2;
                }

                after += Fx.charAt(i);
            }

            if (after.equals("-") || after.equals("+")) {
                after = "";
            }

            //the 2 operands must not be empty.
            //only for the unary operator + , it's allowed
            if (opertor != '+' && opertor != '-') {
                if (before.equals("") || after.equals("")) {
                    System.err.println("The Function is not correct (wrong use of " + opertor + " )");
                    System.exit(0);
                }
            } else {
                if (after.equals("")) {
                    System.err.println("The Function is not correct (wrong use of " + opertor + " )");
                    System.exit(0);
                }
            }

            Old = before + opertor + after;
            //TEMP:
            //System.err.println("b : " + before + ",  a : " + after);
            //System.err.println("f = " + Fx);

            switch (opertor) {
                case '*':
                    New = toString(Double.parseDouble(before) * Double.parseDouble(after));
                    break;
                case '/':
                    New = toString(Double.parseDouble(before) / Double.parseDouble(after));
                    break;
                case '^':
                    New = toString(Math.pow(Double.parseDouble(before), Double.parseDouble(after)));
                    break;

                case '$':
                    New = toString(Math.pow(Double.parseDouble(before), Double.parseDouble(after)));
                    break;

                case '+':
                    if (!before.equals("")) {
                        New = toString(Double.parseDouble(before) + Double.parseDouble(after));
                    } else {
                        New = toString(Double.parseDouble(after));
                    }
                    break;

                case '-':
                    if (!before.equals("")) {
                        New = toString(Double.parseDouble(before) - Double.parseDouble(after));
                    } else {
                        New = toString(-Double.parseDouble(after));
                    }

                    break;

                default:
                    System.err.println("Wrong operator : " + opertor);
                    System.exit(0);
                    New = "Dead code , just added to remove a compile time error";

            }

            New = "+" + New;

            Fx = Fx.replace(Old, New).replaceAll("--", "+").replaceAll("\\++", "+").replaceAll("\\-\\+", "-").replaceAll("\\+\\-", "-");

            if (Old.equals(New)) {
                break;
            }

            if (opertor == '+' || opertor == '-') {
                if (findNumberOfMatches("\\" + opertor, Fx) == 1 && before.equals("")) {
                    break;
                }
            }

        }

        return Fx.replaceAll("--", "+").replaceAll("\\++", "+").replaceAll("\\-\\+", "-").replaceAll("\\+\\-", "-");

    }

    /**
     * Perform sin , cos , tan , log , ln operations only.
     */
    private String performSpecialOperations(String Fx) {

        String operation;
        int lastestIndex;

        while (Fx.contains("sin") || Fx.contains("cos") || Fx.contains("tan") || Fx.contains("log") || Fx.contains("ln")) {

            String Old = "", New = "", after = "";

            operation = "sin";
            lastestIndex = Fx.lastIndexOf("sin");

            if (Fx.lastIndexOf("cos") > lastestIndex) {
                operation = "cos";
                lastestIndex = Fx.lastIndexOf("cos");
            }

            if (Fx.lastIndexOf("tan") > lastestIndex) {
                operation = "tan";
                lastestIndex = Fx.lastIndexOf("tan");
            }

            if (Fx.lastIndexOf("log") > lastestIndex) {
                operation = "log";
                lastestIndex = Fx.lastIndexOf("log");
            }

            if (Fx.lastIndexOf("ln") > lastestIndex) {
                operation = "ln";
                lastestIndex = Fx.lastIndexOf("ln");
            }

            //TEMP:
            //System.err.println("op : " + operation);
            //if the opeatration is ln , the unique operand will be 2 steps after it ,
            //otherwise(sin , cos ,...) it will be 3 steps.
            loop:
            for (int i = lastestIndex + (operation.equals("ln") ? 2 : 3); i < Fx.length(); i++) {

                if (after.equals("") && (Fx.charAt(i) == '+' || Fx.charAt(i) == '-')) {
                    after = after + Fx.charAt(i);
                    continue;
                }

                switch (Fx.charAt(i)) {
                    case '+':
                    case '*':
                    case '/':
                    case '-':
                        break loop;
                }

                after = after + Fx.charAt(i);
            }

            Old = operation + after;

            if (operation.equalsIgnoreCase("sin")) {
                New = toString(Math.sin(Double.parseDouble(after)));
            } else if (operation.equalsIgnoreCase("cos")) {
                New = toString(Math.cos(Double.parseDouble(after)));
            } else if (operation.equalsIgnoreCase("tan")) {
                New = toString(Math.tan(Double.parseDouble(after)));
            } else if (operation.equalsIgnoreCase("log")) {
                New = toString(Math.log10(Double.parseDouble(after)));
            } else if (operation.equalsIgnoreCase("ln")) {
                New = toString(Math.log(Double.parseDouble(after)));
            } else {
                System.err.println("Wrong operation : " + operation);
                System.exit(0);
            }

            Fx = Fx.replace(Old, New).replaceAll("--", "+").replaceAll("\\++", "+").replaceAll("\\-\\+", "-").replaceAll("\\+\\-", "-");

        }

        return Fx.replaceAll("--", "+").replaceAll("\\++", "+").replaceAll("\\-\\+", "-").replaceAll("\\+\\-", "-");

    }

    //Step 5 
    public Double at(double x) {
        String Fx = function.replaceAll("x", toString(x));

        //After we replace x with a value we must reWrite it againg to prevent some bugs.
        Fx = reWrite(Fx);

        return Double.parseDouble(evalute(Fx));
    }

    //Step 6
    private String evalute(String Fx) {

        if (Fx.matches("^\\-d+")) {
            return Fx;
        }

        String Old, New;

        //perform ( )
        while (Fx.contains("(")) {

            //find the last occurence of (
            //if we find the first occurence it will cause some problems 
            //example : ((2)) will cause an index of bounding
            Old = Fx.substring(Fx.lastIndexOf('(') + 1, Fx.indexOf(')', Fx.lastIndexOf('(')));

            New = evalute(Old);

            Fx = Fx.replace("(" + Old + ")", New);

        }

        Fx = performSpecialOperations(Fx);
        Fx = perform('$', Fx);

        Fx = perform('^', Fx);

        Fx = perform('*', Fx);
        Fx = perform('/', Fx);
        Fx = perform('+', Fx);
        Fx = perform('-', Fx);

        if (!isSimple(Fx))
            Fx = evalute(Fx);

        return Fx;
    }

    public static String repeatString(String str, int times) {
        String result = "";

        for (int i = 0; i < Math.abs(times); i++) {
            result += str;
        }

        return result;
    }

    //This function is used insted of String.valueOf(x)
    //becasue when we have a large or small number such that : 0.000001 
    //and we convert it to string it will become 1E-5
    //and this will casuse an infinte loop.
    public String toString(Double x) {
        DecimalFormat decimalFormat = new DecimalFormat("#." + repeatString("#", digits - 1));
        String str = decimalFormat.format(x);
        return str;
    }

}
