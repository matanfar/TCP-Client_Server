package clientserver;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class MatrixIHandler implements IHandler {
    private Matrix matrix;
    private Index start, end;
    private int mRow, mCol;
    int source, target; //the value of start index and end index
    Graph g;
    int v; //number of nodes in the graph (Index with the value 1)
    private ArrayList<ArrayList<Integer>> adjListArray;
    private Index[] fromValueToIndex; //keeps the index of each value in the matrix
    ArrayList<ArrayList<Index>> allPathsSourceToTarget;

    public MatrixIHandler() {
        this.resetParams();
    }

    /**
     * changes back the matrix from Values to indices
     */
    public void changeNumToBinary(){
        for (int i = 0; i<mRow; i++) {
            for (int j = 0; j < mCol; j++) {
                if (matrix.primitiveMatrix[i][j] == -1)
                    matrix.primitiveMatrix[i][j] = 0;
                else
                    matrix.primitiveMatrix[i][j] = 1;
            }
        }
    }

    /**
     * runs the function addEdge in the class Graph, by adding an edge for each node
     * according the use of getReachables
     * @param g a graph
     * @param matrix a matrix
     */
    public void getAllEdges(Graph g, Matrix matrix){
        ArrayList<Index> adjListArray = new ArrayList<>();
        for(int i=0; i<v; i++){
            adjListArray = (ArrayList)matrix.getReachables(fromValueToIndex[i]);
            for(int j=0; j< adjListArray.size(); j++){
                g.addEdge(i, matrix.primitiveMatrix[adjListArray.get(j).getRow()][adjListArray.get(j).getColumn()]);
            }
        }
    }

    /**
     * resets the input/outputStreams parameters
     */
    private void resetParams() {
        this.matrix = null;
        this.start = null;
        this.end = null;
    }

    /**
     * Changing the matrix from a Binary state to a Valued state
     * (Changing the value of 1 to be 0,1,2,3.....etc, and the value of 0 to be -1)
     * each node in the graph will be represented as a number instead of an index
     * Also, initializing fromValueToIndex array
     */
    public void updateMatrix(){
        int vertexes = 0; //counts how many vertexes we have
        for (int i = 0; i<mRow; i++) {
            for (int j = 0; j < mCol; j++) {
                if (matrix.primitiveMatrix[i][j] == 1)
                    matrix.primitiveMatrix[i][j] = vertexes++;
                else
                    matrix.primitiveMatrix[i][j] = -1;
            }
        }
        this.v = vertexes;
        this.fromValueToIndex = new Index[v];
        for (int i = 0; i<mRow; i++) {
            for (int j = 0; j < mCol; j++) {
                if(matrix.primitiveMatrix[i][j] != -1)
                    fromValueToIndex[matrix.primitiveMatrix[i][j]] = new Index(i, j);
            }
        }
    }

    /**
     * runs the function "findAllPaths", and saves the result
     */
    public void mission4() {
        List<List<Integer>> allPaths = null;
        allPaths = new ArrayList(g.findAllPaths(source, target));
        //from here translates from values to index
        Iterator itr = allPaths.iterator();
        allPathsSourceToTarget = new ArrayList<>();
        while (itr.hasNext()) {
            List<Index> help1 = new ArrayList<>();
            List<Integer> help2;
            help2 = (ArrayList<Integer>) itr.next();
            Iterator itr2 = help2.iterator();
            while (itr2.hasNext())
                help1.add(fromValueToIndex[(Integer) itr2.next()]);
            allPathsSourceToTarget.add((ArrayList<Index>) help1);
        }
    }

    /**
     * looping over all the paths we found at mission2, and sending ths shortest paths among them
     * @return the shortest paths between a given source index to a target
     */
    public ArrayList<ArrayList<Index>> mission2(){
        if(mRow > 50 || mCol > 50){
            System.out.println("The size oof the matrix is too big");
            ArrayList<ArrayList<Index>> result = new ArrayList<>();
            return result;
        }

        ArrayList<ArrayList<Index>> shortestPaths = new ArrayList<>();
        int min = -1;
        for(int i=0; i< allPathsSourceToTarget.size(); i++){
            if(min == -1 || allPathsSourceToTarget.get(i).size()<min)
                min = allPathsSourceToTarget.get(i).size();
        }
        System.out.println("allPaths: " + allPathsSourceToTarget);
        for(int i=0; i< allPathsSourceToTarget.size(); i++){
            if(allPathsSourceToTarget.get(i).size() == min)
                shortestPaths.add(new ArrayList(allPathsSourceToTarget.get(i)));
        }
        return shortestPaths;
    }

    /**
     * executing the relevant function in the class Graph
     * @return list of the connected components in the graph
     */
    public ArrayList<HashSet<Index>> mission1(){
        HashSet<ArrayList<Index>> arr = null;
        arr = new HashSet( g.connectedComponents());
        Iterator itr = arr.iterator();
        ArrayList<HashSet<Index>> result = new ArrayList<>();
        while (itr.hasNext()) {
            HashSet<Index> help1 = new HashSet<>();
            ArrayList<Integer> help2 = new ArrayList<>();
            help2 = (ArrayList<Integer>)itr.next();
            Iterator itr2 = help2.iterator();
            while (itr2.hasNext())
                help1.add(fromValueToIndex[(Integer)itr2.next()]);
            result.add((HashSet<Index>)help1);
        }
        return result;
    }


    /**
     * Checks whether the given index is part of a diagonal
     * @param index
     * @return false if there is a diagonal, else returns true
     */
    public boolean findingDiagonals(Index index){
        int counter1, counter2, counter3, counter4;
        counter1 = counter2 = counter3 = counter4 = 0;
        int extracted = -1;
        try{
            extracted = matrix.primitiveMatrix[index.row+1][index.column];
            if(extracted!=-1) {
                counter3 += extracted;
                counter4 += extracted;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = matrix.primitiveMatrix[index.row][index.column+1];
            if(extracted!=-1) {
                counter2 += extracted;
                counter4 += extracted;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = matrix.primitiveMatrix[index.row-1][index.column];
            if(extracted!=-1) {
                counter1 += extracted;
                counter2 += extracted;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = matrix.primitiveMatrix[index.row][index.column-1];
            if(extracted!=-1) {
                counter1 += extracted;
                counter3 += extracted;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = matrix.primitiveMatrix[index.row-1][index.column-1];
            if(extracted!=-1) {
                counter1 += extracted;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = matrix.primitiveMatrix[index.row+1][index.column-1];
            if(extracted!=-1) {
                counter3 += extracted;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = matrix.primitiveMatrix[index.row+1][index.column+1];
            if(extracted!=-1) {
                counter4 += extracted;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = matrix.primitiveMatrix[index.row-1][index.column+1];
            if(extracted!=-1) {
                counter2 += extracted;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        if((counter1 == 2) || (counter2 == 2) || (counter3 == 2) || (counter4 == 2)){
            return false;
        }
        else
            return true;
    }

    /**
     * Looping over each component in the graph and check if the component is a submarine
     * for each component check:
     * 1) if there is a diagonal in one of the indices in the component (by flag)
     * 2) if the size of the component is 2 or above (by counter)
     * @param arr list of the connected components in the graph (given from mission 1)
     * @return number of legal submarines in the graph
     */
    public int mission3(ArrayList<HashSet<Index>> arr){
        int numOfSubs = 0;
        int counter = 0;
        boolean flag = true;
        Iterator itr = arr.iterator();
        while (itr.hasNext()){
            counter = 0;
            flag = true;
            HashSet<Index> help = new HashSet<>();
            help = (HashSet)itr.next();
            Iterator itr2 = help.iterator();
            while (itr2.hasNext()){
                counter++;
                if(!findingDiagonals((Index)itr2.next()))
                    flag = false;
            }
            if(flag && counter>1)
                numOfSubs++;
        }
        return numOfSubs;
    }


    @Override
    public void handle(InputStream inClient, OutputStream outClient) throws Exception {

        ObjectOutputStream objectOutputStream=new ObjectOutputStream(outClient);
        ObjectInputStream objectInputStream = new ObjectInputStream(inClient);

        this.resetParams();

        boolean dowork = true;
        while (dowork) {
            switch (objectInputStream.readObject().toString()) {
                case "stop":{

                    dowork= false;
                    break;
                }
                case "matrix": {
                    int[][] primitiveMatrix = (int[][]) objectInputStream.readObject();
                    this.matrix = new Matrix(primitiveMatrix);
                    this.matrix.printMatrix();
                    this.mRow = matrix.primitiveMatrix.length;
                    this.mCol = matrix.primitiveMatrix[0].length;
                    updateMatrix();
                    System.out.println("Matrix after update to Valued Matrix");
                    this.matrix.printMatrix();
                    g = new Graph(v);
                    getAllEdges(g, matrix);
                    System.out.println("Count: " + v);
                    break;
                }
                case "start index": {
                    this.start = (Index) objectInputStream.readObject();
                    this.source = matrix.primitiveMatrix[start.row][start.column];
                    break;
                }
                case "end index": {
                    this.end = (Index) objectInputStream.readObject();
                    this.target = matrix.primitiveMatrix[end.row][end.column];
                    break;
                }
                case "TaskOne": {
                    Index indexAdjacentIndices = (Index) objectInputStream.readObject();
                    Collection<Index> adjacentIndices = new ArrayList<>();
                    if (this.matrix != null){
                        adjacentIndices.addAll(this.matrix.getAdjacentIndices(indexAdjacentIndices));
                    }
                    System.out.println("result: " + adjacentIndices);

                    objectOutputStream.writeObject(adjacentIndices);
                    break;
                }
                case "TaskTwo": {
                    Index start = (Index) objectInputStream.readObject();
                    Collection<Index> reachables = new ArrayList<>();
                    if (this.matrix != null){
                        reachables.addAll(this.matrix.getReachables(start));
                    }
                    System.out.println("result: " + reachables);
                    objectOutputStream.writeObject(reachables);
                    break;
                }
                case "Mission1": {
                    ArrayList<HashSet<Index>> result = new ArrayList<>();
                    result = mission1();
                    objectOutputStream.writeObject(result);
                    break;
                }
                case "Mission2": {
                    List<List<Index>> result = new ArrayList<>();
                    result = (ArrayList)mission2();
                    objectOutputStream.writeObject(result);


                    break;
                }
                case "Mission3": {
                    ArrayList<HashSet<Index>> arr = new ArrayList<>();
                    arr = (ArrayList)mission1();
                    changeNumToBinary();
                    int result = mission3(arr);
                    objectOutputStream.writeObject(result);
                    break;
                }
                case "Mission4": {
                    mission4();
                    objectOutputStream.writeObject(allPathsSourceToTarget);
                    break;
                }
            }
        }
    }
}