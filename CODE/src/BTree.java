// Inserting a key on a B-tree in Java 

public class BTree {

    private int T;
  
    // Node Creation
    public class Node {
      int n;
      int key[] = new int[2 * T - 1];
      Record res[]=new Record[2*T-1];
      Node child[] = new Node[2 * T];
      boolean leaf = true;
  
      public Record Find(int k) {
        for (int i = 0; i < this.n; i++) {
          if (this.key[i] == k) {
            return res[i];
          }
        }
        return null;
      };
    }
  
    public BTree(int t) {
      T = t;
      root = new Node();
      root.n = 0;
      root.leaf = true;
    }
  
    private Node root;
  
    // split
    private void split(Node x, int pos, Node y) {
      Node z = new Node();
      z.leaf = y.leaf;
      z.n = T - 1;
      for (int j = 0; j < T - 1; j++) {
        z.key[j] = y.key[j + T];
        z.res[j] = y.res[j + T];
      }
      if (!y.leaf) {
        for (int j = 0; j < T; j++) {
          z.child[j] = y.child[j + T];
        }
      }
      y.n = T - 1;
      for (int j = x.n; j >= pos + 1; j--) {
        x.child[j + 1] = x.child[j];
      }
      x.child[pos + 1] = z;
  
      for (int j = x.n - 1; j >= pos; j--) {
        x.key[j + 1] = x.key[j];
        x.res[j+1]=x.res[j];
      }
      x.key[pos] = y.key[T - 1];
      x.res[pos] = y.res[T - 1];
      x.n = x.n + 1;
    }
  
    // insert key
    public void insert(final int key) {
      Node r = root;
      if (r.n == 2 * T - 1) {
        Node s = new Node();
        root = s;
        s.leaf = false;
        s.n = 0;
        s.child[0] = r;
        split(s, 0, r);
        _insert(s, key, record);
      } else {
        _insert(r, key, record);
      }
    }
  
    // insert node
    final private void _insert(Node x, int k, Record rec ) {
  
      if (x.leaf) {
        int i = 0;
        for (i = x.n - 1; i >= 0 && k < x.key[i]; i--) {
          x.key[i + 1] = x.key[i];
          x.res[i + 1] = x.res[i];
        }
        x.key[i + 1] = k;
        x.res[i + 1] = rec
        x.n = x.n + 1;
      } else {
        int i = 0;
        for (i = x.n - 1; i >= 0 && k < x.key[i]; i--) {
        }
        ;
        i++;
        Node tmp = x.child[i];
        if (tmp.n == 2 * T - 1) {
          split(x, i, tmp);
          if (k > x.key[i]) {
            i++;
          }
        }
        _insert(x.child[i], k, rec);
      }
  
    }
  
    public void display() {
      display(root);
    }
  
    // Display the tree
    private void display(Node x) {
      assert (x == null);
      for (int i = 0; i < x.n; i++) {
        System.out.println("Key : "+x.key[i] + " Record: "+x.res[i]);
      }
      if (!x.leaf) {
        for (int i = 0; i < x.n + 1; i++) {
          display(x.child[i]);
        }
      }
    }
  
    public static void main(String[] args) {
      BTree b = new BTree(3);
      b.insert(8);
      b.insert(9);
      b.insert(10);
      b.insert(11);
      b.insert(15);
      b.insert(20);
      b.insert(17);
  
      b.display();
    }
  }