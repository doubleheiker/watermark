package com.watermark.main.utils.wmoperate;

public class MatrixOperate {
    //矩阵加法 C=A+B
    public static double[][] MatrixAdd(double[][] m1,double[][] m2){
        if(m1==null||m2==null||
                m1.length!=m2.length||
                m1[0].length!=m2[0].length) {
            return null;
        }

        double[][] m = new double[m1.length][m1[0].length];

        for(int i=0;i<m.length;++i){
            for (int j=0;j<m[i].length;++j){
                m[i][j]=m1[i][j]+m2[i][j];
            }
        }

        return m;
    }

    //矩阵减法 C=A-B
    public static double[][] MatrixMinus(double[][] m1, double[][] m2){
        if(m1==null||m2==null||
                m1.length!=m2.length||
                m1[0].length!=m2[0].length) {
            return null;
        }

        double[][] m = new double[m1.length][m1[0].length];

        for(int i=0;i<m.length;++i){
            for (int j=0;j<m[i].length;++j){
                m[i][j]=m1[i][j]-m2[i][j];
            }
        }

        return m;
    }

    //矩阵转置
    public static double[][] MatrixTranspose(double[][] m){
        if (m==null) return null;
        double[][] mt=new double[m[0].length][m.length];
        for(int i=0;i<m.length;++i){
            for (int j=0;j<m[i].length;++j){
                mt[j][i]=m[i][j];
            }
        }
        return mt;
    }

    //矩阵相乘 C=A*B
    public static double[][] MatrixMultiply(double[][] m1,double[][] m2){
        if(m1==null||m2==null||m1[0].length!=m2.length)
            return null;

        double[][] m=new double[m1.length][m2[0].length];
        for(int i=0;i<m1.length;++i){
            for(int j=0;j<m2[0].length;++j){
                for (int k=0;k<m1[i].length;++k){
                    m[i][j]+=m1[i][k]*m2[k][j];
                }
            }
        }

        return m;
    }

    //求矩阵行列式（需为方阵）
    public static double MatrixDet(double[][] m){
        if (m==null||m.length!=m[0].length)
            return 0;

        if (m.length==1)
            return m[0][0];
        else if (m.length==2)
            return Matrix2Det(m);
        else if (m.length==3)
            return Matrix3Det(m);
        else {
            int re=0;
            for (int i=0;i<m.length;++i){
                re+=(((i+1)%2)*2-1)*MatrixDet(CompanionMatrix(m,i,0))*m[i][0];
            }
            return re;
        }
    }

    //求二阶行列式
    public static double Matrix2Det(double[][] m){
        if (m==null||m.length!=2||m[0].length!=2)
            return 0;

        return m[0][0]*m[1][1]-m[1][0]*m[0][1];
    }

    //求三阶行列式
    public static double Matrix3Det(double[][] m){
        if (m==null||m.length!=3||m[0].length!=3)
            return 0;

        double re=0;
        for (int i=0;i<3;++i){
            int temp1=1;
            for(int j=0,k=i;j<3;++j,++k){
                temp1*=m[j][k%3];
            }
            re+=temp1;
            temp1=1;
            for(int j=0,k=i;j<3;++j,--k){
                if (k<0) k+=3;
                temp1*=m[j][k];
            }
            re-=temp1;
        }

        return re;
    }

    //求矩阵的逆（需方阵）
    public static double[][] MatrixInv(double[][] m){
        if (m==null||m.length!=m[0].length)
            return null;

        double A=MatrixDet(m);
        double[][] mi=new double[m.length][m[0].length];
        for(int i=0;i<m.length;++i){
            for (int j=0;j<m[i].length;++j){
                double[][] temp=CompanionMatrix(m,i,j);
                mi[j][i]=(((i+j+1)%2)*2-1)*MatrixDet(temp)/A;
            }
        }

        return mi;
    }

    //求方阵代数余子式
    public static double[][] CompanionMatrix(double[][] m,int x,int y){
        if (m==null||m.length<=x||m[0].length<=y||
                m.length==1||m[0].length==1)
            return null;

        double[][] cm=new double[m.length-1][m[0].length-1];

        int dx=0;
        for(int i=0;i<m.length;++i){
            if(i!=x){
                int dy=0;
                for (int j=0;j<m[i].length;++j){
                    if (j!=y){
                        cm[dx][dy++]=m[i][j];
                    }
                }
                ++dx;
            }
        }
        return cm;
    }

    public static double MaxOfMatrix(double[][] m){
        if(m==null){
            return 0.0;
        }
        double max = m[0][0];

        for(int i=0;i<m.length;++i){
            for(int j=0;j<m[i].length;++j){
                if(max<m[i][j]){
                    max = m[i][j];
                }
            }
        }
        return max;

    }
}
