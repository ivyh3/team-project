package view;

import javax.swing.*;
import java.awt.*;

public class UploadSessionMaterialsView extends View {

    private final JButton cancelButton;
    private final JButton nextButton;

    public UploadSessionMaterialsView() {
        super("uploadSessionMaterials");

        JPanel header = new ViewHeader("Study Materials");

        cancelButton = new JButton("Cancel");
        nextButton = new JButton("Next");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(nextButton);

        this.add(header, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);
    }

    // --- Public API for controller ---

    public void bindCancelAction(Runnable action) {
        cancelButton.addActionListener(e -> action.run());
    }

    public void bindNextAction(Runnable action) {
        nextButton.addActionListener(e -> action.run());
    }
}
