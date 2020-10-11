package mineSweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MineButton extends JButton
{
    private Location location;

    public MineButton(String buttonString, int x, int y, GameBoard gb)
    {
        super(buttonString);
        this.location = new Location(x,y);
        this.addActionListener(e -> handleSquareButtonClick(e, gb));
    }

    public Location getChessLocation() {
        return location;
    }

    public void setChessLocation(Location location) {
        this.location = location;
    }

    private void handleSquareButtonClick(ActionEvent e, GameBoard gb)
    {
        gb.flip(location);
    }
}
