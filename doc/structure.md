# Graveborn - Structure

## Modes

### Server
![server](server.png)

### Client
![client](client.png)

### Host
![host](host.png)

### Standalone
![standalone](standalone.png)

## Game

### Player
![player](player.png)

- is responsible for window settings
- is responsible for audio
- collects player input
- tranlates player input to Objectmanager updates
- submits Objectmanager updates
-
### World
![world](world.png)

- is responsible for npc movement
- is responsible for rewards and score
- calculates nps actions and position updates
- submits Objectmanager updates

### Objectmanager
![objectmanager](objectmanager.png)

- is responsible for physics/collision
- hold all entity related data (e.g position, name, health)
- updates entities according to submited updates from
player and/or World
- bundels all submited updates for sync
- can take bundeled updates from other Objecmanagers

If values do not match:
__Recessive__:
- will take values of other Objectmanager

__Dominant__:
- will prepare SyncMessage data (not the message it self)

## Networking
![netserver](netserver.png)
![netclient](netclient.png)

- handle network connection
- sync with others
