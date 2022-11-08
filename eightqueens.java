package com.company;

import java.util.Arrays;

public class eightqueens {

    public static boolean isValid(int[][] board, int row, int col) {
        if (!isValidRow(board, row, col)) {
            return false;
        }
        /*
        if (!isValidCol(board, num, row, col)) {
            return false;
        }*/
        if (!isValidUpperDiag(board, row, col)) {
            return false;
        }
        if (!isValidLowerDiag(board, row, col)) {
            return false;
        }
        return true;
    }

    public static boolean isValidRow(int[][] board, int row, int col) {
        //modify the col im checking to see of we are in the same row
        //col - i prevents us from being out of bounds
        for (int i = 1; col - i >= 0; i++) {
            //if there is a queen there then not valid row
            if (board[row][col - i] == 1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidUpperDiag(int[][] board, int row, int col) {
        //we still have squares to check so continue
        if (row == 0 || col == 0) {
            return true;
        }
        //https://www.geeksforgeeks.org/n-queen-problem-backtracking-3/ (help from here to set up my for loop)
        int i;
        int j;
        //goes through each square in the upwards direction and checks if there is a queen there
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidLowerDiag(int[][] board, int row, int col) {
        //if we can still have rows to check, then this place is valid
        if (row == board.length - 1 || col == 0) {
            return true;
        }
        int i;
        int j;
        //goes through each square in the downwards direction and checks if there is a queen there
        for (i = row, j = col; j >= 0 && i < board.length; i++, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }
        return true;
    }

    public static boolean solve(int[][] board, int col) {
        //if col is out of bounds then a queen was placed on the last col
        if (col >= board.length) {
            return true;
        }
        for (int row = 0; row < board.length; row++) {
            //checks to see if there is a valid place to put a queen down
            if (isValid(board, row, col)) {
                board[row][col] = 1;
                if (solve(board, col + 1)) {
                    return true;
                } else {
                    board[row][col] = 0;//if not valid then remove queen
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] board = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};
        solve(board, 0);
        for (int[] row : board) {
            System.out.println(Arrays.toString(row));
        }
        //System.out.println(isValid(board,2,5));
    }
}
