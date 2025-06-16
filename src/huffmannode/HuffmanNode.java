package huffmannode;

// 霍夫曼樹節點類別
public class HuffmanNode implements Comparable<HuffmanNode> {
    char character;
    int frequency;
    HuffmanNode left;
    HuffmanNode right;

    // 葉子節點建構子
    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    // 內部節點建構子
    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.character = '\0'; // 內部節點沒有字符
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    // 判斷是否為葉子節點
    public boolean isLeaf() {
        return left == null && right == null;
    }

    // 實作Comparable介面，用於優先佇列排序
    @Override
    public int compareTo(HuffmanNode other) {
        if (this.frequency != other.frequency) {
            return Integer.compare(this.frequency, other.frequency);
        }
        // 如果頻率相同，按字符順序排序（確保結果穩定）
        return Character.compare(this.character, other.character);
    }

    @Override
    public String toString() {
        if (isLeaf()) {
            return character + ":" + frequency;
        } else {
            return "(" + frequency + ")";
        }
    }
}

