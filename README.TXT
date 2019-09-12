------------------------------------------------------------------------
GravField: A 2d "gravity field" simulator that allows the user to generate
spheres that accelerate towards each other at a rate proportional to their
sizes, and generates waves upon collisions.
BouncyBalls: A 2d simulator of bouncy balls that collide elastically and
also produce waves upon collision.
------------------------------------------------------------------------

PROJECT TITLE: GravField and BouncyBalls
PURPOSE OF PROJECT: Using JPanel to draw and simulate waves, elastic collisions, and gravity in 2d. 
HOW TO START THIS PROJECT: Download BlueJ and open package.bluej in it. Right-click on the GravField class for the gravity field, or BackgroundDriver class for the BouncyBalls, and click on "void main(String[] args)", then click "OK".
AUTHORS: Andrew Wang
USER INSTRUCTIONS: For both, pressing and holding generates a sphere of increasing size corresponding to the length of time held for. Holding and dragging gives the sphere an initial velocity.

GravField has several issues currently: The gravity field warps in response to the size of the sphere on top of it, but has unexplained "spazzes" especially with larger spheres. Collisions between spheres don't work out particularly well, with issues ranging from bouncing off in the wrong direction to failing to recognize a boundary during collisions and "falling into" each other.
