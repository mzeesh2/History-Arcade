# History Arcade
## Description
History Arcade is a turn based quiz/map control game played against human or AI opponents. The player must correctly answer historical questions related to the game map in order to take over and control territories that the map is divided into. Whoever controls the majority of the map at the end of the game wins. This game will strike a balance between fun and learning and creates a hands-on, minds on opportunity for students to test their history knowledge.
## Setup
1. Import the project onto your local device
2. Open Eclipse 
3. Click File -> New -> Java Project
4. Uncheck "Use Default Location" and browse to where you saved the project 
5. Navigate into the "History-Arcade" Folder and select "HistoryArcade" and click Finish.
6. Everything should be setup now. 
## Debugging Setup 
Eclipse can sometimes give errors after importing the project. This is because build paths might not be setup up correctly while importing. Fortunately, this is an easy fix. Follow steps below:
1. Expand the History Arcade folder, then src and then default package.
2. Expand lib folder. This folder should contain two files: "json-simple-1.1.jar" and "AnimateFX-1.2.1.jar" 
3. Right click the files and find "Build Path" in the menu. 
4. Click "Add to Build Path"
5. Now scroll to the "Styler" folder and expand it.
6. You should see a file called "CSSStyler.css"
7. Right click this file, navigate to "Build Path" and select "Add to Build Path" 
8. Everything should be working now.
## How to start the game
1. Expand the History Arcade folder, then src and then default package.
2. Find the "GameServer.java" file and execute
3. The console will ask you to enter a port number. You can enter 5555 for default 
4. A started server message should display after port number has been entered.
5. Now find the "PlayerUI_FX.java" file and execute. 
6. Select single player or multiplayer
### Single-Player VS Multi-Player Setup
#### Single-Player Setup 
1. Select single player and choose which war you want to be quizzed on. (Civil War is under progress)
2. You will be playing against an AI whose difficulty is randomized on each instance of the game.
#### Multi-Player Setup
1. Select Multi-Player
2. Choose war
3. Enter in IP Address (127.0.0.1) 
4. Enter in Port Number (5555)
5. Click Create Game
6. Go back to "PlayerUI_FX.java" file and execute again. 
7. This should open another instance of player. 
8. Select Multi-Player
9. Choose the same war as before. 
10. Enter in IP Address (127.0.0.1) 
11. Enter in Port Number (5555)
12. Click Join Game
13. Enjoy!
