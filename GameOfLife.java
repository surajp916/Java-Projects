package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(file); 
        int row = StdIn.readInt(); 
        int col = StdIn.readInt(); 
        grid = new boolean[row][col]; 
        for(int r = 0; r < row;  r++) {
            for(int c = 0; c < col; c++) {
                grid[r][c] = StdIn.readBoolean(); 
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        // WRITE YOUR CODE HERE
        if(grid[row][col] == true) {
            return true; 
        }
        return false; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        // WRITE YOUR CODE HERE
        for(int r = 0; r < grid.length; r++) {
            for(int c = 0; c < grid[0].length; c++) {
                if(grid[r][c] == true) {
                    return true; 
                }
            }
        }
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {

        // WRITE YOUR CODE HERE
        int neighborcount = 0;
        int rowlen = grid.length; 
        int collen = grid[0].length; 
        for(int r = -1; r <= 1; r++) {
            for(int c = -1; c <= 1; c++) {
                if(!(r == 0 && c == 0)) { //make sure it does not count itself 
                    int rows = (row + r + rowlen) % rowlen; 
                    int cols = (col + c + collen) % collen; 

                    if(grid[rows][cols]) {
                        neighborcount++; 
                    }
                }
            }
        }

        return neighborcount; 
    }
            

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        // WRITE YOUR CODE HERE
       int rowlen = grid.length; 
       int collen = grid[0].length; 
       boolean[][] newgrid = new boolean[rowlen][collen]; 

       for(int r = 0; r < rowlen; r++) {
            for(int c = 0; c < collen; c++) {
                int alive = numOfAliveNeighbors(r, c); 
                if(getCellState(r, c) && (alive < 2 || alive > 3)) {
                    newgrid[r][c] = false; 
                }
                else if((getCellState(r, c) == false) && alive == 3) {
                    newgrid[r][c] = true; 
                }
                else if(getCellState(r, c) && (alive == 2 || alive == 3)) {
                    newgrid[r][c] = true; 
                }
                else {
                    newgrid[r][c] = false; 
                }
            }
       }

        return newgrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {

        // WRITE YOUR CODE HERE
        boolean[][] newGrid = computeNewGrid();  
        int rows = grid.length; 
        int cols = grid[0].length; 
        totalAliveCells = 0;    

        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                grid[r][c] = newGrid[r][c]; 
                if(grid[r][c]) {
                    totalAliveCells++; 
                }
            }
        }
        
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        // WRITE YOUR CODE HERE
        for(int i = 0; i < n; i++) {
            nextGeneration();  
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        // WRITE YOUR CODE HERE
        grid = getGrid(); 

        ArrayList<Integer> unionTree = new ArrayList<Integer>(); 

        int rowlen = grid.length; 
        int collen = grid[0].length; 
        
        int right; 
        int left; 
        int up; 
        int down; 

        WeightedQuickUnionUF quickF = new WeightedQuickUnionUF(rowlen, collen); 

        for(int r = 0; r < rowlen; r++) {
            for(int c = 0; c < collen; c++) {
                boolean alive = getCellState(r, c); 
                if(alive) {
                    right = c + 1; 
                    if(c == grid[r].length - 1) {
                        right = 0; 
                    }
                    left = c - 1; 
                    if(c == 0) {
                        left = grid[r].length-1; 

                    }

                    up = r - 1; 
                    if(r == 0 ) {
                        up = grid.length-1; 
                    }
                    down = r + 1; 
                    if(r == grid.length-1) {
                        down = 0; 
                    }
                    
                    if(getCellState(r, right)) {
                        quickF.union(r, c, r, right);
                    }
                    if(getCellState(r, left)) {
                        quickF.union(r, c, r, left);
                    }

                    if (getCellState(up, c)) {
                        quickF.union(r, c, up, c);
                    }
                    if (getCellState(down, c)) {
                        quickF.union(r, c, down, c);
                    }

                    if (getCellState(up, right)) {
                        quickF.union(r, c, up, right);
                    }
                    if (getCellState(down, left)) {
                        quickF.union(r, c, down, left);
                    }

                    if (getCellState(up, left)) {
                        quickF.union(r, c, up, left);
                    }
                    if (getCellState(down, right)) {
                        quickF.union(r, c, down, right);
                    }
                }
            }
        }
    
        for(int i = 0; i < rowlen; i++) {
            for(int j = 0; j < collen; j++) {
                if(getCellState(i, j)) {
                    int roots = quickF.find(i, j); 
                    //make sure it does not count itself 
                    if(!(unionTree.contains(roots))) {
                        unionTree.add(roots); 
                    }
                }
            }
        }


        return unionTree.size(); // update this line, provided so that code compiles
        

    }
}
