
public class GaussNewton {
	double[] xData = new double[]{0.038, 0.194, 0.425, 0.626, 1.253, 2.500, 3.740};
    double[] yData = new double[]{0.050, 0.127, 0.094, 0.2122, 0.2729, 0.2665, 0.3317};

    double[][] bMatrix = new double[2][1];//系数β矩阵
    int m = xData.length;
    int n = bMatrix.length;
    int iterations = 7;//迭代次数

    //迭代公式求解，即1中公式⑩
    private void magic(){
    	//β1,β2迭代初值
    	bMatrix[0][0] = 0.9;
    	bMatrix[1][0] = 0.2;
    	
    	//求J矩阵
        for(int k = 0; k < iterations; k++) { 
        	double[][] J = new double[m][n];
        	double[][] JT = new double[n][m];
            for(int i = 0; i < m; i++){
                for(int j = 0; j < n; j++) {
                    J[i][j] = secondDerivative(xData[i], bMatrix[0][0], bMatrix[1][0], j);
                }
            }

            JT = MatrixMath.invert(J);//求转置矩阵JT
            double[][] invertedPart = MatrixMath.mult(JT, J);//矩阵JT与J相乘
            
            //矩阵invertedPart行列式的值：|JT*J|
            double result = MatrixMath.mathDeterminantCalculation(invertedPart);
            
            //求矩阵invertedPart的逆矩阵:(JT*J)^-1
            double[][] reversedPart = MatrixMath.getInverseMatrix(invertedPart, result);

            //求方差r(β)矩阵: ri = yi - f(xi, b)
            double[][] residuals = new double[m][1];
            for(int i = 0; i < m; i++) {
                residuals[i][0] = yData[i] - (bMatrix[0][0] * xData[i]) / (bMatrix[1][0] + xData[i]);
            }
            
            //求矩阵积reversedPart*JT*residuals: (JT*J)^-1*JT*r
            double[][] product = MatrixMath.mult(MatrixMath.mult(reversedPart, JT), residuals);
            
            //迭代公式, 即公式⑩
            double[][] newB = MatrixMath.plus(bMatrix, product);
            bMatrix = newB;        
        }        
        //显示系数值
        System.out.println("b1: " + bMatrix[0][0] + "\nb2: " + bMatrix[1][0]);        
    }

    //2中公式④
    private static double secondDerivative(double x, double b1, double b2, int index){
        switch(index) {
            case 0: return x / (b2 + x);//对系数bi求导
            case 1: return -1 * (b1 * x) / Math.pow((b2+x), 2);//对系数b2求导
        }
        return 0;
    }
    
    public static void main(String[] args) {
        GaussNewton app = new GaussNewton();
        app.magic();        
    }
}
