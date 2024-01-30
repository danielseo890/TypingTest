package typingtest;

import edu.macalester.graphics.Line;

/**
 * A cursor that is animated on screen and can move alongside the characters being typed
 * @Author Kien Nguyen, Daniel Seo
 */

public class Cursor extends Line{
    private int change = 0; // keeping a constant pace to animate the cursor

    /** 
     * Create a cursor
     * The cursor does not begin animation on created
     */
    public Cursor() {
        super(4, 4, 4, -20);
    }

    /**
     * Move the cursor to immediately before the letterBlock that is given in the input
     */
    public void moveCursor(LetterBlock current) {
        setPosition(current.getPosition());
    }

    /**
     * Create a blinking animation for the cursor that changes every 12 times this method is called
     */
    public void animate() {
        change++;
        setStroked((change/12)%2 == 0);
    }
}
