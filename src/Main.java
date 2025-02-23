import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan nama file test case: ");
        String filename = scanner.nextLine().trim();

        String filePath = "../test/" + filename;
        String outputPath = "../test/solution-" + filename; 

        try {
            Solver.readInput(filePath);
            Solver.solvePuzzle();

            System.out.print("Simpan solusi / state terakhir board? (ya/tidak): ");
            String response = scanner.nextLine().trim();

            if (response.equals("ya")){
                Solver.board.saveResult(outputPath);
                System.out.println("Solusi disimpan pada: " + outputPath);
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file.");
        }
        scanner.close();
    }
}
