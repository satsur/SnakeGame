import javax.swing.*;

public class GameFrame extends JFrame {

    GameFrame() {
        this.add(new GamePanel());
        // Packs the frame around the content
        // In this case, the only content is the GamePanel (JPanel), so I can just pack the frame around that
        // I don't have to manually set the size of the frame
        this.pack();
        this.setTitle("Snake");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
