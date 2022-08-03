import java.util.PriorityQueue;

public class AAlgorithm { // class that contains the whole a* algorithm implementation
    public static final int V_H_COST = 1; // cost of vertical and horizontal moves

    public Cell[][] grid; //grid of cells

    private PriorityQueue<Cell> openCells; //priority queue to operate on open cells

    private boolean[][] closedCells; // array of booleans - if is true it means that cell of [i][j] position is in set of closed cells

    public int iInitial, jInitial; // the initial state
    public int iGoal, jGoal; // the goal state

    public AAlgorithm(int rows, int columns, int iInitial, int jInitial, int iGoal, int jGoal, int[][] blocks) {
        this.iInitial = iInitial;
        this.jInitial = jInitial;
        this.iGoal = iGoal;
        this.jGoal = jGoal;
        grid = new Cell[rows][columns]; // grid is two dimensional array of cells with rows number of rows and columns number of columns
        closedCells = new boolean[rows][columns]; // closed cells is two dimensional array of booleans with rows number of rows and columns number of columns
        openCells = new PriorityQueue<Cell>((Cell c1, Cell c2) -> { // open cells is priority queue that contains open cells and that positions cells by final cost
            return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
        });
        // init heuristic and cells
        for (int i = 0; i < grid.length; i++) { //these loops fills grid with cells, computes heuristic cost and sets solution as false - grid is not a goal state
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i - iGoal) + Math.abs(j - jGoal);
                grid[i][j].solution = false;
            }
        }

        grid[iInitial][jInitial].finalCost = 0; // sets final cost of initial cell in the grid as 0


        for (int i = 0; i < blocks.length; i++) {  // put obstacles on the grid
            addBlockOnCell(blocks[i][0], blocks[i][1]);
        }
    }

    public void addBlockOnCell(int i, int j) { // sets value of grid that is obstacle as null
        grid[i][j] = null;
    }

    public void updateCostIfNeeded(Cell current, Cell t, int cost) { // updates cost if it is needed, current is current cell, t is neighbor cell and cost is cost of moving
        if (t == null || closedCells[t.i][t.j])	// 															// from  current to neighbor cell
            return; // if neighbor is null, so is obstacle or is in closed Cells set, cost is not updated

        int tFinalCost = t.heuristicCost + cost; // if neighbor is not obstacles nor closed cell, its final cost is updated - final cost equals heuristic cost + cost of moving
        boolean isOpen = openCells.contains(t);

        if (!isOpen || tFinalCost < t.finalCost) { // if open set doesn't contain neighbor or calculated final cost is smaller than final cost of neighbor:
            t.finalCost = tFinalCost;			// final cost of neighbor is updated
            t.parent = current;					// current cell becomes neighbor's parent cell

            if (!isOpen) // if open set doesn't contain neighbor, neighbor is added to open cells
                openCells.add(t);
        }
    }

    public void process() {
        openCells.add(grid[iInitial][jInitial]); // add start location to open cells list
        Cell current; // initialization of current cell

        while (true) {
            current = openCells.poll(); // current cell is cell in open cells that has lowest final cost - priority queue positions open cells by final cost
            if (current == null)
            {break;}
            closedCells[current.i][current.j] = true; // current cell is added to closed cells
            if (current.equals(grid[iGoal][jGoal])) // if current cell is goal state it returns - its end of algorithm
            {
                return;
            }

            Cell t; // neighbor cell

            // these ifs update costs of every neighbor if it needed
            if (current.i - 1 >= 0) { // updates cost of moving to a neighbor above
                t = grid[current.i - 1][current.j];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
            }

            if (current.j - 1 >= 0) { // updates cost of moving to a neighbor on the left
                t = grid[current.i][current.j - 1];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
            }

            if (current.j + 1 < grid[0].length) { // updates cost of moving to a neighbor on the right
                t = grid[current.i][current.j + 1];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
            }

            if (current.i + 1 < grid.length) { // updates cost of moving to a neighbor below
                t = grid[current.i + 1][current.j];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
            }

        }
    }

    public String display() { //displays grid of cells but unsolved ( I - initial cell, G - goal cell, " " - empty cells, "*" - obstacles )
        StringBuilder sb = new StringBuilder();
        sb.append("Grid: "+"\n");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == iInitial && j == jInitial) // Initial cell
                    sb.append("I ");
                else if (i == iGoal && j == jGoal)
                    sb.append("G "); //Goal cell
                else if (grid[i][j] != null) // empty cells
                    sb.append("  ");
                else
                    sb.append("* "); // obstacles
            }
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public String displaySolution() { // displays cells grid with path of a* search algorithm
        StringBuilder sb = new StringBuilder();
        if (closedCells[iGoal][jGoal]) { // displays solution when goal state is in set of closed cells // track back the path
            sb.append("Path: "+"\n");
            Cell current = grid[iGoal][jGoal];  // current cell is goal state
            grid[current.i][current.j].solution = true; // current cell is goal state so it part of solution

            while (current.parent != null) { // if parent of current is not obstacle
                grid[current.parent.i][current.parent.j].solution = true; // parent of current grid is part of solution
                current = current.parent; // parent of current cell becomes current cell
            }
            sb.append("\n");
            for (int i = 0; i < grid.length; i++) { //loop that displays grid with solution
                for (int j = 0; j < grid[i].length; j++) {
                    if (i == iInitial && j == jInitial)
                        sb.append("I "); //Initial cell
                    else if (i == iGoal && j == jGoal)
                        sb.append("G "); //Goal cell
                    else if (grid[i][j] != null) {
                        if(grid[i][j].solution) { // displays + if cell is in the solution path
                            sb.append("+ ");
                        }
                        else {
                            sb.append("  "); //empty cell
                        }
                    }
                    else
                        sb.append("* ");// Block cell
                }
                sb.append("\n");
            }
            sb.append("\n");
        } else { // if is not possible to arrive goal state from initial state
            sb.append(this.display()+"\n");
            sb.append("No possible path because of obstacles!");}
        return sb.toString();
    }
}