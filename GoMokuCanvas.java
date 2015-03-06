import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class GoMokuCanvas extends Canvas implements ActionListener, MouseListener {
       
       // static board size and square size which determines the dimension
       static final int BoardSize = 13;
       static final int SqurSize = 13;
       static int PreDim = SqurSize*BoardSize+3;	  
       public int getDimension(){return PreDim;}

       Button resignButton;   // Current player can resign by clicking this button.
       Button newGameButton;  // This button starts a new game.  It is enabled only
                              //     when the current game has ended.
       
       Label message;   // A label for displaying messages to the user.
       
       int[][] board;   // The data for the board is kept here.  The values
                        //   in this array are chosen from the following constants.
       
       static final int EMPTY = 0,       // Represents an empty square.
                        WHITE = 1,       // A white piece.
                        BLACK = 2;       // A black piece.
    
       boolean gameInProgress; // Is a game currently in progress?
       
       int currentPlayer;      // Whose turn is it now?  The possible values
                               //    are WHITE and BLACK.  (This is valid only while
                               //    a game is in progress.)
    
       int win_r1, win_c1, win_r2, win_c2;  // When a player wins by getting five or more
                                            // pieces in a row, the squares at the
                                            // ends of the row are (win_r1,win_c1)
                                            // and (win_r2,win_c2).  A red line is
                                            // drawn between these squares.  When there
                                            // are no five pieces in a row, the value of
                                            // win_r1 is -1.  The values are set in the
                                            // count() method.  The value of win_r1 is
                                            // tested in the paint() method.
    
    
       public GoMokuCanvas() {
              // Constructor.  Create the buttons and label.  Listen for mouse
              // clicks and for clicks on the buttons.  Create the board and
              // start the first game.
          setBackground(new Color(204,102,0));
          addMouseListener(this);
          setFont(new  Font("Serif", Font.BOLD, 14));
          resignButton = new Button("Resign");
          resignButton.addActionListener(this);
          newGameButton = new Button("New Game");
          newGameButton.addActionListener(this);
          message = new Label("",Label.CENTER);
          board = new int[BoardSize][BoardSize];	// size of the board
          doNewGame();
       }
       
    
       public void actionPerformed(ActionEvent evt) {
             // Respond to user's click on one of the two buttons.
          Object src = evt.getSource();
          if (src == newGameButton)
             doNewGame();
          else if (src == resignButton)
             doResign();
       }
       
    
       void doNewGame() {
             // Begin a new game.
          if (gameInProgress == true) {
                 // This should not be possible, but it doesn't 
                 // hurt to check.
             message.setText("Finish the current game first!");
             return;
          }
          for (int row = 0; row < BoardSize; row++)         // Fill the board with EMPTYs
             for (int col = 0; col < BoardSize; col++)
                board[row][col] = EMPTY;
          currentPlayer = BLACK;   // BLACK moves first.
          message.setText("BLACK:  Make your move.");
          gameInProgress = true;
          newGameButton.setEnabled(false);
          resignButton.setEnabled(true);
          win_r1 = -1;  // This value indicates that no red line is to be drawn.
          repaint();
       }
       
    
       void doResign() {
              // Current player resigns.  Game ends.  Opponent wins.
           if (gameInProgress == false) {
                  // This should not be possible.
              message.setText("There is no game in progress!");
              return;
           }
           if (currentPlayer == WHITE)
              message.setText("WHITE resigns.  BLACK wins.");
           else
              message.setText("BLACK resigns.  WHITE wins.");
          newGameButton.setEnabled(true);
          resignButton.setEnabled(false);
          gameInProgress = false;
       }
       
    
       void gameOver(String str) {
              // The game ends.  The parameter, str, is displayed as a message.
          message.setText(str);
          newGameButton.setEnabled(true);
          resignButton.setEnabled(false);
          gameInProgress = false;
       }
          
    
       void doClickSquare(int row, int col) {
             // This is called by mousePressed() when a player clicks on the
             // square in the specified row and col.  It has already been checked
             // that a game is, in fact, in progress.
             
          /* Check that the user clicked an empty square.  If not, show an
             error message and exit. */
             
          if ( board[row][col] != EMPTY ) {
             if (currentPlayer == BLACK)
                message.setText("BLACK:  Please click an empty square.");
             else
                message.setText("WHITE:  Please click an empty square.");
             return;
          }
          
          /* Make the move.  Check if the board is full or if the move
             is a winning move.  If so, the game ends.  If not, then it's
             the other user's turn. */
             
          board[row][col] = currentPlayer;  // Make the move.
          Graphics g = getGraphics();
          drawPiece(g, currentPlayer, row, col);
          g.dispose();
          
          if (winner(row,col)) {  // First, check for a winner.
             if (currentPlayer == WHITE)
                gameOver("WHITE wins the game!");
             else
                gameOver("BLACK wins the game!");
             Graphics w = getGraphics();
             drawWinLine(w);
             w.dispose();
             return;
          }
          
          boolean emptySpace = false;     // Check if the board is full.
          for (int i = 0; i < BoardSize; i++)
             for (int j = 0; j < BoardSize; j++)
                if (board[i][j] == EMPTY)
                   emptySpace = true;
          if (emptySpace == false) {
             gameOver("The game ends in a draw.");
             return;
          }
          
          /* Continue the game.  It's the other player's turn. */
          
          if (currentPlayer == BLACK) {
             currentPlayer = WHITE;
             message.setText("WHITE:  Make your move.");
          }
          else {  
             currentPlayer = BLACK;
             message.setText("BLACK:  Make your move.");
          }
    
       }  // end doClickSquare()
       
       
       private boolean winner(int row, int col) {
            // This is called just after a piece has been played on the
            // square in the specified row and column.  It determines
            // whether that was a winning move by counting the number
            // of squares in a line in each of the four possible
            // directions from (row,col).  If there are 5 squares (or more)
            // in a row in any direction, then the game is won.
            
          if (count( board[row][col], row, col, 1, 0 ) >= 5)	// 5 in a col
             return true;
          if (count( board[row][col], row, col, 0, 1 ) >= 5)	// 5 in a raw
             return true;
          if (count( board[row][col], row, col, 1, -1 ) >= 5)	// 5 in right diagonal 
             return true;
          if (count( board[row][col], row, col, 1, 1 ) >= 5)	// 5 in left diagonal
             return true;
             
          /* When we get to this point, we know that the game is not
             won.  The value of win_r1, which was changed in the count()
             method, has to be reset to -1, to avoid drawing a red line
             on the board. */
    
          win_r1 = -1;
          return false;
          
       }  // end winner()
       
       
       private int count(int player, int row, int col, int dirX, int dirY) {
             // Counts the number of the specified player's pieces starting at
             // square (row,col) and extending along the direction specified by
             // (dirX,dirY).  It is assumed that the player has a piece at
             // (row,col).  This method looks at the squares (row + dirX, col+dirY),
             // (row + 2*dirX, col + 2*dirY), ... until it hits a square that is
             // off the board or is not occupied by one of the players pieces.
             // It counts the squares that are occupied by the player's pieces.
             // Furthermore, it sets (win_r1,win_c1) to mark last position where
             // it saw one of the player's pieces.  Then, it looks in the
             // opposite direction, at squares (row - dirX, col-dirY),
             // (row - 2*dirX, col - 2*dirY), ... and does the same thing.
             // Except, this time it sets (win_r2,win_c2) to mark the last piece.
             // Note:  The values of dirX and dirY must be 0, 1, or -1.  At least
             // one of them must be non-zero.
             
          int ct = 1;  // Number of pieces in a row belonging to the player.
          
          int r, c;    // A row and column to be examined
          
          r = row + dirX;  // Look at square in specified direction.
          c = col + dirY;
          while ( r >= 0 && r < BoardSize && c >= 0 && c < BoardSize && board[r][c] == player ) {
                  // Square is on the board and contains one of the players's pieces.
             ct++;
             r += dirX;  // Go on to next square in this direction.
             c += dirY;
          }
    
          win_r1 = r - dirX;  // The next-to-last square looked at.
          win_c1 = c - dirY;  //    (The LAST one looked at was off the board or
                              //    did not contain one of the player's pieces.
                              
          r = row - dirX;  // Look in the opposite direction.
          c = col - dirY;
          while ( r >= 0 && r < BoardSize && c >= 0 && c < BoardSize && board[r][c] == player ) {
                  // Square is on the board and contains one of the players's pieces.
             ct++;
             r -= dirX;   // Go on to next square in this direction.
             c -= dirY;
          }
    
          win_r2 = r + dirX;
          win_c2 = c + dirY;
          
          // At this point, (win_r1,win_c1) and (win_r2,win_c2) mark the endpoints
          // of the line of pieces belonging to the player.
    
          return ct;
    
       }  // end count()
    
    
       public void paint(Graphics g) {
          
          /* Draw a two-pixel black border around the edges of the canvas,
             and draw grid lines in darkGray.  */
          
          g.setColor(Color.darkGray);
          for (int i = 1; i < BoardSize; i++) {
             g.drawLine(1 + SqurSize*i, 0, 1 + SqurSize*i, getSize().height);
             g.drawLine(0, 1 + SqurSize*i, getSize().width, 1 + SqurSize*i);
          }
          g.setColor(Color.black);
          g.drawRect(0,0,getSize().width-1,getSize().height-1);
          g.drawRect(1,1,getSize().width-3,getSize().height-3);
          
          /* Draw the pieces that are on the board. */
          
          for (int row = 0; row < BoardSize; row++)
             for (int col = 0; col < BoardSize; col++)
                if (board[row][col] != EMPTY)
                   drawPiece(g, board[row][col], row, col);
                   
          /* If the game has been won, then win_r1 >= 0.  Draw a line to mark
             the five winning pieces. */
                   
          if (win_r1 >= 0)
             drawWinLine(g);
          
       }  // end paint()
       
       
       private void drawPiece(Graphics g, int piece, int row, int col) {
              // Draw a piece in the square at (row,col).  The color is specified
              // by the piece parameter, which should be either BLACK or WHITE.
          if (piece == WHITE)
             g.setColor(Color.white);
          else
             g.setColor(Color.black);
          // fill the piece
          g.fillOval(3 + SqurSize*col, 3 + SqurSize*row, SqurSize-3, SqurSize-3);
       }
       
       
       private void drawWinLine(Graphics g) {
             // Draw a 2-pixel wide red line from the middle of the square at
             // (win_r1,win_c1) to the middle of the square at (win_r2,win_c2).
             // This routine is called to mark the 5 pieces that won the game.
             // The values of the variables are set in the count() method.
          g.setColor(Color.red);
          int shift = (int)(SqurSize/2.0 + 1.5);
          g.drawLine( shift + SqurSize*win_c1, shift + SqurSize*win_r1, shift + SqurSize*win_c2, shift + SqurSize*win_r2 );
          if (win_r1 == win_r2)
             g.drawLine( shift + SqurSize*win_c1, shift - 1 + SqurSize*win_r1, shift + SqurSize*win_c2, shift - 1 + SqurSize*win_r2 );
          else
             g.drawLine( shift - 1 + SqurSize*win_c1, shift + SqurSize*win_r1, shift - 1 + SqurSize*win_c2, shift + SqurSize*win_r2 );
       }
       
       
       public Dimension getPreferredSize() {
             // Specify desired size for this component.  
          return new Dimension(BoardSize*SqurSize+3, BoardSize*SqurSize+3);
       }
    
    
       public Dimension getMinimumSize() {
          return new Dimension(BoardSize*SqurSize+3, BoardSize*SqurSize+3);
       }
       
    
       public void mousePressed(MouseEvent evt) {
             // Respond to a user click on the board.  If no game is
             // in progress, show an error message.  Otherwise, find
             // the row and column that the user clicked and call
             // doClickSquare() to handle it.
          if (gameInProgress == false)
             message.setText("Click \"New Game\" to start a new game.");
          else {
             int col = (evt.getX() - 2) / SqurSize;
             int row = (evt.getY() - 2) / SqurSize;
             if (col >= 0 && col < BoardSize && row >= 0 && row < BoardSize)
                doClickSquare(row,col);
          }
       }
       
    
       public void mouseReleased(MouseEvent evt) { }
       public void mouseClicked(MouseEvent evt) { }
       public void mouseEntered(MouseEvent evt) { }
       public void mouseExited(MouseEvent evt) { }
    
    
}  // end class GoMokuCanvas
