package Main;

import Main.Function;

public class Bisection {

    //point A is the inital point.
    //point B is the termianl point.
    //so the interval of the function will be [A , B]
    //point C = (A + B) / 2 
    private double pointA, pointB, pointC;
    private double Fa, Fb, Fc;
    private final Function function;

    //This is only true if Fa or Fb == 0 when we start the method.
    private boolean foundBeforeStart = false;

    public Bisection(String _function, double _pointA, double _pointB) {
        this.function = new Function(_function, 0);
        this.pointA = _pointA;
        this.pointB = _pointB;

        //Find the value of the function at pointA and pointB.
        Fa = function.at(this.pointA);
        Fb = function.at(this.pointB);

        //if fa == 0 or fb == 0 the solution is found before we even start.
        if (Fa == 0) {
            pointC = this.pointA;
            foundBeforeStart = true;
        } else if (Fb == 0) {
            pointC = this.pointB;
            foundBeforeStart = true;
        }

        //check if the sign of Fa is differnt from Fb.
        //if not the program will stop.
        if (Fa * Fb > 0) {
            System.err.println("Wrong input , ths sign of F(a) equlas the sign of F(b) , please restart the program with correct values");
            System.exit(0);
        }
    }

    //if the sign of Fc == the sign of Fa , make PointA = PointC.
    //if the sign of Fc == the sign of Fb , make PointB = PointC.
    private void replacePoints() {
        if (Fc * Fa > 0) {
            pointA = pointC;
        } else {
            pointB = pointC;
        }
    }

    public double findRoot(double errorTolerance) {
        if (foundBeforeStart) {
            System.out.println("Absolute error = 0.0");
            System.out.println("Number of steps = 0");
            return pointC;
        }

        long numberOfSteps = 0;
        double oldC;

        do {
            numberOfSteps++;

            oldC = pointC;
            pointC = (pointA + pointB) / 2.0;

            //if the new pointC equlas the old one stop.
            if (numberOfSteps > 1 && pointC == oldC)
                break;

            Fc = function.at(pointC);

            replacePoints();

            Fa = function.at(pointA);
            Fb = function.at(pointB);
            
                     
        } while (Math.abs(Fc) > errorTolerance);

        System.out.println("Absolute error = " + Math.abs(Fc));
        System.out.println("Number of steps = " + numberOfSteps);
        return pointC;
    }

}
