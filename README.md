# Project

Simulator of animals randomly moving on a grid world.
Map is made out of tiles of grass or water.

## Functions

- Simulator
  - Variable simulation speed:
    - Stepping
    - Auto-step with speed from one step per second up
    - Max possible speed
    - Paused
	- Simulation is running in a separate thread, and should not slow down rendering
	- World state is savable in:
  	- binary format, holding complete state of entities and tiles (.world)
  	- text CSV file separated with semicolumns (;) (.tworld)
    	- can be edited visually with Excel or similar software with automatic color highlighting
    	- does NOT hold complete state of the world, only world size, animal position and tile types
  		- (sample file "sample-world.tworld")
- Entity editor
  - Entities can be selected and their properties modified
  - Properties of single entity can be copied to entities of same type with a button
- Application supports commandline arguments:
  - '--speed=\<NUMBER>'
  - 'file/to/open.world'
  - for example: java -jar animalworld-1.0-SNAPSHOT-jar-with-dependencies.jar --speed=10 ../sample-world.tworld

# Build

Project is created with Maven.
Open in any IDE and execute Maven "package".
Two JAR files will be created - one with and one without dependencies.
