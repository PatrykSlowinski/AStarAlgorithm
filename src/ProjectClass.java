import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class ProjectClass { // class used to apply build grid, apply algorithm, show and save output to text file
    public static final int ROW = 60;		//declaration of number of rows of grid
    public static final int COLUMN = 80;	//declaration of number of columns of grid

    public static File outputFile = new File("output.txt");  //creation of new text file with output.txt name

    public static int[] generatePoint(int row, int column) {  //method that generates points with random i of range [0, row-1] and random
        Random random = new Random();						  // j of range [0, column-1]
        int[] state = new int[2];
        state[0] = random.nextInt(row);
        state[1] = random.nextInt(column);
        return state;
    }
    public static int[][] generateObstacles(int row, int column){  //method that generates two dimensional array of integers that contains positions of obstacles
        int numberOfObstacles = (int) (row * column * 0.3);	 // calculates number of obstacles in the grid - 30 percent of all cells
        int[][] obstacles = new int[numberOfObstacles][2];
        Random random = new Random();
        Set<Point> obstaclesSet = new HashSet<>(); // contains positions of obstacles in the grid of cells
        for(int i=0;i<numberOfObstacles;i++) {
            Point p = new Point(random.nextInt(row), random.nextInt(column));
            while (obstaclesSet.contains(p)) {      // thanks to this loop only new points are added as obstacles - there is no possibility to add one point 2 times
                p = new Point(random.nextInt(row), random.nextInt(column));
            }
            obstaclesSet.add(p);
        }
        int i = 0;
        for(Point p:obstaclesSet) { // loop that fills array of obstacles
            obstacles[i][0] = p.x;
            obstacles[i][1] = p.y;
            i++;
        }
        return obstacles;
    }
    public static void main(String[] args) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)); // creation of instance of BufferedWriter to save app output to text file
        int obstacles[][] = generateObstacles(ROW, COLUMN); // generation of obstacles, initial state and goal state
        int [] initialState = generatePoint(ROW, COLUMN);
        int [] goalState = generatePoint(ROW, COLUMN);

        Boolean b = false;
        if(initialState[0] == goalState[0] && initialState[1]==goalState[1]) { //checking if generated initial state is not equal to goal state
            System.out.println("Initial state cannot be the goal state!");
            writer.append("Initial state cannot be the goal state!");
            b = true;
        }
        for(int[] o : obstacles) {
            if(o[0]==initialState[0] && o[1]==initialState[1]){ //checking if any generated obstacle is not initial state
                System.out.println("Initial state cannot be obstacle!");
                writer.append("Initial state cannot be obstacle!"+"\n");
                b = true;
            }
            else if(o[0]==goalState[0] && o[1]==goalState[1]) { //checking if any generated obstacle is not goal state
                System.out.println("Goal state cannot be obstacle!");
                writer.append("Goal state cannot be obstacle!"+"\n");
                b = true;
            }
        }
        if(b == true) {
            writer.close();
        }
        else { // if b == false, it means if obstacle is not initial state, goal state or goal state is not initial state
            // creation of new instance of class aAlgorithm to apply a* searching algorithm, parameters are: number of rows and columns in grid, position of
            // initial state, position of goal state and array of cells that are obstacles
            AAlgorithm aAlgorithm = new AAlgorithm(ROW, COLUMN, initialState[0], initialState[1], goalState[0], goalState[1], obstacles);
            aAlgorithm.process(); // apply A* Algorithm
            System.out.println(aAlgorithm.displaySolution()); // displays solution of a* algorithm or grid if there is no solution
            writer.append(aAlgorithm.displaySolution()+"\n"); //adds solution to text file
            writer.close();

        }

    }

}