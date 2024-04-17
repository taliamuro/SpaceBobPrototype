import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

public class MyWorld extends SimulationWorld
{

    public MyWorld()
    {    
        super("", 800, 600, new Point2D(0.0, 0.0), 20);

        GreenfootImage background = getBackground();
        background.setColor(Color.WHITE);
        background.fill();       
        prepare();
    }

    public void act()
    {
        super.act();

        
    }

    private void prepare()
    {

        Player player = new Player();
        addObject(player,266,365);
        Platform platform = new Platform();
        addObject(platform,266,457);
        platform.setLocation(268,515);
        platform.setLocation(260,451);
        platform.setLocation(265,479);
        Platform platform2 = new Platform();
        addObject(platform2,403,391);
        Platform platform3 = new Platform();
        addObject(platform3,561,292);
        player.setLocation(12,561);
    }
}
