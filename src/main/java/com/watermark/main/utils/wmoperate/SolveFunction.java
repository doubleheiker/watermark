package com.watermark.main.utils.wmoperate;

public class SolveFunction {
    public int N = 0;
    public static int MAX = 5000;
    public static double esp = 0.00001;


    public static double[][] computeJacobiMatrix( double[][] abr, double[] coefficient){//计算方程组的一阶导
        double[][] jacobiMatrix = new double[3][3];
        double a = abr[0][0];
        double b = abr[1][0];
        double r = abr[2][0];
        jacobiMatrix[0][0] = coefficient[1];
        jacobiMatrix[0][1] = 1;
        jacobiMatrix[0][2] = coefficient[2];
        jacobiMatrix[1][0] = 2*a*coefficient[4];
        jacobiMatrix[1][1] = 0;
        jacobiMatrix[1][2] = 2*r*coefficient[5];
        jacobiMatrix[2][0] = coefficient[7];
        jacobiMatrix[2][1] = coefficient[8];
        jacobiMatrix[2][2] = coefficient[9];
        return jacobiMatrix;
    }

    public static  double[][] computeOriginalFun(double[][] abr, double[] coefficient){
        double[][] matrix = new double[3][1];
        double a = abr[0][0];
        double b = abr[1][0];
        double r = abr[2][0];
        matrix[0][0] = a*coefficient[1] + b + r*coefficient[2] - coefficient[0];
        matrix[1][0] = Math.pow(a,2)*coefficient[4] + Math.pow(r,2)*coefficient[5] - coefficient[3];
        matrix[2][0] = a*coefficient[7] + b*coefficient[8] + r*coefficient[9] - coefficient[6];
        return matrix;
    }

    public double[][] computeResult(double[][] abr,double[][] jacobiMatrix, double[][] matrix,double[] coefficient){


        double[][] result;
        result = MatrixOperate.MatrixMinus(abr,MatrixOperate.MatrixMultiply(MatrixOperate.MatrixInv(jacobiMatrix),matrix));
        double[][] abresp = MatrixOperate.MatrixMinus(result,abr);
        double esmax = MatrixOperate.MaxOfMatrix(abresp);
        //esmax = Math.abs(abresp[1][0]);
        //System.out.println(esmax);
        if(N<MAX && esmax>esp) {
            N++;
            jacobiMatrix = computeJacobiMatrix(result, coefficient);
            matrix = computeOriginalFun(result, coefficient);
            result = computeResult(result, jacobiMatrix, matrix, coefficient);
        }
        return result;

    }

    public static double[] inputCoefficient(double avgOfValue,double avgOfLap,double avgOfS,double avgOfST,double varOfvalue,double varOfST,double avgOfVS,double M){
        double[] coeff = new double[10];
        coeff[0] = avgOfValue;
        coeff[1] = avgOfValue;
        coeff[2] = avgOfST;
        coeff[3] = varOfvalue;
        coeff[4] = varOfvalue;
        coeff[5] = varOfST;
        coeff[6] = M;
        coeff[7] = avgOfVS;
        coeff[8] = avgOfS;
        coeff[9] = avgOfLap;
        return coeff;
    }
}
