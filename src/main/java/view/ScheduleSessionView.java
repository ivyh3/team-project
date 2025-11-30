package view;

import app.AppBuilder;
import interface_adapter.controller.ScheduleStudySessionController;
import entity.ScheduledSession;
import interface_adapter.view_model.ScheduleSessionViewModel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import frameworks_drivers.firebase.FirebaseScheduledSessionDataAccessObject;
import entity.ScheduledSessionFactory;
import use_case.schedule_study_session.ScheduleStudySessionDataAccessInterface;
import interface_adapter.presenter.ScheduleStudySessionPresenter;
import use_case.schedule_study_session.ScheduleStudySessionInteractor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleSessionView extends View {

    private final ScheduleStudySessionController controller;
    private final ScheduleSessionViewModel viewModel;
    private final ScheduleStudySessionDataAccessInterface dataAccess;

    private final JList<String> sessionList;
    private final java.util.List<JButton> dayButtons = new java.util.ArrayList<>();

    private LocalDate weekStart; // Monday of current week
    private LocalDate selectedDate; // currently selected day
    private JPanel header;
    private JLabel headerTitle;

    public ScheduleSessionView() {
        super("scheduleSession");

        this.viewModel = new ScheduleSessionViewModel();
        this.dataAccess = new FirebaseScheduledSessionDataAccessObject(new ScheduledSessionFactory(), "testUser");

        ScheduleStudySessionPresenter presenter = new ScheduleStudySessionPresenter(viewModel);
        ScheduleStudySessionInteractor interactor =
                new ScheduleStudySessionInteractor(dataAccess, presenter);

        this.controller = new ScheduleStudySessionController(interactor);

        sessionList = new JList<>();
        sessionList.setCellRenderer(new SessionCellRenderer());

        // initialize date
        selectedDate = LocalDate.now();
        weekStart = selectedDate.minusDays(selectedDate.getDayOfWeek().getValue() - 1); // get Monday of current week

        // header
        headerTitle = new JLabel();
        headerTitle.setFont(new Font("Arial", Font.BOLD, 18));
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

                List<ScheduledSession> sessionsForDate = viewModel.getSessionsForDate(selectedDate); // get all study sessions on selected date
                ScheduledSession sessionToDelete = sessionsForDate.get(selectedIndex); // get session user selected

                // remove through the view model
                viewModel.removeScheduledSession(sessionToDelete);
                viewModel.setStatusMessage("Session deleted successfully!");

                updateSessionListForSelectedDate(); // update session list
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

        selectDay(selectedDate.getDayOfWeek().getValue() - 1); // highlight Monday button when arrows clicked
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
            selectedDate = pickedDate;
            LocalDateTime start = LocalDateTime.of(pickedDate, startTimePicker.getTime());
            LocalDateTime end = LocalDateTime.of(pickedDate, endTimePicker.getTime());

            // prompt user to enter session topic if end time is after start time
            if (end.isAfter(start)) {
                String title = JOptionPane.showInputDialog(this, "Enter topic:");
                if (title != null && !title.isEmpty()) {
                    String sessionId = java.util.UUID.randomUUID().toString();
                    controller.execute("testUser", start, end, title);

                    updateSessionListForSelectedDate(); // refresh list for selected date
                }
            }
        }

    }

    // display new scheduled study sessions
    private void updateSessionListForSelectedDate() {
        List<ScheduledSession> sessionsForDate = viewModel.getSessionsForDate(selectedDate); // get all sessions for selected day

        // convert each session into String format
        List<String> displayStrings = sessionsForDate.stream()
                .map(s -> String.format("%s\n%s\n%s",
                        s.getTitle(),
                        s.getStartTime().toLocalTime().toString(),
                        s.getEndTime().toLocalTime().toString()))
                .collect(Collectors.toList());

        sessionList.setListData(displayStrings.toArray(new String[0])); // update session list
    }

}
