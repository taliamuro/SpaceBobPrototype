import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Graphics2D; 
import java.awt.image.*; 
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class SimulationWorld extends World
{
    
    // The following are in World Coordinates
    protected final double DEFAULT_CAMERA_WIDTH;
    protected Point2D cameraCenter;
    protected double cameraWidth;
    
    // For conversion between World and Window Coordinates
    private Matrix2D toWindowMatrix;
    private Matrix2D toWorldMatrix;

    // To dynamically calculate the time step duration
    private long lastFrameTimeMS;
    private double timeStepDuration;

    // Looping music
    protected GreenfootSound music;
    
    
    public SimulationWorld(String musicFile, int windowWidth, int windowHeight, Point2D cameraCenter, double cameraWidth)
    {    
        super(windowWidth, windowHeight, 1, false); 
        
        // Setup music
        if (musicFile != null && musicFile.isEmpty() == false)
        {
            music = new GreenfootSound(musicFile);
        }
        
        // Save the initial width to compute the zooming ratio
        DEFAULT_CAMERA_WIDTH = cameraWidth;
        
        // Save Camera Parameters
        this.cameraCenter = cameraCenter;
        this.cameraWidth = cameraWidth;
        
        // Conversion matrices
        this.toWindowMatrix = Matrix2D.worldToWindow(cameraCenter, cameraWidth, new Vector2D(getWidth(), getHeight()));
        this.toWorldMatrix = Matrix2D.windowToWorld(cameraCenter, cameraWidth, new Vector2D(getWidth(), getHeight()));
        
    }
    
    public void act()
    {
        // Update time step duration
        timeStepDuration = (System.currentTimeMillis() - lastFrameTimeMS) / 1000.0;
        lastFrameTimeMS = System.currentTimeMillis();

        // Update Viewport Matrix
        toWindowMatrix = Matrix2D.worldToWindow(cameraCenter, cameraWidth, new Vector2D(getWidth(), getHeight()));
        toWorldMatrix = Matrix2D.windowToWorld(cameraCenter, cameraWidth, new Vector2D(getWidth(), getHeight()));
    }
    
    public void started()
    {
        // Reset the current time when starting game
        lastFrameTimeMS = System.currentTimeMillis();
        
        // Start music when starting game
        if (music != null)
        {
            music.playLoop();
        }
    }

    public void stopped()
    {
        // Pause music when pausing the game
        if (music != null)
        {
            music.pause();
        }        
    }
    
    public GreenfootSound getMusic()
    {
        return music;
    }
    
    public void setMusic(GreenfootSound newMusic)
    {
        // If the music is playing, stop the current music
        if (music != null && music.isPlaying() == true)
        {
            music.stop();
            newMusic.playLoop();
        }
        
        music = newMusic;
    } 
    
    public void transitionToWorld(SimulationWorld newWorld)
    {
        stopped();
        Greenfoot.setWorld(newWorld);
        
        // Continue playing current music is new world has none
        if (newWorld.getMusic() == null)
        {
            newWorld.setMusic(music);
        }
        
        newWorld.started();
    }

    
    public double getTimeStepDuration()
    {
        return timeStepDuration;
    }

    public double worldToWindow(double worldValue)
    {
        return worldValue * getWidth() / cameraWidth;
    }

    public Point2D worldToWindow(Point2D worldCoord)
    {
        return toWindowMatrix.transform(worldCoord);
    }

    public Vector2D worldToWindow(Vector2D worldCoord)
    {
        return toWindowMatrix.transform(worldCoord);
    }
    
    public double windowToWorld(double windowValue)
    {
        return windowValue * cameraWidth / getWidth();
    }
    
    public Point2D windowToWorld(Point2D windowCoord)
    {        
        return toWorldMatrix.transform(windowCoord);
    }

    public Vector2D windowToWorld(Vector2D windowCoord)
    {        
        return toWorldMatrix.transform(windowCoord);
    }
    
    public void scaleActors()
    {
        List<SimulationActor> actors = getObjects(SimulationActor.class);
        
        for (int i=0; i<actors.size(); i++)
        {
            SimulationActor actor = actors.get(i);
            actor.scaleImage(getZoomRatio());
        }
    }
    
    public double getZoomRatio()
    {
        return DEFAULT_CAMERA_WIDTH / cameraWidth;
    }
    
    public void addObject(Actor a, int x, int y)
    {
        super.addObject(a,x,y);
        
        // Set World Position from Window Position for Simulation Actors
        if (a instanceof SimulationActor)
        {
            SimulationActor sa = (SimulationActor) a;
            sa.setPosition(windowToWorld(new Point2D(x, y)));
        }            
    }
}
