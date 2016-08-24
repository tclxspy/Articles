
public class CurveFitting {
    ///<summary>
    ///最小二乘法拟合二元多次曲线
    ///例如y=ax+b
    ///其中MultiLine将返回a，b两个参数。
    ///a对应MultiLine[1]
    ///b对应MultiLine[0]
    ///</summary>
    ///<param name="arrX">已知点的x坐标集合</param>
    ///<param name="arrY">已知点的y坐标集合</param>
    ///<param name="length">已知点的个数</param>
    ///<param name="dimension">方程的最高次数</param>
    public static double[] MultiLine(double[] arrX, double[] arrY, int length, int dimension) {
    	int n = dimension + 1;                 //dimension次方程需要求 dimension+1个 系数    	      
        double[][] Guass = new double[n][n + 1];      
        for (int i = 0; i < n; i++){ //求矩阵公式①
        	int j;
            for (j = 0; j < n; j++){
            	Guass[i][j] = SumArr(arrX, j + i, length);//公式①等号左边第一个矩阵，即Ax=b中的A
            }
            Guass[i][j] = SumArr(arrX, i, arrY, 1, length);//公式①等号右边的矩阵，即Ax=b中的b
        }        
        
        return ComputGauss(Guass, n);//高斯消元法
    }
    
    //求数组的元素的n次方的和，即矩阵A中的元素
    private static double SumArr(double[] arr, int n, int length) {
    	double s = 0;
        for (int i = 0; i < length; i++){
        	if (arr[i] != 0 || n != 0){
                s = s + Math.pow(arr[i], n);
        	}
            else{
                s = s + 1;
            }
        }
        return s;
    }
    
    //求数组的元素的n次方的和，即矩阵b中的元素
    private static double SumArr(double[] arr1, int n1, double[] arr2, int n2, int length) {
    	double s = 0;
        for (int i = 0; i < length; i++)
        {
            if ((arr1[i] != 0 || n1 != 0) && (arr2[i] != 0 || n2 != 0))
                s = s + Math.pow(arr1[i], n1) * Math.pow(arr2[i], n2);
            else
                s = s + 1;
        }
        return s;        
    }
    
    //高斯消元法解线性方程组
    private static double[] ComputGauss(double[][] Guass, int n) {
        int i, j;
        int k, m;
        double temp;
        double max;
        double s;
        double[] x = new double[n];

        for (i = 0; i < n; i++) {
        	x[i] = 0.0;//初始化
        }

        for (j = 0; j < n; j++) {
            max = 0;
            k = j;
            
            // 从第i行开始，找出第j列中的最大值（i、j值应保持不变）  
            for (i = j; i < n; i++) {
                if (Math.abs(Guass[i][j]) > max){
                    max = Guass[i][j];// 使用交换法找出最大值（绝对值最大）
                    k = i;
                }
            }

            if (k != j) {
            	//将第j行与找到的最大值所在行做交换，保持i值不变（j值记录了本次操作的起始行）
                for (m = j; m < n + 1; m++) {
                    temp = Guass[j][m];
                    Guass[j][m] = Guass[k][m];
                    Guass[k][m] = temp;
                }
            }

            if (max == 0) {
                // "此线性方程为奇异线性方程" 
                return x;
            }

            // 第m列中，第(j+1)行以下（包括第(j+1)行）所有元素都减去Guass[j][m] * s / (Guass[j][j])
            //直到第m列的i+1行以後元素均为零
            for (i = j + 1; i < n; i++) {
                s = Guass[i][j];                
                for (m = j; m < n + 1; m++) {
                    Guass[i][m] = Guass[i][m] - Guass[j][m] * s / (Guass[j][j]);                 
                }
            }
        }//结束for (j=0;j<n;j++)

        //回代过程（见公式4.1.5）
        for (i = n - 1; i >= 0; i--) {
            s = 0;
            for (j = i + 1; j < n; j++) {
                s = s + Guass[i][j] * x[j];
            }
            x[i] = (Guass[i][n] - s) / Guass[i][i];
        }

        return x;
    }//返回值是函数的系数
    
    public static void main(String[] args)  {
        double[] x = {0, 1, 2, 3, 4, 5, 6, 7};
        double[] y = {0, 1, 4, 9, 16, 25, 36, 49};
        double[] a = MultiLine(x, y, 8, 2);
		
        for(int i =0; i <a.length;i++){
            System.out.println(a[i]);
            }
    }  
}
