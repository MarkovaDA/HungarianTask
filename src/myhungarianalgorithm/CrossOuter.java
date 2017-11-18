package myhungarianalgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class CrossOuter {

    int size;
    private int[] rowQuantity,colQuantity;
    private boolean occupied[];
    private boolean[] rowsWithIndependentZero;
    private boolean[][] covers;
    List<Integer>checkedRows = new ArrayList<>();
    List<Integer>checkedCols = new ArrayList<>();
    
    CrossOuter(int size) {
        this.size = size;
        rowQuantity = new int[size];
        colQuantity = new int[size];
        occupied = new boolean[size];
        rowsWithIndependentZero = new boolean[size];
        covers = new boolean[size][size];
        Arrays.fill(occupied, false);
        Arrays.fill(rowsWithIndependentZero, false);
    }
        
    private void clear() {
        //очистка рабочих переменных
        covers = new boolean[size][size];
        Arrays.fill(occupied, false);
        Arrays.fill(rowsWithIndependentZero, false);
    }
    //поиск макс.числа независимых нулей
    private void findIndependantZeros(/*int row,*/ int[][] matrix) {
        /*if(row == size) 
            return;
        for(int col=0; col < matrix.length;col++) {
            if (matrix[row][col] == 0 && !occupied[col]) {
                occupied[col] = true;
                rowsWithIndependentZero[row] = true;//строка, в которой нез. нуль нашли
                covers[row][col] = true;//координаты независимого нуля
                break;
                //occupied[col] = false;
                //covers[row][col] = false;      
            }
        }
        findMaxZeros(row + 1, matrix);*/
        List<Edge> zeros = new BinaryGraph(matrix).independentZeros();
        zeros.stream().forEach((zero) -> {
            int rowIndex = zero.getStart();
            int colIndex = zero.getEnd();
            rowsWithIndependentZero[rowIndex] = true;
            covers[rowIndex][colIndex] = true;
        });
        /*сovers - это матрица координат независимых нулей*/
    }
    
    void printCovers() {
        for(int i=0; i< size; i++) {
            for(int j=0; j< size; j++) {
                System.out.printf("%s ", Boolean.toString(covers[i][j]));
            }
            System.out.println();
        }               
    }
    
    /*
    *Возвращаем кол-во вычеркнутых строк
    */
    public int crossCount(int[][] matrix) {
        //countZerosInRowsCols(); - было
        //ошибка: если нет строк с независимыми
        this.clear();
        this.findIndependantZeros(matrix);//добавлено 
        
        checkedRows = new ArrayList<>();
        checkedCols = new ArrayList<>();
        
        for (int row = 0; row < matrix.length; row++) {
            if (!rowsWithIndependentZero[row])
                processRow(row, matrix);
        }
        
        return checkedCols.size() + size - checkedRows.size();
    }
    
    private boolean checkRow(int row, int[][] matrix) {
        //нужно найти строку без независимых нулей
        for (int i = 0; i < size; i++) {
            if (matrix[row][i] == 0 && colQuantity[i] < 2) {
                return false;
            }
        }
        return (rowQuantity[row] > 0);
    }
    
    private void processRow(int row, int[][] matrix) {
        if (checkedRows.contains(row)) {
            return;
        }
        checkedRows.add(row);
        System.out.printf("row: %d\n", row);
        for (int i = 0; i < size; i++){
            //в строке ищем нулевые элементы и переходим к обработке соотв.колонок
            if (matrix[row][i] == 0 && !checkedCols.contains(i)) {
                System.out.printf("col: %d\n", i);
                processCol(i, matrix);
                //return;
            }
        }
    }
    
    private void processCol(int col, int[][] matrix) {
        if (checkedCols.contains(col)) {
            return;
        }
        checkedCols.add(col);
        for (int i = 0; i < size; i++){
            //в колонке ищем незаисимые элементы и переходим к соотв.обработке строк
            if (covers[i][col] && !checkedRows.contains(i))
                 processRow(i, matrix);
        }
    }
    
    //создание дополнительных нулей
    public int[][] doAdditionalZeros(int[][] matrix) {
        double min = minFromUncrossed(matrix);
        for(int i=0; i < size; i++)
            for(int j=0; j < size; j++) {
                //к дважды вычеркнутым прибавить
                if (!checkedRows.contains(i) && checkedCols.contains(j))
                   matrix[i][j]+=min;
                else 
                    //из ни разу не вычеркнутых - отнять
                    if ((checkedRows.contains(i)) && (!checkedCols.contains(j)))
                       matrix[i][j]-= min;
           }
        return matrix;
    }
    
    private int minFromUncrossed(int[][] matrix) {
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < size; i++) {
            for(int j= 0; j < size;j++) {
                //невычеркнутый элемент
                if (checkedRows.contains(i) && !checkedCols.contains(j)) {
                    min = (matrix[i][j] < min) ? matrix[i][j]: min; 
                }
            }
        }
        return min;
    }
}
