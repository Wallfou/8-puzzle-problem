import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {

    private SearchNode curr;
    private boolean solvable = false;
    private List<Board> dequeue = new ArrayList<>();

    public Solver(Board in) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPq = new MinPQ<>();
        curr = new SearchNode(in, null);
        SearchNode twin = new SearchNode(in.twin(), null);
        pq.insert(curr);
        twinPq.insert(twin);

        while (!curr.board.goal() && !twin.board.goal()) {
            curr = pq.delMin();
            twin = twinPq.delMin();
            for (Board child : curr.board.neighbors()) {
                if (curr.preNode == null || !child.equals(curr.preNode.board)) pq.insert(new SearchNode(child, curr));
            }
            for (Board child : twin.board.neighbors()) {
                if (twin.preNode == null || !child.equals(twin.preNode.board)) twinPq.insert(new SearchNode(child, twin));
            }
        }
        if (curr.board.goal()) solvable = true;
    }

    private static class SearchNode implements Comparable<SearchNode> {
        Board board;
        int manhattan;
        int depth = 0;
        SearchNode preNode;
        int value;
        public SearchNode(Board board, SearchNode preNode) {
            this.board = board;
            this.manhattan = board.manhattan();
            if (preNode != null) {
                this.depth = preNode.depth + 1;
                this.preNode = preNode;
            }
            this.value = this.depth + this.manhattan;
        }

        public int compareTo(SearchNode n) {
            if (this.value < n.value) return -1;
            else if (this.value > n.value) return 1;
            else return 0;
        }
    }

    public Iterable<Board> solution() {
        SearchNode temp = new SearchNode(curr.board, curr.preNode);
        if(isSolvable()) {
            while (temp != null) {
                dequeue.add(temp.board);
                temp = temp.preNode;
            }
            Collections.reverse(dequeue);
            return dequeue;
        }
        else return null;
    }

    public boolean isSolvable() {
        return solvable;
    }
    
    public static void main(String[] args) {
        int[][] tiles = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board board = new Board(tiles);
        Solver solution = new Solver(board);
        for (Board b : solution.solution())
            System.out.println(b);
    }
}
