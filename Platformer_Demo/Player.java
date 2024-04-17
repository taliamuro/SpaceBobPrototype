import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends SimulationActor
{
    public int jumpCount = 0;
    public int maxJumpCount = 2;
    public int jumpHeight = 5;
    public int playerSpeed = 5;
    public boolean jumpPressed  = false;
    public Player()
    {
        super(); 
        this.acceleration = new Vector2D(0.0,-10.0);
    }
    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        super.act();
        
        checkCollision();
        processInput();
        
        // Add your action code here.
    }
    public void processInput()
    {
       // Process move input 
       if (Greenfoot.isKeyDown("right")) 
       { 
            this.velocity = new Vector2D(playerSpeed, this.velocity.getY());   
       }
       
       else if (Greenfoot.isKeyDown("left")) 
       { 
            this.velocity = new Vector2D(-playerSpeed, this.velocity.getY());      
       }
       else if (!Greenfoot.isKeyDown("right") && !Greenfoot.isKeyDown("left"))
       {
           this.velocity = new Vector2D(0, this.velocity.getY());  
       }
       
       if (Greenfoot.isKeyDown("space") && jumpCount < maxJumpCount) 
       { 
            this.velocity = new Vector2D(this.velocity.getX(), jumpHeight);   
            jumpPressed = true;
       }
       
       if (jumpPressed && !Greenfoot.isKeyDown("space")) 
       { 
            ++jumpCount;
            jumpPressed = false;
            this.velocity = new Vector2D(this.velocity.getX(), this.velocity.getY() );      
       }


    }
    private void checkPlatformCollision() {
    Platform platform = (Platform)getOneIntersectingObject(Platform.class);
    if (platform != null) {
        int playerBottom = getY() + getImage().getHeight() / 2;
        int playerTop = getY() - getImage().getHeight() / 2;
        int platformTop = platform.getY() - platform.getImage().getHeight() / 2;
        int platformLeft = platform.getX() - platform.getImage().getWidth() / 2 + platform.offset;
        int platformRight = platform.getX() + platform.getImage().getWidth() / 2;
        int platformBottom = platform.getY() + platform.getImage().getHeight(); 
        
        // We check if the feet of the player are in or connected to the platform
        // So that he can go throug it from the bottom but wont pass through it from the top
        if (playerBottom >= platformTop && playerBottom <= platformBottom) {
            setLocation(getX(), platformTop - getImage().getHeight() / 2);
            // He is on the ground the velocity in Y is 0;
            this.velocity = new Vector2D(this.velocity.getX(), 0.0);
            // We reset the player jump count
            jumpCount = 0;
        }
       
    }
    }
    public void checkCollision()
    {
        World world = getWorld();
        
        int worldWidth = world.getWidth();
        int worldHeight = world.getHeight();
        int imageWidth = getImage().getWidth();
        int imageHeight = getImage().getHeight();
        // check Collision with platform 
        checkPlatformCollision();
        
        // Check collision with lower edge of world
        if (getY() + imageHeight / 2 >= worldHeight) 
        {
            setLocation(getX(), worldHeight - imageHeight / 2);
            this.velocity = new Vector2D(this.velocity.getX(), 0.0);
            jumpCount = 0; // Reset jump count when hitting the bottom edge
        }
         // Left edge collision
        if (getX() - imageWidth / 2 <= 0) 
        {
            setLocation(imageWidth / 2, getY());
            this.velocity = new Vector2D(0.0, this.velocity.getY());
            
            
            
            
            
            
        }
        
        // Right edge collision
        if (getX() + imageWidth / 2 >= worldWidth) {
            setLocation(worldWidth - imageWidth / 2, getY());
            this.velocity = new Vector2D(0.0, this.velocity.getY());
            
            World gameWonWorld =  new  MyWorld();
            Greenfoot.setWorld(gameWonWorld);
            
        }
    }
    
}
