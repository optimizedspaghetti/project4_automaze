// Names: Gabe Robertson
// x500s: robe1737
import java.util.Scanner;
import java.util.Random;

public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        // instantiate class variables
        maze = new Cell[rows][cols];
        this.startRow = startRow;
        this.endRow = endRow;

        // create new Cell obj for each spot in maze
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = new Cell();
            }
        }
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze() {
        // takes in rows and columns
        Scanner s = new Scanner(System.in);
        System.out.println("enter rows: ");
        int rows = s.nextInt();
        System.out.println("enter cols: ");
        int cols = s.nextInt();

        if (rows > 5) {
            System.out.println("rows reduced to 5");
            rows = 5;
        }

        if (cols > 20) {
            System.out.println("columns reduced to 20");
            cols = 20;
        }

        // randomly assigns starting and ending position
        Random r = new Random();
        int startRow = r.nextInt(rows);
        int endRow = r.nextInt(rows);

        // create MyMaze and mazeStack objects, instantiated with above vars
        MyMaze theMaze = new MyMaze(rows, cols, startRow, endRow);
        Stack1Gen<int[]> mazeStack = new Stack1Gen<>();
        mazeStack.push(new int[]{startRow, 0}, null);

        // loop til stack is empty
        while (!mazeStack.isEmpty()) {
            // get but do not remove first element in stack
            int[] topElement = mazeStack.top(); // current cell

            // need to check that each available neighbor cell has not been visited
            boolean topVisited = false;
            boolean rightVisited = false;
            boolean downVisited = false;
            boolean leftVisited = false;

            if (topElement[0] == 0 || theMaze.maze[topElement[0] - 1][topElement[1]].getVisited()) {
                topVisited = true;
            }

            if (topElement[1] == cols - 1 || theMaze.maze[topElement[0]][topElement[1] + 1].getVisited()) {
                rightVisited = true;
            }

            if (topElement[0] == rows - 1 || theMaze.maze[topElement[0] + 1][topElement[1]].getVisited()) {
                downVisited = true;
            }

            if (topElement[1] == 0 || theMaze.maze[topElement[0]][topElement[1] - 1].getVisited()) {
                leftVisited = true;
            }

            if (topVisited && rightVisited && downVisited && leftVisited) {
                mazeStack.pop();
                continue;
            }

            int[] nextCell = null; //could save space by not making this, but using it for sake of clarity
            int direction = r.nextInt(4);
            working: { // fun with gotos because it's fun
                switch (direction) {
                    case 0: { // up
                        if (topElement[0] > 0 && !theMaze.maze[topElement[0] - 1][topElement[1]].getVisited()) {
                            nextCell = new int[]{topElement[0] - 1, topElement[1]};
                            theMaze.maze[nextCell[0]][nextCell[1]].setBottom(false); //break barrier
                            break;
                        }
                        break working;
                    }
                    case 1: { // right
                        if (topElement[1] + 1 < cols && !theMaze.maze[topElement[0]][topElement[1] + 1].getVisited()) {
                            nextCell = new int[]{topElement[0], topElement[1] + 1};
                            theMaze.maze[topElement[0]][topElement[1]].setRight(false);
                            break;
                        }
                        break working;
                    }
                    case 2: { // down
                        if (topElement[0] + 1 < rows && !theMaze.maze[topElement[0] + 1][topElement[1]].getVisited()) {
                            nextCell = new int[]{topElement[0] + 1, topElement[1]};
                            theMaze.maze[topElement[0]][topElement[1]].setBottom(false);
                            break;
                        }
                        break working;
                    }
                    case 3: { // left
                        if (topElement[1] > 0 && !theMaze.maze[topElement[0]][topElement[1] - 1].getVisited()) {
                            nextCell = new int[]{topElement[0], topElement[1] - 1};
                            theMaze.maze[nextCell[0]][nextCell[1]].setRight(false);
                            break;
                        }
                        break working;
                    }
                }
                mazeStack.push(nextCell, topElement); // add int[] nextCell to stack
                theMaze.maze[nextCell[0]][nextCell[1]].setVisited(true); // set Cell visited to true
            }
        }

        // set each Cell's visited state to false
        for (int k = 0; k < rows; k++) {
            for (int j = 0; j < cols; j++) {
//                if (!theMaze.maze[k][j].getVisited()) // debug output
//                    System.out.println(String.format("Unvisited cell at %d %d", k, j));
                theMaze.maze[k][j].setVisited(false);
            }
        }

        //finally, return the Maze
        return theMaze;
    }


    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {
        // set end point right = false
        maze[endRow][maze[0].length - 1].setRight(false);

        StringBuilder textMaze = new StringBuilder();
        for (int i = 0; i < maze.length; i++) {
            if (i == 0) { // create top bound
                textMaze.append("|---".repeat(maze[0].length));
                textMaze.append("|\n");
            }
            for (int j = 0; j < maze[0].length; j++) {
                if (j == 0 && i == startRow) { // create left bound, skip if starting point
                    textMaze.append(" ");
                } else if (j == 0) {
                    textMaze.append("|");
                }

                if (maze[i][j].getVisited()) { // print Cell visited state
                    textMaze.append(" * ");
                } else {
                    textMaze.append("   ");
                }

                if (maze[i][j].getRight()) { // print Cell right wall
                    textMaze.append("|");
                } else {
                    textMaze.append(" ");
                }
            }

            textMaze.append("\n");
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].getBottom()) { // print Cell bottom
                    textMaze.append("|---");
                } else {
                    textMaze.append("|   ");
                }
            }
            textMaze.append("|\n");
        }

        System.out.println(textMaze);
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        // create queue with start index
        Q1Gen<int[]> mazeQ = new Q1Gen<>();
        mazeQ.add(new int[] {startRow, 0});

        while (mazeQ.length() != 0) {
            int[] curr = mazeQ.remove();
            maze[curr[0]][curr[1]].setVisited(true);

            if (curr[0] == endRow && curr[1] == maze[0].length - 1) { // reached destination
                break;
            }

            // check in all directions
            if (curr[0] < maze.length - 1) { // down
                if (
                    !maze[curr[0] + 1][curr[1]].getVisited() // unvisited
                    && !maze[curr[0]][curr[1]].getBottom() // accessible
                    ) {
                    mazeQ.add(new int[] {curr[0] + 1, curr[1]});
                }
            }

            if (curr[0] > 0) { // up
                if (
                    !maze[curr[0] - 1][curr[1]].getVisited() // unvisited
                    && !maze[curr[0] - 1][curr[1]].getBottom() // accessible
                    ) {
                    mazeQ.add(new int[] {curr[0] - 1, curr[1]});
                }
            }

            if (curr[1] < maze[0].length - 1) { // right
                if (
                    !maze[curr[0]][curr[1] + 1].getVisited() // unvisited
                    && !maze[curr[0]][curr[1]].getRight() // accessible
                    ) {
                    mazeQ.add(new int[] {curr[0], curr[1] + 1});
                }
            }

            if (curr[1] > 0) { // left
                if (
                    !maze[curr[0]][curr[1] - 1].getVisited() // unvisited
                    && !maze[curr[0]][curr[1] - 1].getRight() // accessible
                    ) {
                    mazeQ.add(new int[] {curr[0], curr[1] - 1});
                }
            }

        }

    }

    public static void main(String[] args){
        /* Make and solve maze */
        MyMaze testMaze = makeMaze();
        testMaze.solveMaze();
        testMaze.printMaze();
    }
}
