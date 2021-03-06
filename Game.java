/**
 * Class to actually run the game
 * @author Rupin Mittal and Brandon Wang
 * @version May 29, 2019
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Game extends Application
{
    //object variables
    MainMenu mainMenu;                          //the mainmenu object
    private Environment currentEnvironment;     //the current environment being used
    private Environment gameEnvironment;        //the game environment
    private IntroEnvironment introEnvironment;  //the intro environment
    private Player player;                      //the player
    private NormalWall nWall;                   //the normal wall
    private BlueWall bWall;                     //the blue wall
    private RedWall rWall;                      //the red wall
    private GreenWall gWall;                    //the green wall
    private AnimationTimer animationTimer;      //the animation timer to run everything
    private Wall colliderWall;                  //the wall that the user is colliding into in collisions

    //variables for movement
    private boolean up, down, right, left;      //the variables for the players movement
    private boolean escape;                     //for escaping the game
    private double cameraOffset;                //the variable to offset the screen for scrolling
    private double futureXVel;                  //the future horizontal velocity
    private double futureYVel;                  //the future vertical velocity
    private double futureX;                     //future horizontal position
    private double futureY;                     //future vertical position
    private boolean isOnGreenHorizontal;        //true if character is on green floor or ceiling, else false
    private boolean isOnGreenVertical;          //true if character is on green wall, else false
    private int sectorNum;                      //the sector number that we are on

    //constants
    private final double X_ACC = 8, FRICT_ACC = 5, GRAV_ACC = 6.9, JUMP_ACC = 5.5, MAX_VEL = 4; //the constants for movement
    private final int TILE_SIZE = 30;    //the tile size

    //variables for the actual display of the game
    private ImageView environment;           //the environment being displayed
    private Group root;                      //the Group
    private Scene scene;          //the scene

    public Game(Player newPlayer)
    {
        player = newPlayer;
    }

    //methods to run class
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        initializeVariables();      //initialize all the variables
        //initialize the display
        root.getChildren().add(environment);
        root.getChildren().add(player.getImageView());
        primaryStage.setTitle("RGB");
        primaryStage.setHeight(780);
        primaryStage.setWidth(1200);
        primaryStage.setScene(scene);
        
        //player initial position
        player.setXPos(0);
        player.setYPos(135);
        primaryStage.show();

        //run controls
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {            //on keys pressed
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    up = true; break;
                    case LEFT:  left = true; break;
                    case RIGHT: right = true; break;
                    case DOWN:  down = true; break;
                    case ESCAPE:  escape = true; break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {           //on keys released
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    up = false; break;
                    case LEFT:  left = false; break;
                    case RIGHT: right = false; break;
                    case DOWN:  down = false; break;
                    case ESCAPE:  escape = false; break;
                }
            }
        });

        //AnimationTimer to run game
        AnimationTimer timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                if(player.isAlive())
                {
                    //handling green stuff
                    //checks 4 corners and 2 edges for green walls
                    isOnGreenVertical = currentEnvironment.getTypeNumber(player.getXPos() + player.getWidth() + 1, player.getYPos()) == 10
                        || currentEnvironment.getTypeNumber(player.getXPos() + player.getWidth() + 1, player.getYPos() + player.getHeight() / 2) == 10
                        || currentEnvironment.getTypeNumber(player.getXPos() + player.getWidth() + 1, player.getYPos() + player.getHeight() - 1) == 10
                        || currentEnvironment.getTypeNumber(player.getXPos() - 1, player.getYPos()) == 9
                        || currentEnvironment.getTypeNumber(player.getXPos() - 1, player.getYPos() + player.getHeight() / 2) == 9
                        || currentEnvironment.getTypeNumber(player.getXPos() - 1, player.getYPos() + player.getHeight() - 1) == 9;
                        //true if character is on green floor or ceiling, else false
                    
                    //checks 4 corners for green floors and ceilings
                    isOnGreenHorizontal = currentEnvironment.getTypeNumber(player.getXPos() + player.getWidth(), player.getYPos() - 1) == 12
                        || currentEnvironment.getTypeNumber(player.getXPos() + player.getWidth(), player.getYPos() + player.getHeight() + 1) == 11
                        || currentEnvironment.getTypeNumber(player.getXPos(), player.getYPos() - 1) == 12
                        || currentEnvironment.getTypeNumber(player.getXPos(), player.getYPos() + player.getHeight() + 1) == 11;
                        //true if character is on green wall, else false
                    
                    //initialize instance variables
                    futureXVel = player.getXVel();              //get the future horizontal velocity
                    futureYVel = player.getYVel();              //get the future vertical velocity
                    futureX = player.getXPos();                 //get the future x position
                    futureY = player.getYPos();                 //get the future y position

                    //make changes to the velocity with the constants
                    futureXVel -= Math.signum(player.getXVel()) * FRICT_ACC/30;     //apply friction
                    
                    //Stopping player if velocity passes 0 (friction)
                    if((int)Math.signum(futureXVel) == -1 * (int)Math.signum(player.getXVel())
                        && ((!left && !right) || (left && right)))
                        futureXVel = 0;

                    //keypresses different depending if on green or not
                    if(isOnGreenHorizontal || isOnGreenVertical)
                    {
                        if(isOnGreenHorizontal) //green floor or ceiling
                        {
                            //delete y velocity lmao
                            futureYVel = 0;
                            
                            //Stopping player if velocity passes 0 due to friction
                            if((int)Math.signum(futureXVel) == -1 * (int)Math.signum(player.getXVel())
                                && ((!left && !right) || (left && right)))
                                futureXVel = 0;
                            //keypresses
                            if(left) //left and on floor/ceiling
                                futureXVel += -1 * X_ACC / 30;
                            if(right) //right and on floor/ceiling
                                futureXVel += X_ACC / 30;
                            
                            //capping velocity
                            if(Math.abs(futureXVel) > MAX_VEL / 2)//if player passes max velocity
                                futureXVel = MAX_VEL * Math.signum(futureXVel) / 2; //then limit the velocity
                        }
                        if(isOnGreenVertical) //green wall
                        {
                            //apply friction
                            futureXVel = 0;
                            futureYVel -= Math.signum(player.getYVel()) * FRICT_ACC/30;
                            
                            //Stopping player if velocity passes 0 due to friction
                            if((int)Math.signum(futureYVel) == -1 * (int)Math.signum(player.getYVel())
                                && ((!up && !down) || (up && down)))
                                futureYVel = 0;
                            //keypresses
                            if(up) //up and on wall
                                futureYVel += -1 * X_ACC / 30;
                            if(down) //down and on wall
                                futureYVel += X_ACC / 30;
                                
                            //capping velocity
                            if(Math.abs(futureYVel) > MAX_VEL / 2) //if player passes max velocity
                                futureYVel = MAX_VEL * Math.signum(futureYVel) / 2; //then limit the velocity
                        }
                    }
                    else
                    {
                        //apply gravity
                        futureYVel += GRAV_ACC/30;                                      //apply gravity
                        
                        //up and touching ground
                        if(up && (currentEnvironment.isCollision(player.getXPos(), futureY + player.getHeight() + 1) 
                              || currentEnvironment.isCollision(player.getXPos() + player.getWidth(), futureY + player.getHeight() + 1)))
                            futureYVel = -1 * JUMP_ACC;
                        if(left && player.getXVel() > -1 * MAX_VEL) //left acceleration under max velocity
                            futureXVel -= X_ACC / 30;
                        if(right && player.getXVel() < MAX_VEL) //right acceleration under max velocity
                            futureXVel += X_ACC / 30;
                        
                        //capping velocity
                        if(Math.abs(futureXVel) > MAX_VEL && Math.abs(player.getXVel()) <= MAX_VEL)//if player passes max velocity
                            futureXVel = MAX_VEL * Math.signum(futureXVel); //then limit the velocity
                    }

                    //update the player's future position
                    futureX += futureXVel;
                    futureY += futureYVel;

                    //collision checks
                    boolean horizontalCollision = false;
                    boolean verticalCollision = false;
                    
                    //top left corner of player
                    if(currentEnvironment.isCollision(futureX, futureY))
                    {
                        colliderWall = getColliderWall(futureX, futureY);
                        
                        //top left collision with ceiling
                        if(currentEnvironment.isCollision(player.getXPos(), futureY))
                        {
                            //wall matches
                            if(currentEnvironment.getTypeNumber(player.getXPos(), futureY) % 4 == 0)
                                colliderWall.interactCeiling(futureY);
                            else //normal wall
                                nWall.interactCeiling(futureY);
                            verticalCollision = true;
                        }
                        //top left collision with left wall
                        if(currentEnvironment.isCollision(futureX, player.getYPos()))
                        {
                            //wall mathces
                            if(currentEnvironment.getTypeNumber(futureX, player.getYPos()) % 4 == 1)
                                colliderWall.interactRight(futureX);
                            else //normal wall
                                nWall.interactRight(futureX);
                            horizontalCollision = true;
                        }
                    }
                    
                    //top right corner of player
                    if(currentEnvironment.isCollision(futureX + player.getWidth(), futureY))
                    {
                        colliderWall = getColliderWall(futureX + player.getWidth(), futureY);
                        
                        //top right collision with ceiling
                        if(currentEnvironment.isCollision(player.getXPos() + player.getWidth(), futureY))
                        {
                            //wall matches
                            if(currentEnvironment.getTypeNumber(player.getXPos() + player.getWidth(), futureY) % 4 == 0)
                                colliderWall.interactCeiling(futureY);
                            else //normal wall
                                nWall.interactCeiling(futureY);
                            verticalCollision = true;
                        }
                        //top right collision with right wall
                        if(currentEnvironment.isCollision(futureX + player.getWidth(), player.getYPos()))
                        {
                            //wall matches
                            if(currentEnvironment.getTypeNumber(futureX + player.getWidth(), player.getYPos()) % 4 == 2)
                                colliderWall.interactLeft(futureX + player.getWidth());
                            else //normal wall
                                nWall.interactLeft(futureX + player.getWidth());
                            horizontalCollision = true;
                        }
                    }
                    
                    //bottom left corner of player
                    if(currentEnvironment.isCollision(futureX, futureY + player.getHeight()))
                    {
                        colliderWall = getColliderWall(futureX, futureY + player.getHeight());
                        
                        //bottom left collision with floor
                        if(currentEnvironment.isCollision(player.getXPos(), futureY + player.getHeight()))
                        {
                            //wall matches
                            if(currentEnvironment.getTypeNumber(player.getXPos(), futureY + player.getHeight()) % 4 == 3)
                                colliderWall.interactFloor(futureY + player.getHeight());
                            else //normal wall
                                nWall.interactFloor(futureY + player.getHeight());
                            verticalCollision = true;
                        }
                        //bottom left collision with left wall
                        if(currentEnvironment.isCollision(futureX, player.getYPos() + player.getHeight() - 0.05))
                        {
                            //wall matches
                            if(currentEnvironment.getTypeNumber(futureX, player.getYPos() + player.getHeight() - 0.05) % 4 == 1)
                                colliderWall.interactRight(futureX);
                            else //normal wall
                                nWall.interactRight(futureX);
                            horizontalCollision = true;
                        }
                    }

                    //bottom right corner of player
                    if(currentEnvironment.isCollision(futureX + player.getWidth(), futureY + player.getHeight()))
                    {
                        colliderWall = getColliderWall(futureX + player.getWidth(), futureY + player.getHeight());
                        
                        //bottom right collision with floor
                        if(currentEnvironment.isCollision(player.getXPos() + player.getWidth(), futureY + player.getHeight()))
                        {
                            //wall matches
                            if(currentEnvironment.getTypeNumber(player.getXPos() + player.getWidth(), futureY + player.getHeight()) % 4 == 3)
                                colliderWall.interactFloor(futureY + player.getHeight());
                            else //normal wall
                                nWall.interactFloor(futureY + player.getHeight());
                            verticalCollision = true;
                        }
                        //bottom right collision with right wall
                        if(currentEnvironment.isCollision(futureX + player.getWidth(), player.getYPos() + player.getHeight() - 0.05))
                        {
                            //wall matches
                            if(currentEnvironment.getTypeNumber(futureX + player.getWidth(), player.getYPos() + player.getHeight() - 0.05) % 4 == 2)
                                colliderWall.interactLeft(futureX + player.getWidth());
                            else //normal wall
                                nWall.interactLeft(futureX + player.getWidth());
                            horizontalCollision = true;
                        }
                    }

                    //left edge of player
                    if(currentEnvironment.isCollision(futureX, futureY + player.getHeight() / 2))
                    {
                        colliderWall = getColliderWall(futureX, futureY + player.getHeight() / 2);
                        
                        //left collision with left wall
                        if(currentEnvironment.isCollision(futureX, player.getYPos() + player.getHeight() / 2))
                        {
                            //wall matches
                            if(currentEnvironment.getTypeNumber(futureX, player.getYPos() + player.getHeight() / 2) % 4 == 1)
                                colliderWall.interactRight(futureX);
                            else //normal wall
                                nWall.interactRight(futureX);
                            horizontalCollision = true;
                        }
                    }

                    //right edge of player
                    if(currentEnvironment.isCollision(futureX + player.getWidth(), futureY + player.getHeight() / 2))
                    {
                        colliderWall = getColliderWall(futureX + player.getWidth(), futureY + player.getHeight() / 2);
                        
                        //right collision with right wall
                        if(currentEnvironment.isCollision(futureX + player.getWidth(), player.getYPos() + player.getHeight() / 2))
                        {
                            //wall matches
                            if(currentEnvironment.getTypeNumber(futureX + player.getWidth(), player.getYPos() + player.getHeight() / 2) % 4 == 2)
                                colliderWall.interactLeft(futureX + player.getWidth());
                            else //normal wall
                                nWall.interactLeft(futureX + player.getWidth());
                            horizontalCollision = true;
                        }
                    }
                    
                    if(!horizontalCollision)
                    {
                        player.setXPos(futureX);
                        player.setXVel(futureXVel);
                    }
                    if(!verticalCollision)
                    {
                        player.setYPos(futureY);
                        player.setYVel(futureYVel);
                    }

                    if(player.isAlive())
                    {
                        //update the animation that is being run
                        player.updateAnimation();
                        
                        //if camera need to scroll
                        if(player.getXPos() - cameraOffset > 700)
                        {
                            cameraOffset = player.getXPos() - 700;
                            environment.setTranslateX(-1 * cameraOffset);
                            player.getImageView().setTranslateX(-1 * cameraOffset);
                        }
                        else
                            if(player.getXPos() - player.getWidth() - cameraOffset < 500)
                            {
                                cameraOffset = player.getXPos() - player.getWidth() - 500;
                                environment.setTranslateX(-1 * cameraOffset);
                                player.getImageView().setTranslateX(-1 * cameraOffset);
                            }
                        //limit camera scrolling
                        //left edge of screen
                        if(cameraOffset < 0)
                        {
                            //set offset to left edge
                            cameraOffset = 0;
                            environment.setTranslateX(-1 * cameraOffset);
                            player.getImageView().setTranslateX(-1 * cameraOffset);
                        }
                        else //right edge
                            if(cameraOffset > environment.getImage().getWidth() - primaryStage.getWidth())
                            {
                                //set offset to right edge
                                cameraOffset = environment.getImage().getWidth() - primaryStage.getWidth();
                                environment.setTranslateX(-1 * cameraOffset);
                                player.getImageView().setTranslateX(-1 * cameraOffset);
                            }

                        if(player.getXPos() > environment.getImage().getWidth() - TILE_SIZE)
                            moveToNextSector();
                        
                        //kill player if outside map
                        if(player.getXPos() < 0 || player.getYPos() < 0 || player.getYPos() > primaryStage.getHeight())
                            resetToSectorStart();

                        //check if interacting with enemies
                        //if player and enemy's position is the same, kill the player

                    }
                    else
                        resetToSectorStart();
                }
                //else
                    //resetToSectorStart();
            }
        };
        timer.start();
    }

    //methods
    /*
     * Method to initialize the variables in the Game class
     */
    private void initializeVariables()
    {
        gameEnvironment = new Environment("sectors/Sector1.txt", "sectors/Sector1.png");   //create first game environment
        //introEnvironment = new IntroEnvironment("IntroCollisionsData.txt", "IntroMap.png", "IntroForeground.png", "IntroBackground.png");    //create intro environment
        currentEnvironment = gameEnvironment;
        //player = mainMenu.getCharacter();              //initialize player
        nWall = new NormalWall(player, TILE_SIZE);  //initialize the wall variables
        bWall = new BlueWall(player, TILE_SIZE);
        gWall = new GreenWall(player, TILE_SIZE);
        rWall = new RedWall(player);
        environment = gameEnvironment.getMapImageView(); //get environment imageview
        root = new Group();                              //the Group
        scene = new Scene(root);                         //the scene

        sectorNum = 1; //sector num the player is on
        }

    /*
     * Method to get the object of the type of wall by color
     * @param nextX the next horizontal position being moved to
     * @param nextY the next vertical position being moved to
     */
    private Wall getColliderWall(double nextX, double nextY)
    {
        Wall wall;                                                //the wall object that will be returned
        int typeNumber = currentEnvironment.getTypeNumber(nextX, nextY); //get the wall number

        if((typeNumber >= 1) && (typeNumber <= 4))      //if the wall number is 1, 2, 3, 4
            wall = nWall;
        else
            if((typeNumber >= 5) && (typeNumber <= 8))      //if the wall number is 5, 6, 7, 8
                wall = rWall;
            else
                if((typeNumber >= 9) && (typeNumber <= 12))      //if the wall number is 9, 10, 11, 12
                    wall = gWall;
                else
                    wall = bWall;                                //else blue wall

        return wall;
    }

    /*
     * Method to reset the player to the start of sector 1 after they die
     */
    private void resetToSectorStart()
    {
        player.setAliveStatus(true);           //revive player
        player.setXPos(0);                     //reset to sector beginning
        player.setYPos(135);
        player.setXVel(0);                     //reset player velocity
        player.setYVel(0);
    }

    /*
     * Method to move to the next sector
     */
    private void moveToNextSector()
    {
        sectorNum++;    //move to next sector
        //if(sectorNum == 3)
            //show win
        if(sectorNum <= 3)
        {
            //remove environment from scene
            root.getChildren().remove(environment);
            root.getChildren().remove(player.getImageView()); //player removed to resolve overlay issues
            
            gameEnvironment = new Environment("sectors/Sector" + sectorNum + ".txt", "sectors/Sector" + sectorNum + ".png");   //create first game environment
            currentEnvironment = gameEnvironment;
            environment = currentEnvironment.getMapImageView(); //get environment imageview
            
            //add elements back to scene
            root.getChildren().add(environment);
            root.getChildren().add(player.getImageView());
        }
        else
            if(sectorNum == 4)
            {
                root.getChildren().remove(environment);
                root.getChildren().remove(player.getImageView());
                cameraOffset = 0;
                root.getChildren().add(new ImageView(new Image("Victory.PNG", 0, 800, true, false)));
            }

        resetToSectorStart();
    }
}