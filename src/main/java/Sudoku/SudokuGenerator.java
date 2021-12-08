
package Sudoku;

/**
 *
 * @author Ashfia
 */

import java.util.Arrays;
// Some methods are reference from GeeksForGeeks(Contributed by Ankur Trisal) Sudoku
/*
 * This class is the main class which generates a multidimensional array that contains numbers of sudoku grid (according to the selected level)
 * It contains methods to replaces some of the digits with zeros to indicate empty cells for the user to input in
 * And also to check the grid with user's inputs to validate if it is right or wrong
 */
public class SudokuGenerator
{
    public int[][] gridArray;
    public int N;              // Number of columns and rows
    public int SRN;            // Square root of N (For inner box)
    public int MD;             // Number Of missing digits
 
    // Constructor
    public SudokuGenerator(int N, int MD)
    {
        this.N = N;
        this.MD = MD;
 
        // Set square root of N according to the selected level
        switch(N){
            case 4:
                SRN = 2;
                break;
            case 9:
                SRN = 3;
                break;
            case 16:
                SRN = 4;
                break;
        }
        
        gridArray = new int[N][N];
    }
    
    // This method fills the grid with values respective to the level
    public void fillValues()
    {
        // Fill the diagonal of SRN x SRN matrices
        fillDiagonal();
        
        // Fill remaining blocks
        int i=0;
        while(i<=0){
            if(fillRemaining(0, SRN) == true){
                i=1;
            } 
            else{                           // Condition for if the generated grid is invalid
                for (int r=0; r<N; r++){
                    for(int c=0; c<N; c++){
                        gridArray[r][c]=0;
                    }
                }
                fillDiagonal();
            }
        }
        
        // Remove Random MD digits to provide input buttons
        removeDigits();
    }
 
    // Fills the diagonal SRN x SRN boxes
    public void fillDiagonal()
    {
        for(int i = 0; i<N; i=i+SRN)
 
            // for diagonal box, start coordinates->i==j
            fillBox(i, i);
    }
 
    // Fills a SRN x SRN grid
    public void fillBox(int row,int col)
    {
        int num;
        for(int i=0; i<SRN; i++)
        {
            for(int j=0; j<SRN; j++)
            {
                do
                {
                    num = (int) Math.floor((Math.random()*N+1));        // Generates a random number between 1-9(9x9) or 1-4(4x4)
                }
                while (!emptyInBox(row, col, num));                     // Checks if box at row and col contains the same num
 
                gridArray[row+i][col+j] = num;
            }
        }
    }
    
    // Returns false if the inner box array contains the same num
    public boolean emptyInBox(int rowStart, int colStart, int num)
    {
        for(int i = 0; i<SRN; i++)
            for(int j = 0; j<SRN; j++)
                if (gridArray[rowStart+i][colStart+j]==num)
                    return false;
        return true;
    }
    
 
    // A recursive function to fill remaining grid array
    public boolean fillRemaining(int i, int j)
    {
        if(i<N-1 && j>=N)
        {
            i = i + 1;
            j = 0;
        }
        if(i>=N && j>=N)
            return true;
 
        if(i < SRN)
        {
            if(j < SRN)
                j = SRN;
        }
        else if(i < N-SRN)
        {
            if(j==(int)(i/SRN)*SRN)
                j =  j + SRN;
        }
        else
        {
            if(j == N-SRN)
            {
                i = i + 1;
                j = 0;
                if (i>=N)
                    return true;
            }
        }
        
        for(int num = 1; num<=N; num++)
        {
            if(checkIfSafe(i, j, num))
            {
                gridArray[i][j] = num;
                if(fillRemaining(i, j+1)){
                    return true;
                }
                gridArray[i][j] = 0;
            }
        }
        return false;
    }
    
    // Check if safe to put num in cell
    public boolean checkIfSafe(int i,int j,int num)
    {
        return (emptyInRow(i, num) && emptyInCol(j, num) && emptyInBox(i-i%SRN, j-j%SRN, num));
    }
 
    // Check in the row for empty cell
    public boolean emptyInRow(int i,int num)
    {
        for(int j = 0; j<N; j++)
           if(gridArray[i][j] == num)
                return false;
        return true;
    }
 
    // Check in the column for empty cell
    public boolean emptyInCol(int j,int num)
    {
        for(int i = 0; i<N; i++)
            if(gridArray[i][j] == num)
                return false;
        return true;
    }
    
    // Removes digits randomly for user input
    public void removeDigits()
    {
        int count = MD;
        int min = 0;
        int max = N-1;
        while (count != 0)
        {   
            // Gets random index i for rows and j for columns
            int i = (int) Math.floor((Math.random() * ((max - min) + 1)) + min);
            int j = (int) Math.floor((Math.random() * ((max - min) + 1)) + min);
            
            if(gridArray[i][j] != 0)
            {
                count--;
                gridArray[i][j] = 0;
            }
        }
    }
    
    // Generates a new grid array
    public void getNewGridArray()
    {
        for(int r=0;r<N;r++){
            for(int c=0;c<N;c++){
                gridArray[r][c]=0;
            }
        }
        fillValues();
    }
    
    // Returns the result true if the Sudoku puzzle is filled correctly and false if it is filled wrong
    public boolean areInputsCorrect(int[][] inputtedGrid, int grid) 
    {
        for(int i = 0; i < grid; i++) {
            int[] column = new int[grid];
            int[] box = new int[grid];
            int[] row = inputtedGrid[i].clone();
            
            for(int j = 0; j < grid; j ++) {
                column[j] = inputtedGrid[j][i];                                                       // Gets the values from rows
                box[j] = inputtedGrid[(i / SRN) * SRN + j / SRN][i * SRN % grid + j % SRN];           // Gets the values at the inner boxes
            }
            if(!(check(column) && check(row) && check(box)))
                return false;
        }
        return true;
    }
    
    // Checks if the numbers are valid in their positions
    public boolean check(int[] check) 
    {
        int i = 1;
        Arrays.sort(check);
        for(int number : check) {
            if(number != i++)
                return false;
        }
        return true;
    }
    
    // Driver main method
    public static void main(String[] args)
    {
        new SudokuUI().setVisible(true);
    }
}
