mikehash
nitzanknoll

Explanation of differences of UMLs:
There were three main changes between the two UMLs: 
1. Addition of the LeafGenerator class in the final UML.
2. Adding the InfiniteWorldGenerator class in the final UML
3. Downloading the connection between Sky and the avatar that we thought would be 
there at the beginning.
Instead of the leafGenerator method that we thought would be in the Tree,
we realized that in our view it makes more sense to make a dedicated class that
will have one responsibility - this way we gain both code readability, 
and a substantial separation between things.
At first, we thought it would make sense to create the infinite world in the
PepseGameManager class, when the method responsible for this createInfiniteWorld would call 
initializeGame from an infinite loop.
After that we realized that it makes more sense to make a separate 
class that will inherit from gameObject and thus manage the creation of the infinite game
Before we started writing the program, we thought that the sun would follow the avatar
and that it would receive its position and thus know how to rotate around it. After that, 
we realized that the sun does not need this information, and it is enough to transfer the location of the 
camera to it.


Infinite World:
The way we implemented the infinite world is by creating a class called
InfiniteWorldGenerator made for controlling the game world. It inherits 
from GameObject, so it has an update method.
In each update, we check if the current camera moved left or right,
based on curMinX and curMaxX attributes. We add an offset to the range in check,
so the creating of the world happens off-screen, and the game won't look odd.
if the avatar moved, we call Terrain's and Tree's createInRange() method.
The trick here is to make sure the min and max x align with the current world,
so we have to make sure they are divisible by Block.SIZE.
The same method is used for removing the objects that are out of range:
for example when the avatar moves right, the object in the offset range in
the left are removed. this is done by iterating over the game objects and 
searching their tag names (terrain, tree, or leaf) and removing them from their
respective layers.


Explanation of Tree design:
We chose to implement the trees using three classes:
a Tree class that creates the tree as a whole, a LeafGenerator class that generates
the leaves and a leaf class. In the Tree department we produce the trunk of the tree
which consists of blocks,Tree receives in its constructor the groundHeightAt function.
the tree department calls LeafGenerator which is responsible for creating the top of the tree
The Leaf class - inherits from GameObjects and is responsible for creating a leaf with
its entire life cycle .The class has four functions in addition to the functions of GameObjects,
two of which are responsible for the movement of the leaf when it is on the tree,
one is responsible for the fall of the leaf and one for "new life" 
for the leaf - to put it back in its place with the appropriate settings.
In this way, each department has its own designated responsibility,
the leaf's movement process is private at the leaf class's, and thus we maintain 
the principle of encapsulation. We chose that terrain will transfer the function 
to the tree so that the tree class does not have to know the specific 
implementation of groundHeightAt and thus there will be programming for the interface 
and not for the implementation.


Design dilemmas:
The main dilemma was how to create the infinite world. As noted above, we
decided the smart way was to have a separate class responsible for creating
the world, which includes terrain, trees and leaves. So the PepseGameManager
does nothing in respect to the world, and hands the responsibility off to
the InfiniteWorldGenerator class. 
We also decided to split the creating of leaves to two different classes,
Leaf and LeafGenerator. This is because, unlike terrain and trees, the leaves
have a lot of functionalities (size, angles, and fade-outs),
and it makes sense to have a class responsible for all of those, while
a different class positions them in the proper places.


Bonus:
We decided to create the avatar as the character of Link, from the Legend of Zelda.
We both grew up playing Zelda games and loving them, and we have fond memories.
To create a game featuring Link is very exciting!
