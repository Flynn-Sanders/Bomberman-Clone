
# Bomberman Clone

A multiplayer Bomberman recreation developed
entirely from scratch in Java.

## Overview

A recreation of the classic NES Bomberman local multiplayer mode where two to four players compete by placing bombs to break structures and eliminate opponents to be the last one standing.
One terminal opens a server, and separate terminals join as clients.

## Features

- Multiplayer gameplay
- TCP client-server communication
- Real-time game-state updates
- Bomb placement and timed explosions
- Destructible and indestructible blocks
- Custom data structures

## Architecture

Manager:
- 
ServerThread:
- 
GameManager:
- 
Player:
- 
ClientScreen:
- 
ClientGameState:
- 
TileMap:
- 

## Data Structures

MyHashMap
- Player and Bomb tracking. 
- For players, the key being a player's ID # allows players to simply pass in the ID whenever they call for an update (movement, placing a bomb) to identify themselves and easily be matched with the player object itself
- For bombs, the key being the row/col identifying string 
MyHashSet
- Required to use MyHashMap
- Wrapper; uses Java HashSet
DLList
- Required to use MyHashMap (bucketing)
MinHeap
- Places the earliest bomb at the top of the heap, allowing the it to be checked with O(1) to see if a bomb needs to explode.
- Efficiency O(log n) by using bubble up insertion


## Running the Game

Preferred JDK 17 or later
To compile: javac *.java
On Server Terminal: java Server
On any Client Terminals (2-4): java Client

## Known Limitations

- The client currently connects to localhost
- The server does not enforce the 2–4 player range.

## Author

Developed independently by Flynn Sanders.
