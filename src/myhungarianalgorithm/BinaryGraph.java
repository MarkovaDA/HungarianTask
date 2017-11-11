package myhungarianalgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author markova
 */
public class BinaryGraph {
    private final int size;
    private int[][] coverXMatrix, coverYMatrix;
    private boolean[][] historyMatrix;//матрица историй ходов - очищать для каждой итерации
    private int s[];//начало графа
    private int t[];//конец графа
    private List<Edge> independantZeros;

    BinaryGraph(int[][] matrix) {
        this.size = matrix.length;
        this.s = new int[size];
        this.t = new int[size];
        independantZeros = new ArrayList<>();
        Arrays.fill(s,1);
        Arrays.fill(t,1);
        buildGraph(matrix);
    }
       
    //строим процедуру по матрице
    private void buildGraph(int[][] matrix) {
        //связи прямыми стреками (X и Y)
        this.coverXMatrix = new int[size][size];
        this.coverYMatrix = new int[size][size];
        this.historyMatrix = new boolean[size][size];
        
        //первоначальная инициализация нулями
        for(int i=0; i < size; i++) {
            Arrays.fill(coverXMatrix[i], 0);
            Arrays.fill(coverYMatrix[i], 0);
        }
        
        for(int i=0; i < size; i++) {
            for(int j=0; j<size; j++) {
                if (matrix[i][j] == 0) //отмечаем позиции нулей
                    coverXMatrix[i][j] = 1;
            }
        }
    }
    
    public List<Edge> independentZeros() {
        ArrayList<Edge> path;
        for(int i=0; i < size; i++) {
            if (s[i] == 1) {
                path = findWay(i, true, new ArrayList<>());
                //printWay(i, path);
                mergeEdgesToResult(path);
                //обращаем дуги
                reverseEdges(path);
                clearOccupiedMatrix();
            }               
        }
        return independantZeros;
    }
   
    private void clearOccupiedMatrix() {
        historyMatrix = new boolean[size][size];
    }
    
    private boolean IsThereExit(int index) {
        if (t[index] == 1) {
            t[index] = 0;
            return true;
        }
        return false;
    }
    
    private ArrayList<Edge> findWay(int index, boolean isRow,  ArrayList<Edge> path) {
         if (isRow) {         
            int col = findStrictEdge(index);
            //нашли - прямая дуга
            if (col >= 0) {
                path.add(new Edge(index, col, true));
                //System.out.printf("(%d %d)\n", index, col);
                if  (!IsThereExit(col)) 
                    return findWay(col, false,  path);
                else 
                    return path;       
            }
            //не нашли пути
            else {
                //пробуем откатываться
                if (path.isEmpty()) {
                    return path; //откатываться некуда
                }
                Edge lastEdge = path.remove(path.size() - 1); //беру последнее ребро
                historyMatrix[lastEdge.getStart()][lastEdge.getEnd()] = true;
                findWay(lastEdge.getStart(), lastEdge.isDirect(), path);   
            }                
        }
        //работаем с колонкой
        else {
            int row = findBackEdge(index);
            //нашли - прямая дуга
            if (row >=0) {
                path.add(new Edge(row, index, false));
                //System.out.printf("(%d %d)\n", index, row);
                return findWay(row, true, path);
            }
            //не нашли - ищем обратнуюу дугу 
            else {
                //пробуем откатываться
                if (path.isEmpty()) {
                    return path;
                }
                Edge lastEdge = path.remove(path.size() - 1);
                historyMatrix[lastEdge.getStart()][lastEdge.getEnd()] = true;//сюда ходить нельзя
                return findWay(lastEdge.getStart(), lastEdge.isDirect(), path);
            }
        }
        return path;
    }
    
    private void printWay(int row, ArrayList<Edge> path) {
        System.out.printf("\n***ПУТЬ ИЗ ВЕРШИНЫ %d***\n", row);
        path.stream().forEach((edge) -> {
            System.out.println(edge.toString());
        });
    }

    private int findStrictEdge(int index) {       
        for(int i=0; i < size; i++) {
            if (coverXMatrix[index][i] == 1 && !historyMatrix[index][i]) {
                return i;
            }
        }
        return -1;
    }
    
    private int findBackEdge(int index) {
        for(int i=0; i < size; i++) {
            if (coverYMatrix[i][index] == 1 && !historyMatrix[i][index]) {
                return i;
            }
        }
        return -1;
    }
    //обращение ребер
    private void reverseEdges(ArrayList<Edge> path) {
        path.stream().forEach((edge) -> {
            int row = edge.getStart();
            int col = edge.getEnd();
            if (edge.isDirect()) {
                coverXMatrix[row][col] = 0; 
                coverYMatrix[row][col] = 1;
            }     
            else {
                coverYMatrix[row][col] = 0; 
                coverXMatrix[row][col] = 1;
            }
        });       
    }
    
    private void mergeEdgesToResult(List<Edge> path) {
        path.stream().forEach((elem) -> {
            if (!IsThereOppositeEdge(elem))
                independantZeros.add(elem);
        });
    }
    
    private boolean IsThereOppositeEdge(Edge elem) {
       Iterator<Edge> iterator = independantZeros.iterator();
        Edge edge;
        while(iterator.hasNext()) {
            edge = iterator.next();
            if (edge.isDirect() != elem.isDirect() && elem.getStart() == edge.getStart() && elem.getEnd() == edge.getEnd()) {
                iterator.remove();
                return true;
            }         
        }
        return false;
    }
  
    private void printCoverX() {
        System.out.println("***COVERX MATRIX****");
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                System.out.printf("%d ", coverXMatrix[i][j]);
            }
            System.out.println();
        }
    }
    
    private void printCoverY() {
        System.out.println("***COVERY MATRIX***");
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                System.out.printf("%d ", coverYMatrix[i][j]);
            }
            System.out.println();
        }
    }
}
