/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myhungarianalgorithm;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Darya
 */
public class ZerosFinder {
    
    private int[][] resultMatrix;       // Матрица, показывающая итоговое расположение найденных нулей
    private  int N;                      // Размерность обрабатываемой матрицы
    private Set<Integer> checkedRows;   // Обработанные строки
    private Set<Integer> checkedCols;   // Обработанные столбцы
    
    public ZerosFinder(int matrixSize) {
        this.checkedCols = new HashSet<>();
        this.checkedRows = new HashSet<>();
        this.N = matrixSize;
    }
    
    private void clearChecked() {
        this.checkedCols.clear();
        this.checkedRows.clear();
        this.resultMatrix = new int[N][N];
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                this.resultMatrix[i][j] = 0;
            }
        }
    }
    /**
     * Вызывает метод поиска нулей и 
     * возвращает матрицу с расположением найденных нулей.
     * 
     * @param matrix - матрица в которой ищут нули
     * @return матрица с расположением найденных нулей
     * @throws Exception 
     */
    public  int[][] getResult(int matrix[][]) throws Exception {
        clearChecked();
        if (this.find(1, matrix)) {
            return this.resultMatrix;
        } else {
            throw new Exception ("Wrong source matrix - cannot find all needed zeros");
        }
    }
    
    private boolean find(int iteration, int matrix[][]) {
        if (iteration > N) {
            return true;
        }
        
        int currentCol = 0;
        int currentRow = 0;
        while (currentRow < N) {
            currentCol = 0;
            if (!checkedRows.contains(currentRow)) {
                while (currentCol < N) {
                    if (!checkedCols.contains(currentCol)
                            && matrix[currentRow][currentCol] == 0) {
                        checkedCols.add(currentCol);
                        checkedRows.add(currentRow);
                        resultMatrix[currentRow][currentCol] = 1;
                        if (this.find(iteration + 1, matrix)) {
                            return true;
                        }
                        checkedRows.remove(currentRow);
                        checkedCols.remove(currentCol);
                        resultMatrix[currentRow][currentCol] = 0;
                    }
                    currentCol++;
                }
            }
            currentRow++;
        }    
        return false;
    }
}
