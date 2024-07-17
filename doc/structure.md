# Graveborn - Structure

## Modes

### Server
![server](./structure_img/server.png)

### Client
![client](./structure_img/client.png)

### Host
![host](./structure_img/host.png)

### Standalone
![standalone](./structure_img/standalone.png)

## Game

### Player
![player](./structure_img/player.png)

- is responsible for window settings
- is responsible for audio
- collects player input
- translates player input to Objectmanager updates
- submits Objectmanager updates

### World
![world](./structure_img/world.png)

- is responsible for npc movement
- is responsible for rewards and score
- calculates npc actions and position updates
- submits Objectmanager updates

### Objectmanager
![objectmanager](./structure_img/objectmanager.png)

- is responsible for physics/collision
- holds all entity related data (e.g. position, name, health)
- updates entities according to submitted updates from
player and/or World
- bundles all submitted updates for sync
- can take bundled updates from other Objecmanagers

If values do not match:

__Recessive__:
- will take values of other Objectmanager

__Dominant__:
- will prepare SyncMessage data (not the message itself)

## Networking
![netserver](./structure_img/netserver.png)
![netclient](./structure_img/netclient.png)

- handles network connection
- sync with others
