mikehash
<nitzan>


<Explanation of differences of UMLs>


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


<Explanation of Tree design>


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
