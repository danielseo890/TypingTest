package typingtest;

import edu.macalester.graphics.*;
import java.awt.Color;

/**
 * Visual representation of each character in the text that can be changed and keeps track of the user's accuracy
 * @Author Kien Nguyen, Daniel Seo
 */
public class LetterBlock extends GraphicsText {
    private String value;
    private int score = 0; //0 if not typed, -1 if wrong, 1 if right

    /**
     * Create the visual of a character, setting its original color and internal value
     */
    public LetterBlock(char letter) {
        super();
        value = Character.toString(letter);
        setText(value);
        setFillColor(new Color(0xBDBDBD));
        setFont("lucida sans typewriter", FontStyle.PLAIN, 30);
    }

    /**
     * A non-visual block to keep track of spaces
     */
    public LetterBlock(String name) {
        this(' ');
        value = name;
        setText("");
    }

    /**
     * Change the block's color to green, indicating a correctly typed character, 
     * updating the internal value accordingly
     */
    public void correctAnimation() {
        setFillColor(new Color(0xBDFFBD));
        score = 1;
    }

    /**
     * Change the block's color to red, indicating a wrongly typed character, 
     * updating the internal value accordingly
     */
    public void wrongAnimation() {
        setFillColor(new Color(0xFFBDBD));
        score = -1;
    }

    /**
     * /**
     * Resetting the block to its original state,
     * updating the internal value accordingly
     */
    public void reset() {
        setFillColor(new Color(0xBDBDBD));
        score = 0;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return value;
    }
}
