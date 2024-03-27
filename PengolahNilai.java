import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PengolahNilai {

    private static final String SEMICOLON_DELIMITER = ";";

    public static void main(String[] args) throws FileNotFoundException {
        menuAwal();
    }

    public static void header() {
        System.out.println("----------------------------------------------------------");
        System.out.println("Aplikasi Pengoolah Nilai Siswa");
        System.out.println("----------------------------------------------------------");
    }

    public static void menuAwal() {
        Scanner scanner = new Scanner(System.in);
        List<List<Integer>> records = readCSV("C:\\temp\\direktori\\data_sekolah.csv");
        header();
        System.out.println("Letakkan file csv dengan nama file data_sekolah di direktori");
        System.out.println("berikut: C://temp/direktori");
        System.out.println("");

        int pilihan;
        do {
            System.out.println("pilih menu:");
            System.out.println("1. Generate txt untuk menampilkan modus");
            System.out.println("2. Generate txt untuk menampilkan nilai rata-rata, median");
            System.out.println("3. Generate kedua file");
            System.out.println("0. Exit");
            pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    generateFrequencyTxt(records);
                    break;
                case 2:
                    generateRataMedianTxt(records);
                    break;
                case 3:
                    generateFrequencyTxt(records);
                    generateRataMedianTxt(records);
                    break;
                case 0:
                    System.out.println("Exit");
                    break;
                default:
                    break;
            }
        } while (pilihan != 0);
    }

    public static void generateFrequencyTxt(List<List<Integer>> data) {
        if (data.isEmpty()) {
            System.out.println("File tidak ditemukan");
            return;
        }

        Map<Integer, Integer> frekuensiNilai = calculateFrequency(data);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/temp/direktori/data_sekolah_modus.txt"))) {
            writer.write("Berikut Hasil Pengolahan Nilai:");
            writer.newLine();
            writer.write("Nilai\t\t| Frekuensi");
            writer.newLine();
            for (Map.Entry<Integer, Integer> entry : frekuensiNilai.entrySet()) {
                if (entry.getKey() < 6) {
                    writer.write("kurang dari 6\t| " + entry.getValue());
                    writer.newLine();
                } else {
                    writer.write(entry.getKey() + "\t\t| " + entry.getValue());
                    writer.newLine();
                }
            }
            header();
            System.out.println("File telah di generate di C://temp/direktori");
        } catch (IOException e) {
            System.out.println("Gagal menulis ke file data_sekolah_modus.txt: " + e.getMessage());
        }

        System.out.println("silahkan cek"); 
        System.out.println("");

        Scanner scanner = new Scanner(System.in);
        int pilih;

        do{
            System.out.println("1. Kembali ke menu utama");
            System.out.println("0. Exit");
            pilih = scanner.nextInt();
            switch (pilih) {
                case 1:
                    menuAwal();
                    break;
                case 0:
                    System.out.println("Exit");
                    break;
                default:
                    break;
            }
        } while (pilih != 0);
        System.out.println("");
}

    
    private static void generateRataMedianTxt(List<List<Integer>> nilaiSiswa) {
        if (nilaiSiswa.isEmpty()) {
            System.out.println("File tidak ditemukan");
            return;
        }

        double median = calculateMedian(nilaiSiswa);
        double mean = calculateMean(nilaiSiswa);
        List<Integer> modus = calculateModus(nilaiSiswa);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/temp/direktori/data_sekolah_modus_median.txt"))) {
            writer.write("Berikut Hasil Pengolahan Nilai:");
            writer.newLine();
            writer.newLine();
            writer.write("Berikut hasil sebaran data nilai");
            writer.newLine();
            String formattedMean = String.format("%.2f", mean);
            writer.write("Mean: " + formattedMean);
            writer.newLine();
            writer.write("Median: " + median);
            writer.newLine();
            writer.write("Modus: " + modus);
            writer.newLine();
            header();
            System.out.println("File telah di generate di C://temp/direktori.");
        } catch (IOException e) {
            System.out.println("Gagal menulis ke file data_sekolah_modus_median.txt: " + e.getMessage());
        }

        System.out.println("silahkan cek"); 
        System.out.println("");

        Scanner scanner = new Scanner(System.in);
        int pilih;

        do{
            System.out.println("1. Kembali ke menu utama");
            System.out.println("0. Exit");
            pilih = scanner.nextInt();
            switch (pilih) {
                case 1:
                    menuAwal();
                    break;
                case 0:
                    System.out.println("Exit");
                    break;
                default:
                    break;
            }
        } while (pilih != 0);
        System.out.println("");
    }

    private static List<List<Integer>> readCSV(String csvFile) {
        List<List<Integer>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(SEMICOLON_DELIMITER);
                List<Integer> record = new ArrayList<>();
                for (String value : values) {
                    if (!value.startsWith("Kelas")) { // Skip values starting with "Kelas"
                        record.add(Integer.parseInt(value));
                    }
                }
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static Map<Integer, Integer> calculateFrequency(List<List<Integer>> data) {
        Map<Integer, Integer> frekuensiNilai = new HashMap<>();
        for (List<Integer> nilaiKelas : data) {
            for (int nilai : nilaiKelas) {
                frekuensiNilai.put(nilai, frekuensiNilai.getOrDefault(nilai, 0) + 1);
            }
        }
        return frekuensiNilai;
    }

    private static double calculateMean(List<List<Integer>> nilaiSiswa) {
        double total = 0;
        int count = 0;

        for (List<Integer> sublist : nilaiSiswa) {
            for (int nilai : sublist) {
                total += nilai;
                count++;
            }
        }

        return total / count;
    }

    private static double calculateMedian(List<List<Integer>> nilaiSiswa) {
        List<Integer> nilaiRata = new ArrayList<>();

        for (List<Integer> sublist : nilaiSiswa) {
            nilaiRata.addAll(sublist);
        }

        Collections.sort(nilaiRata);

        int size = nilaiRata.size();
        if (size % 2 == 0) {
            int mid1 = nilaiRata.get(size / 2 - 1);
            int mid2 = nilaiRata.get(size / 2);
            return (mid1 + mid2) / 2.0;
        } else {
            return nilaiRata.get(size / 2);
        }
    }

    private static List<Integer> calculateModus(List<List<Integer>> nilaiSiswa) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (List<Integer> sublist : nilaiSiswa) {
            for (int nilai : sublist) {
                frequencyMap.put(nilai, frequencyMap.getOrDefault(nilai, 0) + 1);
            }
        }

        List<Integer> modus = new ArrayList<>();
        int maxFrequency = 0;

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int frequency = entry.getValue();
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                modus.clear();
                modus.add(entry.getKey());
            } else if (frequency == maxFrequency) {
                modus.add(entry.getKey());
            }
        }

        return modus;
    }

}
