import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import Model.User;

public class UserRenderer extends JPanel implements ListCellRenderer<User> {
	private JLabel lbIcon = new JLabel();
	private JLabel lbName = new JLabel();
	private JLabel lbIP = new JLabel();

	public UserRenderer() {
		setLayout(new BorderLayout(15, 10));
		setBackground(SystemColor.controlLtHighlight);
		JPanel pnItem = new JPanel(new GridLayout(0, 1));
		pnItem.setBackground(SystemColor.controlLtHighlight);
		pnItem.add(lbName);
		pnItem.add(lbIP);
		add(lbIcon, BorderLayout.WEST);
		add(pnItem, BorderLayout.CENTER);
	}

	public Component getListCellRendererComponent(JList<? extends User> list, User User, int index, boolean isSelected,
			boolean cellHasFocus) {
		lbIcon.setIcon(new ImageIcon(Client.class.getResource("/image/user.png")));
		lbName.setText(User.getName());
		lbName.setForeground(SystemColor.textHighlight);
		lbIP.setText(User.getMyIP());
		lbIP.setForeground(SystemColor.windowBorder);

		// set Opaque to change background color of JLabel
		lbName.setOpaque(true);
		lbIP.setOpaque(true);
		lbIcon.setOpaque(true);

		// when select item
		if (isSelected) {
			lbName.setBackground(new Color(0xbd, 0xc3, 0xc7));
			lbIP.setBackground(new Color(0xbd, 0xc3, 0xc7));
			lbIcon.setBackground(new Color(0xbd, 0xc3, 0xc7));
			setBackground(new Color(0xbd, 0xc3, 0xc7));
		} else {
			lbName.setBackground(list.getBackground());
			lbIP.setBackground(list.getBackground());
			lbIcon.setBackground(list.getBackground());
			setBackground(list.getBackground());
		}
		return this;
	}
}