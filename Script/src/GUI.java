
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GUI {
    private final JDialog mainDialog;
    private final JComboBox<CraftedItem> craftedItemSelector;

    private boolean started;


    public GUI() {

        mainDialog = new JDialog();
        mainDialog.setTitle("Vlad's Progressive crafter");
        mainDialog.setModal(true);
        mainDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainDialog.getContentPane().add(mainPanel);
        //adds new jpanel and gives it flow layout
        JPanel craftedItemSelectionPanel = new JPanel();
        craftedItemSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //adds a jlabel to title the future combo box
        JLabel craftedItemSelectionLabel = new JLabel("Select leveling method:");
        craftedItemSelectionPanel.add(craftedItemSelectionLabel);
        //creates a combo box under the jlabel
        craftedItemSelector = new JComboBox<>(CraftedItem.values());
        craftedItemSelectionPanel.add(craftedItemSelector);
        //adds the new panel created with combobox and jlabel to the main panel
        mainPanel.add(craftedItemSelectionPanel);
        //ADDS CHECKBOXES TO PANEL
        //Register a listener for the checkboxes.

        //ADDS CHECKBOXES TO PANEL

        //Adds the start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            started = true;
            close();
        });
        mainPanel.add(startButton);
        mainDialog.pack();


    }

    public boolean isStarted() {
        return started;
    }

    public CraftedItem getSelectedCraftedItem() {
        return (CraftedItem) craftedItemSelector.getSelectedItem();
    }

    public void open() {
        mainDialog.setVisible(true);
    }

    public void close() {
        mainDialog.setVisible(false);
        mainDialog.dispose();
    }
}

enum CraftedItem {
    Max_profit_leveling,
    Efficient_leveling_with_profit;
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
