
# Bomberman Clone

A multiplayer Bomberman recreation developed
entirely from scratch in Java.

## Overview

Describe the game, its objectives and its
client-server architecture.

## Features

- Multiplayer gameplay
- TCP client-server communication
- Real-time game-state updates
- Bomb placement and timed explosions
- Destructible and indestructible blocks
- Custom data structures

## Architecture

Explain the responsibilities of:
- Manager
- ServerThread
- GameManager
- Player
- ClientScreen
- ClientGameState
- TileMap

## Data Structures

MyHashMap
- Player and Bomb tracking. 
- For players, the key being a player's ID # allows players to simply pass in the ID whenever they call for an update (movment, placing a bomb) to identify themselves nad easily be matched with the player object itself
- For bombs, the key being the row/col/timer identifying string 
MyHashSet
- Required to use MyHashMap
DLList
- Required to use MyHashSet (bucketing)
MinHeap
- Sorts bombs by comparing detonation time and ordering by earliest to latest, allowing the top item in the heap to be checked to see if a bomb needs to explode.
- Efficiency O(log n) by using binary search to add new bombs to heap 


## Running the Game

Preferred JDK 17 or later
To compile: javac *.java
On Server Terminal: java Server
On any Client Terminals (2-4): java Client

## Known Limitations

Document bugs and incomplete features
rather than implying the game is perfect.

## Author

Developed independently by Flynn Sanders.
