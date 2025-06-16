package huffmannode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

// 霍夫曼編碼主類別
public class HuffmanCoding {
    private HuffmanNode root;
    private Map<Character, String> huffmanCodes;

    public HuffmanCoding() {
        this.huffmanCodes = new HashMap<>();
    }

    // 解析輸入字串陣列格式：[a:56,b:19,c:2,d:11,e:18]
    private Map<Character, Integer> parseInput(String input) {
        Map<Character, Integer> frequency = new HashMap<>();

        // 移除方括號和空格
        input = input.replaceAll("[\\[\\]\\s]", "");

        // 用逗號分割
        String[] pairs = input.split(",");

        for (String pair : pairs) {
            String[] parts = pair.split(":");
            if (parts.length == 2) {
                char character = parts[0].charAt(0);
                int freq = Integer.parseInt(parts[1]);
                frequency.put(character, freq);
            }
        }

        return frequency;
    }

    // 建立霍夫曼樹（小的放左邊，大的放右邊）
    private HuffmanNode buildHuffmanTree(Map<Character, Integer> frequency) {
        // 使用優先佇列（最小堆積）
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

        // 將所有字符加入優先佇列
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        System.out.println("初始節點（按頻率排序）：");
        List<HuffmanNode> temp = new ArrayList<>(pq);
        temp.sort(HuffmanNode::compareTo);
        for (HuffmanNode node : temp) {
            System.out.println("  " + node);
        }
        System.out.println();

        // 特殊情況：只有一個字符
        if (pq.size() == 1) {
            HuffmanNode single = pq.poll();
            return new HuffmanNode(single.frequency, single, null);
        }

        // 建立霍夫曼樹
        int step = 1;
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();   // 頻率較小的節點放左邊
            HuffmanNode right = pq.poll();  // 頻率較大的節點放右邊

            System.out.println("步驟 " + step + ":");
            System.out.println("  左子樹（小）: " + left);
            System.out.println("  右子樹（大）: " + right);

            // 建立新的內部節點
            HuffmanNode merged = new HuffmanNode(
                    left.frequency + right.frequency, left,right
            );

            System.out.println("  合併後: " + merged);
            System.out.println();

            pq.offer(merged);
            step++;
        }

        return pq.poll(); // 返回根節點
    }

    // 生成霍夫曼編碼
    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) return;

        // 如果是葉子節點，儲存編碼
        if (node.isLeaf()) {
            // 特殊情況：只有一個字符時
            huffmanCodes.put(node.character, code.isEmpty() ? "0" : code);
            return;
        }

        // 遞迴處理左右子樹（左0右1）
        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }

    // 主要處理方法
    public void processInput(String input) {
        System.out.println("輸入: " + input);

        // 1. 解析輸入格式
        Map<Character, Integer> frequency = parseInput(input);
        System.out.println("解析後的頻率表:");
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            System.out.println("  " + entry.getKey() + " : " + entry.getValue());
        }
        System.out.println();

        // 2. 建立霍夫曼樹
        this.root = buildHuffmanTree(frequency);

        // 3. 生成霍夫曼編碼
        this.huffmanCodes.clear();
        generateCodes(this.root, "");

        // 4. 顯示結果
        printResults();
    }

    // 顯示編碼結果
    private void printResults() {
        System.out.println("=== 霍夫曼編碼結果 ===");

        // 按字母順序排序顯示
        List<Character> sortedChars = new ArrayList<>(huffmanCodes.keySet());
        Collections.sort(sortedChars);

        for (char c : sortedChars) {
            System.out.println(c + " : " + huffmanCodes.get(c));
        }

        // 顯示樹狀結構
        System.out.println("\n=== 霍夫曼樹結構 ===");
        printTree(root, "", true);
    }

    // 印出樹狀結構
    private void printTree(HuffmanNode node, String prefix, boolean isLast) {
        if (node == null) return;

        System.out.println(prefix + (isLast ? "└── " : "├── ") +
                (node.isLeaf() ? node.character + "(" + node.frequency + ")" :
                        "內部節點(" + node.frequency + ")"));

        if (!node.isLeaf()) {
            printTree(node.left, prefix + (isLast ? "    " : "│   "), false);
            printTree(node.right, prefix + (isLast ? "    " : "│   "), true);
        }
    }

    // 編碼文字
    public String encode(String text) {
        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (huffmanCodes.containsKey(c)) {
                encoded.append(huffmanCodes.get(c));
            }
        }
        return encoded.toString();
    }

    // 解碼文字
    public String decode(String encodedText) {
        if (encodedText == null || encodedText.isEmpty() || root == null) {
            return "";
        }

        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;

        for (char bit : encodedText.toCharArray()) {
            // 特殊情況：只有一個字符
            if (root.isLeaf()) {
                decoded.append(root.character);
                continue;
            }

            // 根據位元移動
            if (bit == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            // 如果到達葉子節點
            if (current.isLeaf()) {
                decoded.append(current.character);
                current = root; // 重置到根節點
            }
        }

        return decoded.toString();
    }

    // 主程式測試
    public static void main(String[] args) {
        HuffmanCoding huffman = new HuffmanCoding();

        //Sample
        String input = "[a:13,b:7,c:12,d:9,e:55,f:4]";
        huffman.processInput(input);

        // 測試編碼解碼
        System.out.println("\n=== 編碼測試 ===");
        String testText = "abcde";
        String encoded = huffman.encode(testText);
//        String decoded = huffman.decode(encoded);

        System.out.println("測試文字: " + testText);
        System.out.println("編碼結果: " + encoded);
//        System.out.println("解碼結果: " + decoded);
//        System.out.println("正確性: " + testText.equals(decoded));

//        // 額外測試案例
//        System.out.println("\n" + "=".repeat(50));
//        System.out.println("額外測試案例:");
//        String input2 = "[x:10,y:20,z:5,w:15]";
//        huffman.processInput(input2);
    }
}
