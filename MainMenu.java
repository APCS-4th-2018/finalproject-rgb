import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.effect.BlurType;
import javafx.scene.shape.Shape;
import javafx.geometry.Insets;
import javafx.scene.effect.InnerShadow;

/**
 * MainMenu allows the user to start the game, open up a "How To" play the game, look at the
 * credits, and also read what the plot of the game is 
 *
 * @author Jonathan Lee & Daniel Herrick
 * @version MainMenu JavaFx class - RGB
 */
public class MainMenu extends Application
{
    //Declaration of buttons that will be used in the main menu
    //Button that starts the game
    private Button playButton; 
    //Button that allows the user to go back to the MainMenu
    private Button backToMenuButton; 
    //Button that brings the user to a credit page
    private Button creditsButton; 
    //Button that brings the user to a set of instruction
    private Button howToButton;
    //Button that brings the user to the plot of the game
    private Button plotButton; 
    
    //A top-level container that hosts a Scene
    private Stage window;
    
    //The scenes represent the physical contents of a JavaFX application in the mainMenu, creditMenu, playMenu, and plotScene
    private Scene mainScene, creditScene, playScene, plotScene; 
   
    // /**
     // * This the constructor for MainMenu that will construct all the buttons
     // * and run the main menu via runMenu() 
     // */
    // public MainMenu()
    // {
        
    // }
    
     //*The main() method is ignored in correctly deployed JavaFX application.
     /**
     * Main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support]
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage stage) throws Exception {
        //Instatiation of the Stage window
        window = stage;
        
        //Have the music to play automatically as soon as the user opens the menu
        Music.loop("ollie.mp3");
        //Declaration and Instantiaion of color
        Color c = Color.BLACK;
        
        //Adding the special effects of shadows on the characters
        InnerShadow innerShadow = new InnerShadow();
        //setting the type of blur for the shadow 
        innerShadow.setBlurType(BlurType.GAUSSIAN); 
        //Setting color for the shadow 
        innerShadow.setColor(Color.DARKGRAY); 
        
        //Adds an image to the button
        playButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("Play.PNG"), 100, 100, true, false)));
        // Make button backgrounds transparent
        playButton.setStyle("-fx-background-color: transparent;");
        playButton.setEffect(innerShadow);
        
        //Adds an image to the button
        creditsButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("Credits.PNG"), 150, 150, true, false)));
        // Make button backgrounds transparent
        creditsButton.setStyle("-fx-background-color: transparent;");
        creditsButton.setEffect(innerShadow);
        
        backToMenuButton = new Button("Back To Menu");
        
        //Adds an image to the button
        howToButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("Howto.PNG"), 250, 250, true, false)));
        //Makes the background of the button transparent
        howToButton.setStyle("-fx-background-color: transparent;");
        howToButton.setEffect(innerShadow);
        
        //Adds an image to the button
        plotButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("Gameplot.PNG"), 250, 250, true, false)));
        // Make button backgrounds transparent
        plotButton.setStyle("-fx-background-color: transparent;");
        plotButton.setEffect(innerShadow);
        
        //Sets the width of the button to a set size
        playButton.setMaxWidth(70);
        playButton.setMaxHeight(70);
        howToButton.setMaxWidth(70);
        howToButton.setMaxHeight(70);
        creditsButton.setMaxWidth(70);
        creditsButton.setMaxHeight(70);
        plotButton.setMaxWidth(70);
        plotButton.setMaxHeight(70);
        
        //This class will handle the button event when user hits Play, How To, Game Plot, and Credits (changes the scene)
        playButton.setOnAction(this::characterSelect);
        howToButton.setOnAction(this::howTo);
        creditsButton.setOnAction(this::credits);
        plotButton.setOnAction(this::plot);
        
        //Layout for the Main scene - children are laid out in a veritcal column
        VBox main = new VBox(20);
        
        //Centers the layout in the middle of the scene
        main.setAlignment(Pos.CENTER);
        
        //Declaration and instantiation of a label
        Label title = new Label("", new ImageView(new Image(getClass().getResourceAsStream("Title.PNG"), 300, 300, true, false))); 
        title.setStyle("-fx-background-color: transparent;");
        title.setEffect(innerShadow);
        
        //Adds the buttons and label onto the layout of the scene
        main.getChildren().addAll(title, playButton, plotButton, howToButton, creditsButton);
        
       //Instantiation of the scene in MainMenu
        mainScene = new Scene(main, 900, 600, c);
        
        //Set the color of the background to Gainsboro
        main.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));

        //User can not resize the form
        window.setResizable(false);
        
        //Sets the title of the window
        window.setTitle("RGB GANG");
        window.setScene(mainScene);
        
        //Stage will now appear so that the user can interact with it
        window.show();
    }
    
    /**
     * This method will be called when the user clicks the play button in the Character Select scene. It
     * will switch screens to the actual game
     * 
     * @param pClick - an event representing the user clicking the play button
     */
    private void play(ActionEvent pClick)
    {
        //Layout for Credit Scene
        VBox creditLayout = new VBox(20);
        
        //Centers the layout of the Credit scene in the middle
        creditLayout.setAlignment(Pos.CENTER);
        creditLayout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        
        //This class will handle the button event when user hits Back To Menu
        playScene = new Scene(creditLayout, 900, 600);
        window.setScene(creditScene);
    }
    
    /**
     * This method will be called when the user clicks the play button in the Main Menu scene. It
     * will switch screens to a scene where the user can select a character
     * 
     * @param characterSelectClick - an event representing the user clicking the play button
     */
    private void characterSelect(ActionEvent characterSelectClick)
    {
        //Adding the special effects of shadows on the characters
        InnerShadow innerShadow = new InnerShadow();
        //setting the type of blur for the shadow 
        innerShadow.setBlurType(BlurType.GAUSSIAN); 
        //Setting color for the shadow 
        innerShadow.setColor(Color.DARKGRAY); 
        
        //Layout for the top part of the CharacterSelect Scene
        HBox topPart = new HBox();
        Label title = new Label("Character Select");
        topPart.getChildren().addAll(title);
        
        //Layout for the middle part of the CharacterSelect Scene
        HBox middlePart = new HBox(20);
        Button firstCharacter = new Button("Yello");
        Button secondCharacter = new Button("Orange");
        Button thirdCharacter = new Button("Purple");
        middlePart.getChildren().addAll(firstCharacter, secondCharacter, thirdCharacter);
        
        //Layout for the middle part of the CharacterSelect Scene
        HBox bottomPart = new HBox(20);
        Button backToMenu = new Button("Back to Menu");
        Button play = new Button("", new ImageView(new Image(getClass().getResourceAsStream("Play.PNG"), 50, 50, true, false)));
        // Make button backgrounds transparent
        playButton.setStyle("-fx-background-color: transparent;");
        playButton.setEffect(innerShadow);
        bottomPart.getChildren().addAll(backToMenu, play);
        
        //Centers the layout of the Credit scene in the middle
        middlePart.setAlignment(Pos.CENTER);
        
        //Layout for the bottom part of the CharacterSelect Scene
        BorderPane characterLayout = new BorderPane();
        characterLayout.setTop(topPart);
        characterLayout.setCenter(middlePart);
        characterLayout.setBottom(bottomPart);
        characterLayout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        
        //This class will handle the button event when user hits Back To Menu
        backToMenuButton.setOnAction(e -> window.setScene(mainScene));
        playButton.setOnAction(this::play);
        playScene = new Scene(characterLayout, 900, 600);
        
        //Display the scene to the user so that he/she can interact with it
        window.setScene(creditScene);
    }
    
    /**
     * This method will be called when the user clicks the how to play button. It
     * When clicked, it will switch screens and show instructions on how to
     * play.
     * 
     * @param howToClick - an event representing the user clicking the how to
     * play button
     */
    public void howTo(ActionEvent howtoClick)
    {
        //Layout for Credit Scene
        VBox instructionLayout = new VBox(20);
        
        //Centers the layout of the Credit scene in the middle
        instructionLayout.setAlignment(Pos.CENTER);
        
        //Set the color of the background to Gainsboro
        instructionLayout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Declaration and Instantiation of the Credit Texts
        Text instructionText = new Text();
       
        //Setting the font to Baskerville
        instructionText.setFont(Font.font("Baskerville", 20));
        
        String instructionString = "Press the arrow keys to move up, left, or right \n" 
                              + "Anything that is red will automatically kill you \n" 
                              + "Blue walls allow you to double jump while green wall causes you to stick to walls \n"
                              + "Complete all three sectors to beat the game\n"
                              + "Good luck Adventurer, may the RGB guide you through the game! \n";

        //Setting the value of the creditText to the string creditString
        instructionText.setText(instructionString);
        instructionText.setFill(Color.WHITE);
        
        //Centering the text into middle of the scene
        instructionText.setTextAlignment(TextAlignment.CENTER);
        
        //Adding nodes to the layout
        instructionLayout.getChildren().add(instructionText);
        instructionLayout.getChildren().add(backToMenuButton);
        
        //This class will handle the button event when user hits Back To Menu
        backToMenuButton.setOnAction(e -> window.setScene(mainScene));
        creditScene = new Scene(instructionLayout, 900, 600);
        
        //Display the scene to the user so that he/she can interact with it
        window.setScene(creditScene);
    }
    
    /**
     * This method will be called when the user clicks the credits button. It
     * will switch screens to a credits screen that displays all necessary 
     * credits of the game 
     * 
     * @param cClick - an event representing the user clicking the credits 
     * button 
     */
    private void credits(ActionEvent cClick)
    {
        //Layout for Credit Scene
        VBox creditLayout = new VBox(20);
        
        //Set the color of the background to Gainsboro
        creditLayout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        
        //Centers the layout of the Credit scene in the middle
        creditLayout.setAlignment(Pos.CENTER);
        
        //Declaration and Instantiation of the Credit Texts
        Text creditText = new Text();
        
        //Setting the font to Baskerville
        creditText.setFont(Font.font("Baskerville", 20));
        
        String creditString = "THANK YOU to those who helped made RGB possible \n\n" 
                              + "Creators - Daniel Herrick, Jonathan Lee, Rupin Mittal, and Brandon Wang \n"
                              + "Graphics by Maria Dhilla and Music by Cole Clarkson (IG: @cudi_cole)\n"                      
                              + "Thank you Mr. Lantsberger for everything you've taught us. \n" 
                              + "It has been our pleasure being your students!";

        //Setting the value of the creditText to the string creditString
        creditText.setText(creditString);
        creditText.setFill(Color.WHITE);
        
        //Centering the text into middle of the scene
        creditText.setTextAlignment(TextAlignment.CENTER);
        
        //Adding nodes to the layout
        creditLayout.getChildren().add(creditText);
        creditLayout.getChildren().add(backToMenuButton);
        
        //This class will handle the button event when user hits Back To Menu
        backToMenuButton.setOnAction(e -> window.setScene(mainScene));
        creditScene = new Scene(creditLayout, 900, 600);
        
        //Display the scene to the user so that he/she can interact with it
        window.setScene(creditScene);
    }
    
    /**
     * This method will be called when the user clicks the plot button. It
     * will switch screens to a Game Plot scene that displays the plot of the game
     * 
     * 
     * @param howToClick - an event representing the user clicking the how to
     * play button
     */
    public void plot(ActionEvent howtoClick)
    {
        //Layout for Plot Scene
        VBox plotLayout = new VBox(20);
        
        //Centers the layout of the Credit scene in the middle
        plotLayout.setAlignment(Pos.CENTER);
        
        //Set the color of the background to Gainsboro
        plotLayout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Declaration and Instantiation of the Credit Texts
        Text plotText = new Text();
       
        //Setting the font to Baskerville
        plotText.setFont(Font.font("Baskerville", 20));
        
        String plotString = "Year 2050, humanity suffers from a catastrophic nuclear apocalypse. \n" 
                              + "As one of the last survivor on Earth, you search for a way \n"   
                              + "to go back in time to prevent the destruction of humanity. \n" 
                              + "As you are wandering through a dingy wasteland, you encounter a  \n"
                              + "Time Machine Laboratory in the mountains. \n"
                              + "You enter the lab and discover that if you pass a series of tests known as the RGB sectors, \n"
                              + "you will be able to go back in time to stop the nuclear apocalypse from occurring \n"
                              + "Go and save our world, if you can little hero!";

        //Setting the value of the plotText to the string plotString
        plotText.setText(plotString);
        plotText.setFill(Color.WHITE);
        
        //Centering the text into middle of the scene
        plotText.setTextAlignment(TextAlignment.CENTER);
        
        //Adding nodes to the layout
        plotLayout.getChildren().add(plotText);
        plotLayout.getChildren().add(backToMenuButton);
        
        //This class will handle the button event when user hits Back To Menu
        backToMenuButton.setOnAction(e -> window.setScene(mainScene));
        plotScene = new Scene(plotLayout, 900, 600);
        
        //Display the scene to the user so that he/she can interact with it
        window.setScene(plotScene);
    }
}