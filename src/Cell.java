

public class Cell { // class cell that represents any cell in the grid
    public int i,j;
    public Cell parent;
    public int heuristicCost;
    public int finalCost; // G + H
    public boolean solution; // if cell is solution

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public String toString() {
        return "["+i+" "+j+"]";
    }
}

