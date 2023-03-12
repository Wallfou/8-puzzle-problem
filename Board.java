import java.util.*;


public class Board {
    private final char[] board;
    private int openR = -1;
    private int openC = -1;
    private int n;
    private List<Board> neighbour = new ArrayList<>();

    public Board(int[][] blocks) {
        n = blocks[0].length;
        board = new char[n * n];

        //converting the input into the board and indentifying
        //the zero block
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[n * i + j] = (char)blocks[i][j];
                if (board[n * i + j] == 0) {
                    openR = i;
                    openC = j;
                }
            }
        }
    }

    // shifts the desired block to the desired location by using a temporary board
    private Board shift(char[] board, int openR, int openC, int shiftedR, int shiftedC) {
        int[][] tempboard = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n ; j++) {
                tempboard[i][j] = (int)board[n * i + j];
            }
        }
        int shift = tempboard[openR][openC];
        tempboard[openR][openC] = tempboard[shiftedR][shiftedC];
        tempboard[shiftedR][shiftedC] = shift;
        Board result = new Board(tempboard);
        return result;
    }

    //manhattan priority function finds the sum of the number of steps
    //each block needs to get to its goal position.
    public int manhattan() {
        int sum = 0;
        int target, goalRow, goalCol;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[n * i + j] == (char)0 || board[n * i + j] == (char)n * i + j + 1) continue;
                target = (int)board[n * i + j];
                goalRow = (target - 1) / n;
                goalCol = (target - 1) % n;
                sum += Math.abs((goalRow - i)) + Math.abs((goalCol - j));
            }
        }
        return sum;
    }

    public boolean goal() {
        return manhattan() == 0;
    }

    private boolean inBound(int row, int col) {
        return (row < n && row >= 0 && col >= 0 && col < n);
    }

    //returning a list of neighboring boards
    public Iterable<Board> neighbors() {
        if(neighbour.isEmpty()) {
            if (inBound(openR + 1, openC))
                neighbour.add(shift(board, openR, openC, openR + 1, openC));
            if (inBound(openR, openC + 1))
                neighbour.add(shift(board, openR, openC + 1, openR, openC));
            if (inBound(openR, openC - 1))
                neighbour.add(shift(board, openR, openC - 1, openR, openC));
            if (inBound(openR - 1, openC))
                neighbour.add(shift(board, openR - 1, openC, openR, openC));
        }
        return neighbour;
    }

    public Board twin() {
            if(board[0] != (char)0 && board[n * n - 1] != (char)0) {
                return shift(board,0,0,n-1,n-1);
            }
            else if(board[0] == (char)0) {
                return shift(board,0,1,n-1,n-1);
            }
            else if(board[n * n - 1] == (char)0) {
                return shift(board,0,0,n-2,n-1);
            }
            return null;

    }

    //printing the board
    public String toString() {
        String str = n + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                str += ((int)board[n * i + j]) + " ";
            }
            str += "\n";
        }
        return str;
    }
}
