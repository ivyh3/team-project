package view;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel which contains a title at the left.
 */
public class ViewHeader extends JPanel {
    private static final int PADDING = 20;
    private static final int FONT_SIZE = 20;
//    private static final int HEIGHT = PADDING * 2 + FONT_SIZE + 10;

    public ViewHeader(String header) {
        this.setLayout(new BorderLayout());
//        this.setPreferredSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
        this.setBorder(BorderFactory.createEmptyBorder(0,0, PADDING, 0));

        final JLabel headerLabel = new JLabel(header);
        headerLabel.setFont(new Font(headerLabel.getFont().getFontName(), Font.BOLD, FONT_SIZE));

        this.add(headerLabel, BorderLayout.WEST);
    }
}
