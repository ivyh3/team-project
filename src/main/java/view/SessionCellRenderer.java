package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SessionCellRenderer extends JPanel implements ListCellRenderer<String> {

    private final JLabel titleLabel = new JLabel();
    private final JLabel startLabel = new JLabel();
    private final JLabel endLabel = new JLabel();

    public SessionCellRenderer() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends String> list,
            String value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        // split input into date and times
        String[] parts = value.split("\n");
        String title = parts.length > 0 ? parts[0] : "";
        String startTime = parts.length > 1 ? parts[1] : "";
        String endTime = parts.length > 2 ? parts[2] : "";

        // labels for session
        titleLabel.setText(title);
        startLabel.setText(startTime);
        endLabel.setText(endTime);

        // fonts for session
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        startLabel.setFont(startLabel.getFont().deriveFont(Font.BOLD,18f));
        endLabel.setFont(endLabel.getFont().deriveFont(16f));

        // colours for session
        titleLabel.setForeground(Color.BLACK);
        startLabel.setForeground(Color.DARK_GRAY);
        endLabel.setForeground(Color.DARK_GRAY);

        // position distance between start and end times
        startLabel.setBorder(BorderFactory.createEmptyBorder(-10, 0, -12, 10));
        endLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));

        // panel to store session topic on left side of session
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        leftPanel.setOpaque(false);
        leftPanel.add(titleLabel, BorderLayout.CENTER); // vertically centered

        // panel to store times on right side of session
        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.setOpaque(false);
        rightPanel.add(startLabel);
        rightPanel.add(endLabel);

        // add panels for layout
        removeAll();
        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        // position text in session to be further from border
        setBorder(new EmptyBorder(10, 15, 10, 15));

        // background colours of session
        Color normalColor = new Color(255, 209, 229);
        Color selectedColor = new Color(255, 140, 191);
        setBackground(isSelected ? selectedColor : normalColor); // set background depending on selection status

        // create and add wrapper to add space between sessions
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        wrapper.add(this, BorderLayout.CENTER);

        return wrapper;
    }

    // display session
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        int arc = 30; // round corners of session
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}
