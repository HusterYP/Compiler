import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class StackCompiler {

    private Node head = null; // 树的头结点
    private LinkedList<Node> stack = new LinkedList<>();
    private StringBuilder builder = new StringBuilder();

    public static void main(String[] args) {
        StackCompiler stackCompiler = new StackCompiler();
        String source = Utils.getSourceFromFile("demo.sum");
        stackCompiler.createSumTree(source);
        stackCompiler.postOrderTraversal(stackCompiler.head);
        stackCompiler.writeToFile();
    }

    // 构建加法树
    public void createSumTree(String source) {
        ArrayList<String> data = Utils.splitString(source);
        final int n = data.size();
        for (int i = 0; i < n; i++) {
            if (!data.get(i).equals("+")) {
                Node temp = new Node(data.get(i), false);
                if (i == 0) {
                    stack.push(temp);
                } else if (!stack.isEmpty()) {
                    Node top = stack.pop();
                    if (top.right == null)
                        top.right = temp;
                    stack.push(top);
                }
            } else {
                Node temp = new Node(data.get(i), true);
                if (!stack.isEmpty())
                    temp.left = stack.pop();
                stack.push(temp);
            }
        }
        if (!stack.isEmpty())
            head = stack.pop();
    }

    // 树的后序遍历
    public void postOrderTraversal(Node head) {
        if (head == null)
            return;
        postOrderTraversal(head.left);
        postOrderTraversal(head.right);
        createOption(head);
    }

    private void createOption(Node node) {
        if (head == null)
            return;
        if (node.data.equals("+")) {
            builder.append("add\n");
        } else {
            builder.append("push ").append(node.data).append("\n");
        }
    }

    public void writeToFile() {
        if (builder.toString().isEmpty())
            return;
        File file = new File("demo.exc");
        BufferedWriter writer = null;
        try {
            if (!file.exists())
                file.createNewFile();
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(builder.toString(), 0, builder.toString().length());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    class Node {
        String data; // 数据
        boolean isOption; // 是否是运算符
        Node left; // 左节点
        Node right; // 右节点

        public Node(String data, boolean isOption) {
            this.data = data;
            this.isOption = isOption;
        }
    }
}
