# GoMoku
    Gomoku is an abstract strategy board game. Also called Gobang or Five in a Row.
    
This program(applet) lets two uses play GoMoku (a.k.a Pente) against each other.  

Black always starts the game.  

When a player gets five-in-a-row, that player wins.  

The game ends in a draw if the board is filled before either player wins.
## purpose ##
This project is a modified version of the classic game "GoMoku"

It takes a prewriting online code and adds more specific features
## responsibilities ##
* Add comments
* Define variables 
* Add new feature to the game
* Embellish the user interface

##Project preview##
####Self define dimension of the game board####
    You can set your own board size and square size to play a more challenging game or a easier one
    Also you can change the size of the stone to make it more visible 
You can change the integer value of the size to customize the board
```Java
	   static final int BoardSize = 13;
	   static final int SqurSize = 13;
	   static int PreDim = SqurSize*BoardSize+3;
```   
######next step######
- Adding buttons and messages to prompt user to input customized dimension
- Changing the Applet to JFrame so that it can contain the entire board and messages 
