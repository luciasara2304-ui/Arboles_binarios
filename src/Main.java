import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Arbol de ejemplo del enunciado
        TreeNode a = new TreeNode("A");
        BinaryTree tree = new BinaryTree(a);
        a.left = new TreeNode("B");
        a.right = new TreeNode("C");
        a.left.left = new TreeNode("D");
        a.left.right = new TreeNode("E");
        a.right.left = new TreeNode("F");
        a.right.right = new TreeNode("G");
        a.left.left.left = new TreeNode("H");
        a.left.left.right = new TreeNode("I");

        Scanner sc = new Scanner(System.in);
        int op = 0;

        while (op != 8) {
            System.out.println("\n========== MENU ==========");
            System.out.println("1. Construir arbol");
            System.out.println("2. Mostrar arbol");
            System.out.println("3. Recorrido Preorden");
            System.out.println("4. Recorrido Inorden");
            System.out.println("5. Recorrido Posorden");
            System.out.println("6. Mostrar factores de equilibrio");
            System.out.println("7. Balancear arbol (AVL)");
            System.out.println("8. Salir");
            System.out.println("==========================");
            System.out.print("Seleccione una opcion: ");

            try {
                op = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                op = 0;
            }

            switch (op) {
                case 1: tree.buildTree(sc); tree.printTree(); break;
                case 2: tree.printTree(); break;
                case 3: System.out.println("Preorden: " + tree.preorder()); break;
                case 4: System.out.println("Inorden: " + tree.inorder()); break;
                case 5: System.out.println("Posorden: " + tree.postorder()); break;
                case 6: tree.printBalanceFactors(); break;
                case 7: tree.balance(); break;
                case 8: System.out.println("Hasta luego."); break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }
}
