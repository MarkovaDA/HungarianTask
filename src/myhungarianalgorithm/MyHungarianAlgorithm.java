package myhungarianalgorithm;

import java.util.Arrays;
import java.util.List;


public class MyHungarianAlgorithm {

    private int[][] matrix;//исходная матрица затрат (предполагается квадратной)
    private int[][] source;
    private final int size; //размерность матрицы
    private final CrossOuter crossOuter;
    private final ZerosFinder zerosFinder;//сервис для поиска независимых нулей 
    
    MyHungarianAlgorithm(int[][] matrix) {
        this.matrix = matrix;
        this.size = matrix.length;
        this.zerosFinder = new ZerosFinder(size);
        this.crossOuter = new CrossOuter(size);
        copyMatrix();
    }
    
    void calculate() throws Exception {
        reductionMatrix();
        //кол-во линий, использованных для вычеркивания
        int countLines = crossOuter.crossCount(matrix);
        print(matrix);
        //пока получаем меньшее число линий для вычеркивания, чем нужно
        while(countLines < size) {
            //получаем дополнительные нули
            matrix = crossOuter.doAdditionalZeros(matrix);
            //выполняем редукцию на всякий случай
            reductionMatrix();
            //снова вычеркиваем
            countLines = crossOuter.crossCount(matrix);
            print(matrix);
        }
        //"собираем" независимые нули
        List<Edge> zeros = new BinaryGraph(matrix).independentZeros();
        
        int[][] solution =  getResultMatrix(zeros);
        int min = functionValue(solution);
        print(solution);
        System.out.printf("MIN: %d\n", min);
        
        /*int[][] solution = zerosFinder.getResult(matrix);
        int min = functionValue(solution);
        print(solution);
        System.out.printf("MIN: %d\n", min);*/
        
    }
 
    private int[][] getResultMatrix(List<Edge> path) {
        int[][] result = new int[size][size];
        path.stream().forEach((zero) -> {
            int rowIndex = zero.getStart();
            int colIndex = zero.getEnd();
            result[rowIndex][colIndex] = 1;
        });
        return result;
    }
    void copyMatrix() {
        source = new int[size][size];
        for(int i=0; i < size; i++) {
            source[i] = Arrays.copyOf(matrix[i], size);
        }
    }
    //приведение матрицы (модификация матрицы source)
    void reductionMatrix() {
        double[] minRow = new double [size];
        double[] minCol = new double [size];
        double min;
        for (int i=0; i< size; i++){
            min = matrix[i][0];
            for(int j=0;j< size;j++){
                if(matrix[i][j]< min)
                    min = matrix[i][j];
            }
            minRow[i]= min;
        }
        for (int i=0; i< size; i++){
            for(int j=0;j< size;j++) {
               matrix[i][j]-= minRow[i];
            }
        }    
        for (int j=1; j< size; j++) {
            min = matrix[0][j];
            for(int i=0; i < size; i++){
                if(matrix[i][j] < min)
                    min = matrix[i][j];
            }
            minCol[j]= min;
        }
        for (int j=1; j< size; j++) {
            for(int i=0; i< size; i++) {
               matrix[i][j]-= minCol[j];
            }
        }
   }
    
    void print(int[][] matrix) {
        System.out.println("-------------------");
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                System.out.printf("%d ", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }
    
    int functionValue(int[][] solution) {
        int sum = 0;
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                if (solution[i][j] == 1) {
                    sum += source[i][j];
                }
            }
        }
        return sum;
    }
}
