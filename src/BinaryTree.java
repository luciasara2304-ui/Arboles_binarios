import java.util.*;

public class BinaryTree {

    TreeNode root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(TreeNode root) {
        this.root = root;
    }

    // ================= 1. CONSTRUCCIÓN =================

    public void insertRoot(Object data) {
        if (root == null) {
            root = new TreeNode(data);
        }
    }

    public TreeNode search(Object data) {
        return search(root, data);
    }

    private TreeNode search(TreeNode node, Object data) {
        if (node == null) return null;
        if (Objects.equals(node.data, data)) return node;
        TreeNode found = search(node.left, data);
        if (found != null) return found;
        return search(node.right, data);
    }

    public boolean insertLeft(Object parent, Object data) {
        TreeNode p = search(parent);
        if (p == null || p.left != null) return false;
        p.left = new TreeNode(data);
        return true;
    }

    public boolean insertRight(Object parent, Object data) {
        TreeNode p = search(parent);
        if (p == null || p.right != null) return false;
        p.right = new TreeNode(data);
        return true;
    }

    // Lee una linea. Devuelve null si esta vacia o es "-".
    private String readData(Scanner sc, String pregunta) {
        System.out.print(pregunta);
        String s = sc.nextLine().trim();
        if (s.isEmpty() || s.equals("-")) return null;
        return s;
    }

    // Lee por niveles usando una cola: raiz, luego izquierdo y derecho de cada nodo.
    public void buildTree(Scanner sc) {
        String r = readData(sc, "Raiz: ");
        root = (r == null) ? null : new TreeNode(r);

        Queue<TreeNode> cola = new LinkedList<>();
        if (root != null) cola.add(root);

        while (!cola.isEmpty()) {
            TreeNode n = cola.poll();
            String izq = readData(sc, "Hijo izquierdo de " + n.data + ": ");
            String der = readData(sc, "Hijo derecho de " + n.data + ": ");
            if (izq != null) {
                n.left = new TreeNode(izq);
                cola.add(n.left);
            }
            if (der != null) {
                n.right = new TreeNode(der);
                cola.add(n.right);
            }
        }
    }

    // ================= 2. VISUALIZACION =================

    private Map<TreeNode, Integer> pos = new HashMap<>(); // columna de cada nodo
    private int contador, maxLen;
    private boolean conFactores;

    public void printTree() {
        dibujar(false);
    }

    private void dibujar(boolean conFactores) {
        if (root == null) {
            System.out.println("El arbol esta vacio.");
            return;
        }
        this.conFactores = conFactores;
        pos.clear();
        contador = 0;
        maxLen = 0;
        asignarColumnas(root);                 // 1) columna de cada nodo (orden inorden)

        int paso = maxLen + 1;                 // ancho de cada columna
        char[][] g = new char[2 * height(root) - 1][contador * paso + maxLen];
        for (char[] fila : g) Arrays.fill(fila, ' ');
        escribir(root, 0, g, paso);            // 2) escribir nodos y ramas

        for (char[] fila : g) System.out.println(new String(fila));
    }

    private String texto(TreeNode n) {
        if (!conFactores) return String.valueOf(n.data);
        int f = balanceFactor(n);
        return n.data + "(" + (f > 0 ? "+" : "") + f + ")";
    }

    private void asignarColumnas(TreeNode n) {
        if (n == null) return;
        asignarColumnas(n.left);
        pos.put(n, contador++);
        maxLen = Math.max(maxLen, texto(n).length());
        asignarColumnas(n.right);
    }

    private void escribir(TreeNode n, int nivel, char[][] g, int paso) {
        if (n == null) return;
        int fila = nivel * 2;
        int col = pos.get(n) * paso;
        String t = texto(n);
        for (int i = 0; i < t.length(); i++) g[fila][col + i] = t.charAt(i);

        if (n.left != null) {
            int c = pos.get(n.left) * paso;
            g[fila + 1][c] = '/';
            for (int i = c + 1; i < col; i++) g[fila][i] = '_';
        }
        if (n.right != null) {
            int c = pos.get(n.right) * paso;
            g[fila + 1][c] = '\\';
            for (int i = col + t.length(); i < c; i++) g[fila][i] = '_';
        }
        escribir(n.left, nivel + 1, g, paso);
        escribir(n.right, nivel + 1, g, paso);
    }

    // ================= 3. RECORRIDOS =================

    public String preorder() {
        return pre(root).trim();
    }

    private String pre(TreeNode n) {
        if (n == null) return "";
        return n.data + " " + pre(n.left) + pre(n.right);
    }

    public String inorder() {
        return in(root).trim();
    }

    private String in(TreeNode n) {
        if (n == null) return "";
        return in(n.left) + n.data + " " + in(n.right);
    }

    public String postorder() {
        return post(root).trim();
    }

    private String post(TreeNode n) {
        if (n == null) return "";
        return post(n.left) + post(n.right) + n.data + " ";
    }

    // ================= 4. FACTORES DE EQUILIBRIO =================

    public int height(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public int balanceFactor(TreeNode node) {
        if (node == null) return 0;
        return height(node.right) - height(node.left);
    }

    public boolean isBalanced() {
        return unbalancedNodes().isEmpty();
    }

    public List<Object> unbalancedNodes() {
        List<Object> lista = new ArrayList<>();
        buscarDesbalanceados(root, lista);
        return lista;
    }

    private void buscarDesbalanceados(TreeNode n, List<Object> lista) {
        if (n == null) return;
        if (Math.abs(balanceFactor(n)) > 1) lista.add(n.data);
        buscarDesbalanceados(n.left, lista);
        buscarDesbalanceados(n.right, lista);
    }

    public void printBalanceFactors() {
        dibujar(true);
    }

    // ================= 5. BALANCEO AVL =================

    public TreeNode rotateRight(TreeNode node) {
        TreeNode nueva = node.left;
        node.left = nueva.right;
        nueva.right = node;
        return nueva;
    }

    public TreeNode rotateLeft(TreeNode node) {
        TreeNode nueva = node.right;
        node.right = nueva.left;
        nueva.left = node;
        return nueva;
    }

    public TreeNode rotateLeftRight(TreeNode node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    public TreeNode rotateRightLeft(TreeNode node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    public void balance() {
        if (root == null) {
            System.out.println("El arbol esta vacio.");
            return;
        }
        System.out.println("--- Arbol original ---");
        printBalanceFactors();
        System.out.println("Nodos que requieren balanceo: " + unbalancedNodes());

        int vueltas = 0;
        while (!isBalanced() && vueltas++ < 100) {
            root = rebalance(root);
        }

        System.out.println("--- Arbol despues de balancear ---");
        printBalanceFactors();
    }

    // Balancea de abajo hacia arriba. Devuelve la nueva raiz del subarbol.
    private TreeNode rebalance(TreeNode n) {
        if (n == null) return null;
        n.left = rebalance(n.left);
        n.right = rebalance(n.right);

        int f = balanceFactor(n);
        if (f < -1) {                                   // pesado a la izquierda
            if (balanceFactor(n.left) <= 0) {
                System.out.println("Rotacion simple a la derecha en " + n.data);
                return rotateRight(n);
            }
            System.out.println("Rotacion doble izquierda-derecha en " + n.data);
            return rotateLeftRight(n);
        }
        if (f > 1) {                                    // pesado a la derecha
            if (balanceFactor(n.right) >= 0) {
                System.out.println("Rotacion simple a la izquierda en " + n.data);
                return rotateLeft(n);
            }
            System.out.println("Rotacion doble derecha-izquierda en " + n.data);
            return rotateRightLeft(n);
        }
        return n;
    }
}
