/**
 * 家庭作业0
 * Tiger书第一章Introduction课后作业
 * */
public class Tree {
    Tree left;
    String key;
    Tree right;
    Object binding;

    Tree(Tree l, String k, Tree r) {
        left = l;
        key = k;
        right = r;
    }

    Tree(Tree l, String k, Tree r, Object b) {
        this(l, k, r);
        binding = b;
    }

    Tree insert(String key, Tree t) {
        if (t == null)
            return new Tree(null, key, null);
        else if (key.compareTo(t.key) < 0)
            return new Tree(insert(key, t.left), t.key, t.right);
        else if (key.compareTo(t.key) > 0)
            return new Tree(t.left, t.key, insert(key, t.right));
        else
            return new Tree(t.left, key, t.right);
    }

    // Implement a member function that returns true if the item is found, else false.
    public boolean foundItem(String key, Tree t) {
        if (t == null || key == null)
            return false;
        if (key.compareTo(t.key) == 0)
            return true;
        else if (key.compareTo(t.key) < 0)
            return foundItem(key, t.left);
        else
            return foundItem(key, t.right);
    }

    //  Extend the program to include not just membership, but the mapping of keys to bindings:
    public Tree insert(String key, Object binding, Tree t) {
        if (t == null)
            return new Tree(null, key, null, binding);
        else if (key.compareTo(t.key) < 0)
            return new Tree(t.left, t.key, insert(key, t.right), binding);
        else if (key.compareTo(t.key) > 0)
            return new Tree(insert(key, t.left), t.key, t.right, binding);
        else
            return new Tree(t.left, key, t.right, binding);
    }

    public Object lookup(String key, Tree t) {
        if (t == null || key == null)
            return null;
        if (key.compareTo(t.key) == 0)
            return t.binding;
        else if (key.compareTo(t.key) < 0)
            return lookup(key, t.left);
        else
            return lookup(key, t.right);
    }

}
