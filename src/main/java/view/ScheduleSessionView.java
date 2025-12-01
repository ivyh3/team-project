package view;

import app.AppBuilder;
import interface_adapter.controller.ScheduleStudySessionController;
import interface_adapter.view_model.ScheduleSessionViewModel;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.ScheduleSessionState;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ScheduleSessionView extends View implements PropertyChangeListener {

    private final ScheduleStudySessionController controller;
    private final ScheduleSessionViewModel viewModel;
    private final DashboardViewModel dashboardViewModel;

    private final JList<String> sessionList;
    private final java.util.List<JButton> dayButtons = new java.util.ArrayList<>();

    private LocalDate weekStart; // Monday of current week
    private LocalDate selectedDate; // currently selected day
    private JPanel header;
    private JLabel headerTitle;

    public ScheduleSessionView(ScheduleStudySessionController controller,
                               ScheduleSessionViewModel viewModel,
                               DashboardViewModel dashboardViewModel) {
        super("scheduleSession");

        this.controller = controller;
        this.viewModel = viewModel;
        this.dashboardViewModel = dashboardViewModel;

        this.viewModel.addPropertyChangeListener(this);

        sessionList = new JList<>();
        sessionList.setCellRenderer(new SessionCellRenderer());

        // initialize date
        selectedDate = LocalDate.now();
        weekStart = selectedDate.minusDays(selectedDate.getDayOfWeek().getValue() - 1); // get Monday of current week

        // header
        headerTitle = new JLabel();
        headerTitle.setFont(new Font("Arial", Font.BOLD, 24));
        header = new JPanel();
        header.add(headerTitle);
        updateHeader(selectedDate);

        // arrow buttons in top row
        JPanel dayRow = new JPanel(new BorderLayout());
        JButton leftArrow = new JButton("<");
        JButton rightArrow = new JButton(">");
        leftArrow.setPreferredSize(new Dimension(50, 40));
        rightArrow.setPreferredSize(new Dimension(50, 40));

        // day buttons in top row
        JPanel daysPanel = new JPanel(new GridLayout(1, 7, 5, 0));
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        // display and format day buttons
        for (int i = 0; i < days.length; i++) {
            String dayName = days[i];
            JButton b = new JButton(dayName) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            // remove default button settings
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
            b.setOpaque(false);
            b.setPreferredSize(new Dimension(50, 40));

            // highlight button and update session list when clicked
            final int index = i;
            b.addActionListener(e -> {
                selectDay(index);
            });

            // add buttons to panel
            dayButtons.add(b);
            daysPanel.add(b);
        }

        // format arrows and days
        dayRow.add(leftArrow, BorderLayout.WEST);
        dayRow.add(daysPanel, BorderLayout.CENTER);
        dayRow.add(rightArrow, BorderLayout.EAST);

        // left arrow implementation (go to Monday of next week)
        leftArrow.addActionListener(e -> {
            weekStart = weekStart.minusDays(7);
            selectedDate = weekStart;
            selectDay(0);
        });

        // right arrow implementation (go to Monday of previous week)
        rightArrow.addActionListener(e -> {
            weekStart = weekStart.plusDays(7);
            selectedDate = weekStart;
            selectDay(0);
        });

        JScrollPane scrollPane = new JScrollPane(sessionList); // create new scroll pane to display scheduled sessions

        // create and add bottom buttons
        JPanel main = new JPanel();
        JButton scheduleButton = new JButton("Schedule Session");
        JButton deleteButton = new JButton("Delete Session");
        JButton returnButton = new JButton("Return");
        main.add(scheduleButton);
        main.add(deleteButton);
        main.add(returnButton);

        scheduleButton.addActionListener(e -> openDateTimeDialog()); // open date selection pop up when schedule button clicked
        returnButton.addActionListener(e -> AppBuilder.viewManagerModel.setView("dashboard")); // go back to dashboard when return button clicked

        // delete scheduled session when delete button clicked
        deleteButton.addActionListener(e -> {
            int selectedIndex = sessionList.getSelectedIndex(); // selected study session
            if (selectedIndex != -1) {

                List<ScheduleSessionState> sessionsForDate = viewModel.getSessionsForDate(selectedDate);

                // 💡 FIX 5: Sessions need to be sorted for the index to match the display list index
                sessionsForDate.sort(Comparator.comparing(ScheduleSessionState::getStartTime));

                ScheduleSessionState sessionStateToDelete = sessionsForDate.get(selectedIndex);
                controller.delete(sessionStateToDelete.getId());

                // 💡 REVERT: Manually call update for immediate visual feedback
                updateSessionListForSelectedDate();
            }
            // display message if user clicks delete button without selecting session to delete
            else {
                JOptionPane.showMessageDialog(this, "Please select a session to delete.");
            }
        });

        // format layout
        add(header, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(dayRow, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        add(main, BorderLayout.SOUTH);

        selectDay(selectedDate.getDayOfWeek().getValue() - 1); // highlight correct day

    }

    // highlight day button when clicked
    private void selectDay(int index) {
        for (int i = 0; i < dayButtons.size(); i++) {
            dayButtons.get(i).setBackground(i == index ? new Color(255, 179, 217) : Color.WHITE);
        }
        selectedDate = weekStart.plusDays(index);
        updateHeader(selectedDate);
        updateSessionListForSelectedDate(); // refresh list for that day
    }

    // update header to selected date
    private void updateHeader(LocalDate date) {
        DateTimeFormatter headerFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        headerTitle.setText(date.format(headerFormatter));
        header.revalidate();
        header.repaint();
    }

    // pop-up to select date and time
    private void openDateTimeDialog() {
        DatePicker datePicker = new DatePicker();
        TimePicker startTimePicker = new TimePicker();
        TimePicker endTimePicker = new TimePicker();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Date:"));
        panel.add(datePicker);
        panel.add(new JLabel("Start Time:"));
        panel.add(startTimePicker);
        panel.add(new JLabel("End Time:"));
        panel.add(endTimePicker);

        int option = JOptionPane.showConfirmDialog(
                this,
                panel,
                "",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        // get selected date and times when user clicks ok
        if (option == JOptionPane.OK_OPTION) {
            LocalDate pickedDate = datePicker.getDate();

            if (startTimePicker.getTime() == null || endTimePicker.getTime() == null) {
                JOptionPane.showMessageDialog(this, "Please select both start and end times.");
                return;
            }

            LocalDateTime start = LocalDateTime.of(pickedDate, startTimePicker.getTime());
            LocalDateTime end = LocalDateTime.of(pickedDate, endTimePicker.getTime());

            if (end.isAfter(start)) {
                String title = JOptionPane.showInputDialog(this, "Enter topic:");
                if (title != null && !title.isEmpty()) {

                    String userId = dashboardViewModel.getState().getUserId();
                    controller.execute(userId, start, end, title);

                    if (pickedDate != null && pickedDate.equals(selectedDate)) {
                        updateSessionListForSelectedDate();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "End time must be after start time.");
            }
        }
    }

    // display new scheduled study sessions
    private void updateSessionListForSelectedDate() {
        List<ScheduleSessionState> sessionsForDate = viewModel.getSessionsForDate(selectedDate); // get all sessions for selected day

        sessionsForDate.sort(Comparator.comparing(ScheduleSessionState::getStartTime));

        // convert each session into String format
        List<String> displayStrings = sessionsForDate.stream()
                .map(s -> String.format("%s\n%s\n%s",
                        s.getTitle(),
                        s.getStartTime().toLocalTime().toString(),
                        s.getEndTime().toLocalTime().toString()))
                .collect(Collectors.toList());

        sessionList.setListData(displayStrings.toArray(new String[0])); // update session list
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("statusMessage".equals(evt.getPropertyName())) {
            String message = (String) evt.getNewValue();
            if (message != null && !message.isEmpty()) {
                JOptionPane.showMessageDialog(this, message);
            }
        }

        if ("sessions".equals(evt.getPropertyName())) {
            updateSessionListForSelectedDate();
        }
    }


    public void refreshSessions() {
        System.out.println("Refreshing sessions...");
        String userId = dashboardViewModel.getState().getUserId();

        if (userId == null || userId.isEmpty()) {
            System.err.println("Cannot refresh sessions: User ID is null or empty.");
            return;
        }
        controller.loadInitialSessions(userId);
    }

    @Override
    public void onViewShown() {
        refreshSessions();
        selectDay(selectedDate.getDayOfWeek().getValue() - 1);
    }
}