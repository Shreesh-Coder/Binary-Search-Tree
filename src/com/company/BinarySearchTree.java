package com.company;




import java.util.*;
import java.util.LinkedList;

public class BinarySearchTree {
    private static class Node{
        int data;
        int lcount;
        Node left;
        Node right;
        public Node(int data){
            this.data = data;
        }
    }

    private static class PairInorder{
        int predecessor;
        int successor;
        PairInorder(){
            predecessor = successor = -1;
        }

        @Override
        public String toString() {
            return "successor: " + successor + "\npredecessor: " + predecessor;
        }
    }

    public static Node search(int ele, Node ptr) {
        if(ptr == null){
            return null;
        }
        if (ptr.data == ele) {
            return ptr;
        }
        if (ptr.data > ele) {
            return search(ele, ptr.left);
        }else{
            return search(ele, ptr.right);
        }
    }

    public static Node insert(Node root, int key) {
        if(root == null){
            root = new Node(key);
            return root;
        }
        if(root.data > key){
            root.left = insert(root.left, key);
        }else if(root.data < key){
            root.right = insert(root.right, key);
        }
        return root;
    }

    public static Node insertLCount(Node root, int key){
        if(root == null){
            return new Node(key);
        }

        if(root.data > key){
            root.left = insertLCount(root.left, key);
            root.lcount++;
        }else if(root.data < key){
            root.right = insertLCount(root.right, key);
        }
        return root;
    }

    //Not working
    public static Node delete(Node root, int key) {
        if (root == null) {
            return null;
        }

        Node searchEle = search(key, root);
        if(searchEle == null){
            return root;
        }
        if(searchEle.left != null && searchEle.right != null){
            Node inorderEle = inorderSuccessorRightSubtree(searchEle);

            if(inorderEle != null) {
                Node preLevelInorder = searchPreviousLevel(root, inorderEle.data);
                searchEle.data = inorderEle.data;

                if (preLevelInorder.left == inorderEle) {
                    preLevelInorder.left = null;
                } else {
                    preLevelInorder.right = null;
                }
            }else{
                searchEle = searchEle.left;
            }
        }else if(searchEle.left != null || searchEle.right != null){
            if(searchEle.left != null){
                searchEle.data = searchEle.left.data;
                searchEle.left = null;
            }else{
                searchEle.data = searchEle.right.data;
                searchEle.right = null;
            }
        }else{
            Node preLevelEle = searchPreviousLevel(root, key);
            if(preLevelEle != null) {
                if (preLevelEle.left == searchEle) {
                    preLevelEle.left = null;
                } else {
                    preLevelEle.right = null;
                }
            }else{
                root = null;
            }
        }

        return root;
    }

    private static Node searchPreviousLevel(Node root, int key){
        if(root == null){
            return null;
        }
        if(root.data == key){
            return null;
        }
        if(root.left != null && root.left.data == key){
            return root;
        }
        if(root.right != null && root.right.data == key){
            return root;
        }

        if(root.data > key) {
            return searchPreviousLevel(root.left, key);
        }else {
            return searchPreviousLevel(root.right, key);
        }
    }

    public static Node inorderSuccessorRightSubtree(Node root){
        if(root.right == null){
            return null;
        }
        root = root.right;
        while(root.left != null){
            root = root.left;
        }
        return root;
    }

    public static Node inorderPredecessorLeftSubtree(Node root){
        if(root.left == null){
            return null;
        }
        root = root.left;
        while(root.right != null){
            root = root.right;
        }
        return root;
    }

    public static void printInorder(Node root){
        if(root == null){
            return;
        }
        printInorder(root.left);
        System.out.print(root.data + " ");
        printInorder(root.right);
    }

    public static Node deleteRecursion(Node root, int key){
        if(root == null){
            return null;
        }
        if(root.data > key){
            root.left = deleteRecursion(root.left, key); // Will delete the node
        }else if(root.data < key){
            root.right = deleteRecursion(root.right, key); // Will delete the node
        }else{
            //one or no child condition
            if(root.left == null){
                return root.right;
            }else if(root.right == null){
                return root.left;
            }
            //root with two child is replace with the its inorder successor
            root.data = minValue(root.right);
            //Delete the inorder successor
            root.right = deleteRecursion(root.right, root.data);
        }
        return root;
    }

    private static int minValue(Node root){
        int minV = root.data;
        while(root.left != null){
            minV = root.left.data;
            root = root.left;
        }
        return minV;
    }

    public static PairInorder inorderPredecessorAndSuccessor(Node root, int key){
        PairInorder pairInorder;
        if(root == null){
            return new PairInorder();
        }

        if(root.data > key){
            pairInorder = inorderPredecessorAndSuccessor(root.left, key);
            if(pairInorder.successor == -1){
                pairInorder.successor = root.data;
            }
            return pairInorder;
        }else if (root.data < key){
            pairInorder =  inorderPredecessorAndSuccessor(root.right, key);
            if(pairInorder.predecessor == -1){
                pairInorder.predecessor = root.data;
            }
            return pairInorder;
        }else{
            pairInorder = new PairInorder();
            Node inorderSuccessor = inorderSuccessorRightSubtree(root);
            Node inorderPredecessor = inorderPredecessorLeftSubtree(root);
            if(inorderPredecessor != null){
                pairInorder.predecessor = inorderPredecessor.data;
            }else{
                pairInorder.predecessor = -1;
            }
            if(inorderSuccessor != null){
                pairInorder.successor = inorderSuccessor.data;
            }else{
                pairInorder.successor = -1;
            }
        }
        return pairInorder;
    }

    private static int ele;

    public static boolean isBST(Node root){
        ele = root.data;
        return bstChecker(root);
    }

    private static boolean bstChecker(Node root){
        if(root == null){
            return true;
        }

        if( bstChecker(root.left)) {
            if(root.data > ele)
                ele = root.data;
            else
                return false;
            return bstChecker(root.right);
        }
        else{
            return false;
        }
    }

    public static Node findLCA(Node root, int ele1, int ele2){

        Node ptr1, ptr2, common = null;
        ptr1 = ptr2 = root;

        while(ptr1 != null && ptr2 != null) {
            if (ptr1 == ptr2) {
                common = ptr1;
            }

            if (ptr1.data > ele1) {
                ptr1 = ptr1.left;
            } else if (ptr1.data < ele1) {
                ptr1 = ptr1.right;
            }

            if (ptr2.data > ele2) {
                ptr2 = ptr2.left;
            } else if (ptr2.data < ele2) {
                ptr2 = ptr2.right;
            } else {
                break;
            }

        }
        if(ptr1 == null && ptr2 == null){
            return null;
        }
        return common;
    }

    public static Node findLCAOptimize(Node root, int ele1 , int ele2){
        if(root == null){
            return null;
        }
        if(root.data > ele1 && root.data > ele2)
            return findLCAOptimize(root.left, ele1, ele2);
        if(root.data < ele1 && root.data < ele2)
            return findLCAOptimize(root.right, ele1, ele2);

        return root;
    }

    public static Node inorderSuccessor(Node root, Node x){
        if(root == null){
            return null;
        }
        Node val = inorderSuccessor(root.left, x);
        if(val == null && root.data > x.data){

            return root;
        }

        if(val != null){
            return val;
        }
        val = inorderSuccessor(root.right, x);
        return val;
    }

    public static Node inorderSuccessorOptimize(Node root, Node x){
        if(x.right != null){
            return inorderSuccessorRightSubtree(x);
        }

        Node succ = null;

        while(root != null){
            if(root.data > x.data){
                succ = root;
                root = root.left;
            }else if(root.data < x.data){
                root = root.right;
            }else{
                break;
            }
        }

        return succ;
    }

    public static Node kthSmallestElement(Node root, int k){
        if(root == null){
            return null;
        }

        LinkedList<Node> stack = new LinkedList<>();
        stack.push(root);
        Node ptr = root;
        int count = 1;
        boolean flag = true;
        while(!stack.isEmpty()){
            if(ptr.left != null && flag){
                stack.push(ptr.left);
                ptr = ptr.left;
                continue;
            }
            ptr = stack.pop();
            flag = false;
            System.out.print(ptr.data + " ");
            if(k == count){
                return ptr;
            }
            count++;
            if(ptr.right != null){
                flag = true;
                stack.push(ptr.right);
                ptr = ptr.right;
            }
        }

        return null;
    }

    public static Node kthSmallestOptimize(Node root, int k){
        if(root == null){
            return null;
        }

        int count = root.lcount + 1;

        if(count == k)
            return root;
        else if(count > k){
            return kthSmallestOptimize(root.left, k);
        }else{
            return kthSmallestOptimize(root.right, k - count);
        }
    }

    public static void mergeTwoBSTs(Node root1, Node root2){
        if(root1 == null || root2 == null){
            return;
        }
        LinkedList<Node> stack1 = new LinkedList<>();
        LinkedList<Node> stack2 = new LinkedList<>();

        stack1.push(root1);
        stack2.push(root2);

        Node ptr1 = root1, ptr2 = root2;
        boolean flag1 = true, flag2 = true;

        while(true){
            if(ptr1.left != null && ptr2.left != null && flag1 && flag2){
                stack1.push(ptr1.left);
                stack2.push(ptr2.left);
                ptr1 = ptr1.left;
                ptr2 = ptr2.left;
                continue;
            }else if(ptr1.left != null && flag1){
                stack1.push(ptr1.left);
                ptr1 = ptr1.left;
                continue;
            }else if(ptr2.left != null && flag2){
                stack2.push(ptr2.left);
                ptr2 = ptr2.left;
                continue;
            }

            if(!stack1.isEmpty() && !stack2.isEmpty()){
                if(stack1.peek().data < stack2.peek().data){
                    ptr1 = stack1.pop();
                    System.out.print(ptr1.data + " ");
                    flag1 = false;
                }else {
                    ptr2 = stack2.pop();
                    System.out.print(ptr2.data + " ");
                    flag2 = false;
                }
            }else if(!stack1.isEmpty()){
                ptr1 = stack1.pop();
                System.out.print(ptr1.data + " ");
                flag1 = false;
            }else if(!stack2.isEmpty()){
                ptr2 = stack2.pop();
                System.out.print(ptr2.data + " ");
                flag2 = false;
            }else{
                break;
            }

            if(ptr1.right != null && !flag1){
                stack1.push(ptr1.right);
                ptr1 = ptr1.right;
                flag1 = true;
            }
            if(ptr2.right != null && !flag2){
                stack2.push(ptr2.right);
                ptr2 = ptr2.right;
                flag2 = true;
            }
        }
    }

    public static void mergeTwoBSTsAlternativeWay(Node root1, Node root2) {
        LinkedList<Node> stack1 = new LinkedList<>();
        LinkedList<Node> stack2 = new LinkedList<>();

        Node ptr1 = root1, ptr2 = root2;

        while (ptr1 != null || ptr2 != null || !stack1.isEmpty() || !stack2.isEmpty()) {
            if(ptr1 != null || ptr2 != null){
                if(ptr1 != null){
                    stack1.push(ptr1);
                    ptr1 = ptr1.left;
                }
                if(ptr2 != null){
                    stack2.push(ptr2);
                    ptr2 = ptr2.left;
                }
            }else{
                if(stack1.isEmpty()){
                    while(!stack2.isEmpty()){
                        ptr2 = stack2.pop();
                        ptr2.left = null;
                        printInorder(ptr2);
                    }
                    return;
                }

                if(stack2.isEmpty()){
                    while(!stack1.isEmpty()){
                        ptr1 = stack1.pop();
                        ptr1.left = null;
                        printInorder(ptr1);
                    }
                    return;
                }

                ptr1 = stack1.pop();
                ptr2 = stack2.pop();

                if(ptr1.data < ptr2.data){
                    System.out.print(ptr1.data + " ");
                    ptr1 = ptr1.right;
                    stack2.push(ptr2);
                    ptr2 = null;
                }else{
                    System.out.print(ptr2.data + " ");
                    ptr2 = ptr2.right;
                    stack1.push(ptr1);
                    ptr1 = null;
                }
            }
        }
    }

    //Not Working Properly
    private static Node nodeTwoSwappedBST(Node root, Node l, Node r) {
        if (root == null)
            return null;
        if (l != null && root.data <= l.data) {
            return root;
        }
        if (r != null && root.data >= r.data) {
            return root;
        }

        Node p1 = nodeTwoSwappedBST(root.left, l, root);
        Node p2 = nodeTwoSwappedBST(root.right, root, r);

        if(ele == root.data){
            if(p1 == null && p2 != null){
                p1 = root;
            }else if (p2 == null && p1 != null){
                p2 = root;
            }
        }

        if (p1 == null || p2 == null) {
            if (p1 == null) {
                return p2;
            } else {
                return p1;
            }
        } else {
            int temp = p1.data;
            p1.data = p2.data;
            p2.data = temp;
        }
        return null;
    }
    //Not Working Properly
    public static void nodeTwoSwappedBST(Node root){
        ele = root.data;
        System.out.println("\n" + root.data);
        nodeTwoSwappedBST(root, null, null);
    }

    public static boolean isBSTAlternative(Node root, Node l, Node r){
        if(root == null)
            return true;
        if(l != null && l.data >= root.data){
            return false;
        }
        if(r != null && r.data <= root.data){
            return false;
        }
        return isBSTAlternative(root.left, l, root ) &&
                isBSTAlternative(root.right, root, r);
    }

    private static Node first, middle, last, prev;

    private static void correctBSTUtil(Node root){
        if(root == null){
            return;
        }

        correctBSTUtil(root.left);
        if(prev != null && prev.data > root.data){
            if(first == null){
                first = prev;
                middle = root; //Needed for the case of adjacent values
            }else{
                last = root;
            }
        }

        prev = root;
        correctBSTUtil(root.right);
    }

    public static void correctBST(Node root){
        first = middle = last = prev = null;

        correctBSTUtil(root);

        if(first != null && last != null){  //Condition of non adjacent
            swap(first, last);
        }else if(first != null && middle != null){ //Condition of adjacent
            swap(first, middle);
        }
    }

    private static void swap(Node A, Node B){
        int temp = A.data;
        A.data = B.data;
        B.data = temp;
    }

    public static PairInorder floorAndCeil(Node root, int key){
        int floor = key - 1, ceil = key + 1;
        Node floorNode = search(floor, root);
        Node ceilNode = search(ceil, root);
        PairInorder pairInorder = new PairInorder();

        if(floorNode == null || ceilNode ==null){
            Node keyNode = search(key, root);
            if(keyNode != null) {
                if(floorNode == null && ceilNode == null){
                    floorNode = ceilNode = keyNode;
                }else if (floorNode == null) {
                    floorNode = keyNode;
                } else{
                    ceilNode = keyNode;
                }
                pairInorder.predecessor = floorNode.data;
                pairInorder.successor = ceilNode.data;
                return pairInorder;
            }else{
                if(floorNode == null && ceilNode == null){
                    pairInorder.successor = pairInorder.predecessor = -1;
                }else if (floorNode == null) {
                    pairInorder.predecessor = -1;
                    pairInorder.successor = ceilNode.data;
                } else{
                    pairInorder.successor = -1;
                    pairInorder.predecessor = floorNode.data;
                }
                return pairInorder;
            }
        }
        pairInorder.predecessor = floorNode.data;
        pairInorder.successor = ceilNode.data;
        return pairInorder;
    }

    public static int ceil(Node root, int key){
        int ceil = key+1;
        if(root == null){
            return -1;
        }
        if(ceil == root.data){
            return root.data;
        }else if(root.data < key){
            return ceil(root.right, key);
        }else if(root.data > key){
            return ceil(root.left, key);
        }else {
            return root.data;
        }
    }

    public static int ceilGFG(Node root, int key){
        if(root == null){
            return -1;
        }

        if(root.data == key){
            return root.data;
        }else if(root.data < key){
            return ceilGFG(root.right, key);
        }

        int ceil = ceilGFG(root.left, key);
        return (ceil >= key) ? ceil : root.data;
    }

    private static Node ceil, floor;

    public static void floorAndCeilGFG(Node root, int key){
        floor = new Node(-1);
        ceil = new Node(-1);
        while(root != null){
            if(root.data == key){
                ceil = floor = root;
                return;
            }else if(root.data < key){
                floor = root;
                root = root.right;
            }else {
                ceil = root;
                root = root.left;
            }
        }
    }

    public static int floor(Node root, int key){
        int floor = key - 1;
        if(root == null){
            return -1;
        }

        if(floor == root.data){
            return root.data;
        }else if(root.data < key){
            return floor(root.right, key);
        }else if(root.data > key){
            return floor(root.left,key);
        }else{
            return root.data;
        }
    }

    public static int floorGFG(Node root, int key){
        if(root == null){
            return -1;
        }

        if(root.data == key){
            return root.data;
        }else if(root.data > key){
            return floorGFG(root.left, key);
        }

        int floor = floorGFG(root.right, key);
        return Math.max(floor, root.data);
    }


    public static boolean isHeightBalanced(Node root){
        if(root == null){
            return true;
        }

        if(Math.abs(height(root.left) - height(root.right)) > 1){
            return false;
        }
        return isHeightBalanced(root.left) && isHeightBalanced(root.right);
    }

    private static boolean isHBalanced;

    public static boolean isBalanced(Node root){
        isHBalanced = true;
        isHeightBalancedOptimize(root);
        return isHBalanced;
    }

    private static int isHeightBalancedOptimize(Node root){
        if(root == null){
            return 0;
        }

        int lHeight = isHeightBalancedOptimize(root.left);
        int rHeight = isHeightBalancedOptimize(root.right);

        if(Math.abs(lHeight - rHeight) > 1){
            isHBalanced = false;
            return -1;
        }

        return 1 + Math.max(lHeight, rHeight);

    }

    public static int height(Node root){
        if(root == null){
            return 0;
        }

        int lHeight = height(root.left);
        int rHeight = height(root.right);

        if(lHeight >= rHeight){
            return lHeight + 1;
        }else{
            return rHeight + 1;
        }
    }

    public static Node singleLinkedListToBalancedBST(LinkedList<Integer> list){
        if(list.isEmpty()){
            return null;
        }

        LinkedList<Integer> listL = new LinkedList<>();
        LinkedList<Integer> listR = new LinkedList<>();

        for(int i = 0; i < list.size()/2; i++){
            listL.add(list.get(i));

            if(list.size()/2 + i + 1 == list.size()) {
                continue;
            }

            listR.add(list.get(list.size() / 2 + i + 1));
        }
        Node root = new Node(list.get(list.size()/2));
        root.left = singleLinkedListToBalancedBST(listL);
        root.right = singleLinkedListToBalancedBST(listR);

        return root;
    }

    final private static LinkedList<Integer> listGlobal = new LinkedList<>();

    public static Node singleLinkedListToBalancedBSTOptimize(){
        return singleLinkedListToBalancedBSTOptimize(listGlobal.size());
    }

    private static Node singleLinkedListToBalancedBSTOptimize(int n){
        if(n == 0){
            return null;
        }
        //Using of Inorder traversing to create a bst.
        Node left = singleLinkedListToBalancedBSTOptimize(n / 2);

        Node root = new Node(listGlobal.pop());
        root.left = left;

        root.right = singleLinkedListToBalancedBSTOptimize(n - n / 2 - 1);

        return root;
    }

    public static void printLevelOrder(Node root){
        if(root == null){
            return;
        }
        LinkedList<Node> stack =  new LinkedList<>();
        stack.add(root);
        while(!stack.isEmpty()){
            Node ptr = stack.remove();
            System.out.print(ptr.data + " ");
            if(ptr.left != null){
                stack.add(ptr.left);
            }
            if(ptr.right != null){
                stack.add(ptr.right);
            }
        }
    }

    private static Node head;

    public static Node doubleLinkedListToBalancedBSTOptimize(){
        int n = length();
        System.out.println(n);
        return doubleLinkedListToBalancedBSTOptimize(n);
    }

    private static Node doubleLinkedListToBalancedBSTOptimize(int n){
        if(n == 0){
            return null;
        }

        Node left = doubleLinkedListToBalancedBSTOptimize(n/2);

        Node root = head;
        root.left = left;
        head = head.right; //here right means next in double linked list.

        root.right = doubleLinkedListToBalancedBSTOptimize(n - n / 2 - 1);

        return root;
    }

    /**
     * Length function for finding the length double linked list.
     * @return Length of double linked list.
     */
    public static int length(){
        int count = 0;
        Node ptr = head;
        while(ptr != null){
            count++;
            ptr = ptr.right; //here right means next in double linked list.
        }
        return count;
    }

    public static void insertAtEndDLL(int data){
        Node newNode = new Node(data);
        newNode.left = newNode.right = null;
        //Here left is previous and right is next in double linked list.
        if(head == null){
            head = newNode;
            return;
        }
        Node ptr = head;
        while(ptr.right != null){
            ptr = ptr.right;
        }
        ptr.right = newNode;
        newNode.left = ptr;
    }

    public static void printDLL(Node root){
        Node ptr = root;
        while(ptr != null){
            System.out.print(ptr.data + " ");
            ptr = ptr.right; //Here right is next in double linked list.
        }
    }

    public static void printDLL(){
        Node ptr = head;
        while(ptr != null){
            System.out.print(ptr.data + " ");
            ptr = ptr.right; //Here right is next in double linked list.
        }
    }

    public static boolean isTripletPresent(Node root){
        LinkedList<Node> queue = new LinkedList<>();
        queue.push(root);

        while(!queue.isEmpty()){
            Node ptr = queue.pop();

            LinkedList<Node> queue1 = new LinkedList<>();
            queue1.push(root);

            while(!queue1.isEmpty()){
                Node ptr1 = queue1.pop();

                Integer sum = ptr.data + ptr1.data;
                Node ele;
                ele = search(-sum,root);
                if(ele != null && ele.data + sum == 0 &&
                        (sum != ptr.data/2 && sum != ptr1.data/2) && ptr.data != ptr1.data){
                    return true;
                }

                if(ptr1.left != null){
                    queue1.push(ptr1.left);
                }
                if(ptr1.right != null){
                    queue1.push(ptr1.right);
                }
            }

            if(ptr.left != null){
                queue.push(ptr.left);
            }
            if(ptr.right != null){
                queue.push(ptr.right);
            }
        }
        return false;
    }

    //May be working or not.
    public static boolean isTripletPresentOptimize(Node root){
        prev = null;
        root = BSTToDLL(root);
        Node last = root;
        while(last.right != null){
            last = last.right;
        }
        for(Node ptr = root; ptr.right.right.right != null; ptr = ptr.right){
            Node firstPtr = ptr.right;
            Node lastPtr = last;
            while(lastPtr != firstPtr){
                int sum = lastPtr.data + firstPtr.data + ptr.data;
                if(sum == 0){
                    return true;
                }else if(sum < 0){
                    firstPtr = firstPtr.right;
                }else{
                    lastPtr = lastPtr.left;
                }
            }
        }
        return false;
    }

    public static Node BSTToDLL(Node root) {
        if (root == null) {
            return null;
        }
        Node head = null;
        head = BSTToDLL(root.left);
        root.left = prev;
        if (prev != null) {
            prev.right = root;
        }else{
            head = root;
        }
        prev = root;
        BSTToDLL(root.right);
        return head;
    }

    public static Node mergeBST(Node root1, Node root2){
        if(root1 == null){
            return root2;
        }else if(root2 == null){
            return root1;
        }else if(root1 == null && root2 == null){
            return null;
        }

        int count = 0;
        Node root = null;
        LinkedList<Node> queue1 = new LinkedList<>();
        LinkedList<Node> queue2 = new LinkedList<>();
        queue1.add(root1);
        queue2.add(root2);
        Node ptr1 = null , ptr2 = null;
        while(!queue1.isEmpty()){
            ptr1 = queue1.pop();

            root = insert(root, ptr1.data);

            if(ptr1.left != null){
                queue1.add(ptr1.left);
            }
            if(ptr1.right != null){
                queue1.add(ptr1.right);
            }
        }

        while(!queue2.isEmpty()){
            ptr2 = queue2.pop();

            root = insert(root, ptr2.data);

            if(ptr2.left != null){
                queue2.add(ptr2.left);
            }
            if(ptr2.right != null){
                queue2.add(ptr2.right);
            }
        }

        return root;
    }

    public static int size(Node root){
        if(root == null){
            return 0;
        }
        return size(root.left) + size(root.right) + 1;
    }

    public static ArrayList<Integer> arrayList;
    private static int iterator;
    public static Node sortedArrayToBST(ArrayList<Integer> arr){
        iterator = 0;
        arrayList = arr;
        return sortedArrayToBSTUtil(arrayList.size());
    }

    private static Node sortedArrayToBSTUtil(int n){
        if(n == 0){
            return null;
        }

        Node left = sortedArrayToBSTUtil(n/2);
        Node root = new Node(arrayList.get(iterator));
        iterator++;
        root.left = left;
        root.right = sortedArrayToBSTUtil(n - n/2 - 1);
        return root;
    }

    public static Node mergeBSTAlternative(Node root1 , Node root2) {
        if (root1 == null && root2 == null) {
            return null;
        } else if (root1 == null) {
            return root2;
        } else if (root2 == null) {
            return root1;
        }

        int[] arr1 = BSTtoSortedArray(root1);
        int[] arr2 = BSTtoSortedArray(root2);
        ArrayList<Integer> arr3 = new ArrayList<>(arr1.length + arr2.length);

        int i = 0, j = 0, k = 0;
        while (k < arr1.length + arr2.length) {
            if (i > arr1.length - 1) {
                arr3.add(arr2[j++]);
            } else if (j > arr2.length - 1) {
                arr3.add(arr1[i++]);
            } else {
                if (arr1[i] > arr2[j]) {
                    arr3.add(arr2[j++]);
                } else {
                    arr3.add(arr1[i++]);
                }
            }
            k++;
        }

        return sortedArrayToBST(arr3);
    }

    public static int [] BSTtoSortedArray(Node root){
        iterator = 0;
        int[] arr = new int[size(root)];
        BSTtoSortedArrayUtil(root, arr);
        return arr;
    }

    private static void BSTtoSortedArrayUtil(Node root, int[] arr){
        if(root == null){
            return;
        }
        BSTtoSortedArrayUtil(root.left, arr);
        arr[iterator++] = root.data;
        BSTtoSortedArrayUtil(root.right, arr);
    }

    private static void BSTtoSortedArrayUtil(Node root,ArrayList<Integer> arr){
        if(root == null){
            return;
        }
        BSTtoSortedArrayUtil(root.left, arr);
        arr.add(iterator++, root.data);
        BSTtoSortedArrayUtil(root.right, arr);
    }
    public static ArrayList<Integer> BTtoArray(Node root){
        iterator = 0;
        ArrayList<Integer> arr = new ArrayList<>(size(root));
        BSTtoSortedArrayUtil(root, arr);
        return arr;
    }

    private static void BTtoBST(Node root){

        ArrayList<Integer> arr = BTtoArray(root);
        Collections.sort(arr);
        iterator = 0;
        copyToBT(root, arr);
    }

    private static void copyToBT(Node root, ArrayList<Integer> arr){
        if(root == null){
            return;
        }
        copyToBT(root.left, arr);
        root.data = arr.get(iterator++);
        copyToBT(root.right, arr);
    }


    public static void changeKey(Node root, int oldKey, int newKey){
        deleteRecursion(root, oldKey);
        insert(root, newKey);
    }

    public static void main(String[] args) {
       /* Node root = new Node(8);
        //Layer 2
        root.left = new Node(3);
        root.right = new Node(10);
        //Layer 3
        root.left.left = new Node(1);
        root.left.right = new Node(6);
        root.right.right = new Node(14);
        //Layer 4
        root.left.right.left = new Node(4);
        root.left.right.right = new Node(7);
        root.right.right.left = new Node(13);*/

        Node root1 = new Node(50);
        //Layer 2
        root1.left = new Node(30);
        root1.right = new Node(70);
        //Layer 3
        root1.left.left = new Node(20);
        root1.left.right = new Node(40);
        root1.right.left = new Node(60);
        root1.right.right = new Node(80);

        /*Node root2 = new Node(35);
        //Layer 2
        root2.left = new Node(3);
        root2.right = new Node(39);
        //Layer 3
        root2.left.left = new Node(2);
        root2.left.right = new Node(27);
        root2.right.left = new Node(38);
        root2.right.right = new Node(40);
        //Layer 4
        root2.left.left.left = new Node(1);
        root2.left.right.left = new Node(10);
        root2.left.right.right = new Node(34);
        root2.right.left.left =  new Node(37);
*/

        Node root3 = null;
        root3 = insertLCount(root3, 35);
        root3 = insertLCount(root3, 3);
        root3 = insertLCount(root3, 39);
        root3 = insertLCount(root3, 2);
        root3 = insertLCount(root3, 27);
        root3 = insertLCount(root3, 38);
        root3 = insertLCount(root3, 40);
        root3 = insertLCount(root3, 1);
        root3 = insertLCount(root3, 10);
        root3 = insertLCount(root3, 34);
        root3 = insertLCount(root3, 37);

        Node root4 = null;
        root4 = insert(root4, 5);
        root4 = insert(root4, 3);
        root4 = insert(root4, 6);
        root4 = insert(root4, 2);
        root4 = insert(root4, 4);

        Node root5 = null;
        root5 = insert(root5, 2);
        root5 = insert(root5, 1);
        root5 = insert(root5, 3);
        root5 = insert(root5, 7);
        root5 = insert(root5, 6);

        Node root6 = new Node(10);
        //Layer 2
        root6.left = new Node(5);
        root6.right = new Node(8);
        //Layer 3
        root6.left.left = new Node(2);
        root6.left.right = new Node(20);

        Node root7 = new Node(11);
        //Layer 2
        root7.left = new Node(3);
        root7.right = new Node(17);
        //Layer 3
        root7.left.right = new Node(4);
        root7.right.left = new Node(10);

        Node root8 = new Node(1);
        //Layer 2
        root8.right = new Node(52);
        //Layer 3
        root8.right.left = new Node(5);
        root8.right.right = new Node(62);
        //Layer 4
        root8.right.left.left = new Node(4);
        root8.right.left.right = new Node(26);
        root8.right.right.left = new Node(2);
        root8.right.right.right = new Node(66);
        //Layer 5
        root8.right.left.left.left = new Node(58);

        Node root9 = new Node(2);
        //Layer 2
        root9.left = new Node(1);
        root9.right = new Node(83);
        //Layer 3
        root9.left.right = new Node(3);
        root9.right.left = new Node(47);
        root9.right.right = new Node(96);
        //Layer 4
        root9.left.right.left = new Node(4);
        root9.right.left.left = new Node(6);

        Node root10 = null;
        root10 = insert(root10, 8);
        root10 = insert(root10, 4);
        root10 = insert(root10, 12);
        root10 = insert(root10, 2);
        root10 = insert(root10, 6);
        root10 = insert(root10, 10);
        root10 = insert(root10, 14);

        Node root11 = new Node(9);
        //Layer 2
        root11.left = new Node(7);
        //Layer 3
        root11.left.left = new Node(8);
        root11.left.right = new Node(10);
        //Layer 4
        root11.left.left.right = new Node(6);
        root11.left.right.left = new Node(4);
        //Layer 5
        root11.left.left.right.left = new Node(6);
        root11.left.left.right.right = new Node(10);
        root11.left.right.left.right = new Node(8);

        //Not BST
        Node root12 = new Node(9);
        //Layer 2
        root12.left = new Node(5);
        root12.right = new Node(5);
        //Layer 3
        root12.left.left = new Node(5);
        root12.left.right = new Node(8);
        root12.right.left = new Node(5);
        root12.right.right = new Node(7);
        //Layer 4
        root12.left.left.left = new Node(9);
        root12.left.left.right = new Node(6);
/*
        Node ptr = search(123, root1);
        if(ptr != null){
            System.out.println("ele is found");
        }else{
            System.out.println("ele is not found");
        }*/

//        System.out.print("Before deletion: ");
//        printInorder(root1);

//        root1 = delete(root1, 40);
//        root1 = delete(root1, 30);
//        root1 = delete(root1, 80);
//        root1 = delete(root1, 30);
//        root1 = delete(root1, 60);
//        root1 = delete(root1, 50);
//        root1 = delete(root1, 70);
//        root1 = delete(root1, 20);
//        root1 = delete(root1, 30);

//        root1 = deleteRecursion(root1, 60);
//        root1 = deleteRecursion(root1, 50);
//        root1 = deleteRecursion(root1, 40);
//        root1 = deleteRecursion(root1, 30);

//        System.out.print("\nAfter deletion:  ");
//        printInorder(root1);
//////////////////////////////////////////////////////////////////////////////////
     /*   System.out.println(inorderPredecessorAndSuccessor(root1,100));
        System.out.println(isBST(root1));*/
//////////////////////////////////////////////////////////////////////////////////
/*
        long startTime = System.nanoTime();
        Node lca =  findLCAOptimize(root1, 20, 50);
        long endTime = System.nanoTime();

        long elapsedTime = endTime - startTime;

        if(lca!= null)
            System.out.println("LCA is: " + lca.data);
        else
            System.out.println("Ele is not found");

        System.out.println("Elapsed Time is: " + elapsedTime);*/


        /*long startTime = System.nanoTime();
        System.out.println("Inorder successor is: " + inorderSuccessor(root1, root1.left.right).data);
        long endTime = System.nanoTime();

        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed Time is: " + elapsedTime);*/
       /* printInorder(root4);
        System.out.println();
        printInorder(root5);
        System.out.println();
        System.out.println(root3.lcount);
        System.out.println("Kth smallest element is: " + kthSmallestOptimize(root3,6).data);
        mergeTwoBSTs(root1, root3);
        System.out.println();
        mergeTwoBSTsAlternativeWay(root1, root3);

        System.out.println();
        printInorder(root7);
        correctBST(root7);
        System.out.println();
        printInorder(root7);
        System.out.println();*/

     /*   printInorder(root10);
//        System.out.println(floorAndCeil(root10,14));
        for (int i = 0; i < 16; i++) {
            floorAndCeilGFG(root10, i);
            System.out.println("\n" + i + " " + floor.data + " " + ceil.data
                    + " " + floorGFG(root10, i));
        }

        System.out.println("isHeightBalanced: " + isHeightBalanced(root9));
        System.out.println("isHeightBalanced: " + isBalanced(root9));
        System.out.println();
        printLevelOrder(root1);

        LinkedList<Integer> list = new LinkedList<>();
        list.add(2);
        list.add(4);
        list.add(6);
        list.add(8);
        list.add(10);
        list.add(12);
        list.add(14);

        LinkedList<Integer> list1 = new LinkedList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);

        LinkedList<Integer> list2 = new LinkedList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(6);

        for(int ele : list1){
            listGlobal.add(ele);
        }
        insertAtEndDLL(1);
        insertAtEndDLL(2);
        insertAtEndDLL(3);
        insertAtEndDLL(4);
        insertAtEndDLL(5);
        insertAtEndDLL(6);
        insertAtEndDLL(7);
        System.out.print("\nDLL is ");
        printDLL();

        Node root13 = doubleLinkedListToBalancedBSTOptimize();
        System.out.println();
        printLevelOrder(root13);
        System.out.println(isBalanced(root13));
        printInorder(root13);*/

        Node root14 = null;
        root14 = insert(root14, -13);
        root14 = insert(root14, -8);
        root14 = insert(root14, 6);
        root14 = insert(root14, 7);
        root14 = insert(root14, 13);
        root14 = insert(root14, 14);
        root14 = insert(root14, 15);

        Node root15 = null;
        root15 = insert(root15, -29);
        root15 = insert(root15, 10);
        root15 = insert(root15, -46);
        root15 = insert(root15, -22);
        root15 = insert(root15, -23);
        root15 = insert(root15, 0);
        root15 = insert(root15, -2);
        root15 = insert(root15, 6);
        root15 = insert(root15, -48);
        root15 = insert(root15, 44);
        root15 = insert(root15, 47);
        root15 = insert(root15, 49);
        root15 = insert(root15, -7);
        root15 = insert(root15, -11);
        root15 = insert(root15, -48);

        Node root16 = null;
        root16 = insert(root16, 2);
        root16 = insert(root16, 1);
        root16 = insert(root16, 3);
//        root16 = new Node(2);
//        root16.left = new Node(1);
//        root16.right = new Node(3);

//        System.out.println("Is triplet present: " + isTripletPresent(root14));
//        System.out.println("Is triplet present Optimize: " + isTripletPresentOptimize(root14));

//        root16 = BSTToDLL(root15);
//        printDLL(root15);

//        Node root17 = mergeBST(root1, root15);
//        printInorder(root17);
//        System.out.println();
//        printInorder(mergeBSTAlternative(root1, root15));

        /* for(int ele : BSTtoSortedArray(root15)){
            System.out.print(ele + " ");
        }*/

       /* ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(7);
        arrayList.add(10);
        arrayList.add(45);

        printInorder(sortedArrayToBST(arrayList));*/

        Node root18 = new Node(10);
        //Layer 2
        root18.left = new Node(2);
        root18.right = new Node(7);
        //Layer 3
        root18.left.left = new Node(8);
        root18.left.right = new Node(4);


        printInorder(root14);
//        BTtoBST(root18);
        changeKey(root14, 6,0);
//        deleteRecursion(root14, 6);
        System.out.println();
        printInorder(root14);

    }
}
