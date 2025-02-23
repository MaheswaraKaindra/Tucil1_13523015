import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private int rows, cols;
    private char[][] board;
    private static final Map<Character, String> COLOR_MAP = new HashMap<>();

    private static final String RESET = "\u001B[0m";
    private static final String[] COLORS = {
        "\u001B[31m",
        "\u001B[32m",
        "\u001B[33m",
        "\u001B[34m",
        "\u001B[35m",
        "\u001B[36m",
        "\u001B[91m",
        "\u001B[92m",
        "\u001B[93m",
        "\u001B[94m",
        "\u001B[95m",
        "\u001B[96m"
    };

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new char[rows][cols];
        for (char[] row : board) {
            Arrays.fill(row, '.');
        }
    }

    public Board(int rows, int cols, char[][] customBoard){
        this.rows = rows;
        this.cols = cols;
        this.board = customBoard;
    }

    public boolean isFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean ableToPlaceBlock(char[][] block, int y, int x) {
        int blockRows = block.length;
        int blockCols = block[0].length;

        if ((y + blockRows) > rows || (x + blockCols) > cols) {
            return false;
        }

        for (int i = 0; i < blockRows; ++i) {
            for (int j = 0; j < blockCols; ++j) {
                if (block[i][j] != '.' && board[y + i][x + j] != '.') {
                    return false;
                }
            }
        }

        return true;
    }

    public void placeBlock(char[][] block, int y, int x, char label) {
        for (int i = 0; i < block.length; ++i) {
            for (int j = 0; j < block[i].length; ++j) {
                if (block[i][j] != '.') {
                    board[y + i][x + j] = label;
                }
            }
        }
    }

    public void removeBlock(char[][] block, int y, int x) {
        for (int i = 0; i < block.length; ++i) {
            for (int j = 0; j < block[i].length; ++j) {
                if (block[i][j] != '.') {
                    board[y + i][x + j] = '.';
                }
            }
        }
    }

    public void printBoard() {
        assignColors();

        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '.') {
                    System.out.print(RESET + "." + RESET);
                } else if (cell == '*'){
                    System.out.print(RESET + ' ' + RESET);
                } else if (cell != '\0') {
                    System.out.print(COLOR_MAP.get(cell) + cell + RESET);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void assignColors() {
        if (!COLOR_MAP.isEmpty()) return;

        int colorIndex = 0;
        for (int i = 0; i < Solver.boardBlocks.size(); i++) {
            char blockLabel = (char) ('A' + i);
            COLOR_MAP.put(blockLabel, COLORS[colorIndex % COLORS.length]);
            colorIndex++;
        }
    }

    public void saveResult(String filePath){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    writer.print(board[i][j]);
                }
                writer.println();
            }
        } catch (IOException e) {
            
        }
    }
}
