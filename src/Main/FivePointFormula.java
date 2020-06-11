package Main;
public class FivePointFormula {
    
    public static double findDerivative(String _function, double x, double h) {
        Function function = new Function(_function, 0);
        return (1.0 / (12.0 * h)) * (function.at(x - 2 * h) - 8 * function.at(x - h) + 8 * function.at(x + h) - function.at(x + 2 * h));
    }

}
