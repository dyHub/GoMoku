/*
       This applet lets two uses play GoMoku (a.k.a Pente) against each 
       other.  Black always starts the game.  When a player gets five-in-a-row,
       that player wins.  The game ends in a draw if the board is filled
       before either player wins.
       
       This file defines two classes: the main applet class, GoMuku,
       and a canvas class, GoMokuCanvas.
    
       It is assumed that this applet is 330 pixels wide and 240 pixels high!
*/

import java.awt.*;
import java.applet.*;    

public class GoMoku extends Applet {
    
       /* The main applet class only lays out the applet.  The work of
          the game is all done in the GoMokuCanvas object.   Note that
          the Buttons and Label used in the applet are defined as 
          instance variables in the GoMokuCanvas class.  The applet
          class gives them their visual appearance and sets their
          size and positions.*/
    
       public void init() {
 
          setLayout(null);  // I will do the layout myself.
       
          setBackground(new Color(0,150,0));  // Dark green background.
          
          /* Create the components and add them to the applet. */
    
          GoMokuCanvas board = new GoMokuCanvas();
              // Note: The constructor creates the buttons board.resignButton
              // and board.newGameButton and the Label board.message.
          add(board);
    
          board.newGameButton.setBackground(Color.lightGray);
          add(board.newGameButton);
    
          board.resignButton.setBackground(Color.lightGray);
          add(board.resignButton);
    
          board.message.setForeground(Color.green);
          board.message.setFont(new Font("Serif", Font.BOLD, 14));
          add(board.message);
          
          /* Set the position and size of each component by calling
             its setBounds() method. */
          board.setBounds(16,16,board.getDimension(),board.getDimension()); 
          board.newGameButton.setBounds(board.getDimension()+35, 60, 100, 30);
          board.resignButton.setBounds(board.getDimension()+35, 120, 100, 30);
          board.message.setBounds(0, board.getDimension()+35,300, 30);
       }
       
    } // end class GoMoku
    
    
