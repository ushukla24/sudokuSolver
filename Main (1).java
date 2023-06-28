import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;

class Main
{
  public static void main(String[] args)
  throws
    FileNotFoundException,
    IOException
  {
    while (true)
    {
      System.out.print("\033[H\033[2J");

      Scanner scan = new Scanner(System.in);
      
    System.out.println("Welcome to my sudoku solver!! :D \n"); 
      
      int[][] board = new int[9][9];
      System.out.print("Enter the board you would like to be solved by entering each row as one continuous input, with periods for unknown values.\n\n");
      for (int a = 0; a < board.length; ++a)
      {
        String row = scan.nextLine();
        for (int b = 0; b < board[a].length; ++b)
          board[a][b] = row.charAt(b) == '.'
			? 0
            : row.charAt(b) - '0';
      }

      System.out.println("\n");
      DisplayBoard(board);
      System.out.print("\n\nSolving...\n\n\n");

      ArrayList<ArrayList<Integer> > backtracks = new ArrayList();
      boolean canSolve = true;
      long timesLooped = 0;
      for (int[] cords = FindBlank(board); !CheckBoard(board); ++timesLooped)
      {
        boolean backtrack = true;
        for (int a = board[cords[0]][cords[1]] + 1; a <= 9 && backtrack; ++a)
        {
          board[cords[0]][cords[1]] = a;
          if (CheckRow(board, cords[0]) && CheckCol(board, cords[1]) && CheckCell(board))
            backtrack = false;
        }

        if (backtrack)
        {
          if (backtracks.isEmpty())
          {
            canSolve = false;
            break;
          }

          board[cords[0]][cords[1]] = 0;

          cords[0] = backtracks.get(backtracks.size() - 1).get(0);
          cords[1] = backtracks.get(backtracks.size() - 1).get(1);

          backtracks.remove(backtracks.size() - 1);
        }
        else
        {
          ArrayList<Integer> temp = new ArrayList();
          temp.add(cords[0]);
          temp.add(cords[1]);

          backtracks.add(temp);
          cords = FindBlank(board);
        }
      }

      DisplayBoard(board);

      System.out.print("\n\n"
        + (canSolve
          ? "Complete!\nThis board took " + timesLooped + " loops to solve."
          : "This board is not solvable :(")
        + "\nPress to restart :D");

      Scanner scanner = new Scanner(System.in);
      scanner.nextLine();
    }
  }

  static int[] FindBlank(int[][] board)
  {
    for (int a = 0; a < board.length; ++a)
    {
      for (int b = 0; b < board[a].length; ++b)
      {
        if (board[a][b] == 0)
        {
          int[] cords = { a, b };
          return cords;
        }
      }
    }

    return new int[1];
  }

  static boolean CheckRow(int[][] board, int row)
  {
    HashSet<Integer> rowNums = new HashSet();
    for (int a = 0; a < 9; ++a)
    {
      int saveSize = rowNums.size();
      rowNums.add(board[row][a]);
      if (saveSize == rowNums.size() && board[row][a] != 0)
        return false;
    }

    return rowNums.contains(0) || rowNums.size() == 9;
  }

  static boolean CheckCol(int[][] board, int col)
  {
    HashSet<Integer> colNums = new HashSet();
    for (int a = 0; a < 9; ++a)
    {
      int saveSize = colNums.size();
      colNums.add(board[a][col]);
      if (saveSize == colNums.size() && board[a][col] != 0)
        return false;
    }

    return colNums.contains(0) || colNums.size() == 9;
  }

  static boolean CheckCell(int[][] board)
  {
    for (int a = 0; a < 3; ++a)
    {
      for (int b = 0; b < 3; ++b)
      {
        HashSet<Integer> boardNums = new HashSet();
        for (int c = a * 3; c < (a * 3) + 3; ++c)
        {
          for (int d = b * 3; d < (b * 3) + 3; ++d)
            boardNums.add(board[c][d]);
        }

        if (!boardNums.contains(0) && boardNums.size() != 9)
          return false;
      }
    }

    return true;
  }

  static boolean CheckBoard(int[][] board)
  {
    for (int a = 0; a < 9; ++a)
    {
      for (int b = 0; b < 9; ++b)
      {
        if (board[a][b] == 0)
          return false;
      }
    }

    return true;
  }

  static void DisplayBoard(int[][] board)
  {
    String GRN = "\033[32;1m";
    String WHT = "\033[0;1m";
    String CLR = "\033[0m";

    for (int a = 0; a < 9; ++a)
    {
      if (a == 3 || a == 6)
      {
        for (int b = 0; b < 11; ++b)
          System.out.print(WHT + "- " + CLR);
        System.out.println();
      }

      for (int b = 0; b < 9; ++b)
      {
        if (b == 3 || b == 6)
          System.out.print(WHT + "| " + CLR);

        System.out.print(
          (
            board[a][b] == 0
            ? " "
            : GRN + board[a][b] + CLR
          )
          + " ");
      }

      System.out.println();
      System.out.flush();
    }
  }
}