import java.util.ArrayList;

public class Tree {
    private Node root;

    Tree(){
        root = new Node();
    }

    public boolean insert(int x){

        if(root.keys.isEmpty()) {
            Node start = new Node(x);
            root = start;
            return true;
        }

        return root.searchIns(x) != null;
    }

    public int size(){
        return root.subSize;
    }

    public int size(int x){
        Node get = root.search(x);

        if(get == null)
            return 0;

        return get.subSize;
    }

    public int get(int x){
        return root.select(x).index();
    }


    public class Node{
        ArrayList<Integer> keys = new ArrayList<Integer>();
        ArrayList<Node> child = new ArrayList<Node>();
        int subSize = 1;
        int getKey = 0;
        Node parent;

        Node(){ }
        Node(int x){
            keys.add(x);
        }

        private void setAsParent(){
            for(int i = 0; i < child.size(); i++){
                child.get(i).parent = this;
            }
        }

        private Node searchIns(int x){
            for(int i: keys){
                if(x == i){
                    root.reverseLoop(x);
                    return null;
                }

            }
            if(isLeaf()){
                return input(x);
            }

            for(int i = 0; i < keys.size(); i++){
                if(x < keys.get(i) && x < keys.get(keys.size()-1)) {
                    this.subSize++;
                    return this.child.get(i).searchIns(x);
                }

                if(x > keys.get(keys.size() -1) && x > keys.get(i)) {
                    this.subSize++;
                    return this.child.get(child.size() - 1).searchIns(x);
                }
            }
            return this;
        }

        private Node select(int x){

            for(int i = 0; i < keys.size(); i++){
                if(isLeaf()){
                    for(int j = 0; j < keys.size(); j++){
                        if(x == j)
                            this.getKey = j;
                    }
                    return this;
                }

                int r = this.child.get(i).subSize;

                if(x == r){
                    return this;
                }

                if(isFull()) {
                    int r2 = r + child.get(i + 1).subSize + 1;

                    if (x == r2) {
                        if (x == r2)
                            this.getKey++;
                        return this;
                    }
                    if(x > r2)
                        return child.get(i+2).select(x-r2-1);
                }

                if(x < r){
                    return child.get(i).select(x);
                }

                else if(x > r){
                    return child.get(i+1).select(x-r-1);
                }
            }
            return this;
        }

        private int index(){
            return this.keys.get(getKey);
        }


        private void reverseLoop(int x){
            if(!isLeaf()) {
                for (int i = 0; i < keys.size(); i++) {
                    if (x < keys.get(i) && x < keys.get(keys.size() - 1)) {
                        this.subSize--;
                        this.child.get(i).reverseLoop(x);
                    }

                    if (x > keys.get(keys.size() - 1) && x > keys.get(i)) {
                        this.subSize--;
                        this.child.get(child.size() - 1).reverseLoop(x);
                    }
                }
            }
        }

        private Node search(int x){
            for(int i: keys){
                if(x == i)
                    return this;
            }
            if(isLeaf()){
                return null;
            }

            for(int i = 0; i < keys.size(); i++){
                if(x <= keys.get(i) && x <= keys.get(keys.size()-1)) {
                    return this.child.get(i).search(x);
                }

                if(x >= keys.get(keys.size() -1) && x >= keys.get(i)) {
                    return this.child.get(child.size() - 1).search(x);
                }
            }

            return this;
        }

        private Node input(int x){
            if (isFull()) {
                sortKeys(x);
                split();
            } else {
                sortKeys(x);
            }
            return this;
        }

        private void split(){
            if(parent == null){
                Node r = new Node(keys.get(1));
                keys.remove(1);
                r.subSize = this.subSize;
                this.subSize--;
                r.child.add(this);
                Node right = new Node(keys.get(1));
                keys.remove(1);
                this.subSize--;
                r.child.add(right);
                root = r;
                root.setAsParent();
                for(Node i: child){

                }
                this.redistribute();

            }
            else{
                boolean preCondition = parent.isFull();

                parent.sortKeys(keys.get(1));
                parent.subSize--;
                keys.remove(1);
                this.subSize--;
                parent.child.add(parent.child.indexOf(this)+1, new Node(keys.get(1)));
                keys.remove(1);
                this.subSize--;
                parent.setAsParent();

                if(!preCondition){
                    this.redistribute();
                }

                else{
                    this.redistribute();
                    parent.split();
                }
            }
        }
        private ArrayList<Integer> sortKeys(int x){

            if (x < keys.get(0)) {
                keys.add(0, x);
            }
            else{
                if(x < keys.get(keys.size()-1))
                    keys.add(keys.size()-1, x);
                else
                    keys.add(x);
            }
            this.subSize++;
            return keys;
        }

        private void redistribute(){
            for(int i = child.size()/2; i < child.size(); i++){
                this.parent.child.get(parent.child.indexOf(this)+1).child.add(child.get(i));
                this.parent.child.get(parent.child.indexOf(this)+1).setAsParent();
                this.parent.child.get(parent.child.indexOf(this)+1).subSize += child.get(i).subSize;
            }

            for(int i = 2; i <= child.size(); i++){
                this.subSize -= child.get(child.size()-1).subSize;
                child.remove(child.size()-1);
            }
        }

        private boolean isLeaf(){
            return (child.isEmpty());
        }

        private boolean isFull(){
            return (keys.size() >= 2);
        }

    }

}
