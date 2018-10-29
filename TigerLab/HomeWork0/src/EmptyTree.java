//  Rewrite in an object-oriented (but still “functional”) style, so that insertion
// is now t.insert(key) instead of insert(key,t).
public class EmptyTree extends Tree {

    EmptyTree(Tree l, String k, Tree r) {
        super(l, k, r);
    }

    EmptyTree(Tree l, String k, Tree r, Object b) {
        super(l, k, r, b);
    }

    public void insert(String key) {
        if (key == null)
            return;
        else if (key.compareTo(this.key) < 0)
            this.left = insert(key, this.left);
        else if (key.compareTo(this.key) > 0)
            this.right = insert(key, this.right);
    }
}
