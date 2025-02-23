import java.io.*;
import java.util.*;

public class Solver {
    public static int N, M, P;
    public static String boardType;
    public static List<char[][]> boardBlocks = new ArrayList<>();
    public static long executionTime;
    public static int steps;
    public static Board board;
    private static boolean solutionFound = false;


    public static void solvePuzzle(){
        long initialTime = System.nanoTime();
        backtrack(0);
        long endTime = System.nanoTime();

        executionTime = (endTime - initialTime)/1000000;
        if (solutionFound) {
            System.out.println("Solusi ditemukan.\n");
            board.printBoard();
            System.out.println("Waktu eksekusi: " + Solver.executionTime + " ms.\n");
            System.out.println("Percobaan: " + Solver.steps + ".");
        } else {
            System.out.println("Tidak ada solusi yang ditemukan.\n");
            System.out.println("Waktu eksekusi: " + Solver.executionTime + " ms.\n");
            System.out.println("Percobaan: " + steps + ".");
        }
    }

    private static void backtrack(int blockIndex){
        if (blockIndex == boardBlocks.size()){
            if (board.isFull()){
                solutionFound = true;
            }
            return;
        } else {
            char[][] block = boardBlocks.get(blockIndex);
            char blockName = (char) ('A' + blockIndex);
        
            for (int y = 0; y < N; ++y){
                for (int x = 0; x < M; ++x){
                    List<char[][]> allTransformations = getAllTransformations(block);
                    for (char[][] transformedBlock : allTransformations){
                        steps++;
                        if (board.ableToPlaceBlock(transformedBlock, y, x)){
                            board.placeBlock(transformedBlock, y, x, blockName);
    
                            backtrack(blockIndex + 1);
    
                            if (solutionFound) return;
    
                            board.removeBlock(transformedBlock, y, x);
                        }
                    }
                }
            }
        }
    }    

    private static List<char[][]> getAllTransformations(char[][] block){
        List<char[][]> transformedBlocks = new ArrayList<>();
        transformedBlocks.add(block);

        char[][] rotatedBlock = block;
        for (int i = 0; i < 3; ++i){
            rotatedBlock = rotate(rotatedBlock);
            if (!isFoundInList(transformedBlocks, rotatedBlock)){
                transformedBlocks.add(rotatedBlock);
            }
        }

        char[][] mirroredBlock = mirror(block);
        if (!isFoundInList(transformedBlocks, mirroredBlock)){
            transformedBlocks.add(mirroredBlock);
        }
        for (int i = 0; i < 3; ++i){
            mirroredBlock = rotate(mirroredBlock);
            if (!isFoundInList(transformedBlocks, mirroredBlock)){
                transformedBlocks.add(mirroredBlock);
            }
        }

        return transformedBlocks;
    }

    private static boolean isFoundInList(List<char[][]> list, char[][] block){
        for (char[][] blockInList : list){
            if (isEqual(blockInList, block)){
                return true;
            }
        }
        return false;
    }

    private static boolean isEqual(char[][] block1, char[][] block2){
        if ((block1.length != block2.length) || (block1[0].length != block2[0].length)){
            return false;
        }

        for (int i = 0; i < block1.length; i++){
            if (!Arrays.equals(block1[i], block2[i])){
                return false;
            }
        }

        return true;
    }

    private static char[][] rotate(char[][] block){
        int rows = block.length;
        int cols = block[0].length;
        char[][] rotated = new char[cols][rows];

        for (int i = 0; i < rows; ++i){
            for (int j = 0; j < cols; ++j){
                rotated[j][rows - 1 - i] = block[i][j];
            }
        }

        return rotated;
    }

    private static char[][] mirror(char[][] block){
        int rows = block.length;
        int cols = block[0].length;
        char[][] mirrored = new char[rows][cols];

        for (int i = 0; i < rows; ++i){
            for (int j = 0; j < cols; ++j){
                mirrored[i][cols - 1 - j] = block[i][j];
            }
        }

        return mirrored;
    }

    public static void readInput(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new java.io.FileReader(filename));

        String[] boardSpesification = br.readLine().split(" ");
        N = Integer.parseInt(boardSpesification[0]);
        M = Integer.parseInt(boardSpesification[1]);
        P = Integer.parseInt(boardSpesification[2]);

        boardType = br.readLine();

        char[][] customBoard = new char[N][M];
        if (boardType.equals("CUSTOM")){
            for (int i = 0; i < N; ++i){
                String line = br.readLine().trim();
                for (int j = 0; j < M; ++j){
                    customBoard[i][j] = line.charAt(j);
                }
            }
        }

        List<String> lines = new ArrayList<>();
        String line;
        char tempBlock = '\0';
        while ((line = br.readLine()) != null){
            if (!line.isEmpty()){
                char currentBlock = getCurrentBlock(line);
                if (tempBlock != '\0' && currentBlock != tempBlock){
                    boardBlocks.add(convertToBlock(lines));
                    lines.clear();
                }
                lines.add(line);
                tempBlock = currentBlock;
            }
        }
        boardBlocks.add(convertToBlock(lines));

        if(boardType.equals("DEFAULT")){
            board = new Board(N, M);
        } else {
            board = new Board(N, M, customBoard);
        }

        br.close();
    }

    private static char getCurrentBlock(String line){
        char currentBlock = ' ';
        int rows = line.length();
        int i = 0;
        while (i < rows && currentBlock == ' '){
            currentBlock = line.charAt(i);
            i++;
        }
        return currentBlock;
    }

    private static char[][] convertToBlock(List<String> lines) {
        int rows = lines.size();
        int cols = lines.stream().mapToInt(String::length).max().orElse(0);
        char[][] block = new char[rows][cols];
    
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (j < lines.get(i).length() && lines.get(i).charAt(j) != ' ') {
                    block[i][j] = lines.get(i).charAt(j);
                } else {
                    block[i][j] = '.';
                }
            }
        }
    
        return block;
    }
}
