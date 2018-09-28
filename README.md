# Maze

This maze demo can draw maze to display it with the shortest route from entry to exit.
Meanwhile, some test maze is contained in the `src` folder. Set StartMaze as main class and execute it will draw maze and the path on display.

The algorithm that is used in this maze demo is Dijkstra algorithm. More specifically, it uses a 2D int array to record the shortest distance from entry. And there is the other 2D int array that record whether the point in maze will be revisited in furture recursion or not.

Some sample path output (these test file can be found in src/TestMaze): 

**bigMaze2:**
![bigMaze2](https://github.com/Mirage00/Maze/blob/master/src/PathOutput/bigMaze2.png?raw=true)

**medMaze:**
![medMaze](https://github.com/Mirage00/Maze/blob/master/src/PathOutput/medMaze.png?raw=true)

**noWallsBig:**
![noWallsBig](https://github.com/Mirage00/Maze/blob/master/src/PathOutput/noWallsBig.png?raw=true)

**upperLeftMaze1:**
![upperLeftMaze1](https://github.com/Mirage00/Maze/blob/master/src/PathOutput/upperLeftMaze1.png?raw=true)

