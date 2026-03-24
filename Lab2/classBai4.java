import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class classBai4 {

    public static void main(String[] args) {
        String inputFile = "hotel-review-final.csv";
        String outputFile = "ket-qua-bai4.txt";

        // Map: category → (positive words frequency)
        Map<String, Map<String, Integer>> posWordsByCat = new HashMap<>();
        Map<String, Map<String, Integer>> negWordsByCat = new HashMap<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {

            // Bỏ qua header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = line.split(";", -1);
                if (cols.length < 4) continue;

                String review = cols[0].trim();
                String category = cols[1].trim().toUpperCase();
                String sentiment = cols[3].trim().toLowerCase();

                if (review.isEmpty()) continue;

                String[] words = review.split("\\s+");

                if (sentiment.equals("positive")) {
                    posWordsByCat.putIfAbsent(category, new HashMap<>());
                    Map<String, Integer> map = posWordsByCat.get(category);
                    for (String w : words) {
                        if (!w.isEmpty()) {
                            map.put(w, map.getOrDefault(w, 0) + 1);
                        }
                    }
                } else if (sentiment.equals("negative")) {
                    negWordsByCat.putIfAbsent(category, new HashMap<>());
                    Map<String, Integer> map = negWordsByCat.get(category);
                    for (String w : words) {
                        if (!w.isEmpty()) {
                            map.put(w, map.getOrDefault(w, 0) + 1);
                        }
                    }
                }
            }

            // ====================== GHI KẾT QUẢ ======================
            bw.write("BÁO CÁO BÀI 4 - PHÂN TÍCH TỪ THEO PHÂN LOẠI\n");
            bw.write("==================================================\n\n");

            System.out.println("==================================================");
            System.out.println("               BÀI 4 - HOÀN THÀNH");
            System.out.println("==================================================\n");

            // Lấy tất cả category
            Set<String> categories = new HashSet<>();
            categories.addAll(posWordsByCat.keySet());
            categories.addAll(negWordsByCat.keySet());

            List<String> sortedCats = new ArrayList<>(categories);
            Collections.sort(sortedCats);

            for (String cat : sortedCats) {
                bw.write("PHÂN LOẠI: " + cat + "\n");
                bw.write("--------------------------------------------------\n");

                System.out.println("PHÂN LOẠI: " + cat);

                // 5 từ tích cực nhất
                bw.write("5 từ mang ý nghĩa TÍCH CỰC nhất:\n");
                System.out.println("   5 từ TÍCH CỰC nhất:");
                writeTop5(posWordsByCat.getOrDefault(cat, new HashMap<>()), bw, System.out);

                // 5 từ tiêu cực nhất
                bw.write("\n5 từ mang ý nghĩa TIÊU CỰC nhất:\n");
                System.out.println("   5 từ TIÊU CỰC nhất:");
                writeTop5(negWordsByCat.getOrDefault(cat, new HashMap<>()), bw, System.out);

                bw.write("\n\n");
                System.out.println();
            }

            System.out.println("Đã xuất kết quả ra file: " + outputFile);

        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý file!");
            e.printStackTrace();
        }
    }

    // Ghi và in Top 5 từ
    private static void writeTop5(Map<String, Integer> freqMap, BufferedWriter bw, PrintStream out) throws IOException {
        if (freqMap.isEmpty()) {
            bw.write("   (Không có dữ liệu)\n");
            out.println("      (Không có dữ liệu)");
            return;
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(freqMap.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < Math.min(5, list.size()); i++) {
            String text = String.format("   %d. %-20s : %d lần\n", i + 1, 
                                        list.get(i).getKey(), list.get(i).getValue());
            bw.write(text);
            out.print(text);
        }
    }
}
